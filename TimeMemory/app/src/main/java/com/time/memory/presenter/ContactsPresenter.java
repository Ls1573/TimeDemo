package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Contacts;
import com.time.memory.model.ContactsController;
import com.time.memory.model.FriendController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.IFriendController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IContactsView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.3
 * @Description:好友
 * @date 2016/12/30 14:26
 */
public class ContactsPresenter extends BasePresenter<IContactsView> {
	private static final String TAG = "CirclePresenter";
	// m层
	private IContactsController iContactsController;
	private IFriendController iFriendController;

	public ContactsPresenter() {
		iContactsController = new ContactsController();
		iFriendController = new FriendController();
	}
	/**
	 * 联系人筛选(关键字)
	 */
	public void getConctacts(String keyWord, String userId) {
		List<Contacts> contactses = iContactsController.getContacts(keyWord, userId);
		CLog.e(TAG, "contactses:" + contactses.size());
		if (mView != null) {
			mView.setRecyerAdapter(contactses);
		}
	}

	/**
	 * 申请成为好友
	 */
	public void reqAddFriend(String url, String userId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iFriendController.reqAddFriend(url, userId, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						if (mView != null) {
							mView.showShortToast("等待对方验证");
							mView.showSuccess();
						}
					} else {
						//失败
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(entity.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 * 获取db中的联系人
	 */
	public void getContactsFromDb(String userId) {
		//联系人
		List<Contacts> contacts = iContactsController.getContactsFromDb(userId);
		//字母
		if (contacts != null && !contacts.isEmpty()) {
			//排序
			convertList(contacts);
			//首字母
			mView.reqFLetter(getFLetter(contacts));
		}
		//联系人
		if (mView != null)
			mView.setAdapter(contacts);
	}

	/**
	 * 获取首字母
	 *
	 * @param contacts
	 */
	private ArrayList<String> getFLetter(List<Contacts> contacts) {
		ArrayList<String> fLetters = new ArrayList<>();
		for (Contacts entity : contacts) {
			if (TextUtils.isEmpty(entity.getFLetter()))
				continue;
			if (!fLetters.contains(entity.getFLetter()))
				fLetters.add(entity.getFLetter());
		}
		return fLetters;
	}

	/**
	 * 移除 -
	 *
	 * @param contacts
	 */
	private void convertList(List<Contacts> contacts) {
		for (Contacts entity : contacts) {
			if (entity.getPhone().contains("-"))
				entity.setPhone(entity.getPhone().replace("-", ""));
		}
	}


}
