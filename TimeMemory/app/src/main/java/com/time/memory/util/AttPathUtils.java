package com.time.memory.util;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.time.memory.MainApplication;

public class AttPathUtils {

	private static final String TAG = "AttPathUtils";

	private static final long SIZE_1M = 1048576; // 1M

	public static final String FILE_DIR = "didipark";

	// 拿到缓存地址
	public static File getCacheDir(String currFileName) throws Exception {
		String path = getRootPath();
		File f = new File(path);
		if (!f.exists()) {
			if (!makeDir(f)) {
				return null;
			}
		}
		return new File(path + File.separator + currFileName);
	}

	public static boolean makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		boolean result = dir.mkdir();
		return result;
	}

	/**
	 * 获取附件路径
	 * 
	 * @param data
	 * @return 成功返回路径，磁盘空间不足返回null
	 */
	public static String getRootPath() {
		boolean sdcardAvialable = true;
		if (SIZE_1M > getAvailableStore()) {
			// 空间<1MB，当不可利用
			sdcardAvialable = false;
		}
		if (sdcardAvialable) {
			return getInternalDir().getAbsolutePath();
		}
		return null;
	}

	/**
	 * 删除文件
	 * */
	public static void deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return;
		}
		File file = new File(filePath);
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 ;
			}
		}
	}

	/**
	 * 获取内部存储路径 /data/data/com.xxx.xxx/bedook/cache
	 * 
	 * @return
	 */
	public static File getInternalDir() {
		return MainApplication.getContext().getCacheDir();
	}

	/**
	 * 获取内部存储路径 /data/data/com.xxx.xxx/bedook/webView
	 * 
	 * @return
	 */
	public static File getWebViewDir() {
		File dir = MainApplication.getContext().getDir("webview",
				MainApplication.getContext().MODE_WORLD_READABLE);
		return dir;
	}

	/**
	 * 获取内部存储路径 /data/data/com.xxx.xxx/bedook/databases
	 * 
	 * @return
	 */
	public static File getDatabaseDir() {
		return new File("/data/data/"
				+ MainApplication.getContext().getPackageName() + "/databases");
	}

	/**
	 * 获取存储卡的剩余容量，单位为字节
	 * 
	 * @param filePath
	 * @return availableSpare
	 */
	public static long getAvailableStore() {
		String path = Environment.getExternalStorageDirectory().getPath();
		if (null == path)
			return 0;
		StatFs statFs = new StatFs(path);
		long blocSize = statFs.getBlockSize();
		@SuppressWarnings("unused")
		long totalBlocks = statFs.getBlockCount();
		long availaBlock = statFs.getAvailableBlocks();
		long availableSpare = availaBlock * blocSize;
		return availableSpare;
	}
}
