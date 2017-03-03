package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.SG02RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子
 * @date 2016/10/8 17:50
 */
public interface IMCircleCallBack extends IMCallBack {
	//获取
	void onContacts(SG02RespVo respVo);
}