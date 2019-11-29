package com.zzzwb.myblog.config.aspect;

import com.zzzwb.myblog.annotation.DistributedLock;
import com.zzzwb.myblog.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁切面
 * 		用于将分布式锁的使用做成基于注解的形式
 *
 * @author zeng wenbin
 * @date Created in 2019/11/28
 */
@Slf4j
@Aspect
@Component
public class DistributedLockAspect {

	@Pointcut("@annotation(com.zzzwb.myblog.annotation.DistributedLock)")
	public void pointCut() {
	}

	@Around("pointCut()&& @annotation(distributedLock)")
	public Object around(ProceedingJoinPoint point, DistributedLock distributedLock) throws Throwable {
		Lock lock = RedisUtil.lock(distributedLock.key(), distributedLock.lockExpire(), distributedLock.unit());
		Object proceed = point.proceed();
		RedisUtil.releaseLock(lock);
		return proceed;
	}
}
