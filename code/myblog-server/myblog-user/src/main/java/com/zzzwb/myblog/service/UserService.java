package com.zzzwb.myblog.service;

import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.service.base.BaseService;

import java.util.List;

/**
 * 用户service
 *
 * @author zeng wenbin
 * @date Created in 2019/7/28
 */
public interface UserService extends BaseService<User> {

	/**
	 * 根据账号，电话号码或邮箱查询
	 * @param condition 账号，电话号码或邮箱
	 * @return 用户
	 */
	List<User> findByUserNameOrPhoneOrEmail(String condition);

}
