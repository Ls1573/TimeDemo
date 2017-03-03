package com.time.memory.view.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Location;

import butterknife.Bind;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:广告
 * @date 2016/11/14 10:44
 */
public class LocationHolder extends BaseHolder<Location> {
	private static final String TAG = "LocationHolder";

	@Bind(R.id.location_desc_tv)
	TextView location_desc_tv;//描述(Poi)
	@Bind(R.id.location_city_tv)
	TextView location_city_tv;//城市
	@Bind(R.id.location_address_tv)
	TextView location_address_tv;//地址

	private int mPosition;

	public LocationHolder(View view) {
		super(view);
	}

	@Override
	public void init() {
		mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(mPosition);
			}
		});
	}

	@Override
	public void setData(final Location entity, final int position) {
		super.setData(entity);
		this.mPosition = position;
		if (!TextUtils.isEmpty(entity.getCity())) {
			location_city_tv.setText(entity.getCity());

			location_city_tv.setVisibility(View.VISIBLE);
			location_address_tv.setVisibility(View.GONE);
			location_desc_tv.setVisibility(View.GONE);
		} else {
			location_desc_tv.setText(entity.getPoiName());
			location_address_tv.setText(entity.getAddress());

			location_city_tv.setVisibility(View.GONE);
			location_address_tv.setVisibility(View.VISIBLE);
			location_desc_tv.setVisibility(View.VISIBLE);
		}
	}
}
