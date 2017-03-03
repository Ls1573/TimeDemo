package com.time.memory.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.time.memory.mt.vo.ContactsVo;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.MemoryDto;
import com.time.memory.entity.User;
import com.time.memory.model.CircleController;
import com.time.memory.model.ContactsController;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IImportView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:通讯录
 * @date 2016-9-9下午4:54:08
 * ==============================
 */
public class ImportPresenter extends BasePresenter<IImportView> {
	private static final String TAG = "CirclePresenter";
	// m层
	private IContactsController iContactsController;
	private ILoginController iLoginController;
	private ICircleController iCircleController;
	private ExecutorService executorManager;

	public ImportPresenter() {
		iContactsController = new ContactsController();
		iLoginController = new LoginController();
		iCircleController = new CircleController();
		executorManager = ExecutorManager.getInstance();
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


	/**
	 * 获取网络上的圈子数据
	 */
	public void reqCircleInfos(final String userId) {
		//请求圈子列表数据
		iCircleController.reqCircle(context.getString(R.string.FSMEMORY), userId, new SimpleCallback() {
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
					MemoryDto respVo = (MemoryDto) data;
					if (respVo.getStatus() == 0) {
						//成功
						//存在数据,进行下步操作
						List<MGroup> mGroups = addGroup(respVo, userId);
						//保存
						iCircleController.saveCircle(mGroups);
						//改状态
						iLoginController.updateUser(userId, "importgroup", "1");
						//查询未激活的圈子
						ArrayList<MGroup> unGroups = getActicted(mGroups);
						if (unGroups != null && !unGroups.isEmpty()) {
							//引导激活圈子
							if (mView != null) {
								mView.importCircle(unGroups, (int) respVo.getFriendCntF());
							}
						} else {
							if (mView != null) {
								mView.showSuccess();
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
	 * 上传联系人
	 */
	private void upConstacts(final String userId, ContactsVo contactsVo) {
		CLog.e(TAG, "+++++++++contactsVo:+++++++++" + contactsVo.getContactsVoList().size());
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
						}
						//更改状态
						iLoginController.updateUser(userId);
						//查询圈子权限问题
						reqCircleInfos(userId);
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
	 * 上传联系人(同步)
	 */
	public void upConstacts(final String userId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}

		executorManager.submit(new Runnable() {
			@Override
			public void run() {
				//遍历
				ArrayList<ContactsVo> contacts = iContactsController.getContacts(context);
				ContactsVo contactsVo = new ContactsVo();
				contactsVo.setUserToken(MainApplication.getUserToken());
				contactsVo.setContactsVoList(contacts);

				Bundle bundle = new Bundle();
				bundle.putParcelable("contactsVo", contactsVo);
				bundle.putString("userId", userId);
				Message msg = mHanlder.obtainMessage();
				msg.what = 1;
				msg.setData(bundle);
				mHanlder.sendMessage(msg);
			}
		});
	}

	private Handler mHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				ContactsVo contactsVo = msg.getData().getParcelable("contactsVo");
				String userId = msg.getData().getString("userId");
				// 上传联系人
				upConstacts(userId, contactsVo);
			}
		}
	};


	/**
	 * 看看有没有未激活的圈子
	 *
	 * @return
	 */
	public ArrayList<MGroup> getActicted(List<MGroup> groups) {
		//TODO 应该是1
		ArrayList<MGroup> unGroup = new ArrayList<>();
		for (MGroup group : groups) {
			if (group.getActiveFlg().equals("1") && group.getType() == 2) {
				//存在数据
				unGroup.add(group);
			}
		}
		return unGroup;
	}

	/**
	 * ContactsVo->UserVo
	 */
	public List<ContactsVo> convertUserVo(List<Contacts> contacts) {
		List<ContactsVo> contactsVos = new ArrayList<>();
		ContactsVo contactsVo;
		for (Contacts user : contacts) {
			contactsVo = new ContactsVo();
			contactsVo.setUserName(user.getContactName());
			contactsVo.setUserMobile(user.getPhone());
			contactsVos.add(contactsVo);
		}
		return contactsVos;
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
			contacts.setContactName(user.getUserName());
			contacts.setPhone(user.getUserMobile().trim());
			contacts.setPinyin(PinyinUtil.getPingYin(contacts.getContactName()));
			contacts.setfLetter(PinyinUtil.getFirstLetter(contacts.getPinyin()));
			contacts.setToUserId(fromUserId);
			contactsVos.add(contacts);
		}
		return contactsVos;
	}

	/**
	 * 数据转换
	 *
	 * @param entity
	 */
	private List<MGroup> addGroup(MemoryDto entity, String userId) {
		List<MGroup> mGroups = entity.getGroups();
		for (MGroup group : mGroups) {
			group.setType(2);
			group.setComeFrom(userId);
			//头像
			if (group.getHeadPhotos() != null) {
				if (group.getHeadPhotos().size() > 0)
					group.setHeadPhoto1(group.getHeadPhotos().get(0));
				if (group.getHeadPhotos().size() > 1)
					group.setHeadPhoto2(group.getHeadPhotos().get(1));
				if (group.getHeadPhotos().size() > 2)
					group.setHeadPhoto3(group.getHeadPhotos().get(2));
				if (group.getHeadPhotos().size() > 3)
					group.setHeadPhoto4(group.getHeadPhotos().get(3));
			}
		}
		//我的记忆
		MGroup myMGroup = new MGroup();
		myMGroup.setGroupName("我的记忆");
		myMGroup.setGroupId(userId);
		myMGroup.setTitle(entity.getTitleM());
		myMGroup.setUpdateDateForShow(entity.getUpdateDateMForShow());
		myMGroup.setActiveFlg("0");
		myMGroup.setMemoryCnt(entity.getMemoryCntM() + "");
		myMGroup.setUnReadCnt(entity.getUnReadCntM() + "");
		myMGroup.setGroupCount("0");
		myMGroup.setTotalPCnt(String.valueOf(entity.getTotalPCnt()));
		myMGroup.setType(0);
		myMGroup.setComeFrom(userId);
		myMGroup.setHeadPhoto1(entity.getHeadPhotoM());//头像

		//他的记忆
		MGroup otherMGroup = new MGroup();
		otherMGroup.setGroupName("TA的记忆");
		otherMGroup.setTitle(entity.getTitleF());
		otherMGroup.setGroupId(userId + "a");
		otherMGroup.setUpdateDateForShow(entity.getUpdateDateFForShow());
		otherMGroup.setMemoryCnt(entity.getMemoryCntF() + "");
		otherMGroup.setUnReadCnt(entity.getUnReadCntF() + "");
		otherMGroup.setGroupCount(entity.getFriendCntF() + "");
		otherMGroup.setType(1);
		otherMGroup.setActiveFlg("0");
		otherMGroup.setComeFrom(userId);
		//头像
		if (entity.getHeadPhotosF() != null) {
			if (entity.getHeadPhotosF().size() > 0)
				otherMGroup.setHeadPhoto1(entity.getHeadPhotosF().get(0));
			if (entity.getHeadPhotosF().size() > 1)
				otherMGroup.setHeadPhoto2(entity.getHeadPhotosF().get(1));
			if (entity.getHeadPhotosF().size() > 2)
				otherMGroup.setHeadPhoto3(entity.getHeadPhotosF().get(2));
			if (entity.getHeadPhotosF().size() > 3)
				otherMGroup.setHeadPhoto4(entity.getHeadPhotosF().get(3));
		}
		//创建新编辑部
		MGroup newAddMgroup = new MGroup();
		newAddMgroup.setGroupName("");
		newAddMgroup.setTitle("创建新编辑部");
		newAddMgroup.setGroupId(userId + "b");
		newAddMgroup.setType(3);
		newAddMgroup.setActiveFlg("0");
		newAddMgroup.setComeFrom(userId);

		mGroups.add(0, myMGroup);
		mGroups.add(1, otherMGroup);
		mGroups.add(newAddMgroup);
		CLog.e(TAG, "mGroups:" + mGroups.size());
		return mGroups;
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
}
