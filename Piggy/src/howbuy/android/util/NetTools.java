package howbuy.android.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class NetTools {
	public enum NetType {
		type_2g, type_3g, type_wif, type_unKnow, type_none;
	}

	public static final String CTWAP = "ctwap";
	public static final String CTNET = "ctnet";
	public static final String CMWAP = "cmwap";
	public static final String CMNET = "cmnet";
	public static final String UNI3GWAP = "3gwap";
	public static final String UNI3GNET = "3gnet";
	public static final String UNIUNINET = "uninet";
	public static final int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
	public static final int TYPE_WIFI = 10;
	public static final int TYPE_CM_WAP = 21;// 移动联通wap10.0.0.172
	public static final int TYPE_CM_NET = 22;// 移动联通wap10.0.0.172
	public static final int TYPE_UNI_NET = 32;// 电信wap 10.0.0.200
	public static final int TYPE_UNI_WAP = 31;// 电信wap 10.0.0.200
	public static final int TYPE_UNI_UNINET = 33;// 电信wap 10.0.0.200
	public static final int TYPE_CT_WAP = 41;// 电信wap 10.0.0.200
	public static final int TYPE_CT_NET = 42;// 电信wap 10.0.0.200
	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	public static boolean getNetWorkVisable(Context mContext) {
		Context context = mContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
		} else {// 获取所有网络连接信息
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {// 逐一查找状态为已连接的网络
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getNetworkType(Context mContext) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				Log.i("", "=====================>无网络");
				return TYPE_NET_WORK_DISABLED;
			} else {
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					Log.i("", "=====================>wifi网络");
					return TYPE_WIFI;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！
					Cursor c = mContext.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c.getColumnIndex("user"));
						if (!TextUtils.isEmpty(user)) {
							if (user.startsWith(CTWAP)) {
								return TYPE_CT_WAP;
							} else if (user.startsWith(CTNET)) {
								return TYPE_CT_NET;
							}
						}
					}
					c.close();

					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断
					String netMode = mobNetInfoActivity.getExtraInfo();
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase();
						if (netMode.equals(CMNET)) {
							return TYPE_CM_NET;
						} else if (netMode.equals(CMWAP)) {
							return TYPE_CM_WAP;
						} else if (netMode.equals(UNI3GNET)) {
							return TYPE_UNI_NET;
						} else if (netMode.equals(UNI3GWAP)) {
							return TYPE_UNI_WAP;
						} else if (netMode.equals(UNIUNINET)) {
							return TYPE_UNI_UNINET;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return TYPE_NET_WORK_DISABLED;
	}

	/**
	 * 判断2G还是3G
	 * 
	 * @param mContext
	 * @return
	 */
	public static NetType isConnectionFast(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
		if (mobNetInfoActivity != null && mobNetInfoActivity.isAvailable()) {
			int type = mobNetInfoActivity.getType();
			int subType = mobNetInfoActivity.getSubtype();
			if (type == ConnectivityManager.TYPE_WIFI) {
				System.out.println("CONNECTED VIA WIFI");
				return NetType.type_wif;
			} else if (type == ConnectivityManager.TYPE_MOBILE) {
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					return NetType.type_2g; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					return NetType.type_2g; // ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return NetType.type_2g; // ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					return NetType.type_3g; // ~ 400-1000 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					return NetType.type_3g; // ~ 600-1400 kbps
				case TelephonyManager.NETWORK_TYPE_GPRS:
					return NetType.type_2g; // ~ 100 kbps
				case TelephonyManager.NETWORK_TYPE_HSDPA:
					return NetType.type_2g; // ~ 2-14 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPA:
					return NetType.type_3g; // ~ 700-1700 kbps
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					return NetType.type_2g; // ~ 1-23 Mbps
				case TelephonyManager.NETWORK_TYPE_UMTS:
					return NetType.type_3g; // ~ 400-7000 kbps
				case TelephonyManager.NETWORK_TYPE_EHRPD:
					return NetType.type_3g; // ~ 1-2 Mbps
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
					return NetType.type_3g; // ~ 5 Mbps
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					return NetType.type_3g; // ~ 10-20 Mbps
				case TelephonyManager.NETWORK_TYPE_IDEN:
					return NetType.type_2g; // ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE:
					return NetType.type_3g; // ~ 10+ Mbps
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
					return NetType.type_unKnow;
				default:
					return NetType.type_2g;
				}
			} else {
				return NetType.type_none;
			}
		} else {
			return NetType.type_none;
		}

	}
}
