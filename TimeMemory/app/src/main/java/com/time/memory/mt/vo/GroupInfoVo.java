package com.time.memory.mt.vo;

/**
 * 圈子信息
 *
 * @author admin
 *
 */
public class GroupInfoVo {

	// 用户ID
	private String userId;

	// 圈子ID
	public String groupId;

	// 圈子名称
	private String groupName;

	// 管理员用户ID
	private String adminUserId;

	// 圈子人数
	private Integer groupCount;

	// 圈子冻结状态（0：正常 1：冻结）
	private String freeze;

	// 圈子创建日期
	private String insDate;

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
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public Integer getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(Integer groupCount) {
		this.groupCount = groupCount;
	}

	public String getFreeze() {
		return freeze;
	}

	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

}