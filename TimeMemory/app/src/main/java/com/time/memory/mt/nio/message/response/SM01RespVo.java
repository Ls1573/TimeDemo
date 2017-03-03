package com.time.memory.mt.nio.message.response;

public class SM01RespVo extends MsgResponse {

	// 消息ID
	private String uuid;

	// 消息类型
	private String messageType;

	// 消息详细内容(参数)
	private String messageDetail;

	// 发送用户ID
	private String sendUserId;

	// 阅读标识
	private String readFlg;

	// 做成日
	private String insDate;

	// 做成时间
	private String insTime;

	/**
	 * 发送用户name
	 */
	private String sendUserName;
	/**
	 * 发送用户头像
	 */
	private String sendUserPhoto;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageDetail() {
		return messageDetail;
	}

	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getReadFlg() {
		return readFlg;
	}

	public void setReadFlg(String readFlg) {
		this.readFlg = readFlg;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

	public String getInsTime() {
		return insTime;
	}

	public void setInsTime(String insTime) {
		this.insTime = insTime;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserPhoto() {
		return sendUserPhoto;
	}

	public void setSendUserPhoto(String sendUserPhoto) {
		this.sendUserPhoto = sendUserPhoto;
	}
}
