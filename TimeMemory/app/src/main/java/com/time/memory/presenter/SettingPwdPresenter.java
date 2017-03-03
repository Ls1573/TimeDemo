package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.MyPush;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.PushController;
import com.time.memory.model.RegistController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IPushController;
import com.time.memory.model.impl.IRegistController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.view.impl.ISettingPwdView;

/**
 * @author Qiu
 * @version V1.0
 * @Description:设置密码
 * @date 2016/9/23 13:30
 */
public class SettingPwdPresenter extends BasePresenter<ISettingPwdView> {
	private static final String TAG = "SettingPwdPresenter";
	// m层
	private ILoginController iLoginController;
	private IRegistController registController;
	private IPushController iPushController;

	public SettingPwdPresenter() {
		registController = new RegistController();
		iLoginController = new LoginController();
		iPushController = new PushController();
	}


	/**
	 * 注册
	 */
	public void reqRegist(final String url, final String mobile, final String verifyCode, final String pwd, final String nickName) {
		if (TextUtils.isEmpty(verifyCode)) {
			mView.showShortToast(context.getString(R.string.login_verifty_notice));
			return;
		}
		if (verifyCode.length() != 6) {
			mView.showShortToast(context.getString(R.string.login_verifty_length));
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			mView.showShortToast(context.getString(R.string.login_pwd_in));
			return;
		}

		if (pwd.length() < 6) {
			mView.showShortToast(context.getString(R.string.login_pwd_length));
			return;
		}

		if (TextUtils.isEmpty(nickName)) {
			mView.showShortToast(context.getString(R.string.login_nick_in));
			return;
		}
		mView.showLoadingDialog();
		//都ok
		registController.reqRegist(url, mobile, verifyCode, pwd, nickName,
				new SimpleCallback() {
					@Override
					public void onCallback(Object data) {
						//TODO
						if (mView != null) {
							if (data != null) {
								User entity = (User) data;
								entity.setUserPw(pwd);
								entity.setUserMobile(mobile);
								if (entity.getStatus() == 0) {
									//保存
									//保存数据
									iLoginController.saveUser(entity);
									iPushController.savePush(new MyPush(entity.getUserId()));

									MainApplication.setUserId(entity.getUserId());
									MainApplication.setUserToken(entity.getUserToken());
									//导入通讯录
									mView.importContacts();
								} else {
									mView.showShortToast(entity.getMessage());
									mView.showFaild();
								}
							} else {
								mView.showShortToast(context.getString(R.string.net_error));
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
	 * 发送验证码
	 */
	public void sendVerify(String url, String mobile) {
		if (TextUtils.isEmpty(mobile)) {
			mView.showShortToast("手机号不能为空");
			return;
		}
		if (TextUtils.isEmpty(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_notice));
			return;
		}
		if (mobile.length() != 11) {
			mView.showShortToast(context.getString(R.string.login_phone_length));
			return;
		}
		if (!RegUtils.checkPhone(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_length));
			return;
		}
		mView.showLoadingDialog();
		//都ok,发送验证码
		registController.reqVerify(url, mobile, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				//TODO
				if (mView != null) {
					if (data != null) {
						BaseEntity entity = (BaseEntity) data;
						if (entity.getStatus() == 0)
							mView.showFaild();
						else {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					} else {
						mView.showShortToast(context.getString(R.string.net_error));
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
}
