package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.Memory;
import com.time.memory.entity.MemorySuppory;
import com.time.memory.entity.ReleaseMemory;
import com.time.memory.entity.SupporyMemory;
import com.time.memory.entity.UnReadMemoryNum;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMemoryController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆
 * @date 2016/9/11 8:29
 */
public class MemoryController extends BaseController implements IMemoryController {


	/**
	 * 上传到服务器
	 *
	 * @param url
	 * @param releaseMemory
	 * @param simpleCallback
	 */
	@Override
	public void reqMemoryUpload(String url, ReleaseMemory releaseMemory, final SimpleCallback simpleCallback) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(releaseMemory));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		requestServer(url, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Memory>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				clearrNet();
				onCallback(simpleCallback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(simpleCallback);
			}
		});
	}

	/**
	 * 追加记忆上传到服务器
	 *
	 * @param url
	 * @param releaseMemory
	 * @param simpleCallback
	 */
	@Override
	public void reqMemoryUpload(String url, SupporyMemory releaseMemory, final SimpleCallback simpleCallback) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(releaseMemory));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemorySuppory>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				clearrNet();
				onCallback(simpleCallback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(simpleCallback);
			}
		});
	}

	/**
	 * 获取未读记忆条数
	 *
	 * @param url
	 * @param simpleCallback
	 */
	@Override
	public void reqMemoryUnRead(String url, final SimpleCallback simpleCallback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<UnReadMemoryNum>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				clearrNet();
				onCallback(simpleCallback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(simpleCallback);
			}
		});
	}
}
