package com.time.memory.gui.gallery.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.time.memory.R;
import com.time.memory.core.callback.Callback;
import com.time.memory.entity.PhotoInfo;
import com.time.memory.gui.gallery.widget.zoonview.PhotoView;
import com.time.memory.gui.gallery.widget.zoonview.PhotoViewAttacher;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:viewPager预览
 * @date 2016/9/30 17:09
 */
public class PhotoPreviewAdapter extends ViewHolderRecyclingPagerAdapter<PhotoPreviewAdapter.PreviewViewHolder, PhotoInfo> {

	private static final String TAG = "PhotoPreviewAdapter";
	private Activity mActivity;
	//	private DisplayMetrics mDisplayMetrics;
	private boolean isOnlyWatch;

	private Callback callback;

	public PhotoPreviewAdapter(Activity activity, List<PhotoInfo> list, boolean isOnlyWatch) {
		super(activity, list);
		this.mActivity = activity;
//		this.mDisplayMetrics = DeviceUtils.getScreenPix(mActivity);
		this.isOnlyWatch = isOnlyWatch;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	@Override
	public PreviewViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		View view = getLayoutInflater().inflate(R.layout.gf_adapter_preview_viewpgaer_item, null);
		return new PreviewViewHolder(view);
	}

	@Override
	public void onBindViewHolder(PreviewViewHolder holder, int position) {
		PhotoInfo photoInfo = getDatas().get(position);
		String path = "";
		if (photoInfo != null) {
			path = photoInfo.getPhotoPath();
			if (path.contains("details"))
				path = path.replace("details", "onepicture");
		}
//		Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
		if (isOnlyWatch)
			Picasso.with(mActivity).load(path).config(Bitmap.Config.RGB_565).into(holder.mImageView);//无缓存
		else
			Picasso.with(mActivity).load("file://" + path).config(Bitmap.Config.RGB_565).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(holder.mImageView);

		//点击事件
		holder.mImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				if (callback != null)
					callback.onCallback(true);
			}
		});


	}

	class PreviewViewHolder extends ViewHolderRecyclingPagerAdapter.ViewHolder {
		@Bind(R.id.photo_iv)
		PhotoView mImageView;

		public PreviewViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

}
