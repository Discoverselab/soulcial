package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.admin.dao.CollectInfoMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.CollectInfoPO;
import org.springblade.modules.admin.pojo.query.CollectInfoQuery;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/api/admin/collect")
@Api(value = "管理后台登录",tags = "管理后台登录接口")
public class CollectInfoController {


	@Resource
	CollectInfoMapper collectInfoMapper;

	@GetMapping("/getPage")
	@ApiOperation(value = "获取收集的信息")
	public R<Page<CollectInfoPO>> getPage(@ApiParam(value = "学员姓名",required = false) @RequestParam(required = false) String name,
										  @ApiParam(value = "年龄",required = false) @RequestParam(required = false) Integer age,
										  @ApiParam(value = "性别：0-女 1-男",required = false) @RequestParam(required = false) Integer sex,
										  @ApiParam(value = "联系方式",required = false) @RequestParam(required = false) String phone,
										  @ApiParam(value = "联系地址",required = false) @RequestParam(required = false) String address,
										  @ApiParam(value = "家长微信/QQ",required = false) @RequestParam(required = false) String contact,
										  @ApiParam(value = "留言内容",required = false) @RequestParam(required = false) String remark,
										  @ApiParam(value = "当前页",required = true) @RequestParam("current")Integer current,
										  @ApiParam(value = "每页的数量",required = true) @RequestParam("size")Integer size) {
		Page<CollectInfoPO> page = new Page<>(current,size);
		LambdaQueryWrapper<CollectInfoPO> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BasePO::getIsDeleted,0);
		wrapper.orderByDesc(BasePO::getCreateTime);

		if(StringUtil.isNotBlank(name)){
			wrapper.like(CollectInfoPO::getName,name);
		}
		if(age != null){
			wrapper.eq(CollectInfoPO::getAge,age);
		}
		if(sex != null){
			wrapper.eq(CollectInfoPO::getSex,sex);
		}
		if(StringUtil.isNotBlank(phone)){
			wrapper.like(CollectInfoPO::getPhone,phone);
		}
		if(StringUtil.isNotBlank(address)){
			wrapper.like(CollectInfoPO::getAddress,address);
		}
		if(StringUtil.isNotBlank(contact)){
			wrapper.like(CollectInfoPO::getContact,contact);
		}
		if(StringUtil.isNotBlank(remark)){
			wrapper.like(CollectInfoPO::getRemark,remark);
		}

		page = collectInfoMapper.selectPage(page, wrapper);

		return R.data(page);
	}

	@PostMapping("/addCollect")
	@ApiOperation(value = "添加信息")
	public R addCollect(@RequestBody CollectInfoQuery collectInfoQuery) {
		CollectInfoPO collectInfoPO = new CollectInfoPO();
		BeanUtil.copyProperties(collectInfoQuery,collectInfoPO);

		Date date = new Date();
		collectInfoPO.setCreateTime(date);
		collectInfoPO.setUpdateTime(date);
		collectInfoPO.setIsDeleted(0);

		collectInfoMapper.insert(collectInfoPO);

		return R.success("添加成功");
	}
}
