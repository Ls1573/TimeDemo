package com.time.memory.model.impl;

import com.google.protobuf.ValueOrBuilder;
import com.time.memory.core.callback.SimpleCallback;

/**
 * Created by Administrator on 2016/11/1.
 */
public interface CircleUnreadController {
    //圈子的未读记忆列表
    void getMyUnreadMemory(String url,String type,String groupId, SimpleCallback simpleCallback);
    //圈子的  把未读的记忆设置成已读
    void getHasReadMemoryGroup(String url,String memoryID,String groupId,SimpleCallback callback);
    //圈子的  把未读的记忆片段设置成已读
    void getSupplementHasReadMemoryGroup(String url,String pointID,String groupId,SimpleCallback callback);
}
