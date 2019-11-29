package com.zzzwb.myblog.cache;

import com.zzzwb.myblog.utils.SpringBeanCatchUtil;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Mybatis二级缓存redis
 *
 * @author zeng wenbin
 * @date Created in 2019/7/27
 */
public class MybatisRedisCache implements Cache {

	private static final Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);
	/**
	 * 读写锁
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	/**
	 * 缓存实例id
	 */
	private final String id;
	/**
	 * spring提供的redis操作模板
	 */
	private RedisTemplate<String,Object> redisTemplate;

	public MybatisRedisCache(String id) throws InterruptedException {
		if (id == null) {
			throw new IllegalArgumentException("缓存实例必须要有id");
		}
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * 推送数据
	 *
	 * @param key key
	 * @param value value
	 */
	@Override
	public void putObject(Object key, Object value) {
		try {
			redisTemplate = this.getRedisTemplate();
			redisTemplate.boundHashOps(getId()).put(key.toString(), value);
			logger.debug("向redis推送了一条数据，key:"+ key +",value:" + value);
		} catch (Throwable t) {
			logger.error("向redis 推送数据失败", t);
		}
	}

	/**
	 * 查询数据
	 *
	 * @param key key
	 * @return 查询结果
	 */
	@Override
	public Object getObject(Object key) {
		try {
			redisTemplate = this.getRedisTemplate();
			logger.debug("向redis查询数据,key:" + key);
			return redisTemplate.boundHashOps(getId()).get(key.toString());
		} catch (Throwable t) {
			logger.error("redis查询数据失败,key:" + key, t);
			return null;
		}
	}

	/**
	 * 删除数据
	 *
	 * @param key key
	 * @return 删除结果
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object removeObject(Object key) {
		try {
			redisTemplate = this.getRedisTemplate();
			redisTemplate.boundHashOps(getId()).delete(key.toString());
			logger.debug("向redis删除了一条数据,key:" + key);
		} catch (Throwable t) {
			logger.error("redis移除数据失败,key" + key, t);
		}
		return null;
	}

	/**
	 * 清空全部缓存
	 */
	@Override
	public void clear() {
		redisTemplate = this.getRedisTemplate();
		redisTemplate.delete(getId());
		logger.debug("清空所有缓存");
	}

	/**
	 * 获取数量
	 *
	 * @return 数量
	 */
	@Override
	public int getSize() {
		Long size = redisTemplate.boundHashOps(getId()).size();
		return size == null ? 0 : size.intValue();
	}

	@Override
	public ReadWriteLock getReadWriteLock() {
		return readWriteLock;
	}

	/**
	 * 获取redisTemplate
	 * @return
	 */
	private RedisTemplate getRedisTemplate(){
		if (redisTemplate == null){
			redisTemplate = (RedisTemplate<String, Object>) SpringBeanCatchUtil.getBean("redisTemplate");
		}
		return redisTemplate;
	}
}
