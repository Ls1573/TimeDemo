package com.time.memory.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.time.memory.entity.AddressDao;
import com.time.memory.entity.AdvertDao;
import com.time.memory.entity.ContactsDao;
import com.time.memory.entity.DaoMaster;
import com.time.memory.entity.GroupContactsDao;
import com.time.memory.entity.MGroupDao;
import com.time.memory.entity.MessageDao;
import com.time.memory.entity.MyPushDao;
import com.time.memory.entity.PhotoInfoDao;
import com.time.memory.entity.TempMemoryDao;
import com.time.memory.entity.UserDao;
import com.time.memory.mt.vo.ContactsVoDao;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/12/5 8:45
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
	public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MigrationHelper.migrate(db,
				AdvertDao.class,
				PhotoInfoDao.class,
				UserDao.class,
				AddressDao.class,
				ContactsDao.class,
				GroupContactsDao.class,
				MessageDao.class,
				MGroupDao.class,
				MyPushDao.class,
				TempMemoryDao.class,
				ContactsVoDao.class
		);
	}
}
