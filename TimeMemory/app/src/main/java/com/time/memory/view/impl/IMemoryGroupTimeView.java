package com.time.memory.view.impl;

import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.GroupMemorys;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IMemoryGroupTimeView extends IBaseView {

	void setAdapter(List<GroupMemorys> list, ArrayList<GroupMemory> groupMemories);
}
