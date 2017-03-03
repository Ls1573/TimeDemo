package com.time.memory.view.activity.mine;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.Address;
import com.time.memory.gui.DividerItemDecoration;
import com.time.memory.presenter.CityPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.CityHolder;
import com.time.memory.view.impl.ICityView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author Qiu
 * @version V1.0
 * @Description:地址-市
 * @date 2016/12/1 10:44
 */
public class CityActivity extends BaseActivity implements ICityView, AdapterCallback {

	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private Address address;//当前的省份
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
	public void initData() {
		address = getIntent().getParcelableExtra("address");

		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
		swipeTarget.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL, ContextCompat.getColor(mContext, R.color.grey_divider), 1));

		((CityPresenter) mPresenter).getProvinces(address.getId());
	}

	@Override
	public BasePresenter initPresenter() {
		return new CityPresenter();
	}

	@Override
	public void setAdapter(ArrayList<Address> addresses) {
		if (swipeTarget != null) {
			if (adapter == null) {
				adapter = new BaseRecyclerAdapter(addresses, R.layout.item_address, CityHolder.class);
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
		//当前城市
		Address city = (Address) data;
		setMyResylt(city);
	}

	private void setMyResylt(Address city) {
		Intent intent = new Intent(mContext, MineActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("city", city);
		intent.putExtra("province", address);
		startAnimActivity(intent);
//		ActivityTaskManager.getInstance().removeActivity("ProvinceActivity");
		finish();
	}
}
