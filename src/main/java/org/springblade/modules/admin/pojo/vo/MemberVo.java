package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel("用户VO")
@AllArgsConstructor
@NoArgsConstructor
public class MemberVo implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("请求头参数名：把该字段的返回值作为参数名放置于请求头中,值为tokenValue字段的返回值")
	private String tokenName;

	@ApiModelProperty("tokenValue")
	private String tokenValue;

	@ApiModelProperty("免费铸造：0-未使用 1-已使用")
	private Integer freeMint;

	@ApiModelProperty("钱包地址")
	private String address;

	/**
	 * 登录类型：0-钱包 1-particle
	 */
	@ApiModelProperty("登录类型：0-钱包 1-particle")
	Integer loginType;

	/**
	 * particleType类型：传数字每个数字分别代表一种类型
	 */
	@ApiModelProperty("particleType类型：传数字每个数字分别代表一种类型")
	Integer particleType;

}
