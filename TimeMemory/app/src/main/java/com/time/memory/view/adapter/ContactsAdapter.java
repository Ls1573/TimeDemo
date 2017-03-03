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
import com.time.memory.core.callback.OnContactsCallBack;
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
 * @version V1.3
 * @Description:联系人
 * @date 2017/1/3 9:24
 */
public class ContactsAdapter extends BaseAdapter {
	private static final String TAG = "ContactsAdapter";

	private Context mContext;
	private LayoutInflater inflater;
	private List<Contacts> contacts;
	private HashMap<String, Integer> letterIndexes;
	private String[] sections;
	private String locatedCity;
	private ConstantHolder holder;
	private OnContactsCallBack onContactsCallBack;
	private int actictedColor;
	private int unActictedColor;
	private String imgWeb;
	private String imgOss;

	public void setOnContactsCallBack(OnContactsCallBack onChooseCallBack) {
		this.onContactsCallBack = onChooseCallBack;
	}

	public ContactsAdapter(Context mContext, List<Contacts> contacts) {
		this.mContext = mContext;
		this.contacts = contacts;
		this.inflater = LayoutInflater.from(mContext);
		if (contacts == null) {
			contacts = new ArrayList<>();
		}
		setData();
		actictedColor = mContext.getResources().getColor(R.color.yellow_d9);
		unActictedColor = mContext.getResources().getColor(R.color.common_font_black);
		imgWeb = mContext.getString(R.string.FSIMAGEPATH);
		imgOss = mContext.getString(R.string.FSIMAGEOSS);
	}

	public void notifityAdapter() {
		setData();
		notifyDataSetChanged();
	}

	private void setData() {
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
			view = inflater.inflate(R.layout.item_contacts, parent, false);
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
		//对方未激活时-->邀请
		if (!contacts.get(position).getActiveFlg().equals("0")) {
			holder.applyTv.setVisibility(View.VISIBLE);
			holder.addedTv.setVisibility(View.GONE);
			holder.addTv.setVisibility(View.GONE);
		} else {
			//已添加
			if (contacts.get(position).getIsTwoWayFlg().equals("0")) {
				holder.addedTv.setVisibility(View.VISIBLE);
				holder.addTv.setVisibility(View.GONE);
				holder.applyTv.setVisibility(View.GONE);
			} else {
				holder.addTv.setVisibility(View.VISIBLE);
				holder.addedTv.setVisibility(View.GONE);
				holder.applyTv.setVisibility(View.GONE);
			}
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
		//点击
		holder.circle_check_rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickView(position);
			}
		});
		//点击添加
		holder.addTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onAddClick(contacts.get(position).getUserId());
			}
		});
		//点击邀请
		holder.applyTv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onApplyClick(position);
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
//		onChooseCallBack.onChooseBack();
//		notifyDataSetChanged();
	}


	/**
	 * 添加用户
	 */
	private void onAddClick(String userId) {
		if (onContactsCallBack != null)
			onContactsCallBack.onAddUser(userId);
	}

	/**
	 * 邀请用户
	 */
	private void onApplyClick(int position) {
		if (onContactsCallBack != null)
			onContactsCallBack.onApplyUser(position);

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
		@Bind(R.id.contacts_added_tv)
		TextView addedTv;//已添加
		@Bind(R.id.contacts_add_tv)
		TextView addTv;//添加
		@Bind(R.id.contacts_apply_tv)
		TextView applyTv;//邀请
		@Bind(R.id.circle_check_rl)
		RelativeLayout circle_check_rl;//

		public ConstantHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
