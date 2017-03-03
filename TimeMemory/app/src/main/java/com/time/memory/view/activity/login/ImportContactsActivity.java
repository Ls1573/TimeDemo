package com.time.memory.view.activity.login;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.MGroup;
import com.time.memory.presenter.ImportPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IImportView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:导入通讯录(引导)
 * @date 2016/10/15 16:50
 */
public class ImportContactsActivity extends BaseActivity implements IImportView, EasyPermissions.PermissionCallbacks {

	private static final String TAG = "ImportContactsActivity";

	private static final int PERMISSIONS_CODE_CONSTACTS = 101;//联系人
	private static final int RC_SETTINGS_SCREEN = 102;//去设置页面了


	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_importcontacts);
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new ImportPresenter();
	}


	@OnClick({R.id.app_cancle, R.id.app_submit, R.id.login_reset})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.app_cancle:
				//退出
				doLoginout();
				break;
			case R.id.app_submit:
				//导入通讯录操作
				requestConstacts();
				break;
			case R.id.login_reset:
				//切换账号
				exitLogin();
				break;
		}
	}

	/**
	 * 退出
	 */
	private void doLoginout() {
		// 退出释放内存
//		System.exit(0);
//		System.gc();
//		android.os.Process.killProcess(android.os.Process.myPid());
		finish();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		startAnimActivity(MainActivity.class);
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void importCircle(ArrayList<MGroup> unGroups, int friends) {
		//引导激活圈子页面
		hideLoadingDialog();
		Intent intent = new Intent(mContext, ImportCircleActivity.class);
		intent.putParcelableArrayListExtra("groups", unGroups);
		intent.putExtra("friends", friends);
		startAnimActivity(intent);
		finish();
	}

	/**
	 * 获取联系人
	 */
	@AfterPermissionGranted(PERMISSIONS_CODE_CONSTACTS)
	private void requestConstacts() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_CONTACTS)) {
			//有权限
			((ImportPresenter) mPresenter).upConstacts(MainApplication.getUserId());
		} else {
			// 请求一个权限
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tips_constacts),
					PERMISSIONS_CODE_CONSTACTS, Manifest.permission.READ_CONTACTS);
		}
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
//		((ImportPresenter) mPresenter).upConstacts(MainApplication.getUserId());
//		requestConstacts();
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝,再次申请
//		requestPermissionAgain();
		System.gc();
		android.os.Process.killProcess(android.os.Process.myPid());
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (requestCode == RC_SETTINGS_SCREEN) {
			requestConstacts();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}


	/**
	 * 退出登录
	 */
	private void exitLogin() {
		DialogUtils.request(ImportContactsActivity.this, "确定更换账号吗?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isSure = (boolean) data;
				if (isSure) {
					doLogout();
				}
			}
		});
	}

	/**
	 * 发出退出登陆请求包（Android系统要求必须要在独立的线程中发送）
	 */
	private void doLogout() {
		showLoadingDialog();
		((ImportPresenter) mPresenter).loginOut(MainApplication.getUserId());
		System.gc();
		Intent intent = new Intent(mContext, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startAnimActivity(intent);
	}
}
