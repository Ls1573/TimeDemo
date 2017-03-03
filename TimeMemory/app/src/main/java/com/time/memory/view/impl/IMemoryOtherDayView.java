package com.time.memory.view.impl;

import android.content.Intent;

import com.time.memory.entity.OtherMemory;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IMemoryOtherDayView extends IBaseView {

	void setAdapter(ArrayList<OtherMemory> list);

	void addAdapter(OtherMemory memory);

	void refreshAdapter();

	void startMyActivity(Intent intent);
}
