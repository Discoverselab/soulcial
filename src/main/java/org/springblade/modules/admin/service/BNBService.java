package org.springblade.modules.admin.service;

import org.springblade.core.tool.api.R;

public interface BNBService {

	R mintPFP(String toAddress);

	String createAdminWallet();

	R<String> mintNFT(String adminAddress, String contractAddress, String adminJsonFile, String toAddress,Long tokenId);

	void testApprove() throws Exception;
}
