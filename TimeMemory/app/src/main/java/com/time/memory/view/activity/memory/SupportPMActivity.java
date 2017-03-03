package com.time.memory.view.activity.memory;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.MainApplication;
import com.time.memory.R;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.entity.TempMemory;
import com.time.memory.entity.WriterMemory;
import com.time.memory.gui.sixGridImage.SixGridImageView;
import com.time.memory.gui.sixGridImage.SixGridImageViewAdapter;
import com.time.memory.presenter.SupporyPPresenter;
import com.time.memory.presenter.base.BasePresenter;
import com.time.memory.util.CLog;
import com.time.memory.view.activity.base.BaseActivity;
import com.time.memory.view.impl.ISupporyPView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:补充预览
 * @date 2016/10/25 14:40
 */
public class SupportPMActivity extends BaseActivity implements ISupporyPView {
	private static final String TAG = "SupportPMActivity";
	@Bind(R.id.writer_grid)
	SixGridImageView writer_grid;
	@Bind(R.id.writer_details_tv)
	TextView writerContent;//内容
	@Bind(R.id.writer_date_tv)
	TextView writerDate;//日期
	@Bind(R.id.writer_address_tv)
	TextView writer_address_tv;//地址

	private int size = 1;
	private int state;//上传给谁
	private String Id;//对方Id
	private String userId;//userId
	private String memoryId;//memoryId
	private String memorySourceId;//memoryId
	private WriterMemory writerMemory;//当前补充记忆的信息

	@Override
	public void onCreateMyView() {
		setContentView(R.layout.activity_supporypriview);
	}

	@Override
	public void initView() {
		initTopBarForBoth(getString(R.string.app_preview), R.drawable.image_back, getString(R.string.app_release), -1);
	}

	@Override
	public void initData() {
		init();
		state = getIntent().getIntExtra("state", 1);
		Id = getIntent().getStringExtra("Id");
		userId = getIntent().getStringExtra("userId");
		memoryId = getIntent().getStringExtra("memoryId");
		memorySourceId = getIntent().getStringExtra("memorySourceId");
		writerMemory = getIntent().getParcelableExtra("memory");
		setMemory();
	}

	@Override
	public BasePresenter initPresenter() {
		return new SupporyPPresenter();
	}

	public void init() {
		SixGridImageViewAdapter<PhotoInfo> mAdapter = new SixGridImageViewAdapter<PhotoInfo>() {
			@Override
			protected void onDisplayImage(Context context, final ImageView imageView, PhotoInfo entity) {
				//显示
				if (size == 1) {
					Picasso.with(context).load("file://" + entity.getPhotoPath()).resize(800, 600).centerCrop().into(imageView);
				} else {
					Picasso.with(context).load("file://" + entity.getPhotoPath()).resize(300, 300).centerCrop().into(imageView);
				}

			}

			@Override
			protected void onDeleteClick(int position) {
			}

			@Override
			protected void onItemImageClick(int position) {
				CLog.e(TAG, "position:" + position);
			}

			@Override
			protected void onAddClick(int position) {
			}
		};
		writer_grid.setAdapter(mAdapter);
	}

	@OnClick(R.id.tv_main_right)
	public void onClick(View view) {
		super.onMyClick(view);
		if (view.getId() == R.id.tv_main_right) {
			//下一步(发送)
			((SupporyPPresenter) mPresenter).upLoadMemory(writerMemory, state, Id, memoryId,memorySourceId, userId, MainApplication.getUserId());
		}
	}

	@Override
	public void showSuccess() {
		hideLoadingDialog();
//		Intent intent = new Intent(mContext, MemoryDetailsFragment.class);
//		setResult(ReqConstant.REQUEST_CODE_EDIT, intent);
//		ActivityTaskManager.getInstance().removeActivity("SupportActivity");
//		ActivityTaskManager.getInstance().removeActivity("SupportPMActivity");
//		finish();
	}


	/**
	 * 上传成功,分发
	 *
	 * @param memory
	 */
	@Override
	public void setMemory(TempMemory memory) {
		Intent Intent = new Intent(mContext, MemoryDetailActivtiy.class);
		Intent.putExtra("tempMemory", memory);
		Intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startAnimActivity(Intent);
		finish();
	}

	@Override
	public void showFaild() {
		hideLoadingDialog();
	}

	/**
	 * 设置信息
	 */
	private void setMemory() {
		size = writerMemory.getPictureEntits().size();
		writer_grid.setImagesData(writerMemory.getPictureEntits());//图片
		writerContent.setText(writerMemory.getDesc());//内容
		writerDate.setText(writerMemory.getDate());//日期
		if (!TextUtils.isEmpty(writerMemory.getAddress())) {
			writer_address_tv.setText(writerMemory.getAddress());//地址
			writer_address_tv.setVisibility(View.VISIBLE);
		} else {
			writer_address_tv.setVisibility(View.GONE);
		}
	}

}
