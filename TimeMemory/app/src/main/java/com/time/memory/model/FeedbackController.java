package com.time.memory.model;


import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IFeedbackController;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/14 16:24
 */
public class FeedbackController extends BaseController implements IFeedbackController {

	/**
	 * 意见反馈
	 *
	 * @param url
	 * @param msg
	 */
	@Override
	public void reqFeedback(String url, String msg, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("detail", msg);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<BaseEntity>() {
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
