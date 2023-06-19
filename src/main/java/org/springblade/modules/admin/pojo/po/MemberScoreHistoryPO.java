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
@ApiModel("用户分数历史记录表PO")
@TableName("tb_member_score_history")
public class MemberScoreHistoryPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	* 用户id
	*/
	private Long userId;
	/**
	*钱包地址
	*/
	private String address;

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

	public void countLevel(){
		if(this.levelScore == null || this.levelScore < 200){
			this.level = 1;
		} else if(this.levelScore < 300){
			this.level = 2;
		} else if(this.levelScore < 400){
			this.level = 3;
		} else if(this.levelScore < 500){
			this.level = 4;
		} else {
			this.level = 5;
		}
	}
}
