package com.time.memory.view.impl;

import com.time.memory.entity.Message;

import java.util.List;

/**
 * V-P
 */
public interface IMessageDealView extends IBaseView {
	void setAdapter(List<Message> list);

	void refreshAdapter(int position, int opr);

}
