package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IUpPhoneController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:修改手机号
 * @date 2016/10/24 21:17
 */
public class UpPhoneController extends BaseController implements IUpPhoneController {

	/**
	 * 修改手机号
	 *
	 * @param url
	 * @param mobile
	 * @param verifyCode
	 * @param callback
	 */
	@Override
	public void reqEmail(String url, String mobile, String verifyCode, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userMobile", mobile);
		map.put("mobileVerifyCode", verifyCode);
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
		});
	}

	/**
	 * 请求验证码
	 *
	 * @param url
	 * @param email
	 * @param callback
	 */
	@Override
	public void reqVerify(String url, String email, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userMobile", email);
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
		});
	}

	/**
	 * 请求验证码
	 *
	 * @param url
	 * @param email
	 * @param callback
	 */
	@Override
	public void reqEmailVerify(String url, String email, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("email", email);
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
		});
	}

	/**
	 * 验证邮箱
	 *
	 * @param url
	 * @param callback
	 */
	@Override
	public void reqVerifyEmail(String url, String verifyCode, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
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
		});

	}
}
