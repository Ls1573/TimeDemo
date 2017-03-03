package com.time.memory.view.activity.common;

import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.time.memory.R;
import com.time.memory.entity.MGroup;
import com.time.memory.presenter.ScanPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.circle.AddFriendActivity;
import com.time.memory.view.activity.circle.FriendActivity;
import com.time.memory.view.activity.memory.FindGroupActivity;
import com.time.memory.view.impl.IScanView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:二维码扫描
 * @date 2016/10/9 8:35
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate, IScanView {

	private static final String TAG = "ScanActivity";
	@Bind(R.id.zxingview)
	QRCodeView mQRCodeView;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_scan);
	}

	@Override
	public void initView() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new ScanPresenter();
	}

	@Override
	public void initData() {
		//设置监听
		mQRCodeView.setDelegate(this);
		//扫描二维码
		mQRCodeView.changeToScanQRCodeStyle();
		//显示扫描框
		mQRCodeView.showScanRect();
		//开始扫描
		mQRCodeView.startSpot();
//		mQRCodeView.openFlashlight();
//		mQRCodeView.closeFlashlight();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
	}

	@Override
	protected void onStop() {
		mQRCodeView.stopCamera();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		mQRCodeView.onDestroy();
		super.onDestroy();
	}

	@Override
	public void onScanQRCodeSuccess(String result) {
		CLog.e(TAG, "扫描结果:" + result);
		vibrate();
//		mQRCodeView.startSpot();
		((ScanPresenter) mPresenter).reqMessage(getString(R.string.FSSEARCH), getString(R.string.FSREQAPPLY), result);
	}

	@Override
	public void onScanQRCodeOpenCameraError() {
		//结果回调
		Log.e(TAG, "打开相机出错");
	}

	@OnClick(R.id.app_cancle)
	public void onClick(View view) {
		finish();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}

	@Override
	public void startGroup(MGroup MGroup) {
		//跳转到Group
		Intent intent = new Intent(mContext, FindGroupActivity.class);
		intent.putExtra("group", MGroup);
		startAnimActivity(intent);
		finish();
	}

	@Override
	public void sacnError() {
		mQRCodeView.startSpot();
	}

	@Override
	public void startFriend(String userId, String uName) {
		//已经是好友
		Intent intent = new Intent(mContext, FriendActivity.class);
		intent.putExtra("userId", userId);
		intent.putExtra("userName", uName);
		CLog.e(TAG, "userId:" + userId + "   userName:" + uName);
		startAnimActivity(intent);
		finish();
	}

	@Override
	public void repleyFriend(String userId, String uName, String hPic) {
		//加好友
		Intent intent = new Intent(mContext, AddFriendActivity.class);
		intent.putExtra("userId", userId);
		intent.putExtra("userName", uName);
		intent.putExtra("hPic", hPic);
		startAnimActivity(intent);
		finish();
	}
}