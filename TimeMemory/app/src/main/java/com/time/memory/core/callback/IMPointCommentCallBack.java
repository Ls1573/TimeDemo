package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.mt.nio.message.response.CA10RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:片段评论回调
 * @date 2016/10/28 18:00
 */
public interface IMPointCommentCallBack extends IMCallBack {
	void onPointComment(CA10RespVo ca10RespVo);

	void onRemoveComment(CX02RespVo cx02ReqVo);
}