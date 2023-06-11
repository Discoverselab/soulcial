package org.springblade.modules.admin.pojo.enums;


public enum NFTMoodEnum {

    /**
     * Excited
	 */
	Excited(1,"Excited"),

	/**
	 * Calm
	 */
	Calm(2,"Calm"),

	/**
	 * Angry
	 */
	Angry(3,"Angry"),

	/**
	 * Shocked
	 */
	Shocked(4,"Shocked"),

	/**
	 * Cheerful
	 */
	Cheerful(5,"Cheerful"),

	/**
	 * Confused
	 */
	Confused(6,"Confused"),

	/**
	 * Heartbroken
	 */
	Heartbroken(7,"Heartbroken"),

	/**
	 * Fearful
	 */
	Fearful(8,"Fearful");


	private int code;
	private String name;

	NFTMoodEnum(int code, String name) {
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
