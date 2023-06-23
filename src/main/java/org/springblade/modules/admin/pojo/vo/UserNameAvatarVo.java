package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("用户标签VO")
@AllArgsConstructor
@NoArgsConstructor
public class UserNameAvatarVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户昵称：不传则不更新该字段")
	private String userName;

	@ApiModelProperty("用户头像：不传则不更新该字段")
	private String avatar;

	@ApiModelProperty("用户简介（bio）：不传则不更新该字段")
	private String bio;


}
