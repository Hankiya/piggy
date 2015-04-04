package com.howbuy.aty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.howbuy.adp.FuncAdp;
import com.howbuy.adp.FuncAdp.FuncHolder;
import com.howbuy.adp.MenuAdp.MenuViewHolder;
import com.howbuy.control.FragTabHost;
import com.howbuy.control.FragTabHost.IFragTabChanged;
import com.howbuy.entity.Function;
import com.howbuy.lib.aty.AbsAty;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.libtest.R;

public class AtyMain extends AbsAty implements OnItemClickListener, IFragTabChanged {
	private FragTabHost mTabHost;
	private AutoCompleteTextView mAct = null;
	private GestureDetector gestureDetector;
	private boolean isPortrait = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main);
		setTwiceExitEnable(true);
		mAct = (AutoCompleteTextView) findViewById(R.id.act_search);
		mTabHost = (FragTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setTabChangedListener(this);
		mTabHost.setup(getSupportFragmentManager(), false);
		addLibTabs(R.array.sdk_infs);
		mTabHost.setCurrentTab(0);
		gestureDetector = new GestureDetector(new TabHostTouch());
		isPortrait = getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("frag_host_select", mTabHost.getCurrentTab());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mTabHost.setCurrentTab(savedInstanceState.getInt("frag_host_select", 0));
	}

	private void addLibTabs(int arrayResId) {
		String[] tabs = getResources().getStringArray(arrayResId);
		for (int i = 0; i < tabs.length; i++) {
			String[] ss = tabs[i].split("-");// ss[0]是标题。
			Bundle bundle = new Bundle();
			bundle.putString("filename", ss[1]);
			Fragment f = Fragment.instantiate(this, ss[2], bundle);
			mTabHost.addTab(f, ss[2] + "_" + ss[0]);
		}
	}

	public AutoCompleteTextView getAutoTextView() {
		return mAct;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		Intent tent = new Intent(this, AtySecret.class);
		menu.add("SecretWind")
				.setIcon(R.drawable.abs__ic_menu_moreoverflow_holo_dark)
				.setIntent(tent)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
		// getSupportMenuInflater().inflate(R.menu.aty_main, menu);
	}

	@Override
	protected void onAbsBuildActionBar() {
		buildActionBarSimple();
	}

	public void onXmlBtClick(View v) {
		AbsFrag frag = (AbsFrag) mTabHost.getCurrentFragment();
		boolean handled = frag != null && frag.onXmlBtClick(v);
		if (!handled) {
			if (v.getId() == R.id.bt_params) {
				((FuncHolder) v.getTag()).changeView(0);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		Object obj = v.getTag();
		if (obj instanceof MenuViewHolder) {
			MenuViewHolder hod = (MenuViewHolder) obj;
			hod.changeView(0);
			if (hod.mItem.getIntent() != null) {
				startActivity(hod.mItem.getIntent());
				LogUtils.setToastView(null);
			} else {
				pop("index=" + arg2 + "  title=" + hod.mItem.getTitle(), true);
			}
		} else if (obj instanceof FuncHolder) {
			FuncHolder hod = (FuncHolder) obj;
			Function func = hod.mItem;
			Intent tent = new Intent(this, AtyEmpty.class);
			Bundle frag_arg = new Bundle();
			if (hod.mFuncType == FuncAdp.FUNC_LIB) {
				frag_arg.putParcelable("arg_function", func);
				frag_arg.putString("arg_params", "lib_params.xml");
			}
			tent.putExtra(AtyEmpty.KEY_FRAG_NAME, func.mTarget);
			tent.putExtra(AtyEmpty.KEY_FRAG_ARG, frag_arg);
			startActivity(tent);
		}
	}

	@Override
	protected boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		if (mTabHost != null) {
			AbsFrag frag = (AbsFrag) mTabHost.getCurrentFragment();
			if (frag != null && frag.onKeyBack(fromBar, isFirstPress, isTwiceInTime) && !fromBar) {
				resetTrace();
			}
		}
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	public boolean onTabChangedBefore(int cur, int next) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onTabChanged(int cur) {
		// popMsg("onTabChanged cur="+cur);
	}

	private void createClassFileFromAssets(String fileName) {
		String text = SysUtils.readFromAssets(this, "lib_illustrate.xml");
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("name\\s*=\\s*\"(\\w+)\"");
		Matcher matcher = pattern.matcher(text);
		sb.append("public class CommonLibTest {\r");
		boolean print = false;
		while (matcher.find()) {
			String funcName = matcher.group(1);
			if ("ParUpdateOpenFundBasicInfo".equals(funcName)) {
				print = true;
			}
			if (print) {
				sb.append("public ReqResult ").append(funcName);
				sb.append("(String key,IReqFinished calback){\r");
				sb.append("mParam=new ").append(funcName).append("(key, calback,0);\r");
				sb.append("return mParam.execute();\r");
				sb.append("}\r");
			}
		}
		sb.append('}');
		System.out.println(sb.toString());
		// adb logcat -v raw -s System.out:i >> f:classfile.java
	}

	private class TabHostTouch extends SimpleOnGestureListener {
		/** 滑动翻页所需距离 */
		private static final int ON_TOUCH_DISTANCE = 250;
		private static final int ON_TOUCH_SPEED = 1200;
		private int selectedTab = 0;

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			float dx = e1.getX() - e2.getX();
			if (Math.abs(dx) > Math.abs(e1.getY() - e2.getY())) {
				if (dx <= (-ON_TOUCH_DISTANCE) || velocityX > ON_TOUCH_SPEED) {
					selectedTab = mTabHost.getCurrentTab() - 1;
					if (selectedTab < 0) {// first tab can't be selected.
						return true;
					}
				} else if (dx >= ON_TOUCH_DISTANCE || velocityX < -ON_TOUCH_SPEED) {
					selectedTab = mTabHost.getCurrentTab() + 1;
					if (selectedTab >= mTabHost.getTabCount()) {
						return true;
					}
				} else {
					return true;
				}
				mTabHost.setCurrentTab(selectedTab);
				return true;
			}
			return false;

		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (isPortrait) {
			if (gestureDetector.onTouchEvent(event)) {
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.dispatchTouchEvent(event);
	}

	public void onConfigurationChanged(Configuration newConfig) {

		if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			isPortrait = true;
		} else {
			isPortrait = false;
		}
		{
			// if (newConfig.orientation !=
			// ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			// newConfig.orientation =
			// ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
			// }
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onNetChanged(int netType, int preType) {
		// TODO Auto-generated method stub
		return false;
	}
}
