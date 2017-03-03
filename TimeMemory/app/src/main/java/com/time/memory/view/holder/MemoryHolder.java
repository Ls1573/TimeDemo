package com.time.memory.view.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.nineGridImage.NineGridImageView;
import com.time.memory.gui.nineGridImage.NineGridImageViewAdapter;

import butterknife.Bind;

/**
 * ==============================
 *
 * @author Qiu
 * @version V1.0
 * @Description:记忆首页
 * @date 2016-9-6下午3:09:16
 * ==============================
 */
public class MemoryHolder extends BaseHolder<MGroup> {
	private static final String TAG = "MemoryHolder";

	@Bind(R.id.memory_sign_tv)
	TextView memory_sign_tv;// 谁的记忆
	@Bind(R.id.memory_start_tv)
	TextView memory_start_tv;//开启篇章
	@Bind(R.id.memory_head_tv)
	TextView memory_head_tv;// 追加记忆数量
	@Bind(R.id.memory_man_tv)
	TextView memory_man_tv;// 好友数量
	@Bind(R.id.memory_date_tv)
	TextView memory_date_tv;//时间
	@Bind(R.id.memory_desc_tv)
	TextView memory_desc_tv;//描述
	@Bind(R.id.memory_pic_rl)
	RelativeLayout memory_pic_rl;//书的相对布局
	@Bind(R.id.memory_rl)
	RelativeLayout memory_rl;//外框
	@Bind(R.id.memory_line_img)
	ImageView memory_line_img;//书下面的线条
	@Bind(R.id.add_new)
	LinearLayout add_new;//新增外层布局
	@Bind(R.id.add_new_number)
	TextView add_new_number;//新增数量
	@Bind(R.id.supplement)
	LinearLayout supplement;//补充外层布局
	@Bind(R.id.supplement_number)
	TextView supplement_number;//补充数量
	@Bind(R.id.add_new_tv)
	TextView add_new_tv;//新增汉字

	@Bind(R.id.memory_fork_rl)
	RelativeLayout forkRl;//新增评论点赞外框
	@Bind(R.id.memory_fork_tv)
	TextView forkTv;//新增评论数


	@Bind(R.id.time_imsg)
	NineGridImageView mNglContent;//9宫图

	private String imgPath;
	private String imgOSS;

	public MemoryHolder(View view) {
		super(view);
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
		imgOSS = mContext.getString(R.string.FSIMAGEOSS);
	}

	@Override
	public void init() {
		NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
			@Override
			protected void onDisplayImage(Context context, ImageView imageView, String path) {
				if (TextUtils.isEmpty(path))
					imageView.setImageResource(R.drawable.headpic);
				else
					Picasso.with(context).load(imgPath + path + imgOSS).resize(200, 200).centerCrop().placeholder(R.drawable.headpic).error(R.drawable.headpic).into(imageView);
			}

		};
		mNglContent.setAdapter(mAdapter);
	}

	@Override
	public void setData(final MGroup entity, final int position) {
		super.setData(entity);
		if (entity.getHeadPhotos() == null || entity.getHeadPhotos().isEmpty()) {
			mNglContent.setVisibility(View.GONE);
		} else {
			mNglContent.setVisibility(View.VISIBLE);
			mNglContent.setImagesData(entity.getHeadPhotos());
		}
		//谁的记忆
		memory_sign_tv.setText(entity.getGroupName());
		memory_sign_tv.setTextColor(mContext.getResources().getColor(R.color.grey_42));
		//设置书的时间(建立)
		memory_date_tv.setText(entity.getUpdateDateForShow());
		//描述
		memory_desc_tv.setText(TextUtils.isEmpty(entity.getTitle()) ? "暂无记忆" : entity.getTitle());
		//设置单行显示
		memory_desc_tv.setTextColor(mContext.getResources().getColor(R.color.grey_78));
		memory_desc_tv.setLines(1);
		//记忆数
		memory_head_tv.setText(entity.getMemoryCnt());
		memory_line_img.setVisibility(View.VISIBLE);
		//总未读点赞评论数
		if (entity.getType() == 0) {
			//只有我的记忆有
			if (!TextUtils.isEmpty(entity.getTotalPCnt()) && !entity.getTotalPCnt().equals("0")) {
				//不为空,不为零
				forkRl.setVisibility(View.VISIBLE);
				forkTv.setText(entity.getTotalPCnt());
			} else {
				forkRl.setVisibility(View.GONE);
			}
		} else {
			forkRl.setVisibility(View.GONE);
		}
		//新增记忆数
		if (!TextUtils.isEmpty(entity.getUnReadMemoryCnt()) && !entity.getUnReadMemoryCnt().equals("0")) {
			add_new.setVisibility(View.VISIBLE);
			add_new_number.setText(entity.getUnReadMemoryCnt());
		} else {
			add_new.setVisibility(View.GONE);
		}
		//补充记忆数
		if (!TextUtils.isEmpty(entity.getUnReadMPointAddCnt()) && !entity.getUnReadMPointAddCnt().equals("0")) {
			supplement.setVisibility(View.VISIBLE);
			supplement_number.setText(entity.getUnReadMPointAddCnt());
		} else {
			supplement.setVisibility(View.GONE);
		}

		//设置背景
		memory_rl.setBackgroundResource(R.drawable.mine_ll);
		//设置书的不同背景
		if (entity.getType() == 0) {
			//我的
			memory_pic_rl.setBackgroundResource(R.drawable.memory_bg_my);
			memory_man_tv.setVisibility(View.GONE);
			memory_start_tv.setVisibility(View.GONE);
			memory_line_img.setBackgroundResource(R.drawable.memory_groupline);

		} else if (entity.getType() == 1) {
			//他的
			memory_pic_rl.setBackgroundResource(R.drawable.memory_bg_his);
			//好友数
			memory_man_tv.setText(entity.getGroupCount());
			memory_man_tv.setVisibility(View.VISIBLE);
			memory_start_tv.setVisibility(View.GONE);
			memory_line_img.setBackgroundResource(R.drawable.memory_groupline);
			memory_date_tv.setVisibility(View.VISIBLE);
			memory_head_tv.setVisibility(View.VISIBLE);
		} else if (entity.getType() == 2) {
			//圈子的
			memory_pic_rl.setBackgroundResource(R.drawable.memory_bg_other);
			memory_line_img.setBackgroundResource(R.drawable.memory_other);
			memory_man_tv.setVisibility(View.VISIBLE);
			//好友数
			memory_man_tv.setText(entity.getGroupCount());
			//激活状态
			if (entity.getActiveFlg().equals("1")) {
				//未激活
				memory_start_tv.setVisibility(View.VISIBLE);
				memory_rl.setBackgroundColor(mContext.getResources().getColor(R.color.grey_F5));
			} else {
				//已激活
				memory_start_tv.setVisibility(View.GONE);
			}
			memory_date_tv.setVisibility(View.VISIBLE);
			memory_head_tv.setVisibility(View.VISIBLE);
		} else if (entity.getType() == 3) {
			//创建新的编辑部
			memory_pic_rl.setBackgroundResource(R.drawable.creat_editorial);
			memory_line_img.setVisibility(View.GONE);
			memory_head_tv.setVisibility(View.GONE);
			memory_date_tv.setVisibility(View.GONE);
			memory_sign_tv.setText("新增编辑部");
			memory_sign_tv.setTextColor(mContext.getResources().getColor(R.color.grey_78));
			supplement.setVisibility(View.GONE);
			add_new.setVisibility(View.GONE);
			memory_desc_tv.setText(R.string.miao_shu);
			memory_desc_tv.setTextColor(mContext.getResources().getColor(R.color.grey_A3));
			memory_desc_tv.setLines(2);
			mNglContent.setVisibility(View.GONE);
			memory_man_tv.setVisibility(View.GONE);
			memory_start_tv.setVisibility(View.GONE);
		}

		//人员列表
		memory_man_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!entity.getActiveFlg().equals("1")) {
					//已激活
					mHolderCallBack.onClick(position, 2, entity.getActiveFlg());
				}
			}
		});

		//激活
		memory_start_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(position, -1, entity.getGroupId());
			}
		});

		//新增点击事件
		add_new.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!entity.getActiveFlg().equals("1")) {
					mHolderCallBack.onClick(position, 0, entity.getActiveFlg());
				}
			}
		});

		//补充点击事件
		supplement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!entity.getActiveFlg().equals("1")) {
					mHolderCallBack.onClick(position, 1, entity.getActiveFlg());
				}

			}
		});

		//未读点赞评论
		forkRl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHolderCallBack.onClick(-1);

			}
		});

		//外框
		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (entity.getActiveFlg().equals("1")) {
					//未激活
				} else {
					//已激活
					mHolderCallBack.onClick(position);
				}
			}
		});

	}


}
