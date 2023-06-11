package org.springblade.modules.admin.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("PFPtoken分页VO")
@AllArgsConstructor
@NoArgsConstructor
public class PFPTokenPageVo implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 *主键
	 */
	@ApiModelProperty("Token ID")
	private Long id;

//	/**
//	 *合约所在链：0-BNB Chain 1-Ethereum 2-Polygon
//	 */
//	@ApiModelProperty("合约所在链：0-BNB Chain 1-Ethereum 2-Polygon")
//	private Integer linkType;

//	/**
//	 *链名：BNB Chain/Ethereum/Polygon
//	 */
//	@ApiModelProperty("Network(链名)")
//	private String network;

//	/**
//	 *合约地址
//	 */
//	@ApiModelProperty("Contract Address(合约地址)")
//	private String contractAddress;

//	/**
//	 *合约名称
//	 */
//	@ApiModelProperty("合约名称")
//	private String contractName;

//	/**
//	 *代币链上所属地址
//	 */
//	@ApiModelProperty("代币链上所属地址")
//	private String ownerAddress;

//	/**
//	 *代币所属用户ID
//	 */
//	private Long ownerUserId;

//	/**
//	 *铸造状态：0-未铸造 1-已铸造 2-铸造中
//	 */
//	private Integer mintStatus;

//	/**
//	 *代币铸造用户地址
//	 */
//	private String mintUserAddress;

//	/**
//	 *代币铸造用户id
//	 */
//	private Long mintUserId;

//	/**
//	 *铸造交易哈希
//	 */
//	private String mintTxnHash;

//	/**
//	 *铸造时间
//	 */
//	private Date mintTime;

	/**
	 *图片url
	 */
	@ApiModelProperty("图片url")
	private String pictureUrl;

	/**
	 *personality
	 */
	@ApiModelProperty("personality(铸造时选的属性)")
	private Integer personality;

	/**
	 *mood
	 */
	@ApiModelProperty("mood(铸造时选的属性)")
	private Integer mood;

	/**
	 *weather
	 */
	@ApiModelProperty("weather(铸造时选的属性)")
	private Integer weather;

	/**
	 *color
	 */
	@ApiModelProperty("color(铸造时选的属性)")
	private Integer color;

	/**
	 *price
	 */
	@ApiModelProperty("Listing Price(当前出售价格：单位固定为BNB)")
	private BigDecimal price;

	/**
	 *喜欢人数
	 */
	@ApiModelProperty("likes(喜欢人数)")
	private Integer likes;

	/**
	 *level
	 */
	@ApiModelProperty("level(等级)")
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

	@ApiModelProperty("match(匹配度：整数0-100)")
	private Integer match;

//	@ApiModelProperty("Top Pick(当前最高出价)")
//	private Integer topPick;


}
