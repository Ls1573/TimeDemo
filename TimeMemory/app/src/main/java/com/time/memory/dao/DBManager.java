package com.time.memory.dao;

import android.content.Context;
import android.os.Environment;

import com.time.memory.util.CLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 数据库管理
 */
public class DBManager {
	private static final String TAG = "DBManager";
	private static final int BUFFER_SIZE = 1024;
	private String DB_PATH;
	private Context mContext;

	private static DBManager sInstance;

	// 创建
	public static DBManager getInstance(Context context) {
		if (sInstance == null) {
			synchronized (DBManager.class) {
				if (sInstance == null) {
					sInstance = new DBManager(context);
				}
			}
		}
		return sInstance;
	}

	public DBManager(Context context) {
		this.mContext = context;
		DB_PATH = File.separator + "data"
				+ Environment.getDataDirectory().getAbsolutePath() + File.separator
				+ context.getPackageName() + File.separator + "databases" + File.separator;
		CLog.e(TAG, DB_PATH);
	}

	/**
	 * 将db文件拷贝到本地
	 */
	public void copyDBFile(String dbName) {
		File dir = new File(DB_PATH);
		if (dir.exists()) {
			CLog.e(TAG, "已存在的db---------->");
			return;
		}
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File dbFile = new File(DB_PATH + dbName);
		if (!dbFile.exists()) {
			InputStream is;
			OutputStream os;
			try {
				is = mContext.getResources().getAssets().open(dbName);
				os = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int length;
				while ((length = is.read(buffer, 0, buffer.length)) > 0) {
					os.write(buffer, 0, length);
				}
				os.flush();
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
