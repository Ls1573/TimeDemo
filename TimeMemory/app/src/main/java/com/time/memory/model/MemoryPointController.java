package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMemoryPointController;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆片段
 * @date 2016/10/27 8:51
 */
public class MemoryPointController extends BaseController implements IMemoryPointController {

	/**
	 * 获取片段详情
	 *
	 * @param url
	 * @param pointId
	 * @param type
	 * @param callback
	 */
	@Override
	public void reqMemoryInfos(String url, String pointId, String type, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("pointId", pointId);
		map.put("type", type);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				//CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemoryEdit>() {
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
