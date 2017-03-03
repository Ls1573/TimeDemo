package com.time.memory.presenter;

import com.time.memory.MainApplication;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IMineView;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:我的
 * @date 2016-9-5上午11:02:44
 * ==============================
 */
public class MinePresenter extends BasePresenter<IMineView> {
	private static final String TAG = "MinePresenter";
	// m层
	private ILoginController iLoginController;

	public MinePresenter() {
		iLoginController = new LoginController();
	}

	/**
	 * 获取用户信息
	 */
	public void getUser(String userId) {
		User user = iLoginController.getUser(userId);
		if (mView != null) {
			mView.setUserInfos(user);
		}
	}

	/**
	 * 退出
	 *
	 * @param userId
	 */
	public void loginOut(String userId) {
		iLoginController.updateUser(userId, "active", "0");
		MainApplication.setUserId("-1");
		MainApplication.setUserToken("");
		CLog.e(TAG, "Id:" + MainApplication.getUserId());
	}

}
