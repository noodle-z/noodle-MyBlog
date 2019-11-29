package com.zzzwb.myblog.annotation;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 *	执行分布式锁的注解
 *
 * @author zeng wenbin
 * @date Created in 2019/11/15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
	/**
	 * 锁的键
	 */
	String key();

	/**
	 * 锁过期时间
	 */
	long lockExpire() default -1L;

	/**
	 * 时间单位
	 */
	TimeUnit unit() default TimeUnit.SECONDS;
}
