package com.time.memory.view.impl;

import com.time.memory.entity.GroupContacts;

import java.util.ArrayList;

/**
 * V-P
 */
public interface IDelCircleView extends IBaseView {

	void setAdapter(ArrayList<GroupContacts> list);

	void setFilterAdapter(ArrayList<GroupContacts> list);

	void removeUses();

}
