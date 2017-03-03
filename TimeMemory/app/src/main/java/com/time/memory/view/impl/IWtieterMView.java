package com.time.memory.view.impl;

import com.time.memory.entity.WriterMemory;
import com.time.memory.entity.WriterStyleMemory;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IWtieterMView extends IBaseView {

	void setMaxSize(int size);

	void setAdapter(ArrayList<WriterMemory> list, WriterStyleMemory writerStyleMemory);

	void addAdapter(boolean isAdd);

	void refreshItemAdapter(int positoin);

	void refreshHeaderAdapter();

	void showDatePicker(int year, int month, int day, int position);

	void setMessage(int nState, String groupId, String sign);
}
