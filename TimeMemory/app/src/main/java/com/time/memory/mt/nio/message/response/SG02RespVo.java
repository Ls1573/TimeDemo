package com.time.memory.mt.nio.message.response;

public class SG02RespVo extends MsgResponse {

	// 用户ID
	private String userId;

	// 手机号码
	private String userMobile;

	// 用户姓名
	private String userName;


	private String isTwoWayFlg;

	// 头像
	private String headPhoto;

	// 激活状态
	private String activeFlg;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
	}

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public String getIsTwoWayFlg() {
		return isTwoWayFlg;
	}

	public void setIsTwoWayFlg(String isTwoWayFlg) {
		this.isTwoWayFlg = isTwoWayFlg;
	}
}
