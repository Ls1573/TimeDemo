package com.time.memory.core.task.download;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.time.memory.dao.ConfigDao;

import java.io.File;

/**
 * Apk下载,安装
 */
public class ApkUpdateUtils {
	public static final String TAG = ApkUpdateUtils.class.getSimpleName();

	public static void download(Context context, String url, String title) {
		// 获取下载Id
		long downloadId = ConfigDao.getInstance().getLong("downloadId");
		// Id!=-1->说明下载成功有对应数据
		if (downloadId != -1L) {
			FileDownloadManager fdm = FileDownloadManager.getInstance(context);
			// 获取下载状态
			int status = fdm.getDownloadStatus(downloadId);
			// 当下载已成功完成。
			if (status == DownloadManager.STATUS_SUCCESSFUL) {
				// 获取Id对应的Uri地址
				Uri uri = fdm.getDownloadUri(downloadId);
				// 启动更新界面
				if (uri != null) {
					// 比较下下载的apk和本地的apk
					if (compare(getApkInfo(context, uri.getPath()), context)) {
						// 启动安装
						startInstall(context, uri);
						return;
					} else {
						// 有问题重新下载
						fdm.getDm().remove(downloadId);
						start(context, url, title);
					}
				}
			} else if (status == DownloadManager.STATUS_FAILED) {
				// 失败了,重新下载
				start(context, url, title);
			} else {
				Log.d(TAG, "apk is already downloading");
			}
		} else {
			// 开始下载
			start(context, url, title);
		}
	}

	/**
	 * 开始下载
	 *
	 * @param context
	 */
	private static void start(Context context, String url, String title) {
		long id = FileDownloadManager.getInstance(context).startDownload(url,
				title, "下载完成后点击安装");
		// 保存Id
		ConfigDao.getInstance().setLong("downloadId", id);
		Log.d(TAG, "apk start download " + id);
	}

	/**
	 * 安装
	 *
	 * @param context
	 * @param uri
	 */
	public static void startInstall(Context context, Uri uri) {
//		Intent install = new Intent(Intent.ACTION_VIEW);
//		install.setDataAndType(uri, "application/vnd.android.package-archive");
//		install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		context.startActivity(install);

		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "timememory.apk");
		Intent intent = new Intent(Intent.ACTION_VIEW);
		// 由于没有在Activity环境下启动Activity,设置下面的标签
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
			//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
			Uri apkUri =FileProvider.getUriForFile(context, "com.time.memory.fileprovider", file);
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
		}
		context.startActivity(intent);
	}

	/**
	 * 获取apk程序信息[packageName,versionName...]
	 */
	private static PackageInfo getApkInfo(Context context, String path) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			return info;
		}
		return null;
	}

	/**
	 * 下载的apk和当前程序版本比较
	 *
	 * @return 如果当前应用版本小于apk的版本则返回true
	 */
	private static boolean compare(PackageInfo apkInfo, Context context) {
		if (apkInfo == null) {
			return false;
		}
		String localPackage = context.getPackageName();
		// 安装包和系统的报名一致
		if (apkInfo.packageName.equals(localPackage)) {
			try {
				PackageInfo packageInfo = context.getPackageManager()
						.getPackageInfo(localPackage, 0);
				// 安装包版本号>本地版本号
				if (apkInfo.versionCode > packageInfo.versionCode) {
					return true;
				}
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
