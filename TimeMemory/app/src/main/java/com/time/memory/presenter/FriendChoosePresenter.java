package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.GroupContacts;
import com.time.memory.model.FriendController;
import com.time.memory.model.impl.IFriendController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IFriendChooseView;

/**
 * @author Qiu
 * @version V1.3
 * @Description:
 * @date 2017/1/3 13:45
 */
public class FriendChoosePresenter extends BasePresenter<IFriendChooseView> {

	private IFriendController iFriendController;

	public FriendChoosePresenter() {
		iFriendController = new FriendController();
	}

	/**
	 * 查找好友
	 *
	 * @param url
	 * @param mobile
	 */
	public void searchFriend(String url, String mobile) {
		if (TextUtils.isEmpty(mobile)) {
			if (mView != null) {
				mView.showShortToast(context.getString(R.string.call_empty));
				return;
			}
		}
		if (mView != null)
			mView.showLoadingDialog();

		iFriendController.reqFriends(url, mobile, new SimpleCallback() {
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
					GroupContacts user = (GroupContacts) data;
					if (user.getStatus() == 0) {
						if (mView != null) {
							mView.setAdapter(user.getUserVoList());
							mView.showSuccess();
						}
					} else {
						//出错了
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(user.getMessage());
						}
					}
				}
			}
		});
	}
}
