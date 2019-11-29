package com.zzzwb.myblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 像前端抛出的错误消息提示异常
 * 		错误内容会直接在前端提示
 *
 * @author zeng wenbin
 * @date Created in 2019/8/6
 */
@Setter
@Getter
@AllArgsConstructor
public class ErrorMessageException {
	/**
	 * 错误内容
	 */
	private String message;
}
