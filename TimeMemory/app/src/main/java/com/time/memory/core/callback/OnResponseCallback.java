package com.time.memory.core.callback;

/**
 * ==============================
 *
 * @author Qiu
 *
 * @Package com.time.memory.core.callback
 *
 * @Description:WebService回调
 *
 * @date 2016-7-25 下午4:21:05
 *
 * @version 1.0 ==============================
 */
public class OnResponseCallback<T> {

	public interface Listener<T> {
		public void onResponse(T response);
	}

	public interface ErrorListener {
		public void onErrorResponse();
	}

	public interface NoNetListener {
		public void onNoNetResponse();
	}

}
