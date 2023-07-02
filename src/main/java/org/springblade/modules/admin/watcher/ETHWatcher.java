package org.springblade.modules.admin.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.modules.admin.service.BNBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
public class ETHWatcher extends Watcher {
    @Autowired
	@Qualifier("ethWeb3j")
    private Web3j web3j;

    @Override
    public void replayBlock(Long startBlockNumber, Long endBlockNumber) {
//        List<Deposit> deposits = new ArrayList<>();
//        try {
//            for (Long i = startBlockNumber; i <= endBlockNumber; i++) {
//                EthBlock block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(i), true).send();
//                if(block == null || block.getBlock() == null || block.getBlock().getTransactions() == null){
//                    continue;
//                }
//                block.getBlock().getTransactions().stream().forEach(transactionResult -> {
//                    EthBlock.TransactionObject transactionObject = (EthBlock.TransactionObject) transactionResult;
//                    Transaction transaction = transactionObject.get();
//
//                    // 扫描主币BNB
//                    // 只有from不是平台地址，to是平台地址的才扫描
//                    if (StringUtils.isNotEmpty(transaction.getTo())
//                            && accountService.isAddressExist(transaction.getTo())
//                            && !transaction.getFrom().equalsIgnoreCase(getCoin().getIgnoreFromAddress())
//                            && !accountService.isAddressExist(transaction.getFrom())) {
//
//                        // eth地址转fb地址
//                        String bech32Address = AddressConverUtil.ethToFbAddress(transaction.getTo());
//
//                        Deposit deposit = new Deposit();
//                        deposit.setTxid(transaction.getHash());
//                        deposit.setBlockHeight(transaction.getBlockNumber().longValue());
//                        deposit.setBlockHash(transaction.getBlockHash());
//                        deposit.setAmount(Convert.fromWei(transaction.getValue().toString(), Convert.Unit.ETHER));
//                        deposit.setAddress(bech32Address); // 使用转换后的fb地址
//                        deposit.setHexAddress(transaction.getTo());
//                        deposit.setTime(Calendar.getInstance().getTime());
//                        deposit.setCoinName("BNB");
//                        deposit.setLinkName("BSC");
//                        deposits.add(deposit);
//                        logger.info("received coin {} at height {}", transaction.getValue(), transaction.getBlockNumber());
//                    }
//
//                    //BigInteger gasPrice = transaction.getGasPrice();// gas费用
//                    //BigInteger gasLimit = transaction.getGas();//gasLimit
//
//                    // 扫描NFT
//                    String input = transaction.getInput();// 地址信息
//                    String contractAddress = transaction.getTo();// contractAddress
//                    //System.out.println("MethodID:"+input.substring(0,10));
//
//                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(input) && input.length() >= 138
//                        //TODO 獲取數據庫中所有的合約地址,校驗合約地址是否是數據庫中的地址
//                        //&& contract.getAddress().equalsIgnoreCase(cAddress)
//                    ) {
//                        try {
//                            String data = input.substring(10);
//                            Function function = new Function(
//                                    "safeTransferFrom",
//                                    Arrays.asList(),
//                                    Arrays.asList(new TypeReference<Address>() {
//                                                  },
//                                            new TypeReference<Address>() {
//                                            },
//                                            new TypeReference<Uint256>() {
//                                            })
//                            );
//
//                            List<Type> params = FunctionReturnDecoder.decode(data, function.getOutputParameters());
//                            // 付NFT地址
//                            String fromAddress = params.get(0).getValue().toString().toLowerCase();
//                            // 得NFT地址
//                            String toAddress = params.get(1).getValue().toString().toLowerCase();
//                            // NFTID
//                            String tokenId = params.get(2).getValue().toString();
//
//                            //System.out.println("fromAddress:" + fromAddress);
//                            //System.out.println("toAddress:" + toAddress);
//                            //System.out.println("tokenId:" + tokenId);
//
//                            //收NFT地址存在，並且付NFT地址不存在
//                            if (accountService.isAddressExist(toAddress)
//                                    && !accountService.isAddressExist(transaction.getFrom())) {
//                                logger.info("received NFT {} at height {} tokenId = {}", transaction.getValue(), transaction.getBlockNumber(),tokenId);
//
//                                //重复充值校验
//                                NftDeposit temp = nftDepositService.getByTxid(transaction.getHash());
//                                if(temp == null){
//                                    //添加到NFT充值表
//                                    NftDeposit nftDeposit = new NftDeposit();
//                                    nftDeposit.setFromAddress(fromAddress);
//                                    nftDeposit.setToAddress(toAddress);
//                                    nftDeposit.setHexAddress(toAddress);
//                                    nftDeposit.setContractAddress(contractAddress);
//                                    nftDeposit.setProductId(Long.parseLong(tokenId));
//                                    nftDeposit.setLinkName("BSC");
//                                    nftDeposit.setTxid(transaction.getHash());
//                                    nftDeposit.setBlockHeight(transaction.getBlockNumber().longValue());
//                                    nftDeposit.setBlockHash(transaction.getBlockHash());
//                                    nftDeposit.setTime(Calendar.getInstance().getTime());
//
//                                    nftDepositService.insert(nftDeposit);
//                                }
//
//
//
//                            }
//
//                            //0x1 成功
//                            /*EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(hash).send();
//                            String status = receipt.getTransactionReceipt().get().getStatus();
//                            System.out.println("status:" + status);*/
//                        }catch (ArrayIndexOutOfBoundsException e){
//                            //解析input失敗，不是NFT交易
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return deposits;
    }


    @Override
    public Long getNetworkBlockHeight() {
        try {
            EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
            Long height = blockNumber.getBlockNumber().longValue();
            return height;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
