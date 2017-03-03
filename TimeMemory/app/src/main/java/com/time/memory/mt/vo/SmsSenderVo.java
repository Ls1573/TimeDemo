package com.time.memory.mt.vo;

import java.io.Serializable;

import com.time.memory.mt.common.constant.SmsOperateTypeEnum;
import com.time.memory.mt.common.constant.SmsTypeEnum;

public class SmsSenderVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 用户ID */
	private String userId;
	/** 手机号码 */
	private String userMobile;
	/** 发送状态 */
	private String sendStatus;
	/**
	 * 短信发送方式 1：手机短信 2：手机语音
	 */
	private SmsTypeEnum smsType;
	/** 验证码 */
	private String verifyCode;
	/** 短信内容 */
	private String contents;
	/** 短信类型 */
	private SmsOperateTypeEnum operateType;
	/** 短信认证ID */
	private String smsVerifyCodeId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	public SmsTypeEnum getSmsType() {
		return smsType;
	}

	public void setSmsType(SmsTypeEnum smsType) {
		this.smsType = smsType;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public SmsOperateTypeEnum getOperateType() {
		return operateType;
	}

	public void setOperateType(SmsOperateTypeEnum operateType) {
		this.operateType = operateType;
	}

	public String getSmsVerifyCodeId() {
		return smsVerifyCodeId;
	}

	public void setSmsVerifyCodeId(String smsVerifyCodeId) {
		this.smsVerifyCodeId = smsVerifyCodeId;
	}

}