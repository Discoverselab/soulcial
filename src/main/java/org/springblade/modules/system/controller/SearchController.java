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
package org.springblade.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tenant.annotation.NonDS;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.system.entity.Post;
import org.springblade.modules.system.service.IDeptService;
import org.springblade.modules.system.service.IPostService;
import org.springblade.modules.system.service.IRoleService;
import org.springblade.modules.system.service.IUserService;
import org.springblade.modules.system.vo.DeptVO;
import org.springblade.modules.system.vo.PostVO;
import org.springblade.modules.system.vo.RoleVO;
import org.springblade.modules.system.vo.UserVO;
import org.springblade.modules.system.wrapper.PostWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 查询控制器
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/search")
@Api(value = "信息查询", tags = "信息查询")
public class SearchController {

	private final IRoleService roleService;

	private final IDeptService deptService;

	private final IPostService postService;

	private final IUserService userService;

	/**
	 * 角色信息查询
	 */
	@GetMapping("/role")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "角色信息查询", notes = "传入roleName或者parentId")
	public R<List<RoleVO>> roleSearch(String roleName, Long parentId) {
		return R.data(roleService.search(roleName, parentId));
	}

	/**
	 * 部门信息查询
	 */
	@GetMapping("/dept")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "部门信息查询", notes = "传入deptName或者parentId")
	public R<List<DeptVO>> deptSearch(String deptName, Long parentId) {
		return R.data(deptService.search(deptName, parentId));
	}

	/**
	 * 岗位信息查询
	 */
	@GetMapping("/post")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "岗位信息查询", notes = "传入postName")
	public R<IPage<PostVO>> postSearch(String postName, Query query) {
		LambdaQueryWrapper<Post> queryWrapper = Wrappers.<Post>query().lambda();
		if (Func.isNotBlank(postName)) {
			queryWrapper.like(Post::getPostName, postName);
		}
		IPage<Post> pages = postService.page(Condition.getPage(query), queryWrapper);
		return R.data(PostWrapper.build().pageVO(pages));
	}


	/**
	 * 用户列表查询
	 */
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "人员姓名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "deptName", value = "部门名称", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "postName", value = "职位名称", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "current", value = "当前页数", paramType = "query", dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", paramType = "query", dataType = "int")
	})
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "用户列表查询", notes = "用户列表查询")
	@GetMapping("/user")
	public R<IPage<UserVO>> userSearch(@ApiIgnore UserVO user, @ApiIgnore Query query) {
		return R.data(userService.selectUserSearch(user, query));
	}

}
