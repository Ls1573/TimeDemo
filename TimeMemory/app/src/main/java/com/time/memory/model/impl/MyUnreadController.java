package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.model.base.BaseController;

/**
 * Created by Administrator on 2016/10/31.
 */
public interface MyUnreadController {
    //我的 和 他的 未读消息列表
    void getMyUnreadMemory(String url,String type, SimpleCallback simpleCallback);
    //我的 未读的记忆片段设置成已读
    void getHasReadMemory(String url,String type,String pointId,SimpleCallback callback);
    //他的 把未读的记忆片段设置成已读
    void getSupplementHasReadMemory(String url,String pointID,SimpleCallback callback);
//    他的 吧未读的记忆设置成已读
    void getOReadMemory(String url,String type,String memoryID,SimpleCallback callback);
}
