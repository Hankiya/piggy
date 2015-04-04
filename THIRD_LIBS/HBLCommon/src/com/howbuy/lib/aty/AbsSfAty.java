package com.howbuy.lib.aty;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
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
public abstract class AbsSfAty extends SherlockPreferenceActivity implements OnExitTwiceListener,
		OnPreferenceChangeListener, OnPreferenceClickListener, INetChanged {
	public String TAG = "AbsSfAty";
	private boolean mEnableTwiceExit = false;
	private boolean mEnableAutoRegesterNetChanged = true;
	private boolean mHasRegesterNet = false;
	private static SmartExitTwince mExitTwince = new SmartExitTwince();
	private ArrayList<String> mPreferenceKeys = new ArrayList<String>();
    private int mPauseNet=0;
	protected void buildActionBarSimple() {
		try {
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exitApp(boolean kill) {
		AbsAty.exitApp(kill);
	}

	public static boolean isPressTwinceExit() {
		return mExitTwince.isPressTwinceExit();
	}

	protected boolean isTwiceExitEnable() {
		return mEnableTwiceExit;
	}

	/**
	 * set ActionBar
	 */
	protected abstract void onAbsBuildActionBar();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AbsAty.getAtys().add(0,this);
		TAG = getClass().getSimpleName();
		onAbsBuildActionBar();
		getPreferenceManager().setSharedPreferencesName(getPreferencesFromResourceName());
		addPreferencesFromResource(getPreferencesFromResourceId());
		try {
			parsePreferenceAddAction(getPreferencesFromResourceId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parsePreferenceAddAction(int id) throws XmlPullParserException, IOException {
		mPreferenceKeys.clear();
		XmlResourceParser parser = getResources().getXml(id);
		int event = parser.getEventType();
		while (event != XmlResourceParser.END_DOCUMENT) {
			switch (event) {
			case XmlResourceParser.START_DOCUMENT:
				break;
			case XmlResourceParser.START_TAG:
				if (parser.getName().endsWith("Preference")) {
					String key = parser.getAttributeValue(
							"http://schemas.android.com/apk/res/android", "key");
					if (key != null) {
						addPreferenceListener(key);
					}
				} else if (parser.getName().equals("PreferenceScreen")) {
				}
				break;
			case XmlResourceParser.END_TAG:
				break;
			}
			event = parser.next();
		}
	}

	protected void addPreferenceListener(String key) {
		Preference pf = findPreference(key);
		if (pf != null) {
			if (!mPreferenceKeys.contains(key)) {
				mPreferenceKeys.add(key);
			}
			pf.setOnPreferenceChangeListener(this);
			pf.setOnPreferenceClickListener(this);
		}
	}

	protected void removePreferenceListener(String key) {
		Preference pf = findPreference(key);
		if (pf != null) {
			pf.setOnPreferenceChangeListener(null);
			pf.setOnPreferenceClickListener(null);
		}
	}
 
	protected abstract int getPreferencesFromResourceId();

	protected abstract String getPreferencesFromResourceName();
	public AbsFrag getCurrentFragment(){
		return null;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AbsAty.getAtys().remove(this);
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
						AbsAty.exitAppAlerm(null, null, true);
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
				if (onKeyBack(false, true, false) && mEnableTwiceExit) {
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
			if(mPauseNet!=-1){
				int curNet = GlobalApp.getApp().getNetType();
				if (mPauseNet != curNet) {
					onNetChanged(curNet,mPauseNet);
				}	
				mPauseNet=-1;
			} 
		}
	}

	public boolean onSecondPressDelayed() {// twice pressed twice out time
		boolean result = onKeyBack(false, false, false);
		if (!result) {
			mExitTwince.resetTrace();
			onBackPressed();// finish();
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
			if (!mHasRegesterNet && autoAble) {
				mHasRegesterNet = true;
				GlobalApp.getApp().addNetChangeListener(this);
			}
		}
	}
}
