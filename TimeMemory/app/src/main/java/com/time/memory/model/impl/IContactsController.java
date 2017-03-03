package com.time.memory.model.impl;

import android.content.Context;

import com.time.memory.mt.vo.ContactsVo;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.GroupContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0 ==============================
 * @Description:通讯录操作
 * @date 2016-9-9下午4:56:43
 */
public interface IContactsController {

	void insertGroupTemp(ArrayList<GroupContacts> mList);

	void insertContactsTemp(ArrayList<ContactsVo> mList);

	void reqContacts(final String url, final String userToken, final SimpleCallback callback);

	void reqUpContacts(final String url, ContactsVo contactsVo, final SimpleCallback callback);

	void reqUpContacts(final String url, String friendId,String friendName, final SimpleCallback callback);

	void removeFriend(String friendId);

	void removeAll();

	void reqRemoveFriend(String url, String userId, SimpleCallback callback);

	void saveContacts(List<Contacts> list);

	void saveContact(Contacts contact);

	Contacts getContactFromDb(String userId,String toUserId);

	List<Contacts> getContactsFromDb(String userId);

	List<Contacts> getContactsFromDb(String userId,String isOnly);

	List<Contacts> reqContacts(String userId);

	List<Contacts> getContacts(String keyWord, String userId);

	ArrayList<ContactsVo> getContacts(Context context);

	ArrayList<ContactsVo> getFrContactsFromDb(String userId);
}
