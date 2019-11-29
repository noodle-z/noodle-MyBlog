package com.zzzwb.myblog.config.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器，解决跨域问题
 *
 * @author zeng wenbin
 * @date Created in 2019/11/15
 */
@WebFilter(urlPatterns = "/*", filterName = "crossFilter")
public class CrossFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		//设置允许访问的地址为任何地址
		// 此处不用*号，否则跨域时不支持cookie的传递（不一定使用cookie，但此处保持支持）
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String origin = request.getHeader("origin");
		response.addHeader("Access-Control-Allow-Origin", origin);

		//禁止缓存
		response.setDateHeader("Expires", 0);
		response.addHeader( "Cache-Control", "no-cache" );

		//使用restful风格，支持以下几种方式就够了
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

		//允许任何请求头
		String headers = request.getHeader("Access-Control-Request-Headers");
		if (StringUtils.isNotEmpty(headers)){
			response.addHeader("Access-Control-Allow-Headers", headers);
		}

		//开启coockie
		response.addHeader("Access-Control-Allow-Credentials", "true");

		//预检请求(OPTIONS)设置半小时的缓存
		response.addHeader("Access-Control-Max-Age", "1800");

		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
