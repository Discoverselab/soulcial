package org.springblade.modules.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * tb_action_detail实体类
 *
 * @author yuanxx
 *
 */
@Data
@ApiModel("收集信息PO")
@TableName("tb_collect_info")
public class CollectInfoPO extends BasePO {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty("学员姓名")
	private String name;

	@ApiModelProperty("年龄")
	private Integer age;

	@ApiModelProperty("性别：0-女 1-男")
	private Integer sex;

	@ApiModelProperty("联系方式")
	private String phone;

	@ApiModelProperty("联系地址")
	private String address;

	@ApiModelProperty("家长微信/QQ")
	private String contact;

	@ApiModelProperty("留言内容")
	private String remark;

}
