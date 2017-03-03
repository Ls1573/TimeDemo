package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.UpPhoneController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IUpPhoneController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.view.impl.IUpPhoneView;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:更换手机号
 * @date 2016/10/8 15:35
 */
public class SecurityPhonePresenter extends BasePresenter<IUpPhoneView> {
	// m层
	private ILoginController iLoginController;
	private IUpPhoneController iUpPhoneController;

	public SecurityPhonePresenter() {
		iUpPhoneController = new UpPhoneController();
		iLoginController = new LoginController();
	}

	/**
	 * 获取用户
	 *
	 * @param userId
	 */
	public void getUser(String userId) {
		User user = iLoginController.getUser(userId);
		if (mView != null) {
			mView.setUser(user);
		}
	}

	/**
	 * 更新用户
	 *
	 * @param user
	 */
	public void upUser(User user) {
		iLoginController.updateUser(user);
		if (mView != null)
			mView.showSuccess();
		mView.closeActivity();
	}

	/**
	 * 更换手机号
	 */
	public void reqUpPhone(String url, final String mobile, String verifyCode, final String userId) {
		if (TextUtils.isEmpty(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_notice));
			return;
		}
		if (TextUtils.isEmpty(verifyCode)) {
			mView.showShortToast(context.getString(R.string.login_verifty_notice));
			return;
		}
		if (!RegUtils.checkPhone(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_reg));
			return;
		}
		if (verifyCode.length() != 6) {
			mView.showShortToast(context.getString(R.string.login_verifty_length));
			return;
		}
		mView.showLoadingDialog();

		iUpPhoneController.reqEmail(url, mobile, verifyCode, new SimpleCallback() {
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
						//成功
						if (mView != null) {
							mView.upSuccess(mobile);
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
	 * 发送验证码
	 */
	public void sendVerify(String url, String mobile) {
		mView.showLoadingDialog();
		if (TextUtils.isEmpty(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_notice));
			return;
		}
		if (!RegUtils.checkPhone(mobile)) {
			mView.showShortToast(context.getString(R.string.login_phone_reg));
			return;
		}
		mView.showLoadingDialog();

		iUpPhoneController.reqVerify(url, mobile, new SimpleCallback() {
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
						//成功
						if (mView != null) {
							mView.showSuccess();
							mView.sendVerify();
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
}
