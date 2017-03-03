package com.time.memory.view.activity.login;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.core.task.TimeCount;
import com.time.memory.entity.User;
import com.time.memory.gui.ClearEditText;
import com.time.memory.gui.PwdEditText;
import com.time.memory.presenter.UpdatePwdPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IUpPwdView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:修改密码
 * @date 2016/9/23 11:52
 */
public class UpPwdActivity extends BaseActivity implements IUpPwdView, EasyPermissions.PermissionCallbacks {

	@Bind(R.id.register_code_et)
	EditText registerCodeEt;//验证码
	@Bind(R.id.register_pwd_et)
	PwdEditText registerPwdEt;//密码
	@Bind(R.id.login_send_verifty)
	TextView sendVerifty;// 发送验证码
	@Bind(R.id.app_submit)
	TextView appSubmit;// 提交

	@Bind(R.id.register_et)
	ClearEditText register_et;

	private String phone;//手机号
	private TimeCount timeCountUtil;// 计时器

//	private SmsContent content;
//	private ContentResolver resolver;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_uppwd);
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
	public BasePresenter initPresenter() {
		return new UpdatePwdPresenter();
	}

	@Override
	public void initView() {
		String opr = getIntent().getStringExtra("opr");
		initTopBarForLeft(opr, R.drawable.image_back);
	}

	@Override
	public void initData() {
		//读短信权限申请
//		requestSMSPermission();
		// 计1S一换
		timeCountUtil = new TimeCount(60000, 1000, sendVerifty, mContext);
	}

	@OnClick({R.id.app_submit, R.id.login_send_verifty, R.id.app_cancle})
	public void onClick(View view) {
		phone = register_et.getText().toString().trim();
		if (view.getId() == R.id.app_submit) {
			//提交
			((UpdatePwdPresenter) mPresenter).reqUpPwd(getString(R.string.FSCHANGE), phone, registerCodeEt.getText().toString(), registerPwdEt.getText().toString());
		} else if (view.getId() == R.id.login_send_verifty) {
			//发送验证码
			((UpdatePwdPresenter) mPresenter).sendVerify(getString(R.string.FSCHANGEVERIFTY), phone);
		} else if (R.id.app_cancle == view.getId()) {
			finish();
		}
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();

	}

	@Override
	public void upSuccess() {
		//修改成功
		showShortToast("修改成功,请重新登录");
		finish();
	}

	@Override
	public void setUser(User user) {
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	@Override
	public void sendVerify() {
		timeCountUtil.start();
		// 按钮不可用
		sendVerifty.setEnabled(false);
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
