package com.time.memory.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.time.memory.MainApplication;
import com.time.memory.gui.MyToast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by Administrator on 2016/11/18.
 */
public class ShareUtil {
	protected MyToast mToast;

	/**
	 * 分享
	 */
	public void wXShare(Context context, String titleText, String title, String conent, String img, String url) {
		try {
			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN).
					withTitle(titleText + "\n«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void friendsCircleShare(Context context, String titleText, String title, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).
					withTitle(titleText + "«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void qqFriendShare(Context context, String titleText, String title, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QQ).
					withTitle(titleText + "\n«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void qqSpaceShare(Context context, String titleText, String title, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QZONE).
					withTitle(titleText + "«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sinoShare(Context context, String titleText, String title, String conent, String img, String url) {
		try {
			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.SINA).
					withTitle(titleText + "\n«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 分享设置默认logo图片
	 *
	 * @param context
	 * @param titleText
	 * @param title
	 * @param conent
	 * @param img
	 * @param url
	 */
	public void wXShareInt(Context context, String titleText, String title, String conent, int img, String url) {
		try {


			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN).
					withTitle(titleText + "\n«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void friendsCircleShareInt(Context context, String titleText, String title, String conent, int img, String url) {

		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).
					withTitle(titleText + "«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void qqFriendShareInt(Context context, String titleText, String title, String conent, int img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QQ).
					withTitle(titleText + "\n«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void qqSpaceShareInt(Context context, String titleText, String title, String conent, int img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QZONE).
					withTitle(titleText + "«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sinoShareInt(Context context, String titleText, String title, String conent, int img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.SINA).
					withTitle(titleText + "\n«" + title + "»").
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 群的分享
	 */
	public void wXShareQ(Context context, String titleText, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void friendsCircleShareQ(Context context, String titleText, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void qqFriendShareQ(Context context, String titleText, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QQ).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void qqSpaceShareQ(Context context, String titleText, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QZONE).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sinoShareQ(Context context, String titleText, String conent, String img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.SINA).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置分享圈子的默认图片
	 */
	public void wXShareIntQ(Context context, String titleText, String conent, int img, String url) {

		try {


			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void friendsCircleShareIntQ(Context context, String titleText, String conent, int img, String url) {
		try {

			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void qqFriendShareIntQ(Context context, String titleText, String conent, int img, String url) {
		try {
			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QQ).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void qqSpaceShareIntQ(Context context, String titleText, String conent, int img, String url) {
		try {
			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.QZONE).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sinoShareIntQ(Context context, String titleText, String conent, int img, String url) {
		try {
			ShareAction shareAction = new ShareAction((Activity) context);
			shareAction.setPlatform(SHARE_MEDIA.SINA).
					withTitle(titleText).
					withText(conent).
					withMedia(new UMImage(context, img)).
					withTargetUrl(url).
					setCallback(umShareListener).
					share();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			showShortToast("分享成功啦");
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			showShortToast("分享失败啦");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			//showShortToast(platform + " 分享取消了");
		}
	};

	public void showShortToast(String msg) {
		showToast(msg);
	}

	private void showToast(String text) {
		if (mToast == null) {
			mToast = MyToast.makeTextToast(MainApplication.getContext(), text, Toast.LENGTH_SHORT);
		}
		mToast.setText(text);
		mToast.show();
	}
}
