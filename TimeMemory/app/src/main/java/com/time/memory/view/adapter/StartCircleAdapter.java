package com.time.memory.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.MGroup;
import com.time.memory.gui.nineGridImage.NineGridImageView;
import com.time.memory.gui.nineGridImage.NineGridImageViewAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:开启圈子
 * @date 2016/9/12 14:40
 * ==============================
 */
public class StartCircleAdapter extends BaseAdapter {

	private ViewHolder holder;
	private ArrayList<MGroup> unGroups;
	private Context mContext;
	private LayoutInflater inflater;
	private String imgPath;

	public StartCircleAdapter(Context mContext, ArrayList<MGroup> unGroups) {
		this.mContext = mContext;
		this.unGroups = unGroups;
		this.inflater = LayoutInflater.from(mContext);
		imgPath = mContext.getString(R.string.FSIMAGEPATH);
	}


	@Override
	public int getCount() {
		return unGroups.size();
	}

	@Override
	public Object getItem(int position) {
		return unGroups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		int viewType = getItemViewType(position);
		if (view == null) {
			view = inflater.inflate(R.layout.item_importcircle, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		MGroup entity = unGroups.get(position);
		holder.memory_desc_tv.setText(entity.getGroupName());
		boolean isActivect = false;
		if (entity.getActiveFlg().equals("1")) {
			//未激活
			isActivect = false;
		} else {
			//已激活
			isActivect = true;
		}
		//激活状态
		holder.memory_sign_iv.setSelected(isActivect);

		NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
			@Override
			protected void onDisplayImage(Context context, ImageView imageView, String path) {
				if (TextUtils.isEmpty(path))
					imageView.setImageResource(R.drawable.headpic);
				else
					Picasso.with(mContext).load(imgPath + path).config(Bitmap.Config.RGB_565).placeholder(R.drawable.headpic).error(R.drawable.headpic).into(imageView);
			}
		};

		holder.time_imsg.setAdapter(mAdapter);
		holder.time_imsg.setImagesData(entity.getHeadPhotos());
		return view;
	}


	/**
	 * 联系人
	 */
	class ViewHolder {
		@Bind(R.id.memory_sign_iv)
		ImageView memory_sign_iv;//复选框
		@Bind(R.id.memory_desc_tv)
		TextView memory_desc_tv;//描述
		@Bind(R.id.time_imsg)
		NineGridImageView time_imsg;//图片

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
