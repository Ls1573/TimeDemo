package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.GroupAdDto;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.MGroupDao;
import com.time.memory.entity.MemoryDto;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.ICircleController;
import com.time.memory.mt.nio.message.response.CG01RespVo;
import com.time.memory.mt.vo.UserVo;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子操作
 * @date 2016/10/12 13:40
 */
public class CircleController extends BaseController implements ICircleController {
	private MGroupDao groupDao;

	public CircleController() {
		groupDao = MainApplication.getDaoSession().getMGroupDao();
	}

	/**
	 * 修改圈子
	 *
	 * @param url
	 * @param groupName
	 * @param groupId
	 * @param callback
	 */
	@Override
	public void reqUpCircle(String url, String groupName, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("groupId", groupId);
		map.put("groupName", groupName);
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
	 * 获取圈子列表
	 *
	 * @param url
	 * @param callback
	 */
	@Override
	public void reqCircle(String url, String userId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userId", userId);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
//						CLog.e(TAG, response.toString());
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemoryDto>() {
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
	 * 群激活
	 *
	 * @param url
	 * @param dto
	 * @param callback
	 */
	@Override
	public void reqCircle(String url, GroupAdDto dto, final SimpleCallback callback) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(dto));
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
	 * 追加好友进圈子
	 *
	 * @param url
	 * @param users
	 * @param callback
	 */
	@Override
	public void reqAddMenInCirlce(String url, String userToken, String groupId, List<UserVo> users, final SimpleCallback callback) {
		CG01RespVo msgRequest = new CG01RespVo();
		msgRequest.setUserVoList(users);//用户信息
		msgRequest.setUserToken(userToken);
		msgRequest.setGroupId(groupId);
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(msgRequest));
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
	 * 创建圈子
	 *
	 * @param groupName
	 * @param userToken
	 * @param users
	 */
	@Override
	public void reqAddCircle(String url, String groupName, String
			userToken, List<UserVo> users, final SimpleCallback callback) {
		CG01RespVo msgRequest = new CG01RespVo();
		msgRequest.setUserVoList(users);//用户信息
		msgRequest.setGroupName(groupName);//圈子信息
		msgRequest.setUserToken(userToken);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(msgRequest));
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

	@Override
	public void reqGroups(String url, String memoryIdSource, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("memoryIdSource", memoryIdSource);

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
	 * 插入一条
	 */
	@Override
	public void saveCircle(MGroup entity) {
		groupDao.insertInTx(entity);

	}

	/**
	 * 插入多条
	 *
	 * @param list
	 */
	@Override
	public void saveCircle(List<MGroup> list) {
		groupDao.insertOrReplaceInTx(list);
	}


	/**
	 * 删除一条记录
	 */
	@Override
	public void delCirlceById(String key) {
		groupDao.deleteByKey(key);
	}

	/**
	 * 移除全部
	 */
	@Override
	public void removeAll() {
		groupDao.deleteAll();
	}

	/**
	 * 修改一条
	 */
	@Override
	public void upCirlce(MGroup group) {
		groupDao.update(group);
	}

	/**
	 * 修改多条记录
	 *
	 * @param mGroups
	 */
	@Override
	public void upCirlces(List<MGroup> mGroups) {
		groupDao.updateInTx(mGroups);
	}

	/**
	 * 修改一条
	 *
	 * @param groupId
	 * @param groupName
	 * @param userId
	 */
	@Override
	public void upCirlceById(String groupId, String groupName, String userId) {
		MainApplication.getDb().execSQL("update mgroup set groupName=? ,comeFrom=? where groupId =?", new Object[]{groupName, userId, groupId});
	}


	/**
	 * 修改一条
	 *
	 * @param groupId
	 * @param userId
	 * @param msg
	 * @param data
	 */
	@Override
	public void upCirlceByKey(String groupId, String userId, String msg, String data) {
		//保存用户信息/更新
		MainApplication.getDb().execSQL("update mgroup set " + msg + "=" + data + " where groupId =? and comeFrom=?", new Object[]{groupId, userId});
	}

	@Override
	public MGroup getGroup(String Id) {
		return groupDao.load(Id);
	}

	/**
	 * 根据userId获取多条纪律
	 *
	 * @param userId
	 */
	@Override
	public List<MGroup> getGroups(String userId) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.Type.notEq(1));
		qb.orderAsc(MGroupDao.Properties.Type);
		qb.orderAsc(MGroupDao.Properties.ActiveFlg);
		return qb.list();
	}

	/**
	 * 根据userId获取多条纪律(只有被激活的数据)
	 *
	 * @param userId
	 */
	@Override
	public List<MGroup> getGroupsByUser(String userId) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.Type.eq("2"), MGroupDao.Properties.ActiveFlg.eq("0"));
		qb.orderAsc(MGroupDao.Properties.Type);
		qb.orderAsc(MGroupDao.Properties.ActiveFlg);
		return qb.list();
	}


	@Override
	public List<MGroup> getGroupsByUser(String userId, String type) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.Type.eq(type));
		return qb.list();
	}

	@Override
	public List<MGroup> getGroupsByUId(String userId, String type) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.Type.notEq(type), MGroupDao.Properties.Type.notEq(3), MGroupDao.Properties.ActiveFlg.eq("0"));
		qb.orderAsc(MGroupDao.Properties.Type);
		qb.orderAsc(MGroupDao.Properties.ActiveFlg);
		return qb.list();
	}

	@Override
	public List<MGroup> getGroupsByUIdAndGId(String userId, String groupId) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.GroupId.notEq(groupId), MGroupDao.Properties.Type.notEq(3), MGroupDao.Properties.ActiveFlg.eq("0"));
		qb.orderAsc(MGroupDao.Properties.Type);
		qb.orderAsc(MGroupDao.Properties.ActiveFlg);
		return qb.list();
	}


	@Override
	public List<MGroup> getGroupsByGroupId(String userId, String groupId) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.GroupId.notEq(groupId), MGroupDao.Properties.Type.notEq(3), MGroupDao.Properties.ActiveFlg.eq("0"));
		qb.orderAsc(MGroupDao.Properties.Type);
		qb.orderAsc(MGroupDao.Properties.ActiveFlg);
		return qb.list();
	}

	@Override
	public List<MGroup> getGroupsByGroup(String userId, String groudId) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.GroupId.eq(groudId));
		return qb.list();
	}

	@Override
	public List<MGroup> getGroupsByAdminId(String userId, String adminId) {
		QueryBuilder qb = groupDao.queryBuilder();
		qb.where(MGroupDao.Properties.ComeFrom.eq(userId), MGroupDao.Properties.AdminUserId.eq(adminId));
		return qb.list();
	}
}
