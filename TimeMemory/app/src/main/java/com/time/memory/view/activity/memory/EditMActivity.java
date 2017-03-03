package com.time.memory.view.activity.memory;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.DialogCallback;
import com.time.memory.core.constant.ReqConstant;
import com.time.memory.entity.Lable;
import com.time.memory.entity.MemoryEdit;
import com.time.memory.entity.MemoryInfo;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.gui.DatePickerSheet;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.presenter.EditMPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.LocationActivity;
import com.time.memory.view.adapter.BaseRecyclerBothAdapter;
import com.time.memory.view.holder.EditMHolder;
import com.time.memory.view.holder.WriterFooterHolder;
import com.time.memory.view.holder.WriterStyleHolder;
import com.time.memory.view.impl.IEditMView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:编辑记忆
 * @date 2016/10/25 17:46
 */
public class EditMActivity extends BaseActivity implements IEditMView, AdapterCallback, TextView.OnEditorActionListener {
	private static final String TAG = "EditMActivity";
	private final int REQUEST_CODE_GALLERY = 101;
	private static final int TRANSLATE_DURATION = 120;

	@Bind(R.id.swipe_target)
	RecyclerView swipe_target;
	@Bind(R.id.tag_et)
	EditText tagEt;//输入框
	@Bind(R.id.tag_input_rl)
	RelativeLayout tag_input_rl;//标签外框

	private DatePickerSheet sheet;
	private BaseRecyclerBothAdapter adapter;

	private WriterStyleMemory mWriterStyleMemory;//主题头
	private ArrayList<MemoryEdit> mList;//数据集

	private int curPositoin;//追加记忆位置
	private boolean isAddPic;//追加图片

	private int state;//权限
	private String Id;//对应Id
	private String memoryId;//记忆Id

	private MemoryInfo memoryInfo;//记忆详情

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_writermemory);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_editmemory), R.drawable.image_back, getString(R.string.app_preview), -1);
	}

	@Override
	public BasePresenter initPresenter() {
		return new EditMPresenter();
	}

	@Override
	public void initData() {
		tagEt.setOnEditorActionListener(this);
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		//去闪屏
		((DefaultItemAnimator) swipe_target.getItemAnimator()).setSupportsChangeAnimations(false);

//		state = getIntent().getIntExtra("state", 1);
//		Id = getIntent().getStringExtra("Id");

		memoryInfo = getIntent().getParcelableExtra("memoryInfo");
		state = Integer.parseInt(memoryInfo.getMemory().getState());
		Id = memoryInfo.getMemory().getGroupId();
		((EditMPresenter) mPresenter).setMemoryInfo(memoryInfo);
	}


	@Override
	public void setAdapter(ArrayList<MemoryEdit> list, WriterStyleMemory writerStyleMemory) {
		if (swipe_target == null) return;
		if (adapter == null) {
			this.mList = list;
			this.mWriterStyleMemory = writerStyleMemory;
			adapter = new BaseRecyclerBothAdapter(
					mList, R.layout.item_writermemory, EditMHolder.class,
					mWriterStyleMemory, R.layout.item_writerstyle, WriterStyleHolder.class,
					null, R.layout.item_writerfooter, WriterFooterHolder.class);
			adapter.setCallBack(this);
			swipe_target.setAdapter(adapter);
		}
	}

	@Override
	public void addAdapter(boolean isAdd) {
		//二次更新数据了
		adapter.notifyDataSetChanged();
	}

	@Override
	public void refreshItemAdapter(int positoin) {
		//刷新内容
		hideAddTag();
		adapter.notifiyItem(positoin);
	}

	@Override
	public void removeItemAdapter(int positoin) {
		//刷新内容
		hideAddTag();
//		adapter.notifyItemRemoved(positoin);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void refreshHeaderAdapter() {
		//刷新Header
		adapter.notifiyHeader();
	}

	@OnClick({R.id.tv_main_right})
	public void onClick(View view) {
		if (view.getId() == R.id.tv_main_right) {
			//下一步(带数据)
			memoryPreview();
		} else if (view.getId() == R.id.app_cancle) {
			//退出
			exitDialog();
		}
	}

	@Override
	public void onDataCallBack(Object data, int position, int index) {
		//都是Item的内容操作
		int opr = (int) data;
		this.curPositoin = position;
		CLog.e(TAG, "opr:" + opr + "   position:" + position + "  index:" + index);
		switch (opr) {
			case 1:
				//删除图片
				((EditMPresenter) mPresenter).delete(position, index, mList);
				break;
			case 2:
				//点击图片
				break;
			case 3:
				//添加图片
				isAddPic = true;
				((EditMPresenter) mPresenter).getMaxSize(mList.get(curPositoin).getPhotoInfos());
				break;
			case 4:
				//选择时间
				((EditMPresenter) mPresenter).convertItemDate(mList.get(position).getMemoryPointDate().toString().trim(), position);
				break;
			case 5:
				//编辑地点
//				showAddTag(mList.get(curPositoin).getLocal());
				Intent intent = new Intent(mContext, LocationActivity.class);
				startActivityForResult(intent, ReqConstant.REQUEST_CODE_ADDRESS);
				break;
			case 6:
				//头部追加-相册选取
				isAddPic = false;
				GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(299, true, true, false, state, Id, new ArrayList<PhotoInfo>()), mOnHanlderResultCallback);
				break;
			case 7:
				//尾部追加
				isAddPic = false;
				curPositoin = mList.size();
				GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(299, true, true, false, state, Id, new ArrayList<PhotoInfo>()), mOnHanlderResultCallback);
				break;
			case 8:
				//头部删除
				delDialog();
				break;
		}
	}

	@Override
	public void checkPhoto(int maxSize) {
		//选取图片
		GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(maxSize, true, true, false, true, state, Id, mList.get(curPositoin).getPhotoInfos()), mOnHanlderResultCallback);
	}

	@Override
	public void onDataCallBack(Object data, int position) {
		//都是主题操作
		int opr = (int) data;
		CLog.e(TAG, "opr:" + opr + "   position:" + position);
		if (opr == 1) {
			//创建日期(主题)
			((EditMPresenter) mPresenter).convertDate(mWriterStyleMemory.getMemoryDate().toString().trim(), position);
		} else if (opr == 2) {
			//贴记忆签(主题)
			Intent intent = new Intent(mContext, AddTagActivity.class);
			intent.putExtra("Id", Id);
			intent.putExtra("state", state);
			startActivityForResult(intent, ReqConstant.ADDTAG);
		}
	}

	@Override
	public void onCallback(Object data) {
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND) {
			//发送地址
			String msg = tagEt.getText().toString();
			((EditMPresenter) mPresenter).addAddress(msg, curPositoin, mList);
		}
		return true;
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	//图片回调
	private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
		@Override
		public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
			CLog.d(TAG, "reqeustCode:" + reqeustCode);
			if (isAddPic) {
				//追加图片
				if (resultList != null) {
					((EditMPresenter) mPresenter).addPic(mList, resultList, curPositoin);
				}
			} else {
				//追加记忆
				if (resultList != null) {
//					curPositoin++;
					//头部追加
					((EditMPresenter) mPresenter).addMessage(mList, resultList, curPositoin, memoryId);
				}
			}

		}

		@Override
		public void onHanlderFailure(int requestCode, String errorMsg) {
			CLog.d(TAG, "requestCode:" + requestCode);
			showShortToast(errorMsg);
		}
	};

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
	 * 预览跳转
	 */
	private void memoryPreview() {
		if (TextUtils.isEmpty(mWriterStyleMemory.getTitle())) {
			showShortToast("请输入记忆主题");
		} else {
			if (mList == null || mList.isEmpty()) {
				showShortToast("请添加一个记忆");
				return;
			}
			Intent intent = new Intent(mContext, EditPMActivity.class);
			intent.putParcelableArrayListExtra("writers", mList);
			intent.putExtra("style", mWriterStyleMemory);
			intent.putExtra("state", state);
			intent.putExtra("Id", Id);
			startActivityForResult(intent, ReqConstant.REQUEST_CODE_EDIT);
		}
	}

	/**
	 * 设置请求头
	 */
	private void setWriterStyleMemory(Lable lab) {
		mWriterStyleMemory.setLabelId(lab.getLabelId());
		mWriterStyleMemory.setLabelName(lab.getLabelName());
		if (state == 2) {
			//圈子
			mWriterStyleMemory.setGroupLabelId(lab.getLabelId());
			mWriterStyleMemory.setGroupLabelName(lab.getLabelName());
		}
	}

	/**
	 * 选择日期
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @param position
	 */
	@Override
	public void showDatePicker(int year, int month, int day, final int position) {
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
						if (position == -1) {
							//主题
							mWriterStyleMemory.setMemoryDate(((EditMPresenter) mPresenter).subStringDate(date));
							adapter.notifyItemChanged(0);
						} else {
							//point
							mList.get(position).setNewDate(((EditMPresenter) mPresenter).subStringDate(date));
							adapter.notifyItemChanged(position + 1);
						}
					}

					@Override
					public void onDismiss() {
					}
				}).show();
	}


	/**
	 * 修改地址
	 */
//	private void showAddresss(final String address) {
//		DialogUtils.reqEditDialog(mContext, "修改地址", address, new DialogCallback() {
//			@Override
//			public void onCallback(Object data) {
//				if (data == null) {
//					//取消
//				} else {
//					String msg = (String) data;
//					if (TextUtils.isEmpty(msg)) {
//						showShortToast("地址不能为空");
//					} else {
//						//确定
//						((EditMPresenter) mPresenter).addAddress(msg, curPositoin, mList);
//					}
//				}
//			}
//		});
//	}

	/**
	 * 退出
	 */
	private void exitDialog() {
		DialogUtils.request(EditMActivity.this, "是否放弃当前的编辑状态", "放弃", "取消", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean opr = (boolean) data;
				if (opr) {
					//确定
					finish();
				}
			}
		});
	}

	/**
	 * 删除
	 */
	private void delDialog() {
		DialogUtils.request(EditMActivity.this, "确定删除此记忆片段吗?", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean opr = (boolean) data;
				if (opr) {
					//确定
					mList.remove(curPositoin);
					adapter.notifyItemRemoved(curPositoin + 1);
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
			if (resultCode == ReqConstant.ADDTAG) {
				//选标签
				Lable lab = (Lable) data.getParcelableExtra("lable");
				setWriterStyleMemory(lab);
				//更新
				adapter.notifyItemChanged(0);
			} else if (resultCode == ReqConstant.REQUEST_CODE_ADDRESS) {
				//添加地址
				String address = data.getStringExtra("address");
				((EditMPresenter) mPresenter).addAddress(address, curPositoin, mList);
			}
		}
	}


}
