/**
 *
 */
package com.time.memory.util;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.time.memory.entity.Location;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:定位辅助工具类
 * @date 2016/11/12 14:50
 */
public class LocUtils {
	/**
	 * 开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP = 2;


	/**
	 * 根据定位结果返回定位信息的字符串
	 *
	 * @return
	 */
	public synchronized static Location getLocationStr(AMapLocation location) {
		if (null == location) {
			return null;
		}
		Location myLoc = new Location();
//		StringBuffer sb = new StringBuffer();
		// errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
		if (location.getErrorCode() == 0) {
			// sb.append("定位成功" + "\n");
			//sb.append("定位类型: " + location.getLocationType() + "\n");
			// sb.append("经    度    : " + location.getLongitude() + "\n");
			//sb.append("纬    度    : " + location.getLatitude() + "\n");
			// sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
			// sb.append("提供者    : " + location.getProvider() + "\n");

			// if (location.getProvider().equalsIgnoreCase(
			// android.location.LocationManager.GPS_PROVIDER)) {
			// // 以下信息只有提供者是GPS时才会有
			// sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
			// sb.append("角    度    : " + location.getBearing() + "\n");
			// // 获取当前提供定位服务的卫星个数
			// sb.append("星    数    : " + location.getSatellites() + "\n");
			// } else {
			// // 提供者是GPS时是没有以下信息的
			// sb.append("国    家    : " + location.getCountry() + "\n");
			// sb.append("省            : " + location.getProvince() + "\n");
			// sb.append("市            : " + location.getCity() + "\n");
			// sb.append("城市编码 : " + location.getCityCode() + "\n");
			// sb.append("区            : " + location.getDistrict() + "\n");
			// sb.append("区域 码   : " + location.getAdCode() + "\n");
			// sb.append("地    址    : " + location.getAddress() + "\n");
			// sb.append("兴趣点    : " + location.getPoiName() + "\n");
			// // 定位完成的时间
			// sb.append("定位时间: "
			// + formatUTC(location.getTime(),
			// "yyyy-MM-dd HH:mm:ss:sss") + "\n");
			// }
//			sb.append(location.getCity());

			myLoc.setState(1);
			myLoc.setCity(location.getCity().substring(0, location.getCity().indexOf("市")));
			myLoc.setLatitude(location.getLatitude());
			myLoc.setLongitude(location.getLongitude());
			myLoc.setLocationtype(location.getLocationType());
			myLoc.setAddress(location.getAddress());
			myLoc.setPoiName(location.getPoiName());
			myLoc.setCityCode(location.getCityCode());
		} else {
			// 定位失败
//			sb.append("定位失败");
			myLoc.setState(2);
			// sb.append("错误码:" + location.getErrorCode() + "\n");
			// sb.append("错误信息:" + location.getErrorInfo() + "\n");
			// sb.append("错误描述:" + location.getLocationDetail() + "\n");
		}
		// 定位之后的回调时间
		// sb.append("回调时间: "
		// + formatUTC(System.currentTimeMillis(),
		// "yyyy-MM-dd HH:mm:ss:sss") + "\n");
		return myLoc;
	}

	/**
	 * @param
	 * @Description:城市码
	 * @reurn
	 */
	public synchronized static String getLocationCode(AMapLocation location) {
		if (null == location) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(location.getAdCode());
		return sb.toString();
	}

	private static SimpleDateFormat sdf = null;

	public synchronized static String formatUTC(long l, String strPattern) {
		if (TextUtils.isEmpty(strPattern)) {
			strPattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (sdf == null) {
			try {
				sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
			} catch (Throwable e) {
			}
		} else {
			sdf.applyPattern(strPattern);
		}
		if (l <= 0l) {
			l = System.currentTimeMillis();
		}
		return sdf == null ? "NULL" : sdf.format(l);
	}
}
