package com.time.memory.view.impl;

import com.time.memory.entity.Contacts;
import com.time.memory.entity.GroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IChooseView extends IBaseView {

	void setAdapter(List<Contacts> list);

	void setFilterAdapter(ArrayList<Contacts> list);

	void closeActivity();
}
