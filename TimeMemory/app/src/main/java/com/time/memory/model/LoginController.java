package com.time.memory.model;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.im.IMClientManager;
import com.time.memory.core.im.android.conf.ConfigEntity;
import com.time.memory.core.im.android.core.LocalUDPDataSender;
import com.time.memory.core.volley.AuthFailureError;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.core.volley.toolbox.JsonObjectRequest;
import com.time.memory.core.volley.toolbox.JsonRequest;
import com.time.memory.entity.User;
import com.time.memory.entity.UserDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:登录
 * @date 2016-8-9上午11:52:52
 * ==============================
 */
public class LoginController extends BaseController implements ILoginController {

	private UserDao userDao;

	public LoginController() {
		userDao = MainApplication.getDaoSession().getUserDao();
	}

	/**
	 * @Description:请求登录(HTTP)
	 */
	public void reqLogin(final String url, final String phone, final String pwd, String appVersion,
						 final SimpleCallback callback) {

		ArrayMap<String, String> map = new ArrayMap<>();
		map.put("userMobile", phone);
		map.put("userPw", pwd);
		map.put("appVersion", appVersion);
		JSONObject jsonObject = new JSONObject(map);

		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
				Request.Method.POST, url, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						CLog.e(TAG, response.toString());
						clearrNet();
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
		}) {
			@Override
			public android.support.v4.util.ArrayMap<String, String> getHeaders()
					throws AuthFailureError {
				android.support.v4.util.ArrayMap<String, String> headers = new android.support.v4.util.ArrayMap<>();
				headers.put("application/json", "application/json; charset=UTF-8");
				headers.put("Connection", "keep-alive");
				return headers;
			}
		};
		mNetManager.addToRequestQueue(jsonRequest, TAG);
	}

	/**
	 * 修改用户信息
	 *
	 * @param url
	 * @param callback
	 */
	@Override
	public void upDateUser(String url, String pic, String nickName, String email, String company, String sex, String address, String address1, String sign, String profression, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userName", nickName);
		map.put("headPhoto", pic);
		map.put("userGender", sex);
		map.put("email", email);
		map.put("address", address);
		map.put("address1", address1);
		map.put("sign", sign);
		map.put("company", company);
		map.put("companyIntroduce", profression);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
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
	 * 建立长连接
	 */
	@Override
	public void reqLoginIm(final Context context, String Ip, String port, final String userId, final String pwd, final SimpleCallback callback) {
		// 设置服务器ip
		ConfigEntity.serverIP = Ip;
		// 设置服务器端口
		ConfigEntity.serverUDPPort = Integer.parseInt(port);

		Observer onLoginSucessObserver = onLoginSucessObserver = new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				// 服务端返回的登陆结果值
				callback.onCallback(data);
			}
		};
		IMClientManager.getInstance(context).getBaseEventListener().setLoginOkForLaunchObserver(onLoginSucessObserver);

		// 异步提交登陆名和密码
		new LocalUDPDataSender.SendLoginDataAsync(context, userId, pwd) {
			/**
			 * 登陆信息发送完成后将调用本方法（注意：此处仅是登陆信息发送完成 ，真正的登陆结果要在异步回调中处理哦）。
			 *
			 * @param code
			 *            数据发送返回码，0 表示数据成功发出，否则是错误码
			 */
			@Override
			protected void fireAfterSendLogin(int code) {
				if (code == 0) {
					CLog.e(TAG, "登陆信息已成功发出！userId:" + userId + "     pwd:" + pwd);
				} else {
					CLog.e(TAG, "数据发送失败。错误码是：" + code);
				}
			}
		}.execute();
	}

	@Override
	public void saveUser(User user) {
		//保存用户信息
		user.setActive(1);
		userDao.insertOrReplace(user);
		//更新其他用户数据
		MainApplication.getDb().execSQL("update user set active=0 where Id !=?", new Object[]{user.getUserId()});
	}

	@Override
	public void updateUser(User user) {
		//保存用户信息/更新
		MainApplication.getDaoSession().getUserDao().updateInTx(user);
	}

	@Override
	public void updateUser(String key) {
		//保存用户信息/更新
		MainApplication.getDb().execSQL("update user set contacts_status=1 where Id =?", new Object[]{key});
	}


	@Override
	public void updateUser(String key, String obj, String data) {
		//保存用户信息/更新
		MainApplication.getDb().execSQL("update user set " + obj + "=" + data + " where Id =?", new Object[]{key});
	}

	@Override
	public void reqUser(SimpleCallback call) {
		//查找活跃用户
		QueryBuilder qb = userDao.queryBuilder();
		qb.where(UserDao.Properties.Active.eq(1));
		User user = (User) qb.unique();
		onCallback(call, user);
	}

	@Override
	public User getUser(String key) {
		return userDao.load(key);
	}


	/**
	 * @Title: reqVerify
	 * @Description: 请求验证码(HTTP)
	 */
	public void reqVerify(final String methodName, final String phone,
						  final SimpleCallback callback) {
	}

}
