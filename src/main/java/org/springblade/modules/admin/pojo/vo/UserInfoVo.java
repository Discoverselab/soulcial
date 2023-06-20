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
public class UserInfoVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户id")
	private Long id;

	@ApiModelProperty("用户昵称")
	private String userName;

	@ApiModelProperty("用户头像")
	private String avatar;

	@ApiModelProperty("用户标签：多个用逗号隔开")
	private String userTags;

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

}
