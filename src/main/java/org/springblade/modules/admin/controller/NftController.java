package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.pojo.enums.*;
import org.springblade.modules.admin.pojo.po.MemberPO;
import org.springblade.modules.admin.pojo.vo.EnumVo;
import org.springblade.modules.admin.pojo.vo.MintNftVo;
import org.springblade.modules.admin.pojo.vo.MintPictureVo;
import org.springblade.modules.admin.service.NftService;
import org.springblade.modules.admin.util.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/nft")
@Api(value = "铸造NFT",tags = "铸造NFT")
public class NftController {


	@Autowired
	MemberMapper memberMapper;

	@Autowired
	NftService nftService;


//	@PostMapping("/mintPFP")
//	@ApiOperation(value = "铸造PFP")
//	public R addCollect(@RequestParam("toAddress") String toAddress) {
////		Long userId = StpUtil.getLoginIdAsLong();
//
//		bnbService.mintPFP(toAddress);
//
//		return R.success("铸造成功");
//	}

//	@PostMapping("/createAdminAddress")
//	@ApiOperation(value = "创建平台钱包地址")
//	public R createAdminAddress() {
//		String adminWallet = bnbService.createAdminWallet();
//		return R.data(adminWallet);
//	}


	@GetMapping("/getNFTPersonality")
	@ApiOperation(value = "获取NFT角色下拉框（personality）")
	public R<List<EnumVo>> getNFTPersonality() {
		List<EnumVo> result = new ArrayList<>();
		for (NFTPersonalityEnum value : NFTPersonalityEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			result.add(enumVo);
		}
		return R.data(result);
	}

	@GetMapping("/getNFTLevel")
	@ApiOperation(value = "获取NFT等级下拉框（level）")
	public R<List<EnumVo>> getNFTLevel() {
		List<EnumVo> result = new ArrayList<>();
		for (NFTLevelEnum value : NFTLevelEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			result.add(enumVo);
		}
		return R.data(result);
	}

	@GetMapping("/getNFTMood")
	@ApiOperation(value = "获取NFTmood下拉框（mood）")
	public R<List<EnumVo>> getNFTMood() {
		List<EnumVo> result = new ArrayList<>();
		for (NFTMoodEnum value : NFTMoodEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			result.add(enumVo);
		}
		return R.data(result);
	}

	@GetMapping("/getNFTWeather")
	@ApiOperation(value = "获取NFTWeather下拉框（Weather）")
	public R<List<EnumVo>> getNFTWeather() {
		List<EnumVo> result = new ArrayList<>();
		for (NFTWeatherEnum value : NFTWeatherEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			result.add(enumVo);
		}
		return R.data(result);
	}

	@GetMapping("/getNFTColor")
	@ApiOperation(value = "获取NFTColor下拉框（Color）")
	public R<List<EnumVo>> getNFTColor() {
		List<EnumVo> result = new ArrayList<>();
		for (NFTColorEnum value : NFTColorEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			result.add(enumVo);
		}
		return R.data(result);
	}

	public static void main(String[] args) {
		List<EnumVo> result = new ArrayList<>();
		for (NFTLevelEnum value : NFTLevelEnum.values()) {
			EnumVo enumVo = new EnumVo();
			BeanUtil.copyProperties(value,enumVo);
			System.out.println(enumVo.getCode());
			System.out.println(enumVo.getName());
			result.add(enumVo);
		}

	}

	@GetMapping("/getMintPicture")
	@ApiOperation(value = "获取铸造NFT的图片（返回6张图片）")
	public R<List<MintPictureVo>> getMintPicture(@ApiParam(value = "personality:传1到16",required = true) @RequestParam("personality") Integer personality,
												 @ApiParam(value = "mood:不知道有哪些，先传个1吧",required = true) @RequestParam("mood") Integer mood,
												 @ApiParam(value = "weather:不知道有哪些，先传个1吧",required = true) @RequestParam("weather") Integer weather,
												 @ApiParam(value = "color:不知道有哪些，先传个1吧",required = true) @RequestParam("color") Integer color) {
		//TODO 需要生成图片，目前写死
		List<MintPictureVo> result = new ArrayList<>();
		for (int i=0;i<6;i++){
			MintPictureVo mintPictureVo = new MintPictureVo();


			if(i == 0){
				mintPictureVo.setSquarePictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/12/1667935016998465536.png");
				mintPictureVo.setPictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/12/1667935749877592064.png");
				mintPictureVo.setColorAttribute(166);
			} else if (i == 1) {
				mintPictureVo.setSquarePictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603439399104512.png");
				mintPictureVo.setPictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603329093103616.png");
				mintPictureVo.setColorAttribute(-42);
			} else if(i == 2){
				mintPictureVo.setSquarePictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603730555105280.png");
				mintPictureVo.setPictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603551709982720.png");
				mintPictureVo.setColorAttribute(20);
			} else if (i == 3) {
				mintPictureVo.setSquarePictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603875980013568.png");
				mintPictureVo.setPictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603822204841984.png");
				mintPictureVo.setColorAttribute(-26);
			} else if(i == 4){
				mintPictureVo.setSquarePictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668604008863952896.png");
				mintPictureVo.setPictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668603945051811840.png");
				mintPictureVo.setColorAttribute(41);
			} else if (i == 5) {
				mintPictureVo.setSquarePictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668604170780864512.png");
				mintPictureVo.setPictureUrl("https://sfhmaster-1313464417.cos.ap-nanjing.myqcloud.com/2023/06/13/1668604086018174976.png");
				mintPictureVo.setColorAttribute(-15);
			}

			result.add(mintPictureVo);
		}
		return R.data(result);
	}

	@PostMapping("/mintFreeNft")
	@ApiOperation(value = "铸造免费NFT")
	public R<Long> mintFreeNft(@Valid @RequestBody MintNftVo mintNftVo) {
		R result = nftService.mintFreeNft(mintNftVo);
		return result;
	}

//	@GetMapping("/getNFTPage")
//	@ApiOperation(value = "铸造免费NFT")
//	public R mintFreeNft(@RequestBody MintNftVo mintNftVo) {
//		R result = nftService.mintFreeNft(mintNftVo);
//		return result;
//	}
}
