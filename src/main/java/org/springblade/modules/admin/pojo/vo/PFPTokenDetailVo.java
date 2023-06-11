package org.springblade.modules.admin.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("PFPtoken详情VO")
@AllArgsConstructor
@NoArgsConstructor
public class PFPTokenDetailVo implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 *主键
	 */
	@ApiModelProperty("Token ID")
	private Long id;

	/**
	 *合约所在链：0-BNB Chain 1-Ethereum 2-Polygon
	 */
	@ApiModelProperty("合约所在链：0-BNB Chain 1-Ethereum 2-Polygon")
	private Integer linkType;

	/**
	 *链名：BNB Chain/Ethereum/Polygon
	 */
	@ApiModelProperty("Blockchain(链名)")
	private String network;

	/**
	 *合约地址
	 */
	@ApiModelProperty("Contract Address(合约地址)")
	private String contractAddress;

//	/**
//	 *合约名称
//	 */
//	@ApiModelProperty("合约名称")
//	private String contractName;

	/**
	 *代币链上所属地址
	 */
	@ApiModelProperty("Owner By(NFT持有者地址)")
	private String ownerAddress;

	/**
	 *代币所属用户ID
	 */
	@ApiModelProperty("NFT持有者id")
	private Long ownerUserId;

	/**
	 *NFT持有者用户昵称
	 */
	@ApiModelProperty("NFT持有者用户昵称")
	private String ownerUserName;

	/**
	 *NFT持有者用户头像
	 */
	@ApiModelProperty("NFT持有者用户头像")
	private String ownerUserAvatar;

//	/**
//	 *铸造状态：0-未铸造 1-已铸造 2-铸造中
//	 */
//	private Integer mintStatus;

	/**
	 *代币铸造用户地址
	 */
	@ApiModelProperty("Created By(铸造人地址)")
	private String mintUserAddress;

	/**
	 *代币铸造用户id
	 */
	@ApiModelProperty("NFT铸造者id")
	private Long mintUserId;

	/**
	 *NFT铸造者用户昵称
	 */
	@ApiModelProperty("NFT铸造者用户昵称")
	private String mintUserName;

	/**
	 *NFT铸造者用户头像
	 */
	@ApiModelProperty("NFT铸造者用户头像")
	private String mintUserAvatar;

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

	@ApiModelProperty("方形图片url")
	private String squarePictureUrl;

	@ApiModelProperty("图片颜色属性")
	private Integer colorAttribute;

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
	@ApiModelProperty("price(出售价格：单位固定为BNB)")
	private BigDecimal price;

	/**
	 *price
	 */
	@ApiModelProperty("设置出售价格的时间")
	private Date priceTime;

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

	/**
	 *等级分数（总分）
	 */
	@ApiModelProperty("等级分数（总分）")
	private Integer levelScore;

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

	@ApiModelProperty("是否为自己铸造的：0-否 1-是")
	private Integer isMineMint;

	@ApiModelProperty("是否为自己持有的：0-否 1-是")
	private Integer isMineOwner;

}
