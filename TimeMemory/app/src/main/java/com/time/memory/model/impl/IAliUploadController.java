package com.time.memory.model.impl;

import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/21 9:25
 */
public interface IAliUploadController {

	void upLoad(String filePath, String objectKey,OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback);
}
