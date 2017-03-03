package com.time.memory.view.impl;

import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.OtherMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IMemoryDetailView extends IBaseView {
	void setMyAdapter(List<Memory> memories);

	void setMyInfoAdapter(MemoryInfo memories,int position);

	void setOtherAdapter(ArrayList<OtherMemory> list);

	void setGroupAdapter(ArrayList<GroupMemory> list);

	void removeMemory(int position, int state);

	void sendPointForkSuccess(String flag, boolean isSuccess, int posion,int memoryTag);

	void refreshPraiseAdapter();

}
