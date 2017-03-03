package com.time.memory.presenter;

import com.time.memory.entity.MGroup;
import com.time.memory.model.CircleController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.ICircleEditView;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑部
 * @date 2016/11/5 9:07
 */
public class CircleEditPresenter extends BasePresenter<ICircleEditView> {
	private static final String TAG = "CircleEditPresenter";

	private ICircleController iCircleController;

	public CircleEditPresenter() {
		iCircleController = new CircleController();
	}


	/**
	 * 获取圈子
	 * @param userId
	 */
	public void getGroups(String userId) {
		//圈子
		List<MGroup> groupList = iCircleController.getGroupsByUser(userId);
		//联系人
		if (mView != null)
			mView.setAdapter(groupList);
	}
}
