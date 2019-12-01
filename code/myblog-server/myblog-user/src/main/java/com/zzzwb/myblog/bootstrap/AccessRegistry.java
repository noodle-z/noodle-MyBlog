package com.zzzwb.myblog.bootstrap;

import com.zzzwb.myblog.annotation.Access;
import com.zzzwb.myblog.constant.RedisConstant;
import com.zzzwb.myblog.domain.Resource;
import com.zzzwb.myblog.domain.RoleResource;
import com.zzzwb.myblog.service.ResourceService;
import com.zzzwb.myblog.service.RoleResouceService;
import com.zzzwb.myblog.utils.RedisUtil;
import com.zzzwb.myblog.utils.SpringBeanCatchUtil;
import com.zzzwb.myblog.web.rest.BaseResource;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限仓库
 * 项目启动时:1.更新所有权限接口
 * 			 2.将所有角色和权限的关系加载入内存
 *
 * @author zeng wenbin
 * @date Created in 2019/7/28
 */
@Component
@Order(999)
public class AccessRegistry implements ApplicationRunner {

	private List<BaseResource> resources;
	private ResourceService resourceService;
	private RoleResouceService roleResouceService;
	private static final Logger logger = LoggerFactory.getLogger(AccessRegistry.class);
	/**
	 * 是否进行接口资源重新加载
	 */
	@Value("${myblog.access.load:true}")
	private Boolean accessLoad;


	public AccessRegistry( ResourceService resourceService,
						   RoleResouceService roleResouceService,
						   @Autowired(required = false) List<BaseResource> resources) {
		this.resourceService = resourceService;
		this.roleResouceService = roleResouceService;
		this.resources = resources;

	}

	@Override
	public void run(ApplicationArguments args) {
		if (accessLoad) {
			if (resources == null || resources.size() == 0){
				return;
			}
			logger.info("-------------------- 权限资源更新开始 ----------------------");
			List<Resource> exist = this.resourceService.findAll();
			List<Resource> scan = new ArrayList<>();
			resources.stream().forEach(resource -> {
				Class<? extends BaseResource> resourceClass = resource.getClass();

				//获取类名
				String className = resourceClass.getName();
				int index = className.lastIndexOf(".");
				className = className.substring(index + 1);

				//获取uri前缀
				boolean requestMappingPresent = resourceClass.isAnnotationPresent(RequestMapping.class);
				String prePath = "";
				if (requestMappingPresent) {
					prePath = resourceClass.getAnnotation(RequestMapping.class).value()[0];
				}

				//获取类描述
				boolean apiPresent = resourceClass.isAnnotationPresent(Api.class);
				String classDescription = "";
				if (apiPresent) {
					classDescription = resourceClass.getAnnotation(Api.class).tags()[0];
				}

				//方法层面
				Method[] methods = resourceClass.getMethods();
				for (Method method : methods) {
					boolean accessPresent = method.isAnnotationPresent(Access.class);
					if (!accessPresent) {
						continue;
					}
					Resource resourceEntity = new Resource();
					resourceEntity.setClassName(className);
					resourceEntity.setClassDescription(classDescription);
					resourceEntity.setCreatedBy(0);
					resourceEntity.setLastModifiedBy(0);
					Access access = method.getAnnotation(Access.class);
					//设置权限
					resourceEntity.setAction(access.value().name());

					//设置接口描述
					resourceEntity.setDescription(access.description());

					//设置path
					boolean getMapperPresent = method.isAnnotationPresent(GetMapping.class);
					if (getMapperPresent) {
						if (method.getAnnotation(GetMapping.class).value().length == 0) {
							resourceEntity.setPath(prePath + "");
						} else {
							resourceEntity.setPath(prePath + method.getAnnotation(GetMapping.class).value()[0]);
						}
					}
					boolean deleteMappingPresent = method.isAnnotationPresent(DeleteMapping.class);
					if (deleteMappingPresent) {
						if (method.getAnnotation(DeleteMapping.class).value().length == 0) {
							resourceEntity.setPath(prePath + "");
						} else {
							resourceEntity.setPath(prePath + method.getAnnotation(DeleteMapping.class).value()[0]);
						}
					}
					boolean putMapperPresent = method.isAnnotationPresent(PutMapping.class);
					if (putMapperPresent) {
						if (method.getAnnotation(PutMapping.class).value().length == 0) {
							resourceEntity.setPath(prePath + "");
						} else {
							resourceEntity.setPath(prePath + method.getAnnotation(PutMapping.class).value()[0]);
						}
					}
					boolean postMapperPresent = method.isAnnotationPresent(PostMapping.class);
					if (postMapperPresent) {
						if (method.getAnnotation(PostMapping.class).value().length == 0) {
							resourceEntity.setPath(prePath + "");
						} else {
							resourceEntity.setPath(prePath + method.getAnnotation(PostMapping.class).value()[0]);
						}
					}
					//添加到集合
					scan.add(resourceEntity);
				}
			});
			//处理数据
			updateResource(exist, scan);
			//先redis中存储权限资源数据
			saveDatasToRedis();
			logger.info("-------------------- 权限资源更新结束 ----------------------");
		}
	}

	/**
	 * 将所有接口资源数据与角色对应的接口资源数据存储进数据库
	 */
	private void saveDatasToRedis() {
		List<Resource> resources = resourceService.findAll();
		//根据path的关键字进行接口资源的分组存储
		Map<String, List<Resource>> resourceMap =
				resources.stream().collect(Collectors.groupingBy(resource -> resource.getPath().split("/")[2]));
		RedisUtil.hmset(RedisConstant.ACCESS_RESOURCES, resourceMap);

		//根据角色进行接口资源的分组存储
		List<RoleResource> roleResources = this.roleResouceService.findAll();
		Map<String, List<Resource>> roleResourceMap = new HashMap<>(64);
		roleResources.forEach(roleResource -> {
			roleResourceMap.putIfAbsent(roleResource.getRoleId().toString(), new ArrayList<>(128));
			List<Resource> resourcesOfRole = roleResourceMap.get(roleResource.getRoleId().toString());
			resources.forEach(resource -> {
				if (resource.getId().equals(roleResource.getResourceId())) {
					resourcesOfRole.add(resource);
				}
			});
		});
		RedisUtil.hmset(RedisConstant.RESOURCE_OF_ROLE, roleResourceMap);
	}

	/**
	 * 更新数据，保持数据的一致性
	 * 	有新的接口要添加数据
	 * 	存在被删除的接口要删除数据
	 *
	 * @param exist 数据库中查到的resource
	 * @param scan	扫描生成的resource
	 */
	private void updateResource(List<Resource> exist, List<Resource> scan) {
		int i = 0;
		for (Resource resource : scan){
			boolean have =
					exist.stream().anyMatch(data -> {
						Boolean b = false;
						try {
							b = data.getPath().equals(resource.getPath()) && data.getAction().equals(resource.getAction());
						}catch (Exception e){
							e.printStackTrace();
							logger.error("出错的数据scan:"+resource.toString());
							logger.error("出错的数据exist:"+data.toString());
						}
						return b;
					});
			if (!have){
				this.resourceService.create(resource);
				RoleResource roleResource = new RoleResource();
				roleResource.setRoleId(1);
				roleResource.setResourceId(resource.getId());
				roleResource.setCreatedBy(0);
				roleResource.setLastModifiedBy(0);
				this.roleResouceService.create(roleResource);
				logger.info("新增了接口：path" + resource.getPath() + "    action:" + resource.getAction());
				i++;
			}
		}
		logger.info("-------------------- 新插入"+i+"条数据 ---------------------");
		int y = 0;
		for (Resource resource : exist){
			boolean have =
					scan.stream().anyMatch(data -> resource.getAction().equals(data.getAction()) && resource.getPath().equals(data.getPath()));
			if (!have){
				logger.info("删除接口：path" + resource.getPath() + "    action:" + resource.getAction());
				this.resourceService.deleteOne(resource.getId());
				y++;
			}
		}
		logger.info("-------------------- 共删除"+y+"条数据 ---------------------");
	}

}
