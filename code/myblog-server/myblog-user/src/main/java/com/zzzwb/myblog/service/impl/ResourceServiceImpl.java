package com.zzzwb.myblog.service.impl;

import com.zzzwb.myblog.domain.Resource;
import com.zzzwb.myblog.mapper.ResourceMapper;
import com.zzzwb.myblog.service.ResourceService;
import com.zzzwb.myblog.service.impl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 资源事务层
 *
 * @author zeng wenbin
 * @date Created in 2019/7/28
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements ResourceService {

	@Autowired
	private ResourceMapper resourceMapper;

	public ResourceServiceImpl(ResourceMapper resourceMapper) {
		super(resourceMapper);
	}
}
