package com.zzzwb.myblog.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索bean
 *
 * @author zeng wenbin
 * @date Created in 2019/7/31
 */
@Getter
public class SearchBean {
	/**
	 * 排序字段名集合 名称应使用"字段名-搜索方式"的形式,否则默认使用sql的 == 查询
	 * 	例如name-eq 会根据name进行sql的 == 查询
	 * 		name-like会根据name进行sql的 like 查询
	 * 		详见BaseServiceImpl的packageSearch方法
	 * 	当为name-in时，value多个条件写成字符串并用英文逗号隔开，如 name:id-in,value: "1,2,3,4"
	 */
	private List<String> propertyNames = new ArrayList<>();
	/**
	 * 排序字段值集合
	 */
	private List<Object> propertyValues = new ArrayList<>();

	public SearchBean(String name, Object value){
		this.addProperty(name, value);
	}

	public SearchBean(List<String> propertyNames, List<Object> propertyValues) {
		this.propertyNames = propertyNames;
		this.propertyValues = propertyValues;
	}

	public void addProperty(String name, Object value){
		this.propertyNames.add(name);
		this.propertyValues.add(value);
	}
}
