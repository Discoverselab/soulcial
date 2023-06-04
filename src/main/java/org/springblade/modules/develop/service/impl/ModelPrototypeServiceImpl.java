/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.modules.develop.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.develop.entity.ModelPrototype;
import org.springblade.modules.develop.mapper.ModelPrototypeMapper;
import org.springblade.modules.develop.service.IModelPrototypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据原型表 服务实现类
 *
 * @author Chill
 */
@Service
public class ModelPrototypeServiceImpl extends BaseServiceImpl<ModelPrototypeMapper, ModelPrototype> implements IModelPrototypeService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean submitList(List<ModelPrototype> modelPrototypes) {
		modelPrototypes.forEach(modelPrototype -> {
			if (modelPrototype.getId() == null) {
				this.save(modelPrototype);
			} else {
				this.updateById(modelPrototype);
			}
		});
		return true;
	}

	@Override
	public List<ModelPrototype> prototypeList(Long modelId) {
		return this.list(Wrappers.<ModelPrototype>lambdaQuery().eq(ModelPrototype::getModelId, modelId));
	}

}
