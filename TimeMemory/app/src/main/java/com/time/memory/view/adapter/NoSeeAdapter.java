package com.time.memory.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.core.callback.OnExpenListClick;
import com.time.memory.entity.MGroup;
import com.time.memory.entity.UserGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:不给谁看
 * @date 2016/9/13 15:33
 * ==============================
 */
public class NoSeeAdapter extends BaseExpandableListAdapter {
	private static final String TAG = "NoSeeAdapter";
	private Context context;
	private LayoutInflater inflater;
	private List<MGroup> MGroupEntities;//群
	private List<UserGroup> userEntities;//用户
	private ViewHolder holder;
	private ViewChildHolder childHolder;

	private OnExpenListClick onExpenListClick;//监听

	public void setOnExpenListClick(OnExpenListClick onExpenListClick) {
		this.onExpenListClick = onExpenListClick;
	}

	public NoSeeAdapter(Context context, List<MGroup> MGroupEntities, List<UserGroup> userEntities) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.MGroupEntities = MGroupEntities;
		this.userEntities = userEntities;
	}

	@Override
	public int getGroupCount() {
		return MGroupEntities.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return userEntities.get(groupPosition).userList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return MGroupEntities.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return userEntities.get(groupPosition).userList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_nosee_group, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//设置
		setGroupView(groupPosition, convertView);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_nosee_child, null);
			childHolder = new ViewChildHolder(convertView);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ViewChildHolder) convertView.getTag();
		}
		//设置
		setChildView(groupPosition, childPosition, isLastChild, convertView);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * 父级元素设置
	 *
	 * @param groupPosition
	 * @param view
	 */
	private void setGroupView(int groupPosition, View view) {
		MGroup gEntity = MGroupEntities.get(groupPosition);
		//设置显示状态
		if (gEntity.isVisable())
			holder.nosee_all_tv.setVisibility(View.VISIBLE);
		else
			holder.nosee_all_tv.setVisibility(View.GONE);
		//点击监听
		holder.nosee_check.setOnClickListener(new OnGroupClick(groupPosition));
		holder.nosee_all_tv.setOnClickListener(new OnGroupClick(groupPosition));
	}

	/**
	 * 子元素设置
	 *
	 * @param childPosition
	 * @param view
	 */
	private void setChildView(int groupPosition, int childPosition, boolean isLastChild, View view) {
//第一个分割线
		if (childPosition == 0)
			childHolder.view_line_up.setVisibility(View.VISIBLE);
		else
			childHolder.view_line_up.setVisibility(View.GONE);

		//最后一个分割线
		if (isLastChild)
			childHolder.view_line.setVisibility(View.VISIBLE);
		else
			childHolder.view_line.setVisibility(View.GONE);
	}

	/**
	 * 父
	 */
	class ViewHolder {
		@Bind(R.id.nosee_check)
		CheckBox nosee_check;//复选框
		@Bind(R.id.nosee_desc_tv)
		TextView nosee_desc_tv;//头
		@Bind(R.id.nosee_all_tv)
		Button nosee_all_tv;//全选/反选

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
			nosee_all_tv.setFocusable(false);
			nosee_all_tv.setClickable(true);
			nosee_check.setFocusable(false);
			nosee_check.setClickable(true);
			nosee_check.setTag("check");
			nosee_all_tv.setTag("all");
		}
	}

	/**
	 * 子
	 */
	class ViewChildHolder {
		@Bind(R.id.nosee_desc_tv)
		TextView nosee_desc_tv;//头
		@Bind(R.id.nosee_check_child)
		CheckBox nosee_check_child;//复选框

		@Bind(R.id.view_line_up)
		View view_line_up;//分割线

		@Bind(R.id.view_line)
		View view_line;//分割线

		public ViewChildHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	/**
	 * 点击父类的
	 */
	class OnGroupClick implements View.OnClickListener {
		int position;

		public OnGroupClick(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			String tag = (String) view.getTag();
			if (tag.equals("all")) {
				//所有
				onExpenListClick.onGroupAll(position);
			} else {
				//点击了check
				onExpenListClick.onGroupCheck(position);
			}
		}
	}
}
