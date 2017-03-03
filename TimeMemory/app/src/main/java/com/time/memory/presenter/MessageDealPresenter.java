package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.Message;
import com.time.memory.entity.User;
import com.time.memory.model.ContactsController;
import com.time.memory.model.MessageController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.IMessageController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IMessageDealView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:消息处理
 * @date 2016/10/15 16:01
 */
public class MessageDealPresenter extends BasePresenter<IMessageDealView> {
	// m层
	private IMessageController iMessageController;
	private IContactsController iContactsController;

	public MessageDealPresenter() {
		iMessageController = new MessageController();
		iContactsController = new ContactsController();
	}

	/**
	 * 删除信息
	 *
	 * @param key
	 */
	private void deleteMsg(String key) {
		iMessageController.deleteBykey(key);
	}

	/**
	 * 更新消息
	 *
	 * @param msgId
	 */
	private void updateMsg(String msgId) {
		Message msg = (Message) iMessageController.getMessages(MainApplication.getUserId(), msgId);
		if (msg != null) {
			msg.setHasDeal("1");
			msg.setReadFlg("1");
			iMessageController.upMessage(msg);
		}
	}

	/**
	 * 获取待处理信息
	 *
	 * @param
	 * @reurn
	 */
	public void getMessage(String userId) {
		List<Message> msgs = iMessageController.getMessages(userId);
		// TODO
		if (mView != null) {
			mView.setAdapter(getUnDispose(msgs));
		}
	}


	/**
	 * 拒绝
	 */
	public void refuse(String msgId, int positoin) {
		//移除
		iMessageController.deleteBykey(msgId);
		if (mView != null) {
			mView.refreshAdapter(positoin, -1);
		}
	}

	/**
	 * 接受(好友)
	 */
		public void accept(final String userId, final String msgId, final int positoin) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iMessageController.reqAccept(context.getString(R.string.FSACCEPT), userId, new SimpleCallback() {
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
					updateMsg(msgId);
					User entity = (User) data;
					if (entity.getStatus() == 0) {
						saveContact(entity, userId, msgId);
						//成功
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
					mView.refreshAdapter(positoin, 1);
				}
			}
		});
	}

	/**
	 * 通过(圈子)
	 */
	public void through(final String userId, String msg, final int positoin) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		final String[] msgs = msg.split("&spt;");
		iMessageController.reqThrough(context.getString(R.string.FSTHROUGH), userId, msgs[0], new SimpleCallback() {
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
					updateMsg(msgs[0]);
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						deleteMsg(msgs[0]);
						//成功
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("添加成功");
							mView.refreshAdapter(positoin, 2);
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
	 * 保存联系人
	 */
	private void saveContact(User user, String userId, String msgId) {
		Contacts contacts = new Contacts();
		contacts.setUserId(userId);
		contacts.setContactName(user.getUserName());
		contacts.setPhone(user.getUserMobile());
		contacts.setActiveFlg(user.getActiveFlg());
		contacts.setHeadPhoto(user.getHeadPhoto());
		contacts.setIsTwoWayFlg(user.getIsTwoWayFlg());

		if (!TextUtils.isEmpty(user.getUserName())) {
			//拼音
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
		}
		contacts.setToUserId(MainApplication.getUserId());
		iContactsController.saveContact(contacts);
		//移除
		deleteMsg(msgId);
	}

	/**
	 * 获取待处理消息
	 *
	 * @return
	 */
	private List<Message> getUnDispose(List<Message> msgs) {
		List<Message> list = new ArrayList<>();
		for (Message entity : msgs) {
			if (entity.getMessageType().startsWith("9")) {
				list.add(entity);
			}
		}
		return list;
	}
}
