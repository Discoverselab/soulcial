package org.springblade.modules.admin.service.impl;
import java.math.BigDecimal;
import java.util.Date;

//import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.modules.admin.dao.MemberMapper;
import org.springblade.modules.admin.dao.PFPContractMapper;
import org.springblade.modules.admin.pojo.po.MemberPO;
import org.springblade.modules.admin.pojo.po.PFPContractPO;
import org.springblade.modules.admin.pojo.vo.UserInfoVo;
import org.springblade.modules.admin.service.PfpContractService;
import org.springblade.modules.admin.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserScoreServiceImpl implements UserScoreService {

	private final static int timeout = HttpGlobalConfig.getTimeout();

	private final static String knn3AuthKey = "a97c41edaf7bbcde96dfc31ab15226a2e09c1d15913551f85d56847d3ee10ef8";

	private final static String NFTGOAPIKEY = "875799bb-8645-4b19-a705-eb7299bda7d2";

	private final static String LensFollowingCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/lensFollowingCount";

	private final static String LensFollowersCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/lens/followers/:profileId/count";

	private final static String LensProfileIdsByAddressUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/boundLens";

	private final static String PoapsCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/poaps/count";

	private final static String SnapshotCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/calVotes/";

	private final static String NFTCountUrlKnn3 = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/holdNfts";

	private final static String NFTCountUrlNFTGO = "https://data-api.nftgo.io/eth/v2/address/metrics?address=";

	private final static String CCMainnetUrl = "https://api.cyberconnect.dev/playground";

	private final static String CCTestUrl = "https://api.cyberconnect.dev/testnet/playground";

	@Autowired
	MemberMapper memberMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserScore(Long userId) {
		MemberPO memberPO = memberMapper.selectById(userId);

		Integer oldCharisma = memberPO.getCharisma();
		Integer oldExtroversion = memberPO.getExtroversion();
		Integer oldEnergy = memberPO.getEnergy();
		Integer oldWisdom = memberPO.getWisdom();
		Integer oldArt = memberPO.getArt();
		Integer oldCourage = memberPO.getCourage();

		String address = memberPO.getAddress();

		//getLensProfileIdByAddress
		String lensProfile = getLensProfileIdByAddress(address);
		if(lensProfile != null && lensProfile.length() > 0){
			memberPO.setLensProfile(lensProfile);
		}

		//getLensFollowing
		int lensFollowing = getLensFollowing(address);

		//getLensFollows
		int lensFollows = getLensFollows(lensProfile);

		//getPoapsCount
		int poapsCount = getPoapsCount(address);

		//getSnapshotCount
		int snapshotCount = getSnapshotCount(address);

		//getNFTCount
		int NFTCount = getNFTCount(address);

		//getETHGasTotal(仅集成了ETH)
		BigDecimal ETHGasTotal = getETHGasTotal(address);

		Integer courage = getScoreCourage(ETHGasTotal);
		Integer art = getScoreArt(NFTCount);
		Integer wisdom = getScoreWisdom(snapshotCount);
		Integer energy = getScoreEnergy(poapsCount);
		Integer extroversion = getScoreExtroversion(lensFollowing);
		Integer charisma = getScoreCharisma(lensFollows);

		if(oldCourage == null || oldCourage < courage){
			memberPO.setCourage(courage);
		}
		if(oldArt == null || oldArt < art){
			memberPO.setArt(art);
		}
		if(oldWisdom == null || oldWisdom < wisdom){
			memberPO.setWisdom(wisdom);
		}
		if(oldEnergy == null || oldEnergy < energy){
			memberPO.setEnergy(energy);
		}
		if(oldExtroversion == null || oldExtroversion < extroversion){
			memberPO.setExtroversion(extroversion);
		}
		if(oldCharisma == null || oldCharisma < charisma){
			memberPO.setCharisma(charisma);
		}

		//计算总分
		memberPO.countLevelScore();
		//计算level
		memberPO.countLevel();

		memberPO.initForUpdate();

		memberMapper.updateById(memberPO);
	}

	private Integer getScoreCharisma(int lensFollows) {
		//f1(x)
		int ccFollows = 0;
//		int ccFollows = getCCFollows(handle, address, proxy);

		int count1 = lensFollows + ccFollows;
		double f1 = 0;
		if(count1 == 0){
			f1 = 10;
		}else if(count1 < 100*10000){
			f1 = Math.log(count1) * 6 + 18;
			if(f1 > 100){
				f1 = 100;
			}
		}else {
			f1 = 100;
		}

		log.info("f1:"+f1);

		//f2(x)
		int count2 = 0;

		double f2 = 0;
		if(count2 == 0){
			f2 = 0;
		}else if(count2 <= 148){
			f2 = Math.log(count2) * 16 + 20;
			if(f2 > 100){
				f2 = 100;
			}
		}else {
			f2 = 100;
		}
		log.info("f2:"+f2);

		double x = f1 * 0.8 + f2 * 0.2;

		log.info("getScoreCharisma:"+(int)x);

		return (int)x;
	}

	private Integer getScoreExtroversion(int lensFollowing) {

		//f1(x)
//		int ccFollowing = getCCFollowing(address, proxy);
		int ccFollowing = 0;

		int count1 = lensFollowing + ccFollowing;
		double f1 = 0;
		if(count1 == 0){
			f1 = 20;
		}else if(count1 < 1800){
			f1 = Math.log(count1) * 8 + 40;
			if(f1 > 100){
				f1 = 100;
			}
		}else {
			f1 = 100;
		}

		log.info("f1:"+f1);

		double x = f1 * 0.2;

		log.info("getScoreExtroversion:"+(int)x);

		return (int)x;
	}

	private Integer getScoreEnergy(int poapsCount) {
//		int w3STCount = getW3STCount(address, proxy);
		int w3STCount = 0;

		int count = poapsCount + w3STCount;
		double x = 0;
		if(count == 0){
			x = 20;
		}else if(count < 400){
			x = Math.log(count) * 10 + 40;
			if(x > 100){
				x = 100;
			}
		}else {
			x = 100;
		}

		log.info("getScoreEnergy:"+(int)x);

		return (int)x;
	}

	private Integer getScoreWisdom(int snapshotCount) {

		int count = snapshotCount;
		double x = 0;
		if(count == 0){
			x = 20;
		}else if(count < 100){
			x = Math.log(count) * 13 + 40;
			if(x > 100){
				x = 100;
			}
		}else {
			x = 100;
		}

		log.info("getScoreWisdom:"+(int)x);

		return (int)x;
	}

	private Integer getScoreArt(Integer nftCount) {

		int count = nftCount;
		double x = 0;
		if(count == 0){
			x = 20;
		}else if(count < 220){
			x = Math.log(count) * 11 + 40;
			if(x > 100){
				x = 100;
			}
		}else {
			x = 100;
		}

		log.info("getScoreArt:"+(int)x);

		return (int)x;
	}

	private Integer getScoreCourage(BigDecimal ethGasTotal) {

		BigDecimal sum = ethGasTotal.setScale(0,BigDecimal.ROUND_HALF_UP);
		int count = sum.intValue();

		double x = 0;
		if(count == 0){
			x = 20;
		}else if(count < 8000){
			x = Math.log(count) * 8.8 + 20;
			if(x > 100){
				x = 100;
			}
		}else {
			x = 100;
		}

		log.info("getScoreCourage:"+(int)x);

		return (int)x;
	}


	private static int getLensFollowing(String address) {
		int count = 0;
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);

			//获取lens following
			HttpRequest httpRequest = HttpRequest.get(LensFollowingCountUrl).header("auth-key", knn3AuthKey).form(paramMap).timeout(timeout);
			String body = httpRequest.execute().body();
			log.info("getLensFollowing:body:"+body);
			count = Integer.parseInt(body);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getLensFollowing:count:"+count);
		return count;
	}

	private static String getLensProfileIdByAddress(String address) {
		String lensProfileIds = "";
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);

			HttpRequest httpRequest = HttpRequest.get(LensProfileIdsByAddressUrl).form(paramMap).header("auth-key", knn3AuthKey).timeout(timeout);
			String body = httpRequest.execute().body();

			System.out.println("getLensProfileIdByAddress:body:"+body);

			JSONObject jsonObject = JSONObject.parseObject(body);
			JSONArray list = jsonObject.getJSONArray("list");
			for (int i=0;i<list.size();i++){
				JSONObject profileObj = list.getJSONObject(i);
				Integer profileId = profileObj.getInteger("profileId");
				System.out.println("getLensProfileIdByAddress:profileId:"+profileId);

				lensProfileIds = lensProfileIds + profileId + ",";
			}
			if(lensProfileIds.endsWith(",")){
				lensProfileIds.substring(0,lensProfileIds.length()-1);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getLensProfileIdByAddress:lensProfileIds:"+lensProfileIds);
		return lensProfileIds;
	}

	private static int getLensFollows(String lensProfileIds) {
		int count = 0;
		try {

			if(lensProfileIds == null || lensProfileIds.length() == 0){
				return 0;
			}

			String[] array = lensProfileIds.split(",");
			for (int i=0;i<array.length;i++){
				String profileId = array[i];
				System.out.println("getLensFollows:profileId:"+profileId);
				int lensFollows = getLensFollowsByProfileId(profileId);

				count = count + lensFollows;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getLensFollows:count:"+count);
		return count;
	}

	private static int getLensFollowsByProfileId(String profileId) {
		int count = 0;
		try {
			//获取lens following
			HttpRequest httpRequest = HttpRequest.get(LensFollowersCountUrl.replace(":profileId",profileId)).header("auth-key", knn3AuthKey).timeout(timeout);
			String body = httpRequest.execute().body();

			System.out.println("getLensFollowsByProfileId:body:"+body);
			count = Integer.parseInt(body);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getLensFollowsByProfileId:LensFollows:"+count);
		return count;
	}

	private static int getPoapsCount(String address) {
		int count = 0;
		try {

			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);

			HttpRequest httpRequest = HttpRequest.get(PoapsCountUrl).form(paramMap).header("auth-key", knn3AuthKey).timeout(timeout);
			String body = httpRequest.execute().body();
			System.out.println("getPoapsCount:body:"+body);
			count = Integer.parseInt(body);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getPoapsCount:count:"+count);
		return count;
	}

	private static int getSnapshotCount(String address) {
		int count = 0;
		try {

			HttpRequest httpRequest = HttpRequest.get(SnapshotCountUrl + address).header("auth-key", knn3AuthKey).timeout(timeout);
			String body = httpRequest.execute().body();
			System.out.println("getSnapshotCount:body:"+body);
			count = JSONObject.parseObject(body).getInteger("total");
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getSnapshotCount:count:"+count);
		return count;
	}

	private static int getNFTCount(String address) {
		int count = 0;
		try {
			HttpRequest httpRequest = HttpRequest.get(NFTCountUrlNFTGO + address).header("X-API-KEY", NFTGOAPIKEY).timeout(timeout);
			String body = httpRequest.execute().body();
			System.out.println("getNFTCount:body:"+body);
			JSONObject jsonObject = JSONObject.parseObject(body);
			count = jsonObject.getInteger("nft_num");

		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getNFTCount:count:"+count);
		return count;
	}

	private static BigDecimal getETHGasTotal(String address) {
		//TODO 替换apiKeyToken
		String ethApiKeyToken = "HIMMQA97KPYSC6XGC5GBX5W3NVB9A9JI39";

		BigDecimal ethUsedSum = BigDecimal.ZERO;
		try {

			String ethUrl = "https://api.etherscan.io/api" +
				"?module=account" +
				"&action=txlist" +
				"&address=" + address +
				"&startblock=0" +
				"&endblock=99999999" +
				"&page=1" +
				"&offset=10" +
				"&sort=asc" +
				"&apikey=" + ethApiKeyToken;

			//ethereum 网络
			HttpRequest httpRequest = HttpRequest.get(ethUrl).header("auth-key", knn3AuthKey).timeout(timeout);
			String body = httpRequest.execute().body();
			System.out.println("body:"+body);

			JSONObject jsonObject = JSONObject.parseObject(body);
			JSONArray list = jsonObject.getJSONArray("result");
			for (int i=0;i<list.size();i++){
				JSONObject transactionJsonObj = list.getJSONObject(i);
				String gasUsed = transactionJsonObj.getString("gasUsed");
				String gasPrice = transactionJsonObj.getString("gasPrice");

				BigDecimal ethUsed = new BigDecimal(gasUsed).multiply(new BigDecimal(gasPrice)).divide(new BigDecimal(Math.pow(10,18)),18,BigDecimal.ROUND_HALF_UP);
				System.out.println("getETHGasPrice:ethUsed:"+ethUsed);

				ethUsedSum = ethUsedSum.add(ethUsed);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getETHGasPrice:ethUsedSum:"+ethUsedSum.toString());

		String ETHpriceUrl = "https://api.etherscan.io/api?module=stats&action=ethprice&apikey=" + ethApiKeyToken;
		String ethPriceBody = HttpUtil.get(ETHpriceUrl);
		System.out.println("getETHGasPrice:ethPriceBody:"+ethPriceBody);
		JSONObject jsonObject = JSONObject.parseObject(ethPriceBody);
		String ethPriceStr = jsonObject.getJSONObject("result").getString("ethusd");
		System.out.println("getETHGasPrice:ethPrice:"+ethPriceStr);

		BigDecimal ethPrice = new BigDecimal(ethPriceStr);
		BigDecimal usdtPrice = ethUsedSum.multiply(ethPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println("getETHGasPrice:usdtPrice:"+usdtPrice);
		return usdtPrice;
	}
}
