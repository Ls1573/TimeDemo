package com.time.memory.util;

import android.text.TextUtils;

/**
 * 距离
 * 
 * @author Qiu
 * 
 */
public class MyDistanceUtil {
	// 计算距离
	public static String getDistance(String distance) {
		if (!TextUtils.isEmpty(distance)) {
			// 将距离转换为数字
			int dista = Integer.parseInt(distance);
			if (dista <= 999) {
				return distance + "m";
			} else {
				float cc = dista / 100; // 得到10.51..
				int d = Math.round(cc);// 四舍五入是11
				float e = d / (float) 10;// 把10 也强转为float型的，再让10除以它==
				return (e + "km");
			}
		}
		return "未知";
	}
}
