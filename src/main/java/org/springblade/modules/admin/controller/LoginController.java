package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.MemberPO;
import org.springblade.modules.admin.pojo.vo.MemberVo;
import org.springblade.modules.admin.service.BNBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin/login")
@Api(value = "登录",tags = "登录")
public class LoginController {

	@Autowired
	MemberMapper memberMapper;

	@PostMapping("/login")
	@ApiOperation(value = "登录")
	public R<MemberVo> login(@RequestParam("address") String address,
							 @ApiParam(value = "登录类型：0-钱包 1-particle",required = true) @RequestParam("loginType") Integer loginType,
							 @ApiParam(value = "particleType类型：传数字每个数字分别代表一种类型",required = false) @RequestParam(value = "particleType",required = false) Integer particleType) {
		if(loginType == 1 && particleType == null){
			return R.fail("particleType must not be null!");
		}

		address = address.toLowerCase();
		MemberPO memberPO = memberMapper.selectOne(new LambdaQueryWrapper<MemberPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberPO::getAddress, address.toLowerCase()));

		if(memberPO == null){
			memberPO = new MemberPO();

			memberPO.setAddress(address);
			memberPO.setFreeMint(0);
			Date date = new Date();
			memberPO.setCreateTime(date);
			memberPO.setUpdateTime(date);
			memberPO.setIsDeleted(0);

			memberPO.setUserName(address.substring(0,6));

			List<String> avatarList = new ArrayList<>();
			avatarList.add("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/12/1667928966727528448.png");
			avatarList.add("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/12/1667929421918564352.png");
			avatarList.add("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/12/1667929484132675584.png");

			memberPO.setAvatar(avatarList.get(RandomUtil.randomInt(0,3)));

			//TODO 用户注册。刷新分数
			memberPO.setCharisma(RandomUtil.randomInt(20,100));
			memberPO.setExtroversion(RandomUtil.randomInt(20,100));
			memberPO.setEnergy(RandomUtil.randomInt(20,100));
			memberPO.setWisdom(RandomUtil.randomInt(20,100));
			memberPO.setArt(RandomUtil.randomInt(20,100));
			memberPO.setCourage(RandomUtil.randomInt(20,100));
			//计算总分
			memberPO.setLevelScore(memberPO.getCharisma() + memberPO.getExtroversion() + memberPO.getEnergy() +
				memberPO.getWisdom() + memberPO.getArt() + memberPO.getCourage());
			//设置level
			memberPO.countLevel();

			memberPO.setLoginType(loginType);
			memberPO.setParticleType(particleType);

			memberMapper.insert(memberPO);
		}

		Long userId = memberPO.getId();

		StpUtil.login(userId);
		SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

		MemberVo memberVo = new MemberVo();
		memberVo.setTokenName(tokenInfo.getTokenName());
		memberVo.setTokenValue(tokenInfo.getTokenValue());

		memberVo.setAddress(memberPO.getAddress());
		memberVo.setFreeMint(memberPO.getFreeMint());

		memberVo.setLoginType(memberPO.getLoginType());
		memberVo.setParticleType(memberPO.getParticleType());

		return R.data(memberVo);
	}

	@ApiOperation("退出登录")
	@PostMapping("/logout")
	public R logout() {
		try {
			StpUtil.logout();
		}catch (Exception e){
			e.printStackTrace();
		}
		return R.success("退出成功");
	}
}
