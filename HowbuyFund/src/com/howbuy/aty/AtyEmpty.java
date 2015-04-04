package com.howbuy.aty;

import howbuy.android.palmfund.R;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.view.MenuItem;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.frag.FragSetAbout;
import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.frag.AbsFragMger;
import com.howbuy.lib.frag.AbsFragMger.FragOpt;
import com.howbuy.lib.frag.AbsFragMger.IFragChanged;
import com.howbuy.lib.frag.FragMger;
import com.howbuy.utils.NetToastUtils;

public class AtyEmpty extends AbsAty implements IFragChanged {
	public static final String KEY_FRAG_NAME = "KEY_FRAG_NAME";
	public static final String KEY_FRAG_ARG = "KEY_FRAG_ARG";
	public static final String KEY_EXIT_NOANIM = "KEY_EXIT_NOANIM";
	public static final String KEY_EXIT_TWICEBACK = "KEY_EXIT_TWICEBACK";
	public static final String KEY_AUTO_NET = "KEY_AUTO_NET";
	public static final String KEY_ANIM_ENTER = "KEY_ANIM_ENTER";
	public static final String KEY_ANIM_EXIT = "KEY_ANIM_EXIT";

	private AbsFragMger mFragMger = null;
	private boolean mExitNoAnim = false;
	private int mAnimEnter = 0, mAnimExit = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// FrameLayout content = new FrameLayout(getApplicationContext());
		// content.setId(R.id.frag_empty_id);
		// setContentView(content);
		mFragMger = new FragMger(this,/* R.id.frag_empty_id*/android.R.id.content);
		Intent i = getIntent();
		if (i != null) {
			setTwiceExitEnable(i.getBooleanExtra(KEY_EXIT_TWICEBACK, false));
			setAutoRegesterNetChanged(i.getBooleanExtra(KEY_AUTO_NET, true));
			invokeHostFragment(i);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Resources resource = getResources();
		Configuration c = resource.getConfiguration();
		if (c.fontScale > 1.21f) {
			c.fontScale = 1.21f;
			resource.updateConfiguration(c, resource.getDisplayMetrics());
		}
		Analytics.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Analytics.onPause(this);
	}

	@Override
	public AbsFrag getCurrentFragment() {
		if (mFragMger != null) {
			return mFragMger.getCurrentFrag();
		}
		return null;
	}

	protected void invokeHostFragment(Intent i) {
		String fName = null;
		Bundle fArg = null;
		if (i != null) {
			mExitNoAnim = i.getBooleanExtra(KEY_EXIT_NOANIM, mExitNoAnim);
			mAnimEnter = i.getIntExtra(KEY_ANIM_ENTER, 0);
			mAnimExit = i.getIntExtra(KEY_ANIM_EXIT, 0);
			fName = i.getStringExtra(KEY_FRAG_NAME);
			fArg = i.getBundleExtra(KEY_FRAG_ARG);
		}
		if (fName != null) {
			FragOpt opt = new FragOpt(fName, fArg, FragOpt.FRAG_CACHE);
			opt.setAnimSystem(FragmentTransaction.TRANSIT_UNSET);
			switchToFrag(opt);
		}
	}

	public void switchToFrag(FragOpt fo) {
		if (mFragMger != null) {
			mFragMger.switchToFrag(fo);
		}
	}

	@Override
	public void onXmlBtClick(View v) {
		boolean handled = mFragMger != null && mFragMger.onXmlBtClick(v);
		if (!handled) {
			// TODO
		}
	}

	@Override
	protected boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		boolean handled = mFragMger != null
				&& mFragMger.onKeyBack(fromBar, isFirstPress, isTwiceInTime, isTwiceExitEnable());
		return handled ? true : super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (mExitNoAnim) {
			overridePendingTransition(0, 0);
		} else {
			if (mAnimEnter != 0 || mAnimExit != 0) {
				overridePendingTransition(mAnimEnter, mAnimExit);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean handled = true;
		switch (item.getItemId()) {
		case R.id.menu_setting: {
			startActivity(new Intent(this, AtySettings.class));
			break;
		}
		case R.id.menu_about: {
			Bundle b = new Bundle();
			b.putString(ValConfig.IT_NAME, item.getTitle().toString());
			Intent t = new Intent(this, AtyEmpty.class);
			t.putExtra(AtyEmpty.KEY_FRAG_NAME, FragSetAbout.class.getName());
			t.putExtra(AtyEmpty.KEY_FRAG_ARG, b);
			startActivity(t);
			break;
		}

		default:
			handled = false;
		}
		return handled ? true : super.onOptionsItemSelected(item);
	}

	@Override
	protected void onAbsBuildActionBar() {
		buildActionBarSimple();
	}

	@Override
	public void onFragChanged(AbsFrag frag, String fragTag) {
	}

	@Override
	public void onBackStackChanged(int backCount) {
	}

	@Override
	public boolean onNetChanged(int netType, int preType) {
		boolean handled = false;
		AbsFrag f = null;
		if (mFragMger != null) {
			f = mFragMger.getCurrentFrag();
			if (f != null) {
				handled = f.onNetChanged(netType, preType);
			}
		}
		return handled;
	}

	@Override
	protected boolean needResumeNetNotify(int cur) {
		boolean need = super.needResumeNetNotify(cur);
		if ((GlobalApp.getApp().getFlag() & (NetToastUtils.GLOBAL_SCREEN_ON | NetToastUtils.GLOBAL_HOME_PRESS)) != 0) {
			return true;
		}
		return need;
	}
}
