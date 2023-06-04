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
package org.springblade.modules.develop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.mp.base.BaseEntity;

/**
 * 数据原型表实体类
 *
 * @author Chill
 */
@Data
@TableName("blade_model_prototype")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ModelPrototype对象", description = "数据原型表")
public class ModelPrototype extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 模型主键
	 */
	@ApiModelProperty(value = "模型主键")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long modelId;
	/**
	 * 物理列名
	 */
	@ApiModelProperty(value = "物理列名")
	private String jdbcName;
	/**
	 * 物理类型
	 */
	@ApiModelProperty(value = "物理类型")
	private String jdbcType;
	/**
	 * 实体列名
	 */
	@ApiModelProperty(value = "实体列名")
	private String propertyName;
	/**
	 * 实体类型
	 */
	@ApiModelProperty(value = "实体类型")
	private String propertyType;
	/**
	 * 实体类型引用
	 */
	@ApiModelProperty(value = "实体类型引用")
	private String propertyEntity;
	/**
	 * 注释说明
	 */
	@ApiModelProperty(value = "注释说明")
	private String comment;
	/**
	 * 列表显示
	 */
	@ApiModelProperty(value = "列表显示")
	private Integer isList;
	/**
	 * 表单显示
	 */
	@ApiModelProperty(value = "表单显示")
	private Integer isForm;
	/**
	 * 独占一行
	 */
	@ApiModelProperty(value = "独占一行")
	private Integer isRow;
	/**
	 * 组件类型
	 */
	@ApiModelProperty(value = "组件类型")
	private String componentType;
	/**
	 * 字典编码
	 */
	@ApiModelProperty(value = "字典编码")
	private String dictCode;
	/**
	 * 是否必填
	 */
	@ApiModelProperty(value = "是否必填")
	private Integer isRequired;
	/**
	 * 查询配置
	 */
	@ApiModelProperty(value = "查询配置")
	private Integer isQuery;
	/**
	 * 查询类型
	 */
	@ApiModelProperty(value = "查询类型")
	private String queryType;


}
