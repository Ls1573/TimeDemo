package com.time.memory.core.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * @author Qiu
 * @version V1.0
 * @Description:屏幕监听
 * @date 2016/11/11 16:57
 */
public class ScreenManager {
	private Context mContext;
	private ScreenBroadcastReceiver mScreenReceiver;
	private ScreenStateListener mScreenStateListener;

	public ScreenManager(Context context) {
		mContext = context;
		mScreenReceiver = new ScreenBroadcastReceiver();
	}

	/**
	 * screen状态广播接收者
	 */
	private class ScreenBroadcastReceiver extends BroadcastReceiver {
		private String action = null;
		@Override
		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
				mScreenStateListener.onScreenOn();
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
				mScreenStateListener.onScreenOff();
			} else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
				mScreenStateListener.onUserPresent();
			}
		}
	}

	/**
	 * 开始监听screen状态
	 *
	 * @param listener
	 */
	public void begin(ScreenStateListener listener) {
		mScreenStateListener = listener;
		registerListener();
		getScreenState();
	}

	/**
	 * 获取screen状态
	 */
	private void getScreenState() {
		PowerManager manager = (PowerManager) mContext
				.getSystemService(Context.POWER_SERVICE);
		if (manager.isScreenOn()) {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOn();
			}
		} else {
			if (mScreenStateListener != null) {
				mScreenStateListener.onScreenOff();
			}
		}
	}

	/**
	 * 停止screen状态监听
	 */
	public void unregisterListener() {
		mContext.unregisterReceiver(mScreenReceiver);
	}

	/**
	 * 启动screen状态广播接收器
	 */
	private void registerListener() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		mContext.registerReceiver(mScreenReceiver, filter);
	}

	public interface ScreenStateListener {// 返回给调用者屏幕状态信息

		public void onScreenOn();

		public void onScreenOff();

		public void onUserPresent();
	}
}