package com.zzzwb.myblog.mapper;

import com.zzzwb.myblog.cache.MybatisRedisCache;
import com.zzzwb.myblog.domain.UserRole;
import com.zzzwb.myblog.tkmybatis.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace(implementation = MybatisRedisCache.class)
public interface UserRoleMapper extends BaseMapper<UserRole> {
}