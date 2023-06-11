package org.springblade.modules.admin.pojo.enums;


public enum NFTWeatherEnum {

	/**
	 * Sunny
	 */
	Sunny(1,"Sunny"),

	/**
	 * Cloudy
	 */
	Cloudy(2,"Cloudy"),

	/**
	 * Overcast
	 */
	Overcast(3,"Overcast"),

	/**
	 * Drizzling
	 */
	Drizzling(4,"Drizzling"),

	/**
	 * Stormy
	 */
	Stormy(5,"Stormy"),

	/**
	 * Windy
	 */
	Windy(6,"Windy"),

	/**
	 * Misty
	 */
	Misty(7,"Misty"),

	/**
	 * Snowy
	 */
	Snowy(8,"Snowy");


	private int code;
	private String name;

	NFTWeatherEnum(int code, String name) {
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
