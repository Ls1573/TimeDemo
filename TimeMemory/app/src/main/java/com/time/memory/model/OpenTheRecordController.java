package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.OpenRecordController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/24.
 */
public class OpenTheRecordController extends BaseController implements OpenRecordController {
	/**
	 * @param url
	 * @param channel
	 * @param appVersion
	 * @param deviceFlg
	 * @param deviceName
	 * @param deviceVersion
	 * @param callback
	 */
	@Override
	public void openRecord(String url, String channel, String appVersion, String deviceFlg, String deviceName, String deviceVersion, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("channel", channel);
		map.put("appVersion", appVersion);
		map.put("deviceFlg", deviceFlg);
		map.put("deviceName", deviceName);
		map.put("deviceVersion", deviceVersion);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), BaseEntity.class));
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
