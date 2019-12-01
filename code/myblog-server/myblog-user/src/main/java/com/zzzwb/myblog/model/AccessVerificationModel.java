package com.zzzwb.myblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 权限校验model
 *
 * @author zeng wenbin
 * @date Created in 2019/12/1
 */
@Setter
@Getter
@AllArgsConstructor
public class AccessVerificationModel {
	/**
	 * 是否是权限资源
	 */
	private boolean isAccess;
	/**
	 * 如果是权限资源，则对应的资源uri
	 */
	private String uri;

	private String action;
}
