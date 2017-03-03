package com.time.memory.gui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:下拉刷新-过滤反复刷新
 * @date 2016/11/14 11:21
 */
public class MySwipeRefreshLayout extends SwipeRefreshLayout {

	public MySwipeRefreshLayout(Context context) {
		super(context);
	}

	public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
		return !isRefreshing() && super.onStartNestedScroll(child, target, nestedScrollAxes);
	}
}