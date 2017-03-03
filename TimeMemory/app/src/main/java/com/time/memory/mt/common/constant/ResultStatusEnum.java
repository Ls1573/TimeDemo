package com.time.memory.mt.common.constant;

public enum ResultStatusEnum {
	SUCCESS(0), FAILURE(1);

	private int status;

	private ResultStatusEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
