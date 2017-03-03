package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MGroup;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑部
 * @date 2016/11/5 9:25
 */
public class CircleEditHolder extends BaseHolder<MGroup> {
	@Bind(R.id.circle_editname_tv)
	TextView circle_editname_tv;//圈子名
	@Bind(R.id.circle_editadmin_tv)
	TextView circle_editadmin_tv;//管理员

	private int mPosition;

	public CircleEditHolder(View view) {
		super(view);
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(mPosition);
			}
		});
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(MGroup entity, int position) {
		super.setData(entity);
		this.mPosition = position;
		circle_editname_tv.setText(entity.getGroupName());
		circle_editadmin_tv.setText(String.format(mContext.getString(R.string.group_creater), entity.getAdminUserName()));
	}
}
