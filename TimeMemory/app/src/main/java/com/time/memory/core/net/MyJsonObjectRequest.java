package com.time.memory.core.net;

import android.support.v4.util.ArrayMap;

import com.time.memory.core.volley.AuthFailureError;
import com.time.memory.core.volley.NetworkResponse;
import com.time.memory.core.volley.ParseError;
import com.time.memory.core.volley.Request;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.toolbox.HttpHeaderParser;
import com.time.memory.core.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonSyntaxException;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;


public class MyJsonObjectRequest<T> extends JsonObjectRequest {
	private Class<T> mClassOfT;
	private Type mTypeOfT;
	private String mTag;

	public MyJsonObjectRequest(int method, String url, JSONObject jsonRequest,
			Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	// 构造
	public MyJsonObjectRequest(String url, JSONObject jsonRequest,
			Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
		this(Method.POST, url, jsonRequest, listener, errorListener);
	}

	public void setClassOfT(Class<T> classOfT) {
		this.mClassOfT = classOfT;
	}

	public void setTypeOfT(Type typeOfT) {
		this.mTypeOfT = typeOfT;
	}

	@Override
	public Request<?> setTag(Object tag) {
		this.mTag = tag == null ? "easd" : tag.toString();
		return this;
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			CLog.d(mTag, jsonString);
			T parsedGSON = null;
			if (mClassOfT != null) {
				parsedGSON = JsonUtils.fromJson(jsonString, mClassOfT);
			} else if (mTypeOfT != null) {
				parsedGSON = JsonUtils.fromJson(jsonString, mTypeOfT);
			}
			return Response.success(new JSONObject(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException je) {
			return Response.error(new ParseError(je));
		} catch (JSONException ej) {
			return Response.error(new ParseError(ej));
		}
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		ArrayMap<String, String> headers = new ArrayMap<String, String>();
		headers.put("Accept-Encoding", "gzip");
		return headers;
	}

}