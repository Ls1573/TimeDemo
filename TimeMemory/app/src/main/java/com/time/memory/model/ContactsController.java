package com.time.memory.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.AuthFailureError;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.core.volley.toolbox.JsonObjectRequest;
import com.time.memory.core.volley.toolbox.JsonRequest;
import com.time.memory.entity.BaseEntity;
import com.time.memory.entity.Contacts;
import com.time.memory.entity.ContactsDao;
import com.time.memory.entity.GroupContacts;
import com.time.memory.entity.GroupContactsDao;
import com.time.memory.entity.User;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IContactsController;
import com.time.memory.mt.vo.ContactsVo;
import com.time.memory.mt.vo.ContactsVoDao;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;
import com.time.memory.util.RegUtils;

import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:联系人的操作
 * @date 2016-9-9下午4:59:14
 * ==============================
 */
public class ContactsController extends BaseController implements IContactsController {

	private ContactsVoDao contactsVoDao;
	private ContactsDao contactsDao;
	private GroupContactsDao groupContactsDao;

	public ContactsController() {
		contactsDao = MainApplication.getDaoSession().getContactsDao();
		contactsVoDao = MainApplication.getDaoSession().getContactsVoDao();
		groupContactsDao = MainApplication.getDaoSession().getGroupContactsDao();
	}

	/**
	 * 查询用户
	 *
	 * @param keyWord
	 * @param userId
	 * @return
	 */
	@Override
	public List<Contacts> getContacts(String keyWord, String userId) {
		//SELECT * FROM USER WHERE( USER_NAME LIKE '%邱%' OR USER_MOBILE LIKE '%13500780%') AND USER_GENDER='0' AND USER_GENDER='0'
		QueryBuilder qb = contactsDao.queryBuilder();
		qb.where(ContactsDao.Properties.ContactName.like("%" + keyWord + "%"), ContactsDao.Properties.ToUserId.eq(userId));
		return qb.list();
	}

	/**
	 * 查询联系人
	 */
	@Override
	public ArrayList<ContactsVo> getContacts(Context context) {
//		return queryContacts(context);
		return getAllPhoneContacts(context);
	}

	/**
	 * 保存通讯录
	 *
	 * @param list
	 */
	@Override
	public void saveContacts(List<Contacts> list) {
		contactsDao.insertOrReplaceInTx(list);
		CLog.e(TAG, "save-------------------------------------------------------->");
	}

	/**
	 * 保存通讯录
	 */
	@Override
	public void saveContact(Contacts contact) {
		contactsDao.insertOrReplace(contact);
	}


	/**
	 * 获取一个联系人
	 *
	 * @param userId
	 * @param toUserId
	 * @return
	 */
	@Override
	public Contacts getContactFromDb(String userId, String toUserId) {
		QueryBuilder qb = contactsDao.queryBuilder();
		qb.where(ContactsDao.Properties.ToUserId.eq(toUserId), ContactsDao.Properties.UserId.eq(userId));
		return (Contacts) qb.unique();
	}

	/**
	 * 从本地数据库查询联系人
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List<Contacts> getContactsFromDb(String userId) {
		QueryBuilder qb = contactsDao.queryBuilder();
		qb.where(ContactsDao.Properties.ToUserId.eq(userId), ContactsDao.Properties.IsTwoWayFlg.isNotNull());
		qb.orderAsc(ContactsDao.Properties.FLetter);
		return qb.list();
	}

	/**
	 * 从本地数据库查询联系人
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List<Contacts> getContactsFromDb(String userId, String isOnly) {
		QueryBuilder qb = contactsDao.queryBuilder();
		qb.where(ContactsDao.Properties.ToUserId.eq(userId), ContactsDao.Properties.IsTwoWayFlg.eq(isOnly));
		qb.orderAsc(ContactsDao.Properties.FLetter);
		return qb.list();
	}

	/**
	 * 插入临时数据
	 */
	@Override
	public void insertGroupTemp(ArrayList<GroupContacts> mList) {
		groupContactsDao.deleteAll();
		groupContactsDao.insertOrReplaceInTx(mList);
	}

	/**
	 * 插入临时数据(联系人)
	 */
	@Override
	public void insertContactsTemp(ArrayList<ContactsVo> mList) {
		contactsVoDao.deleteAll();
		contactsVoDao.insertInTx(mList);
	}

	@Override
	public ArrayList<ContactsVo> getFrContactsFromDb(String userId) {
		Cursor cursor = MainApplication.getDaoSession().getDatabase().rawQuery(
				"select c1.userMobile,c1.userName  from contactsvo c1 where not exists (select * from (select * from contacts c2 where c2.userId=?)as c3 where c1.userMobile = c3.phone) group by c1.userMobile"
				, new String[]{userId});

		ArrayList<ContactsVo> contactses = new ArrayList<>();
		if (cursor != null && cursor.getCount() > 0) {
			ContactsVo contactsEntity = null;
			while (cursor.moveToNext()) {
				contactsEntity = new ContactsVo();
				String userMobile = cursor.getString(0);// 当前取的是手机号数据
				String userName = cursor.getString(1);// 当前取的是姓名数据
				contactsEntity.setUserMobile(userMobile);
				contactsEntity.setUserName(userName);
				contactses.add(contactsEntity);
			}
			cursor.close();
		}
		return contactses;
	}

	/**
	 * 删除好友
	 *
	 * @param friendId
	 */
	@Override
	public void removeFriend(String friendId) {
		contactsDao.deleteByKey(friendId);
	}

	@Override
	public void removeAll() {
		contactsDao.deleteAll();
	}

	/**
	 * 删除好友
	 *
	 * @param url
	 * @param userId
	 * @param callback
	 */
	@Override
	public void reqRemoveFriend(String url, String userId, final SimpleCallback callback) {
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
	 * 和临时表比较取数据
	 *
	 * @param userId
	 */
	@Override
	public List<Contacts> reqContacts(String userId) {
		List<Contacts> contacts = new ArrayList<>();
		Contacts entity;
		Cursor cursor = MainApplication.getDb().rawQuery("select  contacts.Id,contacts.contactName,contacts.pinyin,contacts.fLetter,contacts.headPhoto,contacts.isActived from contacts where not exists(select groupcontacts.userId from groupcontacts where groupcontacts.userId=contacts.Id) and contacts.userId=?", new String[]{userId});
		while (cursor.moveToNext()) {
			//光标移动成功
			entity = new Contacts();
			String Id = cursor.getString(cursor.getColumnIndex("Id"));
			String name = cursor.getString(cursor.getColumnIndex("contactName"));
			String pinyin = cursor.getString(cursor.getColumnIndex("pinyin"));
			String fLetter = cursor.getString(cursor.getColumnIndex("fLetter"));
			String pic = cursor.getString(cursor.getColumnIndex("headPhoto"));
			String isActived = cursor.getString(cursor.getColumnIndex("isActived"));

			entity.setUserId(Id);
			entity.setContactName(name);
			entity.setfLetter(fLetter);
			entity.setPinyin(pinyin);
			entity.setHeadPhoto(pic);
			entity.setActiveFlg(isActived);
			contacts.add(entity);
		}
		cursor.close();
		return contacts;
	}

	/**
	 * 查询联系人(传统方法)
	 */
	public ArrayList<Contacts> queryContacts(Context context) {
		// 去raw_contacts表中取所有联系人的_id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);

		/** 联系人 **/
		ArrayList<Contacts> mContacts = new ArrayList<>();

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				int id = cursor.getInt(0);
				// 去data表中根据上面取到的_id查询对应id的数据.
				String selection = "raw_contact_id = ?";
				String[] selectionArgs = {String.valueOf(id)};
				Cursor c = resolver.query(dataUri, new String[]{"data1", "mimetype"},
						selection, selectionArgs, null);
				if (c != null && c.getCount() > 0) {
					Contacts contactsEntity = new Contacts();
					//联系人Id
					contactsEntity.setUserId(String.valueOf(id));
					while (c.moveToNext()) {
						String data1 = c.getString(0);        // 当前取的是data1数据
						String mimetype = c.getString(1);        // 当前取的是mimetype的值
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							// Log.i(TAG, "号码: " + data1);
							contactsEntity.setPhone(data1);
//							CLog.e(TAG, data1);
						} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
							//Log.i(TAG, "姓名: " + data1);
							contactsEntity.setContactName(data1);
						} else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
							// Log.i(TAG, "邮箱: " + data1);
							contactsEntity.setEmail(data1);
						} else if ("vnd.android.cursor.item/nickname".equals(mimetype)) {
							// Log.i(TAG, "昵称: " + data1);
							contactsEntity.setUserName(data1);
						} else if ("vnd.android.cursor.item/photo".equals(mimetype)) {
							// Log.i(TAG, "头像Uri: " + data1);
//                          contactsEntity.picUri = data1;
							// 获得contact_id的Uri
							contactsEntity.setPicUri(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id));
						}
					}
					mContacts.add(contactsEntity);
					c.close();
				}
			}
			cursor.close();
		}

		return mContacts;
	}

	/**
	 * 获取所有拥有手机号的联系人(快速)
	 *
	 * @param context
	 * @return
	 */
	public static ArrayList<ContactsVo> getAllPhoneContacts(Context context) {
		ArrayList<ContactsVo> listContacts = new ArrayList<ContactsVo>();
		int id = -1;
		ContentResolver cr = context.getContentResolver();
		long timeStart = new Date().getTime();
		String[] mContactsProjection = new String[]{
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.PHOTO_ID
		};
		String contactsId;
		String phoneNum;
		String name;

		//查询contacts表中的所有数据
		Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				contactsId = cursor.getString(0);
				phoneNum = cursor.getString(1);
				name = cursor.getString(2);
				// 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
				phoneNum = phoneNum.replaceAll("^(\\+86)", "");
				phoneNum = phoneNum.replaceAll("^(86)", "");
				phoneNum = phoneNum.replaceAll("-", "");
				phoneNum = phoneNum.replaceAll(" ", "");
				phoneNum = phoneNum.trim();
				// 如果当前号码是手机号码
				if (RegUtils.checkPhone(phoneNum)) {
					ContactsVo contactsEntity = new ContactsVo();
					contactsEntity.setUserName(name);
					contactsEntity.setUserMobile(phoneNum);
					listContacts.add(contactsEntity);
					id -= 1;
				}
			}
		}
		cursor.close();
		return listContacts;
	}

	/**
	 * 获取联系人(HTTP)
	 *
	 * @param url
	 * @param userToken
	 * @param callback
	 */
	@Override
	public void reqContacts(final String url, final String userToken,
							final SimpleCallback callback) {
		ArrayMap<String, String> map = new ArrayMap<>();
		map.put("userToken", userToken);
		JSONObject jsonObject = new JSONObject(map);

		JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
				Request.Method.POST, url, jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
//						CLog.e(TAG, response.toString());
						LogUtil.LogShitou(response.toString());
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
			public ArrayMap<String, String> getHeaders()
					throws AuthFailureError {
				ArrayMap<String, String> headers = new ArrayMap<>();
				headers.put("application/json", "application/json; charset=UTF-8");
				headers.put("Connection", "keep-alive");
				return headers;
			}
		};
		mNetManager.addToRequestQueue(jsonRequest, TAG);
	}

	/**
	 * 上传通讯录
	 *
	 * @param url
	 * @param contactsVo
	 * @param callback
	 */
	@Override
	public void reqUpContacts(String url, ContactsVo contactsVo, final SimpleCallback callback) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(contactsVo));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
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
	 * 修改好友备注信息
	 *
	 * @param url
	 * @param friendId
	 * @param friendName
	 * @param callback
	 */
	@Override
	public void reqUpContacts(String url, String friendId, String friendName, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("friendId", friendId);
		map.put("friendName", friendName);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Contacts>() {
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
