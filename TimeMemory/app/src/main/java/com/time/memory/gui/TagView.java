package com.time.memory.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Lable;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:多行流布局(添加标签)
 * @date 2016/9/26 9:46
 */
public class TagView extends ViewGroup {

	private static final String TAG = "TagView";
	private List<Lable> tags;

	private LayoutInflater mInflater;
	private OnTagClickListener onTagClickListener;

	private int sizeWidth;
	private int sizeHeight;

	private float mTagSize;
	private int mViewBorder;
	private int mTagBorderHor;
	private int mTagBorderVer;

	private int mTagResId;
	private int imageWidth;
	private int imageHeight;
	private ImageView imageView = null;

	private int endTextWidth = 0;
	private int endTextHeight = 0;
	private TextView endText = null;

	private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
	private static final int DEFAULT_VIEW_BORDER = 6;
	private static final int DEFAULT_TEXT_BORDER_HORIZONTAL = 0;
	private static final int DEFAULT_TEXT_BORDER_VERTICAL = 5;

	private static final int DEFAULT_TAG_RESID = R.layout.item_add_tag;
	private List<Integer> indexList = new ArrayList<>();
	private static final int SCALE_DURATION = 150;
	private static final int SCALEOUt_DURATION = 120;

	public TagView(Context context) {
		this(context, null);
	}

	public TagView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mInflater = LayoutInflater.from(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TagView,
				defStyleAttr,
				defStyleAttr
		);

		mViewBorder = a.getDimensionPixelSize(R.styleable.TagView_tagBorder, DEFAULT_VIEW_BORDER);
		mTagBorderHor = a.getDimensionPixelSize(
				R.styleable.TagView_tagItemBorderHorizontal, DEFAULT_TEXT_BORDER_HORIZONTAL);
		mTagBorderVer = a.getDimensionPixelSize(
				R.styleable.TagView_tagItemBorderVertical, DEFAULT_TEXT_BORDER_VERTICAL);
		mTagResId = a.getResourceId(R.styleable.TagView_tagTagResId, DEFAULT_TAG_RESID);

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

		int totalWidth = 0;
		int totalHeight = mTagBorderVer;

		totalHeight = getMultiTotalHeight(totalWidth, totalHeight);

		/**
		 * 高度根据设置改变
		 * 如果为 MATCH_PARENT 则充满父窗体，否则根据内容自定义高度
		 */
		setMeasuredDimension(
				sizeWidth,
				(heightMode == MeasureSpec.EXACTLY ? sizeHeight : totalHeight));

	}


	/**
	 * 为 multiLine 模式布局，并计算视图高度
	 *
	 * @param totalWidth
	 * @param totalHeight
	 * @return
	 */
	private int getMultiTotalHeight(int totalWidth, int totalHeight) {
		int childWidth;
		int childHeight;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			childWidth = child.getMeasuredWidth();
			childHeight = child.getMeasuredHeight();

			totalWidth += childWidth + mViewBorder;

			if (i == 0) {
				totalHeight = childHeight + mViewBorder;
			}
			// + marginLeft 保证最右侧与 ViewGroup 右边距有边界
			if (totalWidth + mTagBorderHor + mViewBorder > sizeWidth) {
				totalWidth = mViewBorder;
				totalHeight += childHeight + mTagBorderVer;
				child.layout(
						totalWidth + mTagBorderHor,
						totalHeight - childHeight,
						totalWidth + childWidth + mTagBorderHor,
						totalHeight);
				totalWidth += childWidth;
			} else {
				child.layout(
						totalWidth - childWidth + mTagBorderHor,
						totalHeight - childHeight,
						totalWidth + mTagBorderHor,
						totalHeight);
			}
		}
		return totalHeight + mViewBorder;
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return super.generateLayoutParams(attrs);
	}

	public void setTags(List<Lable> tagList, boolean isVisable) {
		this.tags = tagList;
		this.removeAllViews();
		if (tags != null && tags.size() > 0) {
			for (int i = 0; i < tags.size(); i++) {
				final int finalI = i;
				View view = (View) mInflater.inflate(mTagResId, null);
				TextView tagView = (TextView) view.findViewById(R.id.tag_tv);
				ImageView tagIv = (ImageView) view.findViewById(R.id.tag_iv);
				tagView.setText(tagList.get(i).getLabelName());
				tagIv.setVisibility(isVisable ? View.VISIBLE : View.GONE);
				indexList.add((Integer) i);
				tagView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (onTagClickListener != null) {
							onTagClickListener.onTagClick(indexList.indexOf(finalI));
						}
					}
				});
				tagIv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (onTagClickListener != null) {
							//删除
							onTagClickListener.onIvClick(finalI);
						}
					}
				});
				addView(view);
			}
		}
		postInvalidate();
	}

	/**
	 * 显示状态
	 *
	 * @param isVisable
	 */
	public void setVisable(boolean isVisable) {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			((ImageView) getChildAt(i).findViewById(R.id.tag_iv)).setVisibility(isVisable ? View.VISIBLE : View.GONE);
			if (isVisable)
				((ImageView) getChildAt(i).findViewById(R.id.tag_iv)).startAnimation(createScaleInAnimation());
			else
				((ImageView) getChildAt(i).findViewById(R.id.tag_iv)).startAnimation(createScaleOutAnimation());
		}
		invalidate();
	}


	/**
	 * 移除
	 *
	 * @param position
	 */
	public void removeView(int position) {
		int index = 0;
		//遍历
		for (int i = 0; i < indexList.size(); i++) {
			if (indexList.get(i) == position) {
				index = i;
				break;
			}
		}
		removeViewAt(index);
		indexList.remove(index);
//		tags.remove(position);
		onTagClickListener.onRemoveIndex(index);
	}

	/**
	 * 添加
	 */
	public void addView(Lable tag, boolean isVisable) {
		View view = (View) mInflater.inflate(mTagResId, null);
		TextView tagView = (TextView) view.findViewById(R.id.tag_tv);
		ImageView tagIv = (ImageView) view.findViewById(R.id.tag_iv);
		tagView.setText(tag.getLabelName());
		tagIv.setVisibility(isVisable ? View.VISIBLE : View.GONE);
		final int finalI = indexList.size();
		indexList.add((Integer) finalI);
//		if (tags == null)
//			tags = new ArrayList<>();
//		tags.add(tag);
		tagView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onTagClickListener != null) {
					onTagClickListener.onTagClick(indexList.indexOf(finalI));
				}
			}
		});
		tagIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onTagClickListener != null) {
					removeView(finalI);
				}
			}
		});
		addView(view);
	}

	public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
		this.onTagClickListener = onTagClickListener;
	}

	public interface OnTagClickListener {
		void onTagClick(int position);

		void onIvClick(Integer tag);

		void onRemoveIndex(int index);
	}

	// 创建缩放的动画
	private Animation createScaleInAnimation() {
		ScaleAnimation an = new ScaleAnimation(0.3f, 1f, 0.3f, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		an.setDuration(SCALE_DURATION);
		return an;
	}

	// 创建缩放的动画
	private Animation createScaleOutAnimation() {
		ScaleAnimation an = new ScaleAnimation(1f, 0.3f, 1f, 0.3f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		an.setDuration(SCALEOUt_DURATION);
		return an;
	}

}
