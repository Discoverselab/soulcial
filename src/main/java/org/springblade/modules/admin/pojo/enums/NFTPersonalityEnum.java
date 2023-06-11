package org.springblade.modules.admin.pojo.enums;


public enum NFTPersonalityEnum {

	/**
	 * 建筑师
	 */
	INTJ(1,"Architect INTJ"),

	/**
	 * 逻辑学家
	 */
	INTP(2,"Logician INTP"),

	/**
	 * 指挥官
	 */
	ENTJ(3,"Commander ENTJ"),

	/**
	 * 辩论家
	 */
	ENTP(4,"Debater ENTP"),

	/**
	 * 提倡者
	 */
	INFJ(5,"Advocate INFJ"),

	/**
	 * 调停者
	 */
	INFP(6,"Mediator INFP"),

	/**
	 * 主人公
	 */
	ENFJ(7,"Protagonist ENFJ"),

	/**
	 * 竞选者
	 */
	ENFP(8,"Campaigner ENFP"),

	/**
	 * 物流师
	 */
	ISTJ(9,"Logistician ISTJ"),

	/**
	 * 守卫者
	 */
	ISFJ(10,"Defender ISFJ"),

	/**
	 * 总经理
	 */
	ESTJ(11,"Executive ESTJ"),

	/**
	 * 执政官
	 */
	ESFJ(12,"Consul ESFJ"),

	/**
	 * 鉴赏家
	 */
	ISTP(13,"Virtuoso ISTP"),

	/**
	 * 探险家
	 */
	ISFP(14,"Adventurer ISFP"),

	/**
	 * 企业家
	 */
	ESTP(15,"Entrepreneur ESTP"),

	/**
	 * 表演者
	 */
	ESFP(16,"Entertainer ESFP");


	private int code;
	private String name;

	NFTPersonalityEnum(int code, String name) {
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
