package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * Created by Administrator on 2016/11/24.
 */
public interface OpenRecordController {
    void openRecord(String url, String channel, String appVersion, String deviceFlg, String deviceName, String deviceVersion, SimpleCallback callback);
}
