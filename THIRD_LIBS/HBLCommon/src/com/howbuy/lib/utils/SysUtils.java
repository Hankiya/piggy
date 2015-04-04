package com.howbuy.lib.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;

import com.howbuy.lib.interfaces.INetChanged;

/**
 * this class provide operator that relate to application
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 下午2:57:56
 */
public class SysUtils {

	private static Object mTempObj = null;

	/**
	 * set a static global object for temporary.
	 * 
	 * @param @param obj for any data.
	 */
	public static void setObj(Object obj) {
		mTempObj = obj;
	}

	/**
	 * get a temporary global object and set the global value after get it.
	 * 
	 * @return Object
	 * @throws
	 */
	public static Object getObjAdClear() {
		Object obj = mTempObj;
		mTempObj = null;
		return obj;
	}

	public static String getModel() {
		return android.os.Build.MODEL;
	}

	public static String getRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * get sdk version 1 (0x00000001) Android 1.0 BASE 2 (0x00000002) Android
	 * 1.1 BASE_1_1 3 (0x00000003) Android 1.5 CUPCAKE 4 (0x00000004) Android
	 * 1.6 DONUT 5 (0x00000005) Android 2.0 ECLAIR 6 (0x00000006) Android 2.0.1
	 * ECLAIR_0_1 7 (0x00000007) Android 2.1 ECLAIR_MR1 8 (0x00000008) Android
	 * 2.2 FROYO 9 (0x00000009) Android 2.3 GINGERBREAD 10 (0x0000000a) Android
	 * 2.3.3 GINGERBREAD_MR1 11 (0x0000000b) Android 3.0 HONEYCOMB 12
	 * (0x0000000c) Android 3.1 HONEYCOMB_MR1 13 (0x0000000d) Android 3.2
	 * HONEYCOMB_MR2
	 */
	public static int getApiVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	public static String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * get the version of application with appointed context.
	 * 
	 * @param @param cx
	 * @return int return -1 if no application information
	 * @throws
	 */
	public static int getVersion(Context cx) {
		try {
			PackageInfo info = getPackageInf(cx);
			{
				if (info != null)
					return info.versionCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * get the version name of application with appointed context.
	 * 
	 * @param @param cx
	 * @return String
	 * @throws
	 */
	public static String getVersionName(Context cx) {
		try {
			PackageInfo info = getPackageInf(cx);
			{
				if (info != null)
					return info.versionName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the maximum amount of memory that may be used by the virtual
	 * machine, or Long.MAX_VALUE if there is no such limit.
	 * 
	 * @return int return max available memory measured in KB;
	 */
	public static int getMaxMemory() {
		return (int) (Runtime.getRuntime().maxMemory() / 1024);
	}

	/**
	 * get IMEI this require read phone state permission.
	 * 
	 * @param context
	 * @return null if null;
	 */
	public static String getImei(Context cx) {
		String Imei = "";
		try {
			Imei = ((TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE))
					.getDeviceId();
		} catch (Exception e) {
		}
		return Imei;
	}

	/**
	 * get mac address
	 * 
	 * @param context
	 * @return
	 */
	public static String getWifiMac(Context context) {
		String macAddress = null;
		WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info) {
			macAddress = info.getMacAddress();
			return macAddress;
		}
		return null;
	}

	/**
	 * @return int 0 for null, 1 for GPS_INTERNET , 2 for GPS_SATELLITE ,3 for
	 *         both enable.
	 * @throws
	 */
	public static int getGPSType(final Context context) {
		LocationManager lmg = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		int type = 0;
		if (lmg.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			type |= 1;
		}
		if (lmg.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			type |= 2;
		}
		return type;
	}

	/**
	 * get net type , net state can be judge by comparing with
	 * INetChanged.NET_XXX;
	 * 
	 * @param @param context
	 * @throws
	 */

	public static int getNetType(Context mContext) {
		ConnectivityManager conMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInf = conMgr.getActiveNetworkInfo();
		if (netInf != null && netInf.isAvailable()) {
			int type = netInf.getType();
			int subType = netInf.getSubtype();
			if (type == ConnectivityManager.TYPE_WIFI) {
				return INetChanged.NET_TYPE_WIFI;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return INetChanged.NET_TYPE_2G; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return INetChanged.NET_TYPE_2G; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return INetChanged.NET_TYPE_2G; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return INetChanged.NET_TYPE_3G; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return INetChanged.NET_TYPE_3G; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return INetChanged.NET_TYPE_2G; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return INetChanged.NET_TYPE_2G; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return INetChanged.NET_TYPE_3G; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return INetChanged.NET_TYPE_2G; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return INetChanged.NET_TYPE_3G; // ~ 400-7000 kbps
				case TelephonyManager.NETWORK_TYPE_EHRPD://
					return INetChanged.NET_TYPE_3G; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B://
					return INetChanged.NET_TYPE_3G; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP://
					return INetChanged.NET_TYPE_3G; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN:
					return INetChanged.NET_TYPE_2G; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE://
					return INetChanged.NET_TYPE_3G; // ~ 10+ Mbps
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					return INetChanged.NET_TYPE_UNKNOW;
				default:
					return INetChanged.NET_TYPE_2G;
				}
			} else {
				return INetChanged.NET_TYPE_NONE;
			}
		}
		return INetChanged.NET_TYPE_NONE;
	}

	/**
	 * judge whether this device has root permission.
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

	/**
	 * get the unique uuid as key anytime . it is different from device and time
	 * so that it is unique.
	 * 
	 * @return String
	 * @throws
	 */
	public static String geneUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * get package information of context.
	 * 
	 * @param @param cx
	 * @return PackageInfo
	 * @throws
	 */
	public static PackageInfo getPackageInf(Context cx) {
		try {
			PackageManager manager = cx.getPackageManager();
			return manager.getPackageInfo(cx.getPackageName(), 0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return String[0] for company ,String[1] for project;
	 * @throws
	 */
	public static String[] parsePackageName(Context cx) {
		String[] result = new String[2];
		String[] ss = cx.getPackageName().split("\\.");
		int len = ss == null ? 0 : ss.length;
		if (len > 1) {
			if (ss[0].equals("com")) {
				result[0] = ss[1];
			} else {
				result[0] = ss[0];
			}
			result[1] = ss[len - 1];
		} else {
			result[0] = result[1] = (len == 1 ? ss[0] : null);
		}
		return result;
	}

	/**
	 * iTelephony = (ITelephony) getITelephonyMethod.invoke(telMgr,
	 * (Object[])null); iTelephony.endCall();
	 */
	/**
	 * judge whether it's ready for a call.
	 * 
	 * @param context
	 */
	public static boolean callEnable(Context context) {
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			return false;
		} else {
			return true;
		}
	}

	@SuppressLint("NewApi")
	public static final void copyText(Context cx, String source) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) cx
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(source);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) cx
					.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData.newPlainText(source, source);
			clipboard.setPrimaryClip(clip);
		}
	}

	/**
	 * get screen width and height.
	 * 
	 * @param @param activity
	 * @return int[]
	 * @throws
	 */
	public static int[] getDisplay(Activity activity) {
		int[] result = new int[2];
		Display display = activity.getWindowManager().getDefaultDisplay();
		result[0] = display.getWidth();
		result[1] = display.getHeight();
		return result;
	}

	/**
	 * get screen width and height.
	 * 
	 * @return int[]
	 * @throws
	 */
	public static int[] getDisplay(Context context) {
		int[] result = new int[2];
		result[0] = context.getResources().getDisplayMetrics().widthPixels;
		result[1] = context.getResources().getDisplayMetrics().heightPixels;
		return result;
	}

	public static int getWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = getDensity(context);
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * @param context
	 * @param TypedValue_unit
	 *            TypedValue.COMPLEX_UNIT_PX://像素
	 *            TypedValue.COMPLEX_UNIT_DIP://设备独立像素
	 *            TypedValue.COMPLEX_UNIT_SP://可缩放像素
	 * @param value
	 * @return
	 */
	public static float getDimension(Context context, int TypedValue_unit, float value) {
		switch (TypedValue_unit) {
		case TypedValue.COMPLEX_UNIT_PX:// 像素
			return value;
		case TypedValue.COMPLEX_UNIT_DIP:// 设备独立像素
			return value * context.getResources().getDisplayMetrics().density;
		case TypedValue.COMPLEX_UNIT_SP:// 可缩放像素
			return value * context.getResources().getDisplayMetrics().scaledDensity;
		case TypedValue.COMPLEX_UNIT_PT:// 磅
			return value * context.getResources().getDisplayMetrics().xdpi * (1.0f / 72);
		case TypedValue.COMPLEX_UNIT_IN:// 英尺
			return value * context.getResources().getDisplayMetrics().xdpi;
		case TypedValue.COMPLEX_UNIT_MM:// 毫米
			return value * context.getResources().getDisplayMetrics().xdpi * (1.0f / 25.4f);
		}
		return 0;
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = getDensity(context);
		return (int) (pxValue / scale + 0.5f);
	}

	public static float sp2px(Context context, int spValue) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context
				.getResources().getDisplayMetrics());
	}

	public static String getPicPathFromUri(Uri uri, Activity activity) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}

	public static void installApk(String apkPath, Context context) {
		File apkfile = new File(apkPath);
		if (!apkfile.exists()) {
			return;
		}
		Uri uri = Uri.fromFile(apkfile);
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(i);
	}

	public static boolean intentSafe(Context aty, Intent it) {
		PackageManager packageManager = aty.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(it, 0);
		return activities.size() > 0;
	}

	/**
	 * judget whether this device have install the application with package
	 * packageName.
	 * 
	 * @param packageName
	 *            the package name of application to judge.
	 */
	private boolean checkAPK(String packageName, Context context) {
		List<PackageInfo> pakageinfos = context.getPackageManager().getInstalledPackages(
				PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo pi : pakageinfos) {
			String pi_packageName = pi.packageName;
			if (packageName.endsWith(pi_packageName)) {
				return true;
			}
		}
		return false;
	}

	public static void setAirplaneMode(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
		context.startActivity(intent);
	}

	public static void setWIFI(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_WIFI_SETTINGS);
		context.startActivity(intent);
	}

	public static void setGPRS(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
		context.startActivity(intent);
	}

	public static Bundle getMetaData(Context context) {
		try {
			ApplicationInfo appi = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			return appi.metaData;

		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static Object getMetaData(Context context, String keyName) {
		Bundle bundle = getMetaData(context);
		if (bundle != null) {
			return bundle.get(keyName);
		}
		return null;
	}

	public static String readShareValues(Context cx, String fileName, String key) {
		return cx.getSharedPreferences(fileName, 0).getString(key, "");
	}

	public static void writeShareValues(Context cx, String fileName, String key, String value) {
		Editor editor = cx.getSharedPreferences(fileName, 0).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void writeShareValues(Context cx, String fileName, HashMap<String, String> maps) {
		Editor editor = cx.getSharedPreferences(fileName, 0).edit();
		Set<Entry<String, String>> entrys = maps.entrySet();
		for (Entry<String, String> entry : entrys) {
			editor.putString(entry.getKey(), entry.getValue());
		}
		editor.commit();
	}

	public static String readFromAssets(Context cx, String filename) {
		String result = null;
		try {
			result = StreamUtils.toString(cx.getAssets().open(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 保持屏幕常亮
	 */
	public static void openKeepScreenOn(Context context) {
		PowerManager.WakeLock wakeLock = ((PowerManager) context
				.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK,
				context.getPackageName());
		wakeLock.acquire();
	}

	/**
	 * 不保持屏幕常亮
	 */
	public static void closeKeepScreenOn(Context context) {
		PowerManager.WakeLock wakeLock = ((PowerManager) context
				.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.FULL_WAKE_LOCK,
				context.getPackageName());
		if (wakeLock.isHeld()) {
			wakeLock.release();
		}
	}

	public static boolean isAllNotNull(Object... obs) {
		for (int i = 0; i < obs.length; i++) {
			if (obs[i] == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 执行反射的方法
	 */
	public static Object invokeMethod(Object owner, String methodName, Class<?>[] parms,
			Object[] args) throws Exception {
		Method method = owner.getClass().getMethod(methodName, parms);
		return method.invoke(owner, args);
	}

	/**
	 * 执行反射的方法
	 */
	public static Object invokeMethod(Class ownerClass, Object owner, String methodName,
			Class<?>[] parms, Object[] args) throws Exception {
		Method method = ownerClass.getMethod(methodName, parms);
		return method.invoke(owner, args);
	}

	/**
	 * 获取反射的属性
	 */
	public static Object invokeField(Object owner, String fieldName) throws Exception {
		Field mField = owner.getClass().getDeclaredField(fieldName);
		if (!mField.isAccessible()) {
			mField.setAccessible(true);
		}
		Object property = mField.get(owner);
		return property;
	}

	/**
	 * 设置反射的属性
	 */
	public static Object invokeField(Object owner, String fieldName, Object obj) throws Exception {
		Field mField = owner.getClass().getDeclaredField(fieldName);
		if (!mField.isAccessible()) {
			mField.setAccessible(true);
		}
		mField.set(owner, obj);
		Object property = mField.get(owner);
		return property;
	}

	public static void simulateKey(final int KeyCode) {

		new Thread() {

			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					inst.waitForIdleSync();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}.start();

	}
}
