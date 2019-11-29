package com.zzzwb.myblog.web.vo;

import com.zzzwb.myblog.constant.Direction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页条件
 * 	包含页码，每页数据量，检索条件，排序规则
 *
 * @author zeng wenbin
 * @date Created in 2019/7/26
 */
@Setter
@Getter
public class PageVo {
	/**
	 * 默认排序字段名
	 */
	private final String defaultSortName = "lastModifiedTime";

	/**
	 * 页码
	 */
	@ApiModelProperty("页码，默认0")
	private Integer pageNum = 0;
	/**
	 * 每页数量
	 */
	@ApiModelProperty("每页数量，默认10")
	private Integer pageSize = 10;
	/**
	 * 排序字段名 名称应使用"字段名-搜索方式"的形式,否则默认使用sql的 == 查询
	 * 	例如name-eq 会根据name进行sql的 == 查询
	 * 		name-like会根据name进行sql的 like 查询
	 * 		详见BaseServiceImpl的packageSearch方法
	 * 	当为name-in时，value多个条件写成字符串并用英文逗号隔开，如 name:id-in,value: "1,2,3,4"
	 */
	@ApiModelProperty("排序字段名集合")
	private List<String> sortNames = new ArrayList<>();
	/**
	 * 排序字段值
	 */
	@ApiModelProperty("排序字段值集合")
	private List<Direction> sortValues = new ArrayList<>();
	/**
	 * 检索条件字段名
	 */
	@ApiModelProperty("检索条件字段名集合")
	private List<String> propertyNames = new ArrayList<>();
	/**
	 * 检索字段值
	 */
	@ApiModelProperty("检索条件字段值集合")
	private List<Object> propertyValues = new ArrayList<>();

	public PageVo() {
		//默认根据最后修改时间降序排列
		this.sortNames.add(this.defaultSortName);
		this.sortValues.add(Direction.DESC);
	}

	public void addProperty(String name, Object value){
		this.propertyNames.add(name);
		this.propertyValues.add(value);
	}

	public void removeProvertyByName(String key){
		for (int i = 0; i < this.propertyNames.size(); i++) {
			if (this.propertyNames.get(i).equals(key)){
				this.propertyNames.remove(i);
				this.propertyValues.remove(i);
				return;
			}
		}
	}
}
