package com.time.memory.view.fragment.memory;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IMMemoryCallBack;
import com.time.memory.core.constant.ImConstant;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.core.im.IMClientManager;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.Advert;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.MySwipeRefreshLayout;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.mt.nio.message.response.SA01ReqVo;
import com.time.memory.mt.nio.message.response.SA10ReqVo;
import com.time.memory.mt.nio.message.response.SA20RespVo;
import com.time.memory.mt.nio.message.response.SG01RespVo;
import com.time.memory.mt.nio.message.response.SG03RespVo;
import com.time.memory.mt.nio.message.response.SG04RespVo;
import com.time.memory.mt.nio.message.response.SW01RespVo;
import com.time.memory.mt.nio.message.response.SW02RespVo;
import com.time.memory.presenter.MemoryPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.DisplayUtils;
import com.time.memory.view.activity.circle.CreateCircleActivity;
import com.time.memory.view.activity.circle.FriendChooseActivity;
import com.time.memory.view.activity.circle.GroupActivity;
import com.time.memory.view.activity.circle.SearchGroupActivity;
import com.time.memory.view.activity.common.LockActivity;
import com.time.memory.view.activity.common.WebActivity;
import com.time.memory.view.activity.memory.MemoryGroupActivity;
import com.time.memory.view.activity.memory.MyMemoryActivtiy;
import com.time.memory.view.activity.memory.UnReadForkActivity;
import com.time.memory.view.activity.memory.UnreadMemoryActivity;
import com.time.memory.view.adapter.BaseRecyclerHeaderAdapter;
import com.time.memory.view.fragment.base.BaseFragment;
import com.time.memory.view.holder.AdvertHolder;
import com.time.memory.view.holder.MemoryHolder;
import com.time.memory.view.impl.IMemoryView;
import com.zbar.lib.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:记忆
 * @date 2016-9-6下午12:17:50
 * ==============================
 */
public class MemoryFragment extends BaseFragment implements IMemoryView, AdapterCallback, EasyPermissions.PermissionCallbacks, IMMemoryCallBack {

	private static final String TAG = "MemoryFragment";

	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	@Bind(R.id.iv_main_right)
	ImageView ivMainRight;

	@Bind(R.id.swipeToLoadLayout)
	MySwipeRefreshLayout swipeToLoadLayout;

	private PopupWindow popu;

	private final int REQUEST_CODE_CAMERA = 100;
	private final int REQUEST_CODE_GALLERY = 101;
	private BaseRecyclerHeaderAdapter adapter;
	private List<MGroup> mMGroupList;
	private boolean isLoad = true;
	private boolean isCanLoad = false;
	private Advert mAdvert;

	@Override
	public View onCreateMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_memory, null);
		return view;
	}

	@Override
	public void initView(View view) {
		initTopBarForBoth(view, getString(R.string.main_memory), R.drawable.search_grey, "", R.drawable.memory_more);
	}

	@Override
	public BasePresenter initPresenter() {
		return new MemoryPresenter();
	}

	@Override
	public void initData() {
		//设置颜色
		swipeToLoadLayout.setColorSchemeResources(
				R.color.yellow_DB,
				android.R.color.holo_blue_light,
				android.R.color.holo_red_light,
				android.R.color.holo_green_light);
		//线性
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		//下拉刷新
		swipeToLoadLayout.setOnRefreshListener(this);
		//去闪屏
		((DefaultItemAnimator) swipe_target.getItemAnimator()).setSupportsChangeAnimations(false);
		//设置udp监听
		IMClientManager.getInstance(mContext.getApplicationContext()).getTransDataListener().setImCallBack(this, ImConstant.IMMEMORY);
		//  获取Db中的圈子数据
		((MemoryPresenter) mPresenter).getGrouptFromDb(MainApplication.getUserId());
	}

	@Override
	public void onDestroy() {
		IMClientManager.getInstance(mContext.getApplicationContext()).getTransDataListener().removeCallback(ImConstant.IMMEMORY);
		super.onDestroy();
	}

	@Override
	@OnClick({R.id.iv_main_right, R.id.iv_main_right_one})
	public void onClick(View view) {
		int Id = view.getId();
		hideMemoryPopup();
		switch (Id) {
			case R.id.app_cancle:
				startAnimActivity(SearchGroupActivity.class);
				break;
			case R.id.iv_main_right:
				showMemoryPopup();
				break;
			case R.id.memory_writer_tv:
				//相册选取
				GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(299, false, true, false, 1, "", new ArrayList<PhotoInfo>(), "com.time.memory.view.activity.MainActivity"), null);
				break;
			case R.id.memory_createG_tv:
				startAnimActivity(CreateCircleActivity.class);
				break;
			case R.id.memory_addfriend_tv:
				startAnimActivity(FriendChooseActivity.class);
				break;
			case R.id.memory_sacn_tv:
				requestTakePermission();
				break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden && !isLoad) {
			((MemoryPresenter) mPresenter).getCirclesFromDb(MainApplication.getUserId());
		}
	}

	@Override
	public void reqCircle() {
		//请求圈子
		((MemoryPresenter) mPresenter).reqCircleInfos(getString(R.string.FSMEMORY), MainApplication.getUserId());
	}

	/**
	 * 获取广告数据
	 */
	@Override
	public void reqAdvert() {
		((MemoryPresenter) mPresenter).reqAdvert(getString(R.string.FSADVERT));
	}

	@Override
	public void reqStartCirlce(int position) {
		//激活圈子
		mMGroupList.get(position).setActiveFlg("0");
		adapter.notifyItemChanged(position);
	}

	/**
	 * 设置广告
	 *
	 * @param advert
	 */
	@Override
	public void setAdvert(Advert advert) {
		this.mAdvert.setAdvert(advert);
		if (adapter != null)
			adapter.notifyDataSetChanged();
		//  获取圈子数据
		((MemoryPresenter) mPresenter).reqCircleInfos(getString(R.string.FSMEMORY), MainApplication.getUserId());
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		((MemoryPresenter) mPresenter).reqCircleInfos(getString(R.string.FSMEMORY), MainApplication.getUserId());
	}

	@Override
	public void showSuccess() {
		//成功
		hideLoadingDialog();
		swipeToLoadLayout.setRefreshing(false);
	}

	@Override
	public void showFaild() {
		//失败
		hideLoadingDialog();
		swipeToLoadLayout.setRefreshing(false);
	}

	/**
	 * UDP记忆监听-记忆
	 */
	@Override
	public void onMemory(SW01RespVo reqVo) {
		((MemoryPresenter) mPresenter).upGroupInfo(reqVo, MainApplication.getUserId());
	}

	/**
	 * UDP记忆监听-转发
	 */
	@Override
	public void onForward(SW02RespVo reqVo) {
		((MemoryPresenter) mPresenter).upForward(reqVo, MainApplication.getUserId());
	}

	/**
	 * UDP记忆监听-邀请进群
	 *
	 * @param reqVo
	 */
	@Override
	public void onGroup(SG01RespVo reqVo) {
		((MemoryPresenter) mPresenter).addMGroup(MainApplication.getUserId(), reqVo);
	}

	/**
	 * UDP记忆监听-被从某个群移除
	 *
	 * @param reqVo
	 */
	@Override
	public void onRemoveGroup(SG03RespVo reqVo) {
		((MemoryPresenter) mPresenter).removeGroup(MainApplication.getUserId(), reqVo);
	}

	/**
	 * UDP记忆监听-修改圈子名
	 *
	 * @param reqVo
	 */
	@Override
	public void onUpGroup(SG04RespVo reqVo) {
		((MemoryPresenter) mPresenter).upGroup(MainApplication.getUserId(), reqVo);
	}

	/**
	 * UDP未读消息监听-点赞
	 */
	@Override
	public void onUnRead(SA01ReqVo reqVo) {
		((MemoryPresenter) mPresenter).unReadMsg(reqVo, MainApplication.getUserId());
	}

	/**
	 * UDP未读消息监听-评论
	 */
	@Override
	public void onUnReadComment(SA10ReqVo reqVo) {
		((MemoryPresenter) mPresenter).unReadComment(reqVo, MainApplication.getUserId());
	}

	/**
	 * UDP 更新圈子数据
	 * @param respVo
	 */
	@Override
	public void onAddmemory(SA20RespVo respVo) {
		((MemoryPresenter) mPresenter).upGroupAddMemory(respVo, MainApplication.getUserId());
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
		String mActive = (String) data;
		if (mActive.equals("0")) {
			Intent intent = new Intent();
			intent.putExtra("position", position);
			intent.putExtra("groupId", mMGroupList.get(position).getGroupId());
			//跳转新增页面
			if (index == 0) {
				intent.putExtra("type", "1");
				gotoActivity(intent, position);
			} else if (index == 1) {
				//跳转补充页面
				intent.putExtra("type", "0");
				gotoActivity(intent, position);
			} else if (index == 2) {
				//圈子成员列表
				if (position == 0) return;
				intent.setClass(mContext, GroupActivity.class);
				intent.putExtra("group", mMGroupList.get(position));
			}
			startActivity(intent);
		} else {
			//激活篇章
			((MemoryPresenter) mPresenter).reqStartCircle(getString(R.string.FSSTARTCIRCLE), MainApplication.getUserToken(), MainApplication.getUserId(), (String) data, position);
		}
	}

	@Override
	public void setAdapter(Advert advert, List<MGroup> MGroupList) {
		// 填充数据
		if (swipe_target != null) {
			isCanLoad = true;
			this.mAdvert = advert;
			if (adapter == null) {
				this.mMGroupList = MGroupList;
				isLoad = false;
				adapter = new BaseRecyclerHeaderAdapter(
						mMGroupList, R.layout.item_memory_m, MemoryHolder.class,
						mAdvert, R.layout.item_advert, AdvertHolder.class);
				adapter.setCallBack(this);
				adapter.setHeaderVisable(true);
				swipe_target.setAdapter(adapter);
			} else {
				this.mMGroupList.clear();
				this.mMGroupList.addAll(MGroupList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void setAdapter(List<MGroup> MGroupList) {
		// 填充数据
		if (swipe_target != null) {
			isCanLoad = true;
			this.mMGroupList.clear();
			this.mMGroupList.addAll(MGroupList);
			if (adapter != null)
				adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onCallback(Object data) {
		//跳转
		int index = (int) data;
		Intent intent = new Intent();
		if (index == -1) {
			//未读点赞评论页
			intent.setClass(mContext, UnReadForkActivity.class);
		} else if (index == 0) {
			intent.setClass(mContext, MyMemoryActivtiy.class);
		} else if (mMGroupList.get(index).getType() == 3) {
			intent.setClass(mContext, CreateCircleActivity.class);
		} else {
			//群
			if (TextUtils.isEmpty(mMGroupList.get(index).getGroupPw()))
				intent.setClass(mContext, MemoryGroupActivity.class);
			else {
				intent.setClass(mContext, LockActivity.class);
				intent.putExtra("isAddPwd", false);
				intent.putExtra("isGroup", true);
			}
		}
		if (index != -1)
			intent.putExtra("mGroup", mMGroupList.get(index));
		startAnimActivity(intent);
	}

	/**
	 * 广告
	 *
	 * @param data
	 * @param position
	 */
	@Override
	public void onDataCallBack(Object data, int position) {
		String url = (String) data;
		Intent intent = new Intent(mContext, WebActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", getString(R.string.app_name));
		startAnimActivity(intent);
	}

	/**
	 * 设置群人数
	 *
	 * @param num
	 */
	public void setGroupNum(String num) {
		if (mMGroupList != null && !mMGroupList.isEmpty()) {
			MGroup mGroup = mMGroupList.get(1);
			mMGroupList.get(1).setGroupCount(num);
			adapter.notifyDataSetChanged();
		}
	}

	private void gotoActivity(Intent intent, int position) {
		if (TextUtils.isEmpty(mMGroupList.get(position).getGroupPw()))
			intent.setClass(mContext, UnreadMemoryActivity.class);
		else {
			intent.setClass(mContext, LockActivity.class);
			intent.putExtra("isAddPwd", false);
			intent.putExtra("mGroup", mMGroupList.get(position));
			intent.putExtra("isGroup", true);
			intent.putExtra("cls", "com.time.memory.view.activity.memory.UnreadMemoryActivity");
		}
	}

	/**
	 * 显示弹窗
	 */
	private void showMemoryPopup() {
		//取得状态栏高度
		Rect frame = new Rect();
		getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int xOffset = DisplayUtils.dip2px(mContext, 10f); //
		int yOffset = frame.top + ivMainRight.getHeight() - 10;//
		// 包含popupwindow布局
		View popuView = LayoutInflater.from(getContext()).inflate(R.layout.item_popumemory, null);
		// 创建popupwindow
		popu = new PopupWindow(popuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 不设置的话窗体外不能取消
		popu.setBackgroundDrawable(new BitmapDrawable());
		//窗体外点击
		popu.setOutsideTouchable(true);
		//设置获焦点
		popu.setFocusable(true);
		// 设置popwindow出现和消失动画
		popu.setAnimationStyle(R.style.PopMenuAnimation);
		// 设置显示位置
		popu.showAtLocation(getView(), Gravity.RIGHT | Gravity.TOP, xOffset, yOffset);

		ButterKnife.findById(popuView, R.id.memory_writer_tv).setOnClickListener(this);
		ButterKnife.findById(popuView, R.id.memory_createG_tv).setOnClickListener(this);
		ButterKnife.findById(popuView, R.id.memory_addfriend_tv).setOnClickListener(this);
		ButterKnife.findById(popuView, R.id.memory_sacn_tv).setOnClickListener(this);
	}

	/**
	 * 隐藏Popup
	 */
	private void hideMemoryPopup() {
		if (popu != null) {
			if (popu.isShowing())
				popu.dismiss();
		}
	}

	/**
	 * 拍照获取权限
	 */
	@AfterPermissionGranted(ReqConstant.REQUEST_CODE_CAMERA)
	protected void requestTakePermission() {
		if (EasyPermissions.hasPermissions(mContext, Manifest.permission.CAMERA)) {
			startAnimActivity(CaptureActivity.class);
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tipes_takephoto),
					ReqConstant.REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
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
		if (resultCode == ReqConstant.REQUEST_CODE_CAMERA) {
			requestTakePermission();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// EasyPermissions handles the request result.
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		//请求圈子
		if (isCanLoad) {
			((MemoryPresenter) mPresenter).getCircle(MainApplication.getUserId());
			((MemoryPresenter) mPresenter).getUnReadNum(getString(R.string.FSUNREADNUM), mMGroupList);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (isCanLoad) {
				((MemoryPresenter) mPresenter).getUnReadNum(getString(R.string.FSUNREADNUM), mMGroupList);
			}
		}
	}
}
