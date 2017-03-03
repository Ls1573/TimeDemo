package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.GroupMemorys;
import com.time.memory.entity.OtherMemorys;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:时间轴定义(时间)
 * @date 2016/11/9 16:07
 */
public class GroupTimeHolder extends BaseHolder<GroupMemorys> {
	@Bind(R.id.time_date_tv)
	TextView time_date_tv;//日期(天)
	@Bind(R.id.time_year_tv)
	TextView time_year_tv;//日期(年-月)

	public GroupTimeHolder(View view) {
		super(view);
	}

	@Override
	public void setData(GroupMemorys mData) {
		super.setData(mData);
		time_date_tv.setText(mData.getDate());
		time_year_tv.setText(mData.getYearMouth());
	}
}
