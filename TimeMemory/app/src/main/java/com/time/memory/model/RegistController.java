package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.User;
import com.time.memory.entity.UserDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IRegistController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:注册
 * @date 2016/9/23 13:41
 */
public class RegistController extends BaseController implements IRegistController {
	/**
	 * 验证码
	 *
	 * @param url
	 * @param phone
	 * @param callback
	 */
	@Override
	public void reqVerify(final String url, String phone, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userMobile", phone);
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
	 * 注册
	 *
	 * @param mobile
	 * @param verifyCode
	 * @param pwd
	 * @param nickName
	 */
	@Override
	public void reqRegist(String url, String mobile, String verifyCode, String pwd, String nickName, final SimpleCallback callback) {
		// 请求服务器
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userMobile", mobile);
		map.put("verifyCode", verifyCode);
		map.put("userPw", pwd);
		map.put("userName", nickName);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<User>() {
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
	 * 保存用户信息
	 *
	 * @param user
	 */
	@Override
	public void saveUser(User user) {
		UserDao userDao = MainApplication.getDaoSession().getUserDao();
		//激活状态
		user.setActive(1);
		userDao.insert(user);
	}

}
