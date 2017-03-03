package com.time.memory.core.task.download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 *
 */
public class FileDownloadManager {

	private DownloadManager dm;
	private Context context;
	private static FileDownloadManager instance;

	private FileDownloadManager(Context context) {
		dm = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		this.context = context.getApplicationContext();
	}

	public static FileDownloadManager getInstance(Context context) {
		if (instance == null) {
			instance = new FileDownloadManager(context);
		}
		return instance;
	}

	/**
	 * 开始下载
	 *
	 * @param uri
	 * @param title
	 * @param description
	 * @return download id
	 */
	public long startDownload(String uri, String title, String description) {
		DownloadManager.Request req = new DownloadManager.Request(
				Uri.parse(uri));
		// wifi和联网状态可下载
		req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
				| DownloadManager.Request.NETWORK_MOBILE);
		// 此下载是可见的，并显示在通知
		req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		// 设置文件的保存的位置
		// file:///storage/emulated/0/Android/data/your-package/files/Download/memory.apk
		req.setDestinationInExternalFilesDir(context,
				Environment.DIRECTORY_DOWNLOADS, "timememory.apk");
		// 设置一些基本显示信息
		req.setTitle(title);
		req.setDescription(description);
		// 设置此下载MIME内容类型。此将覆盖服务器响应中声明的内容类型
		req.setMimeType("application/vnd.android.package-archive");
		return dm.enqueue(req);
	}

	/**
	 * 获取保存文件的地址
	 *
	 * @param downloadId an ID for the download, unique across the system. This ID is
	 *                   used to make future calls related to this download.
	 * @see FileDownloadManager#getDownloadPath(long)
	 */
	public Uri getDownloadUri(long downloadId) {
		return dm.getUriForDownloadedFile(downloadId);
	}

	public DownloadManager getDm() {
		return dm;
	}

	/**
	 * 获取下载状态
	 *
	 * @param downloadId an ID for the download, unique across the system. This ID is
	 *                   used to make future calls related to this download.
	 * @return int
	 * @see DownloadManager#STATUS_PENDING
	 * @see DownloadManager#STATUS_PAUSED
	 * @see DownloadManager#STATUS_RUNNING
	 * @see DownloadManager#STATUS_SUCCESSFUL
	 * @see DownloadManager#STATUS_FAILED
	 */
	public int getDownloadStatus(long downloadId) {
		DownloadManager.Query query = new DownloadManager.Query()
				.setFilterById(downloadId);
		Cursor c = dm.query(query);
		if (c != null) {
			try {
				if (c.moveToFirst()) {
					return c.getInt(c
							.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

				}
			} finally {
				c.close();
			}
		}
		return -1;
	}
}
