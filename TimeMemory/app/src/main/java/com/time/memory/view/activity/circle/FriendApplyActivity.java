package com.time.memory.view.activity.circle;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.User;
import com.time.memory.gui.ActionSheet;
import com.time.memory.presenter.FriendApplyPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IFriendApplyView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:好友申请
 * @date 2016/9/29 19:37
 */
public class FriendApplyActivity extends BaseActivity implements IFriendApplyView {

	private static final String TAG = "FriendApplyActivity";
	@Bind(R.id.friend_info_ll)
	LinearLayout friend_info_ll;
	@Bind(R.id.friend_apply_ll)
	LinearLayout friend_apply_ll;

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

	private String userId;
	private String msgId;
	private int position;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_friendapply);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_friendapply), R.drawable.image_back, getString(R.string.app_report), -1);
	}

	@Override
	public void initData() {
		userId = getIntent().getStringExtra("userId");
		msgId = getIntent().getStringExtra("msgId");
		position = getIntent().getIntExtra("position", -1);
		//获取数据
		((FriendApplyPresenter) mPresenter).reqFriendInfo(getString(R.string.FSREQFRIENDINFO), userId);
	}

	@Override
	public BasePresenter initPresenter() {
		return new FriendApplyPresenter();
	}


	@OnClick({R.id.app_cancle, R.id.tv_main_right, R.id.app_submit, R.id.app_delete})
	public void onClick(View view) {
		super.onMyClick(view);
		switch (view.getId()) {
			case R.id.app_cancle:
				finish();
				break;
			case R.id.tv_main_right:
				reportUser();
				break;
			case R.id.app_submit:
				//提交
				((FriendApplyPresenter) mPresenter).accept(MainApplication.getUserId(), userId);
				break;
			case R.id.app_delete:
				//拒绝
				((FriendApplyPresenter) mPresenter).refuse(msgId);
				break;
		}
	}

	@Override
	public void setMessage(User user) {
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
		friend_apply_ll.setVisibility(View.VISIBLE);
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		setMyResult(ReqConstant.APPLYFRIEND, 1);
	}

	@Override
	public void refuse() {
		//拒绝
		setMyResult(ReqConstant.APPLYFRIEND, -1);
	}

	@Override
	public void reportSuccess() {
		//举报成功
		hideLoadingDialog();
	}


	/**
	 * 设置返回结果
	 *
	 * @param resultCode
	 */
	private void setMyResult(int resultCode, int opr) {
		Intent intent = new Intent();
		intent.putExtra("opr", opr);
		intent.putExtra("position", position);
		setResult(resultCode, intent);
		finish();
	}

	/**
	 * 举报用户
	 */
	private void reportUser() {
		ActionSheet.createBuilder(mContext, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setCancelButtonTitle("取消").
				setOtherButtonTitles("色情/暴力信息", "广告信息", "钓鱼/欺诈信息", "诽谤造谣信息").
				setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}

					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						CLog.e(TAG, "index:" + index);
						((FriendApplyPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), userId, "1", String.valueOf(index + 1));
					}
				}).show();
	}
}
