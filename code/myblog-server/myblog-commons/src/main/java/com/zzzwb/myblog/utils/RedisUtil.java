package com.zzzwb.myblog.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * redis操作工具类
 *
 * @author zeng wenbin
 * @date Created in 2019/11/15
 */
public class RedisUtil {

	private static RedisTemplate<String, Object> redisTemplate;

	private static RedisLockRegistry redisLockRegistry;

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	public static final String UNLOCK_LUA;

	static {
		StringBuilder sb = new StringBuilder();
		sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
		sb.append("then ");
		sb.append("    return redis.call(\"del\",KEYS[1]) ");
		sb.append("else ");
		sb.append("    return 0 ");
		sb.append("end ");
		UNLOCK_LUA = sb.toString();
		getRedisTemplate();
		getRedisLockRegistry();
	}

	public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 获取redisTemplate
	 */
	private static void getRedisTemplate(){
		if (redisTemplate == null){
			redisTemplate = (RedisTemplate<String, Object>) SpringBeanCatchUtil.getBean("redisTemplate");
		}
	}

	/**
	 * 获取RedisLockRegistry
	 */
	private static void getRedisLockRegistry(){
		if (redisLockRegistry == null){
			redisLockRegistry = (RedisLockRegistry) SpringBeanCatchUtil.getBean("redisLockRegistry");
		}
	}

	/**
	 * 分布式锁加锁
	 * @param key 键
	 * @param time 加锁时间
	 * @param unit 时间单位
	 * @return 锁
	 */
	public static Lock lock(String key, Long time, TimeUnit unit) {
		Lock lock = redisLockRegistry.obtain(key);
		if (time < 0 || unit == null){
			lock.lock();
		} else {
			try {
				lock.tryLock(time, unit);
			} catch (InterruptedException e) {
				logger.error("分布式加锁线程中断", e);
				lock.unlock();
			}
		}
		return lock;
	}

	/**
	 * 释放锁
	 * @param lock 锁
	 */
	public static void releaseLock(Lock lock) {
		lock.unlock();
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key 键
	 * @return true or false
	 */
	 public static Boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 根据key查询
	 *
	 * @param key 键
	 * @return 对象
	 */
	 public static Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 单项删除
	 *
	 * @param key 键
	 */
	 public static void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 批量删除
	 *
	 * @param keys 键集合
	 */
	 public static void deleteBatch(Collection<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 设置过期时间，多久后过期
	 *
	 * @param key     键
	 * @param timeout 时间值
	 * @param unit    时间单位
	 * @return 设置结果
	 */
	 public static Boolean expire(String key, long timeout, TimeUnit unit) {
		return redisTemplate.expire(key, timeout, unit);
	}

	/**
	 * 设置过期时间，指定时间过期
	 *
	 * @param key  键
	 * @param date 日期
	 * @return 设置结果
	 */
	 public static Boolean expireAt(String key, Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * 返回当前key所对应的剩余过期时间
	 *
	 * @param key 键
	 * @return 剩下多久过期，单位毫秒
	 */
	 public static Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	/**
	 * 设置键值对
	 *
	 * @param key   键
	 * @param value 值
	 */
	 public static void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 设置键值对并添加过期时间
	 * @param key 键
	 * @param value 值
	 * @param timeout 过期时间
	 * @param unit 时间单位
	 */
	 public static void setAndExpire(String key, String value, long timeout, TimeUnit unit){
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	/**
	 * 批量获取值
	 *
	 * @param keys 键集合
	 * @return 值集合
	 */
	 public static List<Object> multiGet(Collection<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 自增或自减
	 *
	 * @param key 键
	 * @param increment 增加多少或减少多少
	 * @return 自增或自减后的值
	 */
	 public static Long increment(String key, long increment) {
		return redisTemplate.opsForValue().increment(key, increment);
	}

	/**
	 * 如果key不存在则set
	 * 		否则不set
	 * @param key 键
	 * @param value 值
	 * @return 设置结果
	 */
	 public static Boolean setIfAbsent(String key, String value){
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	/**
	 * HashSet
	 * @param key 键
	 * @param map 对应多个键值
	 */
	public static void hmset(String key, Map map){
		try {
			redisTemplate.opsForHash().putAll(key, map);
		} catch (Exception e) {
			logger.error("hash表存储失败，key:" + key, e);
		}
	}

	/**
	 * 获取hashKey对应的所有键值
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public static Map<Object,Object> hmget(String key){
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 */
	public static void hset(String key,String item,Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
		} catch (Exception e) {
			logger.error("hash表存储失败，key:" + key + ",item:" + item, e);
		}
	}

	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public static Object hget(String key,String item){
		return redisTemplate.opsForHash().get(key, item);
	}
}
