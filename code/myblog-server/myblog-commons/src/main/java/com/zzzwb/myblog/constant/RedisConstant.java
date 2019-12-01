package com.zzzwb.myblog.constant;

/**
 * redis操作中键的前缀
 *
 * @author zeng wenbin
 * @date Created in 2019/11/28
 */
public class RedisConstant {
	/**
	 * 分布式锁前缀
	 */
	public final static String LOCK_PREFIX = "redis-lock";

	/**
	 * 在线用户前缀
	 */
	public final static String ONLINE_USER = "onlineUser-";

	/**
	 * 角色接口资源map
	 */
	public final static String RESOURCE_OF_ROLE = "resourceOfRole";

	/**
	 * 权限资源
	 */
	public final static String ACCESS_RESOURCES = "accessResources";
}
