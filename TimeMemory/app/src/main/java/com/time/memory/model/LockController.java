package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.ILockController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.3
 * @Description:手势密码
 * @date 2017/1/12 10:12
 */
public class LockController extends BaseController implements ILockController {
	@Override
	public void reqPattern(String url, String groupId, String groupPw, final SimpleCallback simpleCallback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);
		map.put("groupPw", groupPw);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
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
