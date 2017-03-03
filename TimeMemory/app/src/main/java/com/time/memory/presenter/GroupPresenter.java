package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.model.GroupController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IFindGroupView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:群操作
 * @date 2016/10/15 15:44
 */

public class GroupPresenter extends BasePresenter<IFindGroupView> {
	// m层

	IGroupController groupController;

	public GroupPresenter() {
		groupController = new GroupController();
	}

	/**
	 * 筛选数据
	 *
	 * @param mGroup
	 */
	public void convertGroup(MGroup mGroup) {
		//图片集
		List<String> urlList = new ArrayList<>();
		//所有图片url
		List<String> list = mGroup.getHeadPhotos();
		if (list != null) {
			for (String ss : list) {
				//不为空加入
				if (!TextUtils.isEmpty(ss))
					urlList.add(ss);
			}
		}
		if (urlList.size() != 4) {
			int size = 4 - urlList.size();
			for (int i = 0; i < size; i++) {
				urlList.add("");
			}
		}
		if (mView != null) {
			mView.setImagesData(urlList);
		}
	}


	/**
	 * 申请加入圈子
	 *
	 * @param url
	 * @param groupId
	 */
	public void reqAddCircle(String url, final String groupId, final String adminUserId) {
		if (mView != null) {
			mView.showLoadingDialog();
		}
		groupController.reqAddGroup(url, groupId, adminUserId, new SimpleCallback() {
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
					MGroup MGroup = (MGroup) data;
					if (MGroup.getStatus() == 0) {
						//申请成功
						if (mView != null) {
							mView.showSuccess();
							mView.showShortToast("申请成功");
						}
					} else {
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(MGroup.getMessage());
						}
					}
				}
			}
		});
	}

}
