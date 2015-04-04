package com.howbuy.lib.compont;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.howbuy.lib.error.CrashHandler;
import com.howbuy.lib.error.ErrType;
import com.howbuy.lib.error.ErrorHandler;
import com.howbuy.lib.error.UncaughtExcept;
import com.howbuy.lib.interfaces.INetChanged;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.PathUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;

public class GlobalApp extends Application {
	public static final int GLOBAL_APP_CREATED = 1;
	public static final int GLOBAL_SERVICE_INITAPP = 2;
	public static final int GLOBAL_SERVICE_NETRECEIVE = 4;
	public static final int GLOBAL_SERVICE_SERVICE = 8;
	public static final int GLOBAL_SERVICE_TIMER = 16;

	protected int mFlag;
	public String TAG = "GlobalApp";
	public static String PROJ_NAME = null;
	public static String PROJ_NAME_DISPLAY = null;
	private UncaughtExcept mCaughtExp = null;
	private ErrorHandler mErrHand = null;
	private static GlobalApp mApp = null;
	private ConnectedReceiver mConnector = null;
	private Handler mHandler = new Handler();
	private final ArrayList<INetChanged> mNetList = new ArrayList<INetChanged>();
	private int mNetType = INetChanged.NET_TYPE_NONE;
	protected final HashMap<String, String> mMapStr = new HashMap<String, String>();
	protected final HashMap<String, Object> mMapObj = new HashMap<String, Object>();
	protected GlobalServiceMger mService = null;
	private SharedPreferences mSf;
	
	public SharedPreferences getsF() {
		return mSf;
	}

	public void setSharePreference(SharedPreferences sf) {
		mSf = sf;
	}
	
	public void addNetChangeListener(INetChanged listener) {
		if (!mNetList.contains(listener)) {
			mNetList.add(listener);
		}
	}

	public static GlobalApp getApp() {
		return mApp;
	}

	public GlobalServiceMger getGlobalServiceMger() {
		return mService;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public String getMapStr(String key) {
		return mMapStr.get(key);
	}

	public Object getMapObj(String key) {
		return mMapObj.get(key);
	}

	public HashMap<String, String> getMapStr() {
		return mMapStr;
	}

	public HashMap<String, Object> getMapObj() {
		return mMapObj;
	}

	public int getNetType() {
		return mNetType;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		TAG = getClass().getSimpleName();
		mApp = this;
		PROJ_NAME = SysUtils.parsePackageName(mApp)[1];
		PROJ_NAME_DISPLAY = getApplicationInfo().loadLabel(getPackageManager()) + "";
		if (StrUtils.isEmpty(PROJ_NAME_DISPLAY)) {
			PROJ_NAME_DISPLAY = PROJ_NAME;
		}
		mNetType = SysUtils.getNetType(this);
		PathUtils.initPathConfig(false, true);
		LogUtils.initLogTracker(PathUtils.PATH_LOG);
		mCaughtExp = new UncaughtExcept(new CrashHandler(this, PROJ_NAME));
		Thread.setDefaultUncaughtExceptionHandler(mCaughtExp);
		addFlag(GLOBAL_APP_CREATED);
	}

	public void registerNetReceiver() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mConnector = ((mConnector == null) ? new ConnectedReceiver() : mConnector);
		registerReceiver(mConnector, mergeBroadcaset(filter));
		addFlag(GLOBAL_SERVICE_NETRECEIVE);
		d("registerNetReceiver", "has registerNetReceiver");
	}

	public void removeNetChangeListener(INetChanged listener) {
		mNetList.remove(listener);
	}

	public void runOnUiThread(Runnable action, long delay) {
		if (delay > 10) {
			mHandler.postDelayed(action, delay);
		} else {
			mHandler.post(action);
		}
	}

	public void setCrashHnadler(CrashHandler handler) {
		if (mCaughtExp != null) {
			mCaughtExp.setCrashHnadler(handler);
		}
	}

	public void unregisterNetReceiver() {
		if (mConnector != null) {
			unregisterReceiver(mConnector);
			mConnector = null;
			subFlag(GLOBAL_SERVICE_NETRECEIVE);
			d("unregisterNetReceiver", "has unregisterNetReceiver");
		}

	}

	public ErrType getErrType(Throwable e) {
		if (mErrHand == null) {
			mErrHand = new ErrorHandler();
		}
		return mErrHand.getErrorType(e);

	}

	public void setErrHandler(ErrorHandler handler) {
		if (handler != null) {
			mErrHand = handler;
		}
	}

	private class ConnectedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent data) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(data.getAction())) {
				int n = mNetList.size(), preNetType = mNetType;
				mNetType = SysUtils.getNetType(GlobalApp.this);
				for (int i = 0; i < n; i++) {
					INetChanged l = mNetList.get(i);
					l.onNetChanged(mNetType, preNetType);
				}
			} else {
				onMergeBroadcastReceive(context, data);
			}

		}
	}

	protected IntentFilter mergeBroadcaset(IntentFilter filter) {
		return filter;
	}

	protected void onMergeBroadcastReceive(Context context, Intent data) {

	}

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	final public int getFlag() {
		return mFlag;
	}

	final public synchronized void setFlag(int flag) {
		mFlag = flag;
	}

	final public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final public synchronized void addFlag(int flag) {
		mFlag |= flag;
	}

	final public synchronized void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}

	final public synchronized void reverseFlag(int flag) {
		mFlag ^= flag;
	}

	public StringBuffer dumpFlag(StringBuffer sb) {
		if (hasFlag(GLOBAL_APP_CREATED)) {
			sb.append("GLOBAL_APP_CREATED").append(",");
		}
		if (hasFlag(GLOBAL_SERVICE_INITAPP)) {
			sb.append("GLOBAL_SERVICE_INITAPP").append(",");
		}
		if (hasFlag(GLOBAL_SERVICE_NETRECEIVE)) {
			sb.append("GLOBAL_SERVICE_NETRECEIVE").append(",");
		}
		if (hasFlag(GLOBAL_SERVICE_SERVICE)) {
			sb.append("GLOBAL_SERVICE_SERVICE").append(",");
		}
		if (hasFlag(GLOBAL_SERVICE_TIMER)) {
			sb.append("GLOBAL_SERVICE_TIMER").append(",");
		}
		return sb;
	}
}
