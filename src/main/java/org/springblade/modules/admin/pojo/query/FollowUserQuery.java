package org.springblade.modules.admin.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("关注/取消关注用户VO")
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("关注类型：0-取消关注 1-关注")
	private Integer followType;

	@NotNull
	@ApiModelProperty("关注的用户id")
	private Long subscribeUserId;


}
