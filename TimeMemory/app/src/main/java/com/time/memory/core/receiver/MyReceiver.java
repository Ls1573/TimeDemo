package com.time.memory.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.time.memory.dao.ConfigDao;
import com.time.memory.model.JPushController;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	private boolean isNotificatoin;
	private JPushController mController;

	public MyReceiver(Context Context) {
		// 静态注册使用。
		mController = JPushController.getInstance();
	}

	public MyReceiver() {
		// 静态注册使用。
		mController = JPushController.getInstance();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// 收到的消息
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			// 获得本地设置
//			getSettingByLocatoin();
			// 如果打开了本地通知
//			if (isNotificatoin) {
//				// 开启通知栏
			mController.onMessage(
					bundle.getString(JPushInterface.EXTRA_MESSAGE),
					bundle.getString(JPushInterface.EXTRA_EXTRA));
//			}
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			//打开自定义的Activity
			startActivtiy(context, bundle);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 获取推送设置
	private void getSettingByLocatoin() {
		ConfigDao mConfigDao = ConfigDao.getInstance();
		isNotificatoin = mConfigDao.getBoolean("isNotificatoin");
	}

	//通知
	private void startActivtiy(Context context, Bundle bundle) {
//		Intent i = new Intent(context, MessageDealActvity.class);
//		i.putExtras(bundle);
//		i.putExtra("isReceiver", true);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		context.startActivity(i);
	}
}
