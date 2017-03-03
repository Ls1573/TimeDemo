package com.time.memory.presenter;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.model.GroupController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.circle.GroupActivity;
import com.time.memory.view.activity.memory.FindGroupActivity;
import com.time.memory.view.impl.ISearchView;

/**
 * @author Qiu
 * @version V1.0
 * @Description:搜索数据处理
 * @date 2016/9/24 15:10
 */
public class SearchPresenter extends BasePresenter<ISearchView> {
	// m层
	private IGroupController iGroupController;

	public SearchPresenter() {
		iGroupController = new GroupController();
	}


	/**
	 * 启动页面
	 */
	public void startActivity(MGroup group) {
		MGroup curMGroup = iGroupController.getGroupByKey(group.getGroupId());
		if (curMGroup != null) {
			//存在圈子
			if (mView != null) {
				mView.startActivity(group, GroupActivity.class);
			}
		} else {
			//不存在
			if (mView != null) {
				mView.startActivity(group, FindGroupActivity.class);
			}
		}
	}


	/**
	 * 获取群信息
	 *
	 * @param
	 * @reurn
	 */
	public void reqGetGroup(String groupUrl, String groupId) {
		iGroupController.reqGroup(groupUrl, groupId, new SimpleCallback() {
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
					MGroup entity = (MGroup) data;
					if (entity.getStatus() == 0) {
						//成功
						mView.showSuccess();
						mView.setAdapter(entity.getGroupInfoVoList());
					} else {
						mView.showShortToast(entity.getMessage());
						mView.showFaild();
					}
				}
			}
		});

	}


}
