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
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
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
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.develop.entity.Datasource;
import org.springblade.modules.develop.entity.Model;
import org.springblade.modules.develop.entity.ModelPrototype;
import org.springblade.modules.develop.service.IDatasourceService;
import org.springblade.modules.develop.service.IModelPrototypeService;
import org.springblade.modules.develop.service.IModelService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据模型表 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_DEVELOP_NAME + "/model")
@Api(value = "数据模型表", tags = "数据模型表接口")
public class ModelController extends BladeController {

	private final IModelService modelService;
	private final IModelPrototypeService modelPrototypeService;
	private final IDatasourceService datasourceService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入model")
	public R<Model> detail(Model model) {
		Model detail = modelService.getOne(Condition.getQueryWrapper(model));
		return R.data(detail);
	}

	/**
	 * 分页 数据模型表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入model")
	public R<IPage<Model>> list(Model model, Query query) {
		IPage<Model> pages = modelService.page(Condition.getPage(query), Condition.getQueryWrapper(model));
		return R.data(pages);
	}

	/**
	 * 新增 数据模型表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增", notes = "传入model")
	public R save(@Valid @RequestBody Model model) {
		return R.status(modelService.save(model));
	}

	/**
	 * 修改 数据模型表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "修改", notes = "传入model")
	public R update(@Valid @RequestBody Model model) {
		return R.status(modelService.updateById(model));
	}

	/**
	 * 新增或修改 数据模型表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "新增或修改", notes = "传入model")
	public R submit(@Valid @RequestBody Model model) {
		boolean temp = modelService.saveOrUpdate(model);
		if (temp) {
			return R.data(model);
		} else {
			return R.status(Boolean.FALSE);
		}
	}

	/**
	 * 删除 数据模型表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(modelService.delete(Func.toLongList(ids)));
	}

	/**
	 * 模型列表
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "模型列表", notes = "模型列表")
	public R<List<Model>> select() {
		List<Model> list = modelService.list();
		list.forEach(model -> model.setModelName(model.getModelTable() + StringPool.COLON + StringPool.SPACE + model.getModelName()));
		return R.data(list);
	}

	/**
	 * 获取物理表列表
	 */
	@GetMapping("/table-list")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "物理表列表", notes = "传入datasourceId")
	public R<List<TableInfo>> tableList(Long datasourceId) {
		Datasource datasource = datasourceService.getById(datasourceId);
		ConfigBuilder config = getConfigBuilder(datasource);
		List<TableInfo> tableInfoList = config.getTableInfoList().stream()
			.filter(tableInfo -> !StringUtil.startsWithIgnoreCase(tableInfo.getName(), "ACT_"))
			.map(tableInfo -> tableInfo.setComment(tableInfo.getName() + StringPool.COLON + tableInfo.getComment()))
			.collect(Collectors.toList());
		return R.data(tableInfoList);
	}

	/**
	 * 获取物理表信息
	 */
	@GetMapping("/table-info")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "物理表信息", notes = "传入model信息")
	public R<TableInfo> tableInfo(Long modelId, String tableName, Long datasourceId) {
		if (StringUtil.isBlank(tableName)) {
			Model model = modelService.getById(modelId);
			tableName = model.getModelTable();
		}
		TableInfo tableInfo = getTableInfo(tableName, datasourceId);
		return R.data(tableInfo);
	}

	/**
	 * 获取字段信息
	 */
	@GetMapping("/model-prototype")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "物理表字段信息", notes = "传入modelId与datasourceId")
	public R modelPrototype(Long modelId, Long datasourceId) {
		List<ModelPrototype> modelPrototypeList = modelPrototypeService.list(Wrappers.<ModelPrototype>query().lambda().eq(ModelPrototype::getModelId, modelId));
		if (modelPrototypeList.size() > 0) {
			return R.data(modelPrototypeList);
		}
		Model model = modelService.getById(modelId);
		String tableName = model.getModelTable();
		TableInfo tableInfo = getTableInfo(tableName, datasourceId);
		if (tableInfo != null) {
			return R.data(tableInfo.getFields());
		} else {
			return R.fail("未获得相关表信息");
		}
	}

	/**
	 * 获取表信息
	 *
	 * @param tableName    表名
	 * @param datasourceId 数据源主键
	 */
	private TableInfo getTableInfo(String tableName, Long datasourceId) {
		Datasource datasource = datasourceService.getById(datasourceId);
		ConfigBuilder config = getConfigBuilder(datasource, tableName);
		List<TableInfo> tableInfoList = config.getTableInfoList();
		TableInfo tableInfo = null;
		Iterator<TableInfo> iterator = tableInfoList.stream().filter(table -> table.getName().equals(tableName)).collect(Collectors.toList()).iterator();
		if (iterator.hasNext()) {
			tableInfo = iterator.next();
			if (tableName.contains(StringPool.UNDERSCORE)) {
				tableInfo.setEntityName(tableInfo.getEntityName().replace(StringUtil.firstCharToUpper(tableName.split(StringPool.UNDERSCORE)[0]), StringPool.EMPTY));
			} else {
				tableInfo.setEntityName(StringUtil.firstCharToUpper(tableName));
			}
		}
		return tableInfo;
	}

	/**
	 * 获取表配置信息
	 *
	 * @param datasource 数据源信息
	 */
	private ConfigBuilder getConfigBuilder(Datasource datasource) {
		return getConfigBuilder(datasource, null);
	}

	/**
	 * 获取表配置信息
	 *
	 * @param datasource 数据源信息
	 * @param tableName  表名
	 */
	private ConfigBuilder getConfigBuilder(Datasource datasource, String tableName) {
		StrategyConfig.Builder builder = new StrategyConfig.Builder();
		if (StringUtil.isNotBlank(tableName)) {
			builder.addInclude(tableName);
		}
		StrategyConfig strategyConfig = builder.entityBuilder()
			.naming(NamingStrategy.underline_to_camel)
			.columnNaming(NamingStrategy.underline_to_camel).build();
		DataSourceConfig datasourceConfig = new DataSourceConfig.Builder(
			datasource.getUrl(), datasource.getUsername(), datasource.getPassword()
		).build();
		return new ConfigBuilder(null, datasourceConfig, strategyConfig, null, null, null);
	}


}
