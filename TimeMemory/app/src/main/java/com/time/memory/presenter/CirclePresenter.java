package com.time.memory.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.User;
import com.time.memory.model.CircleController;
import com.time.memory.model.ContactsController;
import com.time.memory.model.GroupController;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.mt.nio.message.response.SG02RespVo;
import com.time.memory.mt.vo.ContactsVo;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.ICircleView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:圈子
 * @date 2016-9-9下午4:54:08
 * ==============================
 */
public class CirclePresenter extends BasePresenter<ICircleView> {
	private static final String TAG = "CirclePresenter";
	// m层
	private IContactsController iContactsController;
	private ILoginController iLoginController;
	private ICircleController iCircleController;
	private ExecutorService executorManager;
	private IGroupController iGroupController;

	public CirclePresenter() {
		iContactsController = new ContactsController();
		iCircleController = new CircleController();
		iLoginController = new LoginController();
		executorManager = ExecutorManager.getInstance();
		iGroupController = new GroupController();
	}

	@Override
	public void detachView() {
		super.detachView();
		if (mHanlder != null) {
			mHanlder.removeCallbacksAndMessages(null);
			mHanlder = null;
		}
	}

	/**
	 * 移除好友
	 */
	public void reqRemoveFriend(String url, final String friendId, final int position) {
		if (mView != null)
			mView.showLoadingDialog();
		iContactsController.reqRemoveFriend(url, friendId, new SimpleCallback() {
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
						iContactsController.removeFriend(friendId);
						if (mView != null) {
							mView.removeFriend(position);
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


	/**
	 * 新增新好友
	 */
	public void addContats(String userId, SG02RespVo respVo) {
		if (respVo.getCode() != 0) return;
		Contacts contacts;
		contacts = new Contacts();
		contacts.setActiveFlg(respVo.getActiveFlg());
		contacts.setUserId(respVo.getUserId());
		contacts.setContactName(respVo.getUserName());
		contacts.setHeadPhoto(respVo.getHeadPhoto());
		contacts.setPhone(respVo.getUserMobile());
		contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
		contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
		contacts.setToUserId(userId);
		contacts.setIsTwoWayFlg(respVo.getIsTwoWayFlg());
		iContactsController.saveContact(contacts);
		//刷新
		getContactsFromDb(userId);
	}

	private Handler mHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				//上传
				ContactsVo contactsVo = msg.getData().getParcelable("contactsVo");
				String userId = msg.getData().getString("userId");
				upConstacts(userId, contactsVo);
			} else if (msg.what == -1) {
				//退出,没有数据需要上传
				if (mView != null) {
					//上传完成
					mView.showSuccess();
					mView.showShortToast("同步完成");
				}
			}
		}
	};

	/**
	 * 上传联系人(同步)
	 */
	public void upConstacts(final String userId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		executorManager.submit(new Runnable() {
			@Override
			public void run() {
				//手机通讯录的数据
				ArrayList<ContactsVo> contacts = iContactsController.getContacts(context);
				//保存到临时表,比较,取数据
				//保存
				iContactsController.insertContactsTemp(contacts);
				//获取差集(最新的)
				ArrayList<ContactsVo> nContactses = iContactsController.getFrContactsFromDb(MainApplication.getUserId());
				if (nContactses == null || nContactses.isEmpty()) {
					//没有数据,不执行了
					mHanlder.sendEmptyMessage(-1);
				} else {
					//存在数据,上传
					ContactsVo contactsVo = new ContactsVo();
					contactsVo.setUserToken(MainApplication.getUserToken());
					contactsVo.setContactsVoList(nContactses);

					Bundle bundle = new Bundle();
					bundle.putParcelable("contactsVo", contactsVo);
					bundle.putString("userId", userId);
					Message msg = mHanlder.obtainMessage();
					msg.what = 1;
					msg.setData(bundle);
					mHanlder.sendMessage(msg);
				}
			}
		});
	}


	/**
	 * 上传联系人(同步)
	 */
	public void upConstacts(final String userId, ContactsVo contactsVo) {
		iContactsController.reqUpContacts(context.getString(R.string.FSUPCONSTACTS), contactsVo, new SimpleCallback() {
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
						mView.showShortToast(context.getString(R.string.net_error));
						mView.showFaild();
					}
				} else {
					User respVo = (User) data;
					if (respVo.getStatus() == 0) {
						//成功
						List<User> userVos = respVo.getUserVoList();
						if (userVos != null && !userVos.isEmpty()) {
							//存在数据,进行下步操作
							iContactsController.saveContacts(convertUserVos(userVos));
							//更新TA的记忆圈子人数
							mView.nofityMemoryNum(upGroup(userVos));
						}
						//更改状态
						iLoginController.updateUser(userId);
						mView.refreshContacts();
						mView.showSuccess();
						mView.showShortToast("同步完成");
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(respVo.getMessage());
						}
					}
				}
			}
		});
	}

	/**
	 * 联系人筛选(关键字)
	 */
	public void getConctacts(String keyWord, String userId) {
		List<Contacts> contactses = iContactsController.getContacts(keyWord, userId);
		CLog.e(TAG, "contactses:" + contactses.size());
		if (mView != null) {
			mView.setRecyerAdapter(contactses);
		}
	}


	/**
	 * 更新他的记忆好友数
	 */
	private String upGroup(List<User> userVos) {
		String num = "";
		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		if (mGroup != null) {
			int cCount = 0;
			String count = mGroup.getGroupCount();
			if (!TextUtils.isEmpty(count))
				cCount = Integer.parseInt(count);
			num = String.valueOf(cCount + userVos.size());
			mGroup.setGroupCount(num);
			iGroupController.upMGroup(mGroup);
		}
		return num;
	}

	/**
	 * 获取db中的联系人
	 */
	public void getContactsFromDb(String userId) {
		//联系人(只获取是双向的的好友)
		List<Contacts> contacts = iContactsController.getContactsFromDb(userId, "0");
		//字母
		if (contacts != null && !contacts.isEmpty()) {
			//排序
			convertList(contacts);
			//首字母
			mView.reqFLetter(getFLetter(contacts));
		}
		//联系人
		if (mView != null)
			mView.setAdapter(contacts);
	}

	/**
	 * 请求联系人
	 *
	 * @param url
	 * @param userToken
	 */
	public void reqContacts(String url, String userToken, final String userId) {
		//请求联系人
		iContactsController.reqContacts(url, userToken, new SimpleCallback() {
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
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					User respVo = (User) data;
					if (respVo.getStatus() == 0) {
						//成功
						List<User> userVos = respVo.getUserVoList();
						if (userVos != null && !userVos.isEmpty()) {
							//存在数据,进行下步操作
							iContactsController.saveContacts(convertUserVos(userVos));
							getContactsFromDb(userId);
						} else {
							if (mView != null) {
								mView.showFaild();
								mView.showShortToast(respVo.getMessage());
							}
						}
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(respVo.getMessage());
						}
					}
				}
			}
		});
	}

	/**
		 * UserVo->Contacts
	 */
	public List<Contacts> convertUserVos(List<User> userVos) {
		List<Contacts> contactsVos = new ArrayList<>();
		Contacts contacts;
		String fromUserId = MainApplication.getUserId();
		for (User user : userVos) {
			contacts = new Contacts();
			contacts.setActiveFlg(user.getActiveFlg());
			contacts.setUserId(user.getUserId());
			contacts.setContactName(TextUtils.isEmpty(user.getUserName()) ? "#" : user.getUserName());
			contacts.setHeadPhoto(user.getHeadPhoto());
			contacts.setPhone(user.getUserMobile().trim());
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
			contacts.setToUserId(fromUserId);
			contacts.setIsTwoWayFlg(user.getIsTwoWayFlg());
			contactsVos.add(contacts);
		}
		return contactsVos;
	}

	/**
	 * @param contacts
	 */
	public List<ContactsVo> convertVoList(List<Contacts> contacts) {
		if (contacts == null) contacts = new ArrayList<>();
		List<ContactsVo> contactsVos = new ArrayList<>();
		ContactsVo vo;
		for (Contacts entity : contacts) {
			vo = new ContactsVo();
			vo.setUserMobile(entity.getPhone());
			vo.setUserName(entity.getContactName());
			contactsVos.add(vo);
		}
		return contactsVos;
	}


	/**
	 * 获取首字母
	 *
	 * @param contacts
	 */
	private ArrayList<String> getFLetter(List<Contacts> contacts) {
		ArrayList<String> fLetters = new ArrayList<>();
		for (Contacts entity : contacts) {
			if (TextUtils.isEmpty(entity.getFLetter()))
				continue;
			if (!fLetters.contains(entity.getFLetter()))
				fLetters.add(entity.getFLetter());
		}
		return fLetters;
	}

	/**
	 * 移除 -
	 *
	 * @param contacts
	 */
	private void convertList(List<Contacts> contacts) {
		for (Contacts entity : contacts) {
			if (entity.getPhone().contains("-"))
				entity.setPhone(entity.getPhone().replace("-", ""));
		}
	}

	/**
	 * a-z排序
	 */
	class ConstactComparator implements Comparator<Contacts> {
		@Override
		public int compare(Contacts lhs, Contacts rhs) {
			String a = lhs.getPinyin().substring(0, 1);
			String b = rhs.getPinyin().substring(0, 1);
			return a.compareTo(b);
		}
	}


}
