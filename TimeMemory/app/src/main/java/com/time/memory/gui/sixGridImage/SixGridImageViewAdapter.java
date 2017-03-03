package com.time.memory.gui.sixGridImage;

import android.content.Context;
import android.widget.ImageView;

import com.time.memory.gui.BorderImageView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description: 6宫图回调
 * @date 2016/10/25 11:13
 */
public abstract class SixGridImageViewAdapter<T> {
	protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

	protected abstract void onDeleteClick(int position);

	protected abstract void onItemImageClick(int position);

	protected abstract void onAddClick(int position);

	protected BorderImageView generateImageView(Context context) {
		BorderImageView imageView = new BorderImageView(context);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return imageView;
	}
}