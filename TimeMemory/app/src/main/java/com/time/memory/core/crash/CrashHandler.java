package com.time.memory.core.crash;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.time.memory.dao.ConfigDao;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler instance;
	// 程序的Context对象
	private Context mContext;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (instance == null) {
			synchronized (CrashHandler.class) {
				if (instance == null) {
					instance = new CrashHandler();
				}
			}
		}
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(2000);
				// 可以跳转到首页
				// Intent intent = new Intent(mContext,
				// ContainerActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				// | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				// mContext.startActivity(intent);
				// 退出程序
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			} catch (Exception e) {
				Log.e(TAG, "error : ", e);
			}
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序出错了...", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 保存日志文件
		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 保存错误信息到文件中|上传
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		// 设备信息
		String devicdIndo = collectDeviceInfo(mContext);
		sb.append(devicdIndo + "\r\n");
		// 异常信息
		sb.append(ex + ", Cause By:" + ex).append("\r\n\r\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			sb.append(elements[i].toString() + "\r\n");
		}
		// 保存
		ConfigDao configDiao = ConfigDao.getInstance();
		return null;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public String collectDeviceInfo(Context ctx) {
		// 用来存储设备信息和异常信息
		ArrayMap<String, String> infos = null;
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				infos = new ArrayMap<String, String>();
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		StringBuffer sb = new StringBuffer();
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				sb.append(field.getName() + field.get(null).toString() + "\r\n");
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
		return sb.toString();
	}
}
