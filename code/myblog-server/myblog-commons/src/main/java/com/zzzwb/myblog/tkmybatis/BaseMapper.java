package com.zzzwb.myblog.tkmybatis;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * mybatis持久化接口基类
 *
 * @author zeng wenbin
 * @date Created in 2019/7/24
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
