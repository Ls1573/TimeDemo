package com.time.memory.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Package @com.time.memory.util
 * @Description: 控制软键盘
 * @date @2016-8-3上午11:08:52
 * ==============================
 */
public class KeyBoardUtils {


	/**
	 * @param
	 * @return
	 * @Description:已经显示，则隐藏，反之则显示
	 */
	public static void toggleBoard(Context mcontext) {
		InputMethodManager imm = (InputMethodManager) mcontext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 隐藏输入法
	 *
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			View focusView = activity.getCurrentFocus();
			if (focusView != null) {
				imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
			}
		}
	}

	/**
	 * 隐藏输入法
	 *
	 * @param
	 * @return
	 * @Description:隐藏虚拟键盘
	 */
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * @param
	 * @return
	 * @Description: 显示虚拟键盘
	 */
	public static void ShowKeyboard(View v) {
		v.setFocusableInTouchMode(true);
		v.setFocusable(true);
		v.requestFocus();
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}
}
