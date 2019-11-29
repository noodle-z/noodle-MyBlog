package com.zzzwb.myblog.web.rest;

import com.zzzwb.myblog.domain.RoleResource;
import com.zzzwb.myblog.service.RoleResouceService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色权限resource
 *
 * @author zeng wenbin
 * @date Created in 2019/8/8
 */
@RestController
@RequestMapping("/api/roleAccess")
@Api(tags = "角色权限接口")
public class RoleAccessResource extends BaseResource<RoleResource> {

	private RoleResouceService roleResouceService;

	public RoleAccessResource(RoleResouceService roleResouceService) {
		super(roleResouceService);
		this.roleResouceService = roleResouceService;
	}

}
