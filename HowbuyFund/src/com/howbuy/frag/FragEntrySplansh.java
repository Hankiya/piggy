package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.howbuy.aty.AtyTbMain;
import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.utils.Receiver;

public class FragEntrySplansh extends AbsHbFrag {
	//private ImageView mIvScreen, mIvLogo;
	//private boolean mFirstPublic = true;

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_entry_splansh;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (mTitleLable == null) {
			mTitleLable = getArguments() != null ? getArguments().getString(ValConfig.IT_NAME)
					: null;
		}
		//mIvScreen = (ImageView) root.findViewById(R.id.iv_full_img);
		//mIvLogo = (ImageView) root.findViewById(R.id.iv_logo);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (AppFrame.getApp().hasFlag(AppFrame.GLOBAL_SERVICE_INITAPP)) {
			redictedToMain(false, 1500);
		}
	}

	@Override
	public boolean shouldEnableLocalBroadcast() {
		return true;
	}

	@Override
	public void onReceiveBroadcast(int from, Bundle b) {
		if (from == Receiver.FROM_UPGRADE_DB) {
			redictedToMain(false, 1200);
		}
	}

	public void redictedToMain(boolean anim, final int delay) {
		AppFrame.getApp().getLocalBroadcast().unregisterLocalBroadcast(this);
		final Intent i = new Intent(getActivity(), AtyTbMain.class);//
		if (anim) {
			getActivity()
					.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} else {
			getActivity().overridePendingTransition(0, 0);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		}
		GlobalApp.getApp().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// LogUtils.d("AppService",
				// "redictedToMain from Splansh broadCast=" + fromBroadCast);
				startActivity(i);
			}
		}, delay);

	}

}
