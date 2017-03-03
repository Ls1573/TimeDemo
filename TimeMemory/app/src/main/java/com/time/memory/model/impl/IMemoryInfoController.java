package com.time.memory.model.impl;

import com.time.memory.mt.vo.MemoryDelVo;
import com.time.memory.core.callback.SimpleCallback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆详情
 * @date 2016/10/25 17:52
 */
public interface IMemoryInfoController {

	void reqMemoryInfos(String url,String memoryId, String memorySrcId, String type, SimpleCallback simpleCallback);

	void reqRemoveMemory(String url, String token, MemoryDelVo memoryDelVo, SimpleCallback simpleCallback);

	void reqForwardMemory(String url, String memoryId, String groupId, SimpleCallback simpleCallback);
}
