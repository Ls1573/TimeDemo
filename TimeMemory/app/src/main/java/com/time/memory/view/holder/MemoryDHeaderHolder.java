package com.time.memory.view.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.entity.Memory;
import com.time.memory.gui.MemoryTagView;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:记忆详情头
 * @date 2016/10/28 9:39
 */
public class MemoryDHeaderHolder extends BaseHolder<Memory> {
	@Bind(R.id.memoryd_title_tv)
	TextView memoryd_title_tv;//头
	@Bind(R.id.memoryd_tag_tv)
	TextView memoryd_tag_tv;//标签
	@Bind(R.id.memoryd_name_tv)
	TextView memoryd_name_tv;//用户
	@Bind(R.id.memoryd_date_tv)
	TextView memoryd_date_tv;//日期
	@Bind(R.id.add_tag)
	MemoryTagView tagView;


	public MemoryDHeaderHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void setData(Memory entity) {
		super.setData(mData);
		memoryd_title_tv.setText(entity.getTitle());

		if (entity.getGroupNameList() != null && !entity.getGroupNameList().isEmpty()) {
			tagView.setVisibility(View.VISIBLE);
			tagView.setSTags(entity.getGroupNameList());
		} else {
			tagView.setVisibility(View.GONE);
		}

		if (TextUtils.isEmpty(entity.getLabelName()))
			memoryd_tag_tv.setVisibility(View.GONE);
		else {
			memoryd_tag_tv.setText(entity.getLabelName());
			if (entity.getState() == "0") {
				//他的
				if (entity.getUserId().equals(MainApplication.getUserId())) {
					//当前用户==作者-->显示
					memoryd_tag_tv.setVisibility(View.VISIBLE);
				} else {
					memoryd_tag_tv.setVisibility(View.GONE);
				}
			} else {
				memoryd_tag_tv.setVisibility(View.VISIBLE);
			}
		}
		memoryd_name_tv.setText(entity.getUserName());
		memoryd_date_tv.setText(entity.getMemoryDateForShow());

	}
}
