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
import com.time.memory.entity.MGroup;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.WriterMemory;
import com.time.memory.entity.WriterStyleMemory;
import com.time.memory.gui.DatePickerSheet;
import com.time.memory.gui.gallery.GalleryFinal;
import com.time.memory.presenter.WriterMPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.util.DevUtils;
import com.time.memory.util.DialogUtils;
import com.time.memory.util.KeyBoardUtils;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.activity.common.LocationActivity;
import com.time.memory.view.adapter.BaseRecyclerBothAdapter;
import com.time.memory.view.holder.WriterFooterHolder;
import com.time.memory.view.holder.WriterMHolder;
import com.time.memory.view.holder.WriterStyleHolder;
import com.time.memory.view.impl.IWtieterMView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Qiu
 * @version V1.0
 * @Description:写记忆
 * @date 2016/10/03 16:45
 */
public class WriterMActivity extends BaseActivity implements IWtieterMView, AdapterCallback, TextView.OnEditorActionListener {
	private static final String TAG = "WriterMActivity";
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
	private ArrayList<WriterMemory> mList;//数据集


	private int curPositoin;//追加记忆位置
	private boolean isAddPic;//追加图片

	private int state;//旧权限
	private int nState;//新权限
	private String className;

	private String Id;//对应Id

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_writermemory);
	}


	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_addmemory), R.drawable.image_back, getString(R.string.app_preview), -1);
	}

	@Override
	public void initData() {
		swipe_target.setLayoutManager(new LinearLayoutManager(mContext));
		tagEt.setOnEditorActionListener(this);
		//去闪屏
		((DefaultItemAnimator) swipe_target.getItemAnimator()).setSupportsChangeAnimations(false);
		//获取数据
		ArrayList<PhotoInfo> photoList = getIntent().getParcelableArrayListExtra("photos");
		state = getIntent().getIntExtra("state", 0);
		Id = getIntent().getStringExtra("Id");
		className = getIntent().getStringExtra("class");
		nState = state;
		//数据装换
		((WriterMPresenter) mPresenter).getMessage(photoList, nState, Id);
	}

	@Override
	public BasePresenter initPresenter() {
		return new WriterMPresenter();
	}


	@Override
	public void setAdapter(ArrayList<WriterMemory> list, WriterStyleMemory writerStyleMemory) {
		if (swipe_target == null) return;
		if (adapter == null) {
			this.mList = list;
			this.mWriterStyleMemory = writerStyleMemory;
			adapter = new BaseRecyclerBothAdapter(
					mList, R.layout.item_writermemory, WriterMHolder.class,
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
//		adapter.notifyItemRemoved(positoin + 1);
//		adapter.notifyItemRangeChanged(positoin + 1, mDatas.size() - position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void refreshHeaderAdapter() {
		//刷新Header
		adapter.notifiyHeader();
	}

	@OnClick({R.id.tv_main_right})
	public void onClick(View view) {
		if (view.getId() == R.id.app_cancle) {
			//回退
			showExitDialog();
		} else if (view.getId() == R.id.tv_main_right) {
			//下一步(带数据)
			memoryPreview();
		}
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		showExitDialog();
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
				((WriterMPresenter) mPresenter).delete(position, index, mList);
				break;
			case 2:
				//点击图片
				break;
			case 3:
				//添加图片
				isAddPic = true;
				GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(6, true, true, false, true, nState, Id, mList.get(curPositoin).getPictureEntits()), mOnHanlderResultCallback);
				break;
			case 4:
				//选择时间
				((WriterMPresenter) mPresenter).convertItemDate(mList.get(position).getDate().toString().trim(), position);
				break;
			case 5:
				//编辑地点
//				showAddTag(mList.get(curPositoin).getAddress());
				Intent intent = new Intent(mContext, LocationActivity.class);
				startActivityForResult(intent, ReqConstant.REQUEST_CODE_ADDRESS);
				break;
			case 6:
				//头部追加-相册选取
				isAddPic = false;
				((WriterMPresenter) mPresenter).getMaxSize(mList);
				break;
			case 7:
				//尾部追加
				isAddPic = false;
				curPositoin = mList.size();
				((WriterMPresenter) mPresenter).getMaxSize(mList);
				break;
		}
	}

	@Override
	public void setMaxSize(int size) {
		GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, initPhoto(size, true, true, false, nState, Id, new ArrayList<PhotoInfo>()), mOnHanlderResultCallback);
	}

	@Override
	public void onDataCallBack(Object data, int position) {
		//都是主题操作
		int opr = (int) data;
		CLog.e(TAG, "opr:" + opr + "   position:" + position);
		if (opr == 1) {
			//创建日期(主题)
			((WriterMPresenter) mPresenter).convertDate(mWriterStyleMemory.getMemoryDate().toString().trim(), position);
		} else if (opr == 2) {
			//贴记忆签(主题)
			Intent intent = new Intent(mContext, AddTagActivity.class);
			intent.putExtra("Id", Id);
			intent.putExtra("state", nState);
			startActivityForResult(intent, ReqConstant.ADDTAG);
		} else if (opr == 3) {
			//换目标
			Intent intent = new Intent(mContext, AddTargetActivity.class);
			intent.putExtra("groupId", Id);
			intent.putExtra("state", nState);
			startActivityForResult(intent, ReqConstant.REQUEST_CODE_ADDTARGET);
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
			((WriterMPresenter) mPresenter).addAddress(msg, curPositoin, mList);
		}
		return true;
	}

	//图片回调
	private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
		@Override
		public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
			CLog.d(TAG, "reqeustCode:" + reqeustCode);
			if (isAddPic) {
				//追加图片
				if (resultList != null) {
					((WriterMPresenter) mPresenter).addPic(mList, resultList, curPositoin);
				}
			} else {
				//追加记忆
				if (resultList != null) {
					//头部追加
					((WriterMPresenter) mPresenter).addMessage(mList, resultList, curPositoin);
				}
			}
		}

		@Override
		public void onHanlderFailure(int requestCode, String errorMsg) {
			CLog.d(TAG, "requestCode:" + requestCode);
			showShortToast(errorMsg);
		}
	};

	@Override
	public void showSuccess() {
		super.showSuccess();
		hideLoadingDialog();
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
			Intent intent = new Intent(mContext, PreviewMActivity.class);
			intent.putParcelableArrayListExtra("writers", mList);
			intent.putExtra("style", mWriterStyleMemory);
			intent.putExtra("state", state);//旧的状态
			intent.putExtra("nState", nState);//当前的状态
			intent.putExtra("Id", Id);
			intent.putExtra("className", className);
			intent.putExtra("name", mWriterStyleMemory.getSign());
			startAnimActivity(intent);
		}
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
	 * 设置请求头
	 */
	private void setWriterStyleMemory(Lable lab) {
		mWriterStyleMemory.setLabelId(lab.getLabelId());
		mWriterStyleMemory.setLabelName(lab.getLabelName());
		if (nState == 2) {
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
		try {
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
								mWriterStyleMemory.setMemoryDate(((WriterMPresenter) mPresenter).subStringDate(date));
								adapter.notifyItemChanged(0);
							} else {
								//point
								mList.get(position).setNewDate(((WriterMPresenter) mPresenter).subStringDate(date));
								adapter.notifyItemChanged(position + 1);
							}
						}

						@Override
						public void onDismiss() {
						}
					}).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置信息
	 *
	 * @param nState
	 * @param groupId
	 * @param sign
	 */
	@Override
	public void setMessage(int nState, String groupId, String sign) {
		this.nState = nState;//新权限
		this.Id = groupId;//新Id
		if (adapter != null)
			adapter.notifyItemChanged(0);
	}

	/**
	 * 放弃回退
	 */
	private void showExitDialog() {
		DialogUtils.request(WriterMActivity.this, "是否放弃当前的编辑状态", "放弃", "取消", new DialogCallback() {
			@Override
			public void onCallback(Object data) {
				boolean isExit = (boolean) data;
				if (isExit) {
					finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) return;
		if (data == null) return;
		if (reqCode == resultCode) {
			if (resultCode == ReqConstant.ADDTAG) {
				//选标签
				Lable lab = (Lable) data.getParcelableExtra("lable");
				setWriterStyleMemory(lab);
				//更新
				adapter.notifyItemChanged(0);
			} else if (resultCode == ReqConstant.REQUEST_CODE_ADDRESS) {
				//添加地址
				String address = data.getStringExtra("address");
				((WriterMPresenter) mPresenter).addAddress(address, curPositoin, mList);
			} else if (resultCode == ReqConstant.REQUEST_CODE_ADDTARGET) {
				//切换目标
				MGroup mGroup = data.getParcelableExtra("mGroup");
				((WriterMPresenter) mPresenter).addTarget(mGroup, nState, mWriterStyleMemory);
			}
		}
	}
}
