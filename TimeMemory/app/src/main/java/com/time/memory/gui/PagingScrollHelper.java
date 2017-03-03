package com.time.memory.gui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.time.memory.util.CLog;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:实现RecycleView分页滚动的工具类
 * @date 2016/11/17 23:06
 */
public class PagingScrollHelper {
	private static final String TAG = "PagingScrollHelper";
	RecyclerView mRecyclerView = null;
	private MyOnScrollListener mOnScrollListener = new MyOnScrollListener();

	private MyOnFlingListener mOnFlingListener = new MyOnFlingListener();
	private int offsetY = 0;
	private int offsetX = 0;//起始偏移量
	int startY = 0;

	int startX = 0;//起始点
	private int endX = 0;//终止点


	enum ORIENTATION {
		HORIZONTAL, VERTICAL, NULL
	}

	ORIENTATION mOrientation = ORIENTATION.HORIZONTAL;

	public void setUpRecycleView(RecyclerView recycleView) {
		if (recycleView == null) {
			throw new IllegalArgumentException("recycleView must be not null");
		}
		mRecyclerView = recycleView;
		//处理滑动
		recycleView.setOnFlingListener(mOnFlingListener);
		//设置滚动监听，记录滚动的状态，和总的偏移量
		recycleView.setOnScrollListener(mOnScrollListener);
		//记录滚动开始的位置
		recycleView.setOnTouchListener(mOnTouchListener);
		//获取滚动的方向
		updateLayoutManger();
	}

	public void updateLayoutManger() {
		RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
		if (layoutManager != null) {
			if (layoutManager.canScrollVertically()) {
				mOrientation = ORIENTATION.VERTICAL;
			} else if (layoutManager.canScrollHorizontally()) {
				mOrientation = ORIENTATION.HORIZONTAL;
			} else {
				mOrientation = ORIENTATION.NULL;
			}
			if (mAnimator != null) {
				mAnimator.cancel();
			}
			startX = 0;
			startY = 0;
			offsetX = 0;
			offsetY = 0;

		}
	}

	ValueAnimator mAnimator = null;

	/**
	 * 滑动速度
	 */
	public class MyOnFlingListener extends RecyclerView.OnFlingListener {
		@Override
		public boolean onFling(int velocityX, int velocityY) {
			if (mOrientation == ORIENTATION.NULL) {
				return false;
			}
			//获取开始滚动时所在页面的index
			int p = getStartPageIndex();
			//记录滚动开始和结束的位置
			int endPoint = 0;
			int startPoint = 0;
			//如果是水平方向
			if (mOrientation == ORIENTATION.HORIZONTAL) {
				startPoint = offsetX;
				if (velocityX < 0) {
					p--;
				} else if (velocityX > 0) {
					p++;
				}
				endPoint = p * mRecyclerView.getWidth();
			}
			if (endPoint < 0) {
				endPoint = 0;
			}
			endX = endPoint;
			//TODO
//			CLog.e(TAG, "positin:" + p + "   startPoint:" + startPoint + "    endPoint:" + endPoint + "     velocityX:" + velocityX);
			//使用动画处理滚动
			if (mAnimator == null) {
				mAnimator = new ValueAnimator().ofInt(startPoint, endPoint);
				mAnimator.setDuration(300);
				mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						int nowPoint = (int) animation.getAnimatedValue();
						if (mOrientation == ORIENTATION.HORIZONTAL) {
							//这里通过RecyclerView的scrollBy方法实现滚动。
							int dx = nowPoint - offsetX;
							mRecyclerView.scrollBy(dx, 0);
						}
					}
				});
				mAnimator.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						//回调监听
						if (null != mOnPageChangeListener) {
							mOnPageChangeListener.onPageChange(getPageIndex());
						}
					}
				});
			} else {
				mAnimator.cancel();
				mAnimator.setIntValues(startPoint, endPoint);
			}
			mAnimator.start();
			return true;
		}
	}

	public class MyOnScrollListener extends RecyclerView.OnScrollListener {
		@Override
		public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
			//newState==0表示滚动停止，此时需要处理回滚
			if (newState == RecyclerView.SCROLL_STATE_IDLE && mOrientation != ORIENTATION.NULL) {
				boolean move;
				int vX = 0, vY = 0;
				if (mOrientation == ORIENTATION.HORIZONTAL) {
					//如果滑动的距离超过屏幕的一半表示需要滑动到下一页
					int absX = Math.abs(offsetX - startX);
					move = absX > recyclerView.getWidth() / 2;
					if (move) {
						vX = offsetX - startX < 0 ? -1000 : 1000;
					}
					mOnFlingListener.onFling(vX, vY);
				}
			}
		}

		@Override
		public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			//记录滚动的偏移量
			offsetX += dx;
			startX = offsetX;
//			CLog.e(TAG, "offsetX:" + offsetX + "  dx:" + dx);
		}
	}

	private MyOnTouchListener mOnTouchListener = new MyOnTouchListener();

	public class MyOnTouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//手指按下的时候记录开始滚动的坐标
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				startY = offsetY;
//				startX = offsetX;
			}
			return false;
		}

	}

	/**
	 * 获取当前页数
	 *
	 * @return
	 */
	private int getPageIndex() {
		int p = 0;
		if (mOrientation == ORIENTATION.VERTICAL) {
			p = offsetY / mRecyclerView.getHeight();
		} else {
			p = offsetX / mRecyclerView.getWidth();
		}
		return p;
	}

	/**
	 * 开始页数
	 *
	 * @return
	 */
	private int getStartPageIndex() {
		int p = 0;
		if (mOrientation == ORIENTATION.HORIZONTAL) {
			p = endX / mRecyclerView.getWidth();
//			CLog.e(TAG, "startX:" + startX + "   p:" + p);
		}
		return p;
	}

	/**
	 * 设置宽度,偏移量
	 *
	 * @param count
	 */
	public void setOffsetX(int count, int width) {
		this.offsetX = width * count;
		this.startX = offsetX;
		this.endX = offsetX;
	}

	public void setDelOffsetX() {
		this.offsetX = offsetX - mRecyclerView.getWidth();
		this.startX = offsetX;
		this.endX = offsetX;
	}

	onPageChangeListener mOnPageChangeListener;

	public void setOnPageChangeListener(onPageChangeListener listener) {
		mOnPageChangeListener = listener;
	}

	public interface onPageChangeListener {
		void onPageChange(int index);
	}

}
