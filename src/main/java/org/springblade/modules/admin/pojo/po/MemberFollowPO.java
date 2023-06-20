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
@ApiModel("用户关注PO")
@TableName("tb_member_follow")
public class MemberFollowPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	*主键
	*/
	private Long id;
	/**
	 *用户id
	 */
	private Long userId;
	/**
	 *被关注的用户id
	 */
	private Long subscribeUserId;
}
