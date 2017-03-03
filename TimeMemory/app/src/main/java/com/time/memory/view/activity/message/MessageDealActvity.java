package com.time.memory.view.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;


import com.time.memory.core.constant.ReqConstant;

import com.time.memory.entity.Message;
import com.time.memory.presenter.MessageDealPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.circle.FriendApplyActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.MessageDealHolder;
import com.time.memory.view.impl.IMessageDealView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:消息处理
 * @date 2016/10/15 16:07
 */
public class MessageDealActvity extends BaseActivity implements IMessageDealView, AdapterCallback {

	private static final String TAG = "MessageDealActvity";
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	@Bind(R.id.message_deal_hint)
	TextView message_deal_hint;

	private BaseRecyclerAdapter adapter;
	private List<Message> mList;
	private boolean isChange;
	private boolean isReceiver;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_messagedeal);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MessageDealPresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_messagedel), R.drawable.image_back);
	}

	@Override
	public void initData() {
		isReceiver = getIntent().getBooleanExtra("isReceiver", false);
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		if (!isReceiver)
			((MessageDealPresenter) mPresenter).getMessage(MainApplication.getUserId());
	}

	@Override
	public void setAdapter(List<Message> list) {
		if (swipe_target != null) {
			if (adapter == null) {
				mList = list;
				adapter = new BaseRecyclerAdapter(mList, R.layout.item_messagedeal, MessageDealHolder.class);
				adapter.setCallBack(this);
				swipe_target.setAdapter(adapter);
			} else {
				mList.clear();
				mList.addAll(list);
				adapter.notifyDataSetChanged();
			}
			refreshTv();
		}

	}


	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	/**
	 * 刷新页面
	 *
	 * @param position
	 * @param opr
	 */
	@Override
	public void refreshAdapter(int position, int opr) {
		if (opr == -1) {
			//删除
			adapter.remove(position);
		}
		if (opr == 1) {
			//接受好友申请
			adapter.remove(position);
//			mList.get(position).setMsgType("已接受");
//			adapter.notifyItemChanged(position);
		}
		if (opr == 2) {
			//接受进群申请
			adapter.remove(position);
//			mList.get(position).setMsgType("已接受");
//			adapter.notifyItemChanged(position);
		}
		isChange = true;
		refreshTv();
	}

	@Override
	public void onDataCallBack(Object data, int position) {
		int index = (int) data;
//		CLog.e(TAG, "position:" + position + "  index:" + index);
		switch (index) {
			case -1:
				//拒绝
				refuse(position);
				break;
			case 1:
				//接受
				accept(position);
				break;
			case 2:
				//通过
				through(position);
				break;
			case 3:
				//头像
				friend(position);
				break;
		}
	}

	@OnClick(R.id.app_cancle)
	public void onClick(View view) {
		if (view.getId() == R.id.app_cancle)
			setMyResult();
	}

	private void refreshTv() {
		if (mList == null || mList.isEmpty()) {
			message_deal_hint.setVisibility(View.VISIBLE);
			swipe_target.setVisibility(View.GONE);
		}
	}

	/**
	 * 拒绝
	 */
	private void refuse(int position) {
		((MessageDealPresenter) mPresenter).refuse(mList.get(position).getUuid(), position);
	}

	/**
	 * 接受-好友申请
	 */
	private void accept(int position) {
		((MessageDealPresenter) mPresenter).accept(mList.get(position).getSendUserId(), mList.get(position).getUuid(), position);
	}

	/**
	 * 通过
	 */
	private void through(int position) {
		((MessageDealPresenter) mPresenter).through(mList.get(position).getSendUserId(), mList.get(position).getMessageDetail(), position);
	}

	/**
	 * 头像（朋友）
	 */
	private void friend(int position) {
		Intent intent = new Intent(mContext, FriendApplyActivity.class);
		intent.putExtra("userId", mList.get(position).getSendUserId());
		intent.putExtra("msgId", mList.get(position).getUuid());
		intent.putExtra("position", position);
		startActivityForResult(intent, ReqConstant.APPLYFRIEND);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Activity.RESULT_CANCELED) return;
		if (requestCode == resultCode) {
			if (data == null) return;
			if (resultCode == ReqConstant.APPLYFRIEND) {
				//好友申请
				int opr = (int) data.getIntExtra("opr", -1);
				int mPosition = (int) data.getIntExtra("position", -1);
				CLog.e(TAG, "mPosition:" + mPosition + "  opr:" + opr);
				if (opr == 1) {
					//接受
					refreshAdapter(mPosition, 1);
				} else {
					//拒绝
					refreshAdapter(mPosition, -1);
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		setMyResult();
		super.onBackPressed();
	}

	/**
	 * 返回设置
	 */
	private void setMyResult() {
		Intent intent = new Intent();
		intent.putExtra("isChange", isChange);
		setResult(ReqConstant.MESSAGE, intent);
		finish();
	}
}
