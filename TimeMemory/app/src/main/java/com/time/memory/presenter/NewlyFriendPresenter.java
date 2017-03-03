package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.mt.vo.UserVo;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.GroupContacts;
import com.time.memory.model.CircleController;
import com.time.memory.model.ContactsController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
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
public class NewlyFriendPresenter extends BasePresenter<IChooseView> {
	private static final String TAG = "NewlyFriendPresenter";
	// m层
	private IContactsController iContactsController;
	private ICircleController iCircleController;

	public NewlyFriendPresenter() {
		iContactsController = new ContactsController();
		iCircleController = new CircleController();
	}

	/**
	 * 获取集合中关键字数据
	 *
	 * @param keyWord
	 */
	public void getContactsByWord(String keyWord, ArrayList<Contacts> mList) {
		ArrayList<Contacts> cList = new ArrayList<>();
		for (Contacts entity : mList) {
			if (TextUtils.isEmpty(entity.getContactName()))
				cList.add(entity);
			else if (entity.getContactName().trim().contains(keyWord.trim())) {
				cList.add(entity);
			}
		}
		CLog.e(TAG, "cList:" + cList.size());
		Collections.sort(cList, new ConstactComparator());
		if (mView != null) {
			mView.setFilterAdapter(cList);
		}
	}

	/**
	 * 获取db中的联系人
	 */
	public void getContactsFromDb(ArrayList<GroupContacts> mList, String userId) {
		//插入临时数据
		iContactsController.insertGroupTemp(mList);

		List<Contacts> contacts = iContactsController.reqContacts(userId);
		//字母
		if (contacts != null && !contacts.isEmpty()) {
			//排序
			Collections.sort(contacts, new ConstactComparator());
			//TODO 有bug
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
	 * 添加好友进圈子
	 *
	 * @param url
	 * @param groudId
	 * @param userVoList
	 */
	public void reqAddCirclepeoloe(String url, String userToken, String groudId, List<Contacts> userVoList) {
		if (userVoList == null || userVoList.isEmpty()) {
			if (mView != null) {
				mView.showShortToast("请选择用户");
				return;
			}
		}
		mView.showLoadingDialog();
		iCircleController.reqAddMenInCirlce(url, userToken, groudId, convert(userVoList), new SimpleCallback() {
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
					BaseEntity entity = (BaseEntity) data;
					if (entity.getStatus() == 0) {
						//保存
//							iCircleController.saveCircle(title, userId, entity.getGroupId(), entity.getInsDate());
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("加入成功");
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
	 * a-z排序
	 */
	class ConstactComparator implements Comparator<Contacts> {
		@Override
		public int compare(Contacts lhs, Contacts rhs) {
			String a = lhs.getfLetter();
			String b = rhs.getfLetter();
			return a.compareTo(b);
		}
	}
}
