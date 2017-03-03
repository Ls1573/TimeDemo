package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:举报
 * @date 2016/10/24 13:17
 */
public interface IReportController {
	void reqReport(String url, String complainUserId, String memoryId, String memoryPointId, String commentId, String complainType, String complainDetailType, SimpleCallback callback);

}
