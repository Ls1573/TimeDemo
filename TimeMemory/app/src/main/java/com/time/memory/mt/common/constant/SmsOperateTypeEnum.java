package com.time.memory.mt.common.constant;

/**
 * 短信操作类型
 *
 * @author admin
 */
public enum SmsOperateTypeEnum {

	NONE("00"),
	// 注册-发送验证码
	REGIST_VERIFY_CODE("01"),
	// 忘记密码-发送验证码
	CHANGE_PWD_VERIFY_CODE("02");

	/**
	 * 取得値
	 */
	private String value;

	/**
	 * Constructor.
	 *
	 * @param value
	 */
	private SmsOperateTypeEnum(String value) {
		this.value = value;
	}

	/**
	 * 返回对应的枚举値。
	 *
	 * @return String
	 */
	public String getValue() {
		return this.value;
	}

}
