package com.time.memory.presenter;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.GroupAdDto;
import com.time.memory.entity.MGroup;
import com.time.memory.model.CircleController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IImportCircleView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:导入圈子()
 * @date 2016/10/20 10:25
 */
public class ImportSPresenter extends BasePresenter<IImportCircleView> {
	private static final String TAG = "CirclePresenter";
	// m层
	private ICircleController iCircleController;

	public ImportSPresenter() {
		iCircleController = new CircleController();
	}

	/**
	 * 激活圈子
	 *
	 * @param url
	 */
	public void reqStartCircle(String url, final String userToken, final String userId, final List<MGroup> groupIdList) {
		if (groupIdList == null || groupIdList.isEmpty()) {
			if (mView != null) {
				mView.showShortToast("请选择圈子");
			}
			return;
		}
		GroupAdDto dto = new GroupAdDto();
		dto.setUserToken(userToken);
		dto.setGroupIdList(getUserIds(groupIdList, userId));
		mView.showLoadingDialog();

		//请求圈子列表数据
		iCircleController.reqCircle(url, dto, new SimpleCallback() {
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
					MGroup entity = (MGroup) data;
					if (entity.getStatus() == 0) {
						//成功
						//存在数据,进行下步操作
						iCircleController.upCirlces(groupIdList);
						if (mView != null) {
							//激活成功
							mView.startActivity();
							mView.showShortToast("激活成功");
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
	 * 获取Id
	 *
	 * @param groupIdList
	 * @return
	 */
	private List<String> getUserIds(List<MGroup> groupIdList, String userId) {
		List<String> ids = new ArrayList<>();
		for (MGroup entity : groupIdList) {
			ids.add(entity.getGroupId());
			entity.setComeFrom(userId);
		}
		return ids;
	}


}
