package com.time.memory.core.cache;

import com.time.memory.core.net.NetManager;
import com.time.memory.core.volley.RequestQueue;
import com.time.memory.core.volley.toolbox.ImageLoader;

import java.io.File;

/**
 * 图片下载
 */
public class ImageDownloader {

	private static ImageDownloader mInstance;
	private RequestQueue mRequestQueue; // 请求队列
	private ImageLoader mImageLoader; // Volley_ImageLoader
	private VolleyImageCache mVolleyImageCache;// Volley图片缓存

	private ImageDownloader() {
		mRequestQueue = NetManager.getInstance().getRequestQueue();// 创建请求队列
		mVolleyImageCache = new VolleyImageCache(); // 创建图片缓存
		mImageLoader = new ImageLoader(mRequestQueue, mVolleyImageCache);
	}

	// 拿到实例
	public static ImageDownloader getInstance() {
		if (mInstance == null) {
			mInstance = new ImageDownloader();
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		return this.mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		return this.mImageLoader;
	}

	// 清除内存缓冲
	public void clearMemCache() {
		if (mVolleyImageCache != null) {
			mVolleyImageCache.evictAll();
		}
	}

	// 清除硬盘缓冲
	public void clearDiskCache(String url) {
		if (mVolleyImageCache != null) {
			mVolleyImageCache.clearDiskCache(url);
		}
	}

	// 缓存目录
	public String getDiskCacheFilePath() {
		if (mVolleyImageCache != null) {
			File cacheFolder = mVolleyImageCache.getCacheFolder();
			return cacheFolder.getAbsolutePath();
		}
		return null;
	}

	// 缓存大小
	public long getDiskCacheFileSize() {
		if (mVolleyImageCache != null) {
			return mVolleyImageCache.getCacheSize();
		}
		return 0;
	}
}