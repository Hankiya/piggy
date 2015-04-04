package com.howbuy.component;

import howbuy.android.palmfund.R;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import cn.jpush.android.api.JPushInterface;

import com.howbuy.config.InitConfig;
import com.howbuy.config.ValConfig;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.upgrade.UpgradeUtil2;
import com.howbuy.utils.Receiver;

public class InitHelper {
	private Context mContext = null;
	private UpgradeUtil2 mUpgrade = null;

	InitHelper(Context cx) {
		mContext = cx;
		mUpgrade = new UpgradeUtil2();
	}

	public boolean initUpdate() {
		if (mUpgrade.needUpdate(mContext)) {
			new Thread() {
				@Override
				public void run() {
					try {
						mUpgrade.doUpdate(mContext);
					} catch (Exception e) {
						e.printStackTrace();
						mUpgrade.cleanApplicationData(mContext, null);
					}
					initApplication(true);
				}
			}.start();
			return true;
		} else {
			initApplication(false);
			return false;
		}
	}

	private void initApplication(boolean fromTrd) {
		AppFrame.getApp().setSharePreference(
				mContext.getSharedPreferences(ValConfig.SFbaseUser, mContext.MODE_PRIVATE));
		InitConfig initParams = new InitConfig(mContext);
		if (initParams.isFirstStart()) {
			initParams.initNetPublicParams();
		} else {
			initParams.doUpdate(SysUtils.getVersionName(mContext));
		}
		createPubParams();
		boolean is = AppFrame
				.getApp()
				.getsF()
				.getBoolean(ValConfig.sFSettingPush,
						mContext.getResources().getBoolean(R.bool.SET_PUSH));
		if (is) {
			JPushInterface.init(mContext); // 初始化 JPush
		}
		AppFrame.getApp().getGlobalServiceMger().toggleService(true);
		LogUtils.d("AppService", "has init application base data.");
		AppFrame.getApp().addFlag(AppFrame.GLOBAL_SERVICE_INITAPP);
		if (fromTrd) {
			AppFrame.getApp().getLocalBroadcast().sendBroadcast(Receiver.FROM_UPGRADE_DB, null);
		}
	}

	// 七个公共参数存入Application
	private void createPubParams() {
		HashMap<String, String> map = AppFrame.getApp().getMapStr();
		SharedPreferences sf = AppFrame.getApp().getsF();
		map.put("version", sf.getString(ValConfig.SFfirstVersion, ""));
		map.put("channelId", sf.getString(ValConfig.SFfirstChanneIdId, ""));
		map.put("productId", sf.getString(ValConfig.SFfirstProductId, ""));
		map.put("parPhoneModel", sf.getString(ValConfig.SFfirstParPhoneModel, ""));
		map.put("subPhoneModel", sf.getString(ValConfig.SFfirstSubPhoneModel, ""));
		map.put("token", sf.getString(ValConfig.SFfirstUUid, ""));
		map.put("iVer", sf.getString(ValConfig.SFfirstIVer, ""));
	}
}
