package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.User;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMineController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/14 16:49
 */
public class MineController extends BaseController implements IMineController {
	/**
	 * 修改用户信息
	 *
	 * @param url
	 * @param user
	 * @param callback
	 */
	@Override
	public void reqUpUser(String url, User user, final SimpleCallback callback) {
		ArrayMap<String, Object> userVo = new ArrayMap<>();
		userVo.put("userName", user.getUserName());
		userVo.put("headPhoto", user.getHeadPhoto());
		userVo.put("userGender", user.getUserGender());
		userVo.put("email", user.getEmail());
		userVo.put("address", user.getAddress());
		userVo.put("sign", user.getSign());
		userVo.put("company", user.getCompany());
		userVo.put("companyIntroduce", user.getCompanyIntroduce());

		requestServer(url, userVo, new Response.Listener<JSONObject>() {
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
}
