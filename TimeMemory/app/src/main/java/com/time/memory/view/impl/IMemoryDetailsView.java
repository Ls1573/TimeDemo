package com.time.memory.view.impl;

import com.time.memory.entity.MemoryComment;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.MemoryPraise;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆详情
 * @date 2016/10/28 9:22
 */
public interface IMemoryDetailsView extends IBaseView {
	void setAdapter(MemoryInfo entity);

	void sendCommentSuccess(boolean isSuccess);

	void sendForkSuccess(String flag, boolean isSuccess);

	void sendPointForkSuccess(String flag, boolean isSuccess, int posiont,int memoryTag);

	void refreshComment(MemoryComment comment);

	void refreshPraise(MemoryPraise praise);

	void refreshAdapter();

	void refreshPraiseAdapter();

	void removeComment(int position);

	void removeMemory();

	void startPoint(int index);
}
