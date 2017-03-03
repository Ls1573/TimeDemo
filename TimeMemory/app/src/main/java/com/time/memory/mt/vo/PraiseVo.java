package com.time.memory.mt.vo;

import android.text.TextUtils;

public class PraiseVo {

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 圈子ID
	 */
	private String groupId;

	/**
	 * 记忆Id的源Id
	 */
	String memoryIdSource;

	/**
	 * 记忆片段Id的源Id
	 */
	String memoryPointIdSource;

	/**
	 * 记忆ID
	 */
	private String memoryId;

	/**
	 * 记忆片断ID
	 */
	private String memoryPointId;

	/**
	 * 记忆发布人ID
	 */
	private String memoryUserId;

	/**
	 * 点赞人ID
	 */
	private String praiseUserId;

	/**
	 * 删除标志
	 */
	private String delFlg;

	/**
	 * 点赞人头像
	 */
	private String headPhoto;

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

	public String getHeadPhoto() {
		return headPhoto;
	}

	public void setHeadPhoto(String headPhoto) {
		this.headPhoto = headPhoto;
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

	public String getMemoryUserId() {
		return memoryUserId;
	}

	public void setMemoryUserId(String memoryUserId) {
		this.memoryUserId = memoryUserId;
	}

	public String getPraiseUserId() {
		return praiseUserId;
	}

	public void setPraiseUserId(String praiseUserId) {
		this.praiseUserId = praiseUserId;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
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
}