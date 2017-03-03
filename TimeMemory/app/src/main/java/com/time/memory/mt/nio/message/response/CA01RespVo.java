package com.time.memory.mt.nio.message.response;


import com.time.memory.mt.vo.PraiseVo;

public class CA01RespVo extends MsgResponse {

	// 初次点赞标志
	private boolean firstFlg;

	private String praiserUuid;
	// 点赞VO
	private PraiseVo praiseVo;

	private String userToken;

	public PraiseVo getPraiseVo() {
		return praiseVo;
	}

	public void setPraiseVo(PraiseVo praiseVo) {
		this.praiseVo = praiseVo;
	}

	public String getToken() {
		return userToken;
	}

	public void setToken(String token) {
		this.userToken = token;
	}

	public String getPraiserUuid() {
		return praiserUuid;
	}

	public void setPraiserUuid(String praiserUuid) {
		this.praiserUuid = praiserUuid;
	}

	public boolean isFirstFlg() {
		return firstFlg;
	}

	public void setFirstFlg(boolean firstFlg) {
		this.firstFlg = firstFlg;
	}

}
