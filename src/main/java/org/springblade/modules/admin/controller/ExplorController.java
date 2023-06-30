package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.dao.PFPTokenMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.MemberPO;
import org.springblade.modules.admin.pojo.po.PFPTokenPO;
import org.springblade.modules.admin.pojo.query.CollectCreateOrderQuery;
import org.springblade.modules.admin.pojo.query.CollectNFTQuery;
import org.springblade.modules.admin.pojo.vo.PFPTokenDetailVo;
import org.springblade.modules.admin.pojo.vo.PFPTokenPageVo;
import org.springblade.modules.admin.service.NftService;
import org.springblade.modules.admin.util.ScoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/explor")
@Api(value = "NFT列表相关接口(Explor)",tags = "NFT列表相关接口(Explor)")
public class ExplorController {

	@Autowired
	MemberMapper memberMapper;

	@Autowired
	NftService nftService;

	@Autowired
	PFPTokenMapper pfpTokenMapper;


	@GetMapping("/getNFTPage")
	@ApiOperation(value = "获取NFT分页")
	public R<Page<PFPTokenPageVo>> getNFTPage(@ApiParam(value = "排序字段：0-level(默认) 1-match 2-price 3-likes",required = true) @RequestParam("orderColumn") Integer orderColumn,
											  @ApiParam(value = "排序方式：0-降序(默认) 1-升序",required = true) @RequestParam("orderType") Integer orderType,
											  @ApiParam(value = "当前页",required = true) @RequestParam("current")Integer current,
											  @ApiParam(value = "每页的数量",required = true) @RequestParam("size")Integer size) {
		Page<PFPTokenPO> page = new Page<>(current,size);
		LambdaQueryWrapper<PFPTokenPO> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BasePO::getIsDeleted,0)
			.eq(PFPTokenPO::getMintStatus,1);
		if(orderType == 0){
			//降序
			if(orderColumn == 0){
				wrapper.orderByDesc(PFPTokenPO::getLevel);
			} else if (orderColumn == 1) {
				//TODO 相似度匹配
//				wrapper.orderByDesc(PFPTokenPO::getM);
			} else if (orderColumn == 2) {
				wrapper.orderByDesc(PFPTokenPO::getPrice);
			} else if (orderColumn == 3) {
				wrapper.orderByDesc(PFPTokenPO::getLikes);
			}
		}else {
			//升序
			if(orderColumn == 0){
				wrapper.orderByAsc(PFPTokenPO::getLevel);
			} else if (orderColumn == 1) {
				//TODO 相似度匹配
//				wrapper.orderByAsc(PFPTokenPO::getM);
			} else if (orderColumn == 2) {
				wrapper.orderByAsc(PFPTokenPO::getPrice);
			} else if (orderColumn == 3) {
				wrapper.orderByAsc(PFPTokenPO::getLikes);
			}
		}
		page = pfpTokenMapper.selectPage(page, wrapper);

		Page<PFPTokenPageVo> result = new Page<>(current,size);
		BeanUtil.copyProperties(page,result);

		List<PFPTokenPageVo> pfpTokenPageVos = new ArrayList<>();
		if(page.getRecords() != null && page.getRecords().size() > 0 ){
			pfpTokenPageVos = BeanUtil.copyProperties(page.getRecords(), PFPTokenPageVo.class);
		}
		result.setRecords(pfpTokenPageVos);

		boolean isLogin = StpUtil.isLogin();
		MemberPO memberPO = new MemberPO();
		if(isLogin){
			memberPO = memberMapper.selectById(StpUtil.getLoginIdAsLong());
		}

		//TODO 相似度匹配
		for (PFPTokenPageVo x : result.getRecords()) {
			if(isLogin){
				//TODO 计算相似度
				x.setMatch(ScoreUtil.getMatch(memberPO.getUserTags(),
					memberPO.getCharisma(),memberPO.getExtroversion(),memberPO.getEnergy(),
					memberPO.getWisdom(),memberPO.getArt(),memberPO.getCourage(),
					x.getMintUserTags(),
					x.getCharisma(),x.getExtroversion(),x.getEnergy(),
					x.getWisdom(),x.getArt(),x.getCourage()));
			}
		}

		return R.data(result);
	}

	@GetMapping("/getNFTDetail")
	@ApiOperation(value = "获取NFT详情")
	public R<PFPTokenDetailVo> getNFTDetail(@ApiParam(value = "Token ID(分页返回的id字段)",required = true) @RequestParam("id") Long id) {

		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(id);
		PFPTokenDetailVo result = new PFPTokenDetailVo();
		BeanUtil.copyProperties(pfpTokenPO, result);

		//获取持有人信息
		MemberPO mintUser = memberMapper.selectById(pfpTokenPO.getMintUserId());
		MemberPO ownerUser = memberMapper.selectById(pfpTokenPO.getOwnerUserId());

		result.setMintUserName(mintUser.getUserName());
		result.setMintUserAvatar(mintUser.getAvatar());
		result.setOwnerUserName(ownerUser.getUserName());
		result.setOwnerUserAvatar(ownerUser.getAvatar());

		//TODO 设置最高出价
		result.setBestPick(null);

		result.setIsMineMint(0);
		result.setIsMineOwner(0);

		boolean isLogin = StpUtil.isLogin();
		if(isLogin){
			Long userId = StpUtil.getLoginIdAsLong();
			MemberPO memberPO = memberMapper.selectById(userId);

			//TODO 计算相似度
//			result.setMatch(RandomUtil.randomInt(10,80));
			result.setMatch(ScoreUtil.getMatch(memberPO.getUserTags(),
				memberPO.getCharisma(),memberPO.getExtroversion(),memberPO.getEnergy(),
				memberPO.getWisdom(),memberPO.getArt(),memberPO.getCourage(),
				result.getMintUserTags(),
				result.getCharisma(),result.getExtroversion(),result.getEnergy(),
				result.getWisdom(),result.getArt(),result.getCourage()));

			//是否为本人铸造
			if(pfpTokenPO.getMintUserId().equals(userId)){
				result.setIsMineMint(1);
			}

			//是否为本人持有
			if(pfpTokenPO.getOwnerUserId().equals(userId)){
				result.setIsMineOwner(1);
			}
		}

		return R.data(result);
	}


//	@PostMapping("/collectNFT")
//	@ApiOperation(value = "购买NFT(不上链)")
//	public R<PFPTokenDetailVo> collectNFT(@Valid @RequestBody CollectNFTQuery collectNFTQuery) {
//
//		R result = nftService.collectNFT(collectNFTQuery);
//		if(result.getCode() != 200){
//			return result;
//		}
//
//		return getNFTDetail(collectNFTQuery.getTokenId());
//	}

	@PostMapping("/collectNFTOnline")
	@ApiOperation(value = "购买NFT(上链)")
	public R<PFPTokenDetailVo> collectNFTOnline(@Valid @RequestBody CollectNFTQuery collectNFTQuery) throws Exception{

		R result = nftService.collectNFTOnline(collectNFTQuery);
		if(result.getCode() != 200){
			return result;
		}

		return getNFTDetail(collectNFTQuery.getTokenId());
	}

	@PostMapping("/collectCreateOrder")
	@ApiOperation(value = "购买NFT创建订单")
	public R<PFPTokenDetailVo> collectCreateOrder(@Valid @RequestBody CollectCreateOrderQuery collectCreateOrderQuery) throws Exception{

		R result = nftService.collectCreateOrder(collectCreateOrderQuery);
		if(result.getCode() != 200){
			return result;
		}

		return getNFTDetail(collectCreateOrderQuery.getTokenId());
	}
}
