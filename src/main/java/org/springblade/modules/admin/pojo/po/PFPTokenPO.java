package org.springblade.modules.admin.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * tb_pfp_token实体类
 *
 * @author yuanxx
 *
 */
@Data
@ApiModel("PFPtokenPO")
@TableName("tb_pfp_token")
public class PFPTokenPO extends BasePO {

	private static final long serialVersionUID = 1L;

	/**
	*主键
	*/
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	*admin账号地址
	*/
	private String adminAddress;
	/**
	*合约所在链：0-BNB Chain 1-Ethereum 2-Polygon
	*/
	private Integer linkType;
	/**
	*链名：BNB Chain/Ethereum/Polygon
	*/
	private String network;
	/**
	*合约地址
	*/
	private String contractAddress;
	/**
	*合约名称
	*/
	private String contractName;
	/**
	*代币链上所属地址
	*/
	private String ownerAddress;
	/**
	*代币所属用户ID
	*/
	private Long ownerUserId;
	/**
	*铸造状态：0-未铸造 1-已铸造 2-铸造中
	*/
	private Integer mintStatus;
	/**
	*代币铸造用户地址
	*/
	private String mintUserAddress;
	/**
	*代币铸造用户id
	*/
	private Long mintUserId;
	/**
	 *铸造交易哈希
	 */
	private String mintTxnHash;
	/**
	 *铸造时间
	 */
	private Date mintTime;
	/**
	 *图片url
	 */
	private String pictureUrl;

	/**
	 *personality
	 */
	private Integer personality;

	/**
	 *mood
	 */
	private Integer mood;

	/**
	 *weather
	 */
	private Integer weather;

	/**
	 *color
	 */
	private Integer color;

	/**
	 *出售价格
	 */
	private BigDecimal price;

	/**
	 *设置出售价格的时间
	 */
	private Date priceTime;

	/**
	 *喜欢人数
	 */
	private Integer likes;

	/**
	 *level
	 */
	private Integer level;

	/**
	 *level
	 */
	private Integer levelScore;

	private Integer charisma;
	private Integer extroversion;
	private Integer energy;
	private Integer wisdom;
	private Integer art;
	private Integer courage;

	public void setLevel(){
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
