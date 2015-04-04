package howbuy.android.util;

import java.io.File;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class LocalUtil {
	/**
	 * 判断是否可以打电话
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getCanCall(Context context) {
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取mac
	 * 
	 * @param context
	 * @return
	 */
	public static String getWifiMac(Context context) {
		String macAddress = null, ip = null;
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			macAddress = info.getMacAddress();
			return macAddress;
		}
		return null;
	}

	/**
	 * 获取用sdk版本 1 (0x00000001) Android 1.0 BASE 2 (0x00000002) Android 1.1
	 * BASE_1_1 3 (0x00000003) Android 1.5 CUPCAKE 4 (0x00000004) Android 1.6
	 * DONUT 5 (0x00000005) Android 2.0 ECLAIR 6 (0x00000006) Android 2.0.1
	 * ECLAIR_0_1 7 (0x00000007) Android 2.1 ECLAIR_MR1 8 (0x00000008) Android
	 * 2.2 FROYO 9 (0x00000009) Android 2.3 GINGERBREAD 10 (0x0000000a) Android
	 * 2.3.3 GINGERBREAD_MR1 11 (0x0000000b) Android 3.0 HONEYCOMB 12
	 * (0x0000000c) Android 3.1 HONEYCOMB_MR1 13 (0x0000000d) Android 3.2
	 * HONEYCOMB_MR2
	 * 
	 * @param context
	 * @return
	 */
	public static int getBuildVersion(Context context) {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 获取是否root
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasRoot(Context context) {
		File su = new File("/system/bin/su");
		File su2 = new File("/system/xbin/su");
		if (su.exists() || su2.exists()) {
			return true;
		} else {
			return false;
		}
	}

}
