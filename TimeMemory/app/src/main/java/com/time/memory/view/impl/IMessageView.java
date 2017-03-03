package com.time.memory.view.impl;

import com.time.memory.entity.Message;

import java.util.List;

public interface IMessageView extends IBaseView {
	void setAdapter(List<Message> msgs);

	void refresh(int position);

	void removeAdapter(int position);
}
