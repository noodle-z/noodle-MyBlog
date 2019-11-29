package com.zzzwb.myblog.service;

import com.zzzwb.myblog.domain.Role;
import com.zzzwb.myblog.service.base.BaseService;

import java.util.List;

/**
 * 角色service
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
public interface RoleService extends BaseService<Role> {

	/**
	 * genuine用户id取得该用户全部角色
	 * @param userId 用户id
	 * @return	角色列表
	 */
	List<Role> findByUserId(Integer userId);
}
