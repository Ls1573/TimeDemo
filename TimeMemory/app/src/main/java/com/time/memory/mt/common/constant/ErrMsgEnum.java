package com.time.memory.mt.common.constant;

import java.text.MessageFormat;

/**
 * 错误信息
 * 
 * @author admin
 *
 */
public enum ErrMsgEnum {

	E9999("E9999", "系统异常"),
	E0001("E0001", "{0}不能为空"),
	E0002("E0002", "不是有效的{0}"),
	E0003("E0003", "您输入的手机号或密码有误"),
	E0004("E0004", "您输入的手机号已注册"),
	E0005("E0005", "手机验证码发送失败"),
	E0006("E0006", "验证码输入错误或已过期"),
	E0007("E0007", "您输入的手机号未注册"),
	E0008("E0008", "请求数据已删除");

	private String code;
	private String msg;

	private ErrMsgEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}

	public static String getMsg(String code, Object... arguments) {
		for (ErrMsgEnum responseInfo : ErrMsgEnum.values()) {
			if (code.equals(responseInfo.getCode())) {
				return MessageFormat.format(responseInfo.getMsg(), arguments);
			}
		}
		return E9999.getMsg();
	}
}