package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MGroup;

import java.util.List;

import butterknife.Bind;

/**
 * ==============================
 * @author Qiu
 * @Description:共通脚
 * @date 2016/9/12
 * @version V1.0
 */
public class MsgFootHolder extends BaseHolder<List<MGroup>> {
	@Bind(R.id.group_tv)
	TextView group_tv;

	public MsgFootHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
	}

	@Override
	public void setData(List<MGroup> list) {
		super.setData(list);
		// group_tv.setText(text)
	}
}
