package com.howbuy.lib.aty;

// d\(\s*"[\s\S]*?\);
/**
 * 	String uri="#Intent;action=android.intent.action.MAIN;category=android.intent.category.LAUNCHER;launchFlags=0x10000000;component=com.actionbarsherlock.sample.demos/.SampleList;end";
 item.setIntent(Intent.parseUri(uri, 0));
 */
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.SmartExitTwince;
import com.howbuy.lib.compont.SmartExitTwince.OnExitTwiceListener;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.interfaces.INetChanged;
import com.howbuy.lib.utils.LogUtils;

/**
 * this base Activity extends SherlockFragmentActivity
 */
public abstract class AbsAty extends SherlockFragmentActivity implements OnExitTwiceListener,
		INetChanged {
	public String TAG = "AbsAty";
	private static ArrayList<Activity> mActivitys = new ArrayList<Activity>(5);
	private boolean mEnableTwiceExit = false;
	private boolean mEnableAutoRegesterNetChanged = true;
	private boolean mHasRegesterNet = false;
	private static SmartExitTwince mExitTwince = new SmartExitTwince();
	private int mPauseNet = -1;

	protected void buildActionBarSimple() {
		try {
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		} catch (Exception e) {
			if (e != null) {
				LogUtils.d("buildActionBarSimple", e.getClass().getSimpleName());
			}
		}
	}

	public static void exitApp(boolean kill) {
		while (mActivitys.size() > 0) {
			mActivitys.remove(0).finish();
		}
		if (kill) {
			// DO NOTHING...
		}
	}

	public static void exitAppAlerm(String title, String msg, final boolean kill) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivitys.get(0));
		builder.setTitle(title == null ? "退出提示" : title);
		builder.setMessage(msg == null ? "退出应用程序" : msg);
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				exitApp(kill);
			}
		});
		builder.setNegativeButton(android.R.string.cancel, null);
		builder.show();
	}

	public static ArrayList<Activity> getAtys() {
		return mActivitys;
	}

	public AbsFrag getCurrentFragment() {
		return null;
	}

	protected static boolean isPressTwinceExit() {
		return mExitTwince.isPressTwinceExit();
	}

	public boolean isTwiceExitEnable() {
		return mEnableTwiceExit;
	}

	/**
	 * set ActionBar and youcan setTheme in the first time.
	 */
	protected abstract void onAbsBuildActionBar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivitys.add(0, this);
		TAG = getClass().getSimpleName();
		onAbsBuildActionBar();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mActivitys.remove(this);
	}

	/**
	 * (true,false)第一次按下。 (false,true)第二次按时按下。 (false,false)等待第二次超时。
	 * (true,true)第二次超时按下
	 * ，同时onSecondPressDelayed中调用onKeyBack时返回false时系统不重置mExitTwince
	 * .resetTrace()， 这种情况由于父类定制死，所以永远不会出现，除非重写了onSecondPressDelayed。
	 * 
	 * @param isFirstPress
	 * @param isTwiceInTime
	 * @return
	 */
	protected boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (isFirstPress && !fromBar) {
			return mEnableTwiceExit;
		} else {
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (startTraceExit(this)) {
				if (isPressTwinceExit()) {// twice pressed twice in time
					if (!onKeyBack(false, false, true) && mEnableTwiceExit) {
						exitAppAlerm(null, null, true);
					}
					return true;
				} else {
					mExitTwince.resetTrace();
					if (!onKeyBack(false, true, true)) {
						return super.onKeyDown(keyCode, event);
					}
					return true;
				}
			} else {// first pressed twice in time
				boolean handed = onKeyBack(false, true, false);
				if (handed) {
					if (!mEnableTwiceExit) {
						mExitTwince.resetTrace();
					}
					return true;
				} else {
					mExitTwince.resetTrace();
					return super.onKeyDown(keyCode, event);
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (!onKeyBack(true, true, false)) {
				onBackPressed();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mHasRegesterNet) {
			GlobalApp.getApp().removeNetChangeListener(this);
			mHasRegesterNet = false;
			mPauseNet = GlobalApp.getApp().getNetType();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mEnableAutoRegesterNetChanged && !mHasRegesterNet) {
			GlobalApp.getApp().addNetChangeListener(this);
			mHasRegesterNet = true;
			int curNet = GlobalApp.getApp().getNetType();
			if (needResumeNetNotify(curNet)) {
				onNetChanged(curNet, mPauseNet);
			}
		}
	}

	protected boolean needResumeNetNotify(int cur) {
		if (mPauseNet != -1) {
			if (mPauseNet != cur) {
				return true;
			}
			mPauseNet = -1;
		}
		return false;
	}

	public boolean onSecondPressDelayed() {// twice pressed twice out time
		boolean result = onKeyBack(false, false, false);
		if (!result) {
			mExitTwince.resetTrace();
			onBackPressed();
			// finish();
		}
		return result;
	}

	public void onXmlBtClick(View v) {
	}

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	final protected void dd(String msg, Object... args) {
		d(TAG, String.format(msg, args));
	}

	final protected void pop(String msg, boolean debug) {
		if (debug) {
			LogUtils.popDebug(msg);
		} else {
			LogUtils.pop(msg);
		}
	}

	public static void resetTrace() {
		mExitTwince.resetTrace();
	}

	public static boolean startTraceExit(OnExitTwiceListener listen) {
		return mExitTwince.startTraceExit(listen);
	}

	protected void setTwiceExitEnable(boolean enalbe) {
		mEnableTwiceExit = enalbe;
	}

	protected void setAutoRegesterNetChanged(boolean autoAble) {
		mEnableAutoRegesterNetChanged = autoAble;
		if (mHasRegesterNet && !autoAble) {
			mHasRegesterNet = false;
			GlobalApp.getApp().removeNetChangeListener(this);
		} else {
			if(mPauseNet!=-1){
				if (!mHasRegesterNet && autoAble) {
					mHasRegesterNet = true;
					GlobalApp.getApp().addNetChangeListener(this);
				}	
			}
		}
	}
}
