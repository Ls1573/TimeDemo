package com.time.memory.core.callback;

/**
 * @author Qiu
 * @version V1.0
 * @Description:圈子联系人回调
 * @date 2016/9/22 14:41
 */
public interface OnConstactCallBack {
	void onConstactsCallBack(int position, String constactsId);

	void onLongConstactsCallBack(int position, String constactsId);

	void onCreateCircle();

	void onContastsUp();

	void onCirlceSort();

	void onTags();

}
