package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.MGroup;
import com.time.memory.presenter.SearchPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.SearchGroupHolder;
import com.time.memory.view.impl.ISearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:群搜索
 * @date 2016/10/19 10:38
 */
public class SearchGroupActivity extends BaseActivity implements TextWatcher, ISearchView, AdapterCallback {
	private static final String TAG = "SearchGroupActivity";

	@Bind(R.id.search_et)
	EditText search_et;//搜索输入框
	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private List<MGroup> mMGroupInfoVoList;
	private BaseRecyclerAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_search_group);
	}

	@Override
	public BasePresenter initPresenter() {
		return new SearchPresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.search), R.drawable.image_back);
	}

	@Override
	public void initData() {
		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
		//监听
		search_et.addTextChangedListener(this);
		mMGroupInfoVoList = new ArrayList<>();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		//输入之后的操作
		String keyWord = s.toString();
		if (TextUtils.isEmpty(keyWord)) {
			//空
		} else {
			((SearchPresenter) mPresenter).reqGetGroup(getString(R.string.FSSEARCH), keyWord);
		}
	}

	@OnClick(R.id.search_canel_tv)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.search_canel_tv) {
			//取消-清空操作
			search_et.setText("");
			mMGroupInfoVoList.clear();
			if (adapter != null)
				adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void showSuccess() {
	}

	@Override
	public void showFaild() {
	}

	@Override
	public void setAdapter(List<MGroup> groupInfoVoList) {
		if (swipeTarget != null) {
			if (adapter == null) {
				this.mMGroupInfoVoList = groupInfoVoList;
				adapter = new BaseRecyclerAdapter(mMGroupInfoVoList, R.layout.item_group_, SearchGroupHolder.class);
				adapter.setCallBack(this);
				swipeTarget.setAdapter(adapter);
			} else {
				mMGroupInfoVoList.clear();
				mMGroupInfoVoList.addAll(groupInfoVoList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void startActivity(MGroup mGroup, Class cla) {
		Intent intent = new Intent(mContext, cla);
		intent.putExtra("group", mGroup);
		startAnimActivity(intent);
	}

	@Override
	public void onDataCallBack(Object data, int position) {
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
	}

	@Override
	public void onCallback(Object data) {
		//判断
		int position = (int) data;
		MGroup group = mMGroupInfoVoList.get(position);
		((SearchPresenter) mPresenter).startActivity(group);
	}
}
