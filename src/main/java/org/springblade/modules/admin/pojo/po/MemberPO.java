package org.springblade.modules.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * tb_member实体类
 *
 * @author yuanxx
 *
 */
@Data
@ApiModel("用户信息PO")
@TableName("tb_member")
public class MemberPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	*主键
	*/
	private Long id;
	/**
	*钱包地址
	*/
	private String address;
	/**
	*年龄
	*/
	private Integer age;
	/**
	*性别：0-女 1-男
	*/
	private Integer sex;
	/**
	*联系方式
	*/
	private String phone;
	/**
	*邮箱
	*/
	private String email;
	/**
	*微信/QQ
	*/
	private String contact;
	/**
	*免费铸造：0-未使用 1-已使用
	*/
	private Integer freeMint;
	/**
	 *用户标签（多个用逗号隔开）
	 */
	private String userTags;
	/**
	 * 昵称
	 */
	private String userName;
	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 等级分数：整数0-600
	 */
	private Integer levelScore;

	/**
	 * 用户等级（level）
	 */
	private Integer level;

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

}