package com.time.memory.view.impl;

import com.time.memory.entity.WriterMemory;

/**
 * V-P
 */
public interface IAddMemoryView extends IBaseView {

	void setMemory(WriterMemory memory);


	void showDatePicker(int year, int month, int day);
}
