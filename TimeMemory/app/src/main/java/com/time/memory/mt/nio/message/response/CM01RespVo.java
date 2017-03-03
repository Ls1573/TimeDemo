package com.time.memory.mt.nio.message.response;

import com.time.memory.mt.vo.MessageVo;

import java.util.List;

public class CM01RespVo extends MsgResponse {

	// 消息列表
	private List<MessageVo> messageVoList;

	public List<MessageVo> getMessageVoList() {
		return messageVoList;
	}

	public void setMessageVoList(List<MessageVo> messageVoList) {
		this.messageVoList = messageVoList;
	}

}
