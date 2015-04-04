package com.howbuy.component;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.howbuy.component.DbUtils.IDbUpdate;
import com.howbuy.config.InitConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceMger;
import com.howbuy.lib.net.UrlUtils;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.PathUtils;
import com.howbuy.lib.utils.SysUtils;

public class AppFrame extends GlobalApp implements IDbUpdate {
	private SharedPreferences mSf;
	private static AppFrame mApp = null;
	/**
	 * 是否第一次进入
	 */
	private boolean firstStart;
	private String unicomPhone;
	private InitConfig initParams;

	public static AppFrame getApp() {
		return mApp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
		mService = new GlobalServiceMger(AppService.class);
		PathUtils.initPathConfig(false, true);
		UrlUtils.initUrlBase(ValConfig.URL_BASE_RELEASE,
				ValConfig.URL_BASE_DEBUG);
		DbUtils.initDbUtils(this, 1, null);
		readLogUtilsConfig();
		initParams = new InitConfig(this);
		mSf = getSharedPreferences(ValConfig.SFbaseUser, MODE_PRIVATE);
		if (initParams.isFirstStart()) {
			setFirstStart(true);
			initParams.initNetPublicParams();
		} else {
			initParams.doUpdate(SysUtils.getVersionName(this));
		}
		createPubParams();
		boolean is = AppFrame.getApp().getsF()
				.getBoolean(ValConfig.sFSettingPush, true);
	}

	private void readLogUtilsConfig() {
		// 读取调试配置.
		SharedPreferences sf = GlobalApp.getApp().getSharedPreferences(
				ValConfig.SECRET_SF_NAME, 0);
		LogUtils.mDebugUrl = sf.getBoolean(ValConfig.SECRET_DEBUG_URL, false);
		LogUtils.mDebugPop = sf.getBoolean(ValConfig.SECRET_DEBUG_POP, true);
		LogUtils.mDebugLog = sf.getBoolean(ValConfig.SECRET_DEBUG_LOG, true);
		LogUtils.mDebugLogFile = sf.getBoolean(ValConfig.SECRET_DEBUG_LOG_FILE,
				true);
		LogUtils.mDebugCrashMutiLogFile = sf.getBoolean(
				ValConfig.SECRET_DEBUG_CRASH_MUTIFILE, true);
		LogUtils.mDebugCrashDialog = sf.getBoolean(
				ValConfig.SECRET_DEBUG_CRASH_DIALOG, true);
		LogUtils.mDebugCrashLaunch = sf.getBoolean(
				ValConfig.SECRET_DEBUG_CRASH_LAUNCH, true);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		System.gc();
		super.onLowMemory();
	}

	public boolean isFirstStart() {
		return firstStart;
	}

	public void setFirstStart(boolean firstStart) {
		this.firstStart = firstStart;
	}

	public String getUnicomPhone() {
		return unicomPhone;
	}

	public void setUnicomPhone(String unicomPhone) {
		this.unicomPhone = unicomPhone;
	}

	// 七个公共参数存入Application
	public void createPubParams() {
		mMapStr.put("version", mSf.getString(ValConfig.SFfirstVersion, ""));
		mMapStr.put("channelId", mSf.getString(ValConfig.SFfirstChanneIdId, ""));
		mMapStr.put("productId", mSf.getString(ValConfig.SFfirstProductId, ""));
		mMapStr.put("parPhoneModel",
				mSf.getString(ValConfig.SFfirstParPhoneModel, ""));
		mMapStr.put("subPhoneModel",
				mSf.getString(ValConfig.SFfirstSubPhoneModel, ""));
		mMapStr.put("token", mSf.getString(ValConfig.SFfirstUUid, ""));
		mMapStr.put("iVer", mSf.getString(ValConfig.SFfirstIVer, ""));
		mMapStr.put("corpId", "000004");
	}

	public SharedPreferences getsF() {
		return mSf;
	}

	public void setsF(SharedPreferences sF) {
		this.mSf = sF;
	}

	@Override
	public void onDbUpdate(SQLiteDatabase db, int curVersion, int newVersion) {
		LogUtils.pop("curVersion=" + curVersion + " newVersion="
				+ newVersion);
	}
}
