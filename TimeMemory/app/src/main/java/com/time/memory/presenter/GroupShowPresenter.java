package com.time.memory.presenter;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.GroupContacts;
import com.time.memory.model.GroupController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IGroupShowView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑部
 * @date 2016/11/5 9:07
 */
public class GroupShowPresenter extends BasePresenter<IGroupShowView> {
	private static final String TAG = "CircleEditPresenter";

	private IGroupController iGroupController;

	public GroupShowPresenter() {
		iGroupController = new GroupController();
	}

	/**
	 * 退出圈子
	 *
	 * @param url
	 * @param groupId
	 */
	public void reqExitCircle(String url, final String groupId, String adminId, List<GroupContacts> mList) {
		if (adminId.equals(MainApplication.getUserId())) {
			//管理员Id==用户Id
			if (mList.size() > 3) {
				//圈子里有人,不可以退
				mView.showShortToast("圈子里还有其他用户,暂不可以退出圈子");
				return;
			}
		}
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iGroupController.reqExitCircle(url, groupId, new SimpleCallback() {
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
					//TODO
					GroupContacts entity = (GroupContacts) data;
					if (entity.getStatus() == 0) {
						//保存数据
						iGroupController.deleteById(groupId);
						mView.showSuccess();
						mView.exitCircle();
					} else {
						mView.showShortToast(entity.getMessage());
						mView.showFaild();
					}
				}
			}
		});
	}

	/**
	 * 获取圈子成员列表(HTTP)
	 *
	 * @param groupId
	 */
	public void reqCirlcePerple(String url, String groupId, final String adminUserId) {
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
						ArrayList<GroupContacts> list = new ArrayList<>();
						list.addAll(entity.getUserVoList());
						ArrayList<GroupContacts> oList = new ArrayList<>();
						oList.addAll(entity.getUserVoList());

						if (list != null && !list.isEmpty()) {
							//存在数据,进行下步操作
							if (mView != null) {
								mView.setAdapter(addOpr(list, adminUserId), oList, entity.getMemoryCount(), entity.getAddMemoryPointCount());
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
	 * 添加格外操作
	 *
	 * @param list
	 * @param adminUserId
	 */
	private List<GroupContacts> addOpr(List<GroupContacts> list, String adminUserId) {
		List<GroupContacts> nList;
		//当前用户是管理员
		if (adminUserId.equals(MainApplication.getUserId())) {
			if (list.size() > 13)
				nList = list.subList(0, 13);
			else
				nList = list;
			nList.add(new GroupContacts(1));//添加
			nList.add(new GroupContacts(2));//删除
		} else {
			//不是管理员
			if (list.size() > 14)
				nList = list.subList(0, 14);
			else
				nList = list;
			nList.add(new GroupContacts(1));
		}
		return nList;
	}
}
