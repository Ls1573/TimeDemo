package com.time.memory.model.impl;

import android.content.Context;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/12/14 9:03
 */
public interface IImageDownloaderController {
	void downloadImage(Context context, String url, String filePath, SimpleCallback callback);
}
