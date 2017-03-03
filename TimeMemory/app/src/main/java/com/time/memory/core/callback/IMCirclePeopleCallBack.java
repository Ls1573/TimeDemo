package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.CG04RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子用户
 * @date 2016/10/8 17:50
 */
public interface IMCirclePeopleCallBack extends IMCallBack {
	void onCirclePeople(CG04RespVo respVo);
}