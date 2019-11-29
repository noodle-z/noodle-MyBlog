package com.zzzwb.myblog.web.controllerAdvice.errorDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 越权错误提示信息
 *
 * @author zeng wenbin
 * @date Created in 2019/8/10
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessError {
	private String message;
	private String path;
	private String action;
}
