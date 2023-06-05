package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.admin.dao.CollectInfoMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.CollectInfoPO;
import org.springblade.modules.admin.pojo.query.CollectInfoQuery;
import org.springblade.modules.admin.service.BNBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/api/admin/nft")
@Api(value = "铸造NFT",tags = "铸造NFT")
public class NftController {

	@Autowired
	BNBService bnbService;


	@PostMapping("/mintPFP")
	@ApiOperation(value = "铸造PFP")
	public R addCollect(@RequestParam("toAddress") String toAddress) {
//		Long userId = StpUtil.getLoginIdAsLong();

		bnbService.mintPFP(toAddress);

		return R.success("铸造成功");
	}

	@PostMapping("/createAdminAddress")
	@ApiOperation(value = "创建平台钱包地址")
	public R createAdminAddress() {
		String adminWallet = bnbService.createAdminWallet();
		return R.data(adminWallet);
	}
}
