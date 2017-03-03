package com.time.memory.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.time.memory.MainApplication;

/**
 * Dao层
 * 
 * @author Qiu
 */
public class ConfigDao {

	private static final String SETTING_INFO = "memroy";// 文件名
	private final SharedPreferences mSharePref;
	private final Editor mEditor;
	private static ConfigDao sInstance;

	private ConfigDao(Context context) {
		mSharePref = context.getSharedPreferences(SETTING_INFO,
				Context.MODE_PRIVATE);
		mEditor = mSharePref.edit();
	}

	// 创建
	public static ConfigDao getInstance() {
		if (sInstance == null) {
			synchronized (ConfigDao.class) {
				if (sInstance == null) {
					sInstance = new ConfigDao(MainApplication.getContext());
				}
			}
		}
		return sInstance;
	}

	public void removeAll() {
		mEditor.clear().commit();
	}

	public void remove(String key) {
		if (mSharePref.contains(key)) {
			mEditor.remove(key).commit();
		}
	}

	// boolean
	public void setBoolean(String key, boolean b) {
		mEditor.putBoolean(key, b).commit();
	}

	public boolean getBoolean(String key) {
		return mSharePref.getBoolean(key, true);
	}

	// String
	public void setString(String key, String value) {
		mEditor.putString(key, value).commit();
	}

	public String getString(String key) {
		return mSharePref.getString(key, "");
	}

	// int
	public void setInteger(String key, int value) {
		mEditor.putInt(key, value).commit();
	}

	public int getInt(String key) {
		return mSharePref.getInt(key, -1);
	}

	// long
	public void setLong(String key, long value) {
		mEditor.putLong(key, value).commit();
	}

	public long getLong(String key) {
		return mSharePref.getLong(key, -1L);
	}
}
