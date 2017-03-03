package com.time.memory.model.impl;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.MGroup;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:圈子操作
 * @date 2016/9/13 11:40
 * ==============================
 */
public interface IGroupController {
	List<GroupContacts> getList();

	void deleteById(String groupId);

	void getPeople(String url, String groupId, SimpleCallback callback);

	void reqDeleteUsers(String url, String groupId, List<String> userIdList, SimpleCallback callback);

	void reqExitCircle(String url, String groupId, SimpleCallback callback);

	void reqGroup(String url, String groupId, SimpleCallback callback);

	void reqAddGroup(String url, String groupId, String adminUserId, SimpleCallback callback);

	MGroup getGroupByKey(String key);

	Object getGroupByKey(String key, String userId);

	Object getGroupByUserId(String userId, String type);

	void upMGroup(MGroup mGroup);

}
