package com.time.memory.view.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.R;
import com.time.memory.core.callback.AdapterCallback;
import com.time.memory.core.callback.IHolderCallBack;
import com.time.memory.entity.OtherMemory;
import com.time.memory.entity.OtherMemorys;
import com.time.memory.gui.sticky.GridSLM;
import com.time.memory.gui.sticky.LinearSLM;
import com.time.memory.gui.sticky.SectionAdapter;
import com.time.memory.util.CLog;
import com.time.memory.view.holder.BaseHolder;
import com.time.memory.view.holder.OtherTimeHolder;
import com.time.memory.view.holder.OtherTimeItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:他的时间轴(第二次修改)
 * @date 2016/11/9 16:25
 */
public class OtherTimeAdapter extends RecyclerView.Adapter<BaseHolder> implements SectionAdapter<OtherTimeAdapter.Section>, IHolderCallBack {

	private static final String TAG = "TimeAdapter";

	private static final int VIEW_TYPE_HEADER = 0x01;
	private static final int VIEW_TYPE_CONTENT = 0x00;
	private final Section mSectionGraph = new Section();
	private int mHeaderDisplay = 9;
	private final Context mContext;
	private AdapterCallback mCallback;

	public OtherTimeAdapter(Context context) {
		mContext = context;
	}

	public void clearEmpty() {
		if (mSectionGraph.getSubsections() != null)
			mSectionGraph.getSubsections().clear();
	}

	public void addAll(List<OtherMemorys> list) {
		convertData(list);
		notifyDataSetChanged();
	}

	public void showEmpry() {
		if (getItemCount() > 0) {
			mSectionGraph.getItem(getItemCount() - 1).setState(2);
//			notifyItemChanged(getItemCount() - 1);
			notifyDataSetChanged();
		}
	}

	public void setCallBack(AdapterCallback callback) {
		this.mCallback = callback;
	}

	public void unRegisterCallBack() {
		if (mCallback != null) {
			mCallback = null;
		}
	}

	//转换数据
	private void convertData(List<OtherMemorys> mlist) {
		if (getItemCount() != 0) {
			mSectionGraph.getItem(getItemCount() - 1).setIsLast(false);
		}
		Section currentSection;
		for (int i = 0; i < mlist.size(); i++) {
			OtherMemorys OtherMemorys = mlist.get(i);
			currentSection = new Section(OtherMemorys.getStart());
			currentSection.setEnd(OtherMemorys.getEnd());
			OtherMemorys.setIsHeader(true);
			currentSection.addHeader(OtherMemorys);
			//头部
			mSectionGraph.addSubsection(currentSection);
			CLog.e(TAG, "start:" + OtherMemorys.getStart() + "   end:" + OtherMemorys.getEnd());
			//内容
			for (OtherMemory memory : mlist.get(i).getList()) {
				currentSection.addItem(new OtherMemorys(memory, false, mlist.get(i).getStart(), memory.getPosition()));
			}
		}
		mSectionGraph.getItem(getItemCount() - 1).setIsLast(true);
	}

	@Override
	public List<Section> getSections() {
		return mSectionGraph.getSubsections();
	}

	@Override
	public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		if (viewType == VIEW_TYPE_HEADER) {
			view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_time_header, parent, false);
			return new OtherTimeHolder(view);
		} else {
			view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_memory_sign, parent, false);
			return new OtherTimeItemHolder(view);
		}
	}

	@Override
	public void onBindViewHolder(BaseHolder holder, int position) {
		View itemView = holder.itemView;
		OtherMemorys item = mSectionGraph.getItem(position);
		holder.setData(item, position, this);
		GridSLM.LayoutParams lp = new GridSLM.LayoutParams(
				itemView.getLayoutParams());
		if (item.isHeader) {
			lp.headerDisplay = mHeaderDisplay;
			lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
		}
		if (position == item.getStart()) {
			lp.setSlm(LinearSLM.ID);
		}
		itemView.setLayoutParams(lp);
	}

	@Override
	public int getItemViewType(int position) {
		if (mSectionGraph.getItem(position).isHeader) {
			return VIEW_TYPE_HEADER;
		} else {
			return VIEW_TYPE_CONTENT;
		}
	}

	@Override
	public int getItemCount() {
		return mSectionGraph.getCount();
	}

	private void notifyHeaderChanges() {
		final int count = mSectionGraph.getCount();
		for (int i = 0; i < count; i++) {
			final OtherMemorys item = mSectionGraph.getItem(i);
			if (item.isHeader) {
				notifyItemChanged(i);
			}
		}
	}

	@Override
	public void onClick(int position) {
		if (mCallback != null)
			mCallback.onCallback(position);
	}

	@Override
	public void onLongClick(int position) {
		if (mCallback != null)
			mCallback.onLongCallBack(null, position);
	}

	@Override
	public void onClick(Object obj) {
		if (mCallback != null)
			mCallback.onCallback(obj);
	}

	@Override
	public void onClick(int position, Object obj) {
		if (mCallback != null)
			mCallback.onDataCallBack(obj, position);
	}

	@Override
	public void onClick(int position, int index, Object obj) {
		if (mCallback != null)
			mCallback.onDataCallBack(obj, position, index);
	}

	@Override
	public void onClick(int position, int index, Object obj, int tag) {

	}

	public static class Section extends SectionAdapter.Section<Section> {

		private ArrayList<OtherMemorys> mItems = new ArrayList<>();

		private OtherMemorys mHeader;

		public Section() {
		}

		public Section(int start) {
			super(start);
		}

		public Section addHeader(OtherMemorys header) {
			mHeader = header;
			return this;
		}

		public Section addItem(OtherMemorys item) {
			mItems.add(item);
			return this;
		}

		public Section addSubsection(Section section) {
			if (subsections == null) {
				subsections = new ArrayList<>();
			}
			subsections.add(section);
			return this;
		}

		public int getCount() {
			int sum = mHeader == null ? 0 : 1;
			if (subsections != null && subsections.size() != 0) {
				for (Section sub : subsections) {
					sum += sub.getCount();
				}
			} else {
				sum += mItems.size();
			}
			return sum;
		}

		public OtherMemorys getItem(int position) {
			if (mHeader != null && position == start) {
				return mHeader;
			}

			if (subsections != null) {
				for (Section sub : subsections) {
					if (sub.contains(position)) {
						return sub.getItem(position);
					}
				}
			}
			return mItems.get(position - start - (mHeader != null ? 1 : 0));
		}

		private boolean contains(int position) {
			return start <= position && position <= end;
		}
	}
}
