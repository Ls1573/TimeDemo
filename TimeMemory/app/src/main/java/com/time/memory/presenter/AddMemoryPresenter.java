package com.time.memory.presenter;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.WriterMemory;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.NetUtils;
import com.time.memory.view.impl.IAddMemoryView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:补充记忆
 * @date 2016-9-7上午9:10:07
 * ==============================
 */
public class AddMemoryPresenter extends BasePresenter<IAddMemoryView> {
	// m层
	private ExecutorService executorManager;

	public AddMemoryPresenter() {
		executorManager = ExecutorManager.getInstance();
	}

	@Override
	public void detachView() {
		super.detachView();
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
	}

	/**
	 * 日期截取
	 *
	 * @reurn
	 */
	public String subStringDate(String date) {
		return date.substring(0, 10);
	}

	/**
	 * 日期转换
	 *
	 * @reurn
	 */
	public void convertItemDate(String date) {
		String[] curDates = date.split("\\.");
		int year = Integer.parseInt(curDates[0]);
		int month = Integer.parseInt(curDates[1]);
		int day = Integer.parseInt(curDates[2]);
		if (mView != null)
			mView.showDatePicker(year, month, day);
	}

	/**
	 * 新增图片
	 *
	 * @param photoList
	 */
	public void addPic(WriterMemory memory, ArrayList<PhotoInfo> photoList) {
		//TODO
		memory.getPictureEntits().clear();
		memory.getPictureEntits().addAll(photoList);
		// TODO
		if (mView != null) {
			mView.setMemory(memory);
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				WriterMemory entity = msg.getData().getParcelable("writerMemory");
				setMessage(entity);
			}
		}
	};


	/**
	 * 回调给UI
	 *
	 * @param writerStyleMemory
	 */
	private void setMessage(WriterMemory writerStyleMemory) {
		if (mView != null) {
			mView.setMemory(writerStyleMemory);
			mView.showSuccess();
		}
	}

	/**
	 * 图片转换
	 *
	 * @param photoList
	 */
	public void getMessage(final ArrayList<PhotoInfo> photoList) {
		if (mView != null)
			mView.showLoadingDialog();
		executorManager.submit(new Runnable() {
			@Override
			public void run() {
				try {
					WriterMemory writerMemory = convertPhotos(photoList);
					//获取地址
					getAddress(writerMemory);
					//放到Handler里
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.getData().putParcelable("writerMemory", writerMemory);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 获取地址
	 *
	 * @param memory
	 */
	private void getAddress(WriterMemory memory) {
		if (!NetUtils.isNetworkAvailable(context)) return;
		//构造 GeocodeSearch 对象
		GeocodeSearch geocoderSearch = new GeocodeSearch(context);
		//需查询对象
		//有经纬度
		if (memory.getLatitude() != 0d && memory.getLongitude() != 0d) {
			// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
			RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(memory.getLatitude(), memory.getLongitude()), 200, GeocodeSearch.AMAP);
			try {
				RegeocodeAddress address = geocoderSearch.getFromLocation(query);
				String addressName = address.getCity() + address.getDistrict();
//					CLog.e(TAG, "addressName:" + addressName);
				if (!TextUtils.isEmpty(addressName))
					memory.setAddress(addressName);
			} catch (AMapException e) {
				memory.setAddress("");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 装换
	 */
	private WriterMemory convertPhotos(ArrayList<PhotoInfo> photoList) {
		WriterMemory writerMemory = new WriterMemory();
		writerMemory.setPictureEntits(photoList);
		writerMemory.setDate(photoList.get(0).getDate());
		writerMemory.setLatitude(photoList.get(0).getLatitude());
		writerMemory.setLongitude(photoList.get(0).getLongitude());
		return writerMemory;
	}
}
