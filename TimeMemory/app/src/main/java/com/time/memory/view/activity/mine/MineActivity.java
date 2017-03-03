package com.time.memory.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.Address;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.User;
import com.time.memory.gui.ActionSheet;
import com.time.memory.gui.QrCodeSheet;
import com.time.memory.presenter.MineInfoPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.QrCodeActivity;
import com.time.memory.view.impl.IMineInfoView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:个人信息
 * @date 2016-9-5上午11:28:10
 * ==============================
 */
public class MineActivity extends BaseActivity implements IMineInfoView {
	private static final String TAG = "MineActivity";
	@Bind(R.id.mine_nick_et)	TextView mine_nick_et;//昵称
	@Bind(R.id.mine_company_et)	TextView mine_company_et;//公司
	@Bind(R.id.mine_email_et)	TextView mine_email_et;//邮箱

	@Bind(R.id.mine_phone_tv)	TextView mine_phone_tv;//手机号
	@Bind(R.id.mine_sex_tv)		TextView mine_sex_tv;//性别
	@Bind(R.id.mine_address_tv)	TextView mine_address_tv;//地区
	@Bind(R.id.mine_sign_tv)	TextView mine_sign_tv;//签名
	@Bind(R.id.mine_profession_tv)	TextView mine_profession_tv;//行业
	@Bind(R.id.mine_phone_iv)	ImageView mine_phone_iv;//头像

	private User user;
	private PhotoInfo photoInfo;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_mine);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MineInfoPresenter();
	}

	@Override
	public void initView() {
		initTopBarForLeft(getString(R.string.mine_info), R.drawable.image_back);
	}

	@Override
	public void initData() {
		//获取用户信息
		((MineInfoPresenter) mPresenter).getUser(MainApplication.getUserId());
	}

	@OnClick({R.id.mine_photo_ll, R.id.mine_sex_ll, R.id.mine_sign_ll, R.id.mine_profession_ll, R.id.mine_address_ll, R.id.tv_main_right, R.id.mine_ercode_ll, R.id.mine_nick_ll,
			R.id.mine_email_ll, R.id.mine_company_ll})
	public void onClick(View view) {
		if (user == null) return;
		Intent intnet = new Intent();
		switch (view.getId()) {
			case R.id.app_cancle:
				KeyBoardUtils.hideKeyboard(this);
				finish();
				break;
			case R.id.mine_photo_ll:
				//头像
				intnet.setClass(mContext, PhotoActivity.class);
				intnet.putExtra("netPath", user.getHeadPhoto());
				intnet.putExtra("locPath", photoInfo == null ? user.getLocpath() : photoInfo.getPhotoPath());
				startActivityForResult(intnet, ReqConstant.UPHEADER);
				break;
			case R.id.mine_sex_ll:
				//性别
				showSexDialog();
				break;
			case R.id.mine_sign_ll:
				// 个性签名
				intnet.setClass(mContext, UpSignActivity.class);
				intnet.putExtra("sign", user.getSign());
				startActivityForResult(intnet, ReqConstant.SIGN);
				break;
			case R.id.mine_profession_ll:
				// 行业
				intnet.setClass(mContext, UpProfessionActivity.class);
				intnet.putExtra("profession", user.getCompanyIntroduce());
				startActivityForResult(intnet, ReqConstant.PRESISSION);
				break;
			case R.id.mine_address_ll:
				// 地址
				intnet.setClass(mContext, ProvinceActivity.class);
//				startActivityForResult(intnet, ReqConstant.ADDRESS);
				startActivity(intnet);
				break;
			case R.id.tv_main_right:
				//提交
				sendMsg();
				break;
			case R.id.mine_ercode_ll:
				//二维码
				intnet = new Intent(mContext, QrCodeActivity.class);
				intnet.putExtra("qrcode", "sgjy_user_" + user.getUserId() + "&spt;" + user.getUserName() + "&spt;" + user.getHeadPhoto());
				intnet.putExtra("sign", getString(R.string.qrcode_user));
				startAnimActivity(intnet);
				break;
			case R.id.mine_nick_ll:
				//修改名字
				intnet.setClass(mContext, UpNickNameActivity.class);
				intnet.putExtra("nickName", user.getUserName());
				startActivityForResult(intnet, ReqConstant.NICK_NAME);
				break;
			case R.id.mine_email_ll:
				//修改邮箱
				intnet.setClass(mContext, UpEmailActivity.class);
				intnet.putExtra("email", user.getEmail());
				startActivityForResult(intnet, ReqConstant.EMAIL);
				break;
			case R.id.mine_company_ll:
				//修改公司
				intnet.setClass(mContext, UpCompanyActivity.class);
				intnet.putExtra("company", user.getCompany());
				startActivityForResult(intnet, ReqConstant.COMPANY);
				break;
		}
	}

	@Override
	public void setUserInfos(User user) {
		this.user = user;
		//设置用户信息
		//昵称
		mine_nick_et.setText(user.getUserName());
		//公司
		mine_company_et.setText(user.getCompany());
		//邮箱
		mine_email_et.setText(user.getEmail());
		//手机号
		mine_phone_tv.setText(user.getUserMobile());
		//性别
		mine_sex_tv.setText(user.getUserGender());
		//地址
		mine_address_tv.setText(user.getAddress());
		//签名
		mine_sign_tv.setText(user.getSign());
		//行业
		mine_profession_tv.setText(user.getCompanyIntroduce());

		//头像
		if (!TextUtils.isEmpty(user.getHeadPhoto())) {
			if (user.getHeadPhoto().contains("http"))
				Picasso.with(mContext).load(user.getHeadPhoto() + getString(R.string.FSIMAGEOSS)).resize(300, 300).centerCrop().error(R.drawable.friend_photo).into(mine_phone_iv);
			else
				Picasso.with(mContext).load(getString(R.string.FSIMAGEPATH) + user.getHeadPhoto() + getString(R.string.FSIMAGEOSS)).resize(300, 300).centerCrop().error(R.drawable.friend_photo).into(mine_phone_iv);
		} else if (!TextUtils.isEmpty(user.getLocpath())) {
			Picasso.with(mContext).load("file://" + user.getLocpath()).resize(300, 300).centerCrop().into(mine_phone_iv);
		}
	}

	/**
	 * 上传信息
	 *
	 * @param path
	 */
	@Override
	public void upLoadMsg(String path) {
		String nickName = mine_nick_et.getText().toString();
		String email = mine_email_et.getText().toString();
		String company = mine_company_et.getText().toString();
		String sex = mine_sex_tv.getText().toString();
		((MineInfoPresenter) mPresenter).uploadMsg(getString(R.string.FSUPUSER), path, nickName, email, company, sex, photoInfo, user);
	}


	/**
	 * 发送信息
	 */
	private void sendMsg() {
//		showLoadingDialog();
		((MineInfoPresenter) mPresenter).uploadImage(photoInfo);
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();
//		finish();
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
	}

	/**
	 * 显示QrCode
	 */
	private void showQrCode(String userId) {
		QrCodeSheet.createBuilder(mContext, getSupportFragmentManager()).setCancelableOnTouchOutside(true).setData("sgjy_user_" + userId).show();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//设置singTask的intent
		setIntent(intent);
		Address city = getIntent().getParcelableExtra("city");
		Address province = getIntent().getParcelableExtra("province");

		StringBuffer sb = new StringBuffer();
		if (province != null) {
			sb.append(province.getName());
			user.setProvinceId(province.getId());
		}
		if (city != null) {
			sb.append(" " + city.getName());
			user.setCityId(city.getId());
		}
		user.setAddress(sb.toString());
		mine_address_tv.setText(sb.toString());

		intent.removeExtra("city");
		intent.removeExtra("province");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_CANCELED) return;
		if (data == null) return;
		if (resultCode == ReqConstant.SIGN) {
			//添加签名
			String sign = data.getStringExtra("sign");
			if (!TextUtils.isEmpty(sign)) {
				mine_sign_tv.setText(sign);
				user.setSign(sign);
			}
		} else if (resultCode == ReqConstant.PRESISSION) {
			//添加行业
			String presission = data.getStringExtra("presission");
			if (!TextUtils.isEmpty(presission)) {
				mine_profession_tv.setText(presission);
				user.setCompanyIntroduce(presission);
			}
		} else if (resultCode == ReqConstant.UPHEADER) {
			//修改头像
			photoInfo = data.getParcelableExtra("photoInfo");
			if (photoInfo != null) {
				//保存了一张网络图
				Picasso.with(mContext).load("file://" + photoInfo.getPhotoPath()).config(Bitmap.Config.RGB_565).into(mine_phone_iv);
			}
		} else if (resultCode == ReqConstant.NICK_NAME) {
			//修改用户名
			String nickName = data.getStringExtra("nickName");
			if (!TextUtils.isEmpty(nickName)) {
				mine_nick_et.setText(nickName);
				user.setUserName(nickName);
			}
		} else if (resultCode == ReqConstant.EMAIL) {
			String emial = data.getStringExtra("email");
			if (!TextUtils.isEmpty(emial)) {
				mine_email_et.setText(data.getStringExtra("email"));
				user.setEmail(data.getStringExtra("email"));
			}
		} else if (resultCode == ReqConstant.COMPANY) {
			String company = data.getStringExtra("company");
			if (!TextUtils.isEmpty(company)) {
				mine_company_et.setText(company);
				user.setCompany(company);
			}
		}
	}

	/**
	 * 弹窗选择性别
	 */
	private void showSexDialog() {
		ActionSheet.createBuilder(mContext, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				setCancelButtonTitle(getString(R.string.app_cancle)).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setOtherButtonTitles("男", "女").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						CLog.e(TAG, "index:" + index);
						mine_sex_tv.setText(index == 0 ? "男" : "女");
						sendMsg();
					}

					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
						//取消操作
					}
				}).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sendMsg();
	}
}
