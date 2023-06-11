package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("枚举VO")
@AllArgsConstructor
@NoArgsConstructor
public class EnumVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("code")
	private Integer code;

	@ApiModelProperty("name")
	private String name;


}
