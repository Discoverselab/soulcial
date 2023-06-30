package org.springblade.modules.admin.service.impl;

//import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.tool.api.R;
import org.springblade.modules.admin.config.Web3jConfig;
import org.springblade.modules.admin.service.ETHService;
import org.springblade.modules.admin.util.LeaveMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.Contract;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ETHServiceImpl implements ETHService {


    @Autowired
	@Qualifier("ethWeb3j")
    private Web3j web3j;

	@Value("${spring.profiles.active}")
	List<String> activeProfiles;

//	@Autowired
//	RedisTemplate<String,Object> redisTemplate;

//	private final static BigInteger gasLimit = new BigInteger("1000000");
	private final static BigInteger gasLimit = Contract.GAS_LIMIT;

	private final static BigInteger gasPrice = Contract.GAS_PRICE;


//    @Autowired
//    private JsonRpcHttpClient jsonrpcClient;


    private final static String coinCreatePwd = "peic8888";

	//TODO
	private static final String coinKeystorePathWindows = "E:/work/coin";
	private static final String coinKeystorePathLinux = "/opt/peic/keystore/bnb";
	private static final String coinWalletFile = "UTC--2023-06-05T15-42-39.669000000Z--e52e23326668117034a0ec6a288e5bb117b7f2c6.json";

	private static final String coinWalletFile2 = "UTC--2023-06-22T18-49-03.182000000Z--ad028d3bf652ddab9a7f46d73a20ee24c672e656.json";

	//代币合约地址
	private static final String tokenContractAddress = "";

	//admin钱包地址
	private static final String adminAddress = "0xe52e23326668117034a0ec6a288e5bb117b7f2c6";

	//BSC合约地址
//	private static final String contractAddress = "0x37a7860b29ffF81CDE90C9F1cB741186D3290A0D";

	//ETH合约地址
	private static final String contractAddress = "0xa947AF197bD5105d7f7C454139215fc37829cc86";

	public Boolean testApprove(String txid) throws Exception{
		try {
			EthTransaction transaction = web3j.ethGetTransactionByHash(txid).send();
			try {
				if (transaction != null && transaction.getTransaction() != null && transaction.getTransaction().get() != null) {
					Transaction tx = transaction.getTransaction().get();
					if (!tx.getBlockHash().equalsIgnoreCase("0x0000000000000000000000000000000000000000000000000000000000000000")) {
						EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txid).send();
						if (receipt != null && receipt.getTransactionReceipt() != null
									&& receipt.getTransactionReceipt().get() != null
									&& ("0x1").equalsIgnoreCase(receipt.getTransactionReceipt().get().getStatus())) {

							EthTransaction send = web3j.ethGetTransactionByHash(txid).send();
							BigInteger value = send.getTransaction().get().getValue();

							if(value != null){
								//转账金额
								System.out.println("value:"+value);
								BigDecimal transValue = Convert.fromWei(value.toString(), Convert.Unit.ETHER);
							}
							return false;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public R checkApprove(Long tokenId, String address) {
		try {

			//加載NFT
//			LeaveMsg contract = loadAdminContract();

			Credentials credentials = loadCredentials();
			//獲取gasprice
//			BigInteger gasPrice = getGasPrice();
//			log.info("獲取gasPrice成功：" + gasPrice);

			BigInteger gasPrice = Contract.GAS_PRICE;
			ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
			//加載NFT
			LeaveMsg contract = LeaveMsg.load(contractAddress, web3j, credentials, gasProvider);
			log.info("加載NFT成功：" + contract);

			BigInteger token_id = new BigInteger(tokenId.toString());

			//admin地址
			String admin_address = contract.admin().send();

			//链上持有者地址
			String owner_address = contract.ownerOf(token_id).send();
			if(!address.equalsIgnoreCase(owner_address)){
				return R.fail("check owner failed:This nft's owner is not you!");
			}

			//授权地址
			String approved_address = contract.getApproved(token_id).send();
			if(!admin_address.equalsIgnoreCase(approved_address)){
				return R.fail("check approved failed:This nft has not approved!");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return R.success("check success");
	}

	@Override
	public R<Boolean> checkBNBTransacation(String txn, BigDecimal price,String fromAddress,String toAddress) {
		try {
			EthTransaction transaction = web3j.ethGetTransactionByHash(txn).send();
			if (transaction != null && transaction.getTransaction() != null && transaction.getTransaction().get() != null) {
				Transaction tx = transaction.getTransaction().get();
				if (!tx.getBlockHash().equalsIgnoreCase("0x0000000000000000000000000000000000000000000000000000000000000000")) {
					EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txn).send();
					if (receipt != null && receipt.getTransactionReceipt() != null
						&& receipt.getTransactionReceipt().get() != null
						&& ("0x1").equalsIgnoreCase(receipt.getTransactionReceipt().get().getStatus())) {
						//校验付款方、收款方
						if(!tx.getFrom().equalsIgnoreCase(fromAddress) || !tx.getTo().equalsIgnoreCase(toAddress)){
							log.info("付款方、收款方 不正确：from="+tx.getFrom() + ";to="+tx.getTo());
							return R.data(false);
						}

						BigInteger value = tx.getValue();

						if(value != null){
							//转账金额
							System.out.println("value:"+value);
							BigDecimal transValue = Convert.fromWei(value.toString(), Convert.Unit.ETHER);
							//金额相等
							if(transValue.compareTo(price) == 0){
								return R.data(true);
							}
						}
						return R.data(false);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("Exception");
		}
		return R.fail("not success");
	}

	@Override
	public R<String> approveTransferNFT(String fromAddress,String toAddress, Long tokenId) {
		try {
			System.out.println("fromAddress:"+fromAddress);
			System.out.println("toAddress:"+toAddress);
			System.out.println("tokenId:"+tokenId);

//			LeaveMsg leaveMsg = loadAdminContract();
			Credentials credentials = loadCredentials();
			//獲取gasprice
			BigInteger gasPrice = getGasPrice();
			log.info("獲取gasPrice成功：" + gasPrice);
			ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
			//加載NFT
			LeaveMsg contract = LeaveMsg.load(contractAddress, web3j, credentials, gasProvider);
			log.info("加載NFT成功：" + contract);

			BigInteger token_id = new BigInteger(tokenId.toString());

			//轉賬
			TransactionReceipt receipt = contract.transferFrom(fromAddress, toAddress, token_id).send();
			//獲取交易hash
			String transactionHash = receipt.getTransactionHash();
			log.info("approveTransferNFT:transactionHash："+transactionHash);
			if(transactionHash == null){
				return R.fail("transfer nft failed");
			}else{
				return R.data(transactionHash);
			}
		}catch (Exception e){
			e.printStackTrace();
			return R.fail("exception");
		}
	}

	@Override
	public Boolean checkNFTOwner(String toAddress, Long tokenId) {
		try {
//			LeaveMsg leaveMsg = loadAdminContract();

			Credentials credentials = loadCredentials();
			//獲取gasprice
//			BigInteger gasPrice = getGasPrice();
//			log.info("獲取gasPrice成功：" + gasPrice);

			BigInteger gasPrice = Contract.GAS_PRICE;
			ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
			//加載NFT
			LeaveMsg contract = LeaveMsg.load(contractAddress, web3j, credentials, gasProvider);
			log.info("加載NFT成功：" + contract);

			BigInteger token_id = new BigInteger(tokenId.toString());

			String owner = contract.ownerOf(token_id).send();
			if(toAddress.equalsIgnoreCase(owner)){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return false;
	}

	public String createNewWallet(String password) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException, CipherException {
        log.info("====>  Generate new wallet file for BNB.");
        String fileName = WalletUtils.generateNewWalletFile(password, new File(coinKeystorePathWindows), true);
        Credentials credentials = WalletUtils.loadCredentials(password, coinKeystorePathWindows + "/" + fileName);
        String address = credentials.getAddress();
        return address;
    }

    /**
     * 同步余额
     */
    public void syncAddressBalance(String address) throws IOException {
        BigDecimal balance = getBalance(address);
    }


//    public R transferFromWithdrawWallet(String toAddress, BigDecimal amount, boolean sync, String withdrawId) {
//        Account account = accountService.findByName("admin");
//        Optional.ofNullable(account).orElseThrow(() -> new RuntimeException("賬戶信息異常，請聯繫管理員[C001]"));
//        return transfer(coin.getKeystorePath() + "/" + account.getWalletFile(), coin.getWithdrawWalletPassword(), toAddress, amount, sync, withdrawId);
//    }

    public R transfer(String walletFile, String password, String toAddress, BigDecimal amount, boolean sync, String withdrawId) {
        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(password, walletFile);
        } catch (IOException e) {
            log.error("transfer{}", e);
            // 密钥文件异常
            return R.fail(500, "賬戶信息異常，請聯繫管理員[C002]");
        } catch (CipherException e) {
            log.error("transfer{}", e);
            // 解密失败
            return R.fail(500, "賬戶信息異常，請聯繫管理員[C003]");
        }
//        if (sync) {
//            return paymentHandler.transferBNB(credentials, toAddress, amount);
//        } else {
//            paymentHandler.transferBNBAsync(credentials, toAddress, amount, withdrawId);
//            return new MessageResult(0, "提交成功");
//        }
		return R.success("成功");
    }

    public BigDecimal getBalance(String address) throws IOException {
        EthGetBalance getBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        return Convert.fromWei(getBalance.getBalance().toString(), Convert.Unit.ETHER);
    }

    public BigInteger getGasPrice() throws IOException {
        EthGasPrice gasPrice = web3j.ethGasPrice().send();
        BigInteger baseGasPrice = gasPrice.getGasPrice();

		//TODO ETH测试链固定gasPrice
//		BigInteger baseGasPrice = Contract.GAS_PRICE;

//        return new BigDecimal(baseGasPrice).multiply(coin.getGasSpeedUp()).toBigInteger();
        return baseGasPrice;
    }

    public R transferToken(String fromAddress, String toAddress, BigDecimal amount, boolean sync) {
//        BNBAccountDto account = bnbAccountService.findByAddress(fromAddress);\
		//TODO
		String walletFile = "";

        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(coinCreatePwd, coinKeystorePathWindows + "/" + walletFile);
        } catch (IOException e) {
            log.error("transferToken{}", e);
            // 密钥文件异常
            return R.fail(500, "賬戶信息異常，請聯繫管理員[C004]");
        } catch (CipherException e) {
            log.error("transferToken{}", e);
            // 解密失败
            return R.fail(500, "賬戶信息異常，請聯繫管理員[C005]");
        }
//        if (sync) {
//            return paymentHandler.transferToken(credentials, toAddress, amount);
//        } else {
//            paymentHandler.transferTokenAsync(credentials, toAddress, amount, "");
//            return new MessageResult(0, "提交成功");
//        }
		return R.success("成功");
    }

//    public R transferTokenFromWithdrawWallet(String toAddress, BigDecimal amount, boolean sync, String withdrawId) {
//        Credentials credentials;
//		//TODO
//		String WithdrawWalletPassword = "";
//
//        try {
//            //解锁提币钱包
//            credentials = WalletUtils.loadCredentials(WithdrawWalletPassword, coinKeystorePath + "/" + coinWithdrawWallet);
//        } catch (IOException e) {
//            log.error("transferTokenFromWithdrawWallet{}", e);
//            // 密钥文件异常
//            return R.fail(500, "賬戶信息異常，請聯繫管理員[C006]");
//        } catch (CipherException e) {
//            log.error("transferTokenFromWithdrawWallet{}", e);
//            // 解密失败
//            return R.fail(500, "賬戶信息異常，請聯繫管理員[C007]");
//        }
////        if (sync) {
////            return paymentHandler.transferToken(credentials, toAddress, amount);
////        } else {
////            paymentHandler.transferTokenAsync(credentials, toAddress, amount, withdrawId);
////            return new MessageResult(0, "提交成功");
////        }
//		return R.success("成功");
//    }


//    public BigDecimal getTokenBalance(String address) throws IOException {
//        BigInteger balance = BigInteger.ZERO;
//        Function fn = new Function("balanceOf", Arrays.asList(new org.web3j.abi.datatypes.Address(address)), Collections.emptyList());
//        String data = FunctionEncoder.encode(fn);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("to", tokenContractAddress);
//        map.put("data", data);
//        try {
//            String methodName = "eth_call";
//            Object[] params = new Object[]{map, "latest"};
//            String result = jsonrpcClient.invoke(methodName, params, Object.class).toString();
//            if (StringUtils.isNotEmpty(result)) {
//                if ("0x".equalsIgnoreCase(result) || result.length() == 2) {
//                    result = "0x0";
//                }
//                balance = Numeric.decodeQuantity(result);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//            log.info("查询接口ERROR");
//        }
//        return EthConvert.fromWei(new BigDecimal(balance), contract.getUnit());
//    }

    public Boolean isTransactionSuccess(String txid) throws IOException {
        EthTransaction transaction = web3j.ethGetTransactionByHash(txid).send();
        try {
            if (transaction != null && transaction.getTransaction().get() != null) {
                Transaction tx = transaction.getTransaction().get();
                if (!tx.getBlockHash().equalsIgnoreCase("0x0000000000000000000000000000000000000000000000000000000000000000")) {
                    EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txid).send();
                    if (receipt != null && receipt.getTransactionReceipt().get().getStatus().equalsIgnoreCase("0x1")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public String restTemplateForHttpUrl(String url, Map params) {
        RestTemplate restTemplate = new RestTemplate();
        //入参及头文件
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity httpEntity = new HttpEntity(params, headers);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class).getBody();
    }

    /**
     * 获取订单状态
     * @param txid
     * @return status 0:未到账 1：已到账 2：失败
     */
    public Map<String,Object> getTransactionStatus(String txid) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            EthTransaction transaction = web3j.ethGetTransactionByHash(txid).send();
            if (transaction != null && transaction.getTransaction().get() != null) {
                Transaction tx = transaction.getTransaction().get();
                BigInteger gasPrice = tx.getGasPrice();
                if (!tx.getBlockHash().equalsIgnoreCase("0x0000000000000000000000000000000000000000000000000000000000000000")) {
                    EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txid).send();
                    if (receipt != null) {
                        TransactionReceipt transactionReceipt = receipt.getTransactionReceipt().get();
                        BigInteger gasUsed = transactionReceipt.getGasUsed();

                        BigInteger feeInt = gasPrice.multiply(gasUsed);
                        BigDecimal realFee = (new BigDecimal(feeInt.toString())).divide(Convert.Unit.ETHER.getWeiFactor());
                        map.put("realFee",realFee);

                        String status = transactionReceipt.getStatus();
                        if(status.equalsIgnoreCase("0x1")){
                            //已到账
                            map.put("status","1");
                            return map;
                        }else if(status.equalsIgnoreCase("0x0")){
                            //未到账
                            map.put("status","0");
                            return map;
                        }else{
                            //失败
                            map.put("status","2");
                            return map;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status","0");
            return map;
        }
        map.put("status","0");
        return map;
    }


	@Override
	public String createAdminWallet() {
		String walletAddress = "";
		try {
			walletAddress = createNewWallet(coinCreatePwd);
		}catch (Exception e){}
		return walletAddress;
	}

	@Override
	public synchronized R<String> mintNFT(String adminAddress, String contractAddress, String adminJsonFile, String toAddress,Long tokenId) {
		try {
//			LeaveMsg contract = loadAdminContract();

			Credentials credentials = loadCredentials();
			//獲取gasprice
			BigInteger gasPrice = getGasPrice();
			log.info("獲取gasPrice成功：" + gasPrice);
			ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
			//加載NFT
			LeaveMsg contract = LeaveMsg.load(contractAddress, web3j, credentials, gasProvider);
			log.info("加載NFT成功：" + contract);

			//校驗擁有者
//			TransactionReceipt send = contract.admin().send();

			System.out.println("adminAddress:" + adminAddress);
//			System.out.println("fromAddress:" + fromAddress);
			//System.out.println("approvedAddress:"+approvedAddress);
//			if (!adminAddress.equals(fromAddress)) {
//				logger.info("fromAddress不是admin");
//				return new MessageResult(500, "付款地址不是admin");
//			}
			BigInteger productId = new BigInteger(tokenId.toString());
			//鑄造
			log.info("調用safeMint前：toAddress=" + toAddress + ";productId=" + productId);
			TransactionReceipt receipt = contract.safeMint(toAddress, productId).send();
			log.info("調用safeMint成功：" + receipt);
			//獲取交易hash
			String transactionHash = receipt.getTransactionHash();
			log.info("==============NFT鑄造生成交易hash：" + transactionHash);
			if (transactionHash == null) {
				System.out.println("铸造失败");
			} else {
				System.out.println("铸造成功" + transactionHash);
				return R.data(transactionHash);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		//TODO 翻译
		return R.fail("铸造失败，请联系管理员");
	}

//	private LeaveMsg loadAdminContract() throws Exception{
//
//		//獲取密鑰
//		Credentials credentials = loadCredentials();
//		//獲取gasprice
//		BigInteger gasPrice = getGasPrice();
//		log.info("獲取gasPrice成功：" + gasPrice);
//		ContractGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
//		//加載NFT
//		LeaveMsg contract = LeaveMsg.load(contractAddress, web3j, credentials, gasProvider);
//		log.info("加載NFT成功：" + contract);
//
//		return contract;
//	}

	private Credentials loadCredentials() throws Exception{
		String coinKeystorePath = null;
		if (activeProfiles != null && activeProfiles.size() > 0) {
			if (activeProfiles.get(0).equalsIgnoreCase("dev")) {
				coinKeystorePath = coinKeystorePathWindows;
			} else {
				coinKeystorePath = coinKeystorePathLinux;
			}
		}

		//獲取密鑰文件
		String walletFile = coinKeystorePath + "/" + coinWalletFile;
		//獲取密鑰
		Credentials credentials = WalletUtils.loadCredentials(coinCreatePwd, walletFile);

		return credentials;
	}

	@Override
	public R<String> transferBNB(String toAddress,BigDecimal amount) {
		try {
			Credentials credentials = loadCredentials();

			EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING)
				.sendAsync()
				.get();

			BigInteger nonce = ethGetTransactionCount.getTransactionCount();
			BigInteger gasPrice = getGasPrice();
			BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();

			log.info("value={},gasPrice={},gasLimit={},nonce={},address={}", value, gasPrice, gasLimit, nonce, toAddress);
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
				nonce, gasPrice, gasLimit, toAddress, value);

			//TODO ETH测试链
			Long chainId = Web3jConfig.ETH_SEPOLIA_CHAIN_ID;

			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
			String hexValue = Numeric.toHexString(signedMessage);
			EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
			String transactionHash = ethSendTransaction.getTransactionHash();
			log.info("txid = {}", transactionHash);
			if (StringUtils.isEmpty(transactionHash)) {
				return R.fail("transfer bnb failed");
			} else {
				return R.data(transactionHash);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail("exception");
		}
	}
}
