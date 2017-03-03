package com.time.memory.util;

import android.content.Context;
import android.content.DialogInterface;

import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.callback.SignAlertListener;
import com.time.memory.gui.SignAlertDialog;
import com.time.memory.gui.SignDialog;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Package @com.time.memory.util
 * @Description: Dialog通用类
 * @date @2016-8-3上午9:39:57
 * ==============================
 */
public class DialogUtils {

	/**
	 * 带编辑框的Dialog
	 *
	 * @param mContext
	 * @param title
	 * @param message
	 * @param dialogCallback
	 */
	public static void reqEditDialog(final Context mContext, final String title, final String message, final DialogCallback dialogCallback) {
		SignAlertDialog.Builder builder = new SignAlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new SignAlertListener() {
			@Override
			public void onClick(String var2, int var3) {
				dialogCallback.onCallback(var2);
			}
		});
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogCallback.onCallback(null);
					}
				});
		builder.show();

	}

	/**
	 * 请求
	 */
	public static void request(final Context mContext, final String title, final String leftText, final String rightText,
							   final DialogCallback dialogCallback) {
		SignDialog.Builder builder = new SignDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setPositiveButton(leftText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 设置操作事项
				dialogCallback.onCallback(true);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(rightText,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialogCallback.onCallback(false);
						dialog.dismiss();
					}
				});
		builder.show();
	}


	/**
	 * 请求
	 */
	public static void request(final Context mContext, final String title,
							   final DialogCallback dialogCallback) {
		SignDialog.Builder builder = new SignDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// 设置操作事项
				dialogCallback.onCallback(true);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialogCallback.onCallback(false);
						dialog.dismiss();
					}
				});
		builder.show();
	}
}
