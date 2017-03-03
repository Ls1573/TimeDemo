package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.RegistController;
import com.time.memory.model.impl.IRegistController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.RegUtils;
import com.time.memory.view.impl.IRegistView;

/**
 * @author Qiu
 * @version V1.0
 * @Description:注册
 * @date 2016/9/23 13:30
 */
public class RegistPresenter extends BasePresenter<IRegistView> {
	// m层

	private IRegistController registController;

	public RegistPresenter() {
		registController = new RegistController();
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
						if (entity.getStatus() == 0)
							mView.showSuccess();
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
