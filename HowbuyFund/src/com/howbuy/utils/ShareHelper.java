package com.howbuy.utils;

import howbuy.android.palmfund.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.howbuy.config.Analytics;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 掌上基金 Android版 AppID：wx7a7b7dc1e2764750
 * AppSecret：3d97d6ee448fc386abb1c51d908dad50 已审核通过
 * 
 * @author zheng
 * 
 */
public class ShareHelper {
	private static String WXAPPID = "wx7a7b7dc1e2764750";// 微信开发平台注册应用的AppID
	private static ArrayList<String> SHARE_PLATEFORM = null;
	private static UMSocialService mController = null;
	private static Dialog mDlgShare = null;
	private static DialogInterface.OnClickListener mDlgPickListener = null;
	private static View.OnClickListener mDlgClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int shareType = -1;
			switch (v.getId()) {
			case R.id.lay_share_weixin:
				shareType = 0;
				break;
			case R.id.lay_share_weixin_circle:
				shareType = 1;
				break;
			case R.id.lay_share_weibo:
				shareType = 2;
				break;
			case R.id.lay_share_more:
				shareType = 3;
				break;
			}
			if (mDlgShare != null && mDlgShare.isShowing()) {
				mDlgShare.dismiss();
			}
			if (shareType != -1 && mDlgPickListener != null) {
				mDlgPickListener.onClick(mDlgShare, shareType);
			}
		}
	};

	static {
		SHARE_PLATEFORM = new ArrayList<String>(Arrays.asList(new String[] { "微信", "朋友圈", "新浪微博",
				"更多" }));
		mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
		// mController.getConfig().setSinaSsoHandler(new
		// SinaSsoHandler());//集成sina
		// SSO功能必须要在sina开放平台填写app的签名信息，然后运行带签名的apk文件才可以完成sso功能。
	}

	public static IWXAPI createWXAPI(Context cx) {
		return WXAPIFactory.createWXAPI(cx, WXAPPID, false);
	}

	public static UMSocialService getUMSocialService() {
		return mController;
	}

	public static ArrayList<String> getSharePlateform() {
		return SHARE_PLATEFORM;
	}

	public static void showSharePicker(Context cx, DialogInterface.OnClickListener l) {
		mDlgPickListener = l;
		if (mDlgShare == null || mDlgShare != null) {
			View view = LayoutInflater.from(cx).inflate(R.layout.com_pop_share, null);
			boolean hasInstallWx = ShareHelper.createWXAPI(cx).isWXAppInstalled();
			View weixin = view.findViewById(R.id.lay_share_weixin);
			View circle = view.findViewById(R.id.lay_share_weixin_circle);

			view.findViewById(R.id.lay_share_weibo).setOnClickListener(mDlgClickListener);
			view.findViewById(R.id.lay_share_more).setOnClickListener(mDlgClickListener);
			if (hasInstallWx) {
				weixin.setOnClickListener(mDlgClickListener);
				circle.setOnClickListener(mDlgClickListener);
			} else {
				weixin.setEnabled(false);
				circle.setEnabled(false);
			}
			mDlgShare = new AlertDialog.Builder(cx).setView(view).create();// new
																			// ContextThemeWrapper(cx,R.style.dialog)
			mDlgShare.requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		if (!mDlgShare.isShowing()) {
			mDlgShare.show();
		}
	}

	/**
	 * 
	 * @param cx
	 * @param which
	 * @param title
	 * @param content
	 * @param contentUrl
	 *            for weixin share .
	 * @param bmp
	 *            one of File,Bitmap,byte[],String,int .
	 * @param l
	 *            listener.
	 */
	public static void share(Context cx, int which, String title, String content,
			String contentUrl, Object bmp, SnsPostListener l, String source) {
		if (which < 0 || which > 2) {
			File f = null;
			if (bmp instanceof File) {
				f = (File) bmp;
			} else if (bmp instanceof String) {
				f = new File((String) bmp);
			}
			shareSys(cx, title, content, f);
		} else {
			mController.setShareContent(content);
			UMImage umImage = getUmengImg(bmp, cx);
			if (umImage != null) {
				mController.setShareImage(umImage);
			}else{
				mController.setShareImage(null);
			}
			switch (which) {
			case 0:
				shareWx(cx, false, title, content, contentUrl, l, umImage);
				Analytics.onEvent(cx, Analytics.SHARE_INFO, Analytics.KEY_FROM, source,
						Analytics.KEY_CHANNEL, "微信");
				break;
			case 1:
				shareWx(cx, true, title, content, contentUrl, l, umImage);
				Analytics.onEvent(cx, Analytics.SHARE_INFO, Analytics.KEY_FROM, source,
						Analytics.KEY_CHANNEL, "朋友圈");
				break;
			case 2:
				mController.postShare(cx, SHARE_MEDIA.SINA, l);
				Analytics.onEvent(cx, Analytics.SHARE_INFO, Analytics.KEY_FROM, source,
						Analytics.KEY_CHANNEL, "微博");
				break;
			}
		}
	}

	private static void shareWx(Context cx, boolean circle, String title, String content,
			String contentUrl, SnsPostListener l, UMImage umImage) {
		UMWXHandler wxCircleHandler = new UMWXHandler(cx, WXAPPID);
		if (circle) {
			CircleShareContent circleMedia = new CircleShareContent();
			circleMedia.setShareContent(content);
			circleMedia.setTitle(title);
			circleMedia.setShareImage(umImage);
			circleMedia.setTargetUrl(contentUrl);
			circleMedia.setAppWebSite("http://wap.howbuy.com");
			mController.setShareMedia(circleMedia);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.addToSocialSDK();
			mController.postShare(cx, SHARE_MEDIA.WEIXIN_CIRCLE, l);
		} else {
			WeiXinShareContent weiContent = new WeiXinShareContent();
			weiContent.setShareContent(content);
			weiContent.setTitle(title);
			weiContent.setShareImage(umImage);
			weiContent.setTargetUrl(contentUrl);
			weiContent.setAppWebSite("http://wap.howbuy.com");
			mController.setShareMedia(weiContent);
			wxCircleHandler.addToSocialSDK();
			mController.postShare(cx, SHARE_MEDIA.WEIXIN, l);// 0e:8c:b1:92:7d:d8:f8:5e:a7:b5:55:47:f3:52:a9:13
		}
	}

	public static UMImage getUmengImg(Object bmp, Context cx) {
		if (bmp != null) {
			UMImage um = null;
			if (bmp instanceof File) {
				um = new UMImage(cx, (File) bmp);
			} else if (bmp instanceof Bitmap) {
				um = new UMImage(cx, (Bitmap) bmp);
			} else if (bmp instanceof String) {
				um = new UMImage(cx, bmp.toString());
			} else if (bmp instanceof byte[]) {
				um = new UMImage(cx, (byte[]) bmp);
			} else if (bmp instanceof Integer) {
				um = new UMImage(cx, Integer.parseInt(bmp.toString()));
			}
			return um;
		}
		return null;
	}

	public static void shareSys(Context cx, String title, String text, final File file) {
		boolean hasImage = false;
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text); // 附带的说明信息
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (file != null && file.exists()) {
			String type = file.getAbsolutePath().toLowerCase();
			int n = type.lastIndexOf(".");
			if (n != -1) {
				hasImage = isImage(type.substring(n));
			}
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); // 传输图片或者文件
		}
		if (hasImage) {
			intent.setType("image/*"); // 分享图片
		} else {
			intent.setType("text/plain");
		}
		cx.startActivity(Intent.createChooser(intent, title));
	}

	public static boolean isImage(String type) {
		if (type != null
				&& (type.equals("jpg") || type.equals("gif") || type.equals("png")
						|| type.equals("jpeg") || type.equals("bmp") || type.equals("wbmp")
						|| type.equals("ico") || type.equals("jpe"))) {
			return true;
		}
		return false;
	}
}
