package org.springblade.modules.admin.service.impl;

//import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.api.ResultCode;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.dao.PFPTokenMapper;
import org.springblade.modules.admin.dao.PFPTransactionMapper;
import org.springblade.modules.admin.pojo.po.*;
import org.springblade.modules.admin.pojo.query.CollectCreateOrderQuery;
import org.springblade.modules.admin.pojo.query.CollectNFTQuery;
import org.springblade.modules.admin.pojo.vo.MintNftVo;
import org.springblade.modules.admin.service.BNBService;
import org.springblade.modules.admin.service.ETHService;
import org.springblade.modules.admin.service.NftService;
import org.springblade.modules.admin.service.PfpContractService;
import org.springblade.modules.admin.util.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class NftServiceImpl implements NftService {

	@Autowired
	PfpContractService pfpContractService;

	@Autowired
	MemberMapper memberMapper;

	@Autowired
	PFPTokenMapper pfpTokenMapper;

//	@Autowired
//	BNBService bnbService;

	@Autowired
	ETHService ethService;

	@Autowired
	PFPTransactionMapper pfpTransactionMapper;

	@Override
	public synchronized R mintFreeNft(MintNftVo mintNftVo) {
		Long userId = StpUtil.getLoginIdAsLong();
		MemberPO memberPO = memberMapper.selectById(userId);
		if(memberPO == null){
			return R.fail("user not exist!");
		}

		if(memberPO.getFreeMint() == 1){
			return R.fail("you have used free mint chance!");
		}

		String toAddress = memberPO.getAddress();

		//校验钱包地址合法性
		if(!AddressUtil.isETHAddress(toAddress)){
			return R.fail("address is not illegal");
		}

		//获取合约
		PFPContractPO contract = pfpContractService.getContract();

		if(contract == null){
			return R.fail("contract error");
		}
		//新建token
		PFPTokenPO pfpTokenPO = new PFPTokenPO();

		pfpTokenPO.setAdminAddress(contract.getAdminAddress());
		pfpTokenPO.setLinkType(contract.getLinkType());
		pfpTokenPO.setNetwork(contract.getNetwork());
		pfpTokenPO.setContractAddress(contract.getContractAddress());
		pfpTokenPO.setContractName(contract.getContractName());
		pfpTokenPO.setOwnerAddress(toAddress);
		pfpTokenPO.setOwnerUserId(userId);
		//铸造中
		pfpTokenPO.setMintStatus(2);
		pfpTokenPO.setMintUserAddress(toAddress);
		pfpTokenPO.setMintUserId(userId);
		pfpTokenPO.setPictureUrl(mintNftVo.getPictureUrl());
		pfpTokenPO.setSquarePictureUrl(mintNftVo.getSquarePictureUrl());
		pfpTokenPO.setColorAttribute(mintNftVo.getColorAttribute());
		pfpTokenPO.setMood(mintNftVo.getMood());
		pfpTokenPO.setColor(mintNftVo.getColor());
		pfpTokenPO.setWeather(mintNftVo.getWeather());
		pfpTokenPO.setPersonality(mintNftVo.getPersonality());
		pfpTokenPO.setLikes(0);
		pfpTokenPO.initForInsert();

		//算分
		pfpTokenPO.setCharisma(memberPO.getCharisma());
		pfpTokenPO.setExtroversion(memberPO.getExtroversion());
		pfpTokenPO.setEnergy(memberPO.getEnergy());
		pfpTokenPO.setWisdom(memberPO.getWisdom());
		pfpTokenPO.setArt(memberPO.getArt());
		pfpTokenPO.setCourage(memberPO.getCourage());
		pfpTokenPO.setMintUserTags(memberPO.getUserTags());

		//计算总分
		pfpTokenPO.countLevelScore();

		//设置level
		pfpTokenPO.countLevel();

		pfpTokenMapper.insert(pfpTokenPO);

		//用户免费mint变更为已使用
		memberPO.setFreeMint(1);
		memberPO.initForUpdate();

		memberMapper.updateById(memberPO);

		R<String> result = ethService.mintNFT(contract.getAdminAddress(),contract.getContractAddress(),contract.getAdminJsonFile(),toAddress,pfpTokenPO.getId());

		if(result.getCode() == ResultCode.SUCCESS.getCode()){
			String txnHash = result.getData();

			//已铸造
			pfpTokenPO.setMintStatus(1);
			pfpTokenPO.setMintTime(new Date());
			pfpTokenPO.setMintTxnHash(txnHash);
			pfpTokenPO.initForUpdate();

			pfpTokenMapper.updateById(pfpTokenPO);

			return R.data(pfpTokenPO.getId(),"mint success");

//			PFPTransactionPO pfpTransactionPO = new PFPTransactionPO();
//			pfpTransactionPO.setTokenId(pfpTokenPO.getId());
//			pfpTransactionPO.setAdminAddress(pfpTokenPO.getAdminAddress());
//			pfpTransactionPO.setLinkType(pfpTokenPO.getLinkType());
//			pfpTransactionPO.setNetwork(pfpTokenPO.getNetwork());
//			pfpTransactionPO.setContractAddress(pfpTokenPO.getContractAddress());
//			pfpTransactionPO.setContractName(pfpTokenPO.getContractName());
//			pfpTransactionPO.setFromAddress(pfpTokenPO.getAdminAddress());
//			pfpTransactionPO.setToAddress(pfpTokenPO.getMintUserAddress());
//			pfpTransactionPO.setFromUserId();
//			pfpTransactionPO.setToUserId();
//			pfpTransactionPO.setTransactionStatus();
//			pfpTransactionPO.setMoneyTxnHash();
//			pfpTransactionPO.setPfpTxnHash();
//			pfpTransactionPO.setMintUserId();
//			pfpTransactionPO.setMintUserAddress();
//			pfpTransactionPO.setId();
//			pfpTransactionPO.setCreateUser();
//			pfpTransactionPO.setCreateTime();
//			pfpTransactionPO.setUpdateUser();
//			pfpTransactionPO.setUpdateTime();
//			pfpTransactionPO.setVersion();
//			pfpTransactionPO.setIsDeleted();
//
//			pfpTransactionMapper.insert(pfpTransactionPO);
		}else {
			//铸造失败，退还免费次数
			//用户免费mint变更为未使用
			memberPO.setFreeMint(0);
			memberPO.initForUpdate();

			memberMapper.updateById(memberPO);

			//删除token
			pfpTokenPO.initForUpdate();
			pfpTokenPO.setIsDeleted(1);

			pfpTokenMapper.updateById(pfpTokenPO);

			return result;
		}
	}

	@Override
	public R collectNFT(CollectNFTQuery collectNFTQuery) {
		String txn = collectNFTQuery.getTxn();
		Long tokenId = collectNFTQuery.getTokenId();

		Long userId = StpUtil.getLoginIdAsLong();
		MemberPO memberPO = memberMapper.selectById(userId);
		String toAddress = memberPO.getAddress();

		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		String ownerAddress = pfpTokenPO.getOwnerAddress();
		Long ownerUserId = pfpTokenPO.getOwnerUserId();

		if(pfpTokenPO.getPrice() == null){
			return R.fail("collect is not support");
		}

		//校验转账交易是否被使用
		PFPTransactionPO pfpTransactionPO = pfpTransactionMapper.selectOne(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(PFPTransactionPO::getBuyerMoneyTxnHash, txn));

		if(pfpTransactionPO != null){
			//翻译
			return R.fail("交易hash已被使用");
		}

		//创建订单
		pfpTransactionPO = new PFPTransactionPO();
		pfpTransactionPO.setTokenId(pfpTokenPO.getId());
		pfpTransactionPO.setAdminAddress(pfpTokenPO.getAdminAddress());
		pfpTransactionPO.setLinkType(pfpTokenPO.getLinkType());
		pfpTransactionPO.setNetwork(pfpTokenPO.getNetwork());
		pfpTransactionPO.setContractAddress(pfpTokenPO.getContractAddress());
		pfpTransactionPO.setContractName(pfpTokenPO.getContractName());
		pfpTransactionPO.setFromAddress(ownerAddress);
		pfpTransactionPO.setToAddress(toAddress);
		pfpTransactionPO.setFromUserId(ownerUserId);
		pfpTransactionPO.setToUserId(userId);
//		交易状态：0-未交易 1-已付款未交易PFP 2-交易完成 3-交易取消
		pfpTransactionPO.setTransactionStatus(1);
		pfpTransactionPO.setBuyerMoneyTxnHash(txn);
//		pfpTransactionPO.setPfpTxnHash();
		pfpTransactionPO.setMintUserId(pfpTokenPO.getMintUserId());
		pfpTransactionPO.setMintUserAddress(pfpTokenPO.getMintUserAddress());

		//计算费用
		pfpTransactionPO.setListPrice(pfpTokenPO.getPrice());

		pfpTransactionPO.initForInsert();
		pfpTransactionMapper.insert(pfpTransactionPO);

		//TODO 链上校验交易哈希是否成功、金额是否正确

		//TODO NFT是否授权校验

		//TODO NFT转账

		//TODO NFT转账校验

		//TODO BNB手续费计算

		//TODO BNB转账

		//TODO BNB转账校验

		pfpTransactionPO.setTransactionStatus(2);
		pfpTransactionPO.setPfpTxnHash("pfpTxnHash");
		pfpTransactionMapper.updateById(pfpTransactionPO);


		//修改持有人
		pfpTokenPO.setOwnerAddress(toAddress);
		pfpTokenPO.setOwnerUserId(userId);
		pfpTokenPO.setPrice(null);
		pfpTokenPO.setPriceTime(null);

		pfpTokenPO.initForUpdate();

		pfpTokenMapper.updateById(pfpTokenPO);

		return R.success("success");
	}

	@Override
	public R checkApprove(Long tokenId, Long userId) {
		MemberPO memberPO = memberMapper.selectById(userId);
		String address = memberPO.getAddress();
		R result = ethService.checkApprove(tokenId,address);
		return result;
	}

	@Override
	public PFPTransactionPO getLastTransaction(Long tokenId) {

		//降序获取交易记录
		List<PFPTransactionPO> pfpTransactionPOS = pfpTransactionMapper.selectList(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(PFPTransactionPO::getTokenId, tokenId)
			//已完成
			.eq(PFPTransactionPO::getTransactionStatus, 2)
			.orderByDesc(BasePO::getUpdateTime).last("limit 1"));

		if(pfpTransactionPOS != null && pfpTransactionPOS.size() > 0){
			return pfpTransactionPOS.get(0);
		}else {
			return new PFPTransactionPO();
		}

	}

	//TODO 事务优化
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R collectNFTOnline(CollectNFTQuery collectNFTQuery) throws Exception{
		String txn = collectNFTQuery.getTxn();
		Long tokenId = collectNFTQuery.getTokenId();
		String payAddress = collectNFTQuery.getPayAddress();

		Long userId = StpUtil.getLoginIdAsLong();
		MemberPO memberPO = memberMapper.selectById(userId);
		String toAddress = memberPO.getAddress();

		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		String ownerAddress = pfpTokenPO.getOwnerAddress();
		String minterAddress = pfpTokenPO.getMintUserAddress();
		Long ownerUserId = pfpTokenPO.getOwnerUserId();

		//校验转账交易是否被使用
		PFPTransactionPO pfpTransactionPO = pfpTransactionMapper.selectOne(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(PFPTransactionPO::getBuyerMoneyTxnHash, txn));

		if(pfpTransactionPO != null){
			return R.fail("transaction number :" +  txn + " has been used");
		}

		if(pfpTokenPO.getPrice() == null){
			return R.fail("collect is not support");
		}

		//链上校验交易哈希是否成功、金额是否正确
		int reTryCount = 0;
		Boolean checkFlag = false;
		while (reTryCount < 100){
			R<Boolean> checkBNBTransResult = ethService.checkBNBTransacation(txn, pfpTokenPO.getPrice(),payAddress,pfpTokenPO.getAdminAddress());
			if(checkBNBTransResult.getCode() == 200){
				//终止循环
				reTryCount = 100;
				checkFlag = checkBNBTransResult.getData();
			}else {
				// 休眠3秒再进行重试
				Thread.sleep(3000);
				reTryCount++;
			}
		}

		if(!checkFlag){
			//未校验通过
			return R.fail("bnb transacation check failed: bnb transfer failed!");
		}

		//创建订单
		pfpTransactionPO = new PFPTransactionPO();
		pfpTransactionPO.setTokenId(pfpTokenPO.getId());
		pfpTransactionPO.setAdminAddress(pfpTokenPO.getAdminAddress());
		pfpTransactionPO.setLinkType(pfpTokenPO.getLinkType());
		pfpTransactionPO.setNetwork(pfpTokenPO.getNetwork());
		pfpTransactionPO.setContractAddress(pfpTokenPO.getContractAddress());
		pfpTransactionPO.setContractName(pfpTokenPO.getContractName());
		pfpTransactionPO.setFromAddress(ownerAddress);
		pfpTransactionPO.setToAddress(toAddress);
		pfpTransactionPO.setFromUserId(ownerUserId);
		pfpTransactionPO.setToUserId(userId);
//		交易状态：0-未交易 1-已付款未交易PFP 2-交易完成 3-交易取消
		pfpTransactionPO.setTransactionStatus(1);
		pfpTransactionPO.setBuyerMoneyTxnHash(txn);
//		pfpTransactionPO.setPfpTxnHash();
		pfpTransactionPO.setMintUserId(pfpTokenPO.getMintUserId());
		pfpTransactionPO.setMintUserAddress(pfpTokenPO.getMintUserAddress());

		//计算费用
		pfpTransactionPO.setListPrice(pfpTokenPO.getPrice());

		pfpTransactionPO.initForInsert();
		pfpTransactionMapper.insert(pfpTransactionPO);

		//NFT是否授权校验
		R approveCheckResult = checkApprove(pfpTokenPO.getId(), pfpTokenPO.getOwnerUserId());
		if(approveCheckResult.getCode() != 200){
			return approveCheckResult;
		}

		//NFT转账
		R<String> transferNFTResult = ethService.approveTransferNFT(ownerAddress,toAddress,tokenId);
		if(transferNFTResult.getCode() != 200){
			return transferNFTResult;
		}
		String transferNFTTxn = transferNFTResult.getData();
		pfpTransactionPO.setPfpTxnHash(transferNFTTxn);

		//NFT转账校验
		reTryCount = 0;
		Boolean checkNFTOwner = false;
		while (reTryCount < 10){
			checkNFTOwner = ethService.checkNFTOwner(toAddress,tokenId);
			if(checkNFTOwner){
				//终止循环
				reTryCount = 10;
			}else {
				// 休眠3秒再进行重试
				Thread.sleep(3000);
				reTryCount++;
			}
		}

		if(!checkNFTOwner){
			return R.fail("NFT transfer check failed:NFT owner is not correct");
		}

		//BNB转账：卖NFT收益
		R<String> transferBNBResult = ethService.transferBNB(ownerAddress,pfpTransactionPO.getSellerEarnPrice());
		if(transferBNBResult.getCode() != 200){
			//TODO 稍后再次尝试
		}
		//售卖者收款流水号
		String sellerMoneyTxnHash = transferBNBResult.getData();
		pfpTransactionPO.setSellerMoneyTxnHash(sellerMoneyTxnHash);

		//BNB转账：铸造者收益
		R<String> mintTransferBNBResult = ethService.transferBNB(minterAddress,pfpTransactionPO.getMinterEarnPrice());
		if(mintTransferBNBResult.getCode() != 200){
			//TODO 稍后再次尝试
		}
		//铸造者收益流水号
		String minterMoneyTxnHash = mintTransferBNBResult.getData();
		pfpTransactionPO.setMinterMoneyTxnHash(minterMoneyTxnHash);


		pfpTransactionPO.setTransactionStatus(2);
		pfpTransactionMapper.updateById(pfpTransactionPO);

		//修改持有人
		pfpTokenPO.setOwnerAddress(toAddress);
		pfpTokenPO.setOwnerUserId(userId);
		pfpTokenPO.setPrice(null);
		pfpTokenPO.setPriceTime(null);

		pfpTokenPO.initForUpdate();
		//设置最新成交价
		pfpTokenPO.setLastSale(pfpTransactionPO.getListPrice());
		pfpTokenPO.setLastSaleTime(new Date());

		pfpTokenMapper.updateById(pfpTokenPO);

		return R.success("success");
	}

	/**
	 * 创建订单
	 * @param collectCreateOrderQuery
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R collectCreateOrder(CollectCreateOrderQuery collectCreateOrderQuery) {

		Long userId = StpUtil.getLoginIdAsLong();
		Long tokenId = collectCreateOrderQuery.getTokenId();

		//购买方用户信息
		MemberPO memberPO = memberMapper.selectById(tokenId);

		//获取NFT信息
		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);
		//交易中
		if(pfpTokenPO.getStatus() == 1){
			return R.fail("This PFP is currently being traded");
		}

		BigDecimal price = pfpTokenPO.getPrice();
		//如果价格小于0.01
		if(price == null || price.compareTo(new BigDecimal("0.01")) < 0){
			return R.fail("Price must be greater then or equal to 0.01");
		}

		//校验该用户是否有待付款的订单
		PFPTransactionPO temp = pfpTransactionMapper.selectOne(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(PFPTransactionPO::getToUserId, userId)
			.eq(PFPTransactionPO::getTransactionStatus, 0));

		if(temp != null){
			return R.fail("You have an unpaid order, please make payment or cancel before placing the order");
		}


		//交易中
		pfpTokenPO.setStatus(1);
		pfpTokenMapper.updateById(pfpTokenPO);

		PFPTransactionPO pfpTransactionPO = new PFPTransactionPO();
		//设置价格、收益价格
		pfpTransactionPO.setListPrice(price);
		pfpTransactionPO.setTokenId(tokenId);
		pfpTransactionPO.setAdminAddress(pfpTokenPO.getAdminAddress());
		pfpTransactionPO.setLinkType(pfpTokenPO.getLinkType());
		pfpTransactionPO.setNetwork(pfpTokenPO.getNetwork());
		pfpTransactionPO.setContractAddress(pfpTokenPO.getContractAddress());
		pfpTransactionPO.setContractName(pfpTokenPO.getContractName());

		pfpTransactionPO.setFromAddress(pfpTokenPO.getOwnerAddress());
		pfpTransactionPO.setToAddress(memberPO.getAddress());

		pfpTransactionPO.setFromUserId(pfpTokenPO.getOwnerUserId());
		pfpTransactionPO.setToUserId(userId);
		//未交易，已下单
		pfpTransactionPO.setTransactionStatus(0);

		pfpTransactionPO.setMintUserId(pfpTokenPO.getMintUserId());
		pfpTransactionPO.setMintUserAddress(pfpTokenPO.getMintUserAddress());

		pfpTransactionPO.initForInsert();

		pfpTransactionMapper.insert(pfpTransactionPO);

		return R.success("Create order success");
	}

	/**
	 * 收款验证成功，转NFT以及用户收益
	 */
	@Override
	public R transferNFT(PFPTransactionPO pfpTransactionPO) {
		Long tokenId = pfpTransactionPO.getTokenId();
		PFPTokenPO pfpTokenPO = pfpTokenMapper.selectById(tokenId);

		String ownerAddress = pfpTransactionPO.getFromAddress();
		String toAddress = pfpTransactionPO.getToAddress();

		//NFT是否授权校验
		R approveCheckResult = ethService.checkApprove(tokenId,ownerAddress);
		if(approveCheckResult.getCode() != 200){
			return approveCheckResult;
		}

		//NFT转账
		R<String> transferNFTResult = ethService.approveTransferNFT(ownerAddress,toAddress,tokenId);
		if(transferNFTResult.getCode() != 200){
			return transferNFTResult;
		}
		String transferNFTTxn = transferNFTResult.getData();
		pfpTransactionPO.setPfpTxnHash(transferNFTTxn);

		//NFT转账校验
		int reTryCount = 0;
		Boolean checkNFTOwner = false;
		while (reTryCount < 10){
			checkNFTOwner = ethService.checkNFTOwner(toAddress,tokenId);
			if(checkNFTOwner){
				//终止循环
				reTryCount = 10;
			}else {
				try {
					// 休眠10秒再进行重试
					Thread.sleep(10*000);
				}catch (Exception e){}
				reTryCount++;
			}
		}

		if(!checkNFTOwner){
			return R.fail("NFT transfer check failed:NFT owner is not correct");
		}

		//BNB转账：卖NFT收益
		R<String> transferBNBResult = ethService.transferBNB(ownerAddress,pfpTransactionPO.getSellerEarnPrice());
		if(transferBNBResult.getCode() != 200){
			//TODO 稍后再次尝试
		}
		//售卖者收款流水号
		String sellerMoneyTxnHash = transferBNBResult.getData();
		pfpTransactionPO.setSellerMoneyTxnHash(sellerMoneyTxnHash);

		//BNB转账：铸造者收益
		R<String> mintTransferBNBResult = ethService.transferBNB(pfpTokenPO.getMintUserAddress(),pfpTransactionPO.getMinterEarnPrice());
		if(mintTransferBNBResult.getCode() != 200){
			//TODO 稍后再次尝试
		}
		//铸造者收益流水号
		String minterMoneyTxnHash = mintTransferBNBResult.getData();
		pfpTransactionPO.setMinterMoneyTxnHash(minterMoneyTxnHash);

		pfpTransactionPO.setTransactionStatus(2);
		pfpTransactionMapper.updateById(pfpTransactionPO);

		//修改持有人
		pfpTokenPO.setOwnerAddress(toAddress);
		pfpTokenPO.setOwnerUserId(pfpTransactionPO.getToUserId());
		pfpTokenPO.setPrice(null);
		pfpTokenPO.setPriceTime(null);

		pfpTokenPO.initForUpdate();
		//设置最新成交价
		pfpTokenPO.setLastSale(pfpTransactionPO.getListPrice());
		pfpTokenPO.setLastSaleTime(new Date());

		//交易状态：0-可交易
		pfpTokenPO.setStatus(0);
		pfpTokenMapper.updateById(pfpTokenPO);

		return R.success("transfer success");
	}

	/**
	 * 获得随机标签
	 * @return
	 */
//	private static String getRandomTags() {
//		List<String> tags = new ArrayList<>();
//		for (UserTagsEnum value : UserTagsEnum.values()) {
//			int code = value.getCode();
//			tags.add(code + "");
//		}
//
//		List<String> integers = RandomUtil.randomEleList(tags, 3);
//
//		String userTags = integers.stream().collect(Collectors.joining(","));
//		System.out.println("userTags:"+userTags);
//
//		return userTags;
//	}

}
