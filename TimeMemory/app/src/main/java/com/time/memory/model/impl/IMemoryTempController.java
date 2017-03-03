package com.time.memory.model.impl;

import com.time.memory.entity.TempMemory;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:临时记忆
 * @date 2016/9/11 8:28
 */
public interface IMemoryTempController {

	void saveMemory(TempMemory tempMemory);

	void delByUserId(String type, String userId);

	List<TempMemory> loadMemory(String userId, String type);

	List<TempMemory> loadMemory(String userId, String type, String groupId);

	void deleteAll();
}
