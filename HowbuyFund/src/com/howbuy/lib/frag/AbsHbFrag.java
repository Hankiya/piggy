package com.howbuy.lib.frag;

import android.os.Bundle;

import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.utils.Receiver.ILocalBroadcast;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-1-22 下午5:02:55
 */
public abstract class AbsHbFrag extends AbsFrag implements ILocalBroadcast {
	private boolean mHasRegister = false;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mTitleLable != null) {
			getSherlockActivity().getSupportActionBar().setTitle(mTitleLable);
		}
		if (mTitleLable != null) {
			Analytics.onPageStart(mTitleLable); // 统计一级页面
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mTitleLable != null) {
			Analytics.onPageEnd(mTitleLable); // 统计一级页面
		}
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		if (shouldEnableLocalBroadcast()) {
			mHasRegister = true;
			AppFrame.getApp().getLocalBroadcast().registerLocalBroadcast(this);
		}
		super.onActivityCreated(bundle);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHasRegister) {
			mHasRegister = false;
			AppFrame.getApp().getLocalBroadcast().unregisterLocalBroadcast(this);
		}
	}

	public boolean shouldEnableLocalBroadcast() {
		return false;
	}

	@Override
	public void onReceiveBroadcast(int from, Bundle b) {
	}
}
