package com.time.memory.view.impl;

import com.time.memory.entity.Memory;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IMemoryDayView extends IBaseView {

	void setAdapter(ArrayList<Memory> list);

	void addAdapter(Memory memory);

	void refreshAdapter();
}
