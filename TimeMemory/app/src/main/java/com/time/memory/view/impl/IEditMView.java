package com.time.memory.view.impl;

import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.WriterStyleMemory;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IEditMView extends IBaseView {

	void setAdapter(ArrayList<MemoryEdit> list, WriterStyleMemory writerStyleMemory);

	void addAdapter(boolean isAdd);

	void refreshItemAdapter(int positoin);
	void removeItemAdapter(int positoin);

	void refreshHeaderAdapter();

	void showDatePicker(int year, int month, int day, int position);

	void checkPhoto(int maxSize);


}
