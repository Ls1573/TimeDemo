package com.time.memory.mt.common.constant;

import java.text.MessageFormat;

/**
 * 短信模版
 * 
 * @author admin
 *
 */
public enum SmsTextEnum {

	T0001("T0001", "尊敬的用户，您的手机验证码为{0}。"),
	T0002("T0002", "{0}您好，您更改密码的手机验证码为{1}。");

	private String code;
	private String msg;

	private SmsTextEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}

	public static String getText(String code, Object... arguments) {
		for (SmsTextEnum responseInfo : SmsTextEnum.values()) {
			if (code.equals(responseInfo.getCode())) {
				return MessageFormat.format(responseInfo.getMsg(), arguments);
			}
		}
		return "";
	}
}