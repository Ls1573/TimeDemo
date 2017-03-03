package com.time.memory.presenter;

import com.time.memory.entity.MyPush;
import com.time.memory.model.PushController;
import com.time.memory.model.impl.IPushController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IPushView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:推送设置
 * @date 2016/11/1 9:17
 */
public class PushPresenter extends BasePresenter<IPushView> {
	private static final String TAG = "PushPresenter";
	// m层
	private IPushController iPushController;

	public PushPresenter() {
		iPushController = new PushController();
	}

	/**
	 * 更新推送设置
	 *
	 * @param myPush
	 */
	public void updatePush(MyPush myPush, final int position) {
		CLog.e(TAG, "updatePush------------------>");
		iPushController.updatePush(myPush);
		if (mView != null)
			mView.updatePush(position);
	}

	/**
	 * 获取推送设置
	 *
	 * @param key
	 */
	public void getPushing(String key) {
		MyPush myPush = iPushController.getPushByKey(key);
		if (mView != null)
			mView.setMessage(myPush);
	}

}
