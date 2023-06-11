package org.springblade.modules.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * tb_pfp_transaction实体类
 *
 * @author yuanxx
 *
 */
@Data
@ApiModel("PFP交易信息PO")
@TableName("tb_pfp_transaction")
public class PFPTransactionPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	*主键
	*/
	private Long id;
	/**
	*代币id
	*/
	private Long tokenId;
	/**
	*admin账号地址
	*/
	private String adminAddress;
	/**
	*合约所在链：0-BNB Chain 1-Ethereum 2-Polygon
	*/
	private Integer linkType;
	/**
	*链名：BNB Chain/Ethereum/Polygon
	*/
	private String network;
	/**
	*合约地址
	*/
	private String contractAddress;
	/**
	*合约名称
	*/
	private String contractName;
	/**
	*卖方地址
	*/
	private String fromAddress;
	/**
	*买方地址
	*/
	private String toAddress;
	/**
	*卖方用户id
	*/
	private Long fromUserId;
	/**
	*买房用户id
	*/
	private Long toUserId;
	/**
	*交易状态：0-未交易 1-已付款未交易PFP 2-交易完成 3-交易取消
	*/
	private Integer transactionStatus;
	/**
	*钱的交易哈希
	*/
	private String moneyTxnHash;
	/**
	*PFP的交易哈希
	*/
	private String pfpTxnHash;
	/**
	*代币铸造用户id
	*/
	private Long mintUserId;
	/**
	*代币铸造用户地址
	*/
	private String mintUserAddress;

}
