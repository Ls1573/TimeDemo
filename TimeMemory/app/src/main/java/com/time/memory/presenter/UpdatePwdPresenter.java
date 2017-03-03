package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.RetEntity;
import com.time.memory.model.RegistController;
import com.time.memory.model.UpPwdController;
import com.time.memory.model.impl.IRegistController;
import com.time.memory.model.impl.IUpPwdController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.view.impl.IUpPwdView;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:更换密码
 * @date 2016/10/8 15:35
 */
public class UpdatePwdPresenter extends BasePresenter<IUpPwdView> {
	// m层


	private IRegistController registController;
	private IUpPwdController upPwdController;

	public UpdatePwdPresenter() {
		upPwdController = new UpPwdController();
		registController = new RegistController();
	}

	/**
	 * 更换密码
	 */
	public void reqUpPwd(String url, String mobile, String verifyCode, String pwd) {
		if (TextUtils.isEmpty(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_notice));
			return;
		}
		if (TextUtils.isEmpty(verifyCode)) {
			mView.showShortToast(context.getString(R.string.login_verifty_notice));
			return;
		}

		if (TextUtils.isEmpty(pwd)) {
			mView.showShortToast(context.getString(R.string.login_pwd_in));
			return;
		}
		if (!RegUtils.checkPhone(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_reg));
			return;
		}
		if (pwd.length() < 6) {
			mView.showShortToast(context.getString(R.string.login_pwd_length));
			return;
		}
		if (verifyCode.length() != 6) {
			mView.showShortToast(context.getString(R.string.login_verifty_length));
			return;
		}
		mView.showLoadingDialog();

		upPwdController.reqUpPwd(url, mobile, verifyCode, pwd,
				new SimpleCallback() {
					@Override
					public void onCallback(Object data) {
						//TODO
						if (mView != null) {
							if (data != null) {
								RetEntity entity = (RetEntity) data;
								if (entity.getStatus() == 0) {
									mView.showSuccess();
									mView.upSuccess();
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
			mView.showShortToast(context.getString(R.string.login_phone_reg));
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
						if (entity.getStatus() == 0) {
							mView.sendVerify();
							mView.showSuccess();
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
}
