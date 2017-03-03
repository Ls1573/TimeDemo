package com.time.memory.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.time.memory.MainApplication;
import com.time.memory.gui.CircleImageTransformation;

/**
 * ==============================
 *
 * @author Qiu
 * @version 1.0
 * @Package com.time.memory.util
 * @Description:图片加载(Glide->Picasso 自定义的View 总是闪闪闪)
 * @date 2016-7-5 上午10:02:57
 * ==============================
 */
public class GlideUtil {

	private static Picasso picasso;

	// 创建
	public static Picasso getInstance() {
		if (picasso == null) {
			synchronized (GlideUtil.class) {
				if (picasso == null) {
					picasso = Picasso.with(MainApplication.getContext());
				}
			}
		}
		return picasso;
	}

	/**
	 * @Title: glideWithUrl
	 */
	public static void glideWithUrl(String url,
									ImageView target) {
//		Glide.with(context).load(url).into(target);
		GlideUtil.getInstance().load(url).config(Bitmap.Config.RGB_565).into(target);
	}

	/**
	 * @Title: glideWithAllUrl
	 * @Description: 缓存全部
	 */
	public static void glideWithAllUrl(String url,
									   ImageView target) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(target);
		GlideUtil.getInstance().load(url).config(Bitmap.Config.RGB_565).into(target);
	}


	/**
	 * @Title: glideWithAllUrl
	 * @Description: 缓存全部
	 */
	public static void glideWithAllUrl(String url,
									   ImageView target, int drawable) {
		GlideUtil.getInstance().load(url).config(Bitmap.Config.RGB_565).placeholder(drawable).error(drawable).into(target);
	}

	/**
	 * @Title: glideWithAllUrl
	 * @Description: 缓存全部
	 */
	public static void glideWithErrrAllUrl(String url,
										   ImageView target, int drawable) {
		GlideUtil.getInstance().load(url).config(Bitmap.Config.RGB_565).error(drawable).into(target);
	}

	/**
	 * @Title: glideWithAllUrl
	 * @Description: 缓存全部
	 */
	public static void glideWithNoCacheAllUrl(String url,
											  ImageView target) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
//				.into(target);
		GlideUtil.getInstance().load(url).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(target);
	}

	/**
	 * @Title: glideWithAllUrl
	 * @Description: 缓存全部
	 */
	public static void glideWithAllUrl(String url,
									   ImageView target, int witdh, int height) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(target);

		GlideUtil.getInstance().load(url).config(Bitmap.Config.RGB_565).into(target);
	}

	/**
	 * @Title: glideWithTarget
	 * @Description: 缓存全部
	 */
	public static void glideWithTarget(String url, final ImageView imageView, int drawable) {
//		SimpleTarget target = new SimpleTarget<Drawable>(300, 300) {
//			@Override
//			public void onResourceReady(Drawable resource, GlideAnimation<? super Drawable> glideAnimation) {
//				imageView.setImageDrawable(resource);
//			}
//		};

//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(target);

		GlideUtil.getInstance().load(url).centerCrop().resize(400, 400).placeholder(drawable).into(imageView);
	}

	/**
	 * @Title: glideWithTarget
	 * @Description: 缓存全部
	 */
	public static void glideWithTargetLoc(String url, final ImageView imageView, int witdh, int height) {
//		SimpleTarget target = new SimpleTarget<Drawable>(witdh, height) {
//			@Override
//			public void onResourceReady(Drawable resource, GlideAnimation<? super Drawable> glideAnimation) {
//				imageView.setImageDrawable(resource);
//			}
//		};
//		Glide.with(context).load("file://" + url).diskCacheStrategy(DiskCacheStrategy.ALL).into(target);
		GlideUtil.getInstance().load("file://" + url).config(Bitmap.Config.RGB_565).resize(witdh, height).centerCrop().into(imageView);
	}

	/**
	 * @Title: glideWithNoDisk
	 * @Description: 不缓存全部
	 */
	public static void glideWithNoDisk(Context context, final String url,
									   final ImageView target, int width, int height, int defaultDrawable) {
		GlideUtil.getInstance().load("file://" + url).config(Bitmap.Config.RGB_565).resize(width, height).placeholder(defaultDrawable).centerCrop().into(target);
	}

	/**
	 * @Title: glideWithNoDisk
	 * @Description: 不缓存全部
	 */
	public static void glideWithNoDisk(final String url,
									   final ImageView target, int width, int height) {
//		Glide.with(context)
//				.load("file://" + url)
//				.override(width, height)
//				.placeholder(defaultDrawable)
//				.error(defaultDrawable)
//				.into(target);
		GlideUtil.getInstance().load("file://" + url).config(Bitmap.Config.RGB_565).resize(width, height).centerCrop().into(target);
	}


	/**
	 * @Title: glideWithAllLoc
	 * @Description: 缓存全部
	 */
	public static void glideWithSAllLoc(String url,
										ImageView target) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(target);
		GlideUtil.getInstance().load("file://" + url).config(Bitmap.Config.RGB_565).into(target);
	}

	/**
	 * @Title: glideWithTrans
	 * @Description: 圆形图
	 */
	public static void glideWithTrans(String url, ImageView target) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new CircleTransform(context)).error(R.drawable.login_photo).into(target);
		GlideUtil.getInstance().load(url).transform(new CircleImageTransformation()).into(target);
	}

	/**
	 * @Title: glideWithTrans
	 * @Description: 圆形图
	 */
	public static void glideWithTrans(String url, ImageView target, int drawable) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new CircleTransform(context)).error(R.drawable.login_photo).into(target);
		GlideUtil.getInstance().load(url).transform(new CircleImageTransformation()).error(drawable).placeholder(drawable).into(target);
	}

	/**
	 * @Title: glideWithErrorUrl
	 * @Description: 有错误图
	 */
	public static void glideWithErrorUrl(String url,
										 ImageView target, Drawable drawable) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).error(drawable).into(target);
		GlideUtil.getInstance().load(url).error(drawable).placeholder(drawable).into(target);
	}

	/**
	 * @Title: glideWithCenterUrl
	 * @Description: 图片的缩放，centerCrop()和fitCenter()：
	 */
	public static void glideWithCenterUrl(String url,
										  ImageView target) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(target);
		GlideUtil.getInstance().load(url).centerCrop().resize(400, 400).into(target);
	}

	/**
	 * @Title: glideWithCenterUrl
	 * @Description: 图片的缩放，centerCrop()和fitCenter()：
	 */
	public static void glideWithCenterUrl(String url,
										  ImageView target, int drawable) {
//		Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(target);
		GlideUtil.getInstance().load(url).centerCrop().resize(400, 400).error(drawable).placeholder(drawable).into(target);
	}
}
