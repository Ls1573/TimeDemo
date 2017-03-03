package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.entity.GroupContacts;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IShowCircleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:圈子
 * @date 2016-9-9下午4:54:08
 * ==============================
 */
public class ShowCirclePresenter extends BasePresenter<IShowCircleView> {
	private static final String TAG = "CirclePresenter";

	public ShowCirclePresenter() {
	}

	/**
	 * 获取集合中关键字数据
	 *
	 * @param keyWord
	 */
	public void getContactsByWord(String keyWord, ArrayList<GroupContacts> mList) {
		ArrayList<GroupContacts> cList = new ArrayList<>();
		for (GroupContacts entity : mList) {
			if (TextUtils.isEmpty(entity.getUserName()))
				cList.add(entity);
			else if (entity.getUserName().contains(keyWord)) {
				cList.add(entity);
			}
		}
		CLog.e(TAG, "cList:" + cList.size());
		Collections.sort(cList, new ConstactComparator());
		if (mView != null) {
			mView.setFilterAdapter(cList);
		}
	}

	/**
	 * 数据转换
	 *
	 * @param contacts
	 */
	public void convertAdapter(ArrayList<GroupContacts> contacts) {
		//字母
		convertList(contacts);
		//排序
		Collections.sort(contacts, new ConstactComparator());

		//联系人
		if (mView != null)
			mView.setAdapter(contacts);
	}


	private void convertList(ArrayList<GroupContacts> contactList) {
		if (contactList == null) contactList = new ArrayList<>();
		for (GroupContacts contacts : contactList) {
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getUserName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
		}
	}

	/**
	 * a-z排序
	 */
	class ConstactComparator implements Comparator<GroupContacts> {
		@Override
		public int compare(GroupContacts lhs, GroupContacts rhs) {
			String a = lhs.getfLetter();
			String b = rhs.getfLetter();
			return a.compareTo(b);
		}
	}

}
