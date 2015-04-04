package com.howbuy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.INetChanged;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.SysUtils;

public class NetToastUtils {
	public static final int GLOBAL_SCREEN_ON = 64;
	public static final int GLOBAL_HOME_PRESS = 128;
	private static long mLastGlobalChanged = 0;

	public static boolean whenGlobalChanged(String action, Bundle b) {
		int needAlermNet = 0;
		if (Intent.ACTION_SCREEN_ON.equals(action)) {
			needAlermNet = GLOBAL_SCREEN_ON;
		} else {
			if (b != null && b.containsKey("reason")) {
				if ("recentapps".equals(b.getString("reason"))) {
					needAlermNet = GLOBAL_HOME_PRESS;
				}
			}
		}
		if (needAlermNet != 0) {
			if (GlobalApp.getApp().getNetType() <= 1) {
				GlobalApp.getApp().addFlag(needAlermNet);
				mLastGlobalChanged = System.currentTimeMillis();
				return true;
			}
		}
		return false;
	}

	public static boolean showNetToastIfNeed(AbsFrag frag, int curNet, int preNet) {
		boolean alermed = false;
		if (frag.isVisible()) {
			if (curNet <= 1) {
				if (preNet > INetChanged.NET_TYPE_UNKNOW) {
					// LogUtils.pop("网络不可用");
					alermed = true;
				} else {
					if (System.currentTimeMillis() - mLastGlobalChanged < 1000) {
						GlobalApp app = GlobalApp.getApp();
						if ((app.getFlag() & (GLOBAL_SCREEN_ON | GLOBAL_HOME_PRESS)) != 0) {
							Activity aty = frag.getSherlockActivity();
							if (aty == null) {
								aty = AbsAty.getAtys().get(0);
							}
							showSetNetwork(aty, 800);
							mLastGlobalChanged -= 1000;
							app.subFlag(GLOBAL_SCREEN_ON | GLOBAL_HOME_PRESS);
							alermed = true;
						}
					}
				}
			}
		}
		return alermed;
	}

	public static boolean showNetToastIfNeed(Activity aty, int curNet, int preNet) {
		boolean alermed = false;
		if (curNet <= 1) {
			if (preNet > INetChanged.NET_TYPE_UNKNOW) {
				LogUtils.pop("网络不可用");
				alermed = true;
			} else {
				if (System.currentTimeMillis() - mLastGlobalChanged < 1000) {
					GlobalApp app = GlobalApp.getApp();
					if ((app.getFlag() & (GLOBAL_SCREEN_ON | GLOBAL_HOME_PRESS)) != 0) {
						showSetNetwork(aty, 500);
						mLastGlobalChanged -= 1000;
						app.subFlag(GLOBAL_SCREEN_ON | GLOBAL_HOME_PRESS);
						alermed = true;
					}
				}
			}
		}
		return alermed;
	}

	public static void showSetNetwork(final Activity aty, int delayed) {
		if (aty == null) {
			return;
		}
		delayed = Math.max(5, delayed);
		if (delayed > 0 && GlobalApp.getApp().getNetType() <= 1) {
			GlobalApp.getApp().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(aty);
					builder.setTitle("没有网络");
					builder.setMessage("目前没有连接到任何网络，数据可能无法更新。");
					builder.setPositiveButton("去设置", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							launchNetSetting(aty);
						}
					});
					builder.setNegativeButton(android.R.string.ok, null);
					builder.show();
				}
			}, delayed);
		}
	}

	public static void launchNetSetting(final Activity aty) {
		if (SysUtils.getApiVersion() >= 12) {
			aty.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
		} else {
			aty.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
		}
	}
}
