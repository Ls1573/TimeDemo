package com.time.memory.gui.stickyHeader;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.time.memory.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:定义 StickyHeader
 * @date 2016/11/1 17:43
 */
public class StickyHeaderLayoutManager extends RecyclerView.LayoutManager {

	private static final String TAG = "StickyHeaderLayoutManager";

	public enum HeaderPosition {
		NONE,
		NATURAL,
		STICKY,
		TRAILING
	}

	/**
	 * 头部位置变化的回调
	 *
	 * @see HeaderPosition
	 */
	public interface HeaderPositionChangedCallback {

		/**
		 * @param sectionIndex 下滑部分(第几部分)
		 */
		void onDownPositionChanged(int sectionIndex, int lastIndex);

		/**
		 * @param sectionIndex 上滑部分(第几部分)
		 */
		void onUpPositionChanged(int sectionIndex, int lastIndex);
	}

	SectioningAdapter adapter;

	// 缓存所有可见标题
	HashSet<View> headerViews = new HashSet<>();

	//缓存每个Header对应位置上的下标
	HashMap<Integer, HeaderPosition> headerPositionsBySection = new HashMap<>();

	HeaderPositionChangedCallback headerPositionChangedCallback;

	// 第一个可见项的位置
	int firstViewAdapterPosition;

	// 顶部可见项的位置
	int firstViewTop;

	// adapter position (iff >= 0) of the item selected in scrollToPosition
	int scrollTargetAdapterPosition = -1;

	//保存状态
	SavedState pendingSavedState;

	int topHeight = 0;//HeaderView的高度

	public StickyHeaderLayoutManager() {
	}

	public HeaderPositionChangedCallback getHeaderPositionChangedCallback() {
		return headerPositionChangedCallback;
	}

	/**
	 * 指定回调对象
	 *
	 * @param headerPositionChangedCallback the callback
	 * @see HeaderPosition
	 */
	public void setHeaderPositionChangedCallback(HeaderPositionChangedCallback headerPositionChangedCallback) {
		this.headerPositionChangedCallback = headerPositionChangedCallback;
	}

	@Override
	public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
		super.onAdapterChanged(oldAdapter, newAdapter);
		try {
			adapter = (SectioningAdapter) newAdapter;
		} catch (ClassCastException e) {
			throw new ClassCastException("StickyHeaderLayoutManager必须使用SectioningAdapter");
		}
		removeAllViews();
		headerViews.clear();
		headerPositionsBySection.clear();
	}

	@Override
	public void onAttachedToWindow(RecyclerView view) {
		super.onAttachedToWindow(view);
		try {
			adapter = (SectioningAdapter) view.getAdapter();
		} catch (ClassCastException e) {
			throw new ClassCastException("StickyHeaderLayoutManager must be used with a RecyclerView where the adapter is a kind of SectioningAdapter");
		}
	}

	@Override
	public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
		super.onDetachedFromWindow(view, recycler);
		updateFirstAdapterPosition();
	}

	/**
	 * 保存
	 *
	 * @return
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		if (pendingSavedState != null) {
			return pendingSavedState;
		}
		// Check if we're detached; if not, update
		if (adapter != null)
			updateFirstAdapterPosition();
		SavedState state = new SavedState();
		state.firstViewAdapterPosition = firstViewAdapterPosition;
		state.firstViewTop = firstViewTop;

		return state;
	}

	/**
	 * 获取
	 *
	 * @param state
	 */
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state == null) {
			return;
		}
		if (state instanceof SavedState) {
			pendingSavedState = (SavedState) state;
			requestLayout();
		}
	}

	/**
	 * 放置控件
	 *
	 * @param recycler
	 * @param state
	 */
	@Override
	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
		if (adapter == null) {
			return;
		}
		if (scrollTargetAdapterPosition >= 0) {
			firstViewAdapterPosition = scrollTargetAdapterPosition;
			firstViewTop = 0;
			scrollTargetAdapterPosition = RecyclerView.NO_POSITION;
		} else if (pendingSavedState != null && pendingSavedState.isValid()) {
			firstViewAdapterPosition = pendingSavedState.firstViewAdapterPosition;
			firstViewTop = pendingSavedState.firstViewTop;
			pendingSavedState = null;
		} else {
			updateFirstAdapterPosition();
		}
		int top = firstViewTop;
		// 重置
		headerViews.clear();
		headerPositionsBySection.clear();
		//清屏
		detachAndScrapAttachedViews(recycler);

		//屏幕的左起点
		int left = getPaddingLeft();
		//屏幕的右终点
		int right = getWidth() - getPaddingRight();
		//屏幕的下终点
		int parentBottom = getHeight() - getPaddingBottom();
		//总高度
		int totalVendedHeight = 0;

		//如果用一个通知清空视图，可能会冲突，无法绘制
		if (firstViewAdapterPosition > state.getItemCount()) {
			firstViewAdapterPosition = 0;
		}

		//TODO 循环绘制 ----->卡顿,未处理缓存
		//绘制View
		for (int adapterPosition = firstViewAdapterPosition; adapterPosition < state.getItemCount(); adapterPosition++) {
			//获取View
			View v = recycler.getViewForPosition(adapterPosition);
			//加入到RecyclerView中
			addView(v);
			//对子View进行测量
			measureChildWithMargins(v, 0, 0);
			//把宽高拿到，宽高都是包含ItemDecorate的尺寸
			int width = getDecoratedMeasuredWidth(v);
			int height = getDecoratedMeasuredHeight(v);
			//得到多布局显示对应类型
			int itemViewType = getViewBaseType(v);
			if (itemViewType == SectioningAdapter.TYPE_HEADER) {
				headerViews.add(v);
				topHeight = height;
				//TODO 放入最后，将View布局(headerView)
				layoutDecorated(v, left, top, width, top + height);
				//TODO 自增一个位置,计算第一个Item的数据
				adapterPosition++;
				View itemView = recycler.getViewForPosition(adapterPosition);
				addView(itemView);
				height = getDecoratedMeasuredHeight(itemView);
				layoutDecorated(itemView, left, top, width, top + height);
			} else {
				//Item->項
				height = getDecoratedMeasuredHeight(v);
				layoutDecorated(v, left, top, width, top + height);
			}
			top += height;
			totalVendedHeight += height;
		}
		// 确定是否需要填充视图滚动
		int innerHeight = getHeight() - (getPaddingTop() + getPaddingBottom());
		if (totalVendedHeight < innerHeight) {
			scrollVerticallyBy(totalVendedHeight - innerHeight, recycler, null);
		} else {
			updateHeaderPositions(recycler);
		}
	}

	/**
	 * 获取一个给定部分的头项，如果它不是已经在视图层次结构中创建的
	 *
	 * @param recycler     the recycler
	 * @param sectionIndex the index of the section for in question
	 * @return the header, or null if the adapter specifies no header for the section
	 */
	View createSectionHeaderIfNeeded(RecyclerView.Recycler recycler, int sectionIndex) {
		if (adapter.doesSectionHaveHeader(sectionIndex)) {

			for (int i = 0, n = getChildCount(); i < n; i++) {
				View view = getChildAt(i);
				if (getViewBaseType(view) == SectioningAdapter.TYPE_HEADER && getViewSectionIndex(view) == sectionIndex) {
					return view;
				}
			}

			int headerAdapterPosition = adapter.getAdapterPositionForSectionHeader(sectionIndex);
			View headerView = recycler.getViewForPosition(headerAdapterPosition);
			headerViews.add(headerView);
			addView(headerView);
			measureChildWithMargins(headerView, 0, 0);
			return headerView;
		}

		return null;
	}

	//上一个是否是HeaderView
	boolean isTop = false;

	@Override
	public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
		if (getChildCount() == 0) {
			return 0;
		}
		int scrolled = 0;
		int left = getPaddingLeft();
		int right = getWidth() - getPaddingRight();
		int width;
		//上滑
		if (dy < 0) {
			// content moving downwards, so we're panning to top of list
			View topView = getTopmostChildView();
			width = getDecoratedMeasuredWidth(topView);
			while (scrolled > dy) {
				// get the topmost view
				int hangingTop = Math.max(-getDecoratedTop(topView), 0);
				int scrollBy = Math.min(scrolled - dy, hangingTop); // scrollBy is positive, causing content to move downwards
				scrolled -= scrollBy;
				offsetChildrenVertical(scrollBy);

				// vend next view above topView
				if (firstViewAdapterPosition > 0 && scrolled > dy) {
					firstViewAdapterPosition--;
					int itemViewType = adapter.getItemViewBaseType(firstViewAdapterPosition);
					boolean isHeader = itemViewType == SectioningAdapter.TYPE_HEADER;
					// skip the header, move to next item above
					if (isHeader) {
						firstViewAdapterPosition--;
						if (firstViewAdapterPosition < 0) {
							break;
						}
						itemViewType = adapter.getItemViewBaseType(firstViewAdapterPosition);
						isHeader = itemViewType == SectioningAdapter.TYPE_HEADER;

						// If it's still a header, we don't need to do anything right now
						if (isHeader)
							break;
					}
					View v = recycler.getViewForPosition(firstViewAdapterPosition);
					addView(v, 0);
					width = getDecoratedMeasuredWidth(v);
					int bottom = getDecoratedTop(topView);
					int top;
					boolean isGhostHeader = itemViewType == SectioningAdapter.TYPE_GHOST_HEADER;
					if (isGhostHeader) {
						View header = createSectionHeaderIfNeeded(recycler, adapter.getSectionForAdapterPosition(firstViewAdapterPosition));
						top = bottom - getDecoratedMeasuredHeight(header); // header is already measured
					} else {
						measureChildWithMargins(v, 0, 0);
						top = bottom - getDecoratedMeasuredHeight(v);
					}
					if (isHeader || isGhostHeader) {
						layoutDecorated(v, left, top + topHeight, width, bottom + topHeight);
					} else {
						layoutDecorated(v, left, top, width, bottom);
					}
					topView = v;

				} else {
					break;
				}
			}
		} else {
			//下滑
			int parentHeight = getHeight();
			View bottomView = getBottommostChildView();
			while (scrolled < dy) {
				int hangingBottom = Math.max(getDecoratedBottom(bottomView) - parentHeight, 0);
				int scrollBy = -Math.min(dy - scrolled, hangingBottom);
				scrolled -= scrollBy;
				offsetChildrenVertical(scrollBy);
				int adapterPosition = getViewAdapterPosition(bottomView);
				int nextAdapterPosition = adapterPosition + 1;
				if (scrolled < dy && nextAdapterPosition < state.getItemCount()) {
					int top = getDecoratedBottom(bottomView);
					int itemViewType = adapter.getItemViewBaseType(nextAdapterPosition);
					if (itemViewType == SectioningAdapter.TYPE_HEADER) {
						isTop = true;
						//测试HeaderView数据
						View headerView = createSectionHeaderIfNeeded(recycler, adapter.getSectionForAdapterPosition(nextAdapterPosition));
						int height = getDecoratedMeasuredHeight(headerView);
						width = getDecoratedMeasuredWidth(headerView);
						layoutDecorated(headerView, left, 0, width, height);
						//TODO 测量紧挨着的Item数据
						//TODO 自增一个位置,计算第一个Item的数据
						nextAdapterPosition++;
						View v = recycler.getViewForPosition(nextAdapterPosition);
						width = getDecoratedMeasuredWidth(v);
						addView(v);
						measureChildWithMargins(v, 0, 0);
						height = getDecoratedMeasuredHeight(v);
						layoutDecorated(v, left, top, width, top + height);
						bottomView = v;
					} else {
						View v = recycler.getViewForPosition(nextAdapterPosition);
						width = getDecoratedMeasuredWidth(v);
						addView(v);
						measureChildWithMargins(v, 0, 0);
						int height = getDecoratedMeasuredHeight(v);
						layoutDecorated(v, left, top, width, top + height);
						bottomView = v;
					}
				} else {
					break;
				}
			}
		}

		View topmostView = getTopmostChildView();
		if (topmostView != null) {
			firstViewTop = getDecoratedTop(topmostView);
		}

		updateHeaderPositions(recycler);
		recycleViewsOutOfBounds(recycler);
		return scrolled;
	}

	@Override
	public RecyclerView.LayoutParams generateDefaultLayoutParams() {
		return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public boolean canScrollVertically() {
		return true;
	}

	@Override
	public void scrollToPosition(int position) {
		if (position < 0 || position > getItemCount()) {
			throw new IndexOutOfBoundsException("adapter position out of range");
		}
		scrollTargetAdapterPosition = position;
		pendingSavedState = null;
		requestLayout();
	}

	/**
	 * @param fullyVisibleOnly if true, the search will be limited to the first item not hanging off top of screen or partially obscured by a header
	 * @return the viewholder for the first visible item (not header or footer)
	 */
	@Nullable
	public SectioningAdapter.ItemViewHolder getFirstVisibleItemViewHolder(
			boolean fullyVisibleOnly) {
		return (SectioningAdapter.ItemViewHolder) getFirstVisibleViewHolderOfType(SectioningAdapter.TYPE_ITEM, fullyVisibleOnly);
	}

	/**
	 * @param fullyVisibleOnly if true, the search will be limited to the first header not hanging off top of screen
	 * @return the viewholder for the first visible header (not item or footer)
	 */
	@Nullable
	public SectioningAdapter.HeaderViewHolder getFirstVisibleHeaderViewHolder(
			boolean fullyVisibleOnly) {
		return (SectioningAdapter.HeaderViewHolder) getFirstVisibleViewHolderOfType(SectioningAdapter.TYPE_HEADER, fullyVisibleOnly);
	}

	/**
	 * @param fullyVisibleOnly if true, the search will be limited to the first footer not hanging off top of screen or partially obscured by a header
	 * @return the viewholder for the first visible footer (not header or item)
	 */
	@Nullable
	public SectioningAdapter.FooterViewHolder getFirstVisibleFooterViewHolder(
			boolean fullyVisibleOnly) {
		return (SectioningAdapter.FooterViewHolder) getFirstVisibleViewHolderOfType(SectioningAdapter.TYPE_FOOTER, fullyVisibleOnly);
	}

	@Nullable
	SectioningAdapter.ViewHolder getFirstVisibleViewHolderOfType(int baseType, boolean fullyVisibleOnly) {
		if (getChildCount() == 0) {
			return null;
		}

		// we need to discard items which are obscured by a header, so find
		// how tall the first header is, and we'll filter that the decoratedTop of
		// our items is below this value
		int firstHeaderBottom = 0;
		if (baseType != SectioningAdapter.TYPE_HEADER) {
			SectioningAdapter.HeaderViewHolder firstHeader = getFirstVisibleHeaderViewHolder(false);
			if (firstHeader != null) {
				firstHeaderBottom = getDecoratedBottom(firstHeader.itemView);
			}
		}

		// note: We can't use child view order because we muck with moving things to front
		View topmostView = null;
		int top = Integer.MAX_VALUE;

		for (int i = 0, e = getChildCount(); i < e; i++) {
			View v = getChildAt(i);

			// ignore views which are being deleted
			if (getViewAdapterPosition(v) == RecyclerView.NO_POSITION) {
				continue;
			}

			// filter for desired type
			if (getViewBaseType(v) != baseType) {
				continue;
			}

			// filter out items which are partially or fully obscured by a header
			int t = getDecoratedTop(v);
			int b = getDecoratedBottom(v);

			if (fullyVisibleOnly) {
				if (t < firstHeaderBottom) {
					continue;
				}
			} else {
				if (b <= firstHeaderBottom + 1) {
					continue;
				}
			}

			if (t < top) {
				top = t;
				topmostView = v;
			}
		}

		return topmostView != null ? getViewViewHolder(topmostView) : null;
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
									   int position) {
		if (position < 0 || position > getItemCount()) {
			throw new IndexOutOfBoundsException("adapter position out of range");
		}
		pendingSavedState = null;

		View firstVisibleChild = recyclerView.getChildAt(0);
		int itemHeight = getEstimatedItemHeightForSmoothScroll(recyclerView);
		int currentPosition = recyclerView.getChildAdapterPosition(firstVisibleChild);
		int distanceInPixels = Math.abs((currentPosition - position) * itemHeight);
		if (distanceInPixels == 0) {
			distanceInPixels = (int) Math.abs(firstVisibleChild.getY());
		}

		Context context = recyclerView.getContext();
		SmoothScroller scroller = new SmoothScroller(context, distanceInPixels);
		scroller.setTargetPosition(position);
		startSmoothScroll(scroller);
	}

	int getEstimatedItemHeightForSmoothScroll(RecyclerView recyclerView) {
		int height = 0;
		for (int i = 0, n = recyclerView.getChildCount(); i < n; i++) {
			height = Math.max(getDecoratedMeasuredHeight(recyclerView.getChildAt(i)), height);
		}
		return height;
	}

	int computeScrollVectorForPosition(int targetPosition) {
		updateFirstAdapterPosition();
		if (targetPosition > firstViewAdapterPosition) {
			return 1;
		} else if (targetPosition < firstViewAdapterPosition) {
			return -1;
		}
		return 0;
	}

	void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {

		int height = getHeight();
		int numChildren = getChildCount();
		Set<Integer> remainingSections = new HashSet<>();
		Set<View> viewsToRecycle = new HashSet<>();

		// we do this in two passes.
		// first, recycle everything but headers
		for (int i = 0; i < numChildren; i++) {
			View view = getChildAt(i);

			if (getViewBaseType(view) != SectioningAdapter.TYPE_HEADER) {
				if (getDecoratedBottom(view) < 0 || getDecoratedTop(view) > height) {
					viewsToRecycle.add(view);
				} else {
					// this view is visible, therefore the section lives
					remainingSections.add(getViewSectionIndex(view));
				}
			}
		}

		// second pass, for each "orphaned" header (a header who's section is completely recycled)
		// we remove it if it's gone offscreen
		for (int i = 0; i < numChildren; i++) {
			View view = getChildAt(i);
			int sectionIndex = getViewSectionIndex(view);
			if (getViewBaseType(view) == SectioningAdapter.TYPE_HEADER && !remainingSections.contains(sectionIndex)) {
				float translationY = view.getTranslationY();
				if ((getDecoratedBottom(view) + translationY) < 0 || (getDecoratedTop(view) + translationY) > height) {
					viewsToRecycle.add(view);
					headerViews.remove(view);
					headerPositionsBySection.remove(sectionIndex);
				}
			}
		}

		for (View view : viewsToRecycle) {
			removeAndRecycleView(view, recycler);
		}


		// determine the adapter adapterPosition of first visible item
		updateFirstAdapterPosition();
	}

	View getTopmostChildView() {
		if (getChildCount() == 0) {
			return null;
		}

		// note: We can't use child view order because we muck with moving things to front
		View topmostView = null;
		int top = Integer.MAX_VALUE;

		for (int i = 0, e = getChildCount(); i < e; i++) {
			View v = getChildAt(i);

			// ignore views which are being deleted
			if (getViewAdapterPosition(v) == RecyclerView.NO_POSITION) {
				continue;
			}

			// ignore headers
			if (getViewBaseType(v) == SectioningAdapter.TYPE_HEADER) {
				continue;
			}

			int t = getDecoratedTop(v);
			if (t < top) {
				top = t;
				topmostView = v;
			}
		}

		return topmostView;
	}

	View getBottommostChildView() {
		if (getChildCount() == 0) {
			return null;
		}

		// note: We can't use child view order because we muck with moving things to front
		View bottommostView = null;
		int bottom = Integer.MIN_VALUE;

		for (int i = 0, e = getChildCount(); i < e; i++) {
			View v = getChildAt(i);

			// ignore views which are being deleted
			if (getViewAdapterPosition(v) == RecyclerView.NO_POSITION) {
				continue;
			}

			// ignore headers
			if (getViewBaseType(v) == SectioningAdapter.TYPE_HEADER) {
				continue;
			}

			int b = getDecoratedBottom(v);
			if (b > bottom) {
				bottom = b;
				bottommostView = v;
			}
		}

		return bottommostView;
	}

	/**
	 * Updates firstViewAdapterPosition to the adapter position  of the highest item in the list - e.g., the
	 * adapter position of the item with lowest y value in the list
	 *
	 * @return the y value of the topmost view in the layout, or paddingTop if empty
	 */
	int updateFirstAdapterPosition() {

		// we're empty
		if (getChildCount() == 0) {
			firstViewAdapterPosition = 0;
			firstViewTop = getPaddingTop();
			return firstViewTop;
		}

		View topmostView = getTopmostChildView();
		if (topmostView != null) {
			firstViewAdapterPosition = getViewAdapterPosition(topmostView);
			firstViewTop = Math.min(topmostView.getTop(), getPaddingTop());
			return firstViewTop;
		}

		// as far as I can tell, if notifyDataSetChanged is called, onLayoutChildren
		// will be called, but all views will be marked as having NO_POSITION for
		// adapterPosition, which means the above approach of finding the topmostChildView
		// doesn't work. So, basically, leave firstViewAdapterPosition and firstViewTop
		// alone.
		return firstViewTop;
	}

	void updateHeaderPositions(RecyclerView.Recycler recycler) {
		Set<Integer> visitedSections = new HashSet<>();
		for (int i = 0, n = getChildCount(); i < n; i++) {
			View view = getChildAt(i);
			int sectionIndex = getViewSectionIndex(view);
			if (visitedSections.add(sectionIndex)) {
				createSectionHeaderIfNeeded(recycler, sectionIndex);
			}
		}

		// header is always positioned at top
		int left = getPaddingLeft();
		int right = getWidth() - getPaddingRight();

		for (View headerView : headerViews) {
			int sectionIndex = getViewSectionIndex(headerView);

			// find first and last non-header views in this section
			View ghostHeader = null;
			View firstViewInNextSection = null;
			for (int i = 0, n = getChildCount(); i < n; i++) {
				View view = getChildAt(i);
				int type = getViewBaseType(view);
				if (type == SectioningAdapter.TYPE_HEADER) {
					continue;
				}

				int viewSectionIndex = getViewSectionIndex(view);
				if (viewSectionIndex == sectionIndex) {
					if (type == SectioningAdapter.TYPE_GHOST_HEADER) {
						ghostHeader = view;
					}
				} else if (viewSectionIndex == sectionIndex + 1) {
					if (firstViewInNextSection == null) {
						firstViewInNextSection = view;
					}
				}
			}
			//测量得到高度
			int height = getDecoratedMeasuredHeight(headerView);
			//测量得到宽度
			int width = getDecoratedMeasuredWidth(headerView);
			int top = getPaddingTop();

			// initial position mark
			HeaderPosition headerPosition = HeaderPosition.STICKY;

			if (ghostHeader != null) {
				int ghostHeaderTop = getDecoratedTop(ghostHeader);
				if (ghostHeaderTop >= top) {
					top = ghostHeaderTop;
					headerPosition = HeaderPosition.NATURAL;
				}
			}

			if (firstViewInNextSection != null) {
				int nextViewTop = getDecoratedTop(firstViewInNextSection);
				if (nextViewTop - height < top) {
					top = nextViewTop - height;
					headerPosition = HeaderPosition.TRAILING;
				}
			}
			//将头布局拿到上面
			headerView.bringToFront();
			//放着viewHeader
			layoutDecorated(headerView, left, top, width, top + height);
			//刷新
			recordHeaderPositionAndNotify(sectionIndex, headerView, headerPosition);
		}
	}

	void recordHeaderPositionAndNotify(int sectionIndex, View headerView, HeaderPosition newHeaderPosition) {
		if (headerPositionsBySection.containsKey(sectionIndex)) {
			HeaderPosition currentHeaderPosition = headerPositionsBySection.get(sectionIndex);
			if (currentHeaderPosition != newHeaderPosition) {
				headerPositionsBySection.put(sectionIndex, newHeaderPosition);
				checkDownPosition(sectionIndex, headerView, currentHeaderPosition, newHeaderPosition);
			}
		} else {
			headerPositionsBySection.put(sectionIndex, newHeaderPosition);
			checkUpPosition(sectionIndex, headerView, HeaderPosition.NONE, newHeaderPosition);
		}
	}

	private static final boolean DEBUG = false;

	static class LayoutState {

		final static String TAG = "LinearLayoutManager#LayoutState";

		final static int LAYOUT_START = -1;

		final static int LAYOUT_END = 1;

		final static int INVALID_LAYOUT = Integer.MIN_VALUE;

		final static int ITEM_DIRECTION_HEAD = -1;

		final static int ITEM_DIRECTION_TAIL = 1;

		final static int SCOLLING_OFFSET_NaN = Integer.MIN_VALUE;

		/**
		 * We may not want to recycle children in some cases (e.g. layout)
		 */
		boolean mRecycle = true;

		/**
		 * Pixel offset where layout should start
		 */
		int mOffset;

		/**
		 * Number of pixels that we should fill, in the layout direction.
		 */
		int mAvailable;

		/**
		 * Current position on the adapter to get the next item.
		 */
		int mCurrentPosition;

		/**
		 * Defines the direction in which the data adapter is traversed.
		 * Should be {@link #ITEM_DIRECTION_HEAD} or {@link #ITEM_DIRECTION_TAIL}
		 */
		int mItemDirection;

		/**
		 * Defines the direction in which the layout is filled.
		 * Should be {@link #LAYOUT_START} or {@link #LAYOUT_END}
		 */
		int mLayoutDirection;

		/**
		 * Used when LayoutState is constructed in a scrolling state.
		 * It should be set the amount of scrolling we can make without creating a new view.
		 * Settings this is required for efficient view recycling.
		 */
		int mScrollingOffset;

		/**
		 * Used if you want to pre-layout items that are not yet visible.
		 * The difference with {@link #mAvailable} is that, when recycling, distance laid out for
		 * {@link #mExtra} is not considered to avoid recycling visible children.
		 */
		int mExtra = 0;

		/**
		 * Equal to {@link RecyclerView.State#isPreLayout()}. When consuming scrap, if this value
		 * is set to true, we skip removed views since they should not be laid out in post layout
		 * step.
		 */
		boolean mIsPreLayout = false;

		/**
		 */
		int mLastScrollDelta;

		/**
		 * When LLM needs to layout particular views, it sets this list in which case, LayoutState
		 * will only return views from this list and return null if it cannot find an item.
		 */
		List<RecyclerView.ViewHolder> mScrapList = null;

		/**
		 * @return true if there are more items in the data adapter
		 */
		boolean hasMore(RecyclerView.State state) {
			return mCurrentPosition >= 0 && mCurrentPosition < state.getItemCount();
		}

		/**
		 * Gets the view for the next element that we should layout.
		 * Also updates current item index to the next item, based on {@link #mItemDirection}
		 *
		 * @return The next element that we should layout.
		 */
		View next(RecyclerView.Recycler recycler) {
			if (mScrapList != null) {
				return nextViewFromScrapList();
			}
			final View view = recycler.getViewForPosition(mCurrentPosition);
			mCurrentPosition += mItemDirection;
			return view;
		}

		/**
		 * Returns the next item from the scrap list.
		 * <p/>
		 * Upon finding a valid VH, sets current item position to VH.itemPosition + mItemDirection
		 *
		 * @return View if an item in the current position or direction exists if not null.
		 */
		private View nextViewFromScrapList() {
			final int size = mScrapList.size();
			for (int i = 0; i < size; i++) {
				final View view = mScrapList.get(i).itemView;
				final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
				if (lp.isItemRemoved()) {
					continue;
				}
				if (mCurrentPosition == lp.getViewLayoutPosition()) {
					assignPositionFromScrapList(view);
					return view;
				}
			}
			return null;
		}

		public void assignPositionFromScrapList() {
			assignPositionFromScrapList(null);
		}

		public void assignPositionFromScrapList(View ignore) {
			final View closest = nextViewInLimitedList(ignore);
			if (closest == null) {
				mCurrentPosition = NO_POSITION;
			} else {
				mCurrentPosition = ((RecyclerView.LayoutParams) closest.getLayoutParams())
						.getViewLayoutPosition();
			}
		}

		public View nextViewInLimitedList(View ignore) {
			int size = mScrapList.size();
			View closest = null;
			int closestDistance = Integer.MAX_VALUE;
			if (DEBUG && mIsPreLayout) {
				throw new IllegalStateException("Scrap list cannot be used in pre layout");
			}
			for (int i = 0; i < size; i++) {
				View view = mScrapList.get(i).itemView;
				final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
				if (view == ignore || lp.isItemRemoved()) {
					continue;
				}
				final int distance = (lp.getViewLayoutPosition() - mCurrentPosition) *
						mItemDirection;
				if (distance < 0) {
					continue; // item is not in current direction
				}
				if (distance < closestDistance) {
					closest = view;
					closestDistance = distance;
					if (distance == 0) {
						break;
					}
				}
			}
			return closest;
		}
	}


	/**
	 * 位置检测(下滑)
	 *
	 * @param sectionIndex
	 * @param header
	 * @param oldPosition
	 * @param newPosition
	 */
	private void checkDownPosition(int sectionIndex, View header, HeaderPosition oldPosition, HeaderPosition newPosition) {
		//Log.i(TAG, "checkDownPosition: section: " + sectionIndex + " -> old: " + oldPosition.name() + " new: " + newPosition.name());
		if (headerPositionChangedCallback == null) return;
		//>0项
		if (sectionIndex > 0) {
			if (oldPosition.name().equals("NATURAL") && newPosition.name().equals("STICKY")) {
				//回调
				headerPositionChangedCallback.onDownPositionChanged(sectionIndex, sectionIndex - 1);
			}
		}
	}

	/**
	 * 位置检测(上滑)
	 *
	 * @param sectionIndex
	 * @param header
	 * @param oldPosition
	 * @param newPosition
	 */
	private void checkUpPosition(int sectionIndex, View header, HeaderPosition oldPosition, HeaderPosition newPosition) {
		//Log.i(TAG, "checkUpPosition: section: " + sectionIndex + " -> old: " + oldPosition.name() + " new: " + newPosition.name());
		if (headerPositionChangedCallback == null) return;
		if (oldPosition.name().equals("NONE") && newPosition.name().equals("TRAILING")) {
			//回调
			headerPositionChangedCallback.onUpPositionChanged(sectionIndex, sectionIndex + 1);
		}
	}

	int getViewBaseType(View view) {
		int adapterPosition = getViewAdapterPosition(view);
		return adapter.getItemViewBaseType(adapterPosition);
	}

	int getViewSectionIndex(View view) {
		int adapterPosition = getViewAdapterPosition(view);
		return adapter.getSectionForAdapterPosition(adapterPosition);
	}

	SectioningAdapter.ViewHolder getViewViewHolder(View view) {
		return (SectioningAdapter.ViewHolder) view.getTag(R.id.sectioning_adapter_tag_key_view_viewholder);
	}

	int getViewAdapterPosition(View view) {
		return getViewViewHolder(view).getAdapterPosition();
	}

	//
	class SmoothScroller extends LinearSmoothScroller {
		private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
		private static final float DEFAULT_DURATION = 1000;
		private final float distanceInPixels;
		private final float duration;

		public SmoothScroller(Context context, int distanceInPixels) {
			super(context);
			this.distanceInPixels = distanceInPixels;
			float millisecondsPerPx = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
			this.duration = distanceInPixels < TARGET_SEEK_SCROLL_DISTANCE_PX ?
					(int) (Math.abs(distanceInPixels) * millisecondsPerPx) : DEFAULT_DURATION;
		}

		@Override
		public PointF computeScrollVectorForPosition(int targetPosition) {
			return new PointF(0, StickyHeaderLayoutManager.this.computeScrollVectorForPosition(targetPosition));
		}

		@Override
		protected int calculateTimeForScrolling(int dx) {
			float proportion = (float) dx / distanceInPixels;
			return (int) (duration * proportion);
		}
	}

	static class SavedState implements Parcelable {

		int firstViewAdapterPosition = RecyclerView.NO_POSITION;
		int firstViewTop = 0;

		public SavedState() {
		}

		SavedState(Parcel in) {
			firstViewAdapterPosition = in.readInt();
			firstViewTop = in.readInt();
		}

		public SavedState(SavedState other) {
			firstViewAdapterPosition = other.firstViewAdapterPosition;
			firstViewTop = other.firstViewTop;
		}

		boolean isValid() {
			return firstViewAdapterPosition >= 0;
		}

		void invalidate() {
			firstViewAdapterPosition = RecyclerView.NO_POSITION;
		}

		@Override
		public String toString() {
			return "<" + this.getClass().getCanonicalName() + " firstViewAdapterPosition: " + firstViewAdapterPosition + " firstViewTop: " + firstViewTop + ">";
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(firstViewAdapterPosition);
			dest.writeInt(firstViewTop);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
