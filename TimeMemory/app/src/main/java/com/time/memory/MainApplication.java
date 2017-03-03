package com.time.memory;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.time.memory.dao.DBManager;
import com.time.memory.dao.MigrationHelper;
import com.time.memory.dao.MySQLiteOpenHelper;
import com.time.memory.entity.DaoMaster;
import com.time.memory.entity.DaoSession;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.greendao.database.Database;

import cn.jpush.android.api.JPushInterface;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description: MainApplication
 * @date 2016-8-1上午11:34:53
 * ==============================
 */
public class MainApplication extends Application {

	private static Context sCtx;
	private static DaoSession daoSession;//数据库session
	private static Database db;//数据库
	private static String userId;//用户Id
	private static String userToken;//用户token
	private String dbName = "memory.db";

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sCtx = getApplicationContext();
		//数据库
		upGradeDb();
		UMShareAPI.get(this);
		JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}

	private void upGradeDb() {
		//数据库管理(创建|复制)
		DBManager.getInstance(sCtx).copyDBFile(dbName);
		// 版本升级 查看日志信息
		MigrationHelper.DEBUG = true;
		MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, dbName, null);
		DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
		db = helper.getWritableDb();
		daoSession = daoMaster.newSession();
	}

	{
		//微信(正式)
		PlatformConfig.setWeixin("wx9ba10681d1167dc1", "41aae859fb8f3dc6da0ff09a0ba4297");
		//微博  （正式）
		PlatformConfig.setSinaWeibo("3394562507", "64ac6ab9e3b562b3a57c160a0c87bc40");
		//QQ (正式)
		PlatformConfig.setQQZone("1105821382", "K1epv8GZNjCeHD8s");
		Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
	}

	public static Context getContext() {
		return sCtx;
	}

	public static DaoSession getDaoSession() {
		return daoSession;
	}

	public static Database getDb() {
		return db;
	}

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		MainApplication.userId = userId;
	}

	public static void setUserToken(String userToken) {
		MainApplication.userToken = userToken;
	}

	public static String getUserToken() {
		return userToken;
	}


}