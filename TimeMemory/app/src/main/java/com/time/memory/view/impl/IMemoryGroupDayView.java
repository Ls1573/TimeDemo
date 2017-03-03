package com.time.memory.view.impl;

import android.content.Intent;

import com.time.memory.entity.GroupMemory;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IMemoryGroupDayView extends IBaseView {

	void setAdapter(ArrayList<GroupMemory> list);

	void addAdapter(GroupMemory memory);

	void refreshAdapter();

	void startMyActivity(Intent intent);
}
