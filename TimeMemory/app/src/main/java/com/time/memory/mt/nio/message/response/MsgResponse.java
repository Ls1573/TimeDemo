package com.time.memory.mt.nio.message.response;



/**
 * 响应体的父类
 *
 * @author sunxiao
 */
public class MsgResponse {

	// 业务是否成功的枚举类型
	// ResultStatusEnum
	private Integer code;

	// 返回消息
	private String message;

	// 业务分类 类型
	// BusynessType
	private String type;

	// 客户端用户ID
	private String clientUserId;

//	// 圈子信息
//	private GroupInfoVo groupInfoVo;
//
//	// 圈子列表
//	private List<GroupInfoVo> groupInfoVoList;
//
//	// 用户信息
//	private UserVo userVo;
//
//	// 用户列表
//	private List<UserVo> userVoList;
//
//	// 消息列表
//	private List<MessageVo> messageVoList;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClientUserId() {
		return clientUserId;
	}

	public void setClientUserId(String clientUserId) {
		this.clientUserId = clientUserId;
	}


}
