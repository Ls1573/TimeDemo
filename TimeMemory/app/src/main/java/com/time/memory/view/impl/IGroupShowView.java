package com.time.memory.view.impl;

import com.time.memory.entity.GroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IGroupShowView extends IBaseView {
	void setAdapter(List<GroupContacts> MGroupList, ArrayList<GroupContacts> groupList, int memoryConunt, int addCount);

	void exitCircle();
}
