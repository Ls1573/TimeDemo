package com.time.memory.view.impl;

import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.OtherMemorys;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IMemoryOtherTimeView extends IBaseView {

	void setAdapter(List<OtherMemorys> list, ArrayList<OtherMemory> otherMemories);
}
