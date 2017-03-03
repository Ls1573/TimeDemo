package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.GroupInDto;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.MGroupDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IGroupController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:群操作
 * @date 2016/10/14 11:17
 */
public class GroupController extends BaseController implements IGroupController {
	private MGroupDao mGroupDao;

	public GroupController() {
		mGroupDao = MainApplication.getDaoSession().getMGroupDao();
	}

	/**
	 * 申请进群
	 *
	 * @param url
	 * @param groupId
	 * @param callback
	 */
	@Override
	public void reqAddGroup(String url, String groupId, String adminUserId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);
		map.put("userId", adminUserId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MGroup>() {
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
	 * 获取群信息
	 *
	 * @param key
	 * @return
	 */
	@Override
	public MGroup getGroupByKey(String key) {
		return mGroupDao.load(key);
	}

	/**
	 * 获取群信息
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Object getGroupByKey(String key, String userId) {
		QueryBuilder queryBuilder = mGroupDao.queryBuilder();
		queryBuilder.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.GroupId.eq(key));
		return queryBuilder.unique();
	}

	/**
	 * 获取群信息
	 *
	 * @return
	 */
	@Override
	public Object getGroupByUserId(String userId, String type) {
		QueryBuilder queryBuilder = mGroupDao.queryBuilder();
		queryBuilder.where(MGroupDao.Properties.Type.eq(type), MGroupDao.Properties.ComeFrom.eq(userId));
		return queryBuilder.unique();
	}

	/**
	 * 更新数据
	 *
	 * @param mGroup
	 */
	@Override
	public void upMGroup(MGroup mGroup) {
		mGroupDao.updateInTx(mGroup);
	}

	/**
	 * 获取群信息
	 *
	 * @param url
	 * @param groupId
	 * @param callback
	 */
	@Override
	public void reqGroup(String url, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupNum", groupId);
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MGroup>() {
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
	 * 退出圈子
	 */
	@Override
	public void reqExitCircle(String url, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);

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


	/**
	 * 删除用户
	 *
	 * @param url
	 * @param groupId
	 * @param userIdList
	 * @param callback
	 */
	@Override
	public void reqDeleteUsers(String url, String groupId, List<String> userIdList, final SimpleCallback callback) {
		GroupInDto groupInDto = new GroupInDto();
		groupInDto.setGroupId(groupId);
		groupInDto.setUserToken(MainApplication.getUserToken());
		groupInDto.setUserIdList(userIdList);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(groupInDto));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new Response.Listener<JSONObject>() {
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
	public List<GroupContacts> getList() {
		return null;

	}

	/**
	 * 退出圈子
	 *
	 * @param groupId
	 */
	@Override
	public void deleteById(String groupId) {
		mGroupDao.deleteByKey(groupId);
	}

	/**
	 * 获取用户
	 *
	 * @param url
	 * @param groupId
	 * @param callback
	 */
	@Override
	public void getPeople(String url, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);

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


}
