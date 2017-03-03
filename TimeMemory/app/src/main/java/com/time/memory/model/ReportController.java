package com.time.memory.model;

import android.support.v4.util.ArrayMap;

import com.google.gson.reflect.TypeToken;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.volley.Response;
import com.time.memory.core.volley.VolleyError;
import com.time.memory.entity.BaseEntity;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IReportController;
import com.time.memory.util.CLog;
import com.time.memory.util.JsonUtils;

import org.json.JSONObject;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/24 13:19
 */
public class ReportController extends BaseController implements IReportController {

	/**
	 * 举报/投诉
	 *
	 * @param url
	 * @param complainUserId     被投诉人用户ID	可为空
	 * @param memoryId           被投诉记忆ID	可为空
	 * @param memoryPointId      被投诉记忆片段ID	可为空
	 * @param commentId          被投诉评论ID	可为空
	 * @param complainType       投诉类型	1：用户 2：记忆
	 * @param complainDetailType 投诉内容分类	1：色情/暴力信息 2：广告信息 3：钓鱼/欺诈信息 4：诽谤造谣信息
	 * @param callback
	 */
	@Override
	public void reqReport(String url, String complainUserId, String memoryId, String memoryPointId, String commentId, String complainType, String complainDetailType, final SimpleCallback callback) {
		ArrayMap<String, Object> map = new ArrayMap<>();
		map.put("complainUserId", complainUserId);
		map.put("memoryId", memoryId);
		map.put("memoryPointId", memoryPointId);
		map.put("commentId", commentId);
		map.put("complainType", complainType);
		map.put("complainDetailType", complainDetailType);

		requestServer(url, map, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				clearrNet();
				CLog.e(TAG, "response:" + response.toString());
				onCallback(callback, JsonUtils.fromJson(response.toString(),
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
}
