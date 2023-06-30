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
@ApiModel("购买NFT创建订单Qurey")
@AllArgsConstructor
@NoArgsConstructor
public class CollectCreateOrderQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("tokenId")
	private Long tokenId;

}
