package com.howbuy.frag;

import howbuy.android.palmfund.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.howbuy.aty.AtySecret;
import com.howbuy.component.AppFrame;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.control.VerticalScrollView;
import com.howbuy.control.VerticalScrollView.OnScrollChangedListener;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.SysUtils;

@SuppressLint("SetJavaScriptEnabled")
public class FragSetAbout extends AbsHbFrag implements OnScrollChangedListener {
	private long mLastClickTime = 0;
	private int mClickTimes = 0;
	private final int mRequireClickTime = 5;
	private int mClickDuration = 300;
	private VerticalScrollView mScroll = null;
	private View mLayCall = null;

	private void parseArgments(Bundle arg) {
		if (arg != null) {
			if (mTitleLable == null) {
				mTitleLable = arg.getString(ValConfig.IT_NAME);
				if (mTitleLable != null) {
					getSherlockActivity().getSupportActionBar().setTitle(mTitleLable);
				}
			}
		}
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		parseArgments(getArguments());
		mScroll = (VerticalScrollView) root;
		mScroll.setOnScrollChangedListener(this);
		mLayCall = mRootView.findViewById(R.id.lay_server);
		mLayCall.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				startActivity(new Intent(getSherlockActivity(), AtySecret.class));
				return true;
			}
		});
	}

	/*
	 * private void parseActiveAty() { StringBuffer sb = new StringBuffer(256);
	 * ArrayList<AtyInfs> list = AtyInfs.parseAtys(AbsAty.getAtys()); AtyInfs
	 * aty = null; for (int i = 0; i < list.size(); i++) { aty = list.get(i);
	 * sb.append(aty.toString()).append("\n\t");
	 * sb.append("---------------------------\n\t"); } d("parseActiveAty",
	 * sb.toString()); }
	 */

	@Override
	public boolean onXmlBtClick(View v) {
		boolean handed = true;
		Intent t = null;
		switch (v.getId()) {
		case R.id.lay_server:
			Analytics.onEvent(getSherlockActivity(), Analytics.CALL_400, Analytics.KEY_FROM,
					mTitleLable);
			t = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4007009665"));
			break;
		case R.id.lay_rate:
			String pk = getSherlockActivity().getPackageName();
			t = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pk));
			if (SysUtils.intentSafe(getSherlockActivity(), t)) {
				String app_rate = AppFrame.getApp().getsF()
						.getString(ValConfig.SF_RATE_APP_LEVEL, null);
				if (app_rate == null) {
					app_rate = pk;
					pk = null;
				} else {
					if (!app_rate.contains(pk)) {
						app_rate = app_rate + "#" + pk;
						pk = null;
					}
				}
				if (pk == null) {// 记录评分已经调用过.
					AppFrame.getApp().getsF().edit()
							.putString(ValConfig.SF_RATE_APP_LEVEL, app_rate).commit();
				}
			} else {
				t = new Intent(Intent.ACTION_VIEW, Uri.parse(ValConfig.URL_APP_FUND));
			}
			break;
		case R.id.tv_debug:
			t = tryCallDebutOption(System.currentTimeMillis(), true);
			break;
		default:
			handed = false;
			break;
		}
		if (t != null) {
			startActivity(t);
		}
		return handed ? true : super.onXmlBtClick(v);
	}

	private Intent tryCallDebutOption(long cur, boolean byClick) {
		if (mLastClickTime == 0) {
			mLastClickTime = cur;
			mClickTimes = 1;
		} else {
			int duration = byClick ? mClickDuration : (mClickDuration + mClickDuration);
			if (cur - mLastClickTime < duration) {
				mLastClickTime = cur;
				mClickTimes++;
				d("tryCallDebutOption", "mClickTimes=" + mClickTimes + " in " + mRequireClickTime);
				duration = byClick ? mRequireClickTime
						: (mRequireClickTime + mRequireClickTime / 2);
				if (mClickTimes >= duration) {
					mLastClickTime = 0;
					mClickTimes = 0;
					return new Intent(getSherlockActivity(), AtySecret.class);
				}
			} else {
				mLastClickTime = 0;
				mClickTimes = 0;
			}
		}
		return null;
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_set_about;
	}

	@Override
	public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
	}

	@Override
	public void onScrollIdle() {
		long cur = System.currentTimeMillis();
		if (mScroll.getScrollY() <= 0) {
			Intent t = tryCallDebutOption(cur, false);
			if (t != null) {
				startActivity(t);
			}
		} else {
			mLastClickTime = 1;
		}
	}

}
