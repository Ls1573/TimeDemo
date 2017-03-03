package com.time.memory.mt.nio.message.response;

import com.time.memory.mt.vo.UserVo;

import java.util.List;

public class CG04RespVo extends MsgResponse {

	// 用户列表
	private List<UserVo> userVoList;

	public List<UserVo> getUserVoList() {
		return userVoList;
	}

	public void setUserVoList(List<UserVo> userVoList) {
		this.userVoList = userVoList;
	}

}
