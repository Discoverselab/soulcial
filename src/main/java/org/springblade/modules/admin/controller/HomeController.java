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
import org.springblade.modules.admin.pojo.enums.NFTColorEnum;
import org.springblade.modules.admin.pojo.enums.UserTagsEnum;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.MemberFollowPO;
import org.springblade.modules.admin.pojo.po.MemberPO;
import org.springblade.modules.admin.pojo.po.PFPTokenPO;
import org.springblade.modules.admin.pojo.query.FollowUserQuery;
import org.springblade.modules.admin.pojo.query.UserStreamIdQurey;
import org.springblade.modules.admin.pojo.vo.*;
import org.springblade.modules.admin.service.BNBService;
import org.springblade.modules.admin.service.NftService;
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
	BNBService bnbService;

	@Autowired
	MemberMapper memberMapper;

	@Autowired
	NftService nftService;

	@Autowired
	PFPTokenMapper pfpTokenMapper;

	@Autowired
	MemberFollowMapper memberFollowMapper;


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
	public R<List<SubscribeFollowUserVo>> getFollowers() {

		List<SubscribeFollowUserVo> result = new ArrayList<>();
		Long userId = StpUtil.getLoginIdAsLong();
		//关注
		List<MemberFollowPO> list = memberFollowMapper.selectList(new LambdaQueryWrapper<MemberFollowPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(MemberFollowPO::getSubscribeUserId, userId));

		list.forEach(x->{
			Long subscribeUserId = x.getSubscribeUserId();
			MemberPO memberPO = memberMapper.selectById(subscribeUserId);

			SubscribeFollowUserVo subscribeFollowUserVo = new SubscribeFollowUserVo();
			BeanUtil.copyProperties(memberPO,subscribeFollowUserVo);

			//查询是否互关
			MemberFollowPO memberFollowPO = memberFollowMapper.selectOne(new LambdaQueryWrapper<MemberFollowPO>()
				.eq(BasePO::getIsDeleted, 0)
				.eq(MemberFollowPO::getUserId, userId)
				.eq(MemberFollowPO::getSubscribeUserId, x.getUserId()));
			if(memberFollowPO != null){
				//互关
				subscribeFollowUserVo.setIsFollow(1);
			}

			result.add(subscribeFollowUserVo);
		});

		return R.data(result);
	}

	@GetMapping("/getFollowing")
	@ApiOperation(value = "关注列表（FOLLOWING）")
	public R<List<FollowUserVo>> getFollowing() {

		List<FollowUserVo> result = new ArrayList<>();
		Long userId = StpUtil.getLoginIdAsLong();
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
	public R<UserInfoVo> getUserInfo() {
		//TODO
		Long userId = StpUtil.getLoginIdAsLong();

		UserInfoVo userInfoVo = new UserInfoVo();
		MemberPO memberPO = memberMapper.selectById(userId);
		BeanUtil.copyProperties(memberPO,userInfoVo);

		return R.data(userInfoVo);
	}

	@PostMapping("/setUserStreamId")
	@ApiOperation(value = "设置dataverse的stream_id")
	public R setUserStreamId(@Valid @RequestBody UserStreamIdQurey userStreamIdQurey) {

		Long userId = StpUtil.getLoginIdAsLong();
		MemberPO memberPO = memberMapper.selectById(userId);
		if(StringUtil.isNotBlank(memberPO.getStreamId())){
			//TODO 翻译
			return R.fail("该用户已设置过stream_id");
		}
		memberPO.setStreamId(userStreamIdQurey.getStreamId());
		memberMapper.updateById(memberPO);

		return R.success("success");
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
	@ApiOperation(value = "设置用户昵称和头像")
	public R<UserInfoVo> setUserInfo(@Valid @RequestBody UserNameAvatarVo userNameAvatarVo) {

		String avatar = userNameAvatarVo.getAvatar();
		String userName = userNameAvatarVo.getUserName();

		if(StringUtil.isNotBlank(avatar) && StringUtil.isNotBlank(userName)){
			Long userId = StpUtil.getLoginIdAsLong();

			MemberPO memberPO = memberMapper.selectById(userId);
			if(StringUtil.isNotBlank(avatar)){
				memberPO.setAvatar(avatar);
			}
			if(StringUtil.isNotBlank(userName)){
				memberPO.setUserName(userName);
			}

			memberPO.initForUpdate();

			memberMapper.updateById(memberPO);
		}
		return R.success("修改成功");
	}


	@GetMapping("/getMintedNFTPage")
	@ApiOperation(value = "获取我铸造的NFT分页")
	public R<Page<PFPTokenMinePageVo>> getNFTPage(@ApiParam(value = "当前页",required = true) @RequestParam("current")Integer current,
												  @ApiParam(value = "每页的数量",required = true) @RequestParam("size")Integer size) {
		Long userId = StpUtil.getLoginIdAsLong();

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

	@PostMapping("/listNFT")
	@ApiOperation(value = "设置NFT出售价格")
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

	@PostMapping("/testApprove")
	@ApiOperation(value = "testApprove")
	public R testApprove() throws Exception{
		bnbService.testApprove();
		return null;
	}

}
