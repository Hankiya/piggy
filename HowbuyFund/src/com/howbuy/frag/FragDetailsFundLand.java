package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.howbuy.component.ScreenHelper;
import com.howbuy.component.ScreenHelper.IScreenChanged;
import com.howbuy.config.Analytics;
import com.howbuy.config.ValConfig;
import com.howbuy.control.DetailsCharPagerLayout;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.utils.CharProvider;
import com.howbuy.utils.FundUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-6-30 下午8:20:11
 */
public class FragDetailsFundLand extends AbsHbFrag implements IScreenChanged, OnPageChangeListener {
	private DetailsCharPagerLayout mCharView = null;
	private ScreenHelper mSensorMgr = null;
	private CharProvider mCharProvider = null;
	private boolean mForceScreen = false;
	private TextView mTvDate, mTvValue, mTvIncrease;
	private int mCurrentSelect = -1;
	private Intent mResult = new Intent();
	private ArrayList<OnPageChangeListener> mPageListener = new ArrayList<OnPageChangeListener>();

	public void addPageListener(OnPageChangeListener l) {
		if (!mPageListener.contains(l)) {
			mPageListener.add(l);
		}
	}

	public void removePageListener(OnPageChangeListener l) {
		mPageListener.remove(l);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSherlockActivity().getSupportActionBar().hide();
		mSensorMgr = new ScreenHelper();
		mSensorMgr.registerSensor(getSherlockActivity(), this);
		getSherlockActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	public void onResume() {
		super.onResume();
		mSensorMgr.registerSensor(getSherlockActivity(), this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorMgr.unregisterSensor(this);
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.com_details_char_land;
	}

	public CharProvider getCharProvider() {
		return mCharProvider;
	}

	public DetailsCharPagerLayout getPageChar() {
		return mCharView;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mCurrentSelect = getArguments().getInt(ValConfig.IT_ID);
		mTvDate = (TextView) root.findViewById(R.id.tv_date);
		mTvValue = (TextView) root.findViewById(R.id.tv_value);
		mTvIncrease = (TextView) root.findViewById(R.id.tv_increase);
		mCharView = (DetailsCharPagerLayout) root.findViewById(R.id.lay_char);
		mCharView.setPageChangeListener(this);
		mCharProvider = (CharProvider) GlobalApp.getApp().getMapObj(CharProvider.TAG);
		if (mCharProvider == null) {
			getSherlockActivity().finish();
		} else {
			mCharView.setFragMger(this, mCharProvider, false);
		}
		if (!mCharProvider.getType().isSimu()) {
			if (mCurrentSelect != -1) {
				mCharView.getViewPage().setCurrentItem(mCurrentSelect, false);
			}
		}
		setBarText(null, null, null);
	}

	@Override
	public void onScreenChanged(int preScreen, int curScreen) {
		if (curScreen == IScreenChanged.SENSOR_SCREEN_PORT) {
			if (curScreen == preScreen) {
				mForceScreen = true;
			}
			if (!mForceScreen && getSherlockActivity() != null) {
				mResult.putExtra(ValConfig.IT_TYPE, false);
				getSherlockActivity().setResult(mCurrentSelect, mResult);
				getSherlockActivity().finish();
			}
		} else {
			if (curScreen == IScreenChanged.SENSOR_SCREEN_LAND) {
				mForceScreen = false;
			} else {
				if (curScreen == preScreen) {
					mForceScreen = true;
				}
			}
		}
	}

	public void setBarText(String date, String value, String increase) {
		if (date == null && value == null && increase == null) {
			StringBuffer sb = new StringBuffer(32);
			String cycleName = mCharView.getPageTitle(-1);
			if (cycleName != null) {
				sb.append(cycleName);
			}
			sb.append(mCharProvider.getType().isHuobi() ? "年化收益走势图" : "净值走势图");
			mTvDate.setText(sb.toString());
			ViewUtils.setVisibility(mTvValue, View.GONE);
			ViewUtils.setVisibility(mTvIncrease, View.GONE);
		} else {
			if (date != null && value != null && increase != null) {
				boolean simu = mCharProvider.getType().isSimu();
				ViewUtils.setVisibility(mTvValue, View.VISIBLE);
				ViewUtils.setVisibility(mTvIncrease, View.VISIBLE);
				mTvDate.setText(date);
				FundUtils.formatFundValue(mTvValue, value, mCharProvider.getBean().getDanWei(),
						simu, FundUtils.VALUE_JJJZ);
				FundUtils.formatFundValue(mTvIncrease, increase, null, simu, FundUtils.VALUE_HBDR);
			} else {
				ViewUtils.setVisibility(mTvValue, View.GONE);
				ViewUtils.setVisibility(mTvIncrease, View.VISIBLE);
				mTvDate.setText(date);
				FundUtils.formatFundValue(mTvIncrease, increase, null, false, FundUtils.VALUE_HBDR);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		for (int i = 0; i < mPageListener.size(); i++) {
			mPageListener.get(i).onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		for (int i = 0; i < mPageListener.size(); i++) {
			mPageListener.get(i).onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int cur) {
		mCurrentSelect = cur;
		setBarText(null, null, null);
		for (int i = 0; i < mPageListener.size(); i++) {
			mPageListener.get(i).onPageSelected(cur);
		}
		Analytics.onEvent(getSherlockActivity(), Analytics.VIEW_HORIZONTAL_CHART_VIEW,
				Analytics.KEY_PART, mCharView.getPageTitle(-1));
	}

	@Override
	public boolean onXmlBtClick(View v) {
		if (v.getId() == R.id.ib_back) {
			mResult.putExtra(ValConfig.IT_TYPE, true);
			getSherlockActivity().setResult(mCurrentSelect, mResult);
			getSherlockActivity().finish();
			return true;
		}
		return super.onXmlBtClick(v);
	}

	@Override
	public boolean onKeyBack(boolean fromBar, boolean isFirstPress, boolean isTwiceInTime) {
		mResult.putExtra(ValConfig.IT_TYPE, true);
		getSherlockActivity().setResult(mCurrentSelect, mResult);
		return super.onKeyBack(fromBar, isFirstPress, isTwiceInTime);
	}

	@Override
	public boolean onNetChanged(int netType, int preNet) {
		if (isVisible()) {
			if (netType <= 1 && preNet > 1) {
				//pop("网络不可用", false);
			} else {
				if (netType > 1 && preNet <= 1) {
					mCharProvider.firstQueryNet();
				}
			}
			return true;
		}
		return super.onNetChanged(netType, preNet);
	}
}
