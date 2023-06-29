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
import org.springblade.modules.admin.dao.MemberFollowMapper;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.dao.PFPTokenMapper;
import org.springblade.modules.admin.dao.PFPTransactionMapper;
import org.springblade.modules.admin.pojo.enums.NFTColorEnum;
import org.springblade.modules.admin.pojo.enums.UserTagsEnum;
import org.springblade.modules.admin.pojo.po.*;
import org.springblade.modules.admin.pojo.query.FollowUserQuery;
import org.springblade.modules.admin.pojo.query.IdQurey;
import org.springblade.modules.admin.pojo.query.UserStreamIdQurey;
import org.springblade.modules.admin.pojo.vo.*;
import org.springblade.modules.admin.service.ETHService;
import org.springblade.modules.admin.service.NftService;
import org.springblade.modules.admin.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin/home")
@Api(value = "我的信息相关接口(home)",tags = "我的信息相关接口(home)")
public class HomeController {

	@Autowired
	ETHService ethService;

	@Autowired
	MemberMapper memberMapper;

	@Autowired
	NftService nftService;

	@Autowired
	PFPTokenMapper pfpTokenMapper;

	@Autowired
	MemberFollowMapper memberFollowMapper;

	@Autowired
	UserScoreService userScoreService;

//	@GetMapping("/getScore")
//	@ApiOperation(value = "获取NFT分页")
//	public R<Page<PFPTokenPageVo>> getScore() {
//
//
//
//		return R.data(result);
//	}

	@PostMapping("/followUser")
	@ApiOperation(value = "关注/取消关注")
	public R<UserInfoVo> followUser(@Valid @RequestBody FollowUserQuery followUserQuery) {

		Long userId = StpUtil.getLoginIdAsLong();
		Integer followType = followUserQuery.getFollowType();
		Long subscribeUserId = followUserQuery.getSubscribeUserId();
		if(followType == 0){
			//取消关注
			MemberFollowPO memberFollowPO = memberFollowMapper.selectOne(new LambdaQueryWrapper<MemberFollowPO>()
				.eq(BasePO::getIsDeleted, 0)
				.eq(MemberFollowPO::getUserId, userId)
				.eq(MemberFollowPO::getSubscribeUserId, subscribeUserId));

			//存在，删除关注
			if (memberFollowPO != null){
				memberFollowMapper.deleteById(memberFollowPO.getId());
			}
		}else {
			//关注
			MemberFollowPO memberFollowPO = memberFollowMapper.selectOne(new LambdaQueryWrapper<MemberFollowPO>()
				.eq(BasePO::getIsDeleted, 0)
				.eq(MemberFollowPO::getUserId, userId)
				.eq(MemberFollowPO::getSubscribeUserId, subscribeUserId));

			//不存在，添加关注
			if (memberFollowPO == null){
				memberFollowPO = new MemberFollowPO();
				memberFollowPO.setUserId(userId);
				memberFollowPO.setSubscribeUserId(subscribeUserId);
				memberFollowPO.initForInsert();
				memberFollowMapper.insert(memberFollowPO);
			}
		}
		return R.success("成功");
	}

	@GetMapping("/getFollowers")
	@ApiOperation(value = "被关注列表（FOLLOWERS）")
	public R<List<SubscribeFollowUserVo>> getFollowers(@ApiParam("用户id：不传的话默认查自己，不登录时userId必传")@RequestParam(value = "userId",required = false) Long userId) {

		List<SubscribeFollowUserVo> result = new ArrayList<>();
		//是否是其他人
		boolean isOther = true;

		if(userId == null){
			//是本人
			isOther = false;
			userId = StpUtil.getLoginIdAsLong();
		}
		//关注
		List<MemberFollowPO> list = memberFollowMapper.selectList(new LambdaQueryWrapper<MemberFollowPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberFollowPO::getSubscribeUserId, userId));

		for (MemberFollowPO x : list) {
			Long subscribeUserId = x.getUserId();
			MemberPO memberPO = memberMapper.selectById(subscribeUserId);

			SubscribeFollowUserVo subscribeFollowUserVo = new SubscribeFollowUserVo();
			BeanUtil.copyProperties(memberPO,subscribeFollowUserVo);

			//是本人的时候，查询是否互关
			if(!isOther){
				//查询是否互关
				MemberFollowPO memberFollowPO = memberFollowMapper.selectOne(new LambdaQueryWrapper<MemberFollowPO>()
					.eq(BasePO::getIsDeleted, 0)
					.eq(MemberFollowPO::getUserId, userId)
					.eq(MemberFollowPO::getSubscribeUserId, x.getUserId()));
				if(memberFollowPO != null){
					//互关
					subscribeFollowUserVo.setIsFollow(1);
				}else {
					//未互关
					subscribeFollowUserVo.setIsFollow(0);
				}
			}

			result.add(subscribeFollowUserVo);
		}

		return R.data(result);
	}

	@GetMapping("/getFollowing")
	@ApiOperation(value = "关注列表（FOLLOWING）")
	public R<List<FollowUserVo>> getFollowing(@ApiParam("用户id：不传的话默认查自己，不登录时userId必传")@RequestParam(value = "userId",required = false) Long userId) {

		List<FollowUserVo> result = new ArrayList<>();
		if(userId == null){
			userId = StpUtil.getLoginIdAsLong();
		}
		//关注
		List<MemberFollowPO> list = memberFollowMapper.selectList(new LambdaQueryWrapper<MemberFollowPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberFollowPO::getUserId, userId));

		list.forEach(x->{
			Long subscribeUserId = x.getSubscribeUserId();
			MemberPO memberPO = memberMapper.selectById(subscribeUserId);

			FollowUserVo followUserVo = new FollowUserVo();
			BeanUtil.copyProperties(memberPO,followUserVo);

			result.add(followUserVo);
		});

		return R.data(result);
	}

	@GetMapping("/getTagsList")
	@ApiOperation(value = "获取用户标签选项list（Tags）")
	public R<List<EnumVo>> getTagsList() {
		List<EnumVo> result = new ArrayList<>();
		for (UserTagsEnum value : UserTagsEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			result.add(enumVo);
		}
		return R.data(result);
	}

	@GetMapping("/getUserInfo")
	@ApiOperation(value = "获取用户信息")
	public R<UserInfoVo> getUserInfo(@ApiParam("用户id：不传的话默认查自己，不登录时userId必传")@RequestParam(value = "userId",required = false) Long userId) {
		//是否是其他人
		boolean isOther = true;
		if(userId == null){
			//是本人
			isOther = false;
			userId = StpUtil.getLoginIdAsLong();
		}

		UserInfoVo userInfoVo = new UserInfoVo();
		MemberPO memberPO = memberMapper.selectById(userId);
		BeanUtil.copyProperties(memberPO,userInfoVo);

		userInfoVo.setIsLoginUser(getIsLoginUser(userId));

		//获取被关注人数
		Long followers = memberFollowMapper.selectCount(new LambdaQueryWrapper<MemberFollowPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberFollowPO::getSubscribeUserId, userId));
		userInfoVo.setFollowers(followers);

		//获取关注人数
		Long following = memberFollowMapper.selectCount(new LambdaQueryWrapper<MemberFollowPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberFollowPO::getUserId, userId));
		userInfoVo.setFollowing(following);

		//是其他人，并且已登录时，查询是否关注
		if(isOther && StpUtil.isLogin()){
			//查询是否互关
			MemberFollowPO memberFollowPO = memberFollowMapper.selectOne(new LambdaQueryWrapper<MemberFollowPO>()
				.eq(BasePO::getIsDeleted, 0)
				.eq(MemberFollowPO::getUserId, StpUtil.getLoginIdAsLong())
				.eq(MemberFollowPO::getSubscribeUserId, userId));
			if(memberFollowPO != null){
				//已经关注
				userInfoVo.setIsFollow(1);
			}else {
				//未关注
				userInfoVo.setIsFollow(0);
			}
		}

		return R.data(userInfoVo);
	}

	private Integer getIsLoginUser(Long userId) {
		Integer isLoginUser = 0;
		boolean login = StpUtil.isLogin();
		if(login){
			Long loginUserId = StpUtil.getLoginIdAsLong();
			if(userId.equals(loginUserId)){
				isLoginUser = 1;
			}
		}
		return isLoginUser;
	}

	@GetMapping("/updateUserScore")
	@ApiOperation(value = "更新用户分数：较为耗时推荐异步")
	public R<UserInfoVo> updateUserScore() {
		Long userId = StpUtil.getLoginIdAsLong();

		userScoreService.updateUserScore(userId);

		return getUserInfo(null);
	}

	@PostMapping("/setUserTags")
	@ApiOperation(value = "设置用户标签(1到12,多个用逗号隔开)")
	public R<UserInfoVo> setUserTags(@Valid @RequestBody UserTagsVo userTagsVo) {
		Long userId = StpUtil.getLoginIdAsLong();

		MemberPO memberPO = memberMapper.selectById(userId);
		memberPO.setUserTags(userTagsVo.getUserTags());

		memberPO.initForUpdate();

		memberMapper.updateById(memberPO);

		return R.success("修改成功");
	}

	@PostMapping("/setUserInfo")
	@ApiOperation(value = "设置用户昵称、头像、bio")
	public R<UserInfoVo> setUserInfo(@Valid @RequestBody UserNameAvatarVo userNameAvatarVo) {

		String avatar = userNameAvatarVo.getAvatar();
		String userName = userNameAvatarVo.getUserName();
		String bio = userNameAvatarVo.getBio();

		if(StringUtil.isNotBlank(avatar) || StringUtil.isNotBlank(userName) || StringUtil.isNotBlank(bio)){
			Long userId = StpUtil.getLoginIdAsLong();

			MemberPO memberPO = memberMapper.selectById(userId);
			if(StringUtil.isNotBlank(avatar)){
				memberPO.setAvatar(avatar);
			}
			if(StringUtil.isNotBlank(userName)){
				memberPO.setUserName(userName);
			}
			if(StringUtil.isNotBlank(bio)){
				memberPO.setBio(bio);
			}

			memberPO.initForUpdate();

			memberMapper.updateById(memberPO);
		}
		return R.success("修改成功");
	}


	@GetMapping("/getMintedNFTPage")
	@ApiOperation(value = "获取我铸造的NFT分页")
	public R<Page<PFPTokenMinePageVo>> getNFTPage(@ApiParam("用户id：不传的话默认查自己，不登录时userId必传")@RequestParam(value = "userId",required = false) Long userId,
												  @ApiParam(value = "当前页",required = true) @RequestParam("current")Integer current,
												  @ApiParam(value = "每页的数量",required = true) @RequestParam("size")Integer size) {
		if(userId == null){
			userId = StpUtil.getLoginIdAsLong();
		}

		Page<PFPTokenPO> page = new Page<>(current,size);
		LambdaQueryWrapper<PFPTokenPO> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BasePO::getIsDeleted,0)
			.eq(PFPTokenPO::getMintStatus,1)
			.eq(PFPTokenPO::getMintUserId,userId)
			.orderByDesc(BasePO::getCreateTime);

		page = pfpTokenMapper.selectPage(page, wrapper);

		Page<PFPTokenMinePageVo> result = new Page<>(current,size);
		BeanUtil.copyProperties(page,result);

		List<PFPTokenMinePageVo> pfpTokenPageVos = BeanUtil.copyProperties(result.getRecords(), PFPTokenMinePageVo.class);
		result.setRecords(pfpTokenPageVos);

		//TODO 设置最高出价
		result.getRecords().forEach(x->{
			x.setTopPick(null);
		});

		return R.data(result);
	}

	@GetMapping("/getCollectNFTPage")
	@ApiOperation(value = "获取我购买的NFT分页")
	public R<Page<PFPTokenMinePageVo>> getCollectNFTPage(@ApiParam("用户id：不传的话默认查自己，不登录时userId必传")@RequestParam(value = "userId",required = false) Long userId,
														 @ApiParam(value = "当前页",required = true) @RequestParam("current")Integer current,
												  		 @ApiParam(value = "每页的数量",required = true) @RequestParam("size")Integer size) {
		if(userId == null){
			userId = StpUtil.getLoginIdAsLong();
		}

		Page<PFPTokenPO> page = new Page<>(current,size);
		LambdaQueryWrapper<PFPTokenPO> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BasePO::getIsDeleted,0)
			.eq(PFPTokenPO::getMintStatus,1)
			//持有者是本人
			.eq(PFPTokenPO::getOwnerUserId,userId)
			//不是本人铸造
			.ne(PFPTokenPO::getMintUserId,userId)
			.orderByDesc(BasePO::getUpdateTime);

		page = pfpTokenMapper.selectPage(page, wrapper);

		Page<PFPTokenMinePageVo> result = new Page<>(current,size);
		BeanUtil.copyProperties(page,result);

		List<PFPTokenMinePageVo> pfpTokenPageVos = BeanUtil.copyProperties(result.getRecords(), PFPTokenMinePageVo.class);
		result.setRecords(pfpTokenPageVos);

		//TODO 设置最高出价
		result.getRecords().forEach(x->{
			x.setTopPick(null);

			//设置购买费用
			PFPTransactionPO lastTransaction = nftService.getLastTransaction(x.getId());
			x.setCostPrice(lastTransaction.getListPrice());

		});

		return R.data(result);
	}

	@PostMapping("/cancelListNFT")
	@ApiOperation(value = "取消出售NFT")
	public R<Page<PFPTokenMinePageVo>> cancelListNFT(@Valid  @RequestBody RequestIdVo requestIdVo) {
		Long tokenId = requestIdVo.getId();
		Long userId = StpUtil.getLoginIdAsLong();

		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		if(pfpTokenPO != null && pfpTokenPO.getMintStatus() == 1 && pfpTokenPO.getOwnerUserId().equals(userId) && pfpTokenPO.getIsDeleted() == 0){
			pfpTokenPO.setPrice(null);
			pfpTokenPO.setPriceTime(null);
			pfpTokenPO.initForUpdate();

			pfpTokenMapper.updateById(pfpTokenPO);

			//TODO 取消所有出价

			return R.success("取消成功");
		}else {
			//TODO 翻译
			return R.fail("请刷新后重试");
		}
	}

//	@PostMapping("/cancelList")
//	@ApiOperation(value = "取消NFT的出价(cancelList)")
//	public R cancelList(@Valid  @RequestBody IdQurey idQurey) {
//		Long tokenId = idQurey.getId();
//
//		Long userId = StpUtil.getLoginIdAsLong();
//		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
//		if(pfpTokenPO != null && pfpTokenPO.getMintStatus() == 1 && pfpTokenPO.getOwnerUserId().equals(userId) && pfpTokenPO.getIsDeleted() == 0){
//
//			pfpTokenPO.setPrice(null);
//			pfpTokenPO.setPriceTime(new Date());
//			pfpTokenPO.initForUpdate();
//
//			pfpTokenMapper.updateById(pfpTokenPO);
//			return R.success("Cancel list success");
//		}else {
//			//TODO 翻译
//			return R.fail("请刷新后重试");
//		}
//	}

	@PostMapping("/listNFT")
	@ApiOperation(value = "设置NFT出售价格：不授权(approve)")
	public R<Page<PFPTokenMinePageVo>> listNFT(@Valid  @RequestBody ListNFTVo listNFTVo) {
		Long tokenId = listNFTVo.getId();
		BigDecimal price = listNFTVo.getPrice();
		price = price.setScale(2, BigDecimal.ROUND_HALF_UP);

		Long userId = StpUtil.getLoginIdAsLong();

		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		if(pfpTokenPO != null && pfpTokenPO.getMintStatus() == 1 && pfpTokenPO.getOwnerUserId().equals(userId) && pfpTokenPO.getIsDeleted() == 0){
			pfpTokenPO.setPrice(price);
			pfpTokenPO.setPriceTime(new Date());
			pfpTokenPO.initForUpdate();

			pfpTokenMapper.updateById(pfpTokenPO);
			return R.success("出价成功");
		}else {
			//TODO 翻译
			return R.fail("请刷新后重试");
		}
	}

	@PostMapping("/listNFTApprove")
	@ApiOperation(value = "设置NFT出售价格：授权(approve)")
	public R listNFTApprove(@Valid  @RequestBody ListNFTVo listNFTVo) {
		Long tokenId = listNFTVo.getId();

		BigDecimal price = listNFTVo.getPrice();
		price = price.setScale(2, BigDecimal.ROUND_HALF_UP);

		Long userId = StpUtil.getLoginIdAsLong();

		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		if(pfpTokenPO != null && pfpTokenPO.getMintStatus() == 1 && pfpTokenPO.getOwnerUserId().equals(userId) && pfpTokenPO.getIsDeleted() == 0){
			//是当前用户的资产

			//校验approve
//			R result = nftService.checkApprove(tokenId,userId);
//			if(result.getCode() != 200){
//				return result;
//			}

			pfpTokenPO.setPrice(price);
			pfpTokenPO.setPriceTime(new Date());
			pfpTokenPO.initForUpdate();

			pfpTokenMapper.updateById(pfpTokenPO);
			return R.success("list success");
		}else {
			return R.fail("please refresh and try again");
		}
	}

	@PostMapping("/testApprove")
	@ApiOperation(value = "testApprove")
	public R testApprove(@RequestParam(value = "txid",required = false)String txid) throws Exception{
		Boolean checkNFTOwner = ethService.checkNFTOwner("0x3d4289432e7B7DE5Ac146f51f7eA48Be8F20341f", 27L);
		System.out.println("checkNFTOwner:"+checkNFTOwner);
//		ethService.approveTransferNFT("0xAd028d3bF652ddab9a7f46d73A20eE24C672e656","0x3d4289432e7B7DE5Ac146f51f7eA48Be8F20341f",27L);
		return null;
	}

}
