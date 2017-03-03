package com.time.memory.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.time.memory.MainApplication;

import java.lang.reflect.Method;

public class DevUtils {

	// 获取屏幕的宽度
	public static int getScreenWidth() {
		android.view.WindowManager wm = (android.view.WindowManager) MainApplication
				.getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 *
	 * @return 屏幕高度
	 */
	public static int getScreenHeight() {
		android.view.WindowManager wm = (android.view.WindowManager) MainApplication
				.getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}


	//获取屏幕原始尺寸高度，包括虚拟功能键高度
	public static int getDpi(Context context) {
		int dpi = 0;
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, displayMetrics);
			dpi = displayMetrics.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}

	/**
	 * 获得屏幕高度
	 *
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 标题栏高度
	 *
	 * @return
	 */
	public static int getTitleHeight(Context mContext) {
		/**
		 * 获取状态栏高度——方法1
		 * */
		int statusBarHeight = 0;
		//获取status_bar_height资源的ID
		int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = mContext.getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}

	/**
	 * 获取 虚拟按键的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getBottomStatusHeight(Context context) {
		int totalHeight = getDpi(context);
		int contentHeight = getScreenHeight(context);
		return totalHeight - contentHeight;
	}


	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}


}
