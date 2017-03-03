package com.time.memory.gui.flowGridImage;

import android.content.Context;
import android.widget.ImageView;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:图片流布局
 * @date 2016/9/21 19:00
 */
public interface FlowImageViewCallback<T> {
	void onDisplayImage(Context context, ImageView imageView, T t);
}