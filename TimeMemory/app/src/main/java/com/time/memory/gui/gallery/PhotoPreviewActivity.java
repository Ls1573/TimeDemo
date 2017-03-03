package com.time.memory.gui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.Callback;
import com.time.memory.core.net.ExecutorManager;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.gallery.adapter.PhotoPreviewAdapter;
import com.time.memory.gui.gallery.widget.GFViewPager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:预览
 * @date 2016/9/30 16:53
 */
public class PhotoPreviewActivity extends PhotoBaseActivity implements ViewPager.OnPageChangeListener, Callback {

	private static final String TAG = "PhotoPreviewActivity";
	public static final String PHOTO_LIST = "photo_list";

	public static final int PHOTOPREVIEWACTIVITY = 1001;
	static final int HANDLER_REFRESH_LIST_EVENT = 101;
	static final int HANDLER_SELECT_LIST_EVENT = 102;

	@Bind(R.id.iv_back)
	ImageView mIvBack;
	@Bind(R.id.vp_pager)
	GFViewPager mVpPager;//内容显示区域
	@Bind(R.id.app_eddit)
	ImageView app_eddit;//编辑
	@Bind(R.id.app_submit)
	TextView app_submit;//提交
	@Bind(R.id.photo_submit_rl)
	RelativeLayout photo_submit_rl;//提交

	private ArrayList<PhotoInfo> mPhotoList;
	private ArrayList<PhotoInfo> selectList;//被选中
	private PhotoPreviewAdapter mPhotoPreviewAdapter;

	private boolean isWatch;//观看模式
	private int position;//当前位置
	private int curClick;//当前点击位置
	private int curSize;//当前数量
	private int removeSum;//拿掉项

	private boolean isOld;//返回位置
	private boolean isAddMore;//补充
	private boolean isMore;//继续
	private boolean isWriter;//写记忆
	private boolean isSelect;//列表页进入的
	private int state;//隐私权限
	private String groupId;//群Id

	protected ExecutorService threadPool;    // 线程池

	private int maxSize;//最大选择数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gf_activity_photo_preview);
		ButterKnife.bind(this);
		setListener();
		curClick = getIntent().getIntExtra("curClick", 0);
		isWatch = getIntent().getBooleanExtra("onlyWatch", false);
		isSelect = getIntent().getBooleanExtra("isSelect", false);
		//如果数据集太大->intent 会超出限制,跳过去
		if (isSelect)
			mPhotoList = PhotoSelectActivity.getmCurPhotoList();
		else
			mPhotoList = getIntent().getParcelableArrayListExtra(PHOTO_LIST);
		getOpr();
	}

	/**
	 * 设置提交按钮颜色
	 */
	private void setCheckColor() {
		if (curSize > 0) {
			app_submit.setTextColor(getResources().getColor(R.color.grey_DA));
			app_submit.setEnabled(true);
		} else {
			app_submit.setTextColor(getResources().getColor(R.color.black_83));
			app_submit.setEnabled(false);
		}
		app_eddit.setSelected(mPhotoList.get(position).isActicted());
	}

	/**
	 * 观看?选取模式
	 */
	private void getOpr() {
		if (!isWatch) {
			//选取模式
			getData();
			getThreadPool();
			curSize = getIntent().getIntExtra("curSize", 0);
			filterList();
		} else {
			//仅观看
			app_eddit.setVisibility(View.GONE);
			mPhotoPreviewAdapter = new PhotoPreviewAdapter(PhotoPreviewActivity.this, mPhotoList, isWatch);
			mVpPager.setAdapter(mPhotoPreviewAdapter);
			mVpPager.setCurrentItem(curClick);
			mVpPager.setOffscreenPageLimit(1);
			photo_submit_rl.setVisibility(View.GONE);
		}
	}

	//获取数据
	private void getData() {
		maxSize = GalleryFinal.getFunctionConfig().getMaxSize();
		isOld = GalleryFinal.getFunctionConfig().isOld();
		maxSize = GalleryFinal.getFunctionConfig().getMaxSize();
		isWriter = GalleryFinal.getFunctionConfig().isWriter();
		state = GalleryFinal.getFunctionConfig().getState();
		groupId = GalleryFinal.getFunctionConfig().getId();
		isAddMore = GalleryFinal.getFunctionConfig().isAddMore();
		isMore = GalleryFinal.getFunctionConfig().isMore();
	}

	private void setListener() {
		mVpPager.addOnPageChangeListener(this);
	}

	// 获取线程池
	private void getThreadPool() {
		threadPool = ExecutorManager.getInstance();
	}

	/**
	 * 过滤图片
	 */
	private void filterList() {
		threadPool.submit(new Runnable() {
			@Override
			public void run() {
				//遍历删除
				Iterator<PhotoInfo> stuIter = mPhotoList.iterator();
				while (stuIter.hasNext()) {
					PhotoInfo info = stuIter.next();
					if (info.isTitle()) {
						stuIter.remove();
						removeSum++;
					}
					if (info.isClicked()) {
						curClick = curClick - removeSum;
						info.setIsClicked(false);
					}
				}
				mHanlder.sendEmptyMessage(HANDLER_REFRESH_LIST_EVENT);
			}
		});
	}

	/**
	 * 获取新的图片集合
	 */
	private void getListEdit(List<PhotoInfo> photoList) {
		if (isSelect)
			selectList = PhotoSelectActivity.getmSelPhotoList();
		if (selectList == null) selectList = new ArrayList<>();
//		selectList.clear();
		for (PhotoInfo photoInfo : photoList) {
			if (photoInfo.isActicted()) {
				if (!selectList.contains(photoInfo))
					selectList.add(photoInfo);
			}
		}
	}

	private Handler mHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == HANDLER_REFRESH_LIST_EVENT) {
				setAdapter();
			}
		}
	};

	private void setAdapter() {
		mPhotoPreviewAdapter = new PhotoPreviewAdapter(PhotoPreviewActivity.this, mPhotoList, isWatch);
		mPhotoPreviewAdapter.setCallback(this);
		mVpPager.setAdapter(mPhotoPreviewAdapter);
		mVpPager.setCurrentItem(curClick);
		setCheckColor();
		photo_submit_rl.setVisibility(View.VISIBLE);
	}

	@Override
	protected void takeResult(PhotoInfo info) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageSelected(int position) {
		this.position = position;
		app_eddit.setSelected(mPhotoList.get(position).isActicted());
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	@OnClick({R.id.iv_back, R.id.app_eddit, R.id.app_submit})
	public void onClick(View view) {
		if (view.getId() == R.id.iv_back) {
			//返回
			setResult();
		} else if (view.getId() == R.id.app_submit) {
			//确定
			getListEdit(mPhotoList);
			if (selectList.size() > 0) {
				if (!GalleryFinal.getFunctionConfig().isOld()) {
					if (isAddMore) {
						//补充记忆
						resultSuppory(selectList, state, groupId);
					} else {
						//写记忆
						resultWriter(selectList, state, groupId);
					}
				} else if (isMore) {
					//原路返回
					resultData(selectList);
				} else {
					//原路返回
					resultData(selectList);
				}
			} else {
				showShortToast("请选择照片");
			}
		} else {
			//编辑
			editPhoto();
		}
	}

	private void convertPhoto() {
		for (PhotoInfo photoInfo : mPhotoList) {
			if (TextUtils.isEmpty(photoInfo.getPhotoPath()))
				break;
			if (photoInfo.getPhotoPath().contains("details"))
				photoInfo.getPhotoPath().replace("details", "onepicture");
		}
	}

	/**
	 * 编辑
	 */
	private void editPhoto() {
		//当前激活状态
		boolean isActicted = mPhotoList.get(position).isActicted();
		if (isActicted) {
			mPhotoList.get(position).setIsActicted(!isActicted);
			app_eddit.setSelected(mPhotoList.get(position).isActicted());
			curSize--;
		} else {
			if (curSize < maxSize) {
				mPhotoList.get(position).setIsActicted(!isActicted);
				app_eddit.setSelected(mPhotoList.get(position).isActicted());
				curSize++;
			} else {
				showShortToast("已经是最大数了");
			}
		}
		setCheckColor();
	}

	/**
	 * 返回
	 */
	private void setResult() {
		Intent data = new Intent();
		if (!isSelect)
			data.putParcelableArrayListExtra(PHOTO_LIST, mPhotoList);
		setResult(PHOTOPREVIEWACTIVITY, data);
		finish();
	}

	@Override
	public void onCallback(Object data) {
		//返回
		setResult();
	}

	@Override
	public void onBackPressed() {
		//返回
		setResult();
	}

	@Override
	protected void onDestroy() {
		mHanlder.removeCallbacksAndMessages(null);
		super.onDestroy();
	}


}
