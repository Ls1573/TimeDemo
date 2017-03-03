package com.time.memory.mt.common.constant;

public enum EGender {
	Male("男", 1), Female("女", 2), Blank("未知", 9);
	
	private String name;
	private int index;

	private EGender(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (EGender c : EGender.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}
	
	public static EGender getEnum(int index) {
		for (EGender c : EGender.values()) {
			if (c.getIndex() == index) {
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
