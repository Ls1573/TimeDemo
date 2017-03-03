package com.time.memory.mt.nio.message.response;

/**
 * 请求消息父类
 * @author sunxiao
 *
 */
public class MsgRequest {

	// 业务分类 类型
	// BusynessType
	private String type;

	// 用户ID
	private String userId;

//	// 圈子ID
//	private String groupId;
//
//	// 消息ID
//	private String msgUuid;
//
//	// 圈子信息
//	private GroupInfoVo groupInfoVo;
//
//	// 用户信息
//	private UserVo userVo;
//
//	// 用户列表
//	private List<UserVo> userVoList;
//
//	// 选择的好友列表
//	private List<String> friendIdList;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

//	public String getGroupId() {
//		return groupId;
//	}
//
//	public void setGroupId(String groupId) {
//		this.groupId = groupId;
//	}
//
//	public String getMsgUuid() {
//		return msgUuid;
//	}
//
//	public void setMsgUuid(String msgUuid) {
//		this.msgUuid = msgUuid;
//	}
//
//	public GroupInfoVo getGroupInfoVo() {
//		return groupInfoVo;
//	}
//
//	public void setGroupInfoVo(GroupInfoVo groupInfoVo) {
//		this.groupInfoVo = groupInfoVo;
//	}
//
//	public UserVo getUserVo() {
//		return userVo;
//	}
//
//	public void setUserVo(UserVo userVo) {
//		this.userVo = userVo;
//	}
//
//	public List<UserVo> getUserVoList() {
//		return userVoList;
//	}
//
//	public void setUserVoList(List<UserVo> userVoList) {
//		this.userVoList = userVoList;
//	}
//
//	public List<String> getFriendIdList() {
//		return friendIdList;
//	}
//
//	public void setFriendIdList(List<String> friendIdList) {
//		this.friendIdList = friendIdList;
//	}

}
