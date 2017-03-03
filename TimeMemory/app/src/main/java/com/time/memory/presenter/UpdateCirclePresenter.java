package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.model.CircleController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IUpdateCircleView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:修改圈子
 * @date 2016/10/18 11:35
 */
public class UpdateCirclePresenter extends BasePresenter<IUpdateCircleView> {
	private static final String TAG = "MessagePresenter";

	private ICircleController circleController;

	public UpdateCirclePresenter() {
		circleController = new CircleController();
	}

	/**
	 * 修改圈子
	 */
	public void reqUpdateCircle(String url, final String groupName, final MGroup mGroup) {
		if (mView == null) return;
		if (TextUtils.isEmpty(groupName)) {
			mView.showShortToast("请输入圈子名称");
			return;
		}
		if (groupName.length() < 2) {
			mView.showShortToast("长度错误,请重新输入");
			return;
		}

		if (mView != null) {
			mView.showLoadingDialog();
		}
		circleController.reqUpCircle(url, groupName, mGroup.getGroupId(), new SimpleCallback() {
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
						//视图不为空
						mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					MGroup group = (MGroup) data;
					if (group.getStatus() == 0) {
						mGroup.setGroupName(groupName);
						//成功
						circleController.upCirlce(mGroup);
						if (mView != null) {
							mView.showSuccess();
						}
					} else {
						//失败了,不用更新本地的
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(group.getMessage());
						}
					}
				}
			}
		});
	}

}
