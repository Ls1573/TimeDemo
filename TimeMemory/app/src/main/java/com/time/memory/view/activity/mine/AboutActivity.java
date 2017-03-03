package com.time.memory.view.activity.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.gui.MemoryMoreSheet;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.AppUtil;
import com.time.memory.util.DevUtils;
import com.time.memory.util.ShareUtil;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.WebActivity;
import com.time.memory.view.impl.IBaseView;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:关于我们
 * @date 2016/10/26 11:10
 */
public class AboutActivity extends BaseActivity implements IBaseView, EasyPermissions.PermissionCallbacks {

	@Bind(R.id.about_version_tv)
	TextView about_version_tv;//版本号

	private ShareUtil shareUtil;//分享工具类
	private String sharedUrl;//下载链接
	private int shreadPosition;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_about);
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.setting_about), R.drawable.image_back);
	}

	@Override
	public void initData() {
		about_version_tv.setText(AppUtil.getVersionName());
		shareUtil = new ShareUtil();
		sharedUrl = getString(R.string.FSSHAREURL);
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@OnClick({R.id.about_proto_tv, R.id.about_report_tv})
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.about_proto_tv) {
			//用户协议
			Intent intent = new Intent(mContext, WebActivity.class);
			intent.putExtra("url", getString(R.string.FSAGREEMENT));
			intent.putExtra("title", "用户协议");
			startAnimActivity(intent);
		} else if (view.getId() == R.id.about_report_tv) {
			//推荐给好友
			showMoreDialog();
		}
	}


	/**
	 * 更多的弹窗
	 */
	private void showMoreDialog() {
		MemoryMoreSheet.createBuilder(mContext, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				isDelete(false).
				isShared(true).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setListener(new MemoryMoreSheet.onMemoryMoreListener() {
					@Override
					public void onSubmit(int postion) {
						if (postion == 4) {
							shareUtil.sinoShareIntQ(mContext, "我正在使用时光记忆!", "快去下载啊！", R.drawable.headpic, sharedUrl);
						} else if (postion == 3) {
							shreadPosition = 3;
							requestWRITEPermission();
						} else if (postion == 2) {
							shreadPosition = 2;
							requestWRITEPermission();
						} else if (postion == 1) {
							shareUtil.friendsCircleShareIntQ(mContext, "我正在使用时光记忆!", "快去下载啊！！", R.drawable.headpic, sharedUrl);
						} else if (postion == 0) {
							shareUtil.wXShareIntQ(mContext, "我正在使用时光记忆!", "快去下载啊！", R.drawable.headpic, sharedUrl);
						}
					}

					@Override
					public void onCancle() {
					}
				}).show();
	}

	/**
	 * 分享
	 */
	private void sharedQQ() {
		if (shreadPosition == 3) {
			shareUtil.qqSpaceShareIntQ(mContext, "我正在使用时光记忆!！", "快去下载啊！", R.drawable.headpic, sharedUrl);
		} else {
			shareUtil.qqFriendShareIntQ(mContext, "我正在使用时光记忆!！", "快去下载啊！", R.drawable.headpic, sharedUrl);
		}
	}


	/**
	 * 文件读取
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_WRITE)
	protected void requestWRITEPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			sharedQQ();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_external),
					ReqConstant.REQUEST_CODE_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (requestCode == resultCode) {
			if (requestCode == ReqConstant.REQUEST_CODE_WRITE) {
				//更改权限返回
				requestWRITEPermission();
			}
		}
	}


}
