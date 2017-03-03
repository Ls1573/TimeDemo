package com.time.memory.mt.nio.message.response;

/**
 * 补充记忆推送
 */
public class SA20RespVo extends MsgResponse {

	private String groupId;

	// 1:我的记忆 2:Ta的记忆 3：圈子记忆
	private String messageType;

	private String uuid;

	private String muid;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

