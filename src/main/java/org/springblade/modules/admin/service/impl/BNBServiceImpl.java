package org.springblade.modules.admin.service.impl;

//import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import io.undertow.security.idm.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.tool.api.R;
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
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
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

@Component
@Slf4j
public class BNBServiceImpl {


    @Autowired
	@Qualifier("bscWeb3j")
    private Web3j web3j;


//    @Autowired
//    private JsonRpcHttpClient jsonrpcClient;


    @Value("${coin.create.pwd}")
    private String coinCreatePwd;

	//TODO
	private static final String coinKeystorePath = "";
	private static final String coinWithdrawWallet = "";

	//代币合约地址
	private static final String tokenContractAddress = "";



    public String createNewWallet(String account, String password) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException, CipherException {
        log.info("====>  Generate new wallet file for BNB.");
        String fileName = WalletUtils.generateNewWalletFile(password, new File(coinKeystorePath), true);
        Credentials credentials = WalletUtils.loadCredentials(password, coinKeystorePath + "/" + fileName);
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
//        return new BigDecimal(baseGasPrice).multiply(coin.getGasSpeedUp()).toBigInteger();
        return baseGasPrice;
    }

    public R transferToken(String fromAddress, String toAddress, BigDecimal amount, boolean sync) {
//        BNBAccountDto account = bnbAccountService.findByAddress(fromAddress);\
		//TODO
		String walletFile = "";

        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(coinCreatePwd, coinKeystorePath + "/" + walletFile);
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

    public R transferTokenFromWithdrawWallet(String toAddress, BigDecimal amount, boolean sync, String withdrawId) {
        Credentials credentials;
		//TODO
		String WithdrawWalletPassword = "";

        try {
            //解锁提币钱包
            credentials = WalletUtils.loadCredentials(WithdrawWalletPassword, coinKeystorePath + "/" + coinWithdrawWallet);
        } catch (IOException e) {
            log.error("transferTokenFromWithdrawWallet{}", e);
            // 密钥文件异常
            return R.fail(500, "賬戶信息異常，請聯繫管理員[C006]");
        } catch (CipherException e) {
            log.error("transferTokenFromWithdrawWallet{}", e);
            // 解密失败
            return R.fail(500, "賬戶信息異常，請聯繫管理員[C007]");
        }
//        if (sync) {
//            return paymentHandler.transferToken(credentials, toAddress, amount);
//        } else {
//            paymentHandler.transferTokenAsync(credentials, toAddress, amount, withdrawId);
//            return new MessageResult(0, "提交成功");
//        }
		return R.success("成功");
    }


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

    public BigDecimal getMinerFee(BigInteger gasLimit) throws IOException {
        BigDecimal fee = new BigDecimal(getGasPrice().multiply(gasLimit));
        return Convert.fromWei(fee, Convert.Unit.ETHER);
    }

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
}
