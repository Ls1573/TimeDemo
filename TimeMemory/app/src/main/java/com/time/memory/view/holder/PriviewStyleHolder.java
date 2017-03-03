package com.time.memory.view.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.WriterStyleMemory;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:预览
 * @date 2016/10/11 11:58
 */
public class PriviewStyleHolder extends BaseHolder<WriterStyleMemory> {

	@Bind(R.id.writer_date)
	TextView writer_date;//日期
	@Bind(R.id.writer_style_et)
	TextView writer_style_et;//头
	@Bind(R.id.writer_tag_tv)
	TextView writer_tag_tv;//标签
	@Bind(R.id.writer_user_tv)
	TextView writer_user_tv;//作者

	public PriviewStyleHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(WriterStyleMemory entity) {
		super.setData(entity);
		//标签
		if (!TextUtils.isEmpty(entity.getLabelName())) {
			writer_tag_tv.setText(entity.getLabelName());
			writer_tag_tv.setVisibility(View.VISIBLE);
		} else {
			writer_tag_tv.setVisibility(View.GONE);
		}
		//主题
		writer_style_et.setText(entity.getTitle());
		//日期
		writer_date.setText(entity.getMemoryDate());
		//作者
		writer_user_tv.setText(Html.fromHtml("by<font color=#DEB54C> " + entity.getUsername() + " </font>"));
	}
}
