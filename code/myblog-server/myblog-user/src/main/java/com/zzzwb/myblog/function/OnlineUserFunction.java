package com.zzzwb.myblog.function;

import com.alibaba.fastjson.JSONObject;
import com.zzzwb.myblog.constant.RedisConstant;
import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 在线用户功能类
 * 		存储在线用户信息
 * 		获取在线用户
 * 		更新在新用户
 *
 * @author zeng wenbin
 * @date Created in 2019/8/10
 */
@Component
public class OnlineUserFunction {

	/**
	 * 用户信息存储时间：默认30分钟
	 */
	public static Integer aliveTime;

	@Value("${myblog.user.aliveTime:30}")
	public void setAliveTime(Integer aliveTime){
		OnlineUserFunction.aliveTime = aliveTime;
	}

	/**
	 * 通过token获取在线用户
	 * @param token	用户token
	 * @return	在线用户
	 */
	public static Optional<User> getUserByToken(String token){
		if (StringUtils.isEmpty(token)){
			return Optional.empty();
		}
		String userString = (String) RedisUtil.get(RedisConstant.ONLINE_USER + token);
		if (StringUtils.isNotEmpty(userString)){
			return Optional.of(JSONObject.parseObject(userString, User.class));
		}
		return Optional.empty();
	}

	/**
	 * 根据token删除在线用户
	 * @param token token
	 */
	public static void removeUserByToken(String token){
		RedisUtil.delete(RedisConstant.ONLINE_USER + token);
	}

	/**
	 *  添加在线用户
	 * @param token	token
	 * @param user	用户
	 */
	public static void addOnlineUser(String token, User user){
		String userString = JSONObject.toJSONString(user);
		RedisUtil.setAndExpire(RedisConstant.ONLINE_USER + token, userString, aliveTime, TimeUnit.MINUTES);
	}

	/**
	 * 更新在线用户时间
	 * @param token	token
	 */
	public static void updateOnlineUserTime(String token){
		RedisUtil.expire(RedisConstant.ONLINE_USER + token, aliveTime, TimeUnit.MINUTES);
	}
}
