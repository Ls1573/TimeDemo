package com.time.memory.view.activity.login;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.time.memory.R;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.PickCountryHolder;
import com.time.memory.view.impl.IPickCountryView;

import java.util.List;

import butterknife.Bind;

/**
 * ==============================
 * 
 * @author Qiu
 * 
 * @Description:选择国家码
 * 
 * @date 2016-9-6上午10:49:50
 * 
 * @version V1.0 ==============================
 */
public class PickCountryActivity extends BaseActivity implements
		IPickCountryView {

	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_pickcountry);
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public void initView() {
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
	}

	@Override
	public void initData() {
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setListAdapter(List<Object> list) {
		if (swipe_target != null)
			swipe_target.setAdapter(new BaseRecyclerAdapter(list,
					R.layout.item_pick_country, PickCountryHolder.class));
	}

}
