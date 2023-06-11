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
@ApiModel("铸造使用图片VO")
@AllArgsConstructor
@NoArgsConstructor
public class MintPictureVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("图片颜色属性")
	private Integer colorAttribute;

	@NotNull
	@ApiModelProperty("不规则图片url(用于NFT列表页面的展示)")
	private String pictureUrl;

	@NotNull
	@ApiModelProperty("方形图片url(用于铸造NFT的6选1页面)")
	private String squarePictureUrl;


}
