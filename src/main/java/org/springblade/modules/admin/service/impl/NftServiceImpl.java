package org.springblade.modules.admin.service.impl;

//import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.api.ResultCode;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.dao.PFPTokenMapper;
import org.springblade.modules.admin.dao.PFPTransactionMapper;
import org.springblade.modules.admin.pojo.enums.UserTagsEnum;
import org.springblade.modules.admin.pojo.po.*;
import org.springblade.modules.admin.pojo.query.CollectNFTQuery;
import org.springblade.modules.admin.pojo.vo.MintNftVo;
import org.springblade.modules.admin.service.BNBService;
import org.springblade.modules.admin.service.NftService;
import org.springblade.modules.admin.service.PfpContractService;
import org.springblade.modules.admin.util.AddressUtil;
import org.springblade.modules.admin.util.LeaveMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NftServiceImpl implements NftService {

	@Autowired
	PfpContractService pfpContractService;

	@Autowired
	MemberMapper memberMapper;

	@Autowired
	PFPTokenMapper pfpTokenMapper;

	@Autowired
	BNBService bnbService;

	@Autowired
	PFPTransactionMapper pfpTransactionMapper;

	@Override
	public synchronized R mintFreeNft(MintNftVo mintNftVo) {
		Long userId = StpUtil.getLoginIdAsLong();
		MemberPO memberPO = memberMapper.selectById(userId);
		if(memberPO == null){
			//TODO 翻译
			return R.fail("用户不存在");
		}

		if(memberPO.getFreeMint() == 1){
			//TODO 翻译
			return R.fail("免费铸造权益已使用");
		}

		String toAddress = memberPO.getAddress();

		//校验钱包地址合法性
		if(!AddressUtil.isETHAddress(toAddress)){
			//TODO 翻译
			return R.fail("账号钱包地址不正确");
		}

		//获取合约
		PFPContractPO contract = pfpContractService.getContract();

		if(contract == null){
			//TODO 翻译
			return R.fail("合约异常");
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

		//TODO 算分
		pfpTokenPO.setCharisma(RandomUtil.randomInt(10,100));
		pfpTokenPO.setExtroversion(RandomUtil.randomInt(10,100));
		pfpTokenPO.setEnergy(RandomUtil.randomInt(10,100));
		pfpTokenPO.setWisdom(RandomUtil.randomInt(10,100));
		pfpTokenPO.setArt(RandomUtil.randomInt(10,100));
		pfpTokenPO.setCourage(RandomUtil.randomInt(10,100));
		pfpTokenPO.setMintUserTags(getRandomTags());

		//计算总分
		pfpTokenPO.setLevelScore(pfpTokenPO.getCharisma() + pfpTokenPO.getExtroversion() + pfpTokenPO.getEnergy() +
			pfpTokenPO.getWisdom() + pfpTokenPO.getArt() + pfpTokenPO.getCourage());

		//设置level
		pfpTokenPO.countLevel();

		pfpTokenMapper.insert(pfpTokenPO);

		//用户免费mint变更为已使用
		memberPO.setFreeMint(1);
		memberPO.initForUpdate();

		memberMapper.updateById(memberPO);

		R<String> result = bnbService.mintNFT(contract.getAdminAddress(),contract.getContractAddress(),contract.getAdminJsonFile(),toAddress,pfpTokenPO.getId());

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

		//校验转账交易是否被使用
		PFPTransactionPO pfpTransactionPO = pfpTransactionMapper.selectOne(new LambdaQueryWrapper<PFPTransactionPO>()
			.eq(BasePO::getIsDeleted, 0)
			.eq(PFPTransactionPO::getMoneyTxnHash, txn));

		if(pfpTransactionPO != null){
			//翻译
			return R.fail("交易hash已被使用");
		}

		//创建订单
		pfpTransactionPO = new PFPTransactionPO();
		pfpTransactionPO.setTokenId(userId);
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
		pfpTransactionPO.setMoneyTxnHash(txn);
//		pfpTransactionPO.setPfpTxnHash();
		pfpTransactionPO.setMintUserId(pfpTokenPO.getMintUserId());
		pfpTransactionPO.setMintUserAddress(pfpTokenPO.getMintUserAddress());

		pfpTransactionPO.initForInsert();
		pfpTransactionMapper.insert(pfpTransactionPO);

		//TODO 链上校验交易哈希是否成功、金额是否正确

		//TODO NFT是否授权校验

		//TODO NFT转账

		//TODO NFT转账校验

		//TODO BNB手续费计算

		//TODO BNB转账

		//TODO BNB转账校验

		pfpTransactionPO.setPfpTxnHash("pfpTxnHash");
		pfpTransactionMapper.updateById(pfpTransactionPO);


		//修改持有人
		pfpTokenPO.setOwnerAddress(toAddress);
		pfpTokenPO.setOwnerUserId(userId);

		pfpTokenPO.initForUpdate();

		pfpTokenMapper.updateById(pfpTokenPO);

		return R.success("success");
	}

	/**
	 * 获得随机标签
	 * @return
	 */
	private static String getRandomTags() {
		List<String> tags = new ArrayList<>();
		for (UserTagsEnum value : UserTagsEnum.values()) {
			int code = value.getCode();
			tags.add(code + "");
		}

		List<String> integers = RandomUtil.randomEleList(tags, 3);

		String userTags = integers.stream().collect(Collectors.joining(","));
		System.out.println("userTags:"+userTags);

		return userTags;
	}

}
