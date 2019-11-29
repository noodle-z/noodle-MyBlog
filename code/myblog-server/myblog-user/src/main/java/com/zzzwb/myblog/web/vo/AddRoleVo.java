package com.zzzwb.myblog.web.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * 给用户添加角色vo
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
@Setter
@Getter
public class AddRoleVo {

	private Integer userId;

	private Set<Integer> roles = new HashSet<>();

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Set<Integer> getRoles() {
		return roles;
	}

	public void setRoles(Set<Integer> roles) {
		this.roles = roles;
	}
}
