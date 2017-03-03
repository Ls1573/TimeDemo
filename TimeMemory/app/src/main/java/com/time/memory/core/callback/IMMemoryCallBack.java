package com.time.memory.core.callback;


import com.time.memory.mt.nio.message.response.SA01ReqVo;
import com.time.memory.mt.nio.message.response.SA10ReqVo;
import com.time.memory.mt.nio.message.response.SA20RespVo;
import com.time.memory.mt.nio.message.response.SG01RespVo;
import com.time.memory.mt.nio.message.response.SG03RespVo;
import com.time.memory.mt.nio.message.response.SG04RespVo;
import com.time.memory.mt.nio.message.response.SW01RespVo;
import com.time.memory.mt.nio.message.response.SW02RespVo;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:首页记忆监听
 * @date 2016/11/2 14:18
 */
public interface IMMemoryCallBack extends IMCallBack {

	void onMemory(SW01RespVo reqVo);

	void onForward(SW02RespVo reqVo);

	void onGroup(SG01RespVo reqVo);

	void onRemoveGroup(SG03RespVo reqVo);

	void onUpGroup(SG04RespVo reqVo);

	void onUnRead(SA01ReqVo reqVo);

	void onUnReadComment(SA10ReqVo reqVo);

	void onAddmemory(SA20RespVo respVo);

}