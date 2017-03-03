package com.time.memory.mt.vo;

import java.io.Serializable;

public class BaseVo implements Serializable {
	private static final long serialVersionUID = -3087767076568180058L;
	
	private Integer status;
	private String message;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
