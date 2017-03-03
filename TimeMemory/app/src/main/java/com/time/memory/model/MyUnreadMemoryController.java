package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.Reminds;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.MyUnreadController;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/31.
 */
public class MyUnreadMemoryController extends BaseController implements MyUnreadController {
	//我的 和 他的 未读消息列表
	@Override
	public void getMyUnreadMemory(String url, String type, final SimpleCallback simpleCallback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("type", type);


		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogShitou(response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
				}.getType()));

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				onCallback(simpleCallback, null);
			}

			@Override
			public void onNoNetError() {
				clearrNet();
				onNoNetCallback(simpleCallback);
			}
		});
	}

	//我的 和 他的 吧未读的记忆设置成已读
	@Override
	public void getHasReadMemory(String url, String type, String pointId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("type", type);
		map.put("pointId", pointId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
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

	//他的 把未读的记忆片段设置成已读
	@Override
	public void getSupplementHasReadMemory(String url, String pointID, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("pointId", pointID);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
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
	public void getOReadMemory(String url, String type, String memoryID, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("type", type);
		map.put("memoryId", memoryID);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), new TypeToken<Reminds>() {
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
