package com.time.memory.core.cache;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.time.memory.core.volley.toolbox.ImageLoader;
import com.time.memory.util.AppUtil;
import com.time.memory.util.CLog;
import com.time.memory.util.FileUtil;
import com.time.memory.util.Md5;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 硬盘缓存
 */
public class VolleyImageCache implements ImageLoader.ImageCache {

	private static final String TAG = "VolleyImageCache";

	private LruCache<String, Bitmap> mMemCache;

	private DiskLruCache mDiskCache;

	private CompressFormat mCompressFormat = CompressFormat.PNG;// 过滤压缩png格式图

	private static int IO_BUFFER_SIZE = 8 * 1024;// buffer缓冲8M

	private int mCompressQuality = 70;// 压缩质量70

	private static final int APP_VERSION = AppUtil.getVersionCode();

	private static final int VALUE_COUNT = 1;

	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 30;// 图片缓存 10m
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;

	public static final String DISK_CACHE = "bitmap";// cache目录

	public VolleyImageCache() {
		initMemCache(getDefaultLruCacheSize());
		initDiskCache(DISK_CACHE, DISK_IMAGECACHE_SIZE,
				DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY);
	}

	private void initDiskCache(String uniqueName, int diskCacheSize,
			CompressFormat compressFormat, int quality) {
		try {
			// diskCacheDir:/sdcard/Android/data/com.x.x/other/cache/XXX
			final File diskCacheDir = FileUtil.getDiskCacheDir(uniqueName);
			mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION,
					VALUE_COUNT, diskCacheSize);
			mCompressFormat = compressFormat;
			mCompressQuality = quality;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 初始化sd卡缓存大小??
	private void initMemCache(int maxSize) {
		mMemCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// getRowBytes:用于计算位图每一行所占用的内存字节数。
				return value.getRowBytes() * value.getHeight();
			}

		};
	}

	// 拿到当前运行程序最大缓存
	public static int getDefaultLruCacheSize() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		return cacheSize;
	}

	// 将bitMap 写入到文件
	private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor)
			throws IOException, FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0),
					IO_BUFFER_SIZE);
			// 压缩缓存了
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@Override
	public void putBitmap(String key, Bitmap data) {
		if (key.indexOf("http:") != -1) {
			key = key.substring(key.indexOf("http:"));
		}
		CLog.d(key, "key_url:" + key);// url
		key = Md5.getMD5(key);
		CLog.d(TAG, "putBitmap_key:" + key);// md5
		// mem
		mMemCache.put(key, data);
		// disk
		DiskLruCache.Editor editor = null;
		try {
			// key将会成为缓存文件的文件名,必须要和图片的URL是一一对应的
			editor = mDiskCache.edit(key);
			if (editor == null) {
				return;
			}
			if (writeBitmapToFile(data, editor)) {
				mDiskCache.flush();
				editor.commit();
			} else {
				editor.abort();
			}
		} catch (IOException e) {
			try {
				if (editor != null) {
					editor.abort();
				}
			} catch (IOException ignored) {
			}
		}

	}

	@Override
	public Bitmap getBitmap(String key) {
		key = Md5.getMD5(key);
		Bitmap bitmap = null;
		// 拿到key 返回 bitmap
		bitmap = mMemCache.get(key);

		if (bitmap != null) {
			return bitmap;
		}
		// 从Disk中抓取图片
		DiskLruCache.Snapshot snapshot = null;
		try {

			snapshot = mDiskCache.get(key);
			if (snapshot == null) {
				return null;
			}
			final InputStream in = snapshot.getInputStream(0);
			if (in != null) {
				final BufferedInputStream buffIn = new BufferedInputStream(in,
						IO_BUFFER_SIZE);
				bitmap = BitmapFactory.decodeStream(buffIn);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}

		return bitmap;

	}

	public boolean containsKey(String key) {
		boolean contained = false;
		DiskLruCache.Snapshot snapshot = null;
		try {
			snapshot = mDiskCache.get(key);
			contained = snapshot != null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (snapshot != null) {
				snapshot.close();
			}
		}

		return contained;

	}

	/**
	 * 清除缓存(硬盘上指定的内容)
	 * */
	public void clearDiskCache(String imageUrl) {
		try {
			CLog.d(TAG, "imageUrl:" + imageUrl);// 传入的url
			String key = Md5.getMD5(imageUrl);
			CLog.d(TAG, "clearDiskCache_key:" + key);// 转换的key
			mDiskCache.remove(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除缓存(内存上)
	 */
	public void evictAll() {
		if (mMemCache != null) {
			mMemCache.evictAll();
		}
	}

	/**
	 * 缓存文件夹
	 * */
	public File getCacheFolder() {
		return mDiskCache.getDirectory();
	}

	/**
	 * 缓存文件夹
	 * */
	public long getCacheSize() {
		return mDiskCache.size();
	}

}
