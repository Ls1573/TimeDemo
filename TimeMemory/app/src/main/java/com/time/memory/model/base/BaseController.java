package com.time.memory.model.base;


import android.support.v4.util.ArrayMap;

import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.net.NetManager;
import com.time.memory.core.volley.AuthFailureError;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.toolbox.JsonObjectRequest;
import com.time.memory.core.volley.toolbox.JsonRequest;
import com.time.memory.util.NetUtils;

import org.json.JSONObject;

/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0
 * @Package com.time.memory.controller
 * @Description:网络请求(WebService)
 * @date 2016-7-20 上午10:46:40
 * ==============================
 */
public abstract class BaseController {

	protected final String TAG = getClass().getSimpleName();

	private static final int SUCCESS = 1;// 成功
	private static final int FINAL = -1;// 失败
	private static final int NONET = -2;// 没有网络

	private String userToken;

	public NetManager mNetManager;

	public BaseController() {
		mNetManager = NetManager.getInstance();
	}

	/**
	 * 共通请求
	 */
	public void requestServer(final String url, final ArrayMap<String, Object> map, final Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		boolean hasNet = NetUtils.isNetworkAvailable(MainApplication.getContext());
		map.put("userToken", getUserToken());
		JSONObject jsonObject = new JSONObject(map);

		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
				Request.Method.POST, url, jsonObject, listener, errorListener) {
			@Override
			public ArrayMap<String, String> getHeaders()
					throws AuthFailureError {
				ArrayMap<String, String> headers = new ArrayMap<>();
				headers.put("application/json", "application/json; charset=UTF-8");
				headers.put("Connection", "keep-alive");
				return headers;
			}
		};
		mNetManager.addToRequestQueue(jsonRequest, TAG);
	}

	/**
	 * 共通请求
	 */
	public void requestServer(final String url, final JSONObject jsonObject, final Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		boolean hasNet = NetUtils.isNetworkAvailable(MainApplication.getContext());
		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
				Request.Method.POST, url, jsonObject, listener, errorListener) {
			@Override
			public ArrayMap<String, String> getHeaders()
					throws AuthFailureError {
				ArrayMap<String, String> headers = new ArrayMap<>();
				headers.put("application/json", "application/json; charset=UTF-8");
				headers.put("Connection", "keep-alive");
				return headers;
			}
		};
		mNetManager.addToRequestQueue(jsonRequest, TAG);
	}

	public void onCallback(SimpleCallback callback, Object data) {
		if (callback != null) {
			callback.onCallback(data);
		}
	}

	public void onNoNetCallback(SimpleCallback callback) {
		if (callback != null) {
			callback.onNoNetCallback();
		}
	}

	public void clearrNet() {
		if (mNetManager != null) {
			mNetManager.clearCache(TAG);
		}
	}


	public void setUserToken(String token) {
		this.userToken = token;
	}

	public String getUserToken() {
		return MainApplication.getUserToken();
	}
}