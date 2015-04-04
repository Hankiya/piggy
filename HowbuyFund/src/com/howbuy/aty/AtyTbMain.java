package com.howbuy.aty;

import howbuy.android.palmfund.R;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.frag.FragSearch;
import com.howbuy.frag.FragTbHome;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.utils.NetToastUtils;

public class AtyTbMain extends AtyEmpty/* implements IFragTabChanged */{
	protected void invokeHostFragment(Intent i) {
		AppFrame app = AppFrame.getApp();
		app.addFlag(AppFrame.GLOBAL_APP_CREATED);
		if (!app.hasFlag(AppFrame.GLOBAL_SERVICE_NETRECEIVE)) {
			app.registerNetReceiver();
		}
		if (app.getNetType() <= 1) {
			NetToastUtils.whenGlobalChanged(Intent.ACTION_SCREEN_ON, null);
		}
		Bundle b = new Bundle();
		b.putString(ValConfig.IT_NAME, getString(R.string.tb_title_home));
		FragOpt opt = new FragOpt(FragTbHome.class.getName(), b, FragOpt.FRAG_CACHE);
		opt.setAnimSystem(FragmentTransaction.TRANSIT_UNSET);
		switchToFrag(opt);
		int curNet = GlobalApp.getApp().getNetType();
		if (curNet <= 1) {
			NetToastUtils.showNetToastIfNeed(this, curNet, curNet);
		}
	}

	@Override
	protected void onAbsBuildActionBar() {
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	protected void onDestroy() {
		super.onDestroy();
		AppFrame app = AppFrame.getApp();
		app.getGlobalServiceMger().toggleTimer(false, 0, 0);
		app.getGlobalServiceMger().toggleService(false);
		app.unregisterNetReceiver();
		app.getLocalBroadcast().toggleLocalBroadcast(this, false);
		app.subFlag(AppFrame.GLOBAL_APP_CREATED);
		HashMap<String, Object> map = AppFrame.getApp().getMapObj();
		map.remove(FragSearch.Map_Search_GM);
		map.remove(FragSearch.Map_Search_Sm);
		// Debug.stopMethodTracing();
	}

}
