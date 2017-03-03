package com.time.memory.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.GroupContacts;
import com.time.memory.gui.CircleImageTransformation;
import com.time.memory.gui.MyImageView;
import com.time.memory.gui.MyTextView;
import com.time.memory.util.CLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:圈子编辑部
 * @date 2016/11/7 20:50
 */
public class GroupShowAdapter extends BaseAdapter {
	private static final String TAG = "GroupShowAdapter";
	private List<GroupContacts> cList;
	private Context context;
	private LayoutInflater inflater;
	private ConstantHolder holder;
	private String imgOss;

	public GroupShowAdapter(List<GroupContacts> cList, Context context) {
		this.cList = cList;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.imgOss = context.getString(R.string.FSIMAGEPATH);
	}

	@Override
	public int getCount() {
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
			convertView = inflater.inflate(R.layout.item_choosefriend_, parent, false);
			holder = new ConstantHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ConstantHolder) convertView.getTag();
		}
		//联系人
		GroupContacts groupContacts = cList.get(position);
		//添加
		if (groupContacts.getState() == 1) {
			holder.circle_constant_iv.setVisibility(View.VISIBLE);
			holder.circle_constant_iv.setImageResource(R.drawable.groupadd);
			holder.circle_constant_iv.setBackgroundColor(context.getResources().getColor(R.color.yellow_A8));
			holder.constantTv.setVisibility(View.GONE);
			holder.constantnameTv.setVisibility(View.GONE);
			CLog.e(TAG, "addPosition:------------------------>" + position);
		} else if (groupContacts.getState() == 2) {
			//删除
			holder.circle_constant_iv.setVisibility(View.VISIBLE);
			holder.circle_constant_iv.setImageResource(R.drawable.groupdel);
			holder.circle_constant_iv.setBackgroundColor(context.getResources().getColor(R.color.red_CC));
			holder.constantTv.setVisibility(View.GONE);
			holder.constantnameTv.setVisibility(View.GONE);
			CLog.e(TAG, "delPosition:------------------------>" + position);
		} else {
			//用户名
			holder.constantnameTv.setVisibility(View.VISIBLE);
			holder.constantnameTv.setText(groupContacts.getUserName());

			//头像为空
			if (TextUtils.isEmpty(groupContacts.getHeadPhoto())) {
				//设置用户名
				holder.constantTv.setText(groupContacts.getSubName());
				holder.constantTv.setVisibility(View.VISIBLE);
				holder.circle_constant_iv.setVisibility(View.GONE);
			} else {
				holder.circle_constant_iv.setVisibility(View.VISIBLE);
				holder.constantTv.setVisibility(View.GONE);
				Picasso.with(parent.getContext()).load(imgOss + groupContacts.getHeadPhoto()).transform(new CircleImageTransformation()).error(R.drawable.login_photo).placeholder(R.drawable.login_photo).into(holder.circle_constant_iv);
			}
		}
		return convertView;
	}


	/**
	 * 联系人
	 */
	class ConstantHolder {
		@Bind(R.id.circle_constant_tv)
		MyTextView constantTv;//好友名
		@Bind(R.id.circle_constantname_tv)
		TextView constantnameTv;//用户名
		@Bind(R.id.circle_constant_iv)
		MyImageView circle_constant_iv;//好友头像

		public ConstantHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
