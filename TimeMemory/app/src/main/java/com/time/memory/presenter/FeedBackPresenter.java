package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.FeedbackController;
import com.time.memory.model.impl.IFeedbackController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.IBaseView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:意见反馈
 * @date 2016/10/14 16:21
 */
public class FeedBackPresenter extends BasePresenter<IBaseView> {
	IFeedbackController feedbackController;

	// m层
	public FeedBackPresenter() {
		feedbackController = new FeedbackController();
	}


	/**
	 * 意见反馈
	 *
	 * @param msg
	 */
	public void reqFeedback(String url, String msg) {
		if (TextUtils.isEmpty(msg)) {
			if (mView != null) {
				mView.showShortToast("请输入反馈内容");
				return;
			}
		}
		if (mView != null) {
			mView.showLoadingDialog();
		}
		feedbackController.reqFeedback(url, msg, new SimpleCallback() {
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
						mView.showShortToast(context.getString(R.string.net_error));
						mView.showFaild();
					}
				} else {
					BaseEntity msg = (BaseEntity) data;
					if (msg.getStatus() == 0) {
						//成功
						if (mView != null) {
							mView.showSuccess();
						}
					} else {
						//失败了
						if (mView != null) {
							mView.showFaild();
							mView.showShortToast(msg.getMessage());
						}
					}
				}
			}
		});
	}
}
