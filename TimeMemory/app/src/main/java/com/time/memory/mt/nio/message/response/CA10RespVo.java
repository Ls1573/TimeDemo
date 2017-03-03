package com.time.memory.mt.nio.message.response;


import com.time.memory.mt.vo.CommentVo;

public class CA10RespVo extends MsgResponse {

	// 评论VO
	private String uuid;

	private String userToken;

	private CommentVo commentVo;

	public CommentVo getCommentVo() {
		return commentVo;
	}

	public void setCommentVo(CommentVo commentVo) {
		this.commentVo = commentVo;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
