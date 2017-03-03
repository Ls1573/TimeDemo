package com.time.memory.presenter;

import com.amap.api.services.core.PoiItem;
import com.time.memory.entity.Location;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.impl.ILocationView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:定位
 * @date 2016/11/15 15:04
 */
public class LocationPresenter extends BasePresenter<ILocationView> {
	private static final String TAG = "LocationPresenter";

	public LocationPresenter() {
	}

	/**
	 * 添加地址
	 * n:大连市
	 * o:甘井子区
	 * m:辽宁省
	 * mTitle:纳米大厦
	 * mSnippet:火炬路3号
	 *
	 * @param poiItems
	 */
	public void addLocation(List<PoiItem> poiItems, Location mLocation) {
		if (poiItems == null)
			return;
		List<Location> locations = new ArrayList<>();
		locations.add(mLocation);
		Location location;
		for (PoiItem poiItem : poiItems) {
			location = new Location();
			location.setAddress(poiItem.getAdName() + poiItem.getSnippet());
			location.setPoiName(poiItem.getTitle());
			locations.add(location);
		}
		if (mView != null) {
			mView.setAdapter(locations);
		}
	}


	/**
	 * 获取地址
	 * state类型(1:定位完成;2:停止定位;3:开始定位)
	 *
	 * @return
	 */
	public void getLocatoin(Location location) {
		List<Location> locations = new ArrayList<>();
		if (location == null) {
			//默认
			locations.add(getLocFalied());
		} else {
			if (location.getState() == 1) {
				//成功
				locations.add(location);
			}
		}
		if (mView != null) {
			mView.setAdapter(locations);
		}
	}

	/**
	 * 定位失败
	 *
	 * @return
	 */
	private Location getLocFalied() {
		Location myLoc = new Location();
		myLoc.setCity("不显示位置");
		myLoc.setAddress("");
		return myLoc;
	}
}
