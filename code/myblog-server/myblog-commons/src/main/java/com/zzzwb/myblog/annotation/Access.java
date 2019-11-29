package com.zzzwb.myblog.annotation;


import com.zzzwb.myblog.constant.Action;

import java.lang.annotation.*;

/**
 * 访问权限控制注解
 * 		方法级别的注解
 * 		在resource层使用
 *
 * @author zeng wenbin
 * @date Created in 2019/7/25
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Access {
	/**
	 * 访问类别，默认查询
	 */
	Action value() default Action.SELECT;

	/**
	 * 描述
	 */
	String description();
}
