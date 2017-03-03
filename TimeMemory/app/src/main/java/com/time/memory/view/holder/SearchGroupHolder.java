package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MGroup;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:搜索群
 * @date 2016/9/20 11:16
 */
public class SearchGroupHolder extends BaseHolder<MGroup> {
	@Bind(R.id.searchgroupp_tv)
	TextView searchgroupp_tv;
	@Bind(R.id.searchgroup_tv)
	TextView searchgroup_tv;

	public SearchGroupHolder(View view) {
		super(view);
	}

	@Override
	public void init() {

	}

	@Override
	public void setData(MGroup entity, final int position) {
		super.setData(entity);
		//群名
		searchgroup_tv.setText(entity.getGroupName());
		//创建者
		searchgroupp_tv.setText(String.format(mContext.getString(R.string.group_creater), entity.getAdminUserName()));

		//点击回调
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position);

			}
		});
	}
}
