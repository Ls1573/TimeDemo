package com.time.memory.mt.nio.message.response;

import java.util.List;

public class SG01RespVo extends MsgResponse {

	// 圈子ID
	private String groupId;
	// 圈子名称
	private String groupName;
	//激活状态
	private String activeFlg;
	//管理员id
	private String adminUserId;
	//管理员name
	private String adminUserName;
	//冻结状态
	private String freeze;
	//成员数量
	private int groupCount;
	private String groupInsDateForShow;
	//圈子号
	private int groupNum;
	//头像地址
	private List<String> headPhotos;
	//记忆数
	private int memoryCnt;
	//更新时间
	private String updateDateForShow;

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

	public String getActiveFlg() {
		return activeFlg;
	}

	public void setActiveFlg(String activeFlg) {
		this.activeFlg = activeFlg;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public int getGroupCount() {
		return groupCount;
	}

	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}

	public String getGroupInsDateForShow() {
		return groupInsDateForShow;
	}

	public void setGroupInsDateForShow(String groupInsDateForShow) {
		this.groupInsDateForShow = groupInsDateForShow;
	}

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public List<String> getHeadPhotos() {
		return headPhotos;
	}

	public void setHeadPhotos(List<String> headPhotos) {
		this.headPhotos = headPhotos;
	}

	public int getMemoryCnt() {
		return memoryCnt;
	}

	public void setMemoryCnt(int memoryCnt) {
		this.memoryCnt = memoryCnt;
	}

	public String getUpdateDateForShow() {
		return updateDateForShow;
	}

	public void setUpdateDateForShow(String updateDateForShow) {
		this.updateDateForShow = updateDateForShow;
	}

	public String getFreeze() {
		return freeze;
	}

	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}
}

