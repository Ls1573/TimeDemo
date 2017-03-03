package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.User;
import com.time.memory.gui.ActionSheet;
import com.time.memory.presenter.FriendPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DevUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.QrCodeActivity;
import com.time.memory.view.impl.IFriendView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:好友页(资料)
 * @date 2016/9/20 18:37
 */
public class FriendActivity extends BaseActivity implements IFriendView {

	private static final String TAG = "FriendActivity";

	@Bind(R.id.friend_info_ll)
	ScrollView friend_info_ll;

	@Bind(R.id.user_photo_iv)
	ImageView userPhotoIv;//用户头像
	@Bind(R.id.user_name_tv)
	TextView userNameTv;//姓名
	@Bind(R.id.user_phone_tv)
	TextView userPhoneTv;//手机号
	@Bind(R.id.user_email_tv)
	TextView userEmailTv;//email
	@Bind(R.id.user_sex_tv)
	TextView userSexTv;//性别
	@Bind(R.id.user_address_tv)
	TextView userAddressTv;//地址
	@Bind(R.id.user_sign_tv)
	TextView userSignTv;//签名
	@Bind(R.id.friend_profession_tv)
	TextView friendProfessionTv;//行业
	@Bind(R.id.friend_company_tv)
	TextView friendCompanyTv;//公司

	private String userId;//用户Id
	private String userName;//用户名
	private int position;//下标
	private User user;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_friend);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_friend), R.drawable.image_back, getString(R.string.app_report), -1);
	}

	@Override
	public void initData() {
		userId = getIntent().getStringExtra("userId");
		userName = getIntent().getStringExtra("userName");
		position = getIntent().getIntExtra("position", -1);
		//获取数据
		((FriendPresenter) mPresenter).reqFriendInfo(getString(R.string.FSREQFRIENDINFO), userId);
	}

	@Override
	public BasePresenter initPresenter() {
		return new FriendPresenter();
	}


	@OnClick({R.id.app_cancle, R.id.tv_main_right, R.id.mine_photo_ll, R.id.mine_remark_ll})
	public void onClick(View view) {
		super.onMyClick(view);
		switch (view.getId()) {
			case R.id.app_cancle:
				finish();
				break;
			case R.id.tv_main_right:
				reportUser();
				break;
			case R.id.mine_photo_ll:
				//二维码
				showQrCode();
				break;
			case R.id.mine_remark_ll:
				//设置备注
				Intent intent = new Intent(mContext, ReMarkActivity.class);
				intent.putExtra("user", user);
				intent.putExtra("position", position);
				startActivityForResult(intent, ReqConstant.REQUEST_CODE_REMARK);
				break;
		}
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void setMessage(User user) {
		this.user = user;
		if (!TextUtils.isEmpty(userName))
			user.setUserName(userName);
		user.setUserId(userId);
		//姓名
		userNameTv.setText(user.getUserName());
		//手机号
		userPhoneTv.setText(user.getUserMobile());
		//email
		userEmailTv.setText(user.getEmail());
		//性别
		userSexTv.setText(user.getUserGender());
		//地址
		userAddressTv.setText(user.getAddress());
		//签名
		userSignTv.setText(user.getSign());
		//行业
		friendProfessionTv.setText(user.getCompanyIntroduce());
		//公司
		friendCompanyTv.setText(user.getCompany());
		//TODO 用户头像
		if (!TextUtils.isEmpty(user.getHeadPhoto()))
			Picasso.with(mContext).load(getString(R.string.FSIMAGEPATH) + user.getHeadPhoto()).resize(400, 400).error(R.drawable.friend_photo).centerCrop().into(userPhotoIv);
		//显示
		friend_info_ll.setVisibility(View.VISIBLE);
		hideLoadingDialog();
	}

	/**
	 * 显示QrCode
	 */
	private void showQrCode() {
		Intent intent = new Intent(mContext, QrCodeActivity.class);
		intent.putExtra("qrcode", "sgjy_user_" + userId + "&spt;" + user.getUserName() + "&spt;" + user.getHeadPhoto());
		intent.putExtra("sign", getString(R.string.qrcode_user));
		startAnimActivity(intent);
	}

	/**
	 * 举报用户
	 */
	private void reportUser() {
		ActionSheet.createBuilder(mContext, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				setCancelButtonTitle("取消").
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setOtherButtonTitles("色情/暴力信息", "广告信息", "钓鱼/欺诈信息", "诽谤造谣信息").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						((FriendPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), userId, "1", String.valueOf(index + 1));
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		if (data == null) return;
		if (requestCode == resultCode) {
			if (resultCode == ReqConstant.REQUEST_CODE_REMARK) {
				//备注信息
				String userName = data.getStringExtra("userName");
				user.setUserName(userName);
				//姓名
				userNameTv.setText(user.getUserName());
			}
		}
	}
}
