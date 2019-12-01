package com.zzzwb.myblog.utils;

import com.zzzwb.myblog.constant.RedisConstant;
import com.zzzwb.myblog.domain.Resource;
import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.exception.AccessException;
import com.zzzwb.myblog.exception.NoLoginException;
import com.zzzwb.myblog.function.OnlineUserFunction;
import com.zzzwb.myblog.model.AccessVerificationModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限校验工具类，有两个方法
 * 1.校验所访问的uri是否是权限资源
 * 2.校验用户是否有权限访问指定权限接口权限接口
 *
 * @author zeng wenbin
 * @date Created in 2019/12/1
 */
public class AccessVerificationUtil {

	/**
	 * 校验所访问的uri是否是权限资源
	 *
	 * @param uri uri
	 * @param method 请求方式
	 * @return 校验结果 AccessVerificationModel对象
	 */
	public static AccessVerificationModel isPublicUri(String uri, String method) {
		//使用uri的功能字符串查询对应的资源  如user/role
		String[] uriSplit = uri.split("/");
		if (uriSplit.length < 3) {
			//系统提供的接口此length都会大于3
			throw new AccessException(uri, method);
		}
		String item = uriSplit[2];
		List<Resource> accessResources = (List<Resource>) RedisUtil.hget("accessResources", item);

		if (accessResources == null || accessResources.size() == 0) {
			throw new AccessException(uri, method);
		}
		if ("OPTIONS".equals(method)) {
			return new AccessVerificationModel(true, "", "");
		}
		StringBuffer action = new StringBuffer();
		switch (method) {
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
		accessResources =
				accessResources.stream().filter(resource -> "OTHER".equals(resource.getAction()) || resource.getAction().equals(action.toString())).collect(Collectors.toList());
		//请求的资源
		StringBuffer pathBuffer = new StringBuffer();
		StringBuffer actionBuffer = new StringBuffer();

		//判断是否在权限管控列表中
		boolean isAccess = accessResources.stream().anyMatch(resource -> {
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
			if (check) {
				pathBuffer.append(resource.getPath());
				actionBuffer.append(resource.getAction());
			}
			return check;
		});
		return new AccessVerificationModel(isAccess, isAccess ? pathBuffer.toString() : "", isAccess ?
				actionBuffer.toString() : "");
	}

	public static boolean hasAccess(AccessVerificationModel model, HttpServletRequest request){
		//获取角色
		String token = request.getHeader("token");
		Optional<User> userOpt = OnlineUserFunction.getUserByToken(token);
		if (!userOpt.isPresent()){
			throw new NoLoginException();
		}
		//更新用户token有效时间
		OnlineUserFunction.updateOnlineUserTime(token);
		User user = userOpt.get();
		String[] roles = user.getRoles().split(",");

		//判断是否有权限
		boolean hasAccess = Arrays.stream(roles).anyMatch(role -> {
			List<Resource> resources = (List<Resource>) RedisUtil.hget(RedisConstant.RESOURCE_OF_ROLE, role);
			for (Resource resource : resources) {
				if (resource.getPath().equals(model.getUri()) && resource.getAction().equals(model.getAction())) {
					return true;
				}
			}
			return false;
		});

		//有权限则返回true  否则  抛权限异常
		if (hasAccess){
			return true;
		}else {
			throw new AccessException(model.getUri(), request.getMethod());
		}
	}
}
