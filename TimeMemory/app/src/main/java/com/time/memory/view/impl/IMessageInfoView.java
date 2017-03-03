package com.time.memory.view.impl;

import com.time.memory.mt.vo.MessageVo;

import java.util.List;

public interface IMessageInfoView extends IBaseView {
	void setAdapter(List<MessageVo> msgs);
}
