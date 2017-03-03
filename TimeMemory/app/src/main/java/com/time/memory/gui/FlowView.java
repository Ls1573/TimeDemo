package com.time.memory.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.time.memory.R;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:单行流布局
 * @date 2016/9/21 17:58
 */
public class FlowView extends ViewGroup {

	private static final String TAG = FlowView.class.getSimpleName();
	private static final Integer TYPE_TEXT_NORMAL = 1;
	private List<String> tags;

	private LayoutInflater mInflater;

	private int sizeWidth;
	private int sizeHeight;

	private float mTagSize;
	private int mTagColor;
	private int mViewBorder;
	private int mTagBorderHor;
	private int mTagBorderVer;

	private int mTagResId;
	private boolean mShowEndText;
	private String endTextString;

	private int imageWidth;
	private int imageHeight;

	private int endTextWidth = 0;
	private int endTextHeight = 0;
	private TextView endText = null;

	private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
	private static final int DEFAULT_TEXT_SIZE = 14;
	private static final int DEFAULT_VIEW_BORDER = 6;
	private static final int DEFAULT_TEXT_BORDER_HORIZONTAL = 8;
	private static final int DEFAULT_TEXT_BORDER_VERTICAL = 5;

	private static final int DEFAULT_TAG_RESID = R.layout.item_memory_tag;
	private static final boolean DEFAULT_SINGLE_LINE = true;
	private static final boolean DEFAULT_SHOW_END_TEXT = true;
	private static final String DEFAULT_END_TEXT_STRING = "...";

	public FlowView(Context context) {
		this(context, null);
	}

	public FlowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mInflater = LayoutInflater.from(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.FlowView,
				defStyleAttr,
				defStyleAttr
		);

		mTagSize = a.getInteger(R.styleable.FlowView_tcvTextSize, DEFAULT_TEXT_SIZE);
		mTagColor = a.getColor(R.styleable.FlowView_tcvTextColor, DEFAULT_TEXT_COLOR);
		mViewBorder = a.getDimensionPixelSize(R.styleable.FlowView_tcvBorder, DEFAULT_VIEW_BORDER);
		mTagBorderHor = a.getDimensionPixelSize(
				R.styleable.FlowView_tcvItemBorderHorizontal, DEFAULT_TEXT_BORDER_HORIZONTAL);
		mTagBorderVer = a.getDimensionPixelSize(
				R.styleable.FlowView_tcvItemBorderVertical, DEFAULT_TEXT_BORDER_VERTICAL);

		mShowEndText = a.getBoolean(R.styleable.FlowView_tcvShowEndText, DEFAULT_SHOW_END_TEXT);
		endTextString = a.getString(R.styleable.FlowView_tcvEndText);

		mTagResId = a.getResourceId(R.styleable.FlowView_tcvTagResId, DEFAULT_TAG_RESID);
		a.recycle();
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	}

	/**
	 * 计算 ChildView 宽高
	 *
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 计算 ViewGroup 上级容器为其推荐的宽高
		 */
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

		//计算 childView 宽高
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		initSingleLineView(widthMeasureSpec, heightMeasureSpec);

		int totalWidth = 0;
		int totalHeight = mTagBorderVer;

		totalHeight = getSingleTotalHeight(totalWidth, totalHeight);
		/**
		 * 高度根据设置改变
		 * 如果为 MATCH_PARENT 则充满父窗体，否则根据内容自定义高度
		 */
		setMeasuredDimension(
				sizeWidth,
				(heightMode == MeasureSpec.EXACTLY ? sizeHeight : totalHeight));

	}

	/**
	 * 初始化 singleLine 模式需要的视图
	 *
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	private void initSingleLineView(int widthMeasureSpec, int heightMeasureSpec) {
		if (mShowEndText) {
			endText = (TextView) mInflater.inflate(mTagResId, null);
			@SuppressLint("DrawAllocation")
			LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			endText.setLayoutParams(layoutParams);
			endText.setText(endTextString == null || endTextString.equals("") ? DEFAULT_END_TEXT_STRING : endTextString);
			measureChild(endText, widthMeasureSpec, heightMeasureSpec);
			endTextHeight = endText.getMeasuredHeight();
			endTextWidth = endText.getMeasuredWidth();
			addView(endText);
		}
	}

	/**
	 * 为 singleLine 模式布局，并计算视图高度
	 *
	 * @param totalWidth
	 * @param totalHeight
	 * @return
	 */
	private int getSingleTotalHeight(int totalWidth, int totalHeight) {
		int childWidth;
		int childHeight;

		totalWidth += mViewBorder;

		int textTotalWidth = getTextTotalWidth();
		if (textTotalWidth < sizeWidth - imageWidth) {
			endText = null;
			endTextWidth = 0;
		}

		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			childWidth = child.getMeasuredWidth();
			childHeight = child.getMeasuredHeight();


			if (i == 0) {
				totalWidth += childWidth;
				totalHeight = childHeight + mViewBorder;
			} else {
				totalWidth += childWidth + mTagBorderHor;
			}

			if (child.getTag() != null && child.getTag() == TYPE_TEXT_NORMAL) {
				if (totalWidth + mTagBorderHor + mViewBorder + mViewBorder + endTextWidth + imageWidth < sizeWidth) {
					child.layout(
							totalWidth - childWidth + mTagBorderVer,
							totalHeight - childHeight,
							totalWidth + mTagBorderVer,
							totalHeight);
				} else {
					totalWidth -= childWidth + mViewBorder;
					break;
				}
			}
		}

		if (endText != null) {
			endText.layout(
					totalWidth + mViewBorder + mTagBorderVer,
					totalHeight - endTextHeight,
					totalWidth + mViewBorder + mTagBorderVer + endTextWidth,
					totalHeight);
		}

		totalHeight += mViewBorder;
		return totalHeight;
	}


	private int getTextTotalWidth() {
		if (getChildCount() == 0) {
			return 0;
		}
		int totalChildWidth = 0;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			if (child.getTag() != null && (int) child.getTag() == TYPE_TEXT_NORMAL) {
				totalChildWidth += child.getMeasuredWidth() + mViewBorder;
			}
		}
		return totalChildWidth + mTagBorderHor * 2;
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return super.generateLayoutParams(attrs);
	}

	public void setTags(List<String> tagList) {
		this.tags = tagList;
		this.removeAllViews();
		if (tags != null && tags.size() > 0) {
			for (int i = 0; i < tags.size(); i++) {
				TextView tagView = (TextView) mInflater.inflate(mTagResId, null);
				LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tagView.setLayoutParams(layoutParams);
				tagView.setText(tags.get(i));
				tagView.setTag(TYPE_TEXT_NORMAL);
				addView(tagView);
			}
		}
		postInvalidate();
	}
}
