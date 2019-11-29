package com.zzzwb.myblog.web.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回消息的基本结构
 *
 * @author zeng wenbin
 * @date Created in 2019/7/26
 */
@Setter
@Getter
public class BaseDto {
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 数据
	 */
	private Object data;
	/**
	 * 描述
	 */
	private String description;

	/**
	 * 无数据的成功消息
	 * @return 消息
	 */
	public static BaseDto success(){
		BaseDto vo = new BaseDto();
		vo.setStatus("success");
		return vo;
	}

	/**
	 * 有数据的成功消息
	 * @param data 数据
	 * @return 消息
	 */
	public static BaseDto success(Object data){
		BaseDto vo = success();
		vo.setData(data);
		return vo;
	}

	/**
	 * 无描述的失败消息
	 * @return 消息
	 */
	public static BaseDto error(){
		BaseDto vo = new BaseDto();
		vo.setStatus("error");
		return vo;
	}

	/**
	 * 有描述的失败消息
	 * @param description 描述
	 * @return 消息
	 */
	public static BaseDto error(String description){
		BaseDto error = error();
		error.setDescription(description);
		return error;
	}
}