package com.zzzwb.myblog.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 参数错误异常
 *
 * @author zeng wenbin
 * @date Created in 2019/7/26
 */
@Setter
@Getter
@AllArgsConstructor
public class ParamErrorException extends Exception {

	private String errorCode;

	private String errorMsg;

}
