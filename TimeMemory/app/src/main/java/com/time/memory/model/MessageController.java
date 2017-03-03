package com.time.memory.model;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Message;
import com.time.memory.entity.MessageDao;
import com.time.memory.entity.User;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IMessageController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONObject;

import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:消息
 * @date 2016/9/12
 * ==============================
 */
public class MessageController extends BaseController implements IMessageController {
	private MessageDao messageVoDao;

	public MessageController() {
		messageVoDao = MainApplication.getDaoSession().getMessageDao();
	}

	/**
	 * 好友申请
	 *
	 * @param url
	 * @param userId
	 * @param callback
	 */
	@Override
	public void reqAccept(String url, String userId, final SimpleCallback callback) {
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

	/**
	 * 圈子申请
	 *
	 * @param url
	 * @param userId
	 * @param callback
	 */
	@Override
	public void reqThrough(String url, String userId, String groupId, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userId", userId);
		map.put("groupId", groupId);
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
	 * 加入消息
	 *
	 * @param msg
	 */
	@Override
	public void addMessage(Message msg) {
		messageVoDao.insertInTx(msg);
	}

	/**
	 * 修改消息
	 *
	 * @param msg
	 */
	@Override
	public void upMessage(Message msg) {
		messageVoDao.update(msg);
	}

	/**
	 * 修改本地的
	 *
	 * @param list
	 */
	@Override
	public void updateMessage(List<Message> list) {
		for (Message entity : list) {
			CLog.e(TAG, "readFlag:" + entity.getReadFlg());
		}
		messageVoDao.updateInTx(list);
	}

	/**
	 * 获取未讀消息数量
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public long getUnReadedMessage(String userId) {
		QueryBuilder qb = messageVoDao.queryBuilder();
		qb.and(MessageDao.Properties.UserId.eq(userId), MessageDao.Properties.ReadFlg.eq("0"));
		return qb.count();
	}

	/**
	 * 删除
	 *
	 * @param Id
	 */
	@Override
	public void deleteBykey(String Id) {
		messageVoDao.deleteByKey(Id);
	}

	/***
	 * 保存到 db
	 *
	 * @param list
	 */
	@Override
	public void saveMesageList(final List<Message> list) {
		//插入信息数据,用户Id
		messageVoDao.insertOrReplaceInTx(list);
	}

	/**
	 * 获取所有的消息(db)
	 *
	 * @return
	 */
	@Override
	public List<Message> getMessages(String userId) {
		//查询信息数据
		QueryBuilder qb = messageVoDao.queryBuilder();
		qb.where(MessageDao.Properties.UserId.eq(userId), MessageDao.Properties.HasDeal.eq("0"));
		qb.orderAsc(MessageDao.Properties.ReadFlg);
		qb.orderDesc(MessageDao.Properties.InsDate);
		qb.orderDesc(MessageDao.Properties.InsTime);
		return qb.list();
	}

	/**
	 * 获取一条消息
	 *
	 * @param userId
	 * @param msgId
	 * @return
	 */
	@Override
	public Object getMessages(String userId, String msgId) {
		QueryBuilder qb = messageVoDao.queryBuilder();
		qb.where(MessageDao.Properties.UserId.eq(userId), MessageDao.Properties.Uuid.eq(msgId));
		qb.orderAsc(MessageDao.Properties.ReadFlg);
		return qb.unique();
	}


	@Override
	public long selectMesages() {
		//查询信息数据
		QueryBuilder qb = messageVoDao.queryBuilder();
		qb.where(MessageDao.Properties.ReadFlg.eq("0"));
		return qb.count();
	}

	/**
	 * 获取Message信息
	 *
	 * @param url
	 * @param callback
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public void reqMessage(String url, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Message>() {
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
	 * 设置消息已读
	 *
	 * @param userToken
	 * @param callback
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	public void setRead(String url, String userToken, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("userToken", userToken);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Message>() {
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
