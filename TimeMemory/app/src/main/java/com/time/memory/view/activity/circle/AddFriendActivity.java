package com.time.memory.view.activity.circle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.presenter.AddFriendPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IBaseView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:加好友
 * @date 2016/9/29 19:18
 * ==============================
 */
public class AddFriendActivity extends BaseActivity implements IBaseView {

	@Bind(R.id.add_friend_iv)
	ImageView add_friend_iv;
	@Bind(R.id.add_friendname_tv)
	TextView friendTv;

	private String userId;//用户Id
	private String userName;//用户name
	private String hPic;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_addfriend);
	}

	@Override
	public void initView() {
		userId = getIntent().getStringExtra("userId");
		userName = getIntent().getStringExtra("userName");
		hPic = getIntent().getStringExtra("hPic");
		//获取用户名
		initTopBarForLeft(getString(R.string.app_newfriend), R.drawable.image_back);
	}

	@Override
	public void initData() {
		if (!TextUtils.isEmpty(hPic))
			Picasso.with(mContext).load(getString(R.string.FSIMAGEPATH) + hPic + getString(R.string.FSIMAGEOSSDETAIL)).error(R.drawable.friend_photo).placeholder(R.drawable.friend_photo).into(add_friend_iv);
		else
			add_friend_iv.setImageResource(R.drawable.friend_photo);
		friendTv.setText(userName);
	}

	@Override
	public BasePresenter initPresenter() {
		return new AddFriendPresenter();
	}

	@OnClick({R.id.app_submit})
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//申请成为好友
			((AddFriendPresenter) mPresenter).reqAddFriend(getString(R.string.FSREQAPPLY), userId);
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		showShortToast("申请成功!");
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}
}
