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
package org.springblade.modules.develop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.modules.develop.entity.ModelPrototype;
import org.springblade.modules.develop.service.IModelPrototypeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 数据原型表 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_DEVELOP_NAME + "/model-prototype")
@Api(value = "数据原型表", tags = "数据原型表接口")
public class ModelPrototypeController extends BladeController {

	private final IModelPrototypeService modelPrototypeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入modelPrototype")
	public R<ModelPrototype> detail(ModelPrototype modelPrototype) {
		ModelPrototype detail = modelPrototypeService.getOne(Condition.getQueryWrapper(modelPrototype));
		return R.data(detail);
	}

	/**
	 * 分页 数据原型表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入modelPrototype")
	public R<IPage<ModelPrototype>> list(ModelPrototype modelPrototype, Query query) {
		IPage<ModelPrototype> pages = modelPrototypeService.page(Condition.getPage(query), Condition.getQueryWrapper(modelPrototype));
		return R.data(pages);
	}

	/**
	 * 新增 数据原型表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入modelPrototype")
	public R save(@Valid @RequestBody ModelPrototype modelPrototype) {
		return R.status(modelPrototypeService.save(modelPrototype));
	}

	/**
	 * 修改 数据原型表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入modelPrototype")
	public R update(@Valid @RequestBody ModelPrototype modelPrototype) {
		return R.status(modelPrototypeService.updateById(modelPrototype));
	}

	/**
	 * 新增或修改 数据原型表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入modelPrototype")
	public R submit(@Valid @RequestBody ModelPrototype modelPrototype) {
		return R.status(modelPrototypeService.saveOrUpdate(modelPrototype));
	}

	/**
	 * 批量新增或修改 数据原型表
	 */
	@PostMapping("/submit-list")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "批量新增或修改", notes = "传入modelPrototype集合")
	public R submitList(@Valid @RequestBody List<ModelPrototype> modelPrototypes) {
		return R.status(modelPrototypeService.submitList(modelPrototypes));
	}

	/**
	 * 删除 数据原型表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(modelPrototypeService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 * 数据原型列表
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "数据原型列表", notes = "数据原型列表")
	public R<List<ModelPrototype>> select(@ApiParam(value = "数据模型Id", required = true) @RequestParam Long modelId) {
		List<ModelPrototype> list = modelPrototypeService.list(Wrappers.<ModelPrototype>query().lambda().eq(ModelPrototype::getModelId, modelId));
		list.forEach(prototype -> prototype.setComment(prototype.getJdbcName() + StringPool.COLON + StringPool.SPACE + prototype.getComment()));
		return R.data(list);
	}

}
