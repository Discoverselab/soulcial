package org.springblade.modules.admin.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

@Configuration
public class Web3jConfig  {

	/**
	 * BSC测试网RPC
	 */
	public static final String BSC_RPC_TEST = "https://data-seed-prebsc-1-s1.binance.org:8545/";

	/**
	 * BSC测试网网链ID
	 */
	public static final Long BSC_CHAIN_ID_TEST = 97L;

	/**
	 * BSC正式网RPC
	 */
	public static final String BSC_RPC = "https://bsc-dataseed.binance.org/";

	/**
	 * BSC正式网链ID
	 */
	public static final Long BSC_CHAIN_ID = 56L;

	/**
	 * ETH_SEPOLIA测试网RPC
	 */
//	public static final String ETH_SEPOLIA_RPC = "https://eth-sepolia.g.alchemy.com/v2/VAaFGT2RIlmQ-BLDfaWS9YXwDEZE-aCr";
	public static final String ETH_SEPOLIA_RPC = "https://eth-sepolia.g.alchemy.com/v2/G0t3sPN8quwUxWAPpuQA8p2lDBR3olhM";

	/**
	 * ETH_SEPOLIA测试网链ID
	 */
	public static final Long ETH_SEPOLIA_CHAIN_ID = 11155111L;

	@Bean(name = "bscWeb3j")
    public Web3j bscWeb3j(){
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		builder.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		builder.readTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		OkHttpClient httpClient = builder.build();
		//TODO BSC测试网
		Web3j web3j = Web3j.build(new HttpService(BSC_RPC_TEST, httpClient, false));
		return web3j;
    }


	@Bean(name = "ethWeb3j")
	public Web3j ethWeb3j(){
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		builder.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		builder.readTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		OkHttpClient httpClient = builder.build();
		//TODO ETH_SEPOLIA测试网
		Web3j web3j = Web3j.build(new HttpService(ETH_SEPOLIA_RPC, httpClient, false));
		return web3j;
	}

}
