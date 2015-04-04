package com.howbuy.component;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.howbuy.config.ValConfig;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceMger;
import com.howbuy.lib.net.UrlUtils;
import com.howbuy.utils.NetToastUtils;
import com.howbuy.utils.Receiver;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

public class AppFrame extends GlobalApp {
	public static final int GLOBAL_SERVICE_BROADCAST = 32;
	private Receiver mLocalBroadcast = null;
	private static AppFrame mApp = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
		mService = new GlobalServiceMger(AppService.class);
		initImageLoader(getApplicationContext());
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setDebugMode(false);
		UrlUtils.initUrlBase(ValConfig.URL_BASE_RELEASE, ValConfig.URL_BASE_DEBUG);
		mLocalBroadcast = new Receiver();
		mLocalBroadcast.toggleLocalBroadcast(this, true);
		boolean needUpdate = new InitHelper(this).initUpdate();
		d("onCreate", "update database before 3.0 is " + needUpdate);
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static AppFrame getApp() {
		return mApp;
	}

	@Override
	public void onLowMemory() {
		System.gc();
		super.onLowMemory();
	}

	public Receiver getLocalBroadcast() {
		return mLocalBroadcast;
	}

	@Override
	public StringBuffer dumpFlag(StringBuffer sb) {
		super.dumpFlag(sb);
		if (hasFlag(GLOBAL_SERVICE_BROADCAST)) {
			sb.append("GLOBAL_SERVICE_BROADCAST").append(",");
		}
		return sb;
	}

	protected IntentFilter mergeBroadcaset(IntentFilter filter) {
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		return filter;
	}

	protected void onMergeBroadcastReceive(Context context, Intent data) {
		boolean globalChanged = NetToastUtils.whenGlobalChanged(data.getAction(), data.getExtras());
		if (globalChanged) {
			d("onMergeBroadcastReceive", data.getAction() + ",need notify global .");
		}
	}
}
