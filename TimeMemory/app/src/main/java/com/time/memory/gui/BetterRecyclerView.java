package com.time.memory.gui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.time.memory.util.CLog;

/**
 * @author Qiu
 * @version V1.0
 * @Description:水平垂直滚动冲突
 * @date 2016/11/21 17:16
 */
public class BetterRecyclerView extends RecyclerView {
	private static final String TAG = "BetterRecyclerView";
	private static final int INVALID_POINTER = -1;
	private int mScrollPointerId = INVALID_POINTER;
	private int mInitialTouchX, mInitialTouchY;
	private int mTouchSlop;

	public BetterRecyclerView(Context context) {
		super(context);
	}

	public BetterRecyclerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public BetterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		final ViewConfiguration vc = ViewConfiguration.get(getContext());
		mTouchSlop = vc.getScaledTouchSlop();
	}


	@Override
	public void setScrollingTouchSlop(int slopConstant) {
		super.setScrollingTouchSlop(slopConstant);
		final ViewConfiguration vc = ViewConfiguration.get(getContext());
		switch (slopConstant) {
			case TOUCH_SLOP_DEFAULT:
				mTouchSlop = vc.getScaledTouchSlop();
				break;
			case TOUCH_SLOP_PAGING:
				mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(vc);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		final int action = MotionEventCompat.getActionMasked(e);
		final int actionIndex = MotionEventCompat.getActionIndex(e);
//		CLog.e(TAG, "action---------->" + action);
		boolean state = false;
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mScrollPointerId = MotionEventCompat.getPointerId(e, 0);
				mInitialTouchX = (int) (e.getX() + 0.5f);
				mInitialTouchY = (int) (e.getY() + 0.5f);
				state = super.onInterceptTouchEvent(e);
//				CLog.e(TAG, "_DOWN:    state:" + state);
				return state;
			case MotionEvent.ACTION_MOVE:
//				CLog.e(TAG, "ACTION_MOVE:------------>");
				final int index = MotionEventCompat.findPointerIndex(e, mScrollPointerId);
				if (index < 0) {
					return false;
				}
				final int x = (int) (MotionEventCompat.getX(e, index) + 0.5f);
				final int y = (int) (MotionEventCompat.getY(e, index) + 0.5f);
				if (getScrollState() != SCROLL_STATE_DRAGGING) {
					final int dx = x - mInitialTouchX;
					final int dy = y - mInitialTouchY;
					final boolean canScrollHorizontally = getLayoutManager().canScrollHorizontally();
					final boolean canScrollVertically = getLayoutManager().canScrollVertically();
					boolean startScroll = false;
					if (canScrollHorizontally && Math.abs(dx) > mTouchSlop && (Math.abs(dx) >= Math.abs(dy))) {
						startScroll = true;
					}
					if (canScrollVertically && Math.abs(dy) > mTouchSlop && (Math.abs(dy) >= Math.abs(dx))) {
						startScroll = true;
					}
					state = startScroll && super.onInterceptTouchEvent(e);
//					CLog.e(TAG, "MOVE1:    state:" + state + "   :" + startScroll);
					return state;
				}
				state = super.onInterceptTouchEvent(e);
//				CLog.e(TAG, "MOVE2:    state:" + state);
				return state;
//			default:
//				return super.onInterceptTouchEvent(e);
		}
		return false;
	}
}