package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.ReleaseMemory;
import com.time.memory.entity.SupporyMemory;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆发布
 * @date 2016/9/11 8:28
 */
public interface IMemoryController {

	void reqMemoryUpload(String url, ReleaseMemory releaseMemory, SimpleCallback simpleCallback);

	void reqMemoryUpload(String url, SupporyMemory releaseMemory, SimpleCallback simpleCallback);

	void reqMemoryUnRead(String url,SimpleCallback simpleCallback);
}
