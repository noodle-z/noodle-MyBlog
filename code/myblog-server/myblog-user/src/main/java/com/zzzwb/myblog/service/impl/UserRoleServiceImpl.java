package com.zzzwb.myblog.service.impl;

import com.zzzwb.myblog.domain.UserRole;
import com.zzzwb.myblog.mapper.UserRoleMapper;
import com.zzzwb.myblog.service.UserRoleService;
import com.zzzwb.myblog.service.impl.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户角色事务层实现
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {

	private UserRoleMapper userRoleMapper;

	public UserRoleServiceImpl(UserRoleMapper userRoleMapper){
		super(userRoleMapper);
		this.userRoleMapper = userRoleMapper;
	}
}
