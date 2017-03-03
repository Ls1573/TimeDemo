package com.time.memory.gui.sticky;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:Layout工具类
 * @date 2016/11/9 11:59
 */
public abstract class LayoutHelper implements LayoutQueryHelper, LayoutModifierHelper {

	public abstract int getLeadingEdge();

	public abstract LayoutHelper getSubsectionLayoutHelper();

	public abstract void init(SectionData sd, int markerLine, int leadingEdge, int stickyEdge);

	public abstract void init(SectionData sd, int horizontalOffset, int unavailableWidth,
							  int markerLine, int leadingEdge, int stickyEdge);

	public abstract void recycle();

	abstract int layoutHeaderTowardsEnd(View header, int markerLine, RecyclerView.State state);

	abstract int layoutHeaderTowardsStart(View header, int offset, int sectionTop,
										  int sectionBottom, RecyclerView.State state);

	abstract void measureHeader(View header);

	abstract int translateFillResult(int markerLine);

	abstract void updateVerticalOffset(int markerLine);

	static interface Parent extends LayoutQueryHelper, LayoutModifierHelper {

		void measureHeader(View header, int widthUsed, int heightUsed);
	}
}
