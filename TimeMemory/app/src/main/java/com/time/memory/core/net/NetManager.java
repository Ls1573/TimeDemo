package com.time.memory.core.net;


import com.time.memory.core.volley.Cache;
import com.time.memory.core.volley.DefaultRetryPolicy;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.RequestQueue;
import com.time.memory.core.volley.toolbox.JsonRequest;
import com.time.memory.util.CLog;

/**
 * 网络管理
 */
public class NetManager {

	public static final String TAG = "NetManager";
	// 网络请求队列(Volley)
	private NetRequestQueue mRequestQueue;

	private static class NetManagerHolder {
		private static NetManager sEngine = new NetManager();
	}

	// 拿到实例
	public static NetManager getInstance() {
		return NetManagerHolder.sEngine;
	}

	private NetManager() {
		mRequestQueue = NetRequestQueue.getInstance();
	}

	/**
	 * 加入到消息队列中
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setRetryPolicy(new DefaultRetryPolicy(50 * 1000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		if (mRequestQueue != null) {
			mRequestQueue.addToRequestQueue(req, tag);
		}
		// 打印log
		try {
			if (CLog.DEBUG) {
				JsonRequest<T> cReq = (JsonRequest<T>) req;
				CLog.e(TAG, "url:" + cReq.getUrl());
				CLog.e(TAG, "body:" + new String(cReq.getBody()));
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 加入到消息队列中
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		req.setRetryPolicy(new DefaultRetryPolicy(50 * 1000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		if (mRequestQueue != null) {
			mRequestQueue.addToRequestQueue(req);
		}
		// 打印log
		try {
			if (CLog.DEBUG) {
				GsonRequest<T> cReq = (GsonRequest<T>) req;
				CLog.e(TAG, "url=[ " + cReq.getUrl());
				CLog.e(TAG, "getBody=[ " + new String(cReq.getBody()));
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 清理缓存
	 */
	public void clearCache(Runnable r) {
		if (mRequestQueue != null) {
			mRequestQueue.clearCache(r);
		}
	}

	/**
	 * 清理缓存_特定
	 */
	public void clearCache(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelPendingRequests(tag);
		}
	}

	/**
	 * 拿到缓存中url对应的数据
	 *
	 * @param url
	 * @return
	 */
	public Cache.Entry getCache(String url) {
		return getRequestQueue().getCache().get(url);
	}

	/**
	 * 拿到消息队列
	 */
	public RequestQueue getRequestQueue() {
		return mRequestQueue.getRequestQueue();
	}
}
