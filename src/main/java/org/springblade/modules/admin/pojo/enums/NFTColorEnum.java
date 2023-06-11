package org.springblade.modules.admin.pojo.enums;


public enum NFTColorEnum {

    /**
     * White
	 */
	White(1,"White"),

	/**
	 * Red
	 */
	Red(2,"Red"),

	/**
	 * Orange
	 */
	Orange(3,"Orange"),

	/**
	 * Yellow
	 */
	Yellow(4,"Yellow"),

	/**
	 * Green
	 */
	Green(5,"Green"),

	/**
	 * Blue
	 */
	Blue(6,"Blue"),

	/**
	 * Indigo
	 */
	Indigo(7,"Indigo"),

	/**
	 * Violet
	 */
	Violet(8,"Violet"),

	/**
	 * Black
	 */
	Black(9,"Black");


	private int code;
	private String name;

	NFTColorEnum(int code, String name) {
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
