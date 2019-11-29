package com.zzzwb.myblog.web.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 登陆数据
 *
 * @author zeng wenbin
 * @date Created in 2019/7/29
 */
@Setter
@Getter
public class LoginVo {
	/**
	 * 用户名
	 */
	@NotNull
	private String username;
	/**
	 * 密码
	 */
	@NotNull
	private String password;
}
