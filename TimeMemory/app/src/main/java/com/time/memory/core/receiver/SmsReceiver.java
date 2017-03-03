package com.time.memory.core.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.time.memory.util.CLog;

/**
 * @author Qiu
 * @version V1.3
 * @Description:短信状态监听
 * @date 2017/1/3 11:28
 */
public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try {
			switch (getResultCode()) {
				case Activity.RESULT_OK:
					CLog.d("lmn", "----发送短信成功---------------------------");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
				case SmsManager.RESULT_ERROR_NULL_PDU:
				default:
					CLog.d("lmn", "----发送短信失败---------------------------");
					break;
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}