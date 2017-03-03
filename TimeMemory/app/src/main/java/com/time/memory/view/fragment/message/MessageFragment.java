package com.time.memory.view.fragment.message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.callback.IMMessageCallBack;
import com.time.memory.core.callback.IMMsgReadCallBack;
import com.time.memory.core.callback.OnMessageCallBack;
import com.time.memory.core.constant.ImConstant;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.im.IMClientManager;
import com.time.memory.entity.Message;
import com.time.memory.mt.nio.message.response.SM01RespVo;
import com.time.memory.presenter.MessagePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.message.MessageDealActvity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.holder.MessageHolder;
import com.time.memory.view.impl.IMessageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:消息
 * @date 2016-9-9上午9:16:01
 * ==============================
 */
public class MessageFragment extends BaseFragment implements IMessageView, IMMessageCallBack, IMMsgReadCallBack, AdapterCallback {
	private static final String TAG = "MessageFragment";
	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;

	private BaseRecyclerAdapter adapter;
	private ArrayList<Message> msgs;
	private boolean isLoad;
	private OnMessageCallBack onMessageCallBack;
	private int position;

	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container,
							   Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, null);
		return view;
	}

	@Override
	public void initView(View view) {
		initTopBarForOnlyTitle(view, getString(R.string.main_message));
	}

	@Override
	public void initData() {
		msgs = new ArrayList<>();
		//排列方式
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		//数据回调监听
		IMClientManager.getInstance(mContext.getApplicationContext()).getTransDataListener().setImCallBack(this, ImConstant.IMMESSAGE);
	}

	@Override
	public void onDestroy() {
		IMClientManager.getInstance(mContext.getApplicationContext()).getTransDataListener().removeCallback(ImConstant.IMMESSAGE);
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return new MessagePresenter();
	}

	public void setOnMessageCallBack(OnMessageCallBack onMessageCallBack) {
		this.onMessageCallBack = onMessageCallBack;
	}

	@Override
	public void setAdapter(List<Message> list) {
		CLog.e(TAG, "list:" + list.size());
		if (swipe_target != null) {
			if (adapter == null) {
				msgs.addAll(list);
				adapter = new BaseRecyclerAdapter(msgs, R.layout.item_message, MessageHolder.class);
				adapter.setCallBack(this);
				swipe_target.setAdapter(adapter);
			} else {
				msgs.clear();
				msgs.addAll(list);
				adapter.notifyDataSetChanged();
			}
			isLoad = true;
		}
	}

	@Override
	public void refresh(int position) {
		//刷新
		adapter.notifyItemChanged(position);
	}

	@Override
	public void removeAdapter(int position) {
		adapter.remove(position);
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			onMessageCallBack.setMesageTag(View.GONE);
			if (!isLoad) {
				//显示-获取数据
				((MessagePresenter) mPresenter).getMessage(MainApplication.getUserId());
			}
		}
	}

	@Override
	public void onCallback(Object data) {
		//待处理事项
		int position = (int) data;
		if (position == 0) {
			Intent intent = new Intent(mContext, MessageDealActvity.class);
			startActivityForResult(intent, ReqConstant.MESSAGE);
		}
	}

	/**
	 * UDP-申请消息
	 *
	 * @param msg
	 */
	@Override
	public void onGroup(SM01RespVo msg) {
		((MessagePresenter) mPresenter).addMessage(msg, MainApplication.getUserId());
		CLog.e(TAG, "getUserVisibleHint:" + getUserVisibleHint() + "  " + isResumed() + "   " + isHidden() + "   " + isAdded() + "   " + isDetached());
		if (!getUserVisibleHint())
			onMessageCallBack.setMesageTag(View.VISIBLE);
	}

	/**
	 * 长点击
	 *
	 * @param data
	 * @param position
	 */
	@Override
	public void onLongCallBack(Object data, int position) {
		if (position != 0) {
			//可以删除
			reqDialog(position);
		}
	}

	@Override
	public void onRead(int code) {
		//已读信息验证
	}


	/**
	 * 删除信息
	 *
	 * @param position
	 */
	private void reqDialog(final int position) {
		DialogUtils.request(getActivity(), "确定删除此信息吗?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isTure = (boolean) data;
				if (isTure) {
					//删除
					((MessagePresenter) mPresenter).removeMessage(msgs.get(position), position);
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (resultCode == ReqConstant.MESSAGE) {
			boolean isChange = data.getBooleanExtra("isChange", false);
			((MessagePresenter) mPresenter).getMessage(MainApplication.getUserId(), isChange);
		}
	}
}
