package org.springblade.modules.admin.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("购买NFTQurey")
@AllArgsConstructor
@NoArgsConstructor
public class CollectNFTQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("tokenId")
	private Long tokenId;

	@NotBlank
	@ApiModelProperty("转账BNB的交易流水号，先随便传未链上校验")
	private String txn;

	@NotBlank
	@ApiModelProperty("支出BNB的钱包地址")
	private String payAddress;

}
