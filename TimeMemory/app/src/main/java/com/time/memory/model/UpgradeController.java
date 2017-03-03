/**
 *
 */
package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.AuthFailureError;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.core.volley.toolbox.JsonObjectRequest;
import com.time.memory.core.volley.toolbox.JsonRequest;
import com.time.memory.entity.Upgrade;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IUpgradeController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:版本更新
 * @date 2016/11/28 11:22
 */
public class UpgradeController extends BaseController implements IUpgradeController {

	/**
	 * @param url
	 * @Description: 检测版本-获得App最新版本号
	 */
	@Override
	public void reqUpgrade(final String url, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userToken", MainApplication.getUserToken());
		JSONObject jsonObject = new JSONObject(map);
		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
				Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "*************response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Upgrade>() {
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
		}) {
			@Override
			public ArrayMap<String, String> getHeaders()
					throws AuthFailureError {
				ArrayMap<String, String> headers = new ArrayMap<>();
				headers.put("application/json", "application/json; charset=UTF-8");
				headers.put("Connection", "keep-alive");
				return headers;
			}
		};
		mNetManager.addToRequestQueue(jsonRequest, TAG);
	}
}
