package com.time.memory.mt.nio.message.response;

import com.time.memory.mt.vo.UserVo;

import java.util.List;

public class CG01RespVo extends MsgResponse {

	// 圈子ID
	private String groupId;

	// 圈子创建时间
	private String insDate;

	// 圈子名称
	private String groupName;

	// token
	private String userToken;


	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	// 用户列表
	private List<UserVo> userVoList;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<UserVo> getUserVoList() {
		return userVoList;
	}

	public void setUserVoList(List<UserVo> userVoList) {
		this.userVoList = userVoList;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getInsDate() {
		return insDate;
	}

	public void setInsDate(String insDate) {
		this.insDate = insDate;
	}

}
