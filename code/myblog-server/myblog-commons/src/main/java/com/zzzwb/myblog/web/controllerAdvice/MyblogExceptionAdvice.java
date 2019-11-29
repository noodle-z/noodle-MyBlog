package com.zzzwb.myblog.web.controllerAdvice;

import com.zzzwb.myblog.constant.ExceptionCode;
import com.zzzwb.myblog.exception.AccessException;
import com.zzzwb.myblog.exception.NoLoginException;
import com.zzzwb.myblog.web.controllerAdvice.errorDto.AccessError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一的异常处理
 *
 * @author zeng wenbin
 * @date Created in 2019/8/10
 */
@ControllerAdvice
public class MyblogExceptionAdvice {

	/**
	 * 越权异常
	 * @param e 异常
	 * @return 结果
	 */
	@ExceptionHandler(value = AccessException.class)
	@ResponseBody
	public ResponseEntity accessException(AccessException e) {
		e.printStackTrace();
		return ResponseEntity.status(ExceptionCode.ACCESS.getCode())
				.body(new AccessError(ExceptionCode.ACCESS.getMessage(), e.getPath(), e.getAction()));
	}

	/**
	 * 未登录异常
	 * @param e 异常
	 * @return 结果
	 */
	@ExceptionHandler(value = NoLoginException.class)
	@ResponseBody
	public ResponseEntity noLoginException(NoLoginException e) {
		e.printStackTrace();
		return ResponseEntity.status(ExceptionCode.NO_LOGIN.getCode()).body(ExceptionCode.NO_LOGIN.getMessage());
	}
}
