package com.time.memory.view.holder;

import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Address;

import butterknife.Bind;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:城市管理
 * @date 2016/12/1 11:18
 */
public class CityHolder extends BaseHolder<Address> {
	@Bind(R.id.add_address_tv)
	TextView addressTv;

	public CityHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
	}

	@Override
	public void setData(final Address entity, final int position) {
		super.setData(entity);
		addressTv.setText(entity.getName());

		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(entity);
			}
		});

	}
}
