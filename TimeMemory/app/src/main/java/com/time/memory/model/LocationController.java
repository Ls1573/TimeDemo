package com.time.memory.model;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.time.memory.MainApplication;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:定位基类
 * @date 2016/11/12 14:46
 */
public class LocationController {

	private static LocationController sInstance;
	protected AMapLocationClient mLocationClient = null;// 声明AMapLocationClient类对象
	protected AMapLocationClientOption locationOption = null;// 声明mLocationOption对象

	// 创建
	public static LocationController getInstance() {
		if (sInstance == null) {
			synchronized (LocationController.class) {
				if (sInstance == null) {
					sInstance = new LocationController();
				}
			}
		}
		return sInstance;
	}

	// 开始定位
	public void startLoc() {
		mLocationClient.startLocation();
	}

	// 停止定位
	public void onDestroy() {
		if (null != mLocationClient) {
			mLocationClient.stopLocation();// 停止定位
			mLocationClient.onDestroy();// 销毁定位客户端
			locationOption = null;
		}
	}

	// 初始化定位
	public void initLocation(AMapLocationListener locationListener) {
		// 初始化定位
		mLocationClient = new AMapLocationClient(MainApplication.getContext());
		// 设置定位回调监听
		mLocationClient.setLocationListener(locationListener);
		// 初始化定位参数
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		locationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		locationOption.setOnceLocation(true);
		// 设置是否开启缓存
		locationOption.setLocationCacheEnable(true);

		if (locationOption.isOnceLocationLatest()) {
			locationOption.setOnceLocationLatest(true);
			// 设置setOnceLocationLatest(boolean
			// b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
			// 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
		}
		// 设置是否强制刷新WIFI，默认为强制刷新
		locationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		locationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		locationOption.setInterval(1000);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(locationOption);
	}

}
