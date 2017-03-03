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
import com.time.memory.view.impl.IVeriftyEmailView;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:更换手机号
 * @date 2016/10/8 15:35
 */
public class SecurityEmailPresenter extends BasePresenter<IVeriftyEmailView> {
	// m层
	private ILoginController iLoginController;
	private IUpPhoneController iUpPhoneController;

	public SecurityEmailPresenter() {
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
	 * 验证
	 */
	public void reqVerify(String url, String eVerifty) {
		//没有验证码
		if (TextUtils.isEmpty(eVerifty)) {
			if (mView != null)
				mView.showShortToast("请输入验证码");
			return;
		}
		//格式错误
		if (eVerifty.length() != 6) {
			if (mView != null)
				mView.showShortToast(context.getString(R.string.login_emailcode_error));
			return;
		}
		if (mView != null) {
			mView.showLoadingDialog();
		}

		//开始绑定
		iUpPhoneController.reqVerifyEmail(url, eVerifty, new SimpleCallback() {
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
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						if (mView != null) {
							mView.showSuccess();
						}
					} else {
						//失败
						if (mView != null) {
							mView.showShortToast(entity.getMessage());
							mView.showFaild();
						}
					}

				}
			}
		});
	}

	/**
	 * 发送验证码
	 */
	public void sendVerify(String url, String email) {
		if (TextUtils.isEmpty(email)) {
			mView.showShortToast("邮箱不能为空");
			return;
		}
		if (!RegUtils.isEmail(email)) {
			mView.showShortToast(context.getString(R.string.login_email_error));
			return;
		}
		mView.showLoadingDialog();

		iUpPhoneController.reqEmailVerify(url, email, new SimpleCallback() {
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
