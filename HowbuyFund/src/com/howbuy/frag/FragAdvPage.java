package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.ViewUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class FragAdvPage extends AbsHbFrag implements ImageLoadingListener {
	private static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_menu_refresh)
			.showImageOnFail(R.drawable.ic_action_discard).resetViewBeforeLoading(true)
			.cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(0)).build();// ;
	ImageView mImageView;
	ProgressBar mProgressBar;
	TextView madText;

	String mImageUrl;
	String mActionString;
	String mIdString;
	String mTitle;

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (bundle != null) {
			mImageUrl = bundle.getString("advImageUrl");
			mIdString = bundle.getString("advID");
			mActionString = bundle.getString("advEventCode");
			mTitle = bundle.getString("advTitle");
		} else {
			mImageUrl = getArguments().getString("advImageUrl");
			mIdString = getArguments().getString("advID");
			mActionString = getArguments().getString("advEventCode");
			mTitle = getArguments().getString("advTitle");
		}

		mImageView = (ImageView) root.findViewById(R.id.adimage);
		mImageView.setTag(R.id.key_tag0, mIdString);
		mImageView.setTag(R.id.key_tag1, mActionString);
		mImageView.setTag(R.id.key_tag2, mTitle);
		mProgressBar = (ProgressBar) root.findViewById(R.id.adprogerss);
		madText = (TextView) root.findViewById(R.id.adtext);
		if (mFragCreateType == CREATE_FIRST_TIME) {
			ViewUtils.setVisibility(mProgressBar, View.VISIBLE);
		} else {
			ViewUtils.setVisibility(mProgressBar, View.GONE);
		}
		loadAdvBitmap();
	}

	private void loadAdvBitmap() {
		ImageLoader.getInstance().displayImage(mImageUrl, mImageView, options, this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("advID", mIdString);
		outState.putString("advTitle", mTitle);
		outState.putString("advImageUrl", mImageUrl);
		outState.putString("advEventCode", mActionString);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_pager_adv;
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		ViewUtils.setVisibility(mProgressBar, View.GONE);
	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {

	}
}