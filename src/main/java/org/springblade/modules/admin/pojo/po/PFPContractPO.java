package org.springblade.modules.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * tb_pfp_contract实体类
 *
 * @author yuanxx
 *
 */
@Data
@ApiModel("PFP合约PO")
@TableName("tb_pfp_contract")
public class PFPContractPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	*主键
	*/
	private Long id;
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
	 *admin账号文件名
	 */
	private String adminJsonFile;

}
