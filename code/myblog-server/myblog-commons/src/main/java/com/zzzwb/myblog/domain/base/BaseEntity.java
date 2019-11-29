package com.zzzwb.myblog.domain.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 *
 * @author zeng wenbin
 * @date Created in 2019/7/25
 */
@Setter
@Getter
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 5200354594013774535L;
	/**
	 * 创建时间
	 */
	private Date createdTime = new Date();
	/**
	 * 创建人id
	 */
	private Integer createdBy;
	/**
	 * 最后修改时间
	 */
	private Date lastModifiedTime = new Date();
	/**
	 * 最后修改人id
	 */
	private Integer lastModifiedBy;

	/**
	 * 子类重写，方便基类通过泛型获取id值
	 * @return
	 */
	public abstract Integer getId();
}
