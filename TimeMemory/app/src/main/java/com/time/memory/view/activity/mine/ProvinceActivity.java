package com.time.memory.view.activity.mine;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.Address;
import com.time.memory.gui.DividerItemDecoration;
import com.time.memory.presenter.ProvincePresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.ProvinceHolder;
import com.time.memory.view.impl.IProvinceView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:地址-省
 * @date 2016/12/1 10:45
 */
public class ProvinceActivity extends BaseActivity implements IProvinceView, AdapterCallback {
	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private BaseRecyclerAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_address);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_editaddress), R.drawable.image_back);
	}

	@Override
	public BasePresenter initPresenter() {
		return new ProvincePresenter();
	}

	@Override
	public void initData() {
		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
		swipeTarget.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, ContextCompat.getColor(mContext, R.color.grey_divider), 1));

		((ProvincePresenter) mPresenter).getProvinces();
	}

	@Override
	public void setAdapter(ArrayList<Address> addresses) {
		if (swipeTarget != null) {
			if (adapter == null) {
				adapter = new BaseRecyclerAdapter(addresses, R.layout.item_address, ProvinceHolder.class);
				adapter.setCallBack(this);
				swipeTarget.setAdapter(adapter);
			}
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void onCallback(Object data) {
		Address address = (Address) data;
		Intent intent = new Intent(mContext, CityActivity.class);
		intent.putExtra("address", address);
		startAnimActivity(intent);
//		startActivityForResult(intent, ReqConstant.ADDRESS);
	}
}
