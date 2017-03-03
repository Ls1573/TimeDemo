package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.util.CLog;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:新好友搜索
 * @date 2016/9/24 9:47
 */
public class NewFriendSearchHolder extends BaseHolder<Object> {
	private static final String TAG = "NewFriendSearchHolder";
	@Bind(R.id.new_search_tv)
	TextView new_search_tv;//搜索

	public NewFriendSearchHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(Object entity) {
		super.setData(entity);
	}


	@OnClick({R.id.new_search_tv})
	public void onClick(View view) {
		if (R.id.new_search_tv == view.getId()) {
			//搜索
			CLog.e(TAG, "搜索页面");
		}
	}
}
