package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆片段
 * @date 2016/10/27 8:50
 */
public interface IMemoryPointController {

	void reqMemoryInfos(String url, String pointId, String type, SimpleCallback simpleCallback);
}
