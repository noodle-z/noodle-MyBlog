package com.zzzwb.myblog.config.interceptor;

import com.zzzwb.myblog.model.AccessVerificationModel;
import com.zzzwb.myblog.utils.AccessVerificationUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *	用户权限校验拦截器
 *
 * @author zeng wenbin
 * @date Created in 2019/8/5
 */
@Configuration
@Component
public class UserInterceptor extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		/**
		 * 添加权限判断拦截器
		 */
		registry.addInterceptor(new HandlerInterceptor() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
				//获取uri
				String uri = request.getRequestURI();
				//获取请求方式
				String method = request.getMethod();

				AccessVerificationModel model = AccessVerificationUtil.isPublicUri(uri, method);
				//在权限管控列表中则进行权限校验
				if (model.isAccess()){
					return AccessVerificationUtil.hasAccess(model, request);
				}
				//不在权限管控列表则放过
				return true;
			}
		}).addPathPatterns("/**").order(1);
	}
}