package com.time.memory.model;

import com.google.gson.reflect.TypeToken;
import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.CA01RespVo;
import com.time.memory.mt.vo.PraiseVo;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.MemoryPraise;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IForkController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:点赞处理
 * @date 2016/11/4 9:39
 */
public class ForkController extends BaseController implements IForkController {
	@Override
	public void reqFork(String url, String token, String memoryUserId, String praiseUserId, String memoryId, String memoryPointId, String groupId, String delFlg, String memoryIdSource, String memoryPointIdSource, final SimpleCallback callback) {
		//构建
		final PraiseVo praiseVo = new PraiseVo();
		praiseVo.setMemoryId(memoryId);
		praiseVo.setMemoryUserId(memoryUserId);
		praiseVo.setPraiseUserId(praiseUserId);
		praiseVo.setMemoryPointId(memoryPointId);
		praiseVo.setGroupId(groupId);
		praiseVo.setDelFlg(delFlg);
		praiseVo.setMemoryIdSource(memoryIdSource);
		praiseVo.setMemoryPointIdSource(memoryPointIdSource);

		CA01RespVo msgRequest = new CA01RespVo();
		msgRequest.setType(BusynessType.CA01.getIndex());
		msgRequest.setPraiseVo(praiseVo);
		msgRequest.setToken(token);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(msgRequest));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new com.time.memory.core.volley.Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemoryPraise>() {
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
