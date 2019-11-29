package com.zzzwb.myblog.mapper;

import com.zzzwb.myblog.cache.MybatisRedisCache;
import com.zzzwb.myblog.domain.Role;
import com.zzzwb.myblog.tkmybatis.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace(implementation = MybatisRedisCache.class)
public interface RoleMapper extends BaseMapper<Role> {
}