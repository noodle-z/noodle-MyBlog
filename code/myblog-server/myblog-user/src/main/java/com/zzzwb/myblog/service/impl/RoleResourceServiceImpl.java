package com.zzzwb.myblog.service.impl;

import com.zzzwb.myblog.domain.RoleResource;
import com.zzzwb.myblog.mapper.RoleResourceMapper;
import com.zzzwb.myblog.service.RoleResouceService;
import com.zzzwb.myblog.service.impl.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 角色资源关系事务层实现
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
@Service
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResource> implements RoleResouceService {

	private RoleResourceMapper roleResourceMapper;

	public RoleResourceServiceImpl (RoleResourceMapper roleResourceMapper){
		super(roleResourceMapper);
		this.roleResourceMapper = roleResourceMapper;
	}
}
