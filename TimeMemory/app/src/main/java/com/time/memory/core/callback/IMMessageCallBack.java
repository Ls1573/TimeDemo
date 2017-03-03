package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.SM01RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:消息回调
 * @date 2016/10/8 17:50
 */
public interface IMMessageCallBack extends IMCallBack {
	//进群申请
	void onGroup(SM01RespVo msg);
}