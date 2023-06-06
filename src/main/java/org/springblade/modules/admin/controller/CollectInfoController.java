package org.springblade.modules.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.modules.admin.dao.CollectInfoMapper;
import org.springblade.modules.admin.pojo.po.BasePO;
import org.springblade.modules.admin.pojo.po.CollectInfoPO;
import org.springblade.modules.admin.pojo.query.CollectInfoQuery;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/collect")
@Api(value = "管理后台登录",tags = "管理后台登录接口")
@Slf4j
public class CollectInfoController {


	@Resource
	CollectInfoMapper collectInfoMapper;

	private final static int timeout = HttpGlobalConfig.getTimeout();
	private final static String knn3AuthKey = "a97c41edaf7bbcde96dfc31ab15226a2e09c1d15913551f85d56847d3ee10ef8";

	private final static String LensFollowingCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/lensFollowingCount";

	private final static String LensFollowersCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/lens/followers/:profileId/count";

	private final static String PoapsCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/poaps/count";

	private final static String NFTCountUrl = "https://knn3-gateway.knn3.xyz/data-api/api/addresses/holdNfts";


//	@GetMapping("/calculateScore")
//	@ApiOperation(value = "算分")
//	public R addCollect(@ApiParam("查lensFollowers需要的profileId")@RequestParam(value = "profileId",required = false)String profileId,
//						@ApiParam("查lensFollowing需要的address")@RequestParam(value = "address",required = false)String address) {
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		getLensFollowingScore(address,proxy);
//
//		return R.success("添加成功");
//	}


	@GetMapping("/getLensFollowing")
	@ApiOperation(value = "getLensFollowing")
	public R<Integer> getLensFollowing(@ApiParam(value = "查lensFollowing需要的address",required = true)@RequestParam(value = "address")String address) {
		int lensFollowing = getLensFollowing(address, null);
		return R.data(lensFollowing);
	}

	private static int getLensFollowing(String address,Proxy proxy) {
		int count = 0;
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);

			//获取lens following
			HttpRequest httpRequest = HttpRequest.get(LensFollowingCountUrl).header("auth-key", knn3AuthKey).form(paramMap).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println("body"+body);
			count = Integer.parseInt(body);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getLensFollowing:"+count);
		return count;
	}

	@GetMapping("/getLensFollows")
	@ApiOperation(value = "getLensFollows")
	public R<Integer> getLensFollows(@ApiParam(value = "查lensFollowers需要的profileId",required = true)@RequestParam(value = "profileId",required = true)String profileId) {
		int lensFollowing = getLensFollows(profileId, null);
		return R.data(lensFollowing);
	}

	private static int getLensFollows(String profileId,Proxy proxy) {
		int count = 0;
		try {

			//获取lens following
			HttpRequest httpRequest = HttpRequest.get(LensFollowersCountUrl.replace(":profileId","")).header("auth-key", knn3AuthKey).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println("body"+body);
			JSONObject jsonObject = JSONObject.parseObject(body);
			JSONArray list = jsonObject.getJSONArray("list");

			count = list.size();
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getLensFollows:"+count);
		return count;
	}

	@GetMapping("/getCCFollowing")
	@ApiOperation(value = "getCCFollowing")
	public R<Integer> getCCFollowing(@ApiParam(value = "查CCFollowing需要的address",required = true)@RequestParam(value = "address",required = true)String address) {
		int lensFollowing = getCCFollowing(address, null);
		return R.data(lensFollowing);
	}
	private static int getCCFollowing(String address,Proxy proxy) {
		int count = 0;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("query","query getFollowingsByAddressEVM($address: AddressEVM!, ) {\\n      address(address: $address) {\\n        followingCount\\n        followings {\\n          totalCount\\n          edges {\\n            node {\\n              address {\\n                address\\n              }\\n            }\\n          }\\n          pageInfo {\\n            hasPreviousPage\\n            startCursor\\n            hasNextPage\\n          }\\n        }\\n      }\\n    }");
			JSONObject variables = new JSONObject();
			variables.put("address",address);
			jsonObject.put("variables",variables);
			jsonObject.put("operationName","getFollowingsByAddressEVM");

			String bbb = "{\"query\":\"query getFollowingsByAddressEVM($address: AddressEVM!, ) {\\n      address(address: $address) {\\n        followingCount\\n        followings {\\n          totalCount\\n          edges {\\n            node {\\n              address {\\n                address\\n              }\\n            }\\n          }\\n          pageInfo {\\n            hasPreviousPage\\n            startCursor\\n            hasNextPage\\n          }\\n        }\\n      }\\n    }\",\"variables\":{\"address\":\"" +
				address +
				"\"},\"operationName\":\"getFollowingsByAddressEVM\"}";

			System.out.println(jsonObject.toString());
			System.out.println(bbb);

			HttpRequest httpRequest = HttpRequest.post("https://api.cyberconnect.dev/testnet/").body(bbb).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println(body);

			count = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("address").getInteger("followingCount");
			System.out.println("getCCFollowing:"+count);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getCCFollowing:"+count);
		return count;
	}

	@GetMapping("/getCCFollows")
	@ApiOperation(value = "getCCFollows")
	public R<Integer> getCCFollows(@ApiParam(value = "查getCCFollows需要的handle",required = true)@RequestParam(value = "handle",required = true)String handle,
								   @ApiParam(value = "查getCCFollows需要的addressMe",required = true)@RequestParam(value = "addressMe",required = true)String addressMe) {
		int lensFollowing = getCCFollows(handle,addressMe, null);
		return R.data(lensFollowing);
	}

	private static int getCCFollows(String handle,String addressMe,Proxy proxy) {
		int count = 0;
		try {
			String bbb = "{\"query\":\"query getFollowersByHandle($handle: String!, $me: AddressEVM!) {\\n      profileByHandle(handle: $handle) {\\n        followerCount\\n        isFollowedByMe(me: $me)\\n        followers {\\n          totalCount\\n          pageInfo {\\n            hasPreviousPage\\n            startCursor\\n            hasNextPage\\n          }\\n        }\\n      }\\n    }\",\"variables\":{\"handle\":\"" +
				"shiyu" +
				"\",\"me\":\"" +
				"0xD790D1711A9dCb3970F47fd775f2f9A2f0bCc348" +
				"\"},\"operationName\":\"getFollowersByHandle\"}";

			HttpRequest httpRequest = HttpRequest.post("https://api.cyberconnect.dev/testnet/").body(bbb).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println(body);

			count = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("profileByHandle").getInteger("followerCount");
			System.out.println("getCCFollows:"+count);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getCCFollows:"+count);
		return count;
	}

	@GetMapping("/getPoapsCount")
	@ApiOperation(value = "getPoapsCount")
	public R<Integer> getPoapsCount(@ApiParam(value = "查PoapsCount需要的address",required = true)@RequestParam(value = "address",required = true)String address) {
		int lensFollowing = getPoapsCount(address, null);
		return R.data(lensFollowing);
	}

	private static int getPoapsCount(String address,Proxy proxy) {
		int count = 0;
		try {

			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);

			HttpRequest httpRequest = HttpRequest.get(PoapsCountUrl).form(paramMap).header("auth-key", knn3AuthKey).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println(body);
			count = Integer.parseInt(body);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getPoapsCount:"+count);
		return count;
	}

	@GetMapping("/getW3STCount")
	@ApiOperation(value = "getW3STCount")
	public R<Integer> getW3STCount(@ApiParam(value = "查W3STCount需要的address",required = true)@RequestParam(value = "address",required = true)String address) {
		int lensFollowing = getW3STCount(address, null);
		return R.data(lensFollowing);
	}

	private static int getW3STCount(String address,Proxy proxy) {
		int count = 0;
		try {

			String bbb = "{\"query\":\"query getFollowingsByAddressEVM($address: AddressEVM!, ) {\\n      address(address: $address) {\\n        followingCount\\n        followings {\\n          totalCount\\n          edges {\\n            node {\\n              address {\\n                address\\n              }\\n            }\\n          }\\n          pageInfo {\\n            hasPreviousPage\\n            startCursor\\n            hasNextPage\\n          }\\n        }\\n      }\\n    }\",\"variables\":{\"address\":\"" +
				address +
				"\"},\"operationName\":\"getFollowingsByAddressEVM\"}";

			HttpRequest httpRequest = HttpRequest.post("https://api.cyberconnect.dev/testnet/").body(bbb).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println(body);

			count = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("address").getInteger("followingCount");
			System.out.println("getW3STCount:"+count);
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getW3STCount:"+count);
		return count;
	}

	@GetMapping("/getNFTCount")
	@ApiOperation(value = "getNFTCount")
	public R<Integer> getNFTCount(@ApiParam(value = "查NFTCount需要的address",required = true)@RequestParam(value = "address",required = true)String address) {
		int lensFollowing = getNFTCount(address, null);
		return R.data(lensFollowing);
	}
	private static int getNFTCount(String address,Proxy proxy) {
		int count = 0;
		try {
			//ethereum 网络
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);
			paramMap.put("network","ethereum");
			HttpRequest httpRequest = HttpRequest.get(NFTCountUrl).header("auth-key", knn3AuthKey).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.form(paramMap).execute().body();
			System.out.println("body1:"+body);
			JSONObject jsonObject = JSONObject.parseObject(body);
			JSONArray list = jsonObject.getJSONArray("list");

			count = list.size();

			//polygon 网络
			paramMap = new HashMap<String,Object>();
			paramMap.put("address",address);
			paramMap.put("network","ethereum");
			paramMap.put("network","polygon");
			httpRequest = HttpRequest.get(NFTCountUrl).header("auth-key", knn3AuthKey).timeout(timeout);
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			body = httpRequest.form(paramMap).execute().body();
			System.out.println("body2:"+body);
			jsonObject = JSONObject.parseObject(body);
			list = jsonObject.getJSONArray("list");

			count = count + list.size();
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getNFTCount:"+count);
		return count;
	}

	@GetMapping("/getGasTotal")
	@ApiOperation(value = "getGasTotal")
	public R<String> getGasTotal(@ApiParam(value = "查手续费需要的address",required = true)@RequestParam(value = "address",required = true)String address) {
		BigDecimal ethGasPrice = getETHGasPrice(address, null);
		return R.data(ethGasPrice.toString());
	}

	private static BigDecimal getETHGasPrice(String address,Proxy proxy) {
		BigDecimal ethUsedSum = BigDecimal.ZERO;
		try {
			//TODO 替换
			String ethApiKeyToken = "HIMMQA97KPYSC6XGC5GBX5W3NVB9A9JI39";
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
			if(proxy != null){
				httpRequest.setProxy(proxy);
			}
			String body = httpRequest.execute().body();
			System.out.println("body:"+body);

			JSONObject jsonObject = JSONObject.parseObject(body);
			JSONArray list = jsonObject.getJSONArray("result");
			for (int i=0;i<list.size();i++){
				JSONObject transactionJsonObj = list.getJSONObject(i);
				String gasUsed = transactionJsonObj.getString("gasUsed");
				String gasPrice = transactionJsonObj.getString("gasPrice");

				BigDecimal ethUsed = new BigDecimal(gasUsed).multiply(new BigDecimal(gasPrice)).divide(new BigDecimal(Math.pow(10,18)),18,BigDecimal.ROUND_HALF_UP);
				System.out.println("ethUsed:"+ethUsed);

				ethUsedSum = ethUsedSum.add(ethUsed);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		log.info("getETHGasPrice:"+ethUsedSum.toString());

		BigDecimal ethPrice = new BigDecimal("1864.31");
		BigDecimal usdtPrice = ethUsedSum.multiply(ethPrice).setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println("usdtPrice:"+usdtPrice);
		return usdtPrice;
	}

	public static void main(String[] args) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		getCCFollowing("0x148D59faF10b52063071eDdf4Aaf63A395f2d41c",proxy);
//		getCCFollows("shiyu","0xD790D1711A9dCb3970F47fd775f2f9A2f0bCc348",proxy);
//		getPoapsCount("0x148D59faF10b52063071eDdf4Aaf63A395f2d41c",proxy);
//		getLensFollows("4",proxy);
//		getLensFollowing("0x148D59faF10b52063071eDdf4Aaf63A395f2d41c",proxy);
//		getW3STCount("0x148D59faF10b52063071eDdf4Aaf63A395f2d41c",proxy);
//		getNFTCount("0x148D59faF10b52063071eDdf4Aaf63A395f2d41c",proxy);
//		getETHGasPrice("0xc5102fE9359FD9a28f877a67E36B0F050d81a3CC",proxy);


//		scoreCharisma("4","0xD790D1711A9dCb3970F47fd775f2f9A2f0bCc348","shiyu");
	}


	@GetMapping("/scoreCourage")
	@ApiOperation(value = "勇气")
	public  R<Integer> scoreCourage(@ApiParam(value = "address", required = true) @RequestParam(value = "address") String address) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		Proxy proxy = null;
		BigDecimal ethGasPrice = getETHGasPrice(address, proxy);

		BigDecimal sum = ethGasPrice.setScale(0,BigDecimal.ROUND_HALF_UP);
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

		log.info("score:"+(int)x);

		return R.data((int)x);
	}

	@GetMapping("/scoreArt")
	@ApiOperation(value = "品味")
	public  R<Integer> scoreArt(@ApiParam(value = "address", required = true) @RequestParam(value = "address") String address) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		Proxy proxy = null;
		int nftCount = getNFTCount(address, proxy);

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

		log.info("score:"+(int)x);

		return R.data((int)x);
	}



	@GetMapping("/scoreWisdom")
	@ApiOperation(value = "感知")
	public  R<Integer> scoreWisdom(@ApiParam(value = "address", required = true) @RequestParam(value = "address") String address) {
		return R.fail("暂未集成");
	}

	@GetMapping("/scoreEnergy")
	@ApiOperation(value = "精力")
	public  R<Integer> scoreEnergy(@ApiParam(value = "查following需要的address", required = true) @RequestParam(value = "address") String address) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		Proxy proxy = null;

		int poapsCount = getPoapsCount(address, proxy);
		int w3STCount = getW3STCount(address, proxy);

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

		log.info("score:"+(int)x);

		return R.data((int)x);
	}

	@GetMapping("/scoreExtroversion")
	@ApiOperation(value = "外向性")
	public  R<Integer> scoreExtroversion(@ApiParam(value = "查following需要的address", required = true) @RequestParam(value = "address") String address) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		Proxy proxy = null;

		//f1(x)
		int lensFollowing = getLensFollowing(address, proxy);
		int ccFollowing = getCCFollowing(address, proxy);

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
		//TODO
		double x = f1 * 0.2;

		log.info("score:"+(int)x);

		return R.data((int)x);
	}


	@GetMapping("/scoreCharisma")
	@ApiOperation(value = "魅力")
	public  R<Integer> scoreCharisma(@ApiParam(value = "查lensFollowers需要的profileId", required = true) @RequestParam(value = "profileId") String profileId,
										   @ApiParam(value = "查following需要的address", required = true) @RequestParam(value = "address") String address,
										   @ApiParam(value = "查ccFollows需要的handle", required = true) @RequestParam(value = "handle") String handle) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//		Proxy proxy = null;

		//f1(x)
		int lensFollows = getLensFollows(profileId, proxy);
		int ccFollows = getCCFollows(handle, address, proxy);

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
		int lensFollowing = getLensFollowing(address, proxy);
		int ccFollowing = getCCFollowing(address, proxy);

		int count2 = lensFollowing + ccFollowing;
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

		log.info("score:"+(int)x);

		return R.data((int)x);
	}
}
