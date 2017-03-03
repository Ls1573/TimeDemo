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
import com.time.memory.core.callback.OnChooseCallBack;
import com.time.memory.entity.Contacts;
import com.time.memory.gui.CircleImageTransformation;
import com.time.memory.util.pinyin.PinyinUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:选择好友
 * @date 2016/10/12 11:59
 */
public class ChooseFriendsAdapter extends BaseAdapter {
	private static final String TAG = "ConstantsAdapter";

	private Context mContext;
	private LayoutInflater inflater;
	private List<Contacts> contacts;
	private List<Contacts> checkList;
	private HashMap<String, Integer> letterIndexes;
	private String[] sections;
	private String locatedCity;
	private ConstantHolder holder;
	private OnChooseCallBack onChooseCallBack;
	private int actictedColor;
	private int unActictedColor;
	private String imgWeb;
	private String userId;

	public void setOnChooseCallBack(OnChooseCallBack onChooseCallBack) {
		this.onChooseCallBack = onChooseCallBack;
	}

	public ChooseFriendsAdapter(Context mContext, String userId, List<Contacts> contacts, List<Contacts> checkList) {
		this.mContext = mContext;
		this.contacts = contacts;
		this.userId = userId;
		this.checkList = checkList;
		this.inflater = LayoutInflater.from(mContext);
		if (contacts == null) {
			contacts = new ArrayList<>();
		}
		int size = contacts.size();
		letterIndexes = new HashMap<String, Integer>();
		sections = new String[size];
		for (int index = 0; index < size; index++) {
			//当前拼音首字母
			String currentLetter = PinyinUtil.getFirstLetter(contacts.get(index).getPinyin());
			//上个首字母，如果不存在设为""
			String previousLetter = index >= 1 ? PinyinUtil.getFirstLetter(contacts.get(index - 1).getPinyin()) : "";
			if (!TextUtils.equals(currentLetter, previousLetter)) {
				letterIndexes.put(currentLetter, index);
				sections[index] = currentLetter;
			}
		}
		actictedColor = mContext.getResources().getColor(R.color.yellow_d9);
		unActictedColor = mContext.getResources().getColor(R.color.common_font_black);
		imgWeb = mContext.getString(R.string.FSIMAGEPATH);
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
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		int viewType = getItemViewType(position);
		if (view == null) {
			//所有
			view = inflater.inflate(R.layout.item_addfriend_, parent, false);
			holder = new ConstantHolder(view);
			view.setTag(holder);
		} else {
			holder = (ConstantHolder) view.getTag();
		}
		String constact = contacts.get(position).getContactName();
		holder.circle_constant_tv.setText(constact);

		String pic = contacts.get(position).getHeadPhoto();
		if (!TextUtils.isEmpty(pic))
			Picasso.with(mContext).load(imgWeb + pic).transform(new CircleImageTransformation()).error(R.drawable.login_photo).placeholder(R.drawable.login_photo).into(holder.circle_pic_iv);
		else {
			holder.circle_pic_iv.setImageResource(R.drawable.login_photo);
		}
		//已激活
		if (contacts.get(position).getActiveFlg().equals("0")) {
			holder.circle_constant_tv.setTextColor(actictedColor);
		} else {
			holder.circle_constant_tv.setTextColor(unActictedColor);
		}

		String previousLetter = "-1";
		String currentLetter = PinyinUtil.getFirstLetter(contacts.get(position).getPinyin());
		if (position != 0)
			previousLetter = PinyinUtil.getFirstLetter(contacts.get(position - 1).getPinyin());
		if (!TextUtils.equals(currentLetter, previousLetter)) {
			holder.letter.setVisibility(View.VISIBLE);
			holder.letter.setText(currentLetter);
		} else {
			holder.letter.setVisibility(View.GONE);
		}
		//设置图标状态
		holder.circle_check.setActivated(contacts.get(position).isCheck());
		//点击
		holder.circle_check_rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickView(position);
			}
		});


		return view;
	}

	/**
	 * 点击
	 *
	 * @param position
	 */
	private void onClickView(int position) {
		Contacts contact = contacts.get(position);
		if (userId.equals(contact.getUserId())) return;
		contact.setIsCheck(!contact.isCheck());
		if (contact.isCheck()) {
			//加入
			checkList.add(contact);
		} else {
			//移除
			checkList.remove(contact);
		}
		onChooseCallBack.onChooseBack();
		notifyDataSetChanged();
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
		@Bind(R.id.circle_check)
		ImageView circle_check;//指示图
		@Bind(R.id.circle_check_rl)
		RelativeLayout circle_check_rl;//

		public ConstantHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
