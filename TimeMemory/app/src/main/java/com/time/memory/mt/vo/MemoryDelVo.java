package com.time.memory.mt.vo;


import com.time.memory.mt.nio.message.response.MsgRequest;

public class MemoryDelVo extends MsgRequest {


	private String memoryId;

	private String memoryPointId;

	private String addUserId;

	private String userToken;

	private String groupId;

	// 本体/补充区分 0:本体片段 1:补充片段
	private String mpFlg;

	//1:我的记忆;2:其他书的
	private String source;

	// 记忆Id的源Id
	private String memoryIdSource;

	//记忆片段Id的源Id
	private String memoryPointIdSource;


	private MemoryDelVo memoryDelVo;


	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getMemoryPointId() {
		return memoryPointId;
	}

	public void setMemoryPointId(String memoryPointId) {
		this.memoryPointId = memoryPointId;
	}

	public String getAddUserId() {
		return addUserId;
	}

	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public MemoryDelVo getMemoryDelVo() {
		return memoryDelVo;
	}

	public void setMemoryDelVo(MemoryDelVo memoryDelVo) {
		this.memoryDelVo = memoryDelVo;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getMpFlg() {
		return mpFlg;
	}

	public void setMpFlg(String mpFlg) {
		this.mpFlg = mpFlg;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMemoryIdSource() {
		return memoryIdSource;
	}

	public void setMemoryIdSource(String memoryIdSource) {
		this.memoryIdSource = memoryIdSource;
	}

	public String getMemoryPonitIdSource() {
		return memoryPointIdSource;
	}

	public void setMemoryPonitIdSource(String memoryPonitIdSource) {
		this.memoryPointIdSource = memoryPonitIdSource;
	}
}