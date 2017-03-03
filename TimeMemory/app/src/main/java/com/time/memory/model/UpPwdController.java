package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.User;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IUpPwdController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:更换密码
 * @date 2016/10/8 15:36
 */
public class UpPwdController extends BaseController implements IUpPwdController {

	/**
	 * 密码操作
	 *
	 * @param mobile
	 * @param verifyCode
	 * @param pwd
	 */
	@Override
	public void reqUpPwd(String url, String mobile, String verifyCode, String pwd, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userMobile", mobile);
		map.put("verifyCode", verifyCode);
		map.put("userPw", pwd);
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

}
