package com.time.memory.presenter;

import android.text.TextUtils;

import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.model.ImageDownloaderController;
import com.time.memory.model.impl.IImageDownloaderController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IBaseView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:图片管理
 * @date 2016/12/14 9:08
 */
public class PhotoManagerPresenter extends BasePresenter<IBaseView> {
	private static final String TAG = "PhotoManagerPresenter";
	// m层
	private IImageDownloaderController iImageDownloaderController;

	public PhotoManagerPresenter() {
		iImageDownloaderController = new ImageDownloaderController();
	}

	/**
	 * 下载图片
	 */
	public void downLoadImage(String url) {
		if (TextUtils.isEmpty(url)) {
			if (mView != null) {
				mView.showShortToast(context.getString(R.string.data_error));
			}
			return;
		}
		String path = System.currentTimeMillis() + ".jpg";
		CLog.e(TAG, "path:" + url.substring(url.lastIndexOf("/")));
//		if (mView != null) {
//			mView.showLoadingDialog();
//		}
		iImageDownloaderController.downloadImage(context.getApplicationContext(), url, path, new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					if (mView != null) {
						//mView.showFaild();
						mView.showShortToast(context.getString(R.string.net_error));
					}
				} else {
					String message = (String) data;
					if (mView != null) {
						//mView.showSuccess();
						mView.showShortToast(message);
					}
				}
			}

			@Override
			public void onNoNetCallback() {
				if (mView != null) {
					mView.showShortToast(context.getString(R.string.net_no_connection));
					mView.showFaild();
				}
			}
		});
	}

}
