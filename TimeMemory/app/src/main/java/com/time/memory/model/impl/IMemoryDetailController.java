package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆
 * @date 2016/9/14 11:14
 */
public interface IMemoryDetailController {

	//我的记忆
	void getMemoryAll(String url, String searchType, String labelId, String groupId, int pageIndex, int pageSize, SimpleCallback callback);

	//他的记忆
	void getOtherMemoryAll(String url, String searchType, String labelId, String groupId, int pageIndex, int pageSize, SimpleCallback callback);

	//圈子的
	void getGroupMemoryAll(String url, String searchType, String labelId, String groupId, int pageIndex, int pageSize, SimpleCallback callback);


}
