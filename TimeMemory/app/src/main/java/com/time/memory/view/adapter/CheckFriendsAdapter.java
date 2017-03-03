package com.time.memory.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.time.memory.R;
import com.time.memory.entity.Contacts;
import com.time.memory.util.CLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:选择好友
 * @date 2016/10/13 10:41
 */
public class CheckFriendsAdapter extends BaseAdapter {
	private List<Contacts> cList;
	private Context context;
	private LayoutInflater inflater;
	private ConstantHolder holder;

	public CheckFriendsAdapter(List<Contacts> cList, Context context) {
		this.cList = cList;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		CLog.e("TAG,", "  " + cList.size());
		return cList.size();
	}

	@Override
	public Object getItem(int position) {
		return cList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			//所有
			convertView = inflater.inflate(R.layout.item_choosefriend, parent, false);
			holder = new ConstantHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ConstantHolder) convertView.getTag();
		}
		//设置用户名
		holder.constantTv.setText(cList.get(position).getSubName());
		return convertView;
	}


	/**
	 * 联系人
	 */
	class ConstantHolder {
		@Bind(R.id.circle_constant_tv)
		TextView constantTv;//好友名

		public ConstantHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
