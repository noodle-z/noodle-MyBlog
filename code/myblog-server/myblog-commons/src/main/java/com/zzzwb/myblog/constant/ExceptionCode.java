package com.zzzwb.myblog.constant;

/**
 * 自定义异常编码
 *
 * @author zeng wenbin
 * @date Created in 2019/8/10
 */
public enum ExceptionCode {
	/**
	 * 越权
	 */
	ACCESS(403, "您没有权限访问哦!"),
	/**
	 * 未登录
	 */
	NO_LOGIN(401,"您还未登陆哦！");

	private Integer code;

	private String message;

	ExceptionCode(Integer code, String msg) {

		this.code = code;

		this.message = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
