package com.time.memory.model;

import com.time.memory.MainApplication;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.TempMemoryDao;
import com.time.memory.model.impl.IMemoryTempController;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/2 11:40
 */
public class MemoryTempController extends BaseEntity implements IMemoryTempController {

	private TempMemoryDao tempMemoryDao;

	public MemoryTempController() {
		tempMemoryDao = MainApplication.getDaoSession().getTempMemoryDao();
	}

	/**
	 * 保存到临时表里
	 *
	 * @param tempMemory
	 */
	@Override
	public void saveMemory(TempMemory tempMemory) {
		tempMemoryDao.insertInTx(tempMemory);
	}

	/**
	 * 删除所有数据
	 */
	@Override
	public void deleteAll() {
		tempMemoryDao.deleteAll();
	}

	@Override
	public List<TempMemory> loadMemory(String userId, String type) {
		QueryBuilder qb = tempMemoryDao.queryBuilder();
		qb.where(TempMemoryDao.Properties.UserId.eq(userId), TempMemoryDao.Properties.Type.eq(type));
		return qb.list();
	}

	@Override
	public List<TempMemory> loadMemory(String userId, String type, String groupId) {
		QueryBuilder qb = tempMemoryDao.queryBuilder();
		qb.where(TempMemoryDao.Properties.UserId.eq(userId), TempMemoryDao.Properties.Type.eq(type), TempMemoryDao.Properties.GroupId.eq(groupId));
		return qb.list();
	}

	/**
	 * @param type
	 */
	@Override
	public void delByUserId(String type, String userId) {
	}


}
