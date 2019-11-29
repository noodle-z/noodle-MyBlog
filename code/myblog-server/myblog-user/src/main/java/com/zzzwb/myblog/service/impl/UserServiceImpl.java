package com.zzzwb.myblog.service.impl;

import com.zzzwb.myblog.annotation.DistributedLock;
import com.zzzwb.myblog.domain.User;
import com.zzzwb.myblog.mapper.UserMapper;
import com.zzzwb.myblog.service.UserService;
import com.zzzwb.myblog.service.impl.base.BaseServiceImpl;
import com.zzzwb.myblog.tkmybatis.BaseMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 用户事务层实现
 *
 * @author zeng wenbin
 * @date Created in 2019/7/28
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
	/**
	 * 用户持久化层
	 */
	private UserMapper userMapper;

	public UserServiceImpl(UserMapper userMapper) {
		super(userMapper);
		this.userMapper = userMapper;
	}

	@Override
	public List<User> findByUserNameOrPhoneOrEmail(String condition) {
		Example example = this.getExample();
		example.createCriteria().orEqualTo("username", condition).orEqualTo("phone", condition).orEqualTo("email", condition);
		return this.userMapper.selectByExample(example);
	}
}
