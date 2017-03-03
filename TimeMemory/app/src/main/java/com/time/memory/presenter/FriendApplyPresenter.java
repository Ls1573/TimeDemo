package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.User;
import com.time.memory.model.ContactsController;
import com.time.memory.model.FriendController;
import com.time.memory.model.MessageController;
import com.time.memory.model.ReportController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.IFriendController;
import com.time.memory.model.impl.IMessageController;
import com.time.memory.model.impl.IReportController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IFriendApplyView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:好友
 * @date 2016/10/8 10:24
 */
public class FriendApplyPresenter extends BasePresenter<IFriendApplyView> {

	private IFriendController iFriendController;
	private IReportController iReportController;
	private IMessageController iMessageController;
	private IContactsController iContactsController;

	public FriendApplyPresenter() {
		iReportController = new ReportController();
		iFriendController = new FriendController();
		iMessageController = new MessageController();
		iContactsController = new ContactsController();
	}

	/**
	 * 拒绝
	 */
	public void refuse(String msgId) {
		iMessageController.deleteBykey(msgId);
		if (mView != null) {
			mView.refuse();
		}
	}

	/**
	 * 接受(好友)
	 */
	public void accept(final String userId, final String msgId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iMessageController.reqAccept(context.getString(R.string.FSACCEPT), msgId, new SimpleCallback() {
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
					User entity = (User) data;
					if (entity.getStatus() == 0) {
						//成功
						saveContact(entity, userId, msgId);
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("添加成功");
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
	 * 获取好友资料
	 *
	 * @param userId
	 */
	public void reqFriendInfo(String url, String userId) {
		mView.showLoadingDialog();
		iFriendController.reqFriendInfo(url, userId, new SimpleCallback() {
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
					User user = (User) data;
					if (user.getStatus() == 0) {
						if (mView != null) {
							mView.setMessage(user.getUserVo());
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

	/**
	 * 举报用户
	 *
	 * @param url
	 * @param complainUserId
	 * @param complainType
	 * @param complainDetailType
	 */
	public void reqReport(String url, String complainUserId, String complainType, String complainDetailType) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iReportController.reqReport(url, complainUserId, null, null, null, complainType, complainDetailType, new SimpleCallback() {
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
						//成功
						if (mView != null) {
							mView.reportSuccess();
							mView.showShortToast("举报成功");
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

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

	/**
	 * 保存联系人
	 */
	private void saveContact(User user, String userId, String msgId) {
		Contacts contacts = new Contacts();
		contacts.setUserId(userId);
		contacts.setContactName(user.getUserName());
		contacts.setPhone(user.getUserMobile());
		contacts.setActiveFlg(user.getActiveFlg());
		if (!TextUtils.isEmpty(user.getUserName())) {
			//拼音
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
		}
		contacts.setToUserId(MainApplication.getUserId());
		iContactsController.saveContact(contacts);
	}
}
