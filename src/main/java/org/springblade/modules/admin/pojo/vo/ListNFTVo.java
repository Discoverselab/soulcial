package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("出售NFTVO")
@AllArgsConstructor
@NoArgsConstructor
public class ListNFTVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("id")
	private Long id;

	@NotNull
	@ApiModelProperty("price:范围 整数8位，小数2位 共10位")
	private BigDecimal price;


}
