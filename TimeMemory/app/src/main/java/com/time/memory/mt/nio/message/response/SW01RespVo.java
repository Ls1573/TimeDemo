package com.time.memory.mt.nio.message.response;



/**
 * 评论
 */
public class SW01RespVo extends MsgResponse {
	private String dateYmd;
	private String groupId;
	private String messageDetail;
	private String messageType;
	private String uuid;
	private String muid;

	public String getDateYmd() {
		return dateYmd;
	}

	public void setDateYmd(String dateYmd) {
		this.dateYmd = dateYmd;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getMessageDetail() {
		return messageDetail;
	}

	public void setMessageDetail(String messageDetail) {
		this.messageDetail = messageDetail;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getMuid() {
		return muid;
	}

	public void setMuid(String muid) {
		this.muid = muid;
	}
}
