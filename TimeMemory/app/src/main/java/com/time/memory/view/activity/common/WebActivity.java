package com.time.memory.view.activity.common;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.time.memory.R;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;


/**
 * @author Qiu
 * @version V1.0
 * @Description:共通H5
 * @date 2016/9/20 8:56
 */
public class WebActivity extends BaseActivity implements IBaseView {

	LinearLayout web_ll;

	WebView webView;

	private String url;
	private String title;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_web);
		web_ll = (LinearLayout) findViewById(R.id.web_ll);
		webView = new WebView(mContext);
		webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		web_ll.addView(webView);
	}

	@Override
	public void initView() {
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		initTopBarForLeft(title, R.drawable.image_back);
	}

	@Override
	public void initData() {
		//url = getIntent().getStringExtra("url");
		if (!TextUtils.isEmpty(url)) {
			webView.loadUrl(url);
			initWebview(webView);
		}
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public void onPause() {
		webView.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		webView.removeAllViews();
		webView.destroy();
		webView = null;
		super.onDestroy();
	}

	/**
	 * 设置webView参数
	 *
	 * @param webView
	 */
	private void initWebview(WebView webView) {
		try {
			// 代码加载
			WebSettings webSettings = webView.getSettings();
			// 编码支持
			webSettings.setDefaultTextEncodingName("UTF-8");
			// 设置WebView属性，能够执行Javascript脚本
			webSettings.setJavaScriptEnabled(true);
			// 设置可以访问文件
			webSettings.setAllowFileAccess(true);
			// 设置支持缩放
			webSettings.setSupportZoom(false);
			webSettings.setBuiltInZoomControls(false);
			// 滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
			webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
			// 用于处理webView的控制问题，如加载、关闭、错误处理等
			webView.setWebViewClient(new MwebViewClient());
			// 处理js对话框、图标、页面标题
			webView.setWebChromeClient(new MwebchromeClient());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用于处理webView的控制问题，如加载、关闭、错误处理等
	 *
	 * @author Qiu
	 */
	class MwebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
									String description, String failingUrl) {
			// 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
			String url = "file:///android_asset/error.html";
			view.loadUrl(url);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			// showLoadingDialog(getString(R.string.loading));
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// hideLoadingDialog();
		}
	}

	/**
	 * 处理js对话框、图标、页面标题
	 *
	 * @author Qiu
	 */
	class MwebchromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
								 JsResult result) {
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public void onProgressChanged(WebView view, int progress) {
			CLog.d("MwebchromeClient", "progress:" + progress);
			if (progress < 100) {
				showMyDialog();
			} else {
				hideLoadingDialog();
			}
			super.onProgressChanged(view, progress);
		}
	}
}
