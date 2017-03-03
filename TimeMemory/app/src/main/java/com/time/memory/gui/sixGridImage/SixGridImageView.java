package com.time.memory.gui.sixGridImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.time.memory.R;
import com.time.memory.gui.BorderImageView;

import java.util.List;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:6宫图观看
 * @date 2016/10/25 11:13
 */
public class SixGridImageView<T> extends ViewGroup {

	private static final String TAG = "SixGridImageView";

	private int mMaxSize = 6;        // 最大图片数
	private int mGap;           // 宫格间距
	private int[] mGridWidth;   // 宫格大小,即图片宽度数据
	private int[] mGridHeight; // 宫格大小,即图片高度数据

	private List<T> mImgDataList;
	private SixGridImageViewAdapter<T> mAdapter;
	private int width;
	private int height;

	public SixGridImageView(Context context) {
		this(context, null);
	}

	public SixGridImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGridHeight = new int[6];
		mGridWidth = new int[6];
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridImageView);
		this.mGap = (int) typedArray.getDimension(R.styleable.NineGridImageView_imgGap, 0);
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

		if (mImgDataList != null && mImgDataList.size() > 0) {
			//分情况
			//一张图
			if (mImgDataList.size() == 1) {
				mGridHeight[0] = height;
				mGridWidth[0] = width;
			} else if (mImgDataList.size() == 2) {
				//二张图
				mGridHeight[0] = mGridHeight[1] = height;
				mGridWidth[0] = mGridWidth[1] = (width - mGap) / 2;
			} else if (mImgDataList.size() == 3) {
				//三张图
				mGridHeight[0] = height;
				mGridHeight[1] = mGridHeight[2] = (height - mGap) / 2;
				//计算宽度
				mGridWidth[0] = mGridWidth[1] = mGridWidth[2] = (width - mGap) / 2;
			} else if (mImgDataList.size() == 4) {
				//四张图
				mGridHeight[0] = mGridHeight[1] = mGridHeight[2] = mGridHeight[3] = (height - mGap) / 2;
				//宽度 0=3;1=2;
				mGridWidth[0] = mGridWidth[3] = (int) ((width - mGap) * 0.4375);
				mGridWidth[1] = mGridWidth[2] = width - mGap - mGridWidth[0];
			} else if (mImgDataList.size() == 5) {
				//五张图
				//270
				mGridWidth[0] = mGridWidth[1] = (int) ((width - mGap) * 0.421875);
				mGridWidth[2] = width - mGridWidth[0] - mGap;
				mGridWidth[3] = mGridWidth[4] = (mGridWidth[2] - mGap) / 2;

				//高度
				mGridHeight[0] = mGridHeight[1] = (height - mGap) / 2;
				mGridHeight[2] = (int) (height * 0.625);
				mGridHeight[3] = height - mGridHeight[2] - mGap;
				mGridHeight[4] = mGridHeight[3];
			} else {
				//六张图
				mGridHeight[0] = mGridHeight[1] = mGridHeight[2] = mGridHeight[3] = mGridHeight[4] = mGridHeight[5] = (height - mGap) / 2;

				mGridWidth[0] = mGridWidth[2] = (int) (width * 0.28125);
				mGridWidth[1] = width - 2 * mGridWidth[0] - 2 * mGap;
				mGridWidth[3] = mGridWidth[4] = mGridWidth[5] = (width - 2 * mGap) / 3;
			}
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
		//图片显示数量
		int childrenCount = mImgDataList.size();
		//按数量排列
		switch (childrenCount) {
			case 1:
				//一张图
				getImageOne();
				break;
			case 2:
				//两张图1
				getImageTwo();
				break;
			case 3:
				//三张图1
				getImageThree();
				break;
			case 4:
				//四张图
				getImageFour();
				break;
			case 5:
				//五张图
				getImageFive();
				break;
			case 6:
				//六张图
				getImageSix();
				break;
		}
	}

	/**
	 * 一张图
	 */
	private void getImageOne() {
		BorderImageView childrenView = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(0));
		}
		//计算位置
		int left = getPaddingLeft();
		int top = 0;
		int right = mGridWidth[0];
		int bottom = mGridHeight[0];
		childrenView.layout(left, top, right, bottom);
	}

	/**
	 * 两张图
	 */
	private void getImageTwo() {
		BorderImageView childrenView_2 = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_2, mImgDataList.get(0));
		}
		//计算位置
		int left_2 = getPaddingLeft();
		int top_2 = 0;
		int right_2 = mGridWidth[0];
		int bottom_2 = mGridHeight[0];
		childrenView_2.layout(left_2, top_2, right_2, bottom_2);
		//两张图2
		BorderImageView childrenView_2_1 = (BorderImageView) getChildAt(1);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_2_1, mImgDataList.get(1));
		}
		//计算位置
		int left_2_1 = mGridWidth[0] + mGap;
		int top_2_1 = 0;
		int right_2_1 = left_2_1 + mGridWidth[1];
		int bottom_2_1 = mGridHeight[0];
		childrenView_2_1.layout(left_2_1, top_2_1, right_2_1, bottom_2_1);
	}

	/**
	 * 三张图
	 */
	private void getImageThree() {
		BorderImageView childrenView_3_1 = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_3_1, mImgDataList.get(0));
		}
		//计算位置
		int left_3_1 = getPaddingLeft();
		int top_3_1 = 0;
		int right_3_1 = mGridWidth[0];
		int bottom_3_1 = mGridHeight[0];
		childrenView_3_1.layout(left_3_1, top_3_1, right_3_1, bottom_3_1);
		//三张图2
		BorderImageView childrenView_3_2 = (BorderImageView) getChildAt(1);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_3_2, mImgDataList.get(1));
		}
		//计算位置
		int left_3_2 = mGridWidth[1] + mGap;
		int top_3_2 = 0;
		int right_3_2 = left_3_2 + mGridWidth[1];
		int bottom_3_2 = mGridHeight[1] + top_3_2;
		childrenView_3_2.layout(left_3_2, top_3_2, right_3_2, bottom_3_2);
		//三张图3
		BorderImageView childrenView_3_3 = (BorderImageView) getChildAt(2);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_3_3, mImgDataList.get(2));
		}
		//计算位置
		int left_3_3 = mGridWidth[1] + mGap;
		int top_3_3 = bottom_3_2 + mGap;
		int right_3_3 = left_3_3 + mGridWidth[1];
		int bottom_3_3 = mGridHeight[1] + top_3_3;
		childrenView_3_3.layout(left_3_3, top_3_3, right_3_3, bottom_3_3);
	}

	/**
	 * 四张图
	 */
	private void getImageFour() {
		BorderImageView childrenView_4_1 = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_4_1, mImgDataList.get(0));
		}
		//计算位置
		int left_4_1 = getPaddingLeft();
		int top_4_1 = 0;
		int right_4_1 = mGridWidth[0];
		int bottom_4_1 = mGridHeight[0];
		childrenView_4_1.layout(left_4_1, top_4_1, right_4_1, bottom_4_1);
		//四张图2
		BorderImageView childrenView_4_2 = (BorderImageView) getChildAt(1);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_4_2, mImgDataList.get(1));
		}
		//计算位置
		int left_4_2 = mGridWidth[0] + mGap;
		int top_4_2 = 0;
		int right_4_2 = left_4_2 + mGridWidth[1];
		int bottom_4_2 = mGridHeight[1] + top_4_2;
		childrenView_4_2.layout(left_4_2, top_4_2, right_4_2, bottom_4_2);
		//四张图3
		BorderImageView childrenView_4_3 = (BorderImageView) getChildAt(2);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_4_3, mImgDataList.get(2));
		}
		//计算位置
		int left_4_3 = getPaddingLeft();
		int top_4_3 = bottom_4_1 + mGap;
		int right_4_3 = left_4_3 + mGridWidth[2];
		int bottom_4_3 = mGridHeight[2] + top_4_3;
		childrenView_4_3.layout(left_4_3, top_4_3, right_4_3, bottom_4_3);
		//四张图4
		BorderImageView childrenView_4_4 = (BorderImageView) getChildAt(3);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_4_4, mImgDataList.get(3));
		}
		//计算位置
		int left_4_4 = mGridWidth[2] + mGap;
		int top_4_4 = bottom_4_1 + mGap;
		int right_4_4 = left_4_4 + mGridWidth[3];
		int bottom_4_4 = mGridHeight[3] + top_4_4;
		childrenView_4_4.layout(left_4_4, top_4_4, right_4_4, bottom_4_4);
	}

	/**
	 * 五张图
	 */
	private void getImageFive() {
		BorderImageView childrenView_5_1 = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_1, mImgDataList.get(0));
		}
		//计算位置
		int left_5_1 = getPaddingLeft();
		int top_5_1 = 0;
		int right_5_1 = mGridWidth[0];
		int bottom_5_1 = mGridHeight[0];
		childrenView_5_1.layout(left_5_1, top_5_1, right_5_1, bottom_5_1);
		//五张图2
		BorderImageView childrenView_5_2 = (BorderImageView) getChildAt(1);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_2, mImgDataList.get(1));
		}
		//计算位置
		int left_5_2 = getPaddingLeft();
		int top_5_2 = bottom_5_1 + mGap;
		int right_5_2 = left_5_2 + mGridWidth[1];
		int bottom_5_2 = mGridHeight[1] + top_5_2;
		childrenView_5_2.layout(left_5_2, top_5_2, right_5_2, bottom_5_2);
		//五张图3
		BorderImageView childrenView_5_3 = (BorderImageView) getChildAt(2);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_3, mImgDataList.get(2));
		}
		//计算位置
		int left_5_3 = right_5_1 + mGap;
		int top_5_3 = 0;
		int right_5_3 = left_5_3 + mGridWidth[2];
		int bottom_5_3 = mGridHeight[2];
		childrenView_5_3.layout(left_5_3, top_5_3, right_5_3, bottom_5_3);
		//5张图4
		BorderImageView childrenView_5_4 = (BorderImageView) getChildAt(3);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_4, mImgDataList.get(3));
		}
		//计算位置
		int left_5_4 = left_5_3;
		int top_5_4 = bottom_5_3 + mGap;
		int right_5_4 = left_5_3 + mGridWidth[4];
		int bottom_5_4 = mGridHeight[4] + top_5_4;
		childrenView_5_4.layout(left_5_4, top_5_4, right_5_4, bottom_5_4);
		//5张图5
		BorderImageView childrenView_5_5 = (BorderImageView) getChildAt(4);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_5, mImgDataList.get(4));
		}
		//计算位置
		int left_5_5 = right_5_4 + mGap;
		int top_5_5 = bottom_5_3 + mGap;
		int right_5_5 = left_5_5 + mGridWidth[4];
		int bottom_5_5 = mGridHeight[4] + top_5_5;
		childrenView_5_5.layout(left_5_5, top_5_5, right_5_5, bottom_5_5);
	}

	/**
	 * 六张图
	 */
	private void getImageSix() {
		BorderImageView childrenView_6_1 = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_6_1, mImgDataList.get(0));
		}
		//计算位置
		int left_6_1 = getPaddingLeft();
		int top_6_1 = 0;
		int right_6_1 = mGridWidth[0];
		int bottom_6_1 = mGridHeight[0];
		childrenView_6_1.layout(left_6_1, top_6_1, right_6_1, bottom_6_1);
		//六张图2
		BorderImageView childrenView_6_2 = (BorderImageView) getChildAt(1);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_6_2, mImgDataList.get(1));
		}
		//计算位置
		int left_6_2 = right_6_1 + mGap;
		int top_6_2 = 0;
		int right_6_2 = left_6_2 + mGridWidth[1];
		int bottom_6_2 = mGridHeight[1] + top_6_2;
		childrenView_6_2.layout(left_6_2, top_6_2, right_6_2, bottom_6_2);
		//六张图3
		BorderImageView childrenView_6_3 = (BorderImageView) getChildAt(2);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_6_3, mImgDataList.get(2));
		}
		//计算位置
		int left_6_3 = right_6_2 + mGap;
		int top_6_3 = 0;
		int right_6_3 = left_6_3 + mGridWidth[2];
		int bottom_6_3 = mGridHeight[2];
		childrenView_6_3.layout(left_6_3, top_6_3, right_6_3, bottom_6_3);
		//6张图4
		BorderImageView childrenView_6_4 = (BorderImageView) getChildAt(3);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_6_4, mImgDataList.get(3));
		}
		//计算位置
		int left_6_4 = getPaddingLeft();
		int top_6_4 = bottom_6_3 + mGap;
		int right_6_4 = left_6_4 + mGridWidth[3];
		int bottom_6_4 = mGridHeight[3] + top_6_4;
		childrenView_6_4.layout(left_6_4, top_6_4, right_6_4, bottom_6_4);

		//6张图5
		BorderImageView childrenView_6_5 = (BorderImageView) getChildAt(4);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_6_5, mImgDataList.get(4));
		}
		//计算位置
		int left_6_5 = right_6_4 + mGap;
		int top_6_5 = bottom_6_3 + mGap;
		int right_6_5 = left_6_5 + mGridWidth[4];
		int bottom_6_5 = mGridHeight[4] + top_6_5;
		childrenView_6_5.layout(left_6_5, top_6_5, right_6_5, bottom_6_5);

		//6张图6
		BorderImageView childrenView_6_6 = (BorderImageView) getChildAt(5);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_6_6, mImgDataList.get(5));
		}
		//计算位置
		int left_6_6 = right_6_5 + mGap;
		int top_6_6 = bottom_6_3 + mGap;
		int right_6_6 = left_6_6 + mGridWidth[5];
		int bottom_6_6 = mGridHeight[5] + top_6_5;
		childrenView_6_6.layout(left_6_6, top_6_6, right_6_6, bottom_6_6);
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

		removeAllViews();
		int i = 0;
		while (i < lists.size()) {
			ImageView iv = getImageView(i);
			if (iv == null) {
				return;
			}
			addView(iv, generateDefaultLayoutParams());
			i++;
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
	private BorderImageView getImageView(final int position) {
		BorderImageView imageView = mAdapter.generateImageView(getContext());
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapter.onItemImageClick(position);
			}
		});
		return imageView;
	}


	/**
	 * 设置适配器
	 *
	 * @param adapter 适配器
	 */

	public void setAdapter(SixGridImageViewAdapter adapter) {
		mAdapter = adapter;
	}
}