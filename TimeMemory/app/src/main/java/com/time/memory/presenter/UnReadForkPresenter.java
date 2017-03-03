package com.time.memory.presenter;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.UnForkDto;
import com.time.memory.model.GroupController;
import com.time.memory.model.UnReadForkController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.model.impl.IUnReadForkController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IUnReadForkView;

import java.util.Iterator;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:绑定邮箱
 * @date 2016/11/24 11:37
 */
public class UnReadForkPresenter extends BasePresenter<IUnReadForkView> {

	private IUnReadForkController iUnReadForkController;
	private IGroupController iGroupController;

	public UnReadForkPresenter() {
		iUnReadForkController = new UnReadForkController();
		iGroupController = new GroupController();
	}

	/**
	 * 删除
	 */
	public void removeList() {
		int totalSize = 0;
		MGroup mGroup = null;
		mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		if (mGroup != null) {
			mGroup.setTotalPCnt(String.valueOf(totalSize));
			//更新
			iGroupController.upMGroup(mGroup);
		}
	}


	/**
	 * 遍历删除
	 *
	 * @param index
	 * @param reminds
	 * @param mList
	 */
	public void removeList(int index, UnForkDto reminds, List<UnForkDto> mList) {
		String memoryId = reminds.getMemoryId();
		String type = reminds.getMfgflag();
		String groupId = reminds.getGroupId();
		mList.remove(index);
		//遍历删除
		Iterator<UnForkDto> stuIter = mList.iterator();
		while (stuIter.hasNext()) {
			UnForkDto info = stuIter.next();
			if (info.getMemoryId().equals(memoryId)) {
				stuIter.remove();
			}
		}
		int totalSize = mList.size();

		MGroup mGroup = (MGroup) iGroupController.getGroupByUserId(MainApplication.getUserId(), "0");
		//
		mGroup.setTotalPCnt(String.valueOf(totalSize));
		//更新
		iGroupController.upMGroup(mGroup);

		if (mView != null) {
			if (totalSize == 0)
				mView.closeActivity();
			else
				mView.refreshAdapter();
		}
	}

	/**
	 * 获取数据
	 */
	public void reqUnReadForks(String url) {
		if (mView != null)
			mView.showLoadingDialog();
		iUnReadForkController.reqInfos(url, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showEmpty();
				}
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						mView.showEmpty();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					UnForkDto entity = (UnForkDto) data;
					if (entity.getStatus() == 0) {
						//移除
						removeList();
						if (entity.getUnReads() == null || entity.getUnReads().isEmpty()) {
							if (mView != null) {
								mView.showEmpty();
							}
						} else {
							if (mView != null) {
								mView.setAdapter(entity.getUnReads());
								mView.showSuccess();
							}
						}
					} else {
						if (mView != null) {
							mView.showEmpty();
							mView.showShortToast(entity.getMessage());
						}
					}
				}
			}
		});
	}
}
