package com.howbuy.lib.utils;


import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.howbuy.lib.compont.GlobalApp;

/**
 * this class is hot used as print log and toast message and write log to local.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 上午11:09:56
 */
public class LogUtils {
	private static String TAG_D = "LogUtils_D",
			TAG_P = "LogUtils_P";
	public static boolean mDebugUrl = false;
	public static boolean mDebugLog = true;
	public static boolean mDebugLogFile = false;
	public static boolean mDebugPop = false;
	public static boolean mDebugCrashMutiLogFile = false;
	public static boolean mDebugCrashDialog = false;
	public static boolean mDebugCrashLaunch = false;

	private static Toast mToast = null;
	private static LogTracker mTracker = null;

	public static String getDebugState() {
		StringBuffer sb = new StringBuffer(128);
		sb.append(" debug_url=").append(mDebugUrl);
		sb.append(",debug_log=").append(mDebugLog);
		sb.append(",debug_pop=").append(mDebugPop);
		sb.append(",debug_log_file=").append(mDebugLogFile);
		sb.append(",debug_crash_muti_logfile=").append(mDebugCrashMutiLogFile);
		sb.append(",debug_crash_dialog=").append(mDebugCrashDialog);
		sb.append(",debug_crash_launch=").append(mDebugCrashLaunch);
		return sb.toString();
	}

	/**
	 * mCbCheck toast state and ensure it work normal.
	 */
	private static void checkToast() {
		if (mToast == null) {
			mToast = Toast.makeText(GlobalApp.getApp(), null,
					Toast.LENGTH_SHORT);
		}
	}

	public static View getToastView() {
		checkToast();
		return mToast.getView();
	}

	/**
	 * this view must have a TextView control with id android.R.id.message.
	 * 
	 * @param @param v
	 * @return boolean true for successfully setup.
	 */
	public static boolean setToastView(View v) {
		if (v != null) {
			if (v.findViewById(android.R.id.message) != null) {
				checkToast();
				mToast.setView(v);
				return true;
			}
		} else if (mToast != null) {
			mToast.cancel();
			mToast = null;
			checkToast();
		}

		return false;
	}

	public static void setToastGravity(int gravity, int xOffset, int yOffset) {
		checkToast();
		if (gravity > 0) {
			mToast.setGravity(gravity, xOffset, yOffset);
		} else {
			mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,
					xOffset, yOffset);
		}
	}

	/**
	 * Show toast only when its debug state,call this only in ui thread.
	 * 
	 * @param msg
	 */
	public static void popDebug(String msg) {
		if (mDebugPop) {
			pop(msg);
		} else {
			d(TAG_P, msg);
		}

	}

	/**
	 * only call this in ui thread,for release toast.
	 * 
	 * @param msg
	 */
	public static void pop(String msg) {
		if (!StrUtils.isEmpty(msg)) {
			try {
				checkToast();
				mToast.setText(msg);
				mToast.show();
			} catch (Exception e) {
				d(e + "");
				e.printStackTrace();
			}
			d(TAG_P, msg);
		} else if (mToast != null) {
			mToast.cancel();
		}
	}

	public static void popOnUiThrd(final String msg, final boolean debug) {
		GlobalApp.getApp().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (debug) {
					popDebug(msg);
				} else {
					pop(msg);
				}
			}
		}, 0);
	}

	/**
	 * print message in debug level.
	 */
	public static void d(String tag, String msg) {
		if (mDebugLog && msg != null) {
			Log.d(tag, msg);
		}
	}

	/**
	 * print message in debug level.
	 */
	public static void d(String msg) {
		d(TAG_D, msg);
	}

/*	public static void e(String tag, String msg) {
		if (msg != null) {
			Log.e(tag, msg);
		}
	}

	public static void e(String msg) {
		if (msg != null) {
			Log.e(TAG_E, msg);
		}
	}*/

	/**
	 * @param @param dir null for PathUtils.PATH_CACHE_LOG or SDCard root dir.
	 * @return void
	 * @throws
	 */
	public static void initLogTracker(String dirPath) {
		mTracker = LogTracker.getInstance(dirPath);
	}

	/**
	 * prepare to write log into log file ; ensure call initLogTracker before
	 * invoke this method.
	 * 
	 * @param newFileName
	 *            a new file to write the log or a default file if this param is
	 *            null;
	 */
	public static void startLog(String newFileName) {
		if (mDebugLogFile) {
			if (mTracker == null) {
				initLogTracker(PathUtils.PATH_LOG);
			}
			mTracker.startTracking(newFileName);
		}
	}

	/**
	 * append a log message to the log file ; ensure the initLogTracker and
	 * startLog has called before write the log.
	 * 
	 * @param @param log
	 * @return void
	 * @throws
	 */
	public static void appendLog(String log) {
		if (mTracker != null && mDebugLogFile) {
			mTracker.appendLog(log);
		}
	}

	/**
	 * close the log;
	 * 
	 * @param
	 * @return void
	 * @throws
	 */
	public static void endLog() {
		if (mTracker != null) {
			mTracker.stopTracking();
		}
	}

}
