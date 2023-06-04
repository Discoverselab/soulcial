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
package org.springblade.modules.develop.service;

import org.springblade.core.mp.base.BaseService;
import org.springblade.modules.develop.entity.ModelPrototype;

import java.util.List;

/**
 * 数据原型表 服务类
 *
 * @author Chill
 */
public interface IModelPrototypeService extends BaseService<ModelPrototype> {

	/**
	 * 批量提交
	 *
	 * @param modelPrototypes 原型集合
	 * @return boolean
	 */
	boolean submitList(List<ModelPrototype> modelPrototypes);

	/**
	 * 原型列表
	 *
	 * @param modelId 模型ID
	 * @return List<ModelPrototype>
	 */
	List<ModelPrototype> prototypeList(Long modelId);

}
