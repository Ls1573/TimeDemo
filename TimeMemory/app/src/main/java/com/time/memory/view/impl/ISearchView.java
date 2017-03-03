package com.time.memory.view.impl;

import com.time.memory.entity.MGroup;

import java.util.List;

/**
 * V-P
 */
public interface ISearchView extends IBaseView {
	void setAdapter(List<MGroup> MGroupInfoVoList);

	void startActivity(MGroup mGroup,Class cla);
}
