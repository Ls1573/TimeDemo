package com.time.memory.view.activity.memory;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.nineGridImage.NineGridImageView;
import com.time.memory.gui.nineGridImage.NineGridImageViewAdapter;
import com.time.memory.presenter.GroupPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.IFindGroupView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:搜索群
 * @date 2016/10/15 15:10
 */
public class FindGroupActivity extends BaseActivity implements IFindGroupView {
	@Bind(R.id.group_imsg)
	NineGridImageView groupImsg;
	@Bind(R.id.group_name_tv)
	TextView groupNameTv;//群名
	@Bind(R.id.group_admin_tv)
	TextView groupAdminTv;//创建者

	private MGroup mGroup;
	private String imgOss;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_searchgroup);
	}

	@Override
	public BasePresenter initPresenter() {
		return new GroupPresenter();
	}

	@Override
	public void initView() {
	}

	@Override
	public void initData() {
		//封面
		initGroupThumb();
		imgOss = getString(R.string.FSIMAGEPATH);
		mGroup = getIntent().getParcelableExtra("group");

		initTopBarForLeft(mGroup.getGroupName(), R.drawable.image_back);
		groupNameTv.setText(mGroup.getGroupName());
		groupAdminTv.setText(String.format(getString(R.string.group_creater), mGroup.getAdminUserName()));

		((GroupPresenter) mPresenter).convertGroup(mGroup);
	}

	/**
	 * 绑定封面图
	 */

	private void initGroupThumb() {
		NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
			@Override
			protected void onDisplayImage(Context context, ImageView imageView, String path) {
				if (TextUtils.isEmpty(path))
					imageView.setImageResource(R.drawable.headpic);
				else
					Picasso.with(mContext).load(imgOss + path).config(Bitmap.Config.RGB_565).into(imageView);
			}
		};
		groupImsg.setAdapter(mAdapter);
	}

	@Override
	public void setImagesData(List lists) {
		groupImsg.setImagesData(lists);
	}

	@OnClick(R.id.app_submit)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.app_submit) {
			//申请进群
			((GroupPresenter) mPresenter).reqAddCircle(getString(R.string.FSADDGROUP), mGroup.getGroupId(), mGroup.getAdminUserId());
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

}
