package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.CG01RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子
 * @date 2016/10/8 17:50
 */
public interface IMCreateCallBack extends IMCallBack {
	void onCreateCall(CG01RespVo respVo);
}