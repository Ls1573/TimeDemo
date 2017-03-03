package com.time.memory.view.impl;

import com.time.memory.entity.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * V-P
 */
public interface ICircleView extends IBaseView {

	void setAdapter(List<Contacts> list);

	void setRecyerAdapter(List<Contacts> list);

	void reqContacts();

	void reqFLetter(ArrayList<String> fLetters);

	void removeFriend(int position);

	void refreshContacts();

	void nofityMemoryNum(String num);

}
