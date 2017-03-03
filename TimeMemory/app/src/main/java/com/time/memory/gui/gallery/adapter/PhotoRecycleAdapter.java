package com.time.memory.gui.gallery.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.MyTouchImageView;
import com.time.memory.util.CLog;
import com.time.memory.util.CPResourceUtil;
import com.time.memory.util.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:相册多种布局
 * @date 2016/9/27 14:24
 */
public class PhotoRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final String TAG = "PhotoRecycleAdapter";
	private Context context;
	private List<PhotoInfo> results;//当前的
	private List<PhotoInfo> mSelectList;//被选中

	private int maxSize;//最多几张
	//type
	public static final int TYPE_TYPE_HEAD = 0xff01;
	public static final int TYPE_TYPE = 0xff02;

	//Date
	private String yestDate;
	private String curDate;

	public PhotoRecycleAdapter(Context context, List<PhotoInfo> list, List<PhotoInfo> selectList, int maxSize) {
		this.context = context;
		this.results = list;
		this.mSelectList = selectList;
		this.maxSize = maxSize;
		yestDate = DateUtil.getYestDate().get(0);
		curDate = DateUtil.getYestDate().get(1);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case TYPE_TYPE:
				return new HolderType(LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false));
			case TYPE_TYPE_HEAD:
				return new HolderTypeHead(LayoutInflater.from(context).inflate(R.layout.item_photo_head, parent, false));
			default:
				return null;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof HolderTypeHead) {
			//起始位置
			bindTypeHead((HolderTypeHead) holder, position);
		} else if (holder instanceof HolderType) {
			bindType((HolderType) holder, position);
		}
	}

	@Override
	public int getItemCount() {
		return results.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (results.get(position).isTitle())
			return TYPE_TYPE_HEAD;
		else
			return TYPE_TYPE;
	}

	@Override
	public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
		if (manager instanceof GridLayoutManager) {
			final GridLayoutManager gridManager = ((GridLayoutManager) manager);
			gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
				@Override
				public int getSpanSize(int position) {
					int type = getItemViewType(position);
					switch (type) {
						case TYPE_TYPE_HEAD:
							return gridManager.getSpanCount();
						case TYPE_TYPE:
							return 1;
						default:
							return gridManager.getSpanCount();
					}
				}
			});
		}
	}

	private int getIndex() {
		int index = (int) (Math.random() * 5) + 1;
		return CPResourceUtil.getDrawableId(context, "commbg" + index);
	}

	private void bindTypeHead(final HolderTypeHead holder, final int position) {
		final String date = results.get(position).getDate();
		if (date.equals(yestDate)) {
			//昨天
			holder.photo_date_tv.setText("昨天");
		} else if (date.equals(curDate)) {
			//今天
			holder.photo_date_tv.setText("今天");
		} else {
			//其他日期
			holder.photo_date_tv.setText(date);
		}
		holder.photo_all_tv.setText(results.get(position).isAll() ? "取消全选" : "全选");
		//全选/反选
		holder.photo_all_tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//全选状态
				boolean isAll = results.get(position).isAll();
				if (mSelectList.size() < maxSize) {
					checkAll(holder, position, date);
				} else {
					if (isAll) {
						//全选->反选
						checkAll(holder, position, date);
					} else {
						//和最大数一样,不可以选择了
						onPhotoCallback.onMaxSize();
					}
				}
			}
		});
	}

	/**
	 * 全选
	 *
	 * @param holder
	 * @param position
	 * @param date
	 */
	private void checkAll(HolderTypeHead holder, int position, String date) {
		results.get(position).setIsAll(!results.get(position).isAll());
		holder.photo_all_tv.setText(results.get(position).isAll() ? "取消全选" : "全选");
		//TODO -判断
		changeList(date, results.get(position).isAll(), position);
		onPhotoCallback.onAll(results.get(position).isAll());
	}

	private void bindType(HolderType holder, final int position) {
		PhotoInfo info = results.get(position);
		if (mSelectList.contains(info)) {
			info.setIsActicted(true);
		} else {
			info.setIsActicted(false);
		}
		//默认图
		int index = getIndex();
		String url = info.getPhotoPath();
		Picasso.with(context).load("file://" + url).resize(240, 240).placeholder(index).error(index).centerCrop().into(holder.photo_iv);
		//被选中状态
		holder.photo_iv.setActivated(info.isActicted());
		holder.photo_select_iv.setSelected(info.isActicted());

		//选择点击
		holder.photo_select_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//当前状态
				boolean isActicted = results.get(position).isActicted();
				if (mSelectList.size() < maxSize) {
					checkSingle(position, isActicted);
				} else {
					if (isActicted) {
						checkSingle(position, isActicted);
					} else {
						//和最大数一样,不可以选择了
						onPhotoCallback.onMaxSize();
					}
				}
			}
		});
		//图片预览
		holder.photo_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onPhotoCallback.onPreView(position);
			}
		});
	}


	/**
	 * 单选
	 *
	 * @param position
	 */
	private void checkSingle(int position, boolean isActicted) {
		results.get(position).setIsActicted(!isActicted);
		changeSingle(results.get(position).isActicted(), position);
		onPhotoCallback.onSingle(results.get(position).isActicted(), position);
	}

	/**
	 * 全选/取消 改变
	 *
	 * @param date
	 * @param isActicted
	 */
	private void changeList(String date, boolean isActicted, int position) {
		int size = results.size();
		for (int i = 0; i < size; i++) {
			if (results.get(i).getDate().equals(date)) {
				if (isActicted && !results.get(i).isTitle()) {
					//全选并且集合中没有
					if (!mSelectList.contains(results.get(i)) && mSelectList.size() < maxSize) {
						mSelectList.add(results.get(i));
						results.get(i).setIsActicted(isActicted);
					}
				} else {
					//取消全选,从集合中拿掉
					if (mSelectList.contains(results.get(i))) {
						mSelectList.remove(results.get(i));
						results.get(i).setIsActicted(isActicted);
					}
				}
				notifyItemChanged(i);
			}
		}
	}

	/**
	 * 单选 改变
	 *
	 * @param isActicted
	 */
	private void changeSingle(boolean isActicted, int position) {
		if (isActicted && !mSelectList.contains(results.get(position)) && !results.get(position).isTitle()) {
			//全选并且集合中没有
			mSelectList.add(results.get(position));
		} else {
			//取消全选,从集合中拿掉
			mSelectList.remove(results.get(position));
		}
		notifyItemChanged(position);
		//头对应的位置
		int title = results.get(position).getStart();
		int start = results.get(title).getStart();
		int end = results.get(title).getEnd();

		boolean isAll = true;
		//遍历
		for (int i = start; i < end; i++) {
			if (!results.get(i).isActicted()) {
				isAll = false;
				break;
			}
		}
		CLog.e(TAG, "start:" + start + "  end:" + end + "  title:" + title);
		//全选了?
		results.get(title).setIsAll(isAll);
		notifyItemChanged(title);
	}

	/**
	 * 头
	 */
	public class HolderTypeHead extends RecyclerView.ViewHolder {
		@Bind(R.id.photo_date_tv)
		TextView photo_date_tv;//日期
		@Bind(R.id.photo_all_tv)
		TextView photo_all_tv;//全选

		public HolderTypeHead(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	/**
	 * 内容
	 */
	public class HolderType extends RecyclerView.ViewHolder {
		@Bind(R.id.photo_iv)
		MyTouchImageView photo_iv;//主图
		@Bind(R.id.photo_select_iv)
		ImageView photo_select_iv;//选择图标

		public HolderType(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

	public interface OnPhotoCallback {
		void onAll(boolean isAll);

		void onSingle(boolean isActicted, int positoin);

		void onPreView(int positoin);

		void onMaxSize();

	}

	private OnPhotoCallback onPhotoCallback;

	public void setOnPhotoCallback(PhotoRecycleAdapter.OnPhotoCallback onPhotoCallback) {
		this.onPhotoCallback = onPhotoCallback;
	}
}
