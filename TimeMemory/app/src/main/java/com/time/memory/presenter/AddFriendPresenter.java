package com.time.memory.presenter;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.FriendController;
import com.time.memory.model.impl.IFriendController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IBaseView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:好友管理
 * @date 2016/10/20 14:44
 */
public class AddFriendPresenter extends BasePresenter<IBaseView> {
	private static final String TAG = "AddFriendPresenter";
	// m层
	private IFriendController iFriendController;

	public AddFriendPresenter() {
		iFriendController = new FriendController();
	}


	/**
	 * 申请成为好友
	 */
	public void reqAddFriend(String url, String userId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iFriendController.reqAddFriend(url, userId, new SimpleCallback() {
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
						if (mView != null) {
							mView.showSuccess();
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
