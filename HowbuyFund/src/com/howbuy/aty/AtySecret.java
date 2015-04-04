package com.howbuy.aty;

import howbuy.android.palmfund.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.DisplayMetrics;
import android.view.View;

import com.howbuy.component.AppFrame;
import com.howbuy.config.ValConfig;
import com.howbuy.entity.UserInf;
import com.howbuy.frag.FragDetailsTrust;
import com.howbuy.lib.aty.AbsSfAty;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.net.UrlUtils;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.umeng.analytics.MobclickAgent;

public class AtySecret extends AbsSfAty {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String version = SysUtils.getVersionName(getApplicationContext());
		findPreference(ValConfig.SECRET_DEBUG_MORE).setSummary(
				getString(R.string.set_summary_checkupdate, version));
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected int getPreferencesFromResourceId() {
		return R.xml.xml_settings_secret;
	}

	@Override
	protected String getPreferencesFromResourceName() {
		return ValConfig.SECRET_SF_NAME;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference instanceof ListPreference) {
			preference.setSummary(newValue + "");
			View v = getListView().getSelectedView();
			getListView().requestLayout();

			Intent t = new Intent(this, AtyEmpty.class);
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_ID, String.valueOf("G0000486"));
			b.putString(ValConfig.IT_NAME, "信托详情");
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragDetailsTrust.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			startActivity(t);
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if (preference instanceof CheckBoxPreference) {
			CheckBoxPreference box = (CheckBoxPreference) preference;
			boolean isChecked = box.isChecked();
			if (ValConfig.SECRET_DEBUG_URL.equals(key)) {
				LogUtils.mDebugUrl = isChecked;
				pop("debug url is " + isChecked, false);
			} else if (ValConfig.SECRET_DEBUG_LOG.equals(key)) {
				LogUtils.mDebugLog = isChecked;
				pop("debug log is " + isChecked, false);
			} else if (ValConfig.SECRET_DEBUG_LOG_FILE.equals(key)) {
				LogUtils.mDebugLogFile = isChecked;
				pop("debug log file is " + isChecked, false);
			} else if (ValConfig.SECRET_DEBUG_POP.equals(key)) {
				LogUtils.mDebugPop = isChecked;
				pop("debug pop is " + isChecked, false);
			} else if (ValConfig.SECRET_DEBUG_CRASH_MUTIFILE.equals(key)) {
				LogUtils.mDebugCrashMutiLogFile = isChecked;
				pop("debug muti crash file is " + isChecked, false);
			} else if (ValConfig.SECRET_DEBUG_CRASH_DIALOG.equals(key)) {
				LogUtils.mDebugCrashDialog = isChecked;
				pop("debug crash show dialog is " + isChecked + " next time works.", false);
			} else if (ValConfig.SECRET_DEBUG_CRASH_LAUNCH.equals(key)) {
				LogUtils.mDebugCrashLaunch = isChecked;
				pop("debug crash launch self is " + isChecked, false);
			} else {
				pop(key, true);
			}
		} else {
			if (ValConfig.SECRET_DEBUG_MORE.equals(key)) {
				showMoreInfs("更多信息");
			} else {
				pop(key, true);
			}
		}
		return true;
	}

	@Override
	protected void onAbsBuildActionBar() {
		buildActionBarSimple();
	}

	@Override
	public void onXmlBtClick(View v) {
	}

	@Override
	public boolean onNetChanged(int netType, int preType) {
		// TODO Auto-generated method stub
		return false;
	}

	private void showMoreInfs(String title) {

		PackageInfo info = SysUtils.getPackageInf(this);
		StringBuffer sb = new StringBuffer();
		String l = "\r\n";
		sb.append("应用信息：").append(l);
		sb.append("PROJ_PNAME:").append(info.packageName).append(l);
		sb.append("PROJ_VNAME:").append(info.versionName).append(l);
		sb.append("PROJ_VCODE:").append(info.versionCode).append(l);
		sb.append("PROJ_INNERCODE:").append(getResources().getString(R.string.BUILD_PRIVATE_VERION)).append(l);
		long installTime = info.firstInstallTime;
		long updateTime = info.lastUpdateTime;
		if (installTime != 0) {
			sb.append("INSTALL_TIME:").append(StrUtils.timeFormat(installTime, null)).append(l);
		}
		if (installTime != 0) {
			sb.append("UPDATE_TIME:").append(StrUtils.timeFormat(updateTime, null)).append(l);
		}

		sb.append(l).append("设备信息：").append(l);
		sb.append("MOBILE_IEME:").append(SysUtils.getImei(GlobalApp.getApp())).append(l);
		sb.append("MOBILE_MODEL:").append(SysUtils.getModel()).append(l);
		sb.append("MOBILE_OS:ANDROID_").append(SysUtils.getOsVersion()).append(l);
		sb.append("MOBILE_API:").append(SysUtils.getApiVersion()).append(l);
		if (UserInf.getUser().isLogined()) {
			sb.append("USER_NAME:").append(UserInf.getUser().getUserName());
		}
		DisplayMetrics ds = getResources().getDisplayMetrics();
		sb.append("MOBILE_DPI:").append(ds.densityDpi).append(l);
		sb.append("MOBILE_DENSITY:").append(ds.density).append(l);
		sb.append("MOBILE_SIZE:").append(ds.widthPixels + "×" + ds.heightPixels).append(l);
		sb.append(l).append("司服地址：").append(l).append(UrlUtils.buildUrl(null)).append(l);
		sb.append(l).append("网络公参数：").append(l);
		HashMap<String, String> map = AppFrame.getApp().getMapStr();
		Iterator<Entry<String, String>> its = map.entrySet().iterator();
		Entry<String, String> it = null;
		while (its.hasNext()) {
			it = its.next();
			String key = it.getKey();
			String val = it.getValue();
			if (!StrUtils.isEmpty(val)) {
				sb.append(key).append(":").append(val).append(l);
			}
		}
		sb.append(l).append("META数据元：").append(l);
		String umeng_channel = SysUtils.getMetaData(this).get("UMENG_CHANNEL") + "";
		String channelid = SysUtils.getMetaData(this).get("channeId") + "";
		String coopId = SysUtils.getMetaData(this).get("TransactionCoopId") + "";
		String actionId = SysUtils.getMetaData(this).get("TransactionActionId") + "";
		String umengKey = SysUtils.getMetaData(this).get("UMENG_APPKEY") + "";
		String jpushKey = SysUtils.getMetaData(this).get("JPUSH_APPKEY") + "";
		if (!StrUtils.isEmpty(coopId)) {
			sb.append("channelName:").append(umeng_channel).append(l);
		}
		if (!StrUtils.isEmpty(actionId)) {
			sb.append("channelId:").append(channelid).append(l);
		}
		if (!StrUtils.isEmpty(coopId)) {
			sb.append("coopId:").append(coopId).append(l);
		}
		if (!StrUtils.isEmpty(actionId)) {
			sb.append("actionId:").append(actionId).append(l);
		}
		if (!StrUtils.isEmpty(umengKey)) {
			int dlen = umengKey.length() / 3;
			if (dlen > 0) {
				sb.append("umeng:").append(umengKey.substring(0, dlen));
				sb.append("***").append(umengKey.substring(dlen + dlen));
				sb.append(l);
			} else {
				sb.append("umeng:").append(umengKey).append(l);
			}
		}
		if (!StrUtils.isEmpty(jpushKey)) {
			int dlen = jpushKey.length() / 3;
			if (dlen > 0) {
				sb.append("jpush:").append(jpushKey.substring(0, dlen));
				sb.append("***").append(jpushKey.substring(dlen + dlen));
				sb.append(l);
			} else {
				sb.append("jpush:").append(jpushKey).append(l);
			}
		}
		sb.append(l).append("程序服务：").append(l);
		AppFrame.getApp().dumpFlag(sb);
		sb.append(l);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(sb.toString());
		builder.setPositiveButton(android.R.string.ok, null);
		builder.show();
	}
}
