package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface DataStatisticsController {
    //数据统计
    void getDataStatistics(String url, SimpleCallback callback);
}
