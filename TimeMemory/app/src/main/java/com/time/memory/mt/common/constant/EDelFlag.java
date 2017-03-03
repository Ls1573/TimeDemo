package com.time.memory.mt.common.constant;

/**
 * 删除标识(0:有效; 1:无效)
 * @author Think
 *
 */
public enum EDelFlag {
	Normal("有效", "0"), Deleted("无效", "1");
	
	private String name;
	private String index;

	private EDelFlag(String name, String index) {
		this.name = name;
		this.setIndex(index);
	}

	public static String getName(String index) {
		for (EDelFlag c : EDelFlag.values()) {
			if (c.getIndex().equals(index)) {
				return c.name;
			}
		}
		return null;
	}
	
	public static EDelFlag getEnum(String index) {
		for (EDelFlag c : EDelFlag.values()) {
			if (c.getIndex().equals(index)) {
				return c;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}


}
