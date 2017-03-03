package com.time.memory.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/11/23 17:49
 */
public class MacAddressUtils {
	/**
	 * 获取手机的Mac地址，在Wifi未开启或者未连接的情况下也能获取手机Mac地址
	 */
	public static String getMacAddress(Context context) {
		String macAddress = null;
		WifiInfo wifiInfo = getWifiInfo(context);
		if (wifiInfo != null) {
			macAddress = wifiInfo.getMacAddress();
		}
		return macAddress;
	}

	/**
	 * 获取手机的Ip地址
	 */
	public static String getIpAddress(Context context) {
		String IpAddress = null;
		WifiInfo wifiInfo = getWifiInfo(context);
		if (wifiInfo != null) {
			IpAddress = intToIpAddress(wifiInfo.getIpAddress());
		}
		return IpAddress;
	}

	/**
	 * 获取WifiInfo
	 */
	public static WifiInfo getWifiInfo(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = null;
		if (null != wifiManager) {
			info = wifiManager.getConnectionInfo();
		}
		return info;
	}


	public static long ipAddressToint(String ip) {
		String[] items = ip.split("\\.");
		return Long.valueOf(items[0]) << 24
				| Long.valueOf(items[1]) << 16
				| Long.valueOf(items[2]) << 8
				| Long.valueOf(items[3]);
	}

	public static String intToIpAddress(long ipInt) {
		StringBuffer sb = new StringBuffer();
		sb.append(ipInt & 0xFF).append(".");
		sb.append((ipInt >> 8) & 0xFF).append(".");
		sb.append((ipInt >> 16) & 0xFF).append(".");
		sb.append((ipInt >> 24) & 0xFF);
		return sb.toString();
	}
}
