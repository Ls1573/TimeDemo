package com.time.memory.view.activity.memory;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.WriterMemory;
import com.time.memory.gui.DatePickerSheet;
import com.time.memory.gui.WriterEditText;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.gui.sixGridImage.SixGridImageWriterView;
import com.time.memory.presenter.AddMemoryPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.LocationActivity;
import com.time.memory.view.impl.IAddMemoryView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:补充记忆(追加)
 * @date 2016/9/23 16:45
 */
public class SupportActivity extends BaseActivity implements IAddMemoryView, TextWatcher, TextView.OnEditorActionListener {
	protected static final String TAG = "SupportActivity";

	private final int REQUEST_CODE_GALLERY = 101;
	private static final int TRANSLATE_DURATION = 120;

	@Bind(R.id.writer_grid)
	SixGridImageWriterView writerGrid;//6宫图
	@Bind(R.id.writer_et)
	WriterEditText writerEt;//文件描述
	@Bind(R.id.writer_date_tv)
	TextView writerDateTv;//日期
	@Bind(R.id.writer_loc_tv)
	TextView writerLocTv;//地址
	@Bind(R.id.tag_et)
	EditText tagEt;//输入框
	@Bind(R.id.tag_input_rl)
	RelativeLayout tag_input_rl;//标签外框


	private int state;//权限
	private int size;
	private String Id;//对应群Id
	private String userId;//对应userId
	private String memoryId;//对应memoryId
	private String memorySourceId;//Id对应源Id
	private WriterMemory writerMemory;//当前补充记忆的信息

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_supportmemory);
	}

	@Override
	public BasePresenter initPresenter() {
		return new AddMemoryPresenter();
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_supportmemory), R.drawable.image_back, getString(R.string.app_preview), -1);
	}

	@Override
	public void initData() {
		initSixGridImage();
		writerEt.addTextChangedListener(this);
		tagEt.setOnEditorActionListener(this);
		//获取数据
		ArrayList<PhotoInfo> photoList = getIntent().getParcelableArrayListExtra("photos");
		state = getIntent().getIntExtra("state", 0);
		Id = getIntent().getStringExtra("Id");
		userId = getIntent().getStringExtra("userId");
		memoryId = getIntent().getStringExtra("memoryId");
		memorySourceId = getIntent().getStringExtra("memorySourceId");
		//数据装换
		((AddMemoryPresenter) mPresenter).getMessage(photoList);
	}

	@OnClick({R.id.writer_date_tv, R.id.writer_loc_tv, R.id.tv_main_right})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.writer_date_tv:
				// 选择日历
				((AddMemoryPresenter) mPresenter).convertItemDate(writerMemory.getDate().toString().trim());
				break;
			case R.id.writer_loc_tv:
				//选择地址
//				showAddTag(writerMemory.getAddress());
				Intent intent = new Intent(mContext, LocationActivity.class);
				startActivityForResult(intent, ReqConstant.REQUEST_CODE_ADDRESS);
				break;
			case R.id.tv_main_right:
				//预览
				memoryPreview();
				break;
			case R.id.app_cancle:
				//退出
				exitDialog();
		}
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		writerMemory.setDesc(s.toString().trim());
	}

	/**
	 * 图片显示
	 */
	private void initSixGridImage() {
		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
				//显示
				if (size == 1) {
					Picasso.with(mContext).load("file://" + entity.getPhotoPath()).resize(800, 600).centerCrop().into(imageView);
				} else {
					Picasso.with(mContext).load("file://" + entity.getPhotoPath()).resize(400, 400).centerCrop().into(imageView);
				}
			}

			@Override
			protected void onDeleteClick(int position) {
				//删除图片
				CLog.e(TAG, "onDeleteClick:" + position);
				deletePic(position);
			}

			@Override
			protected void onItemImageClick(int position) {
				//点击图片
				CLog.e(TAG, "onItemImageClick:" + position);
			}

			@Override
			protected void onAddClick(int position) {
				//添加
				CLog.e(TAG, "onAddClick:" + position);
				addPic(position);
			}
		};
		writerGrid.setAdapter(mAdapter);
	}

	/**
	 * 填充数据
	 *
	 * @param mEntity
	 */
	@Override
	public void setMemory(WriterMemory mEntity) {
		this.writerMemory = mEntity;
		this.size = writerMemory.getPictureEntits().size();
		//设置数据
		writerGrid.setImagesData(writerMemory.getPictureEntits());
		//文字
		writerEt.setText(writerMemory.getDesc());
		//时间显示
		writerDateTv.setText(writerMemory.getDate());
		//地址
		writerLocTv.setText(writerMemory.getAddress());
	}

	/**
	 * 预览跳转
	 */
	private void memoryPreview() {
		if (writerMemory.getPictureEntits().isEmpty() && TextUtils.isEmpty(writerMemory.getDesc())) {
			//数据为空
			showShortToast("请添加一个记忆");
			return;
		}
		Intent intent = new Intent(mContext, SupportPMActivity.class);
		intent.putExtra("state", state);
		intent.putExtra("Id", Id);
		intent.putExtra("userId", userId);
		intent.putExtra("memoryId", memoryId);
		intent.putExtra("memorySourceId", memorySourceId);
		intent.putExtra("memory", writerMemory);
		startAnimActivity(intent);
	}

	/**
	 * 增加图片
	 */
	private void addPic(int position) {
		GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(6, true, true, false, true, state, Id, writerMemory.getPictureEntits()), mOnHanlderResultCallback);
	}


	/**
	 * 删除图片
	 *
	 * @param position
	 */
	private void deletePic(int position) {
		//删除图片
		if (size == 1) {
			reqDelete();
		} else {
			writerMemory.getPictureEntits().remove(position);
			setMemory(writerMemory);
		}
	}


	//图片回调
	private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
		@Override
		public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
			CLog.d(TAG, "reqeustCode:" + reqeustCode);
			//追加图片
			if (resultList != null) {
				((AddMemoryPresenter) mPresenter).addPic(writerMemory, resultList);
			}
		}

		@Override
		public void onHanlderFailure(int requestCode, String errorMsg) {
			CLog.d(TAG, "requestCode:" + requestCode);
			showShortToast(errorMsg);
		}
	};

	/**
	 * 选择日期
	 *
	 * @param year
	 * @param month
	 * @param day
	 */
	@Override
	public void showDatePicker(int year, int month, int day) {
		DatePickerSheet
				.createBuilder(mContext, getSupportFragmentManager())
				.setCancelableOnTouchOutside(true)
				.setYear(year)
				.setMonty(month)
				.setDay(day)
				.setTitlePadding(DevUtils.getTitleHeight(mContext))
				.setBottomPadding(DevUtils.getBottomStatusHeight(mContext))
				.setListener(new DatePickerSheet.DatePickerListener() {
					@Override
					public void onSubmit(String date) {
						//确定
						writerMemory.setNewDate(((AddMemoryPresenter) mPresenter).subStringDate(date));
						writerDateTv.setText(writerMemory.getDate());
					}

					@Override
					public void onDismiss() {
					}
				}).show();
	}

	/**
	 * 修改地址
	 */
	private void showAddresss(final String address) {
		DialogUtils.reqEditDialog(SupportActivity.this, "修改地址", address, new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				if (data == null) {
					//取消
				} else {
					String msg = (String) data;
					if (TextUtils.isEmpty(msg)) {
						showShortToast("地址不能为空");
					} else {
						//确定
						writerMemory.setAddress(msg);
						writerLocTv.setText(writerMemory.getAddress());
					}
				}
			}
		});
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			//发送评论-回复记忆体
			String msg = tagEt.getText().toString();
			if (TextUtils.isEmpty(msg)) {
				showShortToast("地址不能为空");
			} else {
				//确定
				writerMemory.setAddress(msg);
				writerLocTv.setText(writerMemory.getAddress());
				hideAddTag();
			}
		}
		return true;
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	/**
	 * 新增标签
	 */
	private void showAddTag(String address) {
		tagEt.setText(address);
		tag_input_rl.setVisibility(View.VISIBLE);
		tag_input_rl.startAnimation(createTranslationInAnimation());
		KeyBoardUtils.ShowKeyboard(tagEt);
	}

	/**
	 * 退出
	 */
	private void hideAddTag() {
		tagEt.setText("");
		KeyBoardUtils.HideKeyboard(tagEt);
		tag_input_rl.setVisibility(View.GONE);
	}

	// 创建进入动画
	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	/**
	 * 退出
	 */
	private void exitDialog() {
		DialogUtils.request(SupportActivity.this, "是否放弃当前的补充状态", "放弃", "取消", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean opr = (boolean) data;
				if (opr) {
					//确定
					hideAddTag();
					finish();
				}
			}
		});
	}

	/**
	 * 请求询问
	 */
	private void reqDelete() {
		DialogUtils.request(SupportActivity.this, "确定删除最后一张图片?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isTrue = (boolean) data;
				if (isTrue) {
					writerMemory.getPictureEntits().remove(0);
					setMemory(writerMemory);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		if (data == null) return;
		if (requestCode == resultCode) {
			if (resultCode == ReqConstant.REQUEST_CODE_ADDRESS) {
				//添加地址
				String address = data.getStringExtra("address");
				writerMemory.setAddress(address);
				writerLocTv.setText(writerMemory.getAddress());

			}
		}
	}
}
