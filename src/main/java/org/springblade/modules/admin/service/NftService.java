package org.springblade.modules.admin.service;

import org.springblade.core.tool.api.R;
import org.springblade.modules.admin.pojo.po.PFPTransactionPO;
import org.springblade.modules.admin.pojo.query.CollectNFTQuery;
import org.springblade.modules.admin.pojo.vo.MintNftVo;

public interface NftService {

	R mintFreeNft(MintNftVo mintNftVo);

    R collectNFT(CollectNFTQuery collectNFTQuery);

    R checkApprove(Long tokenId, Long userId);

	PFPTransactionPO getLastTransaction(Long tokenId);

	R collectNFTOnline(CollectNFTQuery collectNFTQuery) throws Exception;
}
