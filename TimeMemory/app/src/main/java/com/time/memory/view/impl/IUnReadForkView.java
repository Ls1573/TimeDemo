package com.time.memory.view.impl;

import com.time.memory.entity.UnForkDto;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IUnReadForkView extends IBaseView {
	void setAdapter(ArrayList<UnForkDto> list);
	void refreshAdapter();

	void closeActivity();
}
