package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.Reminds;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.CircleUnreadController;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CircleUnreadMemoryController extends BaseController implements CircleUnreadController {
	//圈子的未读记忆列表
	@Override
	public void getMyUnreadMemory(String url, String type, String groupId, final SimpleCallback simpleCallback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("type", type);
		map.put("groupId", groupId);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
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

	//圈子的  把未读的记忆设置成已读
	@Override
	public void getHasReadMemoryGroup(String url, String memoryID, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("memoryId", memoryID);
		map.put("groupId", groupId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
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
				clearrNet();
				onNoNetCallback(callback);
			}
		});
	}

	//圈子的  把未读的记忆片段设置成已读
	@Override
	public void getSupplementHasReadMemoryGroup(final String url, String pointID, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("pointId", pointID);
		map.put("groupId", groupId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
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
				clearrNet();
				onNoNetCallback(callback);
			}
		});
	}
}
