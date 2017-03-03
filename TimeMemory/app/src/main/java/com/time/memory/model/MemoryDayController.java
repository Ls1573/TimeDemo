package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.Memory;
import com.time.memory.entity.OtherMemory;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMemoryDetailController;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆(天)
 * @date 2016/9/14 11:15
 */
public class MemoryDayController extends BaseController implements IMemoryDetailController {

	/**
	 * 获取记忆(我的)
	 *
	 * @param url
	 * @param searchType 业务查询类型 ECESearchType 热度：1 ; 日期：2 ; 标签 ：3
	 * @param lableId    标签
	 * @param groupId    圈子ID
	 * @param pageIndex
	 * @param pageSize
	 * @param callback
	 */
	@Override
	public void getMemoryAll(String url, String searchType, String lableId, String groupId, int pageIndex, int pageSize, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("labelId", lableId);
		map.put("groupId", groupId);
		map.put("searchType", searchType);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				LogUtil.LogShitou("response:" + response.toString());
				clearrNet();
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Memory>() {
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
	 * 获取记忆(他的)
	 *
	 * @param url
	 * @param searchType 业务查询类型 ECESearchType 热度：1 ; 日期：2 ; 标签 ：3
	 * @param lableId    标签
	 * @param groupId    圈子ID
	 * @param pageIndex
	 * @param pageSize
	 * @param callback
	 */
	@Override
	public void getOtherMemoryAll(String url, String searchType, String lableId, String groupId, int pageIndex, int pageSize, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("labelId", lableId);
		map.put("groupId", groupId);
		map.put("searchType", searchType);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou("response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<OtherMemory>() {
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
	 * 获取记忆(圈子的)
	 *
	 * @param url
	 * @param searchType 业务查询类型 ECESearchType 热度：1 ; 日期：2 ; 标签 ：3
	 * @param lableId    标签
	 * @param groupId    圈子ID
	 * @param pageIndex
	 * @param pageSize
	 * @param callback
	 */
	@Override
	public void getGroupMemoryAll(String url, String searchType, String lableId, String groupId, int pageIndex, int pageSize, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("pageIndex", pageIndex);
		map.put("pageSize", pageSize);
		map.put("labelId", lableId);
		map.put("groupId", groupId);
		map.put("searchType", searchType);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou("response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<GroupMemory>() {
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
