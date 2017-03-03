package com.time.memory.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.entity.GroupMemory;
import com.time.memory.entity.GroupMemorys;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.nineGridImage.NineGridImageView;
import com.time.memory.gui.nineGridImage.NineGridImageViewAdapter;
import com.time.memory.gui.stickyHeader.SectioningAdapter;
import com.time.memory.gui.stickyHeader.StickyHeaderLayoutManager;
import com.time.memory.util.CLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:时间轴
 * @date 2016/10/17 10:41
 */
public class GroupTimeLineAdapter extends SectioningAdapter {

	static final String TAG = GroupTimeLineAdapter.class.getSimpleName();

	private List<GroupMemorys> list;
	private Context context;
	private int selectColor;//选中颜色
	private int unSelectColor;//未选择颜色
	private AdapterCallback mCallback;

	public void setAdapterCallBack(AdapterCallback callback) {
		this.mCallback = callback;
	}

	private StickyHeaderLayoutManager.HeaderPositionChangedCallback headerPositionChangedCallback;

	public void setCallBack(StickyHeaderLayoutManager.HeaderPositionChangedCallback headerPositionChangedCallback) {
		this.headerPositionChangedCallback = headerPositionChangedCallback;
	}

	/**
	 * 内容
	 */
	public class ItemViewHolder extends SectioningAdapter.ItemViewHolder {
		@Bind(R.id.time_title_tv)
		TextView time_title_tv;//题头
		@Bind(R.id.time_imsg)
		NineGridImageView mNglContent;//9宫图
		@Bind(R.id.memory_pic_tv)
		TextView memory_pic_tv;//图片数
		@Bind(R.id.memory_fork_tv)
		TextView memory_fork_tv;//点赞数
		@Bind(R.id.memory_comment_tv)
		TextView memory_comment_tv;//评论数
		@Bind(R.id.memory_add_tv)
		TextView memory_add_tv;//追加数
		@Bind(R.id.time_from_tv)
		TextView time_from_tv;//来源

		@Bind(R.id.time_loc_tv)
		TextView time_loc_tv;//地址
		@Bind(R.id.time_unread_ll)
		LinearLayout time_unread_ll;//未读记忆
		@Bind(R.id.time_unreadnum_tv)
		TextView time_unreadnum_tv;//未读记忆数

		private int mPosition;
		private int index;

		public ItemViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			NineGridImageViewAdapter<PhotoInfo> mAdapter = new NineGridImageViewAdapter<PhotoInfo>() {
				@Override
				protected void onDisplayImage(Context context, ImageView imageView, PhotoInfo entity) {
					Picasso.with(context).load(entity.getPhotoPath()).config(Bitmap.Config.RGB_565).into(imageView);
				}
			};
			mNglContent.setAdapter(mAdapter);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.onDataCallBack(index, mPosition);
				}
			});
		}

		public void setData(int position, int index, GroupMemory memory) {
			this.mPosition = position;
			this.index = index;
			time_title_tv.setText(memory.getTitle());//题头
			mNglContent.setImagesData(memory.getPictureEntits());//9宫图片
			time_from_tv.setText(memory.getUserNameG());//来源
			memory_pic_tv.setText(memory.getPhotoCount() + "");//图片数
			memory_fork_tv.setText(memory.getPraiseCount() + "");//点赞数
			memory_comment_tv.setText(memory.getCommentCount() + "");//评论数
			memory_add_tv.setText(memory.getAddmemoryCount() + "");//追加数

			// 未读追加记忆数
			if (memory.getUnReadMPointAddCnt() != 0) {
				time_unread_ll.setVisibility(View.VISIBLE);
				time_unreadnum_tv.setText(String.valueOf(memory.getUnReadMPointAddCnt()));
			} else {
				time_unread_ll.setVisibility(View.GONE);
			}
			//地址
			if (!TextUtils.isEmpty(memory.getLocal())) {
				time_loc_tv.setVisibility(View.VISIBLE);
				time_loc_tv.setText(memory.getLocal());
			} else {
				time_loc_tv.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 头
	 */
	public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder implements StickyHeaderLayoutManager.HeaderPositionChangedCallback {

		@Bind(R.id.time_date_tv)
		TextView time_date_tv;//日期(天)
		@Bind(R.id.time_year_tv)
		TextView time_year_tv;//日期(年-月)


		private StickyHeaderLayoutManager.HeaderPositionChangedCallback headerPositionChangedCallback;

		public void setCallBack(StickyHeaderLayoutManager.HeaderPositionChangedCallback headerPositionChangedCallback) {
			this.headerPositionChangedCallback = headerPositionChangedCallback;
		}


		public HeaderViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		@Override
		public void onDownPositionChanged(int sectionIndex, int lastIndex) {
			CLog.e(TAG, "sectionIndex:" + sectionIndex);
		}

		@Override
		public void onUpPositionChanged(int sectionIndex, int lastIndex) {
			CLog.e(TAG, "sectionIndex:" + sectionIndex);
		}
	}

	/**
	 * 构造
	 *
	 * @param list
	 * @param context
	 */
	public GroupTimeLineAdapter(List<GroupMemorys> list, Context context) {
		this.list = list;
		this.context = context;
		selectColor = context.getResources().getColor(R.color.browen_BB);
		unSelectColor = context.getResources().getColor(R.color.grey_73);
	}

	void onToggleSectionCollapse(int sectionIndex) {
		Log.d(TAG, "onToggleSectionCollapse() called with: " + "sectionIndex = [" + sectionIndex + "]");
		setSectionIsCollapsed(sectionIndex, !isSectionCollapsed(sectionIndex));
	}

	void onDeleteSection(int sectionIndex) {
		Log.d(TAG, "onDeleteSection() called with: " + "sectionIndex = [" + sectionIndex + "]");
		list.remove(sectionIndex);
		notifySectionRemoved(sectionIndex);
	}

	void onDeleteItem(int sectionIndex, int itemIndex) {
		Log.d(TAG, "onDeleteItem() called with: " + "sectionIndex = [" + sectionIndex + "], itemIndex = [" + itemIndex + "]");
	}

	/**
	 * 切换状态
	 *
	 * @param sectionIndex
	 * @param lastIndex
	 */
	public void onChangePositionHeader(int sectionIndex, int lastIndex) {
		list.get(sectionIndex).setIsActived(true);
		list.get(lastIndex).setIsActived(false);
		notifyItemChanged(sectionIndex);
		notifyItemChanged(lastIndex);
	}

	@Override
	public int getNumberOfSections() {
		//总共几个模块
		return list.size();
	}

	@Override
	public int getNumberOfItemsInSection(int sectionIndex) {
		//每个模块的内容数量
		return list.get(sectionIndex).getList().size();
	}

	@Override
	public boolean doesSectionHaveHeader(int sectionIndex) {
		return true;
	}

	@Override
	public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.item_memory_sign, parent, false);
		return new ItemViewHolder(v);
	}

	@Override
	public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.item_time_header, parent, false);
		return new HeaderViewHolder(v);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
		//内容详情
		ItemViewHolder ivh = (ItemViewHolder) viewHolder;
		ivh.setData(sectionIndex, itemIndex, list.get(sectionIndex).getList().get(itemIndex));
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
		//左侧固定头
		HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
		GroupMemorys GroupMemorys = list.get(sectionIndex);
		hvh.time_date_tv.setText(GroupMemorys.getDate());
		hvh.time_year_tv.setText(GroupMemorys.getYearMouth());

		if (GroupMemorys.isActived()) {
			//被选中
			hvh.time_date_tv.setTextColor(selectColor);
			hvh.time_year_tv.setTextColor(selectColor);
		} else {
			//删选
			hvh.time_date_tv.setTextColor(unSelectColor);
			hvh.time_year_tv.setTextColor(unSelectColor);
		}
	}
}
