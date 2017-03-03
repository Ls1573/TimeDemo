package com.time.memory.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.entity.GroupContacts;
import com.time.memory.gui.CircleImageTransformation;
import com.time.memory.util.pinyin.PinyinUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ==============================
 *
 * @author @Qiu
 * @version V1.0
 * @Description:圈子列表
 * @date 2016/9/12 14:40
 * ==============================
 */
public class ShowCircleAdapter extends BaseAdapter {
	private static final String TAG = "ConstantsAdapter";
	private Context mContext;
	private LayoutInflater inflater;
	private List<GroupContacts> GroupContacts;
	private HashMap<String, Integer> letterIndexes;
	private String[] sections;
	private ConstantHolder holder;
	private String imgOss;

	public ShowCircleAdapter(Context mContext, List<GroupContacts> GroupContacts) {
		this.mContext = mContext;
		this.GroupContacts = GroupContacts;
		this.inflater = LayoutInflater.from(mContext);
		if (GroupContacts == null) {
			GroupContacts = new ArrayList<>();
		}
		imgOss = mContext.getString(R.string.FSIMAGEPATH);
		initData();
	}

	private void initData() {
		int size = GroupContacts.size();
		letterIndexes = new HashMap<String, Integer>();
		sections = new String[size];
		for (int index = 0; index < size; index++) {
			//当前拼音首字母
			String currentLetter = PinyinUtil.getFirstLetter(GroupContacts.get(index).getPinyin());
			//上个首字母，如果不存在设为""
			String previousLetter = index >= 1 ? PinyinUtil.getFirstLetter(GroupContacts.get(index - 1).getPinyin()) : "";
			if (!TextUtils.equals(currentLetter, previousLetter)) {
				letterIndexes.put(currentLetter, index);
				sections[index] = currentLetter;
			}
		}
	}

	public void notifityAdapter() {
		initData();
		notifyDataSetChanged();
	}

	/**
	 * 获取字母索引的位置
	 *
	 * @param letter
	 * @return
	 */
	public int getLetterPosition(String letter) {
		Integer integer = letterIndexes.get(letter);
		return integer == null ? -1 : integer;
	}


	@Override
	public int getCount() {
		return GroupContacts.size();
	}

	@Override
	public Object getItem(int position) {
		return GroupContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			//所有
			view = inflater.inflate(R.layout.item_showcircle, parent, false);
			holder = new ConstantHolder(view);
			view.setTag(holder);
		} else {
			holder = (ConstantHolder) view.getTag();
		}
		String constact = GroupContacts.get(position).getUserName();
		String headPic = GroupContacts.get(position).getHeadPhoto();
		holder.circle_constant_tv.setText(constact);
		String previousLetter = "-1";
		String currentLetter = PinyinUtil.getFirstLetter(GroupContacts.get(position).getPinyin());
		//头像  circle_pic_iv
		if (position != 0)
			previousLetter = PinyinUtil.getFirstLetter(GroupContacts.get(position - 1).getPinyin());
		if (!TextUtils.equals(currentLetter, previousLetter)) {
			holder.letter.setVisibility(View.VISIBLE);
			holder.letter.setText(currentLetter);
		} else {
			holder.letter.setVisibility(View.GONE);
		}
		//圆圈
		if (TextUtils.isEmpty(headPic)) {
			holder.circle_pic_iv.setImageResource(R.drawable.login_photo);
		} else {
			Picasso.with(mContext).load(imgOss + headPic).transform(new CircleImageTransformation()).error(R.drawable.login_photo).placeholder(R.drawable.login_photo).into(holder.circle_pic_iv);
		}

		return view;
	}

	/**
	 * 联系人
	 */
	class ConstantHolder {
		@Bind(R.id.view_line)
		View view_line;
		@Bind(R.id.circle_pic_iv)
		ImageView circle_pic_iv;
		@Bind(R.id.circle_constant_tv)
		TextView circle_constant_tv;//好友名
		@Bind(R.id.circle_letter_tv)
		TextView letter;//字母


		@Bind(R.id.circle_pic_ll)
		RelativeLayout circle_pic_ll;//外圈

		public ConstantHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
