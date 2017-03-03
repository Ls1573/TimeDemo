package com.time.memory.view.impl;

import com.time.memory.entity.Lable;

import java.util.List;

/**
 * V-P
 */
public interface IAddTagView extends IBaseView {

	void setTags(List<Lable> tags);

	void showAddTagView(boolean isShow);

	void removeTagSuccess(int positoin);

	void addTagView(Lable tag);

	void addTag(Lable lable);
}
