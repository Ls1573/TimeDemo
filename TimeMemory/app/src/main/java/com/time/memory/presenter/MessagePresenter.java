package com.time.memory.presenter;

import com.time.memory.MainApplication;
import com.time.memory.mt.nio.message.response.SM01RespVo;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Message;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.MessageController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IMessageController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IMessageView;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:消息
 * @date 2016-9-12上午09:20:15
 * ==============================
 */
public class MessagePresenter extends BasePresenter<IMessageView> {
	private static final String TAG = "MessagePresenter";
	// m层
	private IMessageController iMessageController;
	private ILoginController iLoginController;

	public MessagePresenter() {
		iMessageController = new MessageController();
		iLoginController = new LoginController();
	}

	/**
	 * 删除消息
	 *
	 * @param msg
	 */
	public void removeMessage(Message msg, int position) {
		if (msg == null) return;
		if (position == 0 || position == -1) return;
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iMessageController.deleteBykey(msg.getUuid());
		if (mView != null) {
			mView.showSuccess();
			mView.removeAdapter(position);
		}
	}


	/**
	 * 获取数据
	 *
	 * @param userId
	 */
	public void getMessage(String userId) {
		//先获取db里的
		getMessageFromDb(userId);
		User user = iLoginController.getUser(userId);
		reqMessage(context.getString(R.string.FSMESSAGE), userId, user);
	}

	/**
	 * 插入数据
	 */
	public void addMessage(SM01RespVo reqVo, String userId) {
		if (reqVo.getCode() == 0) {
			//成功
			Message message = new Message();
			message.setUuid(reqVo.getUuid());
			message.setMessageType(reqVo.getMessageType());
			message.setMessageDetail(reqVo.getMessageDetail());
			message.setSendUserId(reqVo.getSendUserId());
			message.setSendUserName(reqVo.getSendUserName());
			message.setSendUserPhoto(reqVo.getSendUserPhoto());
			message.setReadFlg("0");
			message.setHasDeal("0");
			message.setInsDate(reqVo.getInsDate());
			message.setInsTime(reqVo.getInsTime());
			message.setUserId(userId);
			iMessageController.addMessage(message);
			getMessageFromDb(userId);
		}
	}

	/**
	 * 获取数据
	 *
	 * @param userId
	 * @param isChange
	 */
	public void getMessage(String userId, boolean isChange) {
		if (isChange)
			getMessageFromDb(userId);
	}

	/**
	 * 从db中读取数据
	 */
	public void getMessageFromDb(String userId) {
		List<Message> msgs = iMessageController.getMessages(userId);
		//未读消息数量
		int unRead = getUnDispose(msgs);
		Message unMessage = new Message();
		unMessage.setMessageDetail(String.valueOf(unRead));
		unMessage.setMessageType("unMessage");
		//已处理消息
		List<Message> disposes = getDispose(msgs);
		disposes.add(0, unMessage);
		if (mView != null) {
			mView.setAdapter(disposes);
		}
	}

	/**
	 * 发送已读消息通知
	 */
	public void sendMsg(String url, String userToken, String userId) {
		iMessageController.setRead(url, userToken, new SimpleCallback() {
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
						//视图不为空
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					Message msg = (Message) data;
					if (msg.getStatus() == 0) {
//						iMessageController.updateMessage(list);
					}
				}
			}
		});
	}

	/**
	 * 获取信息(Http)
	 *
	 * @param
	 * @reurn
	 */
	public void reqMessage(String url, final String userId, final User user) {
		iMessageController.reqMessage(url, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
//					mView.showFaild();
				}
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					Message respVo = (Message) data;
					if (respVo.getStatus() == 0) {
						//成功
						List<Message> list = respVo.getMessageVoList();
						//存在数据,进行下步操作
						for (Message message : list) {
							message.setHasDeal("0");
							message.setUserId(userId);
						}
						//保存
						iMessageController.saveMesageList(list);
						//修改user状态
						user.setImportmessage(1);
						iLoginController.updateUser(user);
						//获取
						getMessageFromDb(userId);
						//设置已读
						sendMsg(context.getString(R.string.FSMSGREAD), MainApplication.getUserToken(), MainApplication.getUserId());
					} else {
						if (mView != null) {
//							mView.showFaild();
							mView.showShortToast(respVo.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 * 获取待处理消息
	 *
	 * @return
	 */
	private int getUnDispose(List<Message> msgs) {
		int total = 0;
		for (Message entity : msgs) {
			if (entity.getMessageType().startsWith("9")) {
				total++;
			}
		}
		return total;
	}

	/**
	 * 未读消息
	 *
	 * @param msgs
	 * @return
	 */
	private List<Message> getDispose(List<Message> msgs) {
		List<Message> list = new ArrayList<>();
		for (Message entity : msgs) {
			if (entity.getMessageType().startsWith("0")) {
				list.add(entity);
			}
		}
		return list;
	}
}
