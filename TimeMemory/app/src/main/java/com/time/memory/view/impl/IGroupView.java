package com.time.memory.view.impl;

import java.util.List;

import com.time.memory.entity.MGroup;

/**
 * V-P
 */
public interface IGroupView extends IBaseView {
	void setAdapter(List<MGroup> MGroupList);
}
