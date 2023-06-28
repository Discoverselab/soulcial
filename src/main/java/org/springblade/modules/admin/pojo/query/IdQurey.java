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
@ApiModel("idVO")
@AllArgsConstructor
@NoArgsConstructor
public class IdQurey implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@ApiModelProperty("id")
	private Long id;

}
