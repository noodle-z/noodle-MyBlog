package com.zzzwb.myblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 权限异常
 *		权限不足而抛出的异常
 * @author zeng wenbin
 * @date Created in 2019/8/6
 */
@Setter
@Getter
public class AccessException extends RuntimeException {
	/**
	 * 请求uri
	 */
	private String path;
	/**
	 * 权限类别
	 */
	private String action;

	public AccessException(String path, String action) {
		super("路径：" + path + ",权限动作：" + action);
		this.path = path;
		this.action = action;
	}
}
