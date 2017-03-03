package com.time.memory.view.activity.memory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.TempMemory;
import com.time.memory.gui.ActionSheet;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.gui.indicator.CommonNavigator;
import com.time.memory.gui.indicator.CommonNavigatorAdapter;
import com.time.memory.gui.indicator.IPagerIndicator;
import com.time.memory.gui.indicator.IPagerTitleView;
import com.time.memory.gui.indicator.LinePagerIndicator;
import com.time.memory.gui.indicator.MagicIndicator;
import com.time.memory.gui.indicator.SimplePagerTitleView;
import com.time.memory.gui.indicator.ViewPagerHelper;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DevUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.circle.GroupActivity;
import com.time.memory.view.adapter.FragmentAdapter;
import com.time.memory.view.fragment.memory.group.MemoryGroupHotFragment;
import com.time.memory.view.fragment.memory.group.MemoryGroupTagFragment;
import com.time.memory.view.fragment.memory.group.MemoryGroupTimeFragment;
import com.time.memory.view.impl.IBaseView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:群的记忆
 * @date 2016/10/24 16:18
 */
public class MemoryGroupActivity extends BaseActivity implements IBaseView {

	private static final String TAG = "OtherMemoryActivtiy";
	@Bind(R.id.view_pager)
	ViewPager view_pager;
	@Bind(R.id.tabs)
	MagicIndicator mTabLayout;
	@Bind(R.id.view_line)
	View view_line;
	@Bind(R.id.tv_main_title)
	TextView tvTitle;

	private final int REQUEST_CODE_CAMERA = 100;
	private final int REQUEST_CODE_GALLERY = 101;

	private MemoryGroupHotFragment allMemoryFragment;//(热度)
	private MemoryGroupTimeFragment monthMemoryFragment;//(时间轴)
	private MemoryGroupTagFragment tagMemoryFragment;//(标签)

	private FragmentAdapter mFragmentAdapter;
	private List<Fragment> mFragmentList;//view
	private List<String> mDataList;//title
	private List<PhotoInfo> mPhotoList;//图片集合

	private MGroup mGroup;//当前选择的群组
	private int type = 2;//隐私权限


	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_mymemory);
	}

	@Override
	protected void onDestroy() {
		if (mFragmentList != null) {
			allMemoryFragment = null;
			monthMemoryFragment = null;
			tagMemoryFragment = null;

			mFragmentList.clear();
			mFragmentList = null;
		}

		if (mPhotoList != null) {
			mPhotoList.clear();
			mPhotoList = null;
		}

		if (mFragmentAdapter != null) {
			mFragmentAdapter = null;
		}
		System.gc();
		super.onDestroy();
	}

	@Override
	public BasePresenter initPresenter() {
		return null;
	}

	@Override
	public void initView() {
		mGroup = getIntent().getParcelableExtra("mGroup");
		initTopBarAll(mGroup.getGroupName(), R.drawable.image_back, R.drawable.take_picture, R.drawable.circlepeople);
		initLoading();
	}

	@Override
	public void initData() {

		//跳转信息类型(我的|他的|群的)
		mPhotoList = new ArrayList<>();
		mFragmentList = new ArrayList<>();

		mFragmentList.add(allMemoryFragment = new MemoryGroupHotFragment());
		mFragmentList.add(monthMemoryFragment = new MemoryGroupTimeFragment());
		mFragmentList.add(tagMemoryFragment = new MemoryGroupTagFragment());

		Bundle bundle = new Bundle();
		bundle.putInt("type", mGroup.getType());
		bundle.putString("groupId", mGroup.getGroupId());
		bundle.putString("adminId", mGroup.getAdminUserId());
		bundle.putString("title", mGroup.getGroupName());
		allMemoryFragment.setArguments(bundle);
		monthMemoryFragment.setArguments(bundle);
		tagMemoryFragment.setArguments(bundle);
		//设置Adapter
		mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragmentList);
		view_pager.setAdapter(mFragmentAdapter);
		view_pager.setOffscreenPageLimit(mFragmentList.size());//设置缓存数量
		//初始化指示器
		initIndicator();
		//判断显示画面
		showLoadingView();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//设置singTask的intent
		setIntent(intent);
		TempMemory memory = getIntent().getParcelableExtra("tempMemory");
		if (memory != null) {
			//有记忆了
			showSuccess();
			//共享出去
			getIntent().putExtras(intent);
		}
	}

	/**
	 * 指示器
	 */
	private void initIndicator() {
		mDataList = Arrays.asList(getResources().getStringArray(R.array.memory_tabs));
		CommonNavigator commonNavigator = new CommonNavigator(this);
		commonNavigator.setAdjustMode(true);
		commonNavigator.setAdapter(new CommonNavigatorAdapter() {
			@Override
			public int getCount() {
				return mDataList.size();
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				//文本设置
				SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
				simplePagerTitleView.setNormalColor(getResources().getColor(R.color.grey_67));
				simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.yellow_d9));
				simplePagerTitleView.setText(mDataList.get(index));
				simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						view_pager.setCurrentItem(index);
					}
				});
				return simplePagerTitleView;
			}

			@Override
			public IPagerIndicator getIndicator(Context context) {
				//指示线
				LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
				linePagerIndicator.setColors(getResources().getColor(R.color.yellow_D4));
				linePagerIndicator.setLineHeight(DevUtils.dip2px(mContext, 2));
				return linePagerIndicator;
			}
		});
		mTabLayout.setNavigator(commonNavigator);
		//互相绑定
		ViewPagerHelper.bind(mTabLayout, view_pager);
	}

	@OnClick({R.id.iv_main_right, R.id.iv_main_right_one})
	public void onClick(View view) {
		super.onMyClick(view);
		if (R.id.iv_main_right == view.getId()) {
			//相册选取
			GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(299, false, true, false, type, mGroup.getGroupId(), mPhotoList), null);
		} else if (R.id.iv_main_right_one == view.getId()) {
			//圈子成员管理
			Intent intent = new Intent(mContext, GroupActivity.class);
			intent.putExtra("group", mGroup);
			startActivityForResult(intent, ReqConstant.REQUEST_CODE_WATGROUP);
		}
	}

	/**
	 * 初始化LoadingView
	 */
	private void showLoadingView() {
		if (mGroup.getMemoryCnt().equals("0")) {
			//没有记忆
			super.showEmpty();
		} else {
			//有记忆
			showSuccess();
		}
	}

	@Override
	public void showSuccess() {
		super.showSuccess();
		view_pager.setVisibility(View.VISIBLE);
		view_line.setVisibility(View.VISIBLE);
		mTabLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 刷新数据
	 */
	@Override
	public void onRetry() {
		GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(299, false, true, false, type, mGroup.getGroupId(), mPhotoList), null);
	}

	// 弹出窗口选择图片
	public void showPicDialog() {
		// dialog弹窗
		ActionSheet.createBuilder(mContext, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("拍照", "从手机相册选择")
				.setCancelableOnTouchOutside(true).setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext))
				.setListener(new ActionSheet.ActionSheetListener() {
					@Override
					public void onOtherButtonClick(ActionSheet actionSheet, int index) {
						switch (index) {
							case 0:
								// 拍照
								GalleryFinal.openCamera(REQUEST_CODE_CAMERA, initPhoto(1, false, true, false, type, mGroup.getGroupId(), mPhotoList), null);
								break;
							case 1:
								//相册选取
								GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(199, false, true, false, type, mGroup.getGroupId(), mPhotoList), null);
								break;
						}
					}

					// 选择的性别
					@Override
					public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
					}
				}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		if (requestCode == ReqConstant.REQUEST_CODE_WATGROUP) {
			//群成员查看
			if (data == null) return;
			mGroup = data.getParcelableExtra("group");
			tvTitle.setText(mGroup.getGroupName());
		}
	}
}
