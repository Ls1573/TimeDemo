package com.time.memory.view.activity.memory;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.User;
import com.time.memory.gui.ActionSheet;
import com.time.memory.gui.MemoryMoreSheet;
import com.time.memory.presenter.MemoryPPresenter;
import com.time.memory.presenter.MemoryPointPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.util.ShareUtil;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.adapter.MyMemoryAdapter;
import com.time.memory.view.fragment.memory.MemoryPointlFragment;
import com.time.memory.view.impl.IMemoryPView;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:记忆片段
 * @date 2016/10/27 9:01
 */
public class MemoryPointActivity extends BaseActivity implements IMemoryPView, EasyPermissions.PermissionCallbacks, ViewPager.OnPageChangeListener {

	private static final String TAG = "MemoryPointActivity";
	@Bind(R.id.view_pager)
	ViewPager viewPager;

	private ArrayList<Fragment> fragments;
	private ArrayList<MemoryEdit> mList;//记忆片段数据
	private MemoryInfo memoryInfo;//记忆详情

	private int curPosition;//当前选择第几个片段

	private String userId;//发布人
	private String memoryId;//记忆Id
	private String groupId;//群Id
	private String groupName;//群name
	private int state;
	private int sharedPosition;//分享位置
	private String url;//分享网址
	private ShareUtil shareUtil;//分享工具类
	private String contents;
	private MyMemoryAdapter adapter;

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_memory_detail);
	}

	@Override
	public void initView() {
		//获取数据
		memoryInfo = getIntent().getParcelableExtra("memoryInfo");
		state = getIntent().getIntExtra("state", 1);
		curPosition = getIntent().getIntExtra("curPosition", 0);
		groupId = getIntent().getStringExtra("groupId");
		groupName = getIntent().getStringExtra("groupName");
		mList = memoryInfo.getMemoryPointVos();//记忆片段
		userId = memoryInfo.getMemory().getUserId();
		memoryId = memoryInfo.getMemory().getMemoryId();

		initTopBarForBoth(memoryInfo.getMemory().getTitle(), R.drawable.image_back, "", R.drawable.image_more);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryPPresenter();
	}

	@Override
	public void initData() {
		url = getString(R.string.FSSHAREURL) + "/mt-nio/webPage/memoryInfo.htm?memoryId=" + memoryId;
		shareUtil = new ShareUtil();
		//获取用户个人信息数据
		((MemoryPPresenter) mPresenter).getUser(MainApplication.getUserId());
		if (!TextUtils.isEmpty(mList.get(curPosition).getDetail())) {
			contents = mList.get(curPosition).getDetail();
		} else {
			contents = "快去看看吧！";
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		CLog.e(TAG, "onPageSelected:" + position);
		curPosition = position;
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@OnClick({R.id.app_cancle, R.id.iv_main_right})
	public void onClick(View view) {
		if (view.getId() == R.id.iv_main_right)
			showMoreDialog();
		if (view.getId() == R.id.app_cancle) {
			setMyResult();
		}
	}

	/**
	 * 获取用户信息
	 *
	 * @param user
	 */
	@Override
	public void setUser(User user) {
		//填充数据
		setMyFragments(user);
	}

	@Override
	public void removeMemory(int position, int state) {
		mList.remove(position);
		fragments.remove(position);
		//移除记忆代表集合
		if (mList.size() == 0) {
			memoryInfo = null;
			setMyResult();
			return;
		} else {
			adapter.notifyDataSetChanged();
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
	public void onBackPressed() {
		setMyResult();
	}

	private void setMyResult() {
		KeyBoardUtils.hideKeyboard(this);
		Intent intent = new Intent();
		intent.putExtra("memoryInfo", memoryInfo);
		setResult(ReqConstant.REQUEST_CODE_POINT, intent);
		finish();
	}

	/**
	 * Fragment填充
	 */
	private void setMyFragments(User user) {
		MemoryPointlFragment fragment;
		fragments = new ArrayList<Fragment>();
		int size = mList.size();
		for (int i = 0; i < size; i++) {
			fragments.add(fragment = new MemoryPointlFragment());
			Bundle bundle = new Bundle();
			bundle.putParcelable("memoryPoint", mList.get(i));
			bundle.putString("userId", userId);
			bundle.putInt("state", state);
			bundle.putString("memoryId", memoryId);
			bundle.putString("memorySrcId", memoryInfo.getMemory().getMemoryIdSource());
			bundle.putString("groupName", groupName);
			bundle.putString("groupId", groupId);
			bundle.putString("headPic", user.getHeadPhoto());
			bundle.putString("userName", user.getUserName());
			bundle.putInt("curPoint", i);
			bundle.putString("title", memoryInfo.getMemory().getTitle());
			fragment.setArguments(bundle);
		}
		adapter = new MyMemoryAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(curPosition);
//		viewPager.setOffscreenPageLimit(1);
		viewPager.addOnPageChangeListener(this);
	}

	/**
	 * 举报
	 *
	 * @param position  评论位置
	 * @param isComment 对评论操作
	 */
	private void reportUser(final int position, final boolean isComment) {
		ActionSheet.createBuilder(MemoryPointActivity.this, getSupportFragmentManager()).
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
						if (isComment) {
							//((MemoryPointPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), memoryId,mList.get(curPosition).getMemoryPointId(), memoryEdit.getCommentVos().get(position).getU1id(), "2", String.valueOf(index + 1));
						} else {
							((MemoryPointPresenter) mPresenter).reqReport(getString(R.string.FSREPORT), memoryId, mList.get(curPosition).getMemoryPointId(), "", "2", String.valueOf(index + 1));

						}
					}
				}).show();
	}

	/**
	 * 更多的弹窗
	 */
	private void showMoreDialog() {
		boolean isDelete = false;
		boolean isShared = true;
		if (userId.equals(MainApplication.getUserId())) {
			isDelete = true;
			isShared = false;
		}
		MemoryMoreSheet.createBuilder(MemoryPointActivity.this, getSupportFragmentManager()).
				setCancelableOnTouchOutside(true).
				isDelete(isDelete).
				isEdit(false).
				isShared(isShared).
				setTitlePadding(DevUtils.getTitleHeight(mContext)).
				setBottomPadding(DevUtils.getBottomStatusHeight(mContext)).
				setListener(new MemoryMoreSheet.onMemoryMoreListener() {
					@Override
					public void onSubmit(int postion) {
						if (postion == 7) {
							//删除记忆
							deleteMemory();
						} else if (postion == 6) {
							//举报
							reportUser(-1, false);
						} else if (postion == 5) {
							//编辑记忆
							Intent intent = new Intent(mContext, EditMActivity.class);
							intent.putExtra("memoryInfo", memoryInfo);
							intent.putExtra("state", state);
							startActivityForResult(intent, ReqConstant.REQUEST_CODE_EDIT);
						} else {
							sharedPosition = postion;
							requestWriterPermission();
						}
					}

					@Override
					public void onCancle() {
					}
				}).show();
	}

	/**
	 * 分享
	 *
	 * @param postion
	 */
	private void shared(int postion) {
		String content = "";
		if (state == 2) {
			content = "我刚在«" + groupName + "»写了一篇记忆";
		} else {
			content = "我刚写了一篇记忆";
		}
		if (postion == 4) {
			if (mList.get(curPosition).getPhotoInfos().size() != 0) {
				shareUtil.sinoShare(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, mList.get(curPosition).getPhotoInfos().get(0).getPhotoPath(), url);
			} else {
				shareUtil.sinoShareInt(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, R.drawable.headpic, url);
			}
		} else if (postion == 3) {
			if (mList.get(curPosition).getPhotoInfos().size() != 0) {
				shareUtil.qqSpaceShare(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, mList.get(curPosition).getPhotoInfos().get(0).getPhotoPath(), url);
			} else {
				shareUtil.qqSpaceShareInt(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, R.drawable.headpic, url);
			}
		} else if (postion == 2) {
			if (mList.get(curPosition).getPhotoInfos().size() != 0) {
				shareUtil.qqFriendShare(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, mList.get(curPosition).getPhotoInfos().get(0).getPhotoPath(), url);
			} else {
				shareUtil.qqFriendShareInt(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, R.drawable.headpic, url);
			}
		} else if (postion == 1) {
			if (mList.get(curPosition).getPhotoInfos().size() != 0) {
				shareUtil.friendsCircleShare(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, mList.get(curPosition).getPhotoInfos().get(0).getPhotoPath(), url);
			} else {
				shareUtil.friendsCircleShareInt(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, R.drawable.headpic, url);
			}
		} else if (postion == 0) {
			if (mList.get(curPosition).getPhotoInfos().size() != 0) {
				shareUtil.wXShare(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, mList.get(curPosition).getPhotoInfos().get(0).getPhotoPath(), url);
			} else {
				shareUtil.wXShareInt(MemoryPointActivity.this, content, memoryInfo.getMemory().getTitle(), contents, R.drawable.headpic, url);
			}
		}
	}

	/**
	 * 删除记忆
	 */
	private void deleteMemory() {
		String memoryId = mList.get(curPosition).getMemoryId();
		String memoryPointId = mList.get(curPosition).getMemoryPointId();

		deleteMemroyDialog(userId, memoryId, memoryPointId, mList.size() == 1, curPosition);
	}

	/**
	 * 删除记忆
	 */
	private void deleteMemroyDialog(final String userId, final String memoryId, final String memoryPointId, final boolean isLast, final int mPosition) {
		DialogUtils.request(MemoryPointActivity.this, "确定要删除此记忆片段吗?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isSure = (boolean) data;
				if (isSure) {
					//确定删除
					((MemoryPPresenter) mPresenter).removeMemory(getString(R.string.FSDELETEMEMORY), MainApplication.getUserToken(), userId, memoryId, memoryPointId, "", groupId, mPosition, state, isLast, "", String.valueOf(state), memoryInfo.getMemory().getMemoryIdSource(), mList.get(curPosition).getMemorySrcId());
				}
			}
		});
	}

	/**
	 * 读写文件
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_WRITE)
	protected void requestWriterPermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			shared(sharedPosition);
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_writer),
					ReqConstant.REQUEST_CODE_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == ReqConstant.REQUEST_CODE_WRITE) {
			//更改权限返回
			requestWriterPermission();
		} else {
			UMShareAPI.get(mContext).onActivityResult(requestCode, resultCode, data);
		}
	}
}
