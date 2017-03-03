package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.mt.vo.ContactsVo;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.User;
import com.time.memory.model.ContactsController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.INewFriendiew;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:新的好友
 * @date 2016/9/24 9:29
 */
public class NewFriendPresenter extends BasePresenter<INewFriendiew> {
	private static final String TAG = "NewFriendPresenter";
	// m层
	private IContactsController iContactsController;

	public NewFriendPresenter() {
		iContactsController = new ContactsController();
	}


	/**
	 * 上传新好友
	 */
	public void addNewFriend(String userId, String name, String phone) {
		if (TextUtils.isEmpty(name)) {
			//用户名为空
			if (mView != null) {
				mView.showShortToast("用户名不能为空");
				return;
			}
		}
		if (TextUtils.isEmpty(phone)) {
			//号码为空
			if (mView != null) {
				mView.showShortToast("号码不能为空");
				return;
			}
		}
		if (phone.length() != 11) {
			if (mView != null)
				mView.showShortToast(context.getString(R.string.login_phone_length));
			return;
		}
		if (!RegUtils.checkPhone(phone)) {
			if (mView != null)
				mView.showShortToast(context.getString(R.string.login_phone_reg));
			return;
		}
		upConstacts(userId, name, phone);
	}

	/**
	 * 上传联系人(同步)
	 */
	public void upConstacts(final String userId, final String name, final String phone) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		//获取联系人
		ContactsVo contactsVo = new ContactsVo();
		contactsVo.setUserToken(MainApplication.getUserToken());
		contactsVo.setContactsVoList(convertVoList(name, phone));

		iContactsController.reqUpContacts(context.getString(R.string.FSUPCONSTACTS), contactsVo, new SimpleCallback() {
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
						mView.showShortToast(context.getString(R.string.net_error));
						mView.showFaild();
					}
				} else {
					User respVo = (User) data;
					if (respVo.getStatus() == 0) {
						//成功
						List<User> userVos = respVo.getUserVoList();
						if (userVos != null && !userVos.isEmpty()) {
							//存在数据,进行下步操作
							iContactsController.saveContacts(convertUserVos(userVos, name));
						}
						mView.showSuccess();
						mView.showShortToast("添加完成");
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(respVo.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 *
	 */
	public ArrayList<ContactsVo> convertVoList(String name, String phone) {
		ArrayList<ContactsVo> contactsVos = new ArrayList<>();
		ContactsVo vo;
		vo = new ContactsVo();
		vo.setUserMobile(phone);
		vo.setUserName(name);
		contactsVos.add(vo);
		return contactsVos;
	}

	/**
	 * UserVo->Contacts
	 */
	public List<Contacts> convertUserVos(List<User> userVos, String userName) {
		List<Contacts> contactsVos = new ArrayList<>();
		Contacts contacts;
		String fromUserId = MainApplication.getUserId();
		for (User user : userVos) {
			contacts = new Contacts();
			contacts.setActiveFlg(user.getActiveFlg());
			contacts.setUserId(user.getUserId());
			contacts.setContactName(userName);
			contacts.setHeadPhoto(user.getHeadPhoto());
			contacts.setPhone(user.getUserMobile().trim());
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
			contacts.setToUserId(fromUserId);
			contacts.setIsTwoWayFlg(user.getIsTwoWayFlg());
			contactsVos.add(contacts);
		}
		return contactsVos;
	}
}
