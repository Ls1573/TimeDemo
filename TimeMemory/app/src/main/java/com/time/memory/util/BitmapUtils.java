package com.time.memory.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.util.Log;

import com.time.memory.dao.ConfigDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片处理
 *
 * @author Qiu
 */
public class BitmapUtils {

	private static final String TAG = "BitmapUtils";

	/**
	 * 二进制转Bitmap
	 *
	 * @param temp
	 * @return
	 */
	public static Bitmap getBitmapFromByte(byte[] temp) {
		if (temp != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 从SD卡文件中读取Bitmap
	 *
	 * @param filePath
	 * @return
	 */
	public static Bitmap readBitmapFromFile(String filePath) {
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			return BitmapFactory.decodeFile(filePath, opt);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 该方法就是对drawable形式的图片进行压缩,转换成bitmap设置采样率, 减少Bitmap的像素, 从而减少了它所占用的内存
	 *
	 * @param
	 * @return
	 */
	public static Bitmap getDrawable(Resources res, int id) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bmp = BitmapFactory.decodeResource(res, id);
		int w = options.outWidth;
		int h = options.outHeight;
		float hh = 1080f;//
		float ww = 1920f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (options.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (options.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;// 设置采样率
//		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;// 同时设置才会有效
		options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		options.inJustDecodeBounds = false;
		// bmp = BitmapFactory.decodeResource(res, id, options);
		// Drawable drawable =new BitmapDrawable(res,bmp);
		return BitmapFactory.decodeResource(res, id, options);
	}

	/**
	 * 该方法就是对Bitmap形式的图片进行压缩, 也就是通过设置采样率, 减少Bitmap的像素, 从而减少了它所占用的内存
	 *
	 * @param srcPath
	 * @return
	 */
	public static Bitmap compressImageFromFile(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 1080f;//
		float ww = 1920f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// 图片的度数
		int degree = readPictureDegree(srcPath);
		if (bitmap != null) {
			bitmap = rotaingImageView(degree, bitmap);
		}
		return bitmap;
	}

	// 旋转
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	// 读取图片的角度
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
		}
		return degree;
	}


	/**
	 * 将Bitmap写到sd缓存中
	 *
	 * @param path 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩 特点是:
	 *             File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
	 */
	@SuppressWarnings("finally")
	public static boolean compressBmpToFile(Bitmap bmp, String path) {
		if (bmp == null) return false;
		File file = new File(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 90;// 个人喜欢从90开始,
		bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		while (baos.toByteArray().length / 1920 > 300) {
			baos.reset();
			quality -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (bmp != null) {
				bmp.recycle();
				bmp = null;
			}
			return true;
		}
	}

	/**
	 * 根据url连接去网络抓取图片
	 *
	 * @param url
	 * @return url对应的图片
	 */
	public static void writeImage(String url, ConfigDao configDao) {
		HttpURLConnection conn = null;
		InputStream is = null;
		String fileName;// 解析url得到文件名
		try {
			// 得到最后一个'/'的位置
			int lastIndexOf = url.lastIndexOf("/");
			// 字符截取
			fileName = url.substring(lastIndexOf + 1);
			URL mURL = new URL(url); // 创建一个url对象
			// 得到http的连接对象
			conn = (HttpURLConnection) mURL.openConnection();
			conn.setRequestMethod("GET"); // 设置请求方法为Get
			conn.setConnectTimeout(10000); // 设置连接服务器的超时时间, 如果超过10秒钟, 没有连接成功,
			conn.setReadTimeout(5000); // 设置读取数据时超时时间, 如果超过5秒, 抛异常
			conn.connect(); // 开始链接
			int responseCode = conn.getResponseCode(); // 得到服务器的响应码
			if (responseCode == 200) {
				// 得到一个输入流
				is = conn.getInputStream();
				// 创建一个bitmap
				Bitmap bitmap = BitmapFactory.decodeStream(is); // 根据 流数据
				saveFile(bitmap, fileName, configDao);// 保存到本地
			} else {
				Log.i(TAG, "访问失败: responseCode = " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect(); // 断开连接
			}
			if (is != null) {
				try {
					is.close();// 关闭流
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 指定bitmap到本地保存的路径：
	 *
	 * @param bm       -bitmap图片
	 * @param fileName -文件名
	 */
	public static void saveFile(Bitmap bm, String fileName, ConfigDao configDao)
			throws Exception {
		// data/data/xxx/cache/roll_list.png(文件保存地址)
		File myCaptureFile = AttPathUtils.getCacheDir(fileName);
		OutputStream stream = null;
		CompressFormat format = Bitmap.CompressFormat.PNG;// bitmap图片压缩
		int quality = 100;
		try {
			stream = new FileOutputStream(myCaptureFile);
			bm.compress(format, quality, stream);
			// 保存到本地luckPan地址
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (bm != null) {
				bm.recycle();
				bm = null;
			}
		}
	}

	/**
	 * @param context
	 * @param cr
	 * @param Imagepath
	 * @return
	 */
	public static Bitmap getImageThumbnail(Context context, ContentResolver cr, String Imagepath) {
		ContentResolver testcr = context.getContentResolver();
		String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,};
		String whereClause = MediaStore.Images.Media.DATA + " = '" + Imagepath + "'";
		Cursor cursor = testcr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,
				null, null);
		int _id = 0;
		String imagePath = "";
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		if (cursor.moveToFirst()) {

			int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

			do {
				_id = cursor.getInt(_idColumn);
				imagePath = cursor.getString(_dataColumn);
			} while (cursor.moveToNext());
		}
		cursor.close();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, _id, MediaStore.Images.Thumbnails.MINI_KIND,
				options);
		return bitmap;
	}

	/**
	 * @param context
	 * @param cr
	 * @param Imagepath
	 * @return
	 */
	public static Bitmap getImageThumbnail2(Context context, ContentResolver cr, String Imagepath) {
		ContentResolver testcr = context.getContentResolver();
		String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,};
		String whereClause = MediaStore.Images.Media.DATA + " = '" + Imagepath + "'";
		Cursor cursor = testcr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,
				null, null);
		int _id = 0;
		String imagePath = "";
		if (cursor == null || cursor.getCount() == 0) {
			return null;
		}
		if (cursor.moveToFirst()) {
			int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			do {
				_id = cursor.getInt(_idColumn);
				imagePath = cursor.getString(_dataColumn);
			} while (cursor.moveToNext());
		}
		cursor.close();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, _id, MediaStore.Images.Thumbnails.MINI_KIND,
				options);
		return bitmap;
	}

}
