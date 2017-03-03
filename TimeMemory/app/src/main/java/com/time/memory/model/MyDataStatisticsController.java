package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.DataStatistics;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.DataStatisticsController;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/7.
 */
public class MyDataStatisticsController extends BaseController implements DataStatisticsController {

	@Override
	public void getDataStatistics(String url, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou("response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(), DataStatistics.class));
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
