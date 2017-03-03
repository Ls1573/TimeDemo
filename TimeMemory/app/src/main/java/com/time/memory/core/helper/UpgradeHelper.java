package com.time.memory.core.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.time.memory.core.service.DownloadService;
import com.time.memory.gui.CustomDialog;
import com.time.memory.util.CLog;


/**
 * 版本更新
 */
public class UpgradeHelper {
	protected static final String TAG = "UpgradeHelper";

	private Dialog mNorDialog;
	private ProgressDialog mProgressDialog;
	private Context mAct;
	//	private ActivityTaskManager taskManager;
	private String url;// 下载地址

	public UpgradeHelper(Context context) {
		this.mAct = context;
//		this.taskManager = ActivityTaskManager.getInstance();
	}

	public void setDownloadUrl(String url) {
		this.url = url;
	}

	/**
	 *
	 */
	// 更新
	public void confNorUpdate(String tips, String desc, boolean needUpdate
	) {
//		this.taskManager = taskManager;
		if (TextUtils.isEmpty(tips))
			tips = "发现新版本，请更新后使用";
		// 强制更新
		if (needUpdate) {
			if (mNorDialog == null) {
				mNorDialog = CustomDialog.createCommonCustomDialog(mAct, tips,
						desc, true, needClickListener);
			}
		} else {
			if (mNorDialog == null) {
				mNorDialog = CustomDialog.createCommonCustomDialog(mAct, tips,
						desc, false, mDlgClickListener);
			}
		}
		// mNorDialog.setTitle(tips);
		mNorDialog.setCancelable(false);
		if (!((Activity) mAct).isFinishing())
			mNorDialog.show();
	}

	// 强制更新
	protected DialogInterface.OnClickListener needClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (dialog.equals(mNorDialog)) {
				switch (which) {
					// 退出
					case CustomDialog.LEFT_BUTTON_CLICK:
						CLog.e(TAG, "退出。。");
						dialog.dismiss();
//						taskManager.closeAllActivity();
						System.gc();
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					case CustomDialog.RIGHT_BUTTON_CLICK:
						// 更新
						CLog.e(TAG, "更新-----------------》");
						download();
						break;
				}
			}
		}
	};
	// 更新
	protected DialogInterface.OnClickListener mDlgClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			if (dialog.equals(mNorDialog)) {
				switch (which) {
					case CustomDialog.LEFT_BUTTON_CLICK:
						CLog.e(TAG, "不更新-----------------》");
						mNorDialog = null;
						break;
					case CustomDialog.RIGHT_BUTTON_CLICK:
						CLog.e(TAG, "更新-----------------》");
						download();
						break;
				}
			}
		}
	};

	// 下载
	private void download() {
		//	ApkUpdateUtils.download(mAct, url, mAct.getResources().getString(R.string.app_name));
		Intent intent = new Intent(mAct, DownloadService.class);
		intent.putExtra("downloadUrl", url);
		mAct.startService(intent);
	}

}
