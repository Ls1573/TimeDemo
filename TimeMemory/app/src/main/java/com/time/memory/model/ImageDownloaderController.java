package com.time.memory.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.time.memory.core.callback.SimpleCallback;
import com.time.memory.model.base.BaseController;
import com.time.memory.model.impl.IImageDownloaderController;
import com.time.memory.util.CLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Qiu
 * @version V1.0
 * @Description:图片操作
 * @date 2016/12/14 9:02
 */
public class ImageDownloaderController extends BaseController implements IImageDownloaderController {
	@Override
	public void downloadImage(final Context context, String url, final String fileName, final SimpleCallback callback) {
		Target target = new Target() {
			@Override
			public void onBitmapLoaded(Bitmap bmp, Picasso.LoadedFrom from) {
				CLog.e(TAG, "获取成功-------------------->保存");
				FileOutputStream fos = null;
				ByteArrayOutputStream baos = null;
				if (bmp != null) {
					try {
						//存储路径
						File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
								Environment.DIRECTORY_PICTURES), "Memory/PhotoDownload");
						// 如果存储位置不存在，创建它．
						if (!mediaStorageDir.exists()) {
							if (!mediaStorageDir.mkdirs()) {
							}
						}
						File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + fileName);
						if (mediaFile.exists())
							mediaFile.delete();
						baos = new ByteArrayOutputStream();
						int quality = 100;
						bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
						fos = new FileOutputStream(mediaFile);
						fos.write(baos.toByteArray());
						fos.flush();
						//刷新图库
						Uri localUri = Uri.fromFile(mediaFile);
						Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
						context.sendBroadcast(localIntent);
						//返回结果
						onCallback(callback, "保存成功:" + mediaFile.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
						onCallback(callback, "获取失败");
					} finally {
						try {
							if (fos != null)
								fos.close();
							if (baos != null)
								baos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
//						if (bmp != null) {
//							bmp.recycle();
//							bmp = null;
//						}
					}
				}
			}

			@Override
			public void onBitmapFailed(Drawable errorDrawable) {
				CLog.e(TAG, "获取失败-------------------->");
				onCallback(callback, "获取失败");
			}

			@Override
			public void onPrepareLoad(Drawable placeHolderDrawable) {
				CLog.e(TAG, "准备获取图像");
			}
		};
		//Picasso下载
		Picasso.with(context).load(url).into(target);
	}

	public boolean makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		boolean result = dir.mkdir();
		return result;
	}
}
