package com.time.memory.view.impl;

import com.time.memory.entity.MGroup;

import java.util.List;

/**
 * V-P
 */
public interface IForwardView extends IBaseView {

	void setAdapter(List<MGroup> groupList);

	void finishActivity(int type,boolean remove,String groupName);
}
