package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("用户信息VO")
@AllArgsConstructor
@NoArgsConstructor
public class UserScoreInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户钱包地址")
	private String address;

	@ApiModelProperty("用户等级（level）")
	private Integer level;

	@ApiModelProperty("等级分数：整数0-600")
	private Integer levelScore;

	@ApiModelProperty("charisma(6边型算分)")
	private Integer charisma;

	@ApiModelProperty("extroversion(6边型算分)")
	private Integer extroversion;

	@ApiModelProperty("energy(6边型算分)")
	private Integer energy;

	@ApiModelProperty("wisdom(6边型算分)")
	private Integer wisdom;

	@ApiModelProperty("art(6边型算分)")
	private Integer art;

	@ApiModelProperty("courage(6边型算分)")
	private Integer courage;

	@ApiModelProperty("dataverse：stream_id")
	private String streamId;

	@ApiModelProperty("是否为注册：0-否 1-是")
	private Integer isRegister;

	/**
	 * lens账号，多个用逗号隔开
	 */
	@ApiModelProperty("lens账号，多个用逗号隔开")
	private String lensProfile;
}
