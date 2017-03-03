package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.MGroup;
import com.time.memory.model.CircleController;
import com.time.memory.model.MemoryInfoController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.model.impl.IMemoryInfoController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IForwardView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:转一转
 * @date 2016/11/4 16:35
 */
public class ForwardPresenter extends BasePresenter<IForwardView> {
	private static final String TAG = "ForwardPresenter";
	private ICircleController iCircleController;
	private IMemoryInfoController iMemoryInfoController;

	public ForwardPresenter() {
		iCircleController = new CircleController();
		iMemoryInfoController = new MemoryInfoController();
	}

	/**
	 * 转发记忆
	 */
	public void reqForward(boolean isOwen, int state, String memoryId, List<MGroup> groupList, int positon) {
		if (positon == -1) {
			if (mView != null) {
				mView.showShortToast("请选择一项");
				return;
			}
		}
		String url = null;
		String groupId = "";
		String groupName = "";
		final int type = groupList.get(positon).getType();

		if (isOwen) {
			// ->转发到群
			url = context.getString(R.string.FSFORWARDGROUP);
			groupId = groupList.get(positon).getGroupId();
			groupName = groupList.get(positon).getGroupName();
		} else {
			if (type != 0) {
				//->我的
				url = context.getString(R.string.FSFORWARD);
			} else {
				// ->群的
				url = context.getString(R.string.FSFORWARDGROUP);
				groupId = groupList.get(positon).getGroupId();
				groupName = groupList.get(positon).getGroupName();
			}
		}

		//TODO 我->我的;他的->他的;群的->群的判断
		if (mView != null)
			mView.showLoadingDialog();

		final boolean remove = TextUtils.isEmpty(groupId) && state == 2;
		final String finalGroupName = groupName;
		iMemoryInfoController.reqForwardMemory(url, memoryId, groupId, new SimpleCallback() {
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
						//转发成功
						if (mView != null) {
							mView.showShortToast("转发成功");
							mView.showSuccess();
							mView.finishActivity(type, remove, finalGroupName);
						}
					} else {
						//失败le
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
	 * 获取可转发的圈子
	 *
	 * @param url
	 * @param memoryIdSource
	 */
	public void reqGroups(String url, String memoryIdSource, final boolean isOwen, final int state) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		iCircleController.reqGroups(url, memoryIdSource, new SimpleCallback() {
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
					MGroup entity = (MGroup) data;
					if (entity.getStatus() == 0) {
						//成功
						if (mView != null) {
							addOwner(isOwen, state, entity.getGroupInfoVoList());
							mView.setAdapter(entity.getGroupInfoVoList());
							mView.showSuccess();
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
	 * 获取圈子
	 * curType 0：公开，1：私密 2：圈子
	 *
	 * @param userId
	 */
	public void getGroups(String userId, int state, String groupId, String curType) {
		//圈子
		List<MGroup> groupList = null;
		String type = "0";//我的
		if (state == 1) {
			if (curType.equals("0")) {
				//已经是公开的->不可以再是公开的
				type = "1";
				groupList = iCircleController.getGroupsByUId(userId, type);
			} else if (curType.equals("1")) {
				//现在是私密的->不可以是私密的
				type = "0";
				groupList = iCircleController.getGroupsByUId(userId, type);
			} else if (curType.equals("2")) {
				//现在在群里->晒出掉当前群
				groupList = iCircleController.getGroupsByUIdAndGId(userId, groupId);
			}
		} else if (state == 0) {
//			type = "0";//他的
//			groupList = iCircleController.getGroupsByUser(userId, type);
			type = "1";
			groupList = iCircleController.getGroupsByUId(userId, type);
		} else {
			//群的
			groupList = iCircleController.getGroupsByGroupId(userId, groupId);
		}
		for (MGroup entity : groupList) {
			entity.setIsCheck(false);
		}
		if (mView != null) {
			//联系人
			mView.setAdapter(groupList);
		}
	}

	/**
	 * 添加私密记忆
	 *
	 * @param isOwen
	 * @param mGroups
	 */
	private void addOwner(boolean isOwen, int state, List<MGroup> mGroups) {
		if (mGroups == null) mGroups = new ArrayList<>();
		//从我的记忆进入&&当前不是私密记忆
		if ((state == 1 && !isOwen) || state == 2) {
			MGroup mGroup = new MGroup();
			mGroup.setType(1);
			mGroup.setGroupName("私密记忆");
			mGroups.add(0, mGroup);
		}
	}
}
