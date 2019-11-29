package com.zzzwb.myblog.service.base;

import com.github.pagehelper.PageInfo;
import com.zzzwb.myblog.bean.SearchBean;
import com.zzzwb.myblog.domain.base.BaseEntity;
import com.zzzwb.myblog.exception.ParamErrorException;
import com.zzzwb.myblog.web.vo.PageVo;

import java.util.List;
import java.util.Optional;

/**
 * service接口基类
 * 		附带通用的增删改查方法
 *		泛型为实体
 * @author zeng wenbin
 * @date Created in 2019/7/25
 */
public interface BaseService<E extends BaseEntity> {
	/**
	 * 根据id查询单个
	 * @param id 主键id
	 * @return 实体类optional
	 */
	Optional<E> findOne(Integer id);

	/**
	 * 分页
	 * @param pageVo 分页条件
	 * @return	分页数据
	 */
	PageInfo<E> queryPage(PageVo pageVo) throws ParamErrorException;

	/**
	 * 根据条件搜索
	 * @param searchBean 搜索条件
	 * @return	数据
	 */
	List<E> queryBySearch(SearchBean searchBean);

	/**
	 * 根据主键删除单个
	 * @param id 主键id
	 */
	void deleteOne(Integer id);

	/**
	 * 根据id列表删除多个
	 * @param ids id列表
	 */
	void deleteList(List<Integer> ids);

	/**
	 * 更新操作
	 * 	 根据主键id进行更新
	 * 	 除主键外有值的属性都更新
	 * @param entity 实体
	 * @return 更新结果optional
	 */
	Optional<E> update(E entity);

	/**
	 * 数据新增
	 * @param entity 数据
	 * @return 新增的实体
	 */
	E create(E entity);

	/**
	 * 查询全部
	 * @return 全部数据
	 */
	List<E> findAll();
}
