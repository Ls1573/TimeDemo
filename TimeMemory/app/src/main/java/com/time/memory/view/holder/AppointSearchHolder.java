package com.time.memory.view.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.time.memory.R;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:指定圈子（头）
 * @date 2016/9/29 16:26
 */
public class AppointSearchHolder extends BaseHolder<Object> {
	@Bind(R.id.searchView)
	TextView searchView;//描述

	public AppointSearchHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
	}

	@Override
	public void setData(Object entity) {
		super.setData(entity);

	}
}
