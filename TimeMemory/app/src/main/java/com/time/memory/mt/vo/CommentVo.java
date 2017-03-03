package com.time.memory.mt.vo;

import android.text.TextUtils;

public class CommentVo {

	/**
	 * 用户ID(记忆发布人ID)
	 */
	private String userId;

	/**
	 * 圈子ID
	 */
	private String groupId;

	/**
	 * 记忆ID
	 */
	private String memoryId;

	/**
	 * 记忆片断ID
	 */
	private String memoryPointId;

	/**
	 * 评论发布人ID
	 */
	private String commentUserId;

	/**
	 * 评论发布人name
	 */
	private String commentUserName;

	/**
	 * 评论发布人头像
	 */
	private String commentUserHead;

	/**
	 * 评论接收人ID
	 */
	private String commentToUserId;

	/**
	 * 评论接收人name
	 */
	private String commentToUserName;

	/**
	 * 评论内容
	 */
	private String commentTitle;


	/**
	 * 删除标志
	 */
	private String delFlg;

	/**
	 * 记忆Id的源Id
	 */
	private String memoryIdSource;

	/**
	 * 记忆片段Id的源Id
	 */
	private String memoryPointIdSource;
	/**
	 * 1:我的记忆;2:其他书中
	 */
	private String source;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		if (TextUtils.isEmpty(groupId))
			this.groupId = "";
		else
			this.groupId = groupId;
	}

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

	public String getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getCommentToUserId() {
		return commentToUserId;
	}

	public void setCommentToUserId(String commentToUserId) {
		if (TextUtils.isEmpty(commentToUserId))
			this.commentToUserId = "";
		else
			this.commentToUserId = commentToUserId;
	}

	public String getCommentTitle() {
		return commentTitle;
	}

	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}

	public String getCommentToUserName() {
		return commentToUserName;
	}

	public void setCommentToUserName(String commentToUserName) {
		this.commentToUserName = commentToUserName;
	}

	public String getCommentUserName() {
		return commentUserName;
	}

	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}

	public String getCommentUserHead() {
		return commentUserHead;
	}

	public void setCommentUserHead(String commentUserHead) {
		this.commentUserHead = commentUserHead;
	}

	public String getMemoryIdSource() {
		return memoryIdSource;
	}

	public void setMemoryIdSource(String memoryIdSource) {
		this.memoryIdSource = memoryIdSource;
	}

	public String getMemoryPointIdSource() {
		return memoryPointIdSource;
	}

	public void setMemoryPointIdSource(String memoryPointIdSource) {
		this.memoryPointIdSource = memoryPointIdSource;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}