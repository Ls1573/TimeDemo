package com.time.memory.view.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Footer;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:
 * @date 2016/10/22 16:13
 */
public class CommonFooterHolder extends BaseHolder<Footer> {
	@Bind(R.id.foot_view_tv)
	TextView foot_view_tv;
	@Bind(R.id.foot_view_pre)
	ProgressBar foot_view_pre;

	//上拉加载更多
	public static final int PULLUP_LOAD_MORE = 0;
	//正在加载中
	public static final int LOADING_MORE = 1;
	//没有更多数据了
	public static final int NO_MORE_DATA = 2;

	public CommonFooterHolder(View view) {
		super(view);
	}

	@Override
	public void setData(Footer data, int positoin) {
		super.setData(mData, positoin);
		switch (data.getType()) {
			case PULLUP_LOAD_MORE:
				foot_view_tv.setText("上拉加载更多记忆");
				foot_view_pre.setVisibility(View.VISIBLE);
				break;
			case LOADING_MORE:
				foot_view_tv.setText("正在加载更多记忆");
				foot_view_pre.setVisibility(View.VISIBLE);
			case NO_MORE_DATA:
				foot_view_tv.setText("无更多记忆");
				foot_view_pre.setVisibility(View.GONE);
				break;
		}
	}
}
