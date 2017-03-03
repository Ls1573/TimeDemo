package com.time.memory.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.MemoryGroup;
import com.time.memory.util.CLog;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.3
 * @Description:当行显示圈子名(怕又需要点击事件,直接用viewGroup了,如果需要点击事件好扩展)
 * @date 2017/1/4 9:27
 */
public class MemoryView extends ViewGroup {

	private static final String TAG = "MemoryView";

	private List<MemoryGroup> tags;
	private LayoutInflater mInflater;

	private int sizeWidth;
	private int sizeHeight;
	private int totalHeight;
	private int contrastWidth;//对比高度

	private int mTagResId;
	private int mTagBorder;

	private static final int DEFAULT_TAG_RESID = R.layout.item_my_memory;
	private static final int DEFAULT_VIEW_BORDER = 10;

	private List<Integer> indexList = new ArrayList<>();

	public MemoryView(Context context) {
		this(context, null);
	}

	public MemoryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MemoryView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mInflater = LayoutInflater.from(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TagView,
				defStyleAttr,
				defStyleAttr
		);

		mTagResId = a.getResourceId(R.styleable.TagView_tagTagResId, DEFAULT_TAG_RESID);
		mTagBorder = (int) a.getDimension(R.styleable.TagView_tagBorder, DEFAULT_VIEW_BORDER);
		a.recycle();
	}

	/**
	 * 计算 ChildView 宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 计算 ViewGroup 上级容器为其推荐的宽高
		 */
		sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

		//计算 childView 宽高
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		totalHeight = getChildAt(0).getMeasuredHeight();
		getMultiTotalHeight(totalHeight, widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(sizeWidth, totalHeight);

	}

	/**
	 * 获取一个对比实例的长度 如:«测试...»
	 *
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 * @param position
	 */
	private void addcontrastView(int widthMeasureSpec, int heightMeasureSpec, int position) {
		TextView view = (TextView) mInflater.inflate(DEFAULT_TAG_RESID, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.setText("«" + (tags.get(position).getName().length() >= 4 ? tags.get(position).getName().substring(0, 4) : tags.get(position).getName()) + "...»");
		measureChild(view, widthMeasureSpec, heightMeasureSpec);
		contrastWidth = view.getMeasuredWidth();
//		CLog.e(TAG, "********************contrastWidth:" + contrastWidth + "    :" + view.getText().toString());
	}

	/**
	 * 布局
	 *
	 * @param totalHeight
	 * @return
	 */
	private void getMultiTotalHeight(int totalHeight, int widthMeasureSpec, int heightMeasureSpec) {
		int totalWidth = 0;
		int childWidth, childWidth2;
		boolean isOver = false;

		int size = getChildCount();
		for (int i = 0; i < size && !isOver; i++) {
			View child = getChildAt(i);
			childWidth = child.getMeasuredWidth();
			totalWidth += childWidth;
			totalWidth += mTagBorder;
//			CLog.e(TAG, "childWidth:" + childWidth);

			//取下一个view计算宽度
			if (i + 1 < size) {
				View child2 = getChildAt(i + 1);
				childWidth2 = child2.getMeasuredWidth();

				addcontrastView(widthMeasureSpec, heightMeasureSpec, i + 1);
				//加入下一个view后宽度超范围了
				if (totalWidth + childWidth2 + mTagBorder > sizeWidth || totalWidth + contrastWidth + mTagBorder > sizeWidth) {
					//TODO
					if (totalWidth + contrastWidth + mTagBorder > sizeWidth) {
						//第二个view缩短4个字以内+...的话,也不可以
						//改变当前view的文字
						TextView tv = (TextView) child;
						if (i != 0) {
							tv.setText("«" + (tags.get(i).getName().length() >= 4 ? tags.get(i).getName().substring(0, 4) : tags.get(i).getName()) + "...»");
						} else {
							tv.setText("«" + tags.get(i).getName() + "...»");
						}
						isOver = true;
						layoutChild(i, child, isOver, totalWidth, childWidth);
						break;
						//只放一个
					} else {
						//第二个view缩短4个字+...的话,可以放进去,前一个保持不变-->只放这些了
						TextView tv = (TextView) child2;
						tv.setText("«" + tags.get(i).getName().substring(0, 4) + "...»");
						layoutChild(i, child, isOver, totalWidth, childWidth);
						layoutChild(i + 1, child2, isOver, totalWidth + childWidth2, childWidth2);
						break;
					}
				}
			}
			layoutChild(i, child, isOver, totalWidth, childWidth);
		}
	}

	private void layoutChild(int i, View child, boolean isOver, int totalWidth, int childWidth) {
		if (i == 0) {
			child.layout(0, 0, totalWidth, totalHeight);
		} else {
			if (isOver) {
				child.layout(totalWidth - childWidth, 0, sizeWidth, totalHeight);
			} else {
				child.layout(totalWidth - childWidth, 0, totalWidth, totalHeight);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return super.generateLayoutParams(attrs);
	}

	public void setTags(List<MemoryGroup> tagList) {
		this.tags = tagList;
		this.removeAllViews();
		if (tags != null && tags.size() > 0) {
			for (int i = 0; i < tags.size(); i++) {
				View view = (View) mInflater.inflate(mTagResId, null);
				TextView tagView = (TextView) view.findViewById(R.id.tag_tv);
				if (tagList.get(i).getSelf() == 0) {
					tagView.setText(tagList.get(i).getName());
					tagView.setTag(true);
				} else {
					tagView.setText("«" + tagList.get(i).getName() + "»");
				}
				addView(view);
			}
		}
		postInvalidate();
	}

}
