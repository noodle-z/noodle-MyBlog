package com.zzzwb.myblog.config.interceptor;

import com.zzzwb.myblog.bean.SearchBean;
import com.zzzwb.myblog.constant.Action;
import com.zzzwb.myblog.domain.Resource;
import com.zzzwb.myblog.domain.RoleResource;
import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.domain.UserRole;
import com.zzzwb.myblog.exception.AccessException;
import com.zzzwb.myblog.exception.NoLoginException;
import com.zzzwb.myblog.function.OnlineUserFunction;
import com.zzzwb.myblog.service.ResourceService;
import com.zzzwb.myblog.service.RoleResouceService;
import com.zzzwb.myblog.service.UserRoleService;
import com.zzzwb.myblog.utils.RedisUtil;
import com.zzzwb.myblog.utils.SpringBeanCatchUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *	用户功能拦截器配置
 *
 * @author zeng wenbin
 * @date Created in 2019/8/5
 */
@Configuration
@Component
public class UserInterceptor extends WebMvcConfigurerAdapter {

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private RoleResouceService roleResouceService;

	@Autowired
	private ResourceService resourceService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		/**
		 * 添加权限判断拦截器
		 */
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
				//获取uri
				String uri = request.getRequestURI().replaceAll("/+", "/");
				String[] uriSplit = uri.split("/");
				if (uriSplit.length < 3){
					return false;
				}
				String item = uriSplit[2];
				List<Resource> accessResources = (List<Resource>) RedisUtil.hget("accessResources",item);
				if (accessResources == null || accessResources.size() == 0){
					return false;
				}
				//获取请求方式
				String method = request.getMethod();
				if ("OPTIONS".equals(method)){
					return true;
				}
				StringBuffer action = new StringBuffer();
				switch (method){
					case "GET":
						action.append("SELECT");
						break;
					case "PUT":
						action.append("UPDATE");
						break;
					case "POST":
						action.append("ADD");
						break;

					default:
						action.append("DELETE");
						break;
				}
				accessResources = accessResources.stream().filter(resource -> "OTHER".equals(resource.getAction()) || resource.getAction().equals(action.toString())).collect(Collectors.toList());
				//请求的资源
				StringBuffer pathBuffer = new StringBuffer();
				StringBuffer actionBuffer = new StringBuffer();

				//判断是否在权限管控列表中
				boolean accessCheck = accessResources.stream().anyMatch(resource -> {
					Boolean check = resource.getPath().equals(uri);
					if (!check) {
						String[] uriArr = uri.split("/");
						String[] pathArr = resource.getPath().split("/");
						if (uriArr.length == pathArr.length) {
							Boolean flag = true;
							for (int i = 0; i < uriArr.length; i++) {
								if (!uriArr[i].equals(pathArr[i])) {
									boolean start = pathArr[i].startsWith("{");
									boolean end = pathArr[i].endsWith("}");
									if (!start || !end) {
										flag = false;
									}
								}
							}
							check = flag;
						}
					}
					if (check){
						pathBuffer.append(resource.getPath());
						actionBuffer.append(resource.getAction());
					}
					return check;
				});
				//在权限管控列表中进行权限校验
				if (accessCheck){
					//获取角色
					String token = request.getHeader("token");
					Optional<User> userOpt = OnlineUserFunction.getUserByToken(token);
					if (!userOpt.isPresent()){
						throw new NoLoginException();
					}
					//更新用户token有效时间
					OnlineUserFunction.updateOnlineUserTime(token);
					User user = userOpt.get();
					SearchBean searchBean = new SearchBean("userId", user.getId());
					List<UserRole> userRoles = userRoleService.queryBySearch(searchBean);
					boolean hasAccesss = userRoles.stream().anyMatch(userRole -> {
						List<RoleResource> roleResources = roleResouceService.queryBySearch(new SearchBean("roleId",
								userRole.getRoleId()));
						List<Integer> collect =
								roleResources.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
						//todo 此处是否可以使用数据库distinct
						List<Resource> resources = resourceService.queryBySearch(new SearchBean("id-in", collect));
						for (Resource resource : resources) {
							if (resource.getPath().equals(pathBuffer.toString()) && resource.getAction().equals(actionBuffer.toString())) {
								return true;
							}
						}
						return false;
					});
					//获取对应资源
					if (hasAccesss){
						return true;
					}else {
						throw new AccessException(pathBuffer.toString(), actionBuffer.toString());
					}
				}
				//不在权限管控列表则放过
				return true;
			}
		}).addPathPatterns("/**").order(1);
	}
}