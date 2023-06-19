package org.springblade.modules.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * tb_pfp_collect实体类
 *
 * @author yuanxx
 *
 */
@Data
@ApiModel("点赞信息PO")
@TableName("tb_pfp_likes")
public class PFPLikesPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	*主键
	*/
	private Long id;
	/**
	*代币id
	*/
	private Long tokenId;
	/**
	*用户id
	*/
	private Long userId;

}
