package com.howbuy.frag;

import howbuy.android.palmfund.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.howbuy.config.ValConfig;
import com.howbuy.curve.CharData;
import com.howbuy.curve.CharValue;
import com.howbuy.curve.SimpleChartView;
import com.howbuy.entity.CharRequest;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.utils.CharProvider;
import com.howbuy.utils.CharProvider.ICharCacheChanged;

public class FragCharPort extends AbsHbFrag implements ICharCacheChanged {
	private int mCycle = 0;
	private FragDetailsFund mTarget = null;
	private View mProgress;
	private SimpleChartView mFundView;// 图表
	private ArrayList<CharData> mCharData = new ArrayList<CharData>();
	private CharRequest mCharOpt = null;
	private CharProvider mCharProvider = null;
	private TextView mTvDate1, mTvDate2;

	@Override
	public void onResume() {
		super.onResume();
		mCharProvider.registerCharCacheChanged(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mCharProvider.unregisterCharCacheChanged(this);
	}

	private void applyCharData() {
		showProgress(false);
		mFundView.setData(mCharData);
		if (mCharData.size() > 1) {
			Calendar left = Calendar.getInstance();
			Calendar right = Calendar.getInstance();
			left.setTimeInMillis(mCharData.get(mCharData.size() - 1).getTime());
			right.setTimeInMillis(mCharData.get(0).getTime());
			if (left.get(Calendar.YEAR) == right.get(Calendar.YEAR)) {
				mTvDate1.setText(StrUtils.timeFormat(left.getTimeInMillis(), "M-dd"));
				mTvDate2.setText(StrUtils.timeFormat(right.getTimeInMillis(), "M-dd"));
			} else {
				mTvDate1.setText(StrUtils.timeFormat(left.getTimeInMillis(), "yyyy-M-dd"));
				mTvDate2.setText(StrUtils.timeFormat(right.getTimeInMillis(), "yyyy-M-dd"));
			}
		}
	}

	private void parseBundle(Bundle bundle) {
		if (bundle != null) {
			mCycle = bundle.getInt(ValConfig.IT_TYPE);
			d("parseBundle", "getTarget=" + getTargetFragment() + " parent frag="
					+ getParentFragment());
			android.support.v4.app.Fragment fgg = getTargetFragment();
			if (!(fgg instanceof FragDetailsFund)) {
				fgg = getParentFragment();
			}
			if (fgg instanceof FragDetailsFund) {
				mTarget = (FragDetailsFund) getTargetFragment();
			} else {
				if (getSherlockActivity() != null) {
					getSherlockActivity().finish();
				} else {
					return;
				}
			}
			mCharProvider = mTarget.getPageChar().getCharProvider();
			d("parseBundle", "mCycle=" + CharProvider.parseCycle(mCycle)
					+ " char has first qury db=" + mCharProvider.hasFirstQureyDb() + " jj size="
					+ mCharProvider.getOperatorList(CharValue.TYPE_JJJZ).size());
			refush(false);
		}
	}

	private void refush(boolean fromErrRefush) {
		if (mCharData.size() == 0) {
			showProgress(true);
		}
		if (mCharProvider.hasFirstQureyDb()) {
			List<CharValue> cache = mCharProvider.request(getRequest());
			d("refush", "cycle=" + CharProvider.parseCycle(mCycle) + " , mCharOpt=" + mCharOpt
					+ " cache size=" + (cache == null ? 0 : cache.size()));
			if (cache != null && cache.size() > 0) {
				CharData.initCharData(mCharData, cache, mCharProvider.getType().isHuobi(),
						mCharProvider.getBeanDanWei());
				applyCharData();
			}
		}

	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		if (bundle != null) {
			mCycle = bundle.getInt(ValConfig.IT_TYPE);
		}
		mFundView = (SimpleChartView) root.findViewById(R.id.chartview);
		mProgress = root.findViewById(R.id.pb_char);
		mTvDate1 = (TextView) root.findViewById(R.id.tv_date1);
		mTvDate2 = (TextView) root.findViewById(R.id.tv_date2);
		mTvDate1.setText(null);
		mTvDate2.setText(null);
		parseBundle(getArguments());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ValConfig.IT_TYPE, mCycle);
	}

	void showProgress(boolean show) {
		if (show) {
			if (mProgress.getVisibility() != View.VISIBLE) {
				mProgress.setVisibility(View.VISIBLE);
			}
		} else {
			if (mProgress.getVisibility() == View.VISIBLE) {
				mProgress.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected int getFragLayoutId() {
		return R.layout.com_page_details_char_port;
	}

	private CharRequest getRequest() {
		if (mCharOpt == null) {
			mCharOpt = CharRequest.getRequest(mCycle, mCharProvider);
		}
		return mCharOpt;
	}

	@Override
	public void onCharCacheChanged(CharProvider charProvider, int from, ArrayList<CharValue> r,
			int cycleType) {
		if (isAdded()) {
			if (from == FIRST_FROM_NET) {
				mCharOpt = null;
			}
			List<CharValue> cache = mCharProvider.request(getRequest());
			d("onCharCacheChanged",
					" from=" + CharProvider.parseFrom(from) + ",from cycleType="
							+ CharProvider.parseCycle(cycleType) + ",r.size=" + r.size()
							+ " cur_cycle=" + CharProvider.parseCycle(mCycle) + " cache size="
							+ (cache == null ? 0 : cache.size()) + " new Date="
							+ mCharProvider.getNewDate());
			if (cache != null && cache.size() > 0) {
				CharData.initCharData(mCharData, cache, mCharProvider.getType().isHuobi(),
						mCharProvider.getBeanDanWei());
				applyCharData();
				showProgress(false);
			}
		}
	}

	@Override
	public void onCharCacheErr(CharProvider charProvider, int from, ReqResult<CharRequest> r,
			int cycleType) {
		if (isAdded()) {
			d("onCharCacheErr",
					"from=" + CharProvider.parseFrom(from) + ",from cycleType="
							+ CharProvider.parseCycle(cycleType) + ",r=" + r + " cur_cycle="
							+ CharProvider.parseCycle(mCycle));
			if (GlobalApp.getApp().getNetType() > 1 && isVisible()) {
				if (from == FROM_NET) {
				} else if (from == FIRST_FROM_NET) {
				}
			}
			showProgress(false);
		}

	}
}