package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.model.ContactsController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IReMarkView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:备注
 * @date 2016/11/30 10:28
 */
public class RemarkPresenter extends BasePresenter<IReMarkView> {
	private static final String TAG = "RemarkPresenter";

	private IContactsController iContactsController;

	public RemarkPresenter() {
		iContactsController = new ContactsController();
	}


	/**
	 * 修改备注信息
	 */
	public void upContacts(String url, final String friendId, final String friendName) {
		if (TextUtils.isEmpty(friendName)) {
			if (mView != null) {
				mView.showShortToast("请输入备注名");
				return;
			}
		}

		if (mView != null) {
			mView.showLoadingDialog();
		}
		iContactsController.reqUpContacts(url, friendId, friendName, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					Contacts entity = (Contacts) data;
					if (entity.getStatus() == 0) {
						//更改成功->改本地
						upContactsWDb(friendId, friendName);
						if (mView != null) {
							mView.showSuccess();
							mView.upSuccess(friendName);
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

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 更改本地
	 *
	 * @param friendId
	 * @param friendName
	 */
	private void upContactsWDb(String friendId, String friendName) {
		Contacts contacts = iContactsController.getContactFromDb(friendId, MainApplication.getUserId());
		if (contacts != null) {
			contacts.setContactName(friendName);
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
			iContactsController.saveContact(contacts);
		}
	}

}
