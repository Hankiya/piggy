package com.howbuy.aty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.howbuy.component.AppFrame;
import com.howbuy.component.AppService;
import com.howbuy.frag.FragEntryGuider;
import com.howbuy.frag.FragEntrySplansh;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.net.ReqOpt;
import com.howbuy.push.PushDispatch;

public class AtyEntry extends AtyEmpty {
	private boolean mEnableSplanshScreen = true;
	private boolean mEnableShowGuide = false;// 判断是不是第一次启动。

	private boolean parseIntent(Bundle b) {
		return new PushDispatch(this).doPushBund(b);
	}

	@Override
	protected void invokeHostFragment(Intent i) {
		CookieSyncManager.createInstance(getApplicationContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		StringBuffer sb = new StringBuffer();
		AppFrame.getApp().dumpFlag(sb);
		AppFrame app = AppFrame.getApp();
		// Debug.startMethodTracing("howbuy/howbuy.trace");
		app.registerNetReceiver();
		if (!app.hasFlag(AppFrame.GLOBAL_APP_CREATED)) {
			app.getLocalBroadcast().toggleLocalBroadcast(this, true);
			app.getGlobalServiceMger().toggleService(true);
			sb.append("just   toggleService");
		}
		d("invokeHostFragment", sb.toString());
		mEnableSplanshScreen = getIntent().getSerializableExtra("crash_err") == null;
		updateBasicVersion();
		if (getIntent() != null) {
			if (parseIntent(getIntent().getExtras())) {
				return;
			}
		}
		if (mEnableSplanshScreen) {
			FragOpt opt = null;
			if (mEnableShowGuide) {// switch to guide switch.
				opt = new FragOpt(FragEntryGuider.class.getName(), null, 0);
			} else {// switch to normal splansh anim.
				opt = new FragOpt(FragEntrySplansh.class.getName(), null, 0);
			}
			opt.setAnimSystem(FragmentTransaction.TRANSIT_UNSET);
			switchToFrag(opt);
			getSupportFragmentManager().executePendingTransactions();
		} else {
			redictedToMain(false);
		}
	}

	/*
	 * private void printMap(HashMap map, StringBuffer sb) { Iterator it =
	 * map.entrySet().iterator(); while (it.hasNext()) { Entry e = (Entry)
	 * it.next();
	 * sb.append(e.getKey()).append(":").append(e.getValue()).append(" , "); } }
	 */

	public void redictedToMain(boolean anim) {
		Intent i = new Intent(this, AtyTbMain.class);//
		if (anim) {
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		} else {
			overridePendingTransition(0, 0);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		}
		// LogUtils.d("AppService", "redictedToMain from AtyEntry ");
		startActivity(i);
	}

	private void updateBasicVersion() {
		int r = GlobalApp.getApp().getGlobalServiceMger()
				.executeTask(new ReqOpt(0, null, AppService.HAND_APP_START), null);
		// 只有key 为 AppService.KEY_SERVICE_EXECUTE_TASK 的任务的执行状态才会保存和更新.
		AppService.updateExecuteState(AppService.HAND_APP_START, r);
		AppFrame.getApp().getGlobalServiceMger()
				.executeTask(new ReqOpt(0, null, AppService.HAND_SYNC_OPTIONAL), null);
	}

}
