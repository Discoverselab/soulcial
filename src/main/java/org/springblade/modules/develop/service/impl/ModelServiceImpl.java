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
import lombok.RequiredArgsConstructor;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.modules.develop.entity.Code;
import org.springblade.modules.develop.entity.Model;
import org.springblade.modules.develop.entity.ModelPrototype;
import org.springblade.modules.develop.mapper.ModelMapper;
import org.springblade.modules.develop.service.ICodeService;
import org.springblade.modules.develop.service.IModelPrototypeService;
import org.springblade.modules.develop.service.IModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据模型表 服务实现类
 *
 * @author Chill
 */
@Service
@RequiredArgsConstructor
public class ModelServiceImpl extends BaseServiceImpl<ModelMapper, Model> implements IModelService {

	private final IModelPrototypeService modelPrototypeService;
	private final ICodeService codeService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delete(List<Long> ids) {
		boolean modelTemp = this.deleteLogic(ids);
		if (modelTemp) {
			if (modelPrototypeService.count(Wrappers.<ModelPrototype>lambdaQuery().in(ModelPrototype::getModelId, ids)) > 0) {
				boolean prototypeTemp = modelPrototypeService.remove(Wrappers.<ModelPrototype>lambdaQuery().in(ModelPrototype::getModelId, ids));
				if (!prototypeTemp) {
					throw new ServiceException("删除数据模型成功，关联数据原型删除失败");
				}
			}
			if (codeService.count(Wrappers.<Code>lambdaQuery().in(Code::getModelId, ids)) > 0) {
				boolean codeTemp = codeService.remove(Wrappers.<Code>lambdaQuery().in(Code::getModelId, ids));
				if (!codeTemp) {
					throw new ServiceException("删除数据模型成功，关联代码生成配置删除失败");
				}
			}
		}
		return true;
	}
}
