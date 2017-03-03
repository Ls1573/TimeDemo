package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.MGroup;
import com.time.memory.model.FriendController;
import com.time.memory.model.GroupController;
import com.time.memory.model.impl.IFriendController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IScanView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:扫描操作
 * @date 2016/10/18 18:18
 */
public class ScanPresenter extends BasePresenter<IScanView> {
	private static final String TAG = "ScanPresenter";
	// m层
	private IGroupController iGroupController;
	private IFriendController iFriendController;

	public ScanPresenter() {
		iGroupController = new GroupController();
		iFriendController = new FriendController();
	}


	/**
	 * 检索信息
	 */
	public void reqMessage(String groupUrl, String userUrl, String message) {
		if (TextUtils.isEmpty(message)) {
			if (mView != null) {
				mView.showShortToast("扫描出错了 ...");
			}
			return;
		}
		if (message.contains("sgjy_group_")) {
			//群的
			reqGetGroup(groupUrl, message.substring(message.lastIndexOf("_") + 1));
		} else if (message.contains("sgjy_user_")) {
			//用户的
			getUserById(message);
		} else {
			//其他情况
			if (mView != null) {
				mView.showShortToast("二维码错误,请重新扫描");
				mView.sacnError();
			}
		}
	}

	/**
	 * 获取用户-判断是不是好友
	 */
	private void getUserById(String msg) {
		String nMsg = msg.substring("sgjy_user_".length());
		String[] msgs = nMsg.split("&spt;");
		String userId = msgs[0];
		String userName = msgs[1];
		String userPic = "";
		if (msgs.length > 2)
			userPic = msgs[2];

		if (userId.equals(MainApplication.getUserId())) {
			if (mView != null)
				mView.showShortToast("当前用户,请重新扫描");
			return;
		}

		Contacts contacts = (Contacts) iFriendController.getContactsById(userId, MainApplication.getUserId());
		if (contacts != null) {
			//存在用户
			if (mView != null) {
				mView.startFriend(userId, userName);
			}
		} else {
			//不存在
			if (mView != null) {
				mView.repleyFriend(userId, userName, userPic);
			}
		}
	}

	/**
	 * 获取群信息
	 *
	 * @param
	 * @reurn
	 */
	public void reqGetGroup(String groupUrl, String groupId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iGroupController.reqGroup(groupUrl, groupId, new SimpleCallback() {
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
					MGroup entity = (MGroup) data;
					if (entity.getStatus() == 0) {
						if (entity.getGroupInfoVoList() != null && !entity.getGroupInfoVoList().isEmpty()) {
							//成功
							mView.showSuccess();
							mView.startGroup(entity.getGroupInfoVoList().get(0));
						}
					} else {
						mView.showShortToast(entity.getMessage());
						mView.showFaild();
					}
				}
			}
		});

	}
}
