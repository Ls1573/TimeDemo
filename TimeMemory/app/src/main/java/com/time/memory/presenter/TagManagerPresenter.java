package com.time.memory.presenter;

import com.time.memory.entity.MGroup;
import com.time.memory.model.CircleController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.ITagManagerView;

import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:标签管理
 * @date 2016/12/1 9:27
 */
public class TagManagerPresenter extends BasePresenter<ITagManagerView> {

	private ICircleController iCircleController;

	// m层
	public TagManagerPresenter() {
		iCircleController = new CircleController();
	}


	/**
	 * 获取我可以管理的群(包括我自己)
	 */
	public void getMyAdminGroup(String userId) {
		if (mView != null) {
			mView.showSuccess();
		}
		List<MGroup> mGroupList = iCircleController.getGroupsByAdminId(userId, userId);
		MGroup mGroup = new MGroup();
		mGroup.setGroupName("我的记忆");
		mGroupList.add(0, mGroup);
		if (mView != null) {
			mView.showSuccess();
			mView.setAdapter(mGroupList);
		}
	}
}
