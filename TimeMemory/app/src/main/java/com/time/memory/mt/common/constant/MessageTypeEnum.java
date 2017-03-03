package com.time.memory.mt.common.constant;

/**
 * 消息类型
 *
 * @author admin
 */
public enum MessageTypeEnum {

	/** 添加好友 */
	ADD_FRIEND("01"),
	/** 加入圈子 */
	JOIN_GROUP("02"),
	/** 点赞 */
	ADD_PRAISE("11");
	
	private String value;

	private MessageTypeEnum(String value) {
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
