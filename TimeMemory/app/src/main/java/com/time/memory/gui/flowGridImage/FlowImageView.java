package com.time.memory.gui.flowGridImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.time.memory.R;

import java.util.ArrayList;
import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:定义图片流布局
 * @date 2016/9/21 18:56
 */
public class FlowImageView<T> extends ViewGroup {


	private static final String TAG ="FlowImageView" ;
	private int mRowCount;       // 行数
	private int mColumnCount;    // 列数

	private int mMaxSize;        // 最大图片数
	private int mGap;           // 宫格间距
	private int mGridSize;   // 宫格大小,即图片大小

	private List<ImageView> mImageViewList = new ArrayList<>();
	private List<T> mImgDataList;//图片的参数类型集合
	private FlowImageViewCallback<T> mAdapter;


	public FlowImageView(Context context) {
		this(context, null);
	}

	public FlowImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowImageView);
		this.mGap = (int) typedArray.getDimension(R.styleable.FlowImageView_fimgGap, 10);
		this.mMaxSize = typedArray.getInt(R.styleable.FlowImageView_fmaxSize, 10);
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height;
		int totalWidth = width - getPaddingLeft() - getPaddingRight();
		if (mImgDataList != null && mImgDataList.size() > 0) {
			mImageViewList.get(0).setScaleType(ImageView.ScaleType.CENTER_CROP);
			//图片大小
			mGridSize = (totalWidth - mGap * (mColumnCount - 1)) / mMaxSize;
			//总高度
			height = mGridSize * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();
		} else {
			//TODO 没有数据的时候高度暂设为0吧
			height = 0;
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutChildrenView();
	}

	/**
	 * 布局 ImageView
	 */
	private void layoutChildrenView() {
		if (mImgDataList == null) {
			return;
		}
		int childrenCount = mImgDataList.size();
		for (int i = 0; i < childrenCount; i++) {
			ImageView childrenView = (ImageView) getChildAt(i);
			if (mAdapter != null) {
				mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i));
			}
			int rowNum = i / mColumnCount;
			int columnNum = i % mColumnCount;
			int left = (mGridSize + mGap) * columnNum + getPaddingLeft();
			int top = (mGridSize + mGap) * rowNum + getPaddingTop();
			int right = left + mGridSize;
			int bottom = top + mGridSize;
			childrenView.layout(left, top, right, bottom);
		}
	}

	/**
	 * 设置图片数据
	 *
	 * @param lists 图片数据集合
	 */
	public void setImagesData(List lists) {
		if (lists == null || lists.isEmpty()) {
			this.setVisibility(GONE);
			return;
		} else {
			this.setVisibility(VISIBLE);
		}

		if (mMaxSize > 0 && lists.size() > mMaxSize) {
			//只取最大长度以内的
			lists = lists.subList(0, mMaxSize);
		}

		//计算行数，列数
		int[] gridParam = calculateGridParam(lists.size());
		//行数
		mRowCount = gridParam[0];
		//列数
		mColumnCount = gridParam[1];
		if (mImgDataList == null) {
			int i = 0;
			while (i < lists.size()) {
				ImageView iv = getImageView(i);
				if (iv == null) {
					return;
				}
				addView(iv, generateDefaultLayoutParams());
				i++;
			}
		} else {
			int oldViewCount = mImgDataList.size();
			int newViewCount = lists.size();
			if (oldViewCount > newViewCount) {
				removeViews(newViewCount, oldViewCount - newViewCount);
			} else if (oldViewCount < newViewCount) {
				for (int i = oldViewCount; i < newViewCount; i++) {
					ImageView iv = getImageView(i);
					if (iv == null) {
						return;
					}
					addView(iv, generateDefaultLayoutParams());
				}
			}
		}
		mImgDataList = lists;
		requestLayout();
	}

	/**
	 * 获得 ImageView
	 * 保证了 ImageView 的重用
	 *
	 * @param position 位置
	 */
	private ImageView getImageView(final int position) {
		if (position < mImageViewList.size()) {
			return mImageViewList.get(position);
		} else {
			if (mAdapter != null) {
				ImageView imageView = new ImageView(getContext());
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				mImageViewList.add(imageView);
				return imageView;
			} else {
				return null;
			}
		}
	}

	/**
	 * 设置 宫格参数
	 *
	 * @param imagesSize 图片数量
	 * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
	 */
	protected static int[] calculateGridParam(int imagesSize) {
		int[] gridParam = new int[2];
		if (imagesSize <=5) {
			gridParam[0] = 1;
			gridParam[1] = imagesSize;
		} else {
			gridParam[0] = 2;
			gridParam[1] = 5;
		}
		return gridParam;
	}

	/**
	 * 设置接口回调
	 */
	public void setFlowCallBack(FlowImageViewCallback adapter) {
		mAdapter = adapter;
	}

}