package com.time.memory.mt.common.constant.message;


public enum Type {
	System("系统消息", 0), Request("请求体", 1), Response("响应体", 2);
	
	private String name;
	private int index;

	private Type(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (Type c : Type.values()) {
			if (c.getIndex() == index) {
				return c.name;
			}
		}
		return null;
	}
	
	public static Type getEnum(int index) {
		for (Type c : Type.values()) {
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
