package com.time.memory.mt.nio.message.response;



/**
 * 未读消息
 */
public class SA01ReqVo extends MsgResponse {
	private String uuid;
	private String groupId;
	private String messageType;

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
