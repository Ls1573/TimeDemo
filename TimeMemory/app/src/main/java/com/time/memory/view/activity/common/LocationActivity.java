package com.time.memory.view.activity.common;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.Location;
import com.time.memory.model.LocationController;
import com.time.memory.presenter.LocationPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.LocUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.LocationHolder;
import com.time.memory.view.impl.ILocationView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:定位+范围搜索
 * @date 2016/11/15 12:03
 */
public class LocationActivity extends BaseActivity implements ILocationView, AMapLocationListener, PoiSearch.OnPoiSearchListener, EasyPermissions.PermissionCallbacks, AdapterCallback {
	private static final String TAG = "LocationActivity";
	@Bind(R.id.location_tv)
	EditText locationTv;
	@Bind(R.id.swipe_target)
	RecyclerView swipeTarget;

	private LocationController locationController;
	private PoiSearch.Query query;
	private PoiSearch poiSearch;


	private BaseRecyclerAdapter mAdapter;
	private Location mLocation;
	private List<Location> mLocations;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_location);
	}

	@Override
	protected void onDestroy() {
		// 销毁定位
		if (locationController != null)
			locationController.onDestroy();
		if (mHandler != null)
			mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_location), R.drawable.image_back);
	}

	@Override
	public void initData() {
		swipeTarget.setLayoutManager(new LinearLayoutManager(mContext));
		((DefaultItemAnimator) swipeTarget.getItemAnimator()).setSupportsChangeAnimations(false);
		//定位
		requestLocationPermission();

		((LocationPresenter) mPresenter).getLocatoin(mLocation);
	}

	@Override
	public BasePresenter initPresenter() {
		return new LocationPresenter();
	}

	@OnClick(R.id.app_submit)
	public void onClick(View view) {
		if (view.getId() == R.id.app_submit) {
			//提交
			setAddress();
		} else if (view.getId() == R.id.app_cancle) {
			setMyResult(RESULT_CANCELED, "");
		}
	}

	@Override
	public void setAdapter(List<Location> locations) {
		if (swipeTarget != null) {
			if (mAdapter == null) {
				mLocations = locations;
				mAdapter = new BaseRecyclerAdapter(mLocations, R.layout.item_location, LocationHolder.class);
				mAdapter.setCallBack(this);
				swipeTarget.setAdapter(mAdapter);
			} else {
				mLocations.addAll(locations);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onBackPressed() {
		setMyResult(RESULT_CANCELED, "");
	}

	/**
	 * 定位回调
	 */
	@Override
	public void onLocationChanged(AMapLocation loc) {
		if (null != loc) {
			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = LocUtils.MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * POI 检索
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					((LocationPresenter) mPresenter).addLocation(result.getPois(), mLocation);
				}
			} else {
				CLog.e(TAG, "poi检索为空");
			}
		} else {
			CLog.e(TAG, "errorCode:" + rCode);
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem result, int rCode) {

	}

	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		Location location = mLocations.get(position);
		if (position == 0) {
			//不显示地址
			setMyResult(ReqConstant.REQUEST_CODE_ADDRESS, "");
		} else if (position == 1) {
			//大连
			setMyResult(ReqConstant.REQUEST_CODE_ADDRESS, location.getCity());
		} else {
			//其他poi
			setMyResult(ReqConstant.REQUEST_CODE_ADDRESS, location.getPoiName());
		}

	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {

	}

	@Override
	public void onDataCallBack(Object data, int position) {

	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 请求数据
			switch (msg.what) {
				case LocUtils.MSG_LOCATION_START:
					//定位开始
					CLog.e(TAG, "定位开始---------------------><");
					break;
				case LocUtils.MSG_LOCATION_FINISH:
					// 定位完成
					AMapLocation loc = (AMapLocation) msg.obj;
					mLocation = LocUtils.getLocationStr(loc);
					//加入
//					((LocationPresenter) mPresenter).getLocatoin(mLocation);
					//Poi检索
					setPoi("");
					//获取poi信息
					break;
				case LocUtils.MSG_LOCATION_STOP:
					//定位停止
					CLog.e(TAG, "定位停止---------------------><");
					break;
				default:
					break;
			}
		}
	};

	/**
	 * 拍照获取权限
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_LOCATION)
	protected void requestLocationPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
			locationController = LocationController.getInstance();
			locationController.initLocation(this);
			locationController.startLoc();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_location),
					ReqConstant.REQUEST_CODE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
		requestLocationPermission();
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝,再次申请
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (requestCode == ReqConstant.REQUEST_CODE_LOCATION) {
			requestLocationPermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	/**
	 * 设置poi信息
	 * , String cityCode, double latitude, double longitude
	 */
	private void setPoi(String keyWord) {
		query = new PoiSearch.Query(keyWord, "商务住宅", mLocation.getCityCode());
		//keyWord表示搜索字符串，
		//第二个参数表示POI搜索类型，二者选填其一，
		//POI搜索类型共分为以下20种：汽车服务|汽车销售|
		//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索

		query.setPageSize(20);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);//设置查询页码
		poiSearch = new PoiSearch(this, query);
		//设置周边搜索的中心点以及半径
		poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mLocation.getLatitude(), mLocation.getLongitude()), 1000));
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	/**
	 * 返回
	 */
	private void setAddress() {
		String address = locationTv.getText().toString();
		if (TextUtils.isEmpty(address)) {
			showShortToast("请输入地址");
			return;
		}
		setMyResult(ReqConstant.REQUEST_CODE_ADDRESS, address);
	}

	/**
	 * 设置返回
	 *
	 * @param resultCode
	 * @param address
	 */
	public void setMyResult(int resultCode, String address) {
		Intent intent = new Intent();
		intent.putExtra("address", address);
		setResult(resultCode, intent);
		finish();
	}

}
