package com.zzzwb.myblog.service.impl;

import com.zzzwb.myblog.bean.SearchBean;
import com.zzzwb.myblog.domain.Role;
import com.zzzwb.myblog.domain.UserRole;
import com.zzzwb.myblog.mapper.RoleMapper;
import com.zzzwb.myblog.mapper.UserRoleMapper;
import com.zzzwb.myblog.service.RoleService;
import com.zzzwb.myblog.service.UserRoleService;
import com.zzzwb.myblog.service.impl.base.BaseServiceImpl;
import org.apache.ibatis.javassist.tools.rmi.Sample;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色事务实现层
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	/**
	 * 角色mapper
	 */
	private RoleMapper roleMapper;

	private UserRoleService userRoleService;

	public RoleServiceImpl(RoleMapper roleMapper, UserRoleService userRoleService){
		super(roleMapper);
		this.userRoleService = userRoleService;
		this.roleMapper = roleMapper;
	}

	@Override
	public List<Role> findByUserId(Integer userId) {
		SearchBean searchBean = new SearchBean("userId", userId);
		List<UserRole> userRoles = this.userRoleService.queryBySearch(searchBean);
		List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
		Example example = new Example(Role.class);
		example.createCriteria().andIn("id", roleIds);
		return this.roleMapper.selectByExample(example);
	}
}
