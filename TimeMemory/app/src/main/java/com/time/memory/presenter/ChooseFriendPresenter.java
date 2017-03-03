package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.mt.vo.UserVo;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.User;
import com.time.memory.model.CircleController;
import com.time.memory.model.ContactsController;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DateUtil;
import com.time.memory.view.impl.IChooseView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:选择好友
 * @date 2016/10/12 11:43
 */
public class ChooseFriendPresenter extends BasePresenter<IChooseView> {
	private static final String TAG = "ChooseFriendPresenter";
	// m层
	private IContactsController iContactsController;
	private ICircleController iCircleController;
	private ILoginController iLoginController;

	public ChooseFriendPresenter() {
		iContactsController = new ContactsController();
		iCircleController = new CircleController();
		iLoginController = new LoginController();
	}


	/**
	 * 新建圈子
	 */
	public void reqCircle(String url, String token, final String userId, final List<Contacts> contactses, final String title) {
		mView.showLoadingDialog();
		iCircleController.reqAddCircle(url, title, token, convert(contactses), new SimpleCallback() {
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
						setMGroup(entity, title, userId, contactses);
						//保存
						iCircleController.saveCircle(entity);
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("创建成功");
							mView.closeActivity();
						}
					} else {
						//失败了
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
	 * 获取db中的联系人
	 */
	public void getContactsFromDb() {
		//新建
		List<Contacts> contacts = iContactsController.getContactsFromDb(MainApplication.getUserId());
		//字母
		if (contacts != null && !contacts.isEmpty()) {
			//排序
			Collections.sort(contacts, new ConstactComparator());
			//TODO
			for (Contacts contact : contacts) {
				contact.setIsCheck(false);
			}
		}
		// TODO
		if (mView != null) {
			mView.setAdapter(contacts);
		}
	}


	/**
	 * 创建圈子
	 *
	 * @param entity
	 */
	private void setMGroup(MGroup entity, String title, String userId, List<Contacts> contactses) {
		User user = iLoginController.getUser(userId);
		entity.setGroupName(title);
		entity.setUserId(userId);
		entity.setGroupCount(String.valueOf(contactses.size() + 1));
		entity.setAdminUserId(userId);
		entity.setMemoryCnt("0");
		entity.setFreeze("0");
		entity.setActiveFlg("0");
		entity.setType(2);
		entity.setHeadPhoto1(user.getHeadPhoto());
		entity.setUserName(user.getUserName());
		entity.setAdminUserName(user.getUserName());
		entity.setTitle("此圈子暂无记忆");
		entity.setUpdateDateForShow(DateUtil.getCurrentDateLine());
		entity.setComeFrom(userId);
		for (Contacts contacts : contactses) {
			String photo = contacts.getHeadPhoto();
			if (!TextUtils.isEmpty(photo)) {
				if (TextUtils.isEmpty(entity.getHeadPhoto1())) {
					entity.setHeadPhoto1(photo);
				} else if (TextUtils.isEmpty(entity.getHeadPhoto2())) {
					entity.setHeadPhoto2(photo);
				} else if (TextUtils.isEmpty(entity.getHeadPhoto3())) {
					entity.setHeadPhoto3(photo);
				} else if (TextUtils.isEmpty(entity.getHeadPhoto4())) {
					entity.setHeadPhoto4(photo);
					break;
				} else {
					break;
				}
			}
		}
	}

	/**
	 * 获取集合中关键字数据
	 *
	 * @param keyWord
	 * @param contacts
	 */
	public void getContactsByWord(String keyWord, List<Contacts> contacts) {
		ArrayList<Contacts> cList = new ArrayList<>();
		for (Contacts entity : contacts) {
			if (entity.getContactName().contains(keyWord)) {
				cList.add(entity);
			}
		}
		CLog.e(TAG, "cList:" + cList.size());
		if (mView != null) {
			mView.setFilterAdapter(cList);
		}
	}


	/**
	 * 数据转换
	 *
	 * @param contactses
	 * @return
	 */
	private List<UserVo> convert(List<Contacts> contactses) {
		List<UserVo> userVos = new ArrayList<>();
		UserVo user;
		for (Contacts contacts : contactses) {
			user = new UserVo();
			user.setUserId(contacts.getUserId());
			user.setActiveFlg(contacts.getActiveFlg());
			userVos.add(user);
		}
		return userVos;
	}

	/**
	 * 检测用户
	 *
	 * @param groups
	 * @param contacts
	 * @return
	 */
	private void checkUsers(ArrayList<GroupContacts> groups, List<Contacts> contacts) {

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
