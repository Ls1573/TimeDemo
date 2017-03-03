package com.time.memory.view.impl;

import com.time.memory.entity.Comment;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.PhotoInfo;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IMemoryPointView extends IBaseView {

	void setMemoryInfo(MemoryEdit entity);

	void sendCommentSuccess(boolean isSuccess);

	void sendForkSuccess(String flag, boolean isSuccess);

	void refreshComment(Comment comment);

	void refreshPraise();

	void removeComment(int position);

	void startActivity(ArrayList<PhotoInfo> photoInfos,int curIndex);
}
