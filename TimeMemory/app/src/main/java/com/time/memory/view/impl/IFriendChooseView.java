package com.time.memory.view.impl;

import com.time.memory.entity.GroupContacts;

import java.util.List;

/**
 * V-P
 */
public interface IFriendChooseView extends IBaseView {

	void setAdapter(List<GroupContacts> users);
}
