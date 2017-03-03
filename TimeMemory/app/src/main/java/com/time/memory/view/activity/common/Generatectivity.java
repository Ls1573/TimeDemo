package com.time.memory.view.activity.common;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.time.memory.R;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:识别生成二维码测试类
 * @date 2016/10/9 9:21
 */
public class Generatectivity extends BaseActivity {
	@Bind(R.id.iv_english)
	ImageView mEnglishIv;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_generate);
	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {
		createEnglishQRCode();
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@OnClick(R.id.btn_orcode)
	public void onClick(View view) {
		if (view.getId() == R.id.btn_orcode) {
			//辨识二维码
			decodeEnglish();
		}
	}

	/**
	 * 创建
	 */
	private void createEnglishQRCode() {
		/**
		 *Activity泄漏的问题?
		 */
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				return QRCodeEncoder.syncEncodeQRCode("qiuyongzhi", BGAQRCodeUtil.dp2px(Generatectivity.this, 150), Color.parseColor("#ff0000"));
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				if (bitmap != null) {
					mEnglishIv.setImageBitmap(bitmap);
				} else {
					showShortToast("生成英文二维码失败");
				}
			}
		}.execute();
	}

	/**
	 * 辨识
	 */
	public void decodeEnglish() {
		mEnglishIv.setDrawingCacheEnabled(true);
		final Bitmap bitmap = mEnglishIv.getDrawingCache();
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				return QRCodeDecoder.syncDecodeQRCode(bitmap);
			}

			@Override
			protected void onPostExecute(String result) {
				if (TextUtils.isEmpty(result)) {
					showShortToast("解析二维码失败");
				} else {
					showShortToast(result);
				}
			}
		}.execute();
	}
}