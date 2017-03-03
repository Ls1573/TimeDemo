package com.time.memory.util;

import android.util.Log;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0 ==============================
 * @Description: 可以全局控制是否打印log日志
 * @date 2016-8-23下午3:27:17
 */
public class LogUtil {

	private static boolean isPrintLog = true;

	private static int LOG_MAXLENGTH = 3000;

	public static void LogShitou(String msg) {
		if (isPrintLog) {
			int strLength = msg.length();
			int start = 0;
			int end = LOG_MAXLENGTH;
			for (int i = 0; i < 100; i++) {
				if (strLength > end) {
					Log.e("log_" + i, msg.substring(start, end));
					start = end;
					end = end + LOG_MAXLENGTH;
				} else {
					Log.e("log_" + i, msg.substring(start, strLength));
					break;
				}
			}
		}
	}

	public static void LogShitou(String type, String msg) {
		if (isPrintLog) {
			int strLength = msg.length();
			int start = 0;
			int end = LOG_MAXLENGTH;
			for (int i = 0; i < 100; i++) {
				if (strLength > end) {
					Log.d(type + "_" + i, msg.substring(start, end));
					start = end;
					end = end + LOG_MAXLENGTH;
				} else {
					Log.d(type + "_" + i, msg.substring(start, strLength));
					break;
				}
			}
		}
	}

}