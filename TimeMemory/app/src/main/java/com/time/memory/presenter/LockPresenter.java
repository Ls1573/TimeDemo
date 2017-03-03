package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.User;
import com.time.memory.model.GroupController;
import com.time.memory.model.LockController;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILockController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IBindLockView;

/**
 * @author Qiu
 * @version V1.3
 * @Description:手势密码
 * @date 2017/1/12 10:05
 */
public class LockPresenter extends BasePresenter<IBindLockView> {

	private ILockController lockController;
	private IGroupController iGroupController;
	private ILoginController iLoginController;

	public LockPresenter() {
		lockController = new LockController();
		iGroupController = new GroupController();
		iLoginController = new LoginController();
	}

	/**
	 * 设置手机密码
	 */
	public void reqPattern(String url, final String groupPw, final MGroup mGroup, final String message, final boolean isForget) {
		if (mView != null) {
			mView.showLoadingDialog();
		}

		lockController.reqPattern(url, mGroup.getGroupId(), groupPw, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}

			@Override
			public void onCallback(Object data) {
				if (mView == null) return;
				if (data == null) {
					mView.showShortToast(context.getString(R.string.net_error));
					mView.showFaild();
				} else {
					//TODO 需要保存用户信息到本地db
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						saveGroup(mGroup, groupPw);
						mView.showShortToast(message);
						if (isForget)
							mView.resetParren();
						else
							mView.showSuccess();
					} else {
						mView.showShortToast(entity.getMessage());
						mView.showFaild();
					}
				}
			}
		});
	}

	/**
	 * 验证手势密码
	 */
	public void veriftyPattern(String groupPwd, String cParrtern) {
		if (groupPwd.equals(cParrtern)) {
			if (mView != null) {
				mView.showSuccess();
			}
		} else {
			if (mView != null) {
				mView.sendErrorMessage("密码错误,请重新输入");
			}
		}
	}

	/**
	 * 验证手势密码
	 */
	public void veriftyCanclePattern(MGroup mGroup, String cParrtern) {
		if (mGroup.getGroupPw().equals(cParrtern)) {
			reqPattern(context.getString(R.string.FSPATTERN), "", mGroup, "取消成功", false);
		} else {
			if (mView != null) {
				mView.sendErrorMessage("密码错误,请重新输入");
			}
		}
	}

	/**
	 * 忘记手势密码
	 */
	public void forgetPattern(String userId, String userPwd, String url, final MGroup mGroup, final boolean isForget) {
		if (TextUtils.isEmpty(userPwd)) {
			if (mView != null)
				mView.showShortToast(context.getString(R.string.login_pwd_in));
			return;
		}
		User user = iLoginController.getUser(userId);
		if (userPwd.equals(user.getUserPw())) {
			//可以取消
			if (isForget)
				reqPattern(url, "", mGroup, "请重新设置手势密码", isForget);
			else
				reqPattern(url, "", mGroup, "取消成功", isForget);
		} else {
			//密码输入错误
			if (mView != null)
				mView.showShortToast(context.getString(R.string.login_pwd_error));
		}
	}

	/**
	 * 保存群信息
	 *
	 * @param mGroup
	 * @param cPattern
	 */
	private void saveGroup(MGroup mGroup, String cPattern) {
		mGroup.setGroupPw(cPattern);
		iGroupController.upMGroup(mGroup);
	}
}
