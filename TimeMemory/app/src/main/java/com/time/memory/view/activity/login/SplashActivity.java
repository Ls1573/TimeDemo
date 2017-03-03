package com.time.memory.view.activity.login;

import android.os.Handler;
import android.text.TextUtils;

import com.time.memory.presenter.SplashPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.ISplashView;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:开屏页
 * @date 2016/9/22 11:36
 */
public class SplashActivity extends BaseActivity implements ISplashView {

	private final String TAG = "StartActivity";

	@Override
	public void onCreateMyView() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return new SplashPresenter();
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isFinishing())
					((SplashPresenter) mPresenter).reqUser();
			}
		}, 500);
	}

	@Override
	public void redirectTo(Class cla) {
		//跳转
		startAnimActivity(cla);
		finish();
	}

	/**
	 * API 来设置别名(JPush)
	 * 有效的别名
	 */
	@Override
	public void initJpush(String userId) {
		try {
			CLog.e(TAG, "************************userId:" + userId);
			if (TextUtils.isEmpty(userId)) {
				return;
			}
			// 设置别名
			JPushInterface.setAliasAndTags(getApplicationContext(), userId,
					null, new TagAliasCallback() {
						String logs = "";
						@Override
						public void gotResult(int code, String alias,
											  Set<String> tags) {
							switch (code) {
								case 0:
									logs = "Set tag and alias success" + "   " + alias;
									CLog.e(TAG, logs);
									break;
								case 6002:
									logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
									CLog.e(TAG, logs);
									break;
								default:
									logs = "Failed with errorCode = " + code;
									CLog.e(TAG, logs);
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}