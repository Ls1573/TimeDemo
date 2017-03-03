package com.time.memory.gui.gallery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.core.permission.AfterPermissionGranted;
import com.time.memory.core.permission.EasyPermissions;
import com.time.memory.entity.PhotoFolderInfo;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.DividerItemDecoration;
import com.time.memory.gui.gallery.adapter.PhotoRecycleAdapter;
import com.time.memory.gui.gallery.utils.PhotoTools;
import com.time.memory.util.CLog;
import com.time.memory.util.DialogUtils;
import com.time.memory.view.adapter.BaseRecyclerAdapter;
import com.time.memory.view.holder.PFolderHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:图片选择器
 * @date 2016/9/27 14:57
 */
public class PhotoSelectActivity extends PhotoBaseActivity implements View.OnClickListener, PhotoRecycleAdapter.OnPhotoCallback, AdapterCallback {

	private static final String TAG = "PhotoSelectActivity";
	private final int HANLDER_TAKE_PHOTO_EVENT = 100;
	private final int HANDLER_REFRESH_LIST_EVENT = 102;
	private final int HANDLER_REFRESH_FOLDERS_EVENT = 104;
	private final int HANDLER_REFRESH_FOLDERLISTS_EVENT = 106;
	private static final int TRANSLATE_DURATION = 200;

	RecyclerView swipe_target;//图片内容
	RecyclerView photoDFile;//图片文件夹
	ImageView mIvBack;//返回
	ImageView ivMainFolder;//指示
	TextView mTvChooseCount;//图片数
	TextView mTVMoreCount;//追加数
	TextView mainTitle;//指示
	TextView folderDesc;//文件夹指示
	RelativeLayout appSubmit;//提交
	TextView mTvEmptyView;//提示书
	TextView photo_submit_tv;//提示文字
	LinearLayout mLlTitle;//选择相册
	RelativeLayout tv_main_title_rL;

	private List<PhotoFolderInfo> mAllPhotoFolderList;//文件夹
	private static ArrayList<PhotoInfo> mCurPhotoList;
	private PhotoRecycleAdapter adapter;//图片
	private BaseRecyclerAdapter fAdapter;//文件夹

	private static ArrayList<PhotoInfo> mSelectPhotoList;
	protected ExecutorService threadPool;    // 线程池
	private boolean isOld;//返回位置
	private boolean isAddMore;//补充
	private boolean isMore;//继续
	private boolean isWriter;//写记忆
	private int maxSize;//最大选择数
	private int state;//隐私权限
	private Class clas;
	private String groupId;//群Id

	private int cPosition;//当前选择的文件夹下标

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("selectPhotoMap", mSelectPhotoList);
	}

	@Override
	protected void onDestroy() {
		try {
			if (mSelectPhotoList != null) {
				mSelectPhotoList.clear();
				mSelectPhotoList = null;
			}
			if (mCurPhotoList != null) {
				mCurPhotoList.clear();
				mCurPhotoList = null;
			}
			if (mAllPhotoFolderList != null) {
				mAllPhotoFolderList.clear();
				mAllPhotoFolderList = null;
			}
			if (adapter != null) {
				adapter = null;
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mSelectPhotoList = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra("selectPhotoMap");
	}

	private Handler mHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HANLDER_TAKE_PHOTO_EVENT) {
				PhotoInfo photoInfo = (PhotoInfo) msg.obj;
				takeRefreshGallery(photoInfo);
				refreshSelectCount();
			} else if (msg.what == HANDLER_REFRESH_LIST_EVENT) {
				refreshSelectCount();
				adapter.notifyDataSetChanged();
				mTvEmptyView.setVisibility(View.GONE);
				swipe_target.setEnabled(true);
				mLlTitle.setEnabled(true);
				CLog.e(TAG, "<--------数据更新完成----->");
			} else if (msg.what == HANDLER_REFRESH_FOLDERS_EVENT) {
				//刷新Title
				String title = (String) msg.obj;
				folderDesc.setText(title);
				folderDesc.setVisibility(View.VISIBLE);
				setTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

			} else if (msg.what == HANDLER_REFRESH_FOLDERLISTS_EVENT) {
				fAdapter.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (GalleryFinal.getFunctionConfig() == null) {
			resultFailureDelayed(getString(R.string.please_reopen_gf), true);
		} else {
			setContentView(R.layout.gf_activity_photo_select);
			getThreadPool();
			//获取数据
			getDate();
			//获取控件
			getView();
			//设置显示布局
			setLayout();
			mSelectPhotoList = new ArrayList<>();
			//写记忆
			if (isWriter) {
				photo_submit_tv.setText(getString(R.string.photo_writer));
			} else {
				//确定
				photo_submit_tv.setText(getString(R.string.app_confirm));
			}

//			mPhotoTargetFolder = null;
			mAllPhotoFolderList = new ArrayList<>();

			mCurPhotoList = new ArrayList<>();
			swipe_target.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));

			photoDFile.setLayoutManager(new LinearLayoutManager(this));
			photoDFile.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, ContextCompat.getColor(this, R.color.grey_divider), 1));

			//文件夹
			if (fAdapter == null) {
				fAdapter = new BaseRecyclerAdapter(mAllPhotoFolderList, R.layout.item_photo_folder, PFolderHolder.class);
				fAdapter.setCallBack(this);
				photoDFile.setAdapter(fAdapter);
			}

			swipe_target.setAdapter((adapter = new PhotoRecycleAdapter(this, mCurPhotoList, mSelectPhotoList, maxSize)));
			adapter.setOnPhotoCallback(this);
			//去掉动画
			((DefaultItemAnimator) swipe_target.getItemAnimator()).setSupportsChangeAnimations(false);
			((DefaultItemAnimator) photoDFile.getItemAnimator()).setSupportsChangeAnimations(false);
			setListener();
			refreshSelectCount();
			requestGalleryPermission();
		}
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		//正在显示->隐藏
		if (photoDFile.getVisibility() == View.VISIBLE) {
			photoDFile.setVisibility(View.GONE);
			photoDFile.startAnimation(createTranslationOutAnimation());
			ivMainFolder.startAnimation(createRoateAnOutimation());
			folderDesc.setVisibility(View.VISIBLE);
			setTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		} else {
			showExitDialog();
		}
	}

	// 获取线程池
	private void getThreadPool() {
		threadPool = ExecutorManager.getInstance();
	}

	//获取数据
	private void getDate() {
		isOld = GalleryFinal.getFunctionConfig().isOld();
		maxSize = GalleryFinal.getFunctionConfig().getMaxSize();
		isWriter = GalleryFinal.getFunctionConfig().isWriter();
		state = GalleryFinal.getFunctionConfig().getState();
		groupId = GalleryFinal.getFunctionConfig().getId();
		isAddMore = GalleryFinal.getFunctionConfig().isAddMore();
		isMore = GalleryFinal.getFunctionConfig().isMore();
		clas = GalleryFinal.getFunctionConfig().getClass();
	}

	//获取控件
	private void getView() {
		swipe_target = (RecyclerView) findViewById(R.id.swipe_target);
		photoDFile = (RecyclerView) findViewById(R.id.photo_rv);
		mIvBack = (ImageView) findViewById(R.id.iv_back);
		ivMainFolder = (ImageView) findViewById(R.id.iv_main_folder);

		folderDesc = (TextView) findViewById(R.id.tv_main_folder);
		mainTitle = (TextView) findViewById(R.id.tv_main_title);
		mTvChooseCount = (TextView) findViewById(R.id.tv_choose_count);
		mTVMoreCount = (TextView) findViewById(R.id.tv_more_count);
		mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
		photo_submit_tv = (TextView) findViewById(R.id.photo_submit_tv);
		appSubmit = (RelativeLayout) findViewById(R.id.app_submit);
		mLlTitle = (LinearLayout) findViewById(R.id.ll_title);
		tv_main_title_rL = (RelativeLayout) findViewById(R.id.tv_main_title_rL);
	}

	/**
	 * 设置布局规范
	 */
	private void setLayout() {
		if (isMore) {
			//只显示6张->追加
			appSubmit.setVisibility(View.GONE);
			mTvChooseCount.setVisibility(View.GONE);
			mTVMoreCount.setVisibility(View.VISIBLE);
		} else {
			//>6张 ->写记忆
			appSubmit.setVisibility(View.VISIBLE);
			mTvChooseCount.setVisibility(View.VISIBLE);
			mTVMoreCount.setVisibility(View.GONE);
		}
	}

	private void setListener() {
		mIvBack.setOnClickListener(this);
		appSubmit.setOnClickListener(this);
		mTVMoreCount.setOnClickListener(this);
		tv_main_title_rL.setOnClickListener(this);
	}

	protected void deleteSelect(int photoId) {
		try {
			for (Iterator<PhotoInfo> iterator = mSelectPhotoList.iterator(); iterator.hasNext(); ) {
				PhotoInfo info = iterator.next();
				if (info != null && info.getPhotoD() == photoId) {
					iterator.remove();
					break;
				}
			}
		} catch (Exception e) {
		}
		refreshAdapter();

		ApplicationInfo applicationInfo = new ApplicationInfo();
	}

	private void refreshAdapter() {
		mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_LIST_EVENT, 60);
	}

	private void refreshFloderAdapter() {
		mHanlder.sendEmptyMessageDelayed(HANDLER_REFRESH_FOLDERLISTS_EVENT, 60);
	}

	private void refreshTitle(String title) {
		Message msg = mHanlder.obtainMessage();
		msg.what = HANDLER_REFRESH_FOLDERS_EVENT;
		msg.obj = title;
		mHanlder.sendMessageDelayed(msg, 100);
	}

	protected void takeRefreshGallery(PhotoInfo photoInfo, boolean selected) {
		if (isFinishing() || photoInfo == null) {
			return;
		}
		Message message = mHanlder.obtainMessage();
		message.obj = photoInfo;
		message.what = HANLDER_TAKE_PHOTO_EVENT;
		mSelectPhotoList.add(photoInfo);
		mHanlder.sendMessageDelayed(message, 100);
	}

	/**
	 * 解决在5.0手机上刷新Gallery问题，从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
	 * adapter的getCount与list.size不一致，这里用了延迟刷新数据
	 *
	 * @param photoInfo
	 */
	private void takeRefreshGallery(PhotoInfo photoInfo) {
		mCurPhotoList.add(0, photoInfo);
		adapter.notifyDataSetChanged();

		//添加到集合中
		List<PhotoInfo> photoInfoList = mAllPhotoFolderList.get(0).getPhotoList();
		if (photoInfoList == null) {
			photoInfoList = new ArrayList<>();
		}
		photoInfoList.add(0, photoInfo);
		mAllPhotoFolderList.get(0).setPhotoList(photoInfoList);

		String folderA = new File(photoInfo.getPhotoPath()).getParent();
		for (int i = 1; i < mAllPhotoFolderList.size(); i++) {
			PhotoFolderInfo folderInfo = mAllPhotoFolderList.get(i);
			String folderB = null;
			if (!TextUtils.isEmpty(photoInfo.getPhotoPath())
					) {
				folderB = new File(photoInfo.getPhotoPath()).getParent();
			}
			if (TextUtils.equals(folderA, folderB)) {
				List<PhotoInfo> list = folderInfo.getPhotoList();
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(0, photoInfo);
				folderInfo.setPhotoList(list);
				if (list.size() == 1) {
					folderInfo.setCoverPhoto(photoInfo);
				}
			}
		}
	}

	@Override
	protected void takeResult(PhotoInfo photoInfo) {
		Message message = mHanlder.obtainMessage();
		message.obj = photoInfo;
		message.what = HANLDER_TAKE_PHOTO_EVENT;

		if (!GalleryFinal.getFunctionConfig().isMutiSelect()) { //单选
			mSelectPhotoList.clear();
			mSelectPhotoList.add(photoInfo);
			ArrayList<PhotoInfo> list = new ArrayList<>();
			list.add(photoInfo);
//			resultData(list);
			mHanlder.sendMessageDelayed(message, 100);
		} else {//多选
			mSelectPhotoList.add(photoInfo);
			mHanlder.sendMessageDelayed(message, 100);
		}
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.iv_back) {
			showExitDialog();
		} else if (id == R.id.app_submit) {
			//确定
			if (mSelectPhotoList.size() > 0) {
				if (!GalleryFinal.getFunctionConfig().isOld()) {
					if (isAddMore) {
						//补充记忆
						resultSuppory(mSelectPhotoList, state, groupId);
					} else {
						//写记忆
						resultWriter(mSelectPhotoList, state, groupId);
					}
				} else {
					//原路返回
					resultData(mSelectPhotoList);
				}
			} else {
				showShortToast("请选择照片");
			}
		} else if (id == R.id.tv_more_count) {
			//继续
			if (isMore) {
				//原路返回
				resultData(mSelectPhotoList);
			}
		} else if (id == R.id.tv_main_title_rL) {
			//点击了Title
			setFolderlist();
		}
	}

	@Override
	public void onAll(boolean isAll) {
		//全选/反选
		refreshSelectCount();
	}

	@Override
	public void onSingle(boolean isActicted, int positoin) {
		//单选
		refreshSelectCount();
	}

	@Override
	public void onPreView(int positoin) {
		//预览视图
		CLog.e(TAG, "positoin:" + positoin);
		mCurPhotoList.get(positoin).setIsClicked(true);
		Intent intent = new Intent(this, PhotoPreviewActivity.class);
		intent.putExtra("curClick", positoin);
		intent.putExtra("curSize", mSelectPhotoList.size());
		intent.putExtra("isSelect", true);
		try {
			startActivityForResult(intent, 1001);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMaxSize() {
		//已经是最大数了
		showShortToast("已经是最大数了");
	}

	/**
	 * 刷新图片显示数
	 */
	public void refreshSelectCount() {
		mTvChooseCount.setText(String.valueOf(mSelectPhotoList.size()));
		mTVMoreCount.setText(String.format(getString(R.string.memory_more_tv), String.valueOf(mSelectPhotoList.size())));
	}


	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//权限通过
		getPhotos();
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		//权限拒绝
	}

	/**
	 * 获取所有图片
	 */
	@AfterPermissionGranted(GalleryFinal.PERMISSIONS_CODE_GALLERY)
	private void requestGalleryPermission() {
		if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
			getPhotos();
		} else {
			// Ask for one permission
			EasyPermissions.requestPermissions(this, getString(R.string.permissions_tips_gallery),
					GalleryFinal.PERMISSIONS_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
		}
	}

	/**
	 * 获取图片
	 */
	private void getPhotos() {
		mTvEmptyView.setText(R.string.waiting);
		mLlTitle.setEnabled(false);
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				CLog.e(TAG, "*******************开始捕获***************");
				//获取所有文件夹及文件
				List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(PhotoSelectActivity.this, mSelectPhotoList);
				CLog.e(TAG, "******************图片1级捕获完成***************");
				mAllPhotoFolderList.clear();
				mAllPhotoFolderList.addAll(allFolderList);
				//给封面和数量
				for (PhotoFolderInfo info : mAllPhotoFolderList) {
					info.setPhotoSize(info.getPhotoList().size());
					info.setPhotoThumbl(info.getPhotoList().get(0).getPhotoPath());
				}
				if (mAllPhotoFolderList.size() > 0) {
					//0-->全部图片 剔除
					mCurPhotoList.clear();
					cPosition = 0;
					//图片集合不为空
					if (mAllPhotoFolderList.get(cPosition).getPhotoList() != null) {
						getList(mAllPhotoFolderList.get(cPosition).getPhotoList());
					}
					//设置头
					refreshTitle(getPhotoFileTitle(cPosition));
					refreshAdapter();
				}
				CLog.e(TAG, "*******************图片2级捕获完成***************");
			}
		});
	}

	/**
	 * 大图预览返回后的数据筛选
	 */
	private void getPhotoEdit(final List<PhotoInfo> mPhotoList) {
		mTvEmptyView.setText(R.string.waiting);
		mLlTitle.setEnabled(false);
		mCurPhotoList.clear();
//		mSelectPhotoList.clear();
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				//刷新图片
				CLog.e(TAG, "*******************开始捕获***************");
				getListEdit(mPhotoList);
				refreshAdapter();
				CLog.e(TAG, "*******************捕获完成***************");
			}
		});
	}

	/**
	 * 选择文件夹
	 */
	private void setFolderlist() {
		swipe_target.stopScroll();
		if (mAllPhotoFolderList != null && !mAllPhotoFolderList.isEmpty()) {
			if (photoDFile.getVisibility() != View.VISIBLE) {
				//弹出来->显示
				photoDFile.setVisibility(View.VISIBLE);
				photoDFile.startAnimation(createTranslationInAnimation());
				ivMainFolder.startAnimation(createRoateAnimation());

				folderDesc.setVisibility(View.GONE);
				setTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

			} else {
				//正在显示->隐藏
				photoDFile.setVisibility(View.GONE);
				photoDFile.startAnimation(createTranslationOutAnimation());
				ivMainFolder.startAnimation(createRoateAnOutimation());

				folderDesc.setVisibility(View.VISIBLE);
				setTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
			}
		}
	}

	/**
	 * 设置Title
	 */
	private String getPhotoFileTitle(int position) {
		if (mAllPhotoFolderList != null && !mAllPhotoFolderList.isEmpty()) {
			String title = mAllPhotoFolderList.get(position).getFolderName();
			if (!TextUtils.isEmpty(title))
				return title;
			else
				return getString(R.string.mine_photo);
		} else {
			return getString(R.string.mine_photo);
		}
	}

	/**
	 * 获取新的图片集合
	 * start有两种:
	 * 1:是Title的start-->指示第一个Item的位置;
	 * 2:是Item的start-->指示所在Title的位置;
	 */
	private void getList(List<PhotoInfo> photoList) {
		int start = 0;
		Collections.sort(photoList, new YMComparator());
		for (int i = 0; i < photoList.size(); i++) {
			if (i == 0) {
				//第一个
				mCurPhotoList.add(new PhotoInfo(true, photoList.get(0).getDate(), 1));
				mCurPhotoList.add(photoList.get(0));
				mCurPhotoList.get(1).setStart(0);
			} else {
				//当前的
				PhotoInfo curInfo = photoList.get(i);
				//上一个
				PhotoInfo preInfo = mCurPhotoList.get(mCurPhotoList.size() - 1);
				if (curInfo.getDate().equals(preInfo.getDate())) {
					//日期相同
					curInfo.setStart(start);
					curInfo.setIsActicted(false);
					mCurPhotoList.get(start).setEnd(mCurPhotoList.size() - 1);
					mCurPhotoList.add(curInfo);
				} else {
					//日期不同时,将提示头提出来
					mCurPhotoList.get(start).setEnd(mCurPhotoList.size());
					start = mCurPhotoList.size();
					mCurPhotoList.add(new PhotoInfo(true, curInfo.getDate(), start + 1));
					curInfo.setStart(start);
					mCurPhotoList.add(curInfo);
				}
			}
		}
		CLog.e(TAG, "<------------------------------photoList-------------------------->: " + photoList.size());
	}

	/**
	 * 获取新的图片集合
	 */
	private void getListEdit(List<PhotoInfo> photoList) {
		int start = 0;
		PhotoInfo curInfo;
		PhotoInfo preInfo;
		Collections.sort(photoList, new YMComparator());
		int size = photoList.size();
		for (int i = 0; i < size; i++) {
			//TODO 可以默认全选
			if (i == 0) {
				//第一个
				mCurPhotoList.add(new PhotoInfo(true, photoList.get(0).getDate(), 1, true));
				mCurPhotoList.add(photoList.get(0));
				mCurPhotoList.get(1).setStart(0);
			} else {
				//当前的
				curInfo = photoList.get(i);
				//上一个
				preInfo = mCurPhotoList.get(mCurPhotoList.size() - 1);
				if (curInfo.getDate().equals(preInfo.getDate())) {
					//日期相同
					curInfo.setStart(start);
					mCurPhotoList.get(start).setEnd(mCurPhotoList.size() - 1);
					mCurPhotoList.add(curInfo);
				} else {
					//日期不同时,将提示头提出来
					mCurPhotoList.get(start).setEnd(mCurPhotoList.size());
					start = mCurPhotoList.size();
					mCurPhotoList.add(new PhotoInfo(true, curInfo.getDate(), start + 1, true));
					curInfo.setStart(start);
					mCurPhotoList.add(curInfo);
				}
			}
			//TODO 当前项不是激活状态时，可以将它对应的头参数改变 ^加入到集合中
			if (photoList.get(i).isActicted()) {
				if (!mSelectPhotoList.contains(photoList.get(i)))
					mSelectPhotoList.add(photoList.get(i));
			} else {
				mSelectPhotoList.remove(photoList.get(i));
				mCurPhotoList.get(start).setIsAll(false);
			}
		}
	}

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		if (GalleryFinal.getCoreConfig() != null &&
				GalleryFinal.getCoreConfig().getImageLoader() != null) {
			GalleryFinal.getCoreConfig().getImageLoader().clearMemoryCache();
		}
	}

	public static ArrayList<PhotoInfo> getmCurPhotoList() {
		return mCurPhotoList;
	}

	public static ArrayList<PhotoInfo> getmSelPhotoList() {
		return mSelectPhotoList;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == PhotoPreviewActivity.PHOTOPREVIEWACTIVITY) {
			ArrayList<PhotoInfo> mPhotoList = new ArrayList<>();
			mPhotoList.addAll(mCurPhotoList);
			if (mPhotoList != null && !mPhotoList.isEmpty()) {
				getPhotoEdit(mPhotoList);
			}
		}
	}


	/**
	 * 获取文件夹图片
	 */
	private void getFloderPhotos(int position) {
		photoDFile.setVisibility(View.GONE);
		photoDFile.startAnimation(createTranslationOutAnimation());
		ivMainFolder.startAnimation(createRoateAnOutimation());

		folderDesc.setText(mAllPhotoFolderList.get(position).getFolderName());
		folderDesc.setVisibility(View.VISIBLE);
		setTitleGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

		if (cPosition == position) {
			return;
		}
		mTvEmptyView.setText(R.string.waiting);
		mLlTitle.setEnabled(false);
		cPosition = position;

		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				//图片集合不为空
				if (mAllPhotoFolderList.get(cPosition).getPhotoList() != null) {
					//0-->当前图片全部清空 剔除
					mCurPhotoList.clear();
					//获取新的图片集合
					getList(mAllPhotoFolderList.get(cPosition).getPhotoList());
					//比较出已选择的图片进行赋值
				}
				refreshAdapter();
			}
		});
	}

	/**
	 * 放弃回退
	 */
	private void showExitDialog() {
		DialogUtils.request(PhotoSelectActivity.this, "是否放弃选择图片", "放弃", "取消", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isExit = (boolean) data;
				if (isExit) {
					finish();
				}
			}
		});
	}

	/**
	 * 相册选择返回了
	 *
	 * @param data
	 */
	@Override
	public void onCallback(Object data) {
		int position = (int) data;
		getFloderPhotos(position);
	}

	@Override
	public void onDataCallBack(Object data, int position) {

	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {

	}

	@Override
	public void onDataCallBack(Object data, int position, int index, int tag) {
	}

	@Override
	public void onLongCallBack(Object data, int position) {

	}

	/**
	 * 设置文字显示
	 * Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL
	 * Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM
	 */
	private void setTitleGravity(int gravity) {
		mainTitle.setGravity(gravity);
	}

	// 创建进入动画
	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				-1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}


	// 创建退出位移动画
	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, -1);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	// 创建逆时针旋转动画
	private Animation createRoateAnimation() {
		RotateAnimation animation = new RotateAnimation(0f, -180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(TRANSLATE_DURATION);
		animation.setFillAfter(true);
		return animation;
	}

	// 创建顺时针旋转动画
	private Animation createRoateAnOutimation() {
		RotateAnimation animation = new RotateAnimation(-180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(TRANSLATE_DURATION);
		animation.setFillAfter(true);
		return animation;
	}


}
