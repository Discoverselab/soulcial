package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("关注用户VO")
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户id")
	private Long id;

	@ApiModelProperty("用户昵称")
	private String userName;

	@ApiModelProperty("用户头像")
	private String avatar;

	@ApiModelProperty("用户钱包地址")
	private String address;

	@ApiModelProperty("lensProfile:lens账号id")
	private String lensProfile;

//	@ApiModelProperty("likes数量")
//	private Integer likesCount;

}
