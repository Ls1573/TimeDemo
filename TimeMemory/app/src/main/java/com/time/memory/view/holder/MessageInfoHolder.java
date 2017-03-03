package com.time.memory.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.mt.vo.MessageVo;
import com.time.memory.R;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:消息条目
 * @date 2016/9/12 10:37
 * ==============================
 */
public class MessageInfoHolder extends BaseHolder<MessageVo> {

	@Bind(R.id.message_pic_iv)
	ImageView message_pic_iv;//  信息图片
	@Bind(R.id.message_date_tv)
	TextView message_date_tv;//新消息日期
	@Bind(R.id.message_time_tv)
	TextView message_time_tv;//新消息时间
	@Bind(R.id.message_new_tv)
	TextView message_new_tv;//新消息内容


	public MessageInfoHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(MessageVo entity) {
		super.setData(entity);
		message_new_tv.setText("测试数据,好好好好好");
	}
}
