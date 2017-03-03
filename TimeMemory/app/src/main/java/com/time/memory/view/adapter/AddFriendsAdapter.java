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

import com.time.memory.R;
import com.time.memory.entity.Contacts;
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
 * @Description:添加好友
 * @date 2016/9/12 14:40
 * ==============================
 */
public class AddFriendsAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_COUNT = 2;
	private static final String TAG = "ConstantsAdapter";

	private Context mContext;
	private LayoutInflater inflater;
	private List<Contacts> contacts;
	private HashMap<String, Integer> letterIndexes;
	private String[] sections;
	private String locatedCity;
	private ConstantHolder holder;

	public AddFriendsAdapter(Context mContext, List<Contacts> contacts) {
		this.mContext = mContext;
		this.contacts = contacts;
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
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getCount() {
		return contacts.size() + 1;
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
			switch (viewType) {
				case 0:
					//搜索
					view = inflater.inflate(R.layout.item_addfriend_search, parent, false);
					break;
				case 1:
					//所有
					view = inflater.inflate(R.layout.item_addfriend_, parent, false);
					holder = new ConstantHolder(view);
					view.setTag(holder);
					break;
			}
		} else {
			switch (viewType) {
				case 0:
					break;
				case 1:
					holder = (ConstantHolder) view.getTag();
					break;
			}
		}
		switch (viewType) {
			case 0:
				break;
			case 1:
				String constact = contacts.get(position - 1).getContactName();
				holder.circle_constant_tv.setText(constact);
				String previousLetter = "-1";
				String currentLetter = PinyinUtil.getFirstLetter(contacts.get(position - 1).getPinyin());
				if (position != 1)
					previousLetter = PinyinUtil.getFirstLetter(contacts.get(position - 2).getPinyin());
				if (!TextUtils.equals(currentLetter, previousLetter)) {
					holder.letter.setVisibility(View.VISIBLE);
					holder.letter.setText(currentLetter);
				} else {
					holder.letter.setVisibility(View.GONE);
				}
				//设置图标状态
				holder.circle_check.setActivated(contacts.get(position - 1).isCheck());
				//点击
				holder.circle_check_rl.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onClickView(position - 1);
					}
				});
				break;

		}
		return view;
	}

	/**
	 * 点击
	 *
	 * @param position
	 */
	private void onClickView(int position) {
		contacts.get(position).setIsCheck(!contacts.get(position).isCheck());
//		booe flag = contacts.get(position).isCheck();
//		if (flag.equals("1")) {
//			contacts.get(position).setActiveFlg("0");
//		} else {
//			contacts.get(position).setActiveFlg("1");
//		}
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
