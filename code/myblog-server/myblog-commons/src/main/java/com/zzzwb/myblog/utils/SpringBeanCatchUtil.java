package com.zzzwb.myblog.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * springBean获取工具
 * 		通过ApplicationContext.getBean(class)获取
 *
 * @author zeng wenbin
 * @date Created in 2019/7/27
 */
@Component
@Order(1)
public class SpringBeanCatchUtil implements ApplicationContextAware, DisposableBean {
	/**
	 * spring应用上下文
	 */
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		SpringBeanCatchUtil.applicationContext = applicationContext;
	}

	/**
	 * 从静态变量 applicationContext 中获取 Bean，自动转型成所赋值对象的类型
	 *
	 * @param clazz  要获取的springbean实例类型
	 * @param <T>	springBean实例泛型
	 * @return	springBean实例
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 从静态变量 applicationContext 中获取 Bean
	 *
	 * @param name bean名称
	 * @return 对象
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	/**
	 * 实现 DisposableBean 接口，在 Context 关闭时清理静态变量
	 *
	 */
	@Override
	public void destroy() {
		applicationContext = null;
	}
}
