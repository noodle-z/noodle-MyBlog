package com.zzzwb.myblog.web.rest;

import com.zzzwb.myblog.domain.Role;
import com.zzzwb.myblog.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色resource
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
@RestController
@RequestMapping("/api/role")
@Api(tags = "角色管理接口")
public class RoleResource extends BaseResource<Role> {

	private RoleService roleService;

	public RoleResource(RoleService roleService){
		super(roleService);
		this.roleService = roleService;
	}
}
