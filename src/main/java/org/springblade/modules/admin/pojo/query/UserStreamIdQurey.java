package org.springblade.modules.admin.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("用户StreamIdVO")
@AllArgsConstructor
@NoArgsConstructor
public class UserStreamIdQurey implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@ApiModelProperty("stream_id")
	private String streamId;

}
