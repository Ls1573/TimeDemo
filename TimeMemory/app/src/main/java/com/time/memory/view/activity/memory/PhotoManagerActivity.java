package com.time.memory.view.activity.memory;

import android.Manifest;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.Callback;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.gallery.adapter.PhotoPreviewAdapter;
import com.time.memory.gui.gallery.widget.GFViewPager;
import com.time.memory.presenter.PhotoManagerPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:图片管理
 * @date 2016/12/13 17:38
 */
public class PhotoManagerActivity extends BaseActivity implements IBaseView, Callback, ViewPager.OnPageChangeListener, EasyPermissions.PermissionCallbacks {

	private static final String TAG = "PhotoManagerActivity";

	public static final String PHOTO_LIST = "photo_list";
	public static final int PHOTO_CODE = 101;

	@Bind(R.id.vp_pager)
	GFViewPager mVpPager;//内容显示区域
	@Bind(R.id.app_download)
	ImageView app_download;//下载
	@Bind(R.id.photo_tv)
	TextView photoTv;//指示头

	private ArrayList<PhotoInfo> mPhotoList;
	private PhotoPreviewAdapter mPhotoPreviewAdapter;

	private int position;//当前位置
	private int curClick;//当前点击位置

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_photomanager);
	}

	@Override
	public BasePresenter initPresenter() {
		return new PhotoManagerPresenter();
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
		position = curClick = getIntent().getIntExtra("curClick", 0);
		mPhotoList = getIntent().getParcelableArrayListExtra(PHOTO_LIST);
		mVpPager.addOnPageChangeListener(this);
		getOpr();
	}

	/**
	 * 观看模式
	 */
	private void getOpr() {
		mPhotoPreviewAdapter = new PhotoPreviewAdapter(PhotoManagerActivity.this, mPhotoList, true);
		mPhotoPreviewAdapter.setCallback(this);
		mVpPager.setAdapter(mPhotoPreviewAdapter);
		mVpPager.setCurrentItem(curClick);
		mVpPager.setOffscreenPageLimit(1);
		setMyTitle(position);
	}

	/**
	 * 设置指示头
	 */
	private void setMyTitle(int position) {
		mPhotoList.get(position);
		photoTv.setText(mPhotoList.get(position).getCurPoint() + " - " + mPhotoList.get(position).getCurPointIndex() + "/" + mPhotoList.get(position).getTotalCount());
	}

	@Override
	public void onPageSelected(int position) {
		this.position = position;
		setMyTitle(position);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@OnClick({R.id.app_download, R.id.app_cancle})
	public void onClick(View view) {
		int Id = view.getId();
		if (Id == R.id.app_cancle) {
			finish();
		} else if (Id == R.id.app_download) {
			//下载请求
			requestPhotoPermission();
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void onCallback(Object data) {
		boolean isFinish = (boolean) data;
		if (isFinish)
			finish();
	}

	/**
	 * 图片下载(读存储卡得权限)
	 */
	@AfterPermissionGranted(PHOTO_CODE)
	protected void requestPhotoPermission() {
		if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			((PhotoManagerPresenter) mPresenter).downLoadImage(mPhotoList.get(position).getPhotoPath());
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_writer),
					PHOTO_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝,再次申请
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (requestCode == PHOTO_CODE) {
			requestPhotoPermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
}
