package com.time.memory.mt.nio.message.response;

import com.time.memory.mt.vo.UserVo;

public class CP01RespVo extends MsgResponse {

	// 用户信息
	private UserVo userVo;

	public UserVo getUserVo() {
		return userVo;
	}

	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

}
