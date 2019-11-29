package com.zzzwb.myblog.constant;

/**
 * 接口访问的动作
 * 		用于@Access注解
 *
 * @author zeng wenbin
 * @date Created in 2019/7/25
 */
public enum Action {
	/**
	 * 新增
	 */
	ADD,
	/**
	 * 查找
	 */
	SELECT,
	/**
	 * 更新
	 */
	UPDATE,
	/**
	 * 删除
	 */
	DELETE,
	/**
	 * 其他
	 */
	OTHER
}
