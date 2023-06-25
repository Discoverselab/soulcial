package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.MemberPO;
import org.springblade.modules.admin.pojo.vo.MemberVo;
import org.springblade.modules.admin.service.BNBService;
import org.springblade.modules.admin.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin/login")
@Api(value = "登录",tags = "登录")
public class LoginController {

	@Autowired
	MemberMapper memberMapper;

	@Autowired
	UserScoreService userScoreService;

	@GetMapping("/checkSteamId")
	@ApiOperation(value = "查询是否已生成steam_id")
	public R<Boolean> checkSteamId(@RequestParam("address") String address) {

		address = address.toLowerCase();
		MemberPO memberPO = memberMapper.selectOne(new LambdaQueryWrapper<MemberPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberPO::getAddress, address.toLowerCase()));

		if(memberPO != null && StringUtil.isNotBlank(memberPO.getStreamId())){
			return R.data(true);
		}
		return R.data(false);
	}

	@PostMapping("/login")
	@ApiOperation(value = "登录")
	public R<MemberVo> login(@RequestParam("address") String address,
							 @RequestParam(value = "dataverse-streamId",required = false) String streamId,
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

			String userName = address.substring(0,6);
			//用户注册，获取lens账号
			String lensName = userScoreService.getLensNameByAddress(address);
			if(StringUtil.isNotBlank(lensName)){
				userName = lensName;
			}
			memberPO.setUserName(userName);

			List<String> avatarList = new ArrayList<>();
			avatarList.add("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/25/1672961118976385024.png");
			avatarList.add("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/25/1672961478398877696.png");
			avatarList.add("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/25/1672961557138546688.png");



			memberPO.setAvatar(avatarList.get(RandomUtil.randomInt(0,3)));


			//TODO 用户注册。刷新分数
//			memberPO.setCharisma(RandomUtil.randomInt(20,100));
//			memberPO.setExtroversion(RandomUtil.randomInt(20,100));
//			memberPO.setEnergy(RandomUtil.randomInt(20,100));
//			memberPO.setWisdom(RandomUtil.randomInt(20,100));
//			memberPO.setArt(RandomUtil.randomInt(20,100));
//			memberPO.setCourage(RandomUtil.randomInt(20,100));
//			//计算总分
//			memberPO.setLevelScore(memberPO.getCharisma() + memberPO.getExtroversion() + memberPO.getEnergy() +
//				memberPO.getWisdom() + memberPO.getArt() + memberPO.getCourage());
//			//设置level
//			memberPO.countLevel();

			memberPO.setLoginType(loginType);
			memberPO.setParticleType(particleType);

			memberMapper.insert(memberPO);

			Long userId = memberPO.getId();

			//TODO 开启线程刷新用户分数(是否需要改成同步)
//			ThreadUtil.execAsync(()->{
//				userScoreService.updateUserScore(userId);
//			});
		}

		if(memberPO.getCharisma() == null){
			//TODO 刷新分数
			memberPO.setCharisma(RandomUtil.randomInt(20,100));
			memberPO.setExtroversion(RandomUtil.randomInt(20,100));
			memberPO.setEnergy(RandomUtil.randomInt(20,100));
			memberPO.setWisdom(RandomUtil.randomInt(20,100));
			memberPO.setArt(RandomUtil.randomInt(20,100));
			memberPO.setCourage(RandomUtil.randomInt(20,100));
//			//计算总分
			memberPO.setLevelScore(memberPO.getCharisma() + memberPO.getExtroversion() + memberPO.getEnergy() +
				memberPO.getWisdom() + memberPO.getArt() + memberPO.getCourage());
//			//设置level
			memberPO.countLevel();
			memberMapper.updateById(memberPO);
		}

		//dataverse steam_Id
		if(StringUtil.isBlank(memberPO.getStreamId()) && StringUtil.isNotBlank(streamId)){
			memberPO.setStreamId(streamId);
			memberMapper.updateById(memberPO);
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
