package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMemoryInfoController;
import com.time.memory.mt.vo.MemoryDelVo;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/25 17:56
 */
public class MemoryInfoController extends BaseController implements IMemoryInfoController {

	/**
	 * 获取记忆详情
	 *
	 * @param url
	 * @param memoryId
	 * @param type
	 * @param callback
	 */
	@Override
	public void reqMemoryInfos(String url,String memoryId, String memorySrcId, String type, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("memoryId", memoryId);
		map.put("memorySrcId", memorySrcId);
		map.put("type", type);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemoryInfo>() {
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

	/**
	 * 移除记忆
	 *
	 * @param url
	 * @param token
	 * @param memoryDelVo
	 * @param simpleCallback
	 */
	@Override
	public void reqRemoveMemory(String url, String token, MemoryDelVo memoryDelVo, final SimpleCallback simpleCallback) {
		//构建
		JSONObject jsonObject = null;
		MemoryDelVo delVo = new MemoryDelVo();
		delVo.setMemoryDelVo(memoryDelVo);
		delVo.setUserToken(token);
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(delVo));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new com.time.memory.core.volley.Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<BaseEntity>() {
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
	 * 转发
	 *
	 * @param url
	 * @param memoryId
	 * @param groupId
	 * @param simpleCallback
	 */
	@Override
	public void reqForwardMemory(String url, String memoryId, String groupId, final SimpleCallback simpleCallback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("memoryId", memoryId);
		map.put("groupId", groupId);

		requestServer(url, map, new com.time.memory.core.volley.Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(simpleCallback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<BaseEntity>() {
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
