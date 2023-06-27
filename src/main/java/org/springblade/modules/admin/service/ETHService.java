package org.springblade.modules.admin.service;

import org.springblade.core.tool.api.R;

import java.math.BigDecimal;

public interface ETHService {

	R mintPFP(String toAddress);

	String createAdminWallet();

	R<String> mintNFT(String adminAddress, String contractAddress, String adminJsonFile, String toAddress,Long tokenId);

	Boolean testApprove(String txid) throws Exception;

    R checkApprove(Long tokenId, String address);

	R<Boolean> checkBNBTransacation(String txn, BigDecimal price,String fromAddress,String toAddress);

	R<String> approveTransferNFT(String fromAddress,String toAddress, Long tokenId);

	Boolean checkNFTOwner(String toAddress, Long tokenId);

	R<String> transferBNB(String ownerAddress, BigDecimal sellerEarnPrice);
}
