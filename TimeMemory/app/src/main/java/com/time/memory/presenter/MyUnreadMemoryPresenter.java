package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.Reminds;
import com.time.memory.model.CircleUnreadMemoryController;
import com.time.memory.model.GroupController;
import com.time.memory.model.MyUnreadMemoryController;
import com.time.memory.model.impl.CircleUnreadController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.MyUnreadController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IUnreadMemory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MyUnreadMemoryPresenter extends BasePresenter<IUnreadMemory> {
	private static final String TAG = "MyUnreadMemoryPresenter";
	// m层
	private IGroupController iGroupController;
	private CircleUnreadController circleUnreadController;
	private MyUnreadController myUnreadController;


	public MyUnreadMemoryPresenter() {
		myUnreadController = new MyUnreadMemoryController();
		circleUnreadController = new CircleUnreadMemoryController();
		iGroupController = new GroupController();

	}

	/**
	 * 遍历删除
	 *
	 * @param index
	 * @param reminds
	 * @param mList
	 */
	public void removeList(int index, Reminds reminds, List<Reminds> mList, String msg, String groupId) {
		String memoryId = reminds.getMemoryId();
		mList.remove(index);
		//遍历删除
		Iterator<Reminds> stuIter = mList.iterator();
		while (stuIter.hasNext()) {
			Reminds info = stuIter.next();
			if (info.getMemoryId().equals(memoryId)) {
				stuIter.remove();
			}
		}
		int size = mList.size();
		if (msg.equals("CF04"))
			//他的新增
			upGroupNewInfo("2", "", MainApplication.getUserId(), size);
		else if (msg.equals("CF03"))
			//我的补充
			upGroupInfo("1", "", MainApplication.getUserId(), size);
		else if (msg.equals("CF03"))
			//他的补充
			upGroupInfo("2", "", MainApplication.getUserId(), size);
		else if (msg.equals("CF06"))
			//群的新增
			upGroupNewInfo("3", groupId, MainApplication.getUserId(), size);
		else if (msg.equals("CF07"))
			//群的补充
			upGroupInfo("3", groupId, MainApplication.getUserId(), size);

		if (mView != null) {
			if (size == 0)
				mView.closeActivity();
			else
				mView.refreshAdapter();
		}
	}

	/**
	 * 更新圈子数据(补充)
	 */

	public void upGroupInfo(String type, String groupId, String userId, int totalSize) {
		MGroup mGroup = null;
		if ("1".equals(type)) {
			//我的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		} else if ("2".equals(type)) {
			//他的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		} else {
			//群
			mGroup = (MGroup) iGroupController.getGroupByKey(groupId, MainApplication.getUserId());
		}
		if (totalSize != -1) {
			mGroup.setUnReadMPointAddCnt(String.valueOf(totalSize));
		} else {
			//计算总数据
			int count = 0;
			if (!TextUtils.isEmpty(mGroup.getUnReadMPointAddCnt())) {
				count = Integer.parseInt(mGroup.getUnReadMPointAddCnt());
			}
			//数据-1
			mGroup.setUnReadMPointAddCnt(String.valueOf((count > 0 ? --count : 0)));
		}

		//更新
		iGroupController.upMGroup(mGroup);
	}


	/**
	 * 更新圈子数据(新增)
	 */
	public void upGroupNewInfo(String type, String groupId, String userId, int totalCount) {
		MGroup mGroup = null;
		if ("2".equals(type)) {
			//他的
			mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "1");
		} else {
			//群
			mGroup = (MGroup) iGroupController.getGroupByKey(groupId, MainApplication.getUserId());
		}
		//当前数量
		if (totalCount != -1) {
			//设置总数
			mGroup.setUnReadMemoryCnt(String.valueOf(totalCount));
		} else {
			//计算总数据
			int count = 0;
			if (!TextUtils.isEmpty(mGroup.getUnReadMemoryCnt())) {
				count = Integer.parseInt(mGroup.getUnReadMemoryCnt());
			}
			//数据-1
			mGroup.setUnReadMemoryCnt(String.valueOf(count > 0 ? --count : 0));
		}
		//更新
		iGroupController.upMGroup(mGroup);
	}


	//他的新增&&我的补充
	public void getMyUnread(String url, final String type) {
		if (mView != null)
			mView.showLoadingDialog();
		myUnreadController.getMyUnreadMemory(url, type, new SimpleCallback() {
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
					Reminds unreadMemory = (Reminds) data;
					if (unreadMemory.getStatus() == 0) {
						if (mView != null) {
							if (unreadMemory.getReminds() != null && !unreadMemory.getReminds().isEmpty()) {
								if (type.equals("CF04"))
									//他的新增
									upGroupNewInfo("2", "", MainApplication.getUserId(), unreadMemory.getReminds().size());
								else if (type.equals("CF03"))
									//我的补充
									upGroupInfo("1", "", MainApplication.getUserId(), unreadMemory.getReminds().size());
								else
									//他的补充
									upGroupInfo("2", "", MainApplication.getUserId(), unreadMemory.getReminds().size());
								mView.setAdapter(converUnread(unreadMemory.getReminds()));
								mView.showSuccess();
							} else {
								//他的新增
								if (type.equals("CF04"))
									upGroupNewInfo("2", "", MainApplication.getUserId(), 0);
								else if (type.equals("CF03"))
									//我的补充
									upGroupInfo("1", "", MainApplication.getUserId(), 0);
								else
									//他的补充
									upGroupInfo("2", "", MainApplication.getUserId(), 0);
								mView.showEmpty();
							}
						}
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(unreadMemory.getMessage());
						}
					}
				}
			}
		});
	}


	//圈子的未读记忆列表
	public void getCircleUnread(String url, final String type, final String groupId) {
		if (mView != null)
			mView.showLoadingDialog();
		circleUnreadController.getMyUnreadMemory(url, type, groupId, new SimpleCallback() {
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
					Reminds unreadMemory = (Reminds) data;
					if (unreadMemory.getStatus() == 0) {
						if (mView != null) {
							if (unreadMemory.getReminds() != null && !unreadMemory.getReminds().isEmpty()) {
								if (type.equals("CF05"))
									//群的新增
									upGroupNewInfo("3", groupId, MainApplication.getUserId(), unreadMemory.getReminds().size());
								else if (type.equals("CF07"))
									//群的补充
									upGroupInfo("3", groupId, MainApplication.getUserId(), unreadMemory.getReminds().size());
								mView.setAdapter(converUnread(unreadMemory.getReminds()));
								mView.showSuccess();
							} else {
								if (type.equals("CF05"))
									//群的新增
									upGroupNewInfo("3", groupId, MainApplication.getUserId(), 0);
								else if (type.equals("CF07"))
									//群的补充
									upGroupInfo("3", groupId, MainApplication.getUserId(), 0);
								mView.showEmpty();
							}
						}

						//群的新增
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(unreadMemory.getMessage());
						}
					}
				}
			}
		});
	}

	public ArrayList<Reminds> converUnread(ArrayList<Reminds> reminds) {
		String imagePath = context.getString(R.string.FSIMAGEPATH);
		for (Reminds entity : reminds) {
			if (!TextUtils.isEmpty(entity.getUserHphoto())) {
				entity.setUserHphoto(imagePath + entity.getUserHphoto());
			}
		}
		return reminds;
	}

}
