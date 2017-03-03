/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.time.memory.core.volley.toolbox;

import android.content.Intent;
import android.widget.Toast;

import com.time.memory.MainApplication;
import com.time.memory.core.im.android.core.LocalUDPDataSender;
import com.time.memory.core.volley.NetworkResponse;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.Response.ErrorListener;
import com.time.memory.core.volley.Response.Listener;
import com.time.memory.core.volley.VolleyLog;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.login.LoginActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * A request for retrieving a T type response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T> JSON type of response expected
 */
public abstract class JsonRequest<T> extends Request<T> {
	private static final String TAG = "JsonRequest";
	/**
	 * Default charset for JSON request.
	 */
	protected static final String PROTOCOL_CHARSET = "utf-8";

	/**
	 * Content type for request.
	 */
	private static final String PROTOCOL_CONTENT_TYPE =
			String.format("application/json; charset=%s", PROTOCOL_CHARSET);


	private final Listener<T> mListener;
	private final String mRequestBody;

	/**
	 * Deprecated constructor for a JsonRequest which defaults to GET unless {@link #getPostBody()}
	 * or {@link #getPostParams()} is overridden (which defaults to POST).
	 *
	 * @deprecated Use {@link #JsonRequest(int, String, String, Listener, ErrorListener)}.
	 */
	public JsonRequest(String url, String requestBody, Listener<T> listener,
					   ErrorListener errorListener) {
		this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener, errorListener);
	}

	public JsonRequest(int method, String url, String requestBody, Listener<T> listener,
					   ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		mRequestBody = requestBody;
	}

	@Override
	protected void deliverResponse(T response) {
		JSONObject js = (JSONObject) response;
		int code;
		try {
			//CLog.e(TAG, "****************SONObject***************:" + js.toString());
			code = js.getInt("code");
			if (code == 2) {
				//登录错误
				Toast.makeText(MainApplication.getContext(), "登录过期,请重新登录", Toast.LENGTH_SHORT).show();
//				loginOut(MainApplication.getUserId());
//				startActivity();
				doLogout();
			} else {
				if (mListener != null) {
					mListener.onResponse(response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	/**
	 * @deprecated Use {@link #getBodyContentType()}.
	 */
	@Override
	public String getPostBodyContentType() {
		return getBodyContentType();
	}

	/**
	 * @deprecated Use {@link #getBody()}.
	 */
	@Override
	public byte[] getPostBody() {
		return getBody();
	}

	@Override
	public String getBodyContentType() {
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public byte[] getBody() {
		try {
			return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException uee) {
			VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
					mRequestBody, PROTOCOL_CHARSET);
			return null;
		}
	}


	/**
	 * 退出
	 *
	 * @param userId
	 */
	public void loginOut(String userId) {
		ILoginController iLoginController = new LoginController();
		User user = iLoginController.getUser(userId);
		user.setActive(0);
		iLoginController.updateUser(user);
//		iLoginController.updateUser(userId, "active", "0");
		MainApplication.setUserId("-1");
		MainApplication.setUserToken("");
	}

	/**
	 * 发出退出登陆请求包（Android系统要求必须要在独立的线程中发送）
	 */
	private void doLogout() {

//		ExecutorManager.getInstance().submit(new Runnable() {
//			@Override
//			public void run() {
		int code = -1;
		code = LocalUDPDataSender.getInstance(MainApplication.getContext()).sendLoginout();
		loginOut(MainApplication.getUserId());
		if (code == 0) {
			CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求已完成！");
		} else {
			CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求发送失败。错误码是：" + code + "！");
		}
		startActivity();
//			}
//		});
	}

	private void startActivity() {
		System.gc();
		Intent intent = new Intent(MainApplication.getContext(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		MainApplication.getContext().startActivity(intent);
	}
}
