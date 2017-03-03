package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:添加标签
 * @date 2016/9/26 10:35
 */
public interface IAddTagController {

	void getTags(String url, SimpleCallback callback);

	void getGroupTags(String url, String groupId, SimpleCallback callback);

	void removeTag(String url, String lableId, SimpleCallback callback);

	void reqAddTag(String url, String title, String groupId, SimpleCallback callback);

	void reqAddUserTag(String url, String title, SimpleCallback callback);


}
