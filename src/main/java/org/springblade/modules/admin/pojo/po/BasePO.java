package org.springblade.modules.admin.pojo.po;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BasePO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	/**
	 *创建人
	 */
	private Long createUser;
	/**
	 *创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createTime;
	/**
	 *修改人
	 */
	private Long updateUser;
	/**
	 *修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updateTime;
	/**
	 *乐观锁
	 */
	@Version
	private Long version;
	/**
	 *是否已删除 0-正常 1-已删除
	 */
	private Integer isDeleted;

	public void initForInsert() {
		Date date = new Date();
		this.createTime = date;
		this.updateTime = date;
		Long userId = StpUtil.getLoginIdAsLong();
		this.createUser = userId;
		this.updateUser = userId;
		this.isDeleted = 0;
	}



	public void initForUpdate() {
		Date date = new Date();
		this.updateTime = date;
		Long userId = StpUtil.getLoginIdAsLong();
		this.updateUser = userId;
		this.isDeleted = 0;
	}
}
