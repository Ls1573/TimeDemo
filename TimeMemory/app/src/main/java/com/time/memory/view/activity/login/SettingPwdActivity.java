package com.time.memory.view.activity.login;

import android.Manifest;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.im.android.core.LocalUDPDataSender;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.core.task.TimeCount;
import com.time.memory.gui.PwdEditText;
import com.time.memory.presenter.SettingPwdPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.MainActivity;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.ISettingPwdView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:设置密码
 * @date 2016-9-6上午8:46:31
 * ==============================
 */
public class SettingPwdActivity extends BaseActivity implements ISettingPwdView, EasyPermissions.PermissionCallbacks {

	private static final String TAG = "SettingPwdActivity";
	@Bind(R.id.register_code_et)
	EditText registerCodeEt;//验证码
	@Bind(R.id.register_pwd_et)
	PwdEditText registerPwdEt;//密码
	@Bind(R.id.register_nick_et)
	EditText registerNickEt;//姓名
	@Bind(R.id.login_send_verifty)
	TextView sendVerifty;// 发送验证码
	@Bind(R.id.app_submit)
	TextView appSubmit;// 提交

	private String phone;//手机号
	private TimeCount timeCountUtil;// 计时器
//	private SmsContent content;
//	private ContentResolver resolver;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_password);
	}

	@Override
	protected void onDestroy() {
//		if (content != null) {
//			resolver.unregisterContentObserver(content);
//			resolver = null;
//			content = null;
//		}
		if (timeCountUtil != null) {
			timeCountUtil.cancel();
			timeCountUtil = null;
		}
		super.onDestroy();
	}

	@Override
	public void initView() {
	}

	@Override
	public BasePresenter initPresenter() {
		return new SettingPwdPresenter();
	}


	@Override
	public void initData() {
		//读短信权限申请
//		requestSMSPermission();
		//获取手机号
		phone = getIntent().getStringExtra("phone");
		// 计1S一换
		timeCountUtil = new TimeCount(60000, 1000, sendVerifty, mContext);
		// 开始倒计时
		sendVerifty();
	}


	@OnClick({R.id.app_submit, R.id.login_send_verifty, R.id.app_cancle})
	public void onClick(View view) {
		if (view.getId() == R.id.app_submit) {
			//提交
			((SettingPwdPresenter) mPresenter).reqRegist(getString(R.string.FSREGIST), phone, registerCodeEt.getText().toString(), registerPwdEt.getText().toString(), registerNickEt.getText().toString());
		} else if (view.getId() == R.id.login_send_verifty) {
			//发送验证码
			sendVerifty();
			((SettingPwdPresenter) mPresenter).sendVerify(getString(R.string.FSGETVALIDCODE), phone);
		} else if (R.id.app_cancle == view.getId()) {
			finish();
		}
	}

	@Override
	public void showSuccess() {
		//注册成功，建立长链接，导入通讯录
		hideLoadingDialog();
		startAnimActivity(ImportContactsActivity.class);
	}

	@Override
	public void showFaild() {
		//发送失败
		hideLoadingDialog();
	}

	/**
	 * 重新发送验证码
	 */
	private void sendVerifty() {
		timeCountUtil.start();
		// 按钮不可用
		sendVerifty.setEnabled(false);
	}

	@Override
	public void importContacts() {
		//导入通讯录
		hideLoadingDialog();
//		startAnimActivity(ImportContactsActivity.class);
//		ActivityTaskManager.getInstance().removeActivity("RegistActivity");
//		ActivityTaskManager.getInstance().removeActivity("LoginActivity");
//		ActivityTaskManager.getInstance().removeActivity("SettingPwdActivity");

		Intent intent = new Intent(mContext, ImportContactsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}


	/**
	 * 发出退出登陆请求包（Android系统要求必须要在独立的线程中发送）
	 */
	private void doLogout() {

		ExecutorManager.getInstance().submit(new Runnable() {
			@Override
			public void run() {
				int code = -1;
				try {
					code = LocalUDPDataSender.getInstance(SettingPwdActivity.this)
							.sendLoginout();
				} catch (Exception e) {
					Log.w(TAG, e);
				}
				if (code == 0) {
					CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求已完成！");
					// 退出释放内存
					System.exit(0);
				} else {
					CLog.e(MainActivity.class.getSimpleName(), "注销登陆请求发送失败。错误码是：" + code + "！");
					System.exit(1);
				}
				System.gc();
				android.os.Process.killProcess(android.os.Process.myPid());
				finish();
			}
		});
	}


	/**
	 * 读取短信
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_SMS)
	protected void requestSMSPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_SMS)) {
			// 监听系统短信
//			content = new SmsContent(this, new Handler(), registerCodeEt);
//			resolver = getContentResolver();
//			// 注册一个内容观察者观察短信数据库
//			resolver.registerContentObserver(Uri.parse("content://sms/"), true, content);
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_sms),
					ReqConstant.REQUEST_CODE_SMS, Manifest.permission.READ_SMS);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//更改权限返回
		if (requestCode == ReqConstant.REQUEST_CODE_SMS) {
			requestSMSPermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}
}
