package com.time.memory.presenter;

import android.content.Context;

import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.MsgRequest;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.model.MinaController;
import com.time.memory.model.impl.IMinaController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IBaseView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:Im发送
 * @date 2016/9/28 10:24
 */
public class ImPresenter extends BasePresenter<IBaseView> {
	private static final String TAG = "ImPresenter";
	// m层
	private IMinaController iMinaController;

	public ImPresenter() {
		iMinaController = new MinaController();
	}

	/**
	 * 发送消息
	 */
	public void sendMsg(Context context) {
		iMinaController.sendMsg(context, getRequest(), true, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				int code = (int) data;
				if (code == 0) {
					CLog.e(TAG, "数据已成功发出！");
				} else {
					CLog.e(TAG, "数据发送失败。错误码是：" + code + "！");
				}
			}
			@Override
			public void onNoNetCallback() {
				//无网络状态
			}
		});
	}
	private MsgRequest getRequest() {
		//构建
		MsgRequest msgRequest = new MsgRequest();
		msgRequest.setType(BusynessType.CM01.getIndex());
		return msgRequest;
	}


}
