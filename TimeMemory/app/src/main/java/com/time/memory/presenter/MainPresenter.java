package com.time.memory.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.core.helper.UpgradeHelper;
import com.time.memory.core.im.IMClientManager;
import com.time.memory.core.im.android.ClientCoreSDK;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.Upgrade;
import com.time.memory.entity.User;
import com.time.memory.model.LoginController;
import com.time.memory.model.OpenTheRecordController;
import com.time.memory.model.UpgradeController;
import com.time.memory.model.impl.ILoginController;
import com.time.memory.model.impl.IUpgradeController;
import com.time.memory.model.impl.OpenRecordController;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.AppUtil;
import com.time.memory.util.CLog;
import com.time.memory.view.impl.IMainView;

import java.util.concurrent.ExecutorService;

/**
 * 首页
 */
public class MainPresenter extends BasePresenter<IMainView> {
	private static final String TAG = "MainPresenter";
	// m层
	private ILoginController iLoginController;
	private ExecutorService executorService;
	private OpenRecordController openRecordController;//app打开记录
	private IUpgradeController iUpgradeController;//版本更新
	private UpgradeHelper mUpgradeHelper; // 版本更新下载

	public MainPresenter() {
		iLoginController = new LoginController();
		executorService = ExecutorManager.getInstance();
		openRecordController = new OpenTheRecordController();
		iUpgradeController = new UpgradeController();
	}

	/**
	 * @Description:版本更新
	 */
	public void reqVersion(String url, final Context context) {
		iUpgradeController.reqUpgrade(url, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
			}

			@Override
			public void onCallback(Object data) {
				if (data == null) {
				} else {
					Upgrade entity = (Upgrade) data;
					if (entity.getStatus() == 0) {
						updateVersion(entity.getResult(), context);
					}
				}
			}
		});
	}

	// 版本更新
	private void updateVersion(Upgrade entity, Context context) {
		mUpgradeHelper = new UpgradeHelper(context);
		if (mUpgradeHelper == null)
			return;
		// TODO 版本对比
		String curVersion = AppUtil.getVersionName();
//		String netVersion = entity.getAndroid();//1.0.0
		String netVersionVersion = entity.getAndroidVersion();//!=1.0.0
		// 版本一致,退出
		if (curVersion.equals("1.0.0"))
			return;
		if (curVersion.equals(netVersionVersion))
			return;

		// 设置下载地址
		mUpgradeHelper.setDownloadUrl(entity.getAndroid_download_link());
		if (!"1".equals(entity.getAndroid_force_update())) {
			//比较androidFlag->版本分割点
			double oldFlag = Double.parseDouble(entity.getAndroidFlag().replace(".", ""));
			double curFlag = Double.parseDouble(curVersion.replace(".", ""));
			if (curFlag < oldFlag) {
				//强制更新
				mUpgradeHelper.confNorUpdate(netVersionVersion, entity.getAndroid_update_message(), true);
			} else {
				// 不需要强制更新,设置描述
				mUpgradeHelper.confNorUpdate(netVersionVersion, entity.getAndroid_update_message(), false);
			}
		} else {
			CLog.e(TAG, "强制更新");
			// 设置描述
			mUpgradeHelper.confNorUpdate(netVersionVersion, entity.getAndroid_update_message(), true);
		}
	}

	/**
	 * 添加App打开记录
	 */
	public void openRecord(String url) {
		openRecordController.openRecord(url, AppUtil.getChannel(), getVersion(), "1", Build.MODEL, Build.VERSION.RELEASE, new SimpleCallback() {
			@Override
			public void onNoNetCallback() {
			}

			@Override
			public void onCallback(Object data) {
			}
		});
	}

	/**
	 * 链接监听
	 */
	public void startListener() {
//		CLog.e(TAG, "startListener------------------------------->");
		executorService.submit(new Runnable() {
			@Override
			public void run() {
//				CLog.e(TAG, "run------------------------------->");
				while (true) {
//					CLog.e(TAG, "while------------------------------->");
					try {
						//休眠5秒
						Thread.sleep(30000);
					} catch (Exception e) {
						e.printStackTrace();
					}
//					CLog.e(TAG, "isConnectedToServer:" + ClientCoreSDK.getInstance().isConnectedToServer());
					if (!ClientCoreSDK.getInstance().isConnectedToServer()) {
						reqLoginIm(MainApplication.getUserId());
					}
				}
			}
		});
	}

	/**
	 * 建立长链接(内部)
	 */
	public void reqLoginIm(String userId) {
		User user = iLoginController.getUser(userId);
		//MobileIMSDK服务初始化
		IMClientManager.getInstance(context).initMobileIMSDK();
		iLoginController.reqLoginIm(context, context.getResources().getString(R.string.FSCLIENTIP), context.getResources().getString(R.string.FSCLIENTHOST), userId, user.getUserPw(), new SimpleCallback() {
			@Override
			public void onCallback(Object data) {
				int code = (Integer) data;
				CLog.e(TAG, "data:" + code);
				// 登陆成功
				if (code == 0) {
					CLog.e(TAG, "长链接成功------------------->");
					return;
				} else {
					CLog.e(TAG, "长链接失败------------------->");
				}
			}

			@Override
			public void onNoNetCallback() {
			}
		});
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "错误";
		}
	}

}