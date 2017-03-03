package com.time.memory.core.callback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:消息已读回调
 * @date 2016/10/10 12:16
 */
public interface IMMsgReadCallBack extends IMCallBack {

	//已读
	void onRead(int code);
}