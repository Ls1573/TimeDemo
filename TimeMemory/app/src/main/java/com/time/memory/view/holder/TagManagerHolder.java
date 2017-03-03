package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MGroup;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:标签管理
 * @date 2016/12/1 9:26
 */
public class TagManagerHolder extends BaseHolder<MGroup> {
	@Bind(R.id.tag_tv)
	TextView tag_tv;

	public TagManagerHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(final MGroup entity, final int position) {
		super.setData(entity);
		tag_tv.setText(entity.getGroupName());

		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);
			}
		});

	}
}
