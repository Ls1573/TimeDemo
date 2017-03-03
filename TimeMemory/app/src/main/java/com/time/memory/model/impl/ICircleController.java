package com.time.memory.model.impl;


import com.time.memory.mt.vo.UserVo;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.GroupAdDto;

import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子
 * @date 2016/10/12 13:40
 */
public interface ICircleController {

	void delCirlceById(String groupId);

	void removeAll();

	void upCirlce(MGroup MGroup);

	void upCirlces(List<MGroup> mGroups);

	void upCirlceById(String groupId, String groupName, String userId);

	void upCirlceByKey(String key, String userId, String msg, String data);

	void reqGroups(String url,String memoryIdSource, SimpleCallback callback);

	MGroup getGroup(String Id);

	List<MGroup> getGroups(String userId);

	List<MGroup> getGroupsByUser(String userId);

	List<MGroup> getGroupsByUser(String userId, String type);

	List<MGroup> getGroupsByUId(String userId, String type);

	List<MGroup> getGroupsByUIdAndGId(String userId, String groupId);

	List<MGroup> getGroupsByGroupId(String userId, String groupId);

	List<MGroup> getGroupsByGroup(String userId, String groudId);

	List<MGroup> getGroupsByAdminId(String userId, String adminId);

	void saveCircle(MGroup entity);

	void saveCircle(List<MGroup> list);

	void reqAddCircle(String url, String groupName, String userToken, List<UserVo> users, SimpleCallback callback);

	void reqUpCircle(String url, String groupName, String groupId, SimpleCallback callback);

	void reqCircle(String url, String userToken, SimpleCallback callback);

	void reqCircle(String url, GroupAdDto dto, SimpleCallback callback);

	void reqAddMenInCirlce(String url, String userToken, String groupId, List<UserVo> users, SimpleCallback callback);

}
