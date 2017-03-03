package com.time.memory.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:Uri取得地址路径
 * @date 2016/9/19 18:36
 */
public class TakePhotoUtils {

	private final String TAG = "TakePhotoController";

	/**
	 * 获取Uri地址
	 *
	 * @param context
	 * @param path
	 * @return
	 */
	public static Uri FilePathToUri(Context context, String path) {
		CLog.d("TAG", "filePath is " + path);
		if (path != null) {
			if (path.contains("file://")) {
				return Uri.parse(path);
			}
			path = Uri.decode(path);
			CLog.d("TAG", "path2 is " + path);
			ContentResolver cr = context.getContentResolver();
			StringBuffer buff = new StringBuffer();
			buff.append("(")
					.append(MediaStore.Images.ImageColumns.DATA)
					.append("=")
					.append("'" + path + "'")
					.append(")");
			Cursor cur = cr.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Images.ImageColumns._ID},
					buff.toString(), null, null);
			int index = 0;
			for (cur.moveToFirst(); !cur.isAfterLast(); cur
					.moveToNext()) {
				index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
				// set _id value
				index = cur.getInt(index);
			}
			if (index == 0) {
				//do nothing
			} else {
				Uri uri_temp = Uri
						.parse("content://media/external/images/media/"
								+ index);
				CLog.e("TAG", "uri_temp is " + uri_temp);
				if (uri_temp != null) {
					return uri_temp;
				}
			}

		}
		return null;
	}


	// 通过uri取得路径
	public static String getFilePathFromUri(Uri shownUri, Context activity) {
		String imgPath = null;
		if (shownUri != null) {
			if (isFileImageUri(shownUri)) {
				String uriStr = shownUri.toString();
				try {
					imgPath = uriStr.substring(uriStr.indexOf("file://")
							+ "file://".length());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return imgPath;
			}
			String[] proj = {MediaStore.Images.Media.DATA};

			try {
				Cursor cursor = activity.getContentResolver().query(shownUri,
						proj, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					int actual_image_column_index = cursor
							.getColumnIndex(MediaStore.Images.Media.DATA);
					// 图片地址全路径
					imgPath = cursor.getString(actual_image_column_index);
				}
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imgPath;
	}

	private static boolean isFileImageUri(Uri shownUri) {
		if (shownUri == null) {
			return false;
		}
		String uriStr = shownUri.toString().toLowerCase();
		if (uriStr.startsWith("file://")) {
			if (isImageFormat(uriStr)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isImageFormat(String fileName) {
		if (fileName == null) {
			return false;
		}
		fileName = fileName.toLowerCase();
		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
				|| fileName.endsWith(".png") || fileName.endsWith(".bmp")
				|| fileName.endsWith(".jpe") || fileName.endsWith(".gif")
				|| fileName.endsWith(".wbmp")) {
			return true;
		}
		return false;
	}

}
