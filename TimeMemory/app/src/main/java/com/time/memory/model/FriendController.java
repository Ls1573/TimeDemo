package com.time.memory.model;


import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.ContactsDao;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.User;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IFriendController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:好友操作
 * @date 2016/10/14 13:45
 */
public class FriendController extends BaseController implements IFriendController {

	private ContactsDao contactsDao;

	public FriendController() {
		contactsDao = MainApplication.getDaoSession().getContactsDao();
	}

	/**
	 * 申请加好友
	 *
	 * @param url
	 * @param userId
	 */
	@Override
	public void reqAddFriend(String url, String userId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userId", userId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
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
	 * 查找好友
	 *
	 * @param url
	 * @param userMobile
	 * @param callback
	 */
	@Override
	public void reqFriends(String url, String userMobile, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userMobile", userMobile);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<GroupContacts>() {
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

	@Override
	public Object getContactsById(String userId, String comeFromId) {
		QueryBuilder qb = contactsDao.queryBuilder();
		qb.where(ContactsDao.Properties.UserId.eq(userId), ContactsDao.Properties.ToUserId.eq(comeFromId));
		return qb.unique();
	}

	/**
	 * 获取资料
	 *
	 * @param url
	 * @param userId
	 */
	@Override
	public void reqFriendInfo(String url, String userId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userId", userId);
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

}
