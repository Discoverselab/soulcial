package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("铸造VO")
@AllArgsConstructor
@NoArgsConstructor
public class MintNftVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("不规则图片url")
	private String pictureUrl;

	@NotNull
	@ApiModelProperty("方形图片url")
	private String squarePictureUrl;

	@NotNull
	@ApiModelProperty("图片颜色属性")
	private Integer colorAttribute;

	@NotNull
	@ApiModelProperty("personality")
	private Integer personality;

	@NotNull
	@ApiModelProperty("mood")
	private Integer mood;

	@NotNull
	@ApiModelProperty("weather")
	private Integer weather;

	@NotNull
	@ApiModelProperty("color")
	private Integer color;

}
