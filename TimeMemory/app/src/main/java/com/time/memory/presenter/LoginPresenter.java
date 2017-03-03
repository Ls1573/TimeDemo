package com.time.memory.presenter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.MyPush;
import com.time.memory.entity.User;
import com.time.memory.model.ContactsController;
import com.time.memory.model.LoginController;
import com.time.memory.model.PushController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IPushController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.ILoginView;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:登录
 * @date 2016-9-5下午4:51:33
 * ==============================
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
	private static final String TAG = "LoginPresenter";
	// m层
	private ILoginController iLoginController;
	private IPushController iPushController;
	private IContactsController iContactsController;


	public LoginPresenter() {
		iContactsController = new ContactsController();
		iLoginController = new LoginController();
		iPushController = new PushController();
	}

	/**
	 * 注册检测
	 *
	 * @param phone
	 */
	public void reqRegister(String phone) {
		if (phone.length() != 11) {
			phone = "";
		}
		if (!RegUtils.checkPhone(phone)) {
			phone = "";
		}
		if (mView != null) {
			mView.reqRegister(phone);
		}
	}

	/**
	 * 请求登录
	 *
	 * @param url
	 * @param phone
	 * @param pwd
	 */
	public void requestLogin(final String url, final String phone, final String pwd) {
		if (TextUtils.isEmpty(phone)) {
			mView.showShortToast(context.getString(R.string.login_phone_notice));
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			mView.showShortToast(context.getString(R.string.login_pwd_in));
			return;
		}
		if (phone.length() != 11) {
			mView.showShortToast(context.getString(R.string.login_phone_length));
			return;
		}
		if (pwd.length() < 6) {
			mView.showShortToast(context.getString(R.string.login_pwd_length));
			return;
		}
		if (!RegUtils.checkPhone(phone)) {
			mView.showShortToast(context.getString(R.string.login_phone_reg));
			return;
		}
		mView.showLoadingDialog();
		iLoginController.reqLogin(url, phone, pwd, getVersion(), new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (mView == null) return;
				if (data == null) {
					mView.showShortToast(context.getString(R.string.net_error));
					mView.showFaild();
				} else {
					//TODO 需要保存用户信息到本地db
					User entity = (User) data;
					entity.setUserPw(pwd);
					if (entity.getStatus() == 0) {
						//保存数据
						iLoginController.saveUser(entity);
						iPushController.savePush(new MyPush(entity.getUserId()));
						MainApplication.setUserId(entity.getUserId());
						MainApplication.setUserToken(entity.getUserToken());
						if (entity.getImportContactsStatus().equals("1")) {
							//已经导入了通讯录
							//获取通讯录,保存到本地
							reqContacts(entity);
						} else {
							//未导入通讯录 跳转到到通讯录页面
							if (mView != null) {
								mView.reqImportConstacts();
							}
						}
					} else {
						mView.showShortToast(entity.getMessage());
						mView.showFaild();
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
	 * 请求联系人
	 */
	public void reqContacts(final User user) {
		//请求联系人
		iContactsController.reqContacts(context.getString(R.string.FSREQFriends), user.getUserToken(), new SimpleCallback() {
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
					}
				} else {
					User respVo = (User) data;
					if (respVo.getStatus() == 0) {
						//成功
						List<User> userVos = respVo.getUserVoList();
						if (userVos != null && !userVos.isEmpty()) {
							//存在数据,进行下步操作
							iContactsController.saveContacts(convertUserVos(userVos));
						}
						if (mView != null)
							mView.showSuccess();
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
	 * UserVo->Contacts
	 */
	public List<Contacts> convertUserVos(List<User> userVos) {
		List<Contacts> contactsVos = new ArrayList<>();
		Contacts contacts;
		//来源关联
		String fromUserId = MainApplication.getUserId();
		for (User user : userVos) {
			contacts = new Contacts();
			contacts.setActiveFlg(user.getActiveFlg());
			contacts.setUserId(user.getUserId());
			contacts.setContactName(TextUtils.isEmpty(user.getUserName()) ? "#" : user.getUserName());
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

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "1.1.0";
		}
	}

}
