package com.time.memory.util;

import java.io.File;

import android.text.TextUtils;

/**
 * @author Qiu
 * 
 */
public class FileUtil {

	static final class FileType {
		public static final String APK = "apk";
		public static final String OTHER = "other";

	}

	// apk地址
	public static File getApkFile(String fileName) throws Exception {
		return AttPathUtils.getCacheDir(fileName);
	}

	// 其他类型地址
	public static File getDiskCacheDir(String fileName) throws Exception {
		return AttPathUtils.getCacheDir(fileName);
	}

	/**
	 * get file name from path, include suffix
	 * 
	 * <pre>
	 *      getFileName(null)               =   null
	 *      getFileName("")                 =   ""
	 *      getFileName("   ")              =   "   "
	 *      getFileName("a.mp3")            =   "a.mp3"
	 *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	 *      getFileName("abc")              =   "abc"
	 *      getFileName("c:\\")              =   ""
	 *      getFileName("c:\\a")             =   "a"
	 *      getFileName("c:\\a.b")           =   "a.b"
	 *      getFileName("c:a.txt\\a")        =   "a"
	 *      getFileName("/home/admin")      =   "admin"
	 *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	 * </pre>
	 * 
	 * @param filePath
	 * @return file name from path, include suffix
	 */
	public static String getFileName(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	}

}
