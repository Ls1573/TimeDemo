package com.time.memory.presenter;

import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.ISecurityView;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:个人设置
 * @date 2016-9-5下午1:23:43
 * ==============================
 */
public class SettingPresenter extends BasePresenter<ISecurityView> {
	private ILoginController iLoginController;
	// m层

	public SettingPresenter() {
		iLoginController = new LoginController();
	}

	public void getUser(String key) {
		User user = iLoginController.getUser(key);
		if (mView != null) {
			mView.setUser(user);
		}
	}
}
