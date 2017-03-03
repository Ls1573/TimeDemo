package com.time.memory.view.impl;

import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.GroupMemorys;
import com.time.memory.entity.Lable;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IMemoryGroupTagView extends IBaseView {

	void getTagSuccess();

	void setTags(List<Lable> tags);

	void setAdapter(List<GroupMemorys> list, ArrayList<GroupMemory> groupMemories);

	void showTags(boolean isSuccess);
}
