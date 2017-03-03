package com.time.memory.gui.sixGridImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.time.memory.R;
import com.time.memory.gui.BorderImageView;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.toolsfinal.DeviceUtils;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:6宫图写入
 * @date 2016/10/9 18:14
 */
public class SixGridImageWriterView<T> extends ViewGroup {

	private static final String TAG = "SixGridImageView";
	private int mMaxSize = 6;        // 最大图片数
	private int mGap;           // 宫格间距
	private int[] mGridWidth;   // 宫格大小,即图片宽度数据
	private int[] mGridHeight; // 宫格大小,即图片高度数据

	private List<BorderImageView> mImageViewList = new ArrayList<>();
	private List<T> mImgDataList;
	private SixGridImageViewAdapter<T> mAdapter;
	private int width;
	private int height;
	private int deWidth;//删除图片的大小
	private int dePadding;//删除图片的边距

	public SixGridImageWriterView(Context context) {
		this(context, null);
	}

	public SixGridImageWriterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGridHeight = new int[6];
		mGridWidth = new int[6];
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridImageView);
		this.mGap = (int) typedArray.getDimension(R.styleable.NineGridImageView_imgGap, 0);
		this.deWidth = (int) typedArray.getDimension(R.styleable.NineGridImageView_deWidth, 80);
		this.dePadding = (int) typedArray.getDimension(R.styleable.NineGridImageView_dePadding, 15);
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

		if (mImgDataList != null && mImgDataList.size() > 0) {
			//分情况
			if (mImgDataList.size() == 1) {
				//一张图
				mGridHeight[0] = mGridHeight[1] = height;
				mGridWidth[0] = mGridWidth[1] = (width - mGap) / 2;
			} else if (mImgDataList.size() == 2) {
				//二张图
				mGridHeight[0] = height;
				mGridHeight[1] = mGridHeight[2] = (height - mGap) / 2;
				//计算宽度
				mGridWidth[0] = mGridWidth[1] = mGridWidth[2] = (width - mGap) / 2;
			} else if (mImgDataList.size() == 3) {
				//三张图
				mGridHeight[0] = mGridHeight[1] = mGridHeight[2] = mGridHeight[3] = (height - mGap) / 2;
				//宽度 0=3;1=2;
				mGridWidth[0] = mGridWidth[3] = (int) ((width - mGap) * 0.4375);
				mGridWidth[1] = mGridWidth[2] = width - mGap - mGridWidth[0];
			} else if (mImgDataList.size() == 4) {
				//四张图
				//270
				mGridWidth[0] = mGridWidth[1] = (int) ((width - mGap) * 0.421875);
				mGridWidth[2] = width - mGridWidth[0] - mGap;
				mGridWidth[3] = mGridWidth[4] = (mGridWidth[2] - mGap) / 2;
				//高度
				mGridHeight[0] = mGridHeight[1] = (height - mGap) / 2;
				mGridHeight[2] = (int) (height * 0.625);
				mGridHeight[3] = mGridHeight[4] = height - mGridHeight[2] - mGap;
			} else {
				//五,六张图
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
			case 0:
				//0张图
				getImageZero();
				break;
			case 1:
				//一张图
				getImageOne();
				break;
			case 2:
				//两张图
				getImageTwo();
				break;
			case 3:
				//三张图
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
	 * 零张图
	 */
	private void getImageZero() {
		//图2
		BorderImageView childrenView0_1 = (BorderImageView) getChildAt(0);
		//计算位置
		int left0_1 = getPaddingLeft();
		int top0_1 = getPaddingTop();
		int right0_1 = width;
		int bottom0_1 = height;
		childrenView0_1.layout(left0_1, top0_1, right0_1, bottom0_1);
	}

	/**
	 * 一张图
	 */
	private void getImageOne() {
		//图1
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
		//图2
		BorderImageView childrenView1_2 = (BorderImageView) getChildAt(1);
		//计算位置
		int left1_2 = mGridWidth[0] + mGap;
		int top1_2 = 0;
		int right1_2 = mGridWidth[1] + left1_2;
		int bottom1_2 = mGridHeight[1];
		childrenView1_2.layout(left1_2, top1_2, right1_2, bottom1_2);
		//图1的删除图
		BorderImageView delView = (BorderImageView) getChildAt(2);
		//计算位置
		int left1_3 = mGridWidth[0] - deWidth - dePadding;
		int top1_3 = mGridHeight[0] - deWidth - dePadding;
		int right1_3 = mGridWidth[0] - dePadding;
		int bottom1_3 = mGridHeight[0] - dePadding;
		delView.layout(left1_3, top1_3, right1_3, bottom1_3);
	}

	/**
	 * 两张图
	 */
	private void getImageTwo() {
		BorderImageView childrenView_2_1 = (BorderImageView) getChildAt(0);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_2_1, mImgDataList.get(0));
		}
		//计算位置
		int left_2_1 = getPaddingLeft();
		int top_2_1 = 0;
		int right_2_1 = mGridWidth[0];
		int bottom_2_1 = mGridHeight[0];
		childrenView_2_1.layout(left_2_1, top_2_1, right_2_1, bottom_2_1);
		//两张图2
		BorderImageView childrenView_2_2 = (BorderImageView) getChildAt(1);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_2_2, mImgDataList.get(1));
		}
		//计算位置
		int left_2_2 = mGridWidth[1] + mGap;
		int top_2_2 = 0;
		int right_2_2 = left_2_2 + mGridWidth[1];
		int bottom_2_2 = mGridHeight[1] + top_2_2;
		childrenView_2_2.layout(left_2_2, top_2_2, right_2_2, bottom_2_2);

		//两张图3
		BorderImageView childrenView_2_3 = (BorderImageView) getChildAt(2);
		//计算位置
		int left_2_3 = mGridWidth[1] + mGap;
		int top_2_3 = bottom_2_2 + mGap;
		int right_2_3 = left_2_3 + mGridWidth[1];
		int bottom_2_3 = mGridHeight[1] + top_2_3;
		childrenView_2_3.layout(left_2_3, top_2_3, right_2_3, bottom_2_3);

		//图1的删除图
		BorderImageView delView2_1_1 = (BorderImageView) getChildAt(3);
		//计算位置
		int left2_1_1 = mGridWidth[0] - deWidth - dePadding;
		int top2_1_1 = mGridHeight[0] - deWidth - dePadding;
		int right2_1_1 = mGridWidth[0] - dePadding;
		int bottom2_1_1 = mGridHeight[0] - dePadding;
		delView2_1_1.layout(left2_1_1, top2_1_1, right2_1_1, bottom2_1_1);

		//图2的删除图
		BorderImageView delView2_2_1 = (BorderImageView) getChildAt(4);
		//计算位置
		int left2_2_1 = width - deWidth - dePadding;
		int top2_2_1 = mGridHeight[1] - deWidth - dePadding;
		int right2_2_1 = width - dePadding;
		int bottom2_2_1 = mGridHeight[1] - dePadding;
		delView2_2_1.layout(left2_2_1, top2_2_1, right2_2_1, bottom2_2_1);
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
		int left_3_2 = mGridWidth[0] + mGap;
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
		int left_3_3 = getPaddingLeft();
		int top_3_3 = bottom_3_1 + mGap;
		int right_3_3 = left_3_3 + mGridWidth[2];
		int bottom_3_3 = mGridHeight[2] + top_3_3;
		childrenView_3_3.layout(left_3_3, top_3_3, right_3_3, bottom_3_3);
		//三张图4
		BorderImageView childrenView_3_4 = (BorderImageView) getChildAt(3);
		//计算位置
		int left_3_4 = mGridWidth[2] + mGap;
		int top_3_4 = bottom_3_1 + mGap;
		int right_3_4 = left_3_4 + mGridWidth[3];
		int bottom_3_4 = mGridHeight[3] + top_3_4;
		childrenView_3_4.layout(left_3_4, top_3_4, right_3_4, bottom_3_4);

		//图1的删除图
		BorderImageView delView3_1_1 = (BorderImageView) getChildAt(4);
		//计算位置
		int left3_1_1 = mGridWidth[0] - deWidth - dePadding;
		int top3_1_1 = mGridHeight[0] - deWidth - dePadding;
		int right3_1_1 = mGridWidth[0] - dePadding;
		int bottom3_1_1 = mGridHeight[0] - dePadding;
		delView3_1_1.layout(left3_1_1, top3_1_1, right3_1_1, bottom3_1_1);

		//图2的删除图
		BorderImageView delView3_2_1 = (BorderImageView) getChildAt(5);
		//计算位置
		int left3_2_1 = width - deWidth - dePadding;
		int top3_2_1 = mGridHeight[1] - deWidth - dePadding;
		int right3_2_1 = width - dePadding;
		int bottom3_2_1 = mGridHeight[1] - dePadding;
		delView3_2_1.layout(left3_2_1, top3_2_1, right3_2_1, bottom3_2_1);

		//图3的删除图
		BorderImageView delView3_3_1 = (BorderImageView) getChildAt(6);
		//计算位置
		int left3_3_1 = mGridWidth[2] - deWidth - dePadding;
		int top3_3_1 = height - deWidth - dePadding;
		int right3_3_1 = mGridWidth[2] - dePadding;
		int bottom3_3_1 = height - dePadding;
		delView3_3_1.layout(left3_3_1, top3_3_1, right3_3_1, bottom3_3_1);
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
		int left_4_2 = getPaddingLeft();
		int top_4_2 = bottom_4_1 + mGap;
		int right_4_2 = left_4_2 + mGridWidth[1];
		int bottom_4_2 = mGridHeight[1] + top_4_2;
		childrenView_4_2.layout(left_4_2, top_4_2, right_4_2, bottom_4_2);
		//四张图3
		BorderImageView childrenView_4_3 = (BorderImageView) getChildAt(2);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_4_3, mImgDataList.get(2));
		}
		//计算位置
		int left_4_3 = right_4_1 + mGap;
		int top_4_3 = 0;
		int right_4_3 = left_4_3 + mGridWidth[2];
		int bottom_4_3 = mGridHeight[2];
		childrenView_4_3.layout(left_4_3, top_4_3, right_4_3, bottom_4_3);

		//四张图4
		BorderImageView childrenView_4_4 = (BorderImageView) getChildAt(3);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_4_4, mImgDataList.get(3));
		}
		//计算位置
		int left_4_4 = left_4_3;
		int top_4_4 = bottom_4_3 + mGap;
		int right_4_4 = left_4_3 + mGridWidth[4];
		int bottom_4_4 = mGridHeight[4] + top_4_4;
		childrenView_4_4.layout(left_4_4, top_4_4, right_4_4, bottom_4_4);

		//四张图5
		BorderImageView childrenView_4_5 = (BorderImageView) getChildAt(4);
		//计算位置
		int left_4_5 = right_4_4 + mGap;
		int top_4_5 = bottom_4_3 + mGap;
		int right_4_5 = left_4_5 + mGridWidth[4];
		int bottom_4_5 = mGridHeight[4] + top_4_5;
		childrenView_4_5.layout(left_4_5, top_4_5, right_4_5, bottom_4_5);

		//图1的删除图
		BorderImageView delView4_1_1 = (BorderImageView) getChildAt(5);
		//计算位置
		int left4_1_1 = mGridWidth[0] - deWidth - dePadding;
		int top4_1_1 = mGridHeight[0] - deWidth - dePadding;
		int right4_1_1 = mGridWidth[0] - dePadding;
		int bottom4_1_1 = mGridHeight[0] - dePadding;
		delView4_1_1.layout(left4_1_1, top4_1_1, right4_1_1, bottom4_1_1);

		//图2的删除图
		BorderImageView delView4_2_1 = (BorderImageView) getChildAt(6);
		//计算位置
		int left4_2_1 = mGridWidth[1] - deWidth - dePadding;
		int top4_2_1 = height - dePadding - deWidth;
		int right4_2_1 = mGridWidth[1] - dePadding;
		int bottom4_2_1 = height - dePadding;
		delView4_2_1.layout(left4_2_1, top4_2_1, right4_2_1, bottom4_2_1);

		//图3的删除图
		BorderImageView delView4_3_1 = (BorderImageView) getChildAt(7);
		//计算位置
		int left4_3_1 = width - dePadding - deWidth;
		int top4_3_1 = mGridHeight[2] - deWidth - dePadding;
		int right4_3_1 = width - dePadding;
		int bottom4_3_1 = mGridHeight[2] - dePadding;
		delView4_3_1.layout(left4_3_1, top4_3_1, right4_3_1, bottom4_3_1);

		//图4的删除图
		BorderImageView delView4_4_1 = (BorderImageView) getChildAt(8);
		//计算位置
		int left4_4_1 = right_4_4 - deWidth - dePadding;
		int top4_4_1 = height - deWidth - dePadding;
		int right4_4_1 = right_4_4 - dePadding;
		int bottom4_4_1 = height - dePadding;
		delView4_4_1.layout(left4_4_1, top4_4_1, right4_4_1, bottom4_4_1);

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
		int left_5_2 = right_5_1 + mGap;
		int top_5_2 = 0;
		int right_5_2 = left_5_2 + mGridWidth[1];
		int bottom_5_2 = mGridHeight[1] + top_5_2;
		childrenView_5_2.layout(left_5_2, top_5_2, right_5_2, bottom_5_2);
		//五张图3
		BorderImageView childrenView_5_3 = (BorderImageView) getChildAt(2);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_3, mImgDataList.get(2));
		}
		//计算位置
		int left_5_3 = right_5_2 + mGap;
		int top_5_3 = 0;
		int right_5_3 = left_5_3 + mGridWidth[2];
		int bottom_5_3 = mGridHeight[2];
		childrenView_5_3.layout(left_5_3, top_5_3, right_5_3, bottom_5_3);
		//五张图4
		BorderImageView childrenView_5_4 = (BorderImageView) getChildAt(3);
		if (mAdapter != null) {
			mAdapter.onDisplayImage(getContext(), childrenView_5_4, mImgDataList.get(3));
		}
		//计算位置
		int left_5_4 = getPaddingLeft();
		int top_5_4 = bottom_5_3 + mGap;
		int right_5_4 = left_5_4 + mGridWidth[3];
		int bottom_5_4 = mGridHeight[3] + top_5_4;
		childrenView_5_4.layout(left_5_4, top_5_4, right_5_4, bottom_5_4);

		//五张图5
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

		//五张图6
		BorderImageView childrenView_5_6 = (BorderImageView) getChildAt(5);
		//计算位置
		int left_5_6 = right_5_5 + mGap;
		int top_5_6 = bottom_5_3 + mGap;
		int right_5_6 = left_5_6 + mGridWidth[5];
		int bottom_5_6 = mGridHeight[5] + top_5_5;
		childrenView_5_6.layout(left_5_6, top_5_6, right_5_6, bottom_5_6);

		//图1的删除图
		BorderImageView delView5_1_1 = (BorderImageView) getChildAt(6);
		//计算位置
		int left5_1_1 = mGridWidth[0] - deWidth - dePadding;
		int top5_1_1 = mGridHeight[0] - deWidth - dePadding;
		int right5_1_1 = mGridWidth[0] - dePadding;
		int bottom5_1_1 = mGridHeight[0] - dePadding;
		delView5_1_1.layout(left5_1_1, top5_1_1, right5_1_1, bottom5_1_1);

		//图2的删除图
		BorderImageView delView5_2_1 = (BorderImageView) getChildAt(7);
		//计算位置
		int left5_2_1 = right_5_2 - deWidth - dePadding;
		int top5_2_1 = top5_1_1;
		int right5_2_1 = right_5_2 - dePadding;
		int bottom5_2_1 = bottom5_1_1;
		delView5_2_1.layout(left5_2_1, top5_2_1, right5_2_1, bottom5_2_1);

		//图3的删除图
		BorderImageView delView5_3_1 = (BorderImageView) getChildAt(8);
		//计算位置
		int left5_3_1 = width - dePadding - deWidth;
		int top5_3_1 = top5_1_1;
		int right5_3_1 = width - dePadding;
		int bottom5_3_1 = bottom5_1_1;
		delView5_3_1.layout(left5_3_1, top5_3_1, right5_3_1, bottom5_3_1);

		//图4的删除图
		BorderImageView delView5_4_1 = (BorderImageView) getChildAt(9);
		//计算位置
		int left5_4_1 = right_5_4 - deWidth - dePadding;
		int top5_4_1 = height - deWidth - dePadding;
		int right5_4_1 = right_5_4 - dePadding;
		int bottom5_4_1 = height - dePadding;
		delView5_4_1.layout(left5_4_1, top5_4_1, right5_4_1, bottom5_4_1);

		//图5的删除图
		BorderImageView delView5_5_1 = (BorderImageView) getChildAt(10);
		//计算位置
		int left5_5_1 = right_5_5 - deWidth - dePadding;
		int top5_5_1 = top5_4_1;
		int right5_5_1 = right_5_5 - dePadding;
		int bottom5_5_1 = bottom5_4_1;
		delView5_5_1.layout(left5_5_1, top5_5_1, right5_5_1, bottom5_5_1);
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


		//图1的删除图
		BorderImageView delView6_1_1 = (BorderImageView) getChildAt(6);
		//计算位置
		int left6_1_1 = mGridWidth[0] - deWidth - dePadding;
		int top6_1_1 = mGridHeight[0] - deWidth - dePadding;
		int right6_1_1 = mGridWidth[0] - dePadding;
		int bottom6_1_1 = mGridHeight[0] - dePadding;
		delView6_1_1.layout(left6_1_1, top6_1_1, right6_1_1, bottom6_1_1);

		//图2的删除图
		BorderImageView delView6_2_1 = (BorderImageView) getChildAt(7);
		//计算位置
		int left6_2_1 = right_6_2 - deWidth - dePadding;
		int top6_2_1 = top6_1_1;
		int right6_2_1 = right_6_2 - dePadding;
		int bottom6_2_1 = bottom6_1_1;
		delView6_2_1.layout(left6_2_1, top6_2_1, right6_2_1, bottom6_2_1);

		//图3的删除图
		BorderImageView delView6_3_1 = (BorderImageView) getChildAt(8);
		//计算位置
		int left6_3_1 = width - dePadding - deWidth;
		int top6_3_1 = top6_1_1;
		int right6_3_1 = width - dePadding;
		int bottom6_3_1 = bottom6_1_1;
		delView6_3_1.layout(left6_3_1, top6_3_1, right6_3_1, bottom6_3_1);

		//图4的删除图
		BorderImageView delView6_4_1 = (BorderImageView) getChildAt(9);
		//计算位置
		int left6_4_1 = right_6_4 - deWidth - dePadding;
		int top6_4_1 = height - deWidth - dePadding;
		int right6_4_1 = right_6_4 - dePadding;
		int bottom6_4_1 = height - dePadding;
		delView6_4_1.layout(left6_4_1, top6_4_1, right6_4_1, bottom6_4_1);

		//图5的删除图
		BorderImageView delView6_5_1 = (BorderImageView) getChildAt(10);
		//计算位置
		int left6_5_1 = right_6_5 - deWidth - dePadding;
		int top6_5_1 = top6_4_1;
		int right6_5_1 = right_6_5 - dePadding;
		int bottom6_5_1 = bottom6_4_1;
		delView6_5_1.layout(left6_5_1, top6_5_1, right6_5_1, bottom6_5_1);

		//图6的删除图
		BorderImageView delView6_6_1 = (BorderImageView) getChildAt(11);
		//计算位置
		int left6_6_1 = width - deWidth - dePadding;
		int top6_6_1 = top6_4_1;
		int right6_6_1 = width - dePadding;
		int bottom6_6_1 = bottom6_4_1;
		delView6_6_1.layout(left6_6_1, top6_6_1, right6_6_1, bottom6_6_1);
	}

	/**
	 * 设置图片数据
	 *
	 * @param lists 图片数据集合
	 */
	public void setImagesData(List lists) {
		if (mMaxSize > 0 && lists.size() > mMaxSize) {
			lists = lists.subList(0, mMaxSize);
		}
		removeAllViews();
		int i = 0;
		int size = lists.size();
		while (i < size) {
			BorderImageView iv = getImageView(i, size, 1);
			if (iv == null) {
				return;
			}
			addView(iv, generateDefaultLayoutParams());
			i++;
		}
		//加入额外的(添加+删除)
		addExtralImage(size);
		mImgDataList = lists;
		requestLayout();
	}

	private void addExtralImage(int size) {
		if (size < 6) {
			//添加
			int resource = R.drawable.writer_two;
			BorderImageView iv = null;
			switch (size) {
				case 0:
					//0个元素
					iv = getImageView(0, 0, 2);
					resource = R.drawable.writer_one;
					break;
				case 1:
					iv = getImageView(1, 1, 2);
					resource = R.drawable.writer_two;
					break;
				case 2:
					iv = getImageView(2, 2, 2);
					resource = R.drawable.writer_three;
					break;
				case 3:
					iv = getImageView(3, 3, 2);
					resource = R.drawable.writer_four;
					break;
				case 4:
					iv = getImageView(4, 4, 2);
					resource = R.drawable.writer_five;
					break;
				case 5:
					iv = getImageView(5, 5, 2);
					resource = R.drawable.writer_six;
					break;
			}
			if (iv == null) return;
			iv.setBackgroundResource(resource);
			addView(iv, generateDefaultLayoutParams());
		}
		//1:从2开始
		//2:从3开始    3,4
		//3:从4开始    4,5,6
		//4:从5开始    5,6,7,8
		//5:从6开始	  6,7,8,9,10
		//6:从6开始    6,7,8,9,10,11
		//删除
		BorderImageView delIv;
		//删除
		int deResource = R.drawable.memorypde_;
		int start = size + 1;
		int end = size * 2 + 1;
		if (size == 6) {
			start = size;
			end = size * 2;
		}
		for (int i = start; i < end; i++) {
			delIv = getImageView(i, start, 3);
			delIv.setBackgroundResource(deResource);
			addView(delIv, generateDefaultLayoutParams());
		}
	}

	/**
	 * 获得 ImageView
	 * 保证了 ImageView 的重用
	 *
	 * @param position 位置
	 * @param opr      -对应的操作:1->普通点击;2->添加图;3->删除图
	 */
	private BorderImageView getImageView(final int position, final int start, final int opr) {
		BorderImageView imageView = mAdapter.generateImageView(getContext());
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (opr == 1) {
					//普通点击
					mAdapter.onItemImageClick(position);
				} else if (opr == 2) {
					//添加图
					mAdapter.onAddClick(position);
				} else {
					//删除图
					mAdapter.onDeleteClick(position - start);
				}
//				if (position - start < 0) {
//					mAdapter.onItemImageClick(position);
//				} else {
//					mAdapter.onDeleteClick(position - start);
//				}
			}
		});
		return imageView;
	}

	private PopupWindow popup;

	/**
	 * 弹窗状态
	 */
	private void showPopup(View view) {
		// 包含popupwindow布局
		View popuView = LayoutInflater.from(getContext()).inflate(R.layout.item_writerpopup, null);
		int width = DeviceUtils.dip2px(getContext(), 105);
		// 创建popupwindow
		popup = new PopupWindow(popuView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 不设置的话窗体外不能取消
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setOutsideTouchable(true);
		popup.setFocusable(true);
		int[] location = new int[2];
		view.getLocationOnScreen(location);
//		CLog.e(TAG, "popuView:" + popup.getWidth());

		if (width + 8 < view.getWidth()) {
			popup.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() - popup.getWidth() - 8, location[1] + view.getHeight() + mGap);
		} else {
			popup.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] + view.getHeight() + mGap);
		}

		//popup.showAsDropDown(view);
		initPopup(popuView);
	}

	/**
	 * 加载Popup控件
	 *
	 * @param view
	 */
	private void initPopup(View view) {
		view.findViewById(R.id.writer_add_iv).setOnClickListener(new PopupClick());
		view.findViewById(R.id.writer_del_iv).setOnClickListener(new PopupClick());
	}

	/**
	 * @author Qiu
	 */
	class PopupClick implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.writer_add_iv:
					//加图
					break;
				case R.id.writer_del_iv:
					//删除
					break;
			}
			popup.dismiss();
		}
	}

	/**
	 * 设置适配器
	 *
	 * @param adapter 适配器
	 */
	public void setAdapter(SixGridImageViewAdapter adapter) {
		mAdapter = adapter;
	}

	/**
	 * 隐藏popup
	 */
	public void popupDismiss() {
		if (popup != null) popup.dismiss();
	}
}