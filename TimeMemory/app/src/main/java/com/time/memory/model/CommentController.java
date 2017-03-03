package com.time.memory.model;

import com.google.gson.reflect.TypeToken;
import com.time.memory.mt.common.constant.message.BusynessType;
import com.time.memory.mt.nio.message.response.CA10RespVo;
import com.time.memory.mt.nio.message.response.CX02RespVo;
import com.time.memory.mt.vo.CommentVo;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.MemoryComment;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.ICommentController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:评论
 * @date 2016/9/20 12:13
 */
public class CommentController extends BaseController implements ICommentController {

	/**
	 * 评论请求
	 */
	@Override
	public void reqComment(String url, String token, CommentVo commentVo, final SimpleCallback callback) {
		//构建
		CA10RespVo msgRequest = new CA10RespVo();
		msgRequest.setType(BusynessType.CA10.getIndex());
		msgRequest.setCommentVo(commentVo);
		msgRequest.setUserToken(token);

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(msgRequest));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new com.time.memory.core.volley.Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemoryComment>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
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
	 * 删除评论
	 *
	 * @param url
	 * @param msgRequest
	 * @param callback
	 */
	@Override
	public void reqRemoveComment(String url, CX02RespVo msgRequest, final SimpleCallback callback) {
		//构建
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(JsonUtils.toJson(msgRequest));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		requestServer(url, jsonObject, new com.time.memory.core.volley.Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				CLog.e(TAG, response.toString());
				onCallback(callback, JsonUtils.fromJson(
						response.toString(),
						new TypeToken<MemoryComment>() {
						}.getType()));
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
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
