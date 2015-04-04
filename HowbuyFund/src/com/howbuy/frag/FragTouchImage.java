package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.howbuy.config.ValConfig;
import com.howbuy.control.TouchImage;
import com.howbuy.entity.NewsItem;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.StrUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

@SuppressLint("SetJavaScriptEnabled")
public class FragTouchImage extends AbsHbFrag implements ImageLoadingListener {

	private static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_menu_refresh)
			.showImageOnFail(R.drawable.ic_action_discard).resetViewBeforeLoading(true)
			.cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(0)).build();// ;

	// private NewsItem mBean = null;
	private String mUrl = null;
	private TouchImage mIvTouch = null;
	private View mLayProgress;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSherlockActivity().getSupportActionBar().hide();
		getSherlockActivity().getWindow().setBackgroundDrawable(new ColorDrawable(0xaa000000));
	}

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
			}
			// mBean = arg.getParcelable(ValConfig.IT_ENTITY);
			mUrl = arg.getString(ValConfig.IT_URL);
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mIvTouch = (TouchImage) root.findViewById(R.id.iv_touch);
		mLayProgress = root.findViewById(R.id.lay_progress);
		parseArgments(getArguments());
		if (!StrUtils.isEmpty(mUrl)) {
			if (mUrl.startsWith("data:image/")) {
				int base64 = mUrl.indexOf("base64,");
				if (base64 != -1) {
					try {
						byte[] bs = Base64.decode(mUrl.substring(base64 + 7), 0);
						Bitmap bm = BitmapFactory.decodeByteArray(bs, 0, bs.length);
						mIvTouch.setImageBitmap(bm);
					} catch (Exception e) {
						e.printStackTrace();
						if (getSherlockActivity() != null) {
							getSherlockActivity().finish();
						}
					}

				}
			} else {
				ImageLoader.getInstance().displayImage(mUrl, mIvTouch, options, this);
			}
			mIvTouch.setGesture(new GestureDetector(new TouchDispatcher()));
		} else {
			if (getSherlockActivity() != null) {
				getSherlockActivity().finish();
			}
		}
	}

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		return handed ? true : super.onXmlBtClick(v);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_touchimg;
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		showProgress(false);
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		showProgress(false);
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		showProgress(false);
	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		showProgress(true);
	}

	private void showProgress(boolean show) {
		if (show) {
			if (mLayProgress.getVisibility() != View.VISIBLE) {
				mLayProgress.setVisibility(View.VISIBLE);
			}

		} else {
			if (mLayProgress.getVisibility() == View.VISIBLE) {
				mLayProgress.setVisibility(View.GONE);
			}

		}
	}

	class TouchDispatcher extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			RectF rect = mIvTouch.getImageBounds();
			if (rect != null && !rect.isEmpty()) {
				if (rect.contains((int) e.getX(), (int) e.getY())) {
					/*
					 * float scale=mIvTouch.getCurrentScale(false);
					 * if(scale<1.5f){
					 * 
					 * }else{
					 * 
					 * } d("onDoubleTap", ""+scale);
					 */
				}
			}
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			/*
			 * RectF rect = mIvTouch.getImageBounds(); if (rect != null &&
			 * !rect.isEmpty()) { if (!rect.contains((int) e.getX(), (int)
			 * e.getY())) { getSherlockActivity().finish(); } }
			 */
			getSherlockActivity().finish();
			getSherlockActivity().overridePendingTransition(0, R.anim.popup_scale_exit);
			return super.onSingleTapConfirmed(e);
		}
	}

}
