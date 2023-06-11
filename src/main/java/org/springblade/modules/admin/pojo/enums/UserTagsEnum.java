package org.springblade.modules.admin.pojo.enums;


public enum UserTagsEnum {

    /**
     * GameFi
	 */
	GameFi(1,"GameFi"),

	/**
	 * Storage
	 */
	Storage(2,"Storage"),

	/**
	 * DAO
	 */
	DAO(3,"DAO"),

	/**
	 * Layer 1&2
	 */
	Layer1And2(4,"Layer 1&2"),

	/**
	 * DeFi
	 */
	DeFi(5,"DeFi"),

	/**
	 * NFT
	 */
	NFT(6,"NFT"),

	/**
	 * DEX
	 */
	DEX(7,"DEX"),

	/**
	 * AI
	 */
	AI(8,"AI"),

	/**
	 * Metaverse
	 */
	Metaverse(9,"Metaverse"),

	/**
	 * ZK
	 */
	ZK(10,"ZK"),

	/**
	 * Wallet
	 */
	Wallet(11,"Wallet"),

	/**
	 * SocialFi
	 */
	SocialFi(12,"SocialFi");


	private int code;
	private String name;

	UserTagsEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	};
	public String getName() {
		return name;
	};

}
