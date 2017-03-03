package com.time.memory.mt.common.constant;

import java.util.Arrays;

public enum SmsTypeEnum {

	/** 文本 */
	TEXT,
	/** 语音 */
	VOICE;

	public String getValue() {
		return this.name();
	}

	public String getConfigCategory() {
		String category = "UNKNOWN";
		switch (this) {
		case TEXT:
			category = "text";
			break;
		case VOICE:
			category = "voice";
			break;
		default:
			break;
		}
		return category;
	}

	public static SmsTypeEnum getEnum(String value) {
		if ("TEXT".equalsIgnoreCase(value)) {
			return TEXT;
		} else if ("VOICE".equalsIgnoreCase(value)) {
			return VOICE;
		}
		throw new RuntimeException("未能转换为枚举类型(class: SmsType, value: '" + value
				+ "', availables: " + Arrays.deepToString(values()) + ")");
	}
}