package com.time.memory.view.impl;

import com.time.memory.entity.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface IContactsView extends IBaseView {
	void setAdapter(List<Contacts> list);

	void reqFLetter(ArrayList<String> fLetters);

	void setRecyerAdapter(List<Contacts> list);

}
