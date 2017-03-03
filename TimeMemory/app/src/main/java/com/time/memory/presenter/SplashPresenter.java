package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.login.ImportContactsActivity;
import com.time.memory.view.activity.login.LoginActivity;
import com.time.memory.view.impl.ISplashView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:启动页
 * @date 2016/10/11 16:48
 */
public class SplashPresenter extends BasePresenter<ISplashView> {
	private static final String TAG = "LoginPresenter";
	// m层
	private ILoginController iLoginController;

	public SplashPresenter() {
		iLoginController = new LoginController();
	}

	/**
	 * @param
	 * @Description: 销毁持有的View
	 */
	@Override
	public void detachView() {
		super.detachView();
		if (iLoginController != null) iLoginController = null;
	}

	/**
	 * 查询用户
	 */
	public void reqUser() {
		iLoginController.reqUser(new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					//无用户
					mView.redirectTo(LoginActivity.class);
				} else {
					User u = (User) data;
					if (TextUtils.isEmpty(u.getUserId())) {
						//无用户,登录
						mView.redirectTo(LoginActivity.class);
					} else {
						//存在用户
						MainApplication.setUserId(u.getUserId());
						MainApplication.setUserToken(u.getUserToken());
						if (mView != null) {
							mView.initJpush(u.getUserId());
						}
						if (u.getImportContactsStatus().equals("1")) {
							//已到通讯录
							mView.redirectTo(MainActivity.class);
						} else {
							//未导入通讯录
							mView.redirectTo(ImportContactsActivity.class);
						}
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
				}
			}
		});
	}

}
