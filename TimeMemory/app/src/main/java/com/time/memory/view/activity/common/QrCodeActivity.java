package com.time.memory.view.activity.common;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import java.util.concurrent.ExecutorService;

import butterknife.Bind;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:二维码
 * @date 2016/12/13 14:15
 */
public class QrCodeActivity extends BaseActivity implements IBaseView {
	private static final int SUCCESS = 1;// 成功
	private static final int FINAL = -1;// 失败
	private static final String MESSAGE = "data_msg";

	@Bind(R.id.qrcode_iv)
	ImageView qrcode_iv;
	@Bind(R.id.qrcode_tv)
	TextView qrcode_tv;

	private String code;
	private String sign;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_qr);
	}

	@Override
	protected void onDestroy() {
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
		super.onDestroy();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.app_qrcode), R.drawable.image_back);
	}

	@Override
	public void initData() {
		//我的二维码-userId
		code = getIntent().getStringExtra("qrcode");
		sign = getIntent().getStringExtra("sign");
		createQrcode();
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	// 放入到主线程
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SUCCESS) {
				Bitmap bitmap = (Bitmap) msg.obj;
				qrcode_iv.setImageBitmap(bitmap);
				qrcode_tv.setText(sign);

				qrcode_iv.setVisibility(View.VISIBLE);
				qrcode_tv.setVisibility(View.VISIBLE);
			} else {
				//出异常了
			}
		}
	};

	/**
	 * 创建QrCode
	 */
	private void createQrcode() {
		if (TextUtils.isEmpty(code)) return;
		ExecutorService threadPool = ExecutorManager.getInstance();
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				try {
					BitmapDrawable bd = (BitmapDrawable) MainApplication.getContext().getResources().getDrawable(R.drawable.headpic);
					Bitmap logo = bd.getBitmap();
					Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(code, BGAQRCodeUtil.dp2px(MainApplication.getContext(), 220), R.color.black_01, logo);
					handler.sendMessage(handler.obtainMessage(SUCCESS, bitmap));
				} catch (Exception e) {
					handler.sendMessage((handler.obtainMessage(FINAL, null)));
				}

			}
		});
	}
}
