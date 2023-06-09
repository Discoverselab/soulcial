/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package org.springblade.common.cache;

import org.springblade.core.tool.utils.StringPool;

/**
 * 缓存名
 *
 * @author Chill
 */
public interface CacheNames {

	/**
	 * 返回拼接后的key
	 *
	 * @param cacheKey      缓存key
	 * @param cacheKeyValue 缓存key值
	 * @return tenantKey
	 */
	static String cacheKey(String cacheKey, String cacheKeyValue) {
		return cacheKey.concat(cacheKeyValue);
	}

	/**
	 * 返回租户格式的key
	 *
	 * @param tenantId      租户编号
	 * @param cacheKey      缓存key
	 * @param cacheKeyValue 缓存key值
	 * @return tenantKey
	 */
	static String tenantKey(String tenantId, String cacheKey, String cacheKeyValue) {
		return tenantId.concat(StringPool.COLON).concat(cacheKey).concat(cacheKeyValue);
	}

	/**
	 * 验证码key
	 */
	String CAPTCHA_KEY = "blade:auth::blade:captcha:";

	/**
	 * 登录失败key
	 */
	String USER_FAIL_KEY = "blade:user::blade:fail:";

	/**
	 * 万能验证码
	 */
	String UNIVERSAL_KEY = "万能验证码";

	/**
	 * 登录注册-验证码频率_key
	 *
	 */
	String REGISTER_LOGIN_PHONE_SEND_KEY = "REGISTER_LOGIN_PHONE_SEND:";

	/**
	 * 登录注册_key
	 */
	String REGISTER_LOGIN_PHONE_KEY = "REGISTER_LOGIN_PHONE:";

	/**
	 * 绑定手机-验证码频率_key
	 */
	String BIND_PHONE_SEND_KEY = "BIND_PHONE_SEND:";

	/**
	 * 绑定手机_key
	 */
	String BIND_PHONE_KEY = "BIND_PHONE:";
}
