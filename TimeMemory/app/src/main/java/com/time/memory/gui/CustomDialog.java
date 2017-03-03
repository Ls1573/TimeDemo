package com.time.memory.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;

/**
 * 下载自定义弹窗
 */
public class CustomDialog {

	public static final int LEFT_BUTTON_CLICK = 12345;
	public static final int RIGHT_BUTTON_CLICK = 54321;

	/**
	 * @param context
	 * @param title        -提示头
	 * @param desc         -描述
	 * @param listener
	 * @param isNeedUpdate -true:强制更新;false:不需要强制
	 * @return
	 */
	public static Dialog createCommonCustomDialog(Context context,
												  String title, String desc, boolean isNeedUpdate,
												  final DialogInterface.OnClickListener listener) {

		return createCommonCustomDialog(R.layout.view_update_dialog, context, title,
				desc, isNeedUpdate, listener);
	}

	public static Dialog createCommonCustomDialog(int layoutId,
												  final Context context, String title, String desc, boolean isNeedUpdate,
												  final DialogInterface.OnClickListener listener) {

		final Dialog dialog = new Dialog(context, R.style.dialog_common);
		LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(context);
		View contentView = null;
		contentView = inflater.inflate(layoutId, null);
		// 关闭
		final TextView leftButton = (TextView) contentView
				.findViewById(R.id.update_dialog_close);
		// 确定提交
		final TextView rightButton = (TextView) contentView
				.findViewById(R.id.update_confirm);
		// 当前版本
		TextView update_version = (TextView) contentView
				.findViewById(R.id.update_version);
		// 版本描述
		TextView update_desc = (TextView) contentView
				.findViewById(R.id.update_desc);

		// 提示头
		if (!TextUtils.isEmpty(title)) {
			update_version.setText(title);
		}
		// 提示内容
		if (!TextUtils.isEmpty(desc)) {
			update_desc.setText(desc);
		}
		View.OnClickListener btnOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					switch (v.getId()) {
						// 关闭
						case R.id.update_dialog_close:
							listener.onClick(dialog, LEFT_BUTTON_CLICK);
							break;
						// 确定
						case R.id.update_confirm:
							listener.onClick(dialog, RIGHT_BUTTON_CLICK);
							rightButton.setText("更新中");
							rightButton.setBackgroundColor(context.getResources().getColor(R.color.grey_F4));
							rightButton.setClickable(false);
							break;
					}
				}
			}
		};

		leftButton.setOnClickListener(btnOnClickListener);
		rightButton.setOnClickListener(btnOnClickListener);

		dialog.setContentView(contentView);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (keyCode == 84) {// HTC的搜索实体键
					return true;
				}
				return false;
			}
		});
		return dialog;
	}

}
