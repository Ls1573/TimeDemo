package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.ActionSheet;
import com.time.memory.gui.ActionSheet.ActionSheetListener;
import com.time.memory.gui.MyImageView;
import com.time.memory.gui.crop.Crop;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.TakePhotoUtils;
import com.time.memory.view.activity.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;
import cn.finalteam.toolsfinal.io.FilenameUtils;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:我的选择图片
 * @date 2016-9-5下午4:26:43
 * ==============================
 */
public class PhotoActivity extends BaseActivity {

	private static final String TAG = "PhotoActivity";
	private final int REQUEST_CODE_CAMERA = 1000;
	private final int REQUEST_CODE_GALLERY = 1001;
	private final int TAKE_REQUEST_CODE = 101;

	private List<PhotoInfo> mPhotoList;//图片集合
	private PhotoInfo photoInfo;//当前图片
	private boolean isCrop = false;//有剪裁图片

	@Bind(R.id.photo_iv)
	MyImageView photo_iv;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_photo);
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.mine_info), R.drawable.image_back, "", R.drawable.image_more);
	}

	@Override
	public void initData() {
		String netPath = getIntent().getStringExtra("netPath");
		String locPath = getIntent().getStringExtra("locPath");
		mPhotoList = new ArrayList<>();
		initImage(netPath, locPath);
	}

	/**
	 * 设置图片
	 *
	 * @param netPath
	 * @param locPath
	 */
	private void initImage(String netPath, String locPath) {
//		if (!TextUtils.isEmpty(locPath)) {
//			photo_iv.setImageURI(Uri.parse(locPath));
//		} else
		if (!TextUtils.isEmpty(netPath)) {
			if (netPath.indexOf("http") != -1)
				Picasso.with(mContext).load(netPath).error(R.drawable.forward_check).placeholder(R.drawable.photobg).into(photo_iv);
			else
				Picasso.with(mContext).load(getString(R.string.FSIMAGEPATH) + netPath).placeholder(R.drawable.photobg).error(R.drawable.forward_check).into(photo_iv);
		} else {
			photo_iv.setImageResource(R.drawable.logom);
		}
	}

	@OnClick(R.id.iv_main_right)
	public void onClick(View view) {
		if (R.id.iv_main_right == view.getId()) {
			// 显示更多
			oprPhoto();
		} else if (R.id.app_cancle == view.getId()) {
			//回退
			setMyResult(ReqConstant.UPHEADER);
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
	public void onBackPressed() {
		setMyResult(Activity.RESULT_CANCELED);
		super.onBackPressed();
	}

	/**
	 * 选择操作
	 */
	private void oprPhoto() {
		if (!isCrop) {
			//没有本地图片
			showPicDialog("拍照", "从手机相册选择");
		} else {
			//选择了一张图片
			showPicDialog("拍照", "从手机相册选择");
		}
	}

	/**
	 * 返回
	 *
	 * @param resultCode
	 */
	private void setMyResult(int resultCode) {
		Intent intent = new Intent();
		intent.putExtra("photoInfo", photoInfo);
		setResult(resultCode, intent);
		finish();
	}

	/**
	 * 弹出窗口选择图片
	 *
	 * @param titles
	 */
	private void showPicDialog(String... titles) {
		mPhotoList.clear();
		// dialog弹窗
		ActionSheet
				.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles(titles).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext))
				.setCancelableOnTouchOutside(true)
				.setListener(new ActionSheetListener() {
					// 选择
					@Override
					public void onDismiss(ActionSheet actionSheet,
										  boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet,
												   int index) {
						switch (index) {
							case 0:
								// 拍照
								GalleryFinal.openCamera(REQUEST_CODE_CAMERA, initPhoto(1, true, false, false, -1, null, mPhotoList), mOnHanlderResultCallback);
								break;
							case 1:
								// 从手机相册选择
								GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(1, true, false, false, -1, null, mPhotoList), mOnHanlderResultCallback);
								break;
						}
					}
				}).show();
	}


	//图片回调
	private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
		@Override
		public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
			if (resultList != null) {
				mPhotoList.clear();
				mPhotoList.addAll(resultList);
				if (mPhotoList.size() > 0)
					cropImage();
			}
		}

		@Override
		public void onHanlderFailure(int requestCode, String errorMsg) {
			showShortToast(errorMsg);
		}
	};

	/**
	 * 剪裁配置
	 */
	private void cropImage() {
		photoInfo = mPhotoList.get(0);
		String ext = FilenameUtils.getExtension(photoInfo.getPhotoPath());
		if (StringUtils.isEmpty(ext) || !(ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg"))) {
			showShortToast(getString(R.string.edit_letoff_photo_format));
		} else {
			beginCrop(TakePhotoUtils.FilePathToUri(mContext, mPhotoList.get(0).getPhotoPath()));
		}
	}

	/**
	 * 开始剪切
	 *
	 * @param source
	 */
	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(), UUID.randomUUID().toString()));
		Crop.of(source, destination).asSquare().start(this);
	}

	private void handleCrop(int resultCode, Intent result) {
		try {
			if (resultCode == RESULT_OK) {
				Uri curUri = Crop.getOutput(result);
				Picasso.with(mContext).load(curUri).error(R.drawable.forward_check).placeholder(R.drawable.photobg).into(photo_iv);
				photoInfo.setPhotoPath(curUri.toString());
			} else if (resultCode == Crop.RESULT_ERROR) {
				showShortToast("剪切错误!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent result) {
		if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
			beginCrop(result.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			CLog.e(TAG, "REQUEST_CROP:" + Crop.REQUEST_CROP);
			handleCrop(resultCode, result);
		}
	}
}
