package com.time.memory.view.activity.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.time.memory.mt.vo.MessageVo;
import com.time.memory.R;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.circle.FriendActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.MessageInfoHolder;
import com.time.memory.view.impl.IMessageInfoView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:消息内容
 * @date 2016/9/13 8:59
 */
public class MessageInfoActvity extends BaseActivity implements IMessageInfoView {
	private static final String TAG = "MessageInfoActvity";
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	@Bind(R.id.tv_main_title)
	TextView tv_main_title;//题头

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_messageinfo);
	}

	@Override
	public void initView() {
		tv_main_title.setText("Qoo");
	}

	@Override
	public void initData() {
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@OnClick({R.id.app_call, R.id.message_mine, R.id.app_cancle})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_cancle:
				finish();
				break;
			case R.id.app_call:
				//TODO 打电话(电话传入)
				showShortToast("打电话？？");
				break;
			case R.id.message_mine:
				//TODO 跳到个人页?
				showShortToast("跳到个人页??");
				startAnimActivity(FriendActivity.class);
				break;
		}
	}

	@Override
	public void setAdapter(List<MessageVo> msgs) {
		CLog.e(TAG, "msgs:" + msgs.size());
		if (swipe_target != null)
			swipe_target.setAdapter(new BaseRecyclerAdapter(msgs, R.layout.item_message_info, MessageInfoHolder.class));
	}

}
