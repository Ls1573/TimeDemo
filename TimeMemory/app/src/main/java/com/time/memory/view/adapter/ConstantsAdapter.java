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
import com.time.memory.core.callback.OnConstactCallBack;
import com.time.memory.entity.Contacts;
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
 * @Description:联系人
 * @date 2016/9/12 14:40
 * ==============================
 */
public class ConstantsAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_COUNT = 2;
	private static final String TAG = "ConstantsAdapter";

	private Context mContext;
	private LayoutInflater inflater;
	private List<Contacts> contacts;
	private HashMap<String, Integer> letterIndexes;
	private String[] sections;
	private String locatedCity;
	private ConstantHolder holder;
	private CreateHolder createHolder;
	private int actictedColor;
	private int unActictedColor;
	private String imgWeb;
	private String imgOss;

	private int constactsSize;//联系人数
	private OnConstactCallBack onConstactCallBack;

	public ConstantsAdapter(Context mContext, List<Contacts> contacts) {
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

	public void setOnConstactCallBack(OnConstactCallBack onConstactCallBack) {
		this.onConstactCallBack = onConstactCallBack;
	}

	public void notifityAdapter() {
		setData();
		notifyDataSetChanged();
	}

	public void remove(int position) {
		contacts.remove(position);
		notifityAdapter();

	}


	/**
	 * 设置数据
	 */
	private void setData() {
		//联系人数量
		constactsSize = contacts.size();
		//联系人数量
		int size = contacts.size();
		//字符对应的下标
		letterIndexes = new HashMap<String, Integer>();
		sections = new String[size];
		for (int index = 0; index < size; index++) {
			//当前拼音首字母
			String currentLetter = PinyinUtil.getFirstLetter(contacts.get(index).getPinyin());
			//上个首字母，如果不存在设为""
			String previousLetter = index >= 1 ? PinyinUtil.getFirstLetter(contacts.get(index - 1).getPinyin()) : "";
			if (!TextUtils.equals(currentLetter, previousLetter)) {
				letterIndexes.put(currentLetter, index + 1);
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
		return constactsSize + 1;
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
	public View getView(final int position, View conview, ViewGroup parent) {
		int viewType = getItemViewType(position);
		if (conview == null) {
			switch (viewType) {
				case 0:
					//创建
					conview = inflater.inflate(R.layout.item_circle_search, parent, false);
					createHolder = new CreateHolder(conview);
					conview.setTag(createHolder);
					break;
				case 1:
					//所有
					conview = inflater.inflate(R.layout.item_search_circle, parent, false);
					holder = new ConstantHolder(conview);
					conview.setTag(holder);
					break;
			}
		} else {
			switch (viewType) {
				case 0:
					createHolder = (CreateHolder) conview.getTag();
					break;
				case 1:
					holder = (ConstantHolder) conview.getTag();
					break;
			}
		}
		switch (viewType) {
			case 0:
				break;
			case 1:
				//联系人前有几个占位
				final int index = 1;
				String constact = contacts.get(position - index).getContactName();
				holder.circle_constant_tv.setText(constact);
				String pic = contacts.get(position - index).getHeadPhoto();
				if (!TextUtils.isEmpty(pic))
					Picasso.with(parent.getContext()).load(imgWeb + pic + imgOss).transform(new CircleImageTransformation()).error(R.drawable.circie_mine).placeholder(R.drawable.circie_mine).into(holder.circle_pic_iv);
				else {
					holder.circle_pic_iv.setImageResource(R.drawable.circie_mine);
				}
				if (contacts.get(position - index).getActiveFlg().equals("0")) {
					//已激活
					holder.circle_constant_tv.setTextColor(actictedColor);
				} else {
					holder.circle_constant_tv.setTextColor(unActictedColor);
				}
				String previousLetter = "-1";
				String currentLetter = PinyinUtil.getFirstLetter(contacts.get(position - index).getPinyin());
				if (position != index)
					previousLetter = PinyinUtil.getFirstLetter(contacts.get(position - index - 1).getPinyin());
				if (!TextUtils.equals(currentLetter, previousLetter)) {
					holder.letter.setVisibility(View.VISIBLE);
					holder.letter.setText(currentLetter);
				} else {
					holder.letter.setVisibility(View.GONE);
				}

				//联系人
				holder.circle_constant_rl.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onConstactCallBack.onConstactsCallBack(position - index, contacts.get(position - index).getUserId() + "");
					}
				});

				//联系人->长点击
				holder.circle_constant_rl.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						onConstactCallBack.onLongConstactsCallBack(position - index, contacts.get(position - index).getUserId() + "");
						return true;
					}
				});
				break;
		}
		return conview;
	}

	View.OnClickListener onCircleClick = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.circle_create_ll)
				//新建
				onConstactCallBack.onCreateCircle();
			else if (view.getId() == R.id.circle_up_ll)
				//同步
				onConstactCallBack.onContastsUp();
			if (view.getId() == R.id.circle_tag_ll)
				//记忆签
				onConstactCallBack.onTags();
		}
	};

	/**
	 * 新建|查找
	 */
	class CreateHolder {
		public CreateHolder(View view) {
			//新建
			ButterKnife.findById(view, R.id.circle_create_ll).setOnClickListener(onCircleClick);
			ButterKnife.findById(view, R.id.circle_up_ll).setOnClickListener(onCircleClick);
			ButterKnife.findById(view, R.id.circle_tag_ll).setOnClickListener(onCircleClick);
		}
	}

	/**
	 * 联系人
	 */
	class ConstantHolder {
		@Bind(R.id.view_line)
		View view_line;
		@Bind(R.id.circle_constant_rl)
		RelativeLayout circle_constant_rl;
		@Bind(R.id.circle_pic_iv)
		ImageView circle_pic_iv;
		@Bind(R.id.circle_constant_tv)
		TextView circle_constant_tv;//好友名
		@Bind(R.id.circle_letter_tv)
		TextView letter;//字母

		public ConstantHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
