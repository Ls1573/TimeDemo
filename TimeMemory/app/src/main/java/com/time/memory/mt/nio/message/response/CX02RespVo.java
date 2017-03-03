package com.time.memory.mt.nio.message.response;


/**
 * 评论
 */
public class CX02RespVo extends MsgResponse {

	// 评论ID
	private String uuid;
	// 圈子ID
	private String groupId;
	// 记忆ID
	private String memoryId;
	//
	private String userToken;


	//评论发布人Id
	private String commentUserId;
	// 当前用户(炒作删除评论用户的ID)
	private String userId;
	// 1:我的记忆;2:书的记忆
	private String source;
	// 记忆Id的源Id
	private String memoryIdSource;


	private CX02RespVo cX02ReqVo;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getMemoryId() {
		return memoryId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public CX02RespVo getCx02ReqVo() {
		return cX02ReqVo;
	}

	public void setCx02ReqVo(CX02RespVo cx02ReqVo) {
		this.cX02ReqVo = cx02ReqVo;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public CX02RespVo getcX02ReqVo() {
		return cX02ReqVo;
	}

	public void setcX02ReqVo(CX02RespVo cX02ReqVo) {
		this.cX02ReqVo = cX02ReqVo;
	}
}
