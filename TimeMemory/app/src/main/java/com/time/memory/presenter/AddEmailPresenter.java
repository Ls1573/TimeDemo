package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.User;
import com.time.memory.model.BindController;
import com.time.memory.model.LoginController;
import com.time.memory.model.UpPhoneController;
import com.time.memory.model.impl.IBindEmailController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IUpPhoneController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.view.impl.IBindEmailView;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:绑定邮箱
 * @date 2016/11/24 11:37
 */
public class AddEmailPresenter extends BasePresenter<IBindEmailView> {
	private static final String TAG = "AddEmailPresenter";

	private IBindEmailController iBindEmailController;
	private IUpPhoneController iUpPhoneController;
	private ILoginController iLoginController;

	public AddEmailPresenter() {
		iBindEmailController = new BindController();
		iUpPhoneController = new UpPhoneController();
		iLoginController = new LoginController();
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

	/**
	 * 绑定邮箱
	 *
	 * @param email
	 */
	public void reqBindEmail(String url, final String email, String eVerifty) {
		//没有邮箱
		if (TextUtils.isEmpty(email)) {
			if (mView != null)
				mView.showShortToast("请输入邮箱");
			return;
		}
		//格式错误
		if (!RegUtils.isEmail(email)) {
			if (mView != null)
				mView.showShortToast(context.getString(R.string.login_email_error));
			return;
		}

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
		iBindEmailController.reqBind(url, email, eVerifty, new SimpleCallback() {
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
						//成功->保存到本地
						saveEmail(email);
						if (mView != null) {
							mView.bindSuccess(true, email);
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
	 * 保存
	 *
	 * @param email
	 */
	private void saveEmail(String email) {
		User user = iLoginController.getUser(MainApplication.getUserId());
		user.setEmail(email);
		iLoginController.saveUser(user);
	}
}
