package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.GroupContacts;
import com.time.memory.model.GroupController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.pinyin.PinyinUtil;
import com.time.memory.view.impl.IDelCircleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:获取群成员
 * @date 2016/10/12 20:53
 */
public class DelCirclePresenter extends BasePresenter<IDelCircleView> {
	private static final String TAG = "ShowCirclePresenter";
	// m层
	private IGroupController iGroupController;

	public DelCirclePresenter() {
		iGroupController = new GroupController();
	}

	/**
	 * 获取集合中关键字数据
	 *
	 * @param keyWord
	 */
	public void getContactsByWord(String keyWord, ArrayList<GroupContacts> mList) {
		ArrayList<GroupContacts> cList = new ArrayList<>();
		for (GroupContacts entity : mList) {
			if (TextUtils.isEmpty(entity.getUserName()))
				cList.add(entity);
			else if (entity.getUserName().contains(keyWord)) {
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
	 * 删除圈子成员
	 *
	 * @param url
	 * @param groupId
	 * @param list
	 */
	public void deleteUsers(String url, String groupId, List<GroupContacts> list) {
		if (list == null || list.isEmpty()) {
			if (mView != null) {
				mView.showShortToast("取消操作");
			}
			return;
		}
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iGroupController.reqDeleteUsers(url, groupId, convertConstacts(list), new SimpleCallback() {
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
					//TODO 需要保存用户信息到本地db
					GroupContacts entity = (GroupContacts) data;
					if (entity.getStatus() == 0) {
						//保存数据
						//TODO 建立长链接
						mView.showSuccess();
					} else {
						mView.showShortToast(entity.getMessage());
						mView.showFaild();
					}
				}
				mView.removeUses();
			}
		});
	}

	/**
	 * 获取圈子成员列表(HTTP)
	 *
	 * @param groupId
	 */
	public void reqCirlcePerple(String url, String groupId) {
		if (mView != null) mView.showLoadingDialog();
		iGroupController.getPeople(url, groupId, new SimpleCallback() {
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
					GroupContacts entity = (GroupContacts) data;
					if (entity.getStatus() == 0) {
						//成功
						ArrayList<GroupContacts> list = entity.getUserVoList();
						if (list != null && !list.isEmpty()) {
							//存在数据,进行下步操作
							if (mView != null) {
								//排序
								convertUsers(list);
								Collections.sort(list, new ConstactComparator());
								mView.setAdapter(list);
								mView.showSuccess();
							}
						} else {
							if (mView != null) {
								mView.showSuccess();
							}
						}
					} else {
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
	 * 转换
	 */
	public void convertUsers(List<GroupContacts> list) {
		for (GroupContacts entity : list) {
			entity.setPinyin(PinyinUtil.getPingYin(entity.getUserName()));
			entity.setfLetter(PinyinUtil.getFirstLetter(entity.getPinyin()));
			entity.setState(0);
		}
	}

	/**
	 * 转换
	 *
	 * @param list
	 * @return
	 */
	public List<String> convertConstacts(List<GroupContacts> list) {
		List<String> userIds = new ArrayList<>();
		for (GroupContacts entity : list) {
			userIds.add(entity.getUserId());
		}
		return userIds;
	}

	/**
	 * a-z排序
	 */
	class ConstactComparator implements Comparator<GroupContacts> {
		@Override
		public int compare(GroupContacts lhs, GroupContacts rhs) {
			String a = lhs.getfLetter();
			String b = rhs.getfLetter();
			return a.compareTo(b);
		}
	}
}
