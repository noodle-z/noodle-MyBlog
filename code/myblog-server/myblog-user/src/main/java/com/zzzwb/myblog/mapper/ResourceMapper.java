package com.zzzwb.myblog.mapper;

import com.zzzwb.myblog.cache.MybatisRedisCache;
import com.zzzwb.myblog.domain.Resource;
import com.zzzwb.myblog.tkmybatis.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace(implementation = MybatisRedisCache.class)
public interface ResourceMapper extends BaseMapper<Resource> {
}