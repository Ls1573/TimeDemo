package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.MainApplication;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.Advert;
import com.time.memory.entity.AdvertDao;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IAdvertController;
import com.time.memory.util.JsonUtils;
import com.time.memory.util.LogUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * @author Qiu
 * @version V1.0
 * @Description:广告
 * @date 2016/11/14 9:58
 */
public class AdvertController extends BaseController implements IAdvertController {

	private AdvertDao advertDao;

	public AdvertController() {
		advertDao = MainApplication.getDaoSession().getAdvertDao();
	}

	@Override
	public void reqAdvert(String url, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				LogUtil.LogShitou(response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<Advert>() {
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
	public List<Advert> getAdvertFromDb() {
		return advertDao.loadAll();
	}

	@Override
	public void saveAdvert(Advert advert) {
		advertDao.deleteAll();
		advertDao.save(advert);
	}
}
