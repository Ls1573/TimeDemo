package com.time.memory.view.impl;

import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemoryComment;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IMemoryDetailMView extends IBaseView {
	void setMyAdapter(List<Memory> memories);

	void setMyInfoAdapter(MemoryInfo memories, int position, boolean iaNew);

	void setOtherAdapter(ArrayList<OtherMemory> list);

	void setGroupAdapter(ArrayList<GroupMemory> list);

	void removeMemory(int position, int state);

	void sendPointForkSuccess(String flag, boolean isSuccess, int posion, int memoryTag);

	void refreshPraiseAdapter(int memoryTag);

	void sendForkSuccess(String flag, boolean isSuccess, int memoryTag);

	void startPoint(int index, MemoryInfo memoryInfo);

	void removeComment(int position, int memoryTag, String mPonitId, String mCMemoryId,String mpSrcId);

	void refreshMemory(int position);

	void refreshComment(MemoryComment comment);

	void sendCommentSuccess(boolean isSuccess);

	void onPhotoPreivew(ArrayList<PhotoInfo> photoInfos, int index);
}
