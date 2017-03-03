package com.time.memory.gui.nineGridImage;

import android.content.Context;
import android.widget.ImageView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description: 9宫图观看
 * @date 2016/10/25 11:15
 */
public abstract class NineGridImageViewAdapter<T> {
	protected abstract void onDisplayImage(Context context, ImageView imageView, T t);


	protected ImageView generateImageView(Context context, boolean isBorder) {
		GridImageView imageView = new GridImageView(context, isBorder);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return imageView;
	}
}