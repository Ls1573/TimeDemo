package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IBindEmailController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:绑定邮箱
 * @date 2016/11/24 11:48
 */
public class BindController extends BaseController implements IBindEmailController {

	@Override
	public void reqBind(String url, String email, String verifyCode, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("email", email);
		map.put("verifyCode", verifyCode);
		requestServer(url, map, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						clearrNet();
						CLog.e(TAG, "response:" + response.toString());
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
				}

		);
	}
}
