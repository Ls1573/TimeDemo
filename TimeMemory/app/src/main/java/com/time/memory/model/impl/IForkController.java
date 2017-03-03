package com.time.memory.model.impl;


import com.time.memory.core.callback.SimpleCallback;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:点赞
 * @date 2016/11/4 9:38
 */
public interface IForkController {
	void reqFork(String url, String token, String memoryUserId, String praiseUserId, String memoryId, String memoryPointId, String groupId, String delFlg, String memoryIdSource, String memoryPointIdSource, SimpleCallback callback);
}
