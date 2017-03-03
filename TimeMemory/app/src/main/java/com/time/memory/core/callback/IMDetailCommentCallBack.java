package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.CA10RespVo;
import com.time.memory.mt.nio.message.response.CX01RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:片段评论回调
 * @date 2016/10/28 18:00
 */
public interface IMDetailCommentCallBack extends IMCallBack {
	void onDetailComment(CA10RespVo ca10RespVo);

	void removeDetail(CX01RespVo cx01RespVo);
}