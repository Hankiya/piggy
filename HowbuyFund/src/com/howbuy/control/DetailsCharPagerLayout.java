package com.howbuy.control;

import howbuy.android.palmfund.R;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.howbuy.adp.FragPageCharAdp;
import com.howbuy.curve.CharValue;
import com.howbuy.entity.CharCycleInf;
import com.howbuy.entity.CharRequest;
import com.howbuy.lib.frag.AbsHbFrag;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.utils.CharProvider;
import com.howbuy.utils.CharProvider.ICharCacheChanged;

public class DetailsCharPagerLayout extends FrameLayout implements ICharCacheChanged {
	private CanScrollViewPager mViewPage;
	private View mProgress;
	private PageFixedSlidingTabStrip mIndicator = null;
	private FragPageCharAdp mAdapter = null;
	private CharProvider mCharProvider;

	public DetailsCharPagerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mViewPage = (CanScrollViewPager) findViewById(R.id.vp_char);
		mProgress = findViewById(R.id.pb_char);
		mIndicator = (PageFixedSlidingTabStrip) findViewById(R.id.indicator);
		mIndicator.setTextSize((int) SysUtils.getDimension(getContext(),
				TypedValue.COMPLEX_UNIT_SP, 14));
		mIndicator.setTextColorId(R.drawable.xml_text_color_gray);
		mIndicator.setIndicatorHeight((int) SysUtils.getDimension(getContext(),
				TypedValue.COMPLEX_UNIT_SP, 2));
		mViewPage.setVisibility(View.INVISIBLE);
		showProgress(true);
	}

	public void setFragMger(AbsHbFrag frager, CharProvider p, boolean port) {
		mCharProvider = p;
		if (mCharProvider.hasFirstQuryNet()) {
			mViewPage.setVisibility(View.VISIBLE);
			showProgress(false);
		}
		mCharProvider.registerCharCacheChanged(this);
		if (mAdapter == null) {
			mAdapter = new FragPageCharAdp(frager, p.getCharCycleAdp(p.getType().isSimu()), port);
			mViewPage.setAdapter(mAdapter);
			if (!p.getType().isSimu()) {
				int current = mAdapter.getCharAdp().getEnablePos(3) == -1 ? 5 : 3;
				mIndicator.setViewPager(mViewPage, mAdapter.getCharAdp(), current);
				// mIndicator.setUnderlineColor(0xffff9500);
				mIndicator.setShowdivider(false);
				mIndicator.setUnderlineHeight(0);
			} else {
				removeView(mIndicator);
				FrameLayout.LayoutParams lp = (LayoutParams) mViewPage.getLayoutParams();
				if (lp != null) {
					lp.setMargins(0, 0, 0, 0);
				}
			}
		}
	}

	public CharProvider getCharProvider() {
		return mCharProvider;
	}

	public PageFixedSlidingTabStrip getIndicator() {
		return mIndicator;
	}

	public CanScrollViewPager getViewPage() {
		return mViewPage;
	}

	public CharCycleInf getCycleInf(int index) {
		if (mAdapter != null) {
			if (index == -1) {
				index = mViewPage.getCurrentItem();
			}
			return mAdapter.getCycleAdp().getItem(index, true);
		}
		return null;
	}

	public String getPageTitle(int index) {
		if (mAdapter != null) {
			if (index == -1) {
				index = mViewPage.getCurrentItem();
			}
			CharCycleInf cycleinf = mAdapter.getCycleAdp().getItem(index, true);
			if (cycleinf != null) {
				return cycleinf.mCycleName;
			}
		}
		return null;
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

	public void setPageChangeListener(OnPageChangeListener l) {
		mIndicator.setOnPageChangeListener(l);
	}

	@Override
	public void onCharCacheChanged(CharProvider charProvider, int from, ArrayList<CharValue> val,
			int cycleType) {
		if (from == ICharCacheChanged.FIRST_FROM_NET) {
			mViewPage.setVisibility(View.VISIBLE);
			int n = val == null ? 0 : val.size();
			if (n < 7) {
				if (!mCharProvider.getType().isSimu()) {
					mAdapter.getCharAdp().getItem(0).mEnabled = false;
				}
				if (n == 0) {
					mViewPage.setVisibility(View.INVISIBLE);
				}
				mAdapter.getCharAdp().notifyDataSetChanged();
				mAdapter.notifyDataSetChanged();
				mIndicator.notifyDataSetChanged();
			}
			showProgress(false);
		}
	}

	@Override
	public void onCharCacheErr(CharProvider charProvider, int from, ReqResult<CharRequest> r,
			int cycleType) {
		if (from == ICharCacheChanged.FIRST_FROM_NET) {
			mViewPage.setVisibility(View.VISIBLE);
			showProgress(false);
		}
	}

}
