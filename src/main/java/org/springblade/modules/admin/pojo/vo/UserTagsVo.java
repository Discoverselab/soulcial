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
@ApiModel("用户标签VO")
@AllArgsConstructor
@NoArgsConstructor
public class UserTagsVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户标签：多个用逗号隔开（例：1,3,4）")
	private String userTags;


}
