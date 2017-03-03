package com.time.memory.presenter;

import com.time.memory.entity.MGroup;
import com.time.memory.model.CircleController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IForwardView;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:发布记忆切换目标
 * @date 2016/11/22 15:59
 */
public class AddTargetPresenter extends BasePresenter<IForwardView> {
	private static final String TAG = "ForwardPresenter";
	private ICircleController iCircleController;

	public AddTargetPresenter() {
		iCircleController = new CircleController();
	}


	/**
	 * 获取圈子
	 * curType 0：公开，1：私密 2：圈子
	 *
	 * @param userId
	 */
	public void getGroups(String userId, int state, String groupId) {
		//圈子
		List<MGroup> groupList = null;
		String type = "0";//我的
		if (state == 1) {
			type = "0";
			groupList = iCircleController.getGroupsByUId(userId, type);
		} else if (state == 0) {
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
}
