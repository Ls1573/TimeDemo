package com.time.memory.model;


import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Lable;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IAddTagController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:添加标签
 * @date 2016/9/26 10:36
 */
public class AddTagController extends BaseController implements IAddTagController {
	/**
	 * 获取群标签
	 *
	 * @param url
	 * @param callback
	 */
	@Override
	public void getGroupTags(String url, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Lable>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				clearrNet();
				onCallback(callback, null);
			}

			@Override
			public void onNoNetError() {

			}
		});
	}

	/**
	 * 获取标签
	 *
	 * @param url
	 * @param callback
	 */
	@Override
	public void getTags(String url, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Lable>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onCallback(callback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(callback);
			}
		});
	}


	/**
	 * 删除标签
	 *
	 * @param url
	 * @param lableId
	 * @param callback
	 */
	@Override
	public void removeTag(String url, String lableId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("labelId", lableId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				//CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<BaseEntity>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onCallback(callback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(callback);
			}
		});
	}

	@Override
	public void reqAddTag(String url, String title, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);
		map.put("labelName", title);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Lable>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onCallback(callback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(callback);
			}
		});
	}

	/**
	 * 创建我的标签
	 *
	 * @param url
	 * @param title
	 * @param callback
	 */
	@Override
	public void reqAddUserTag(String url, String title, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("labelName", title);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Lable>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onCallback(callback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(callback);
			}
		});
	}
}
