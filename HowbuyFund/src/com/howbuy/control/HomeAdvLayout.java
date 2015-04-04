package com.howbuy.control;

import howbuy.android.palmfund.R;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.howbuy.adp.FragAdvPageAdp;
import com.howbuy.component.FixedSpeedScroller;
import com.howbuy.datalib.fund.ParAdvertisementList;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.GlobalServiceMger;
import com.howbuy.lib.compont.GlobalServiceMger.ScheduleTask;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.interfaces.ITimerListener;
import com.howbuy.lib.net.CacheOpt;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;

public class HomeAdvLayout extends FrameLayout implements IReqNetFinished,
		OnPageChangeListener, ITimerListener {
	ViewPager mViewPage;
	Scroller oladScroller = null;
	FixedSpeedScroller mScrollerFixed = null;
	ParAdvertisementList mPar = null;
	AdvertList mList = null;
	FragAdvPageAdp mAdp = null;
	FragmentManager mFragMger = null;
	private int mCurrentItem = -1, mDuration = 0;
	private boolean hasStopAuto = true;

	public HomeAdvLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPar = new ParAdvertisementList(1, this, CacheOpt.TIME_WEEK);
		int w = SysUtils.getDisplay(context)[0];
		if (w <= 480) {
			mPar.setParams(480, 200);
		} else if (w <= 800) {
			mPar.setParams(720, 300);
		} else {
			mPar.setParams(1080, 450);
		}
		mPar.getReqOpt(false).addFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mViewPage = (ViewPager) findViewById(R.id.pageadv_viewpage);
		// mIndicator = (SimpleIndicator) findViewById(R.id.pageadv_indicator);
		// findViewById(R.id.pageadv_indicator).setVisibility(View.GONE);
		mViewPage.setOnPageChangeListener(this);
		mViewPage.setOffscreenPageLimit(3);
		mViewPage.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
				} else {
					if (!hasStopAuto) {
						toggleTimer(false);
					}
				}
				return false;
			}
		});
		setScrollerDuration(800);
		startRequest(false);
		ViewGroup.LayoutParams lp = getLayoutParams();
		if (lp != null) {
			lp = new ViewGroup.LayoutParams(-1, -2);
		}
		lp.height = (int) (getContext().getResources().getDisplayMetrics().widthPixels / 2.4f);
		setLayoutParams(lp);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mPar.cancle();
	}

	public void refush(boolean force) {
		startRequest(force);
	}

	public void cancleRequest() {
		mPar.cancle();
	}

	public void setFragMger(FragmentManager frager) {
		mFragMger = frager;
	}

	private void startRequest(boolean forceRefush) {
		if (forceRefush) {
			mPar.getReqOpt(false).addFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
		} else {
			mPar.getReqOpt(false).subFlag(ReqNetOpt.FLAG_FORCE_UNCACHE);
		}
		mPar.execute();
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		if (result.isSuccess()) {
			mList = (AdvertList) result.mData;
			if (mAdp == null) {
				mAdp = new FragAdvPageAdp(mFragMger, mList);
				mViewPage.setAdapter(mAdp);
			}
			mAdp.setData(mList);
			mAdp.notifyDataSetChanged();
			int n = mList == null ? 0 : mList.getIcAdvertsCount();
			if (n > 0) {
				if (mCurrentItem == -1) {
					mCurrentItem = mAdp.CYCLE * n / 2;
				}
				mViewPage.setCurrentItem(mCurrentItem, false);
				// mIndicator.setCurGrid(mCurrentItem % n, n);
				if (!result.isResultFromCache()) {
					mPar.getReqOpt(false).subFlag(ReqNetOpt.FLAG_CACHE_THEN_REQ);
				}
			}
		} else {
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (arg0 == 0) {
			if (hasStopAuto) {
				toggleTimer(true);
			}
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (positionOffset == 0) {
			adjustPageCurrent();
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		mCurrentItem = arg0;
		// adjustPageCurrent();
	}

	private void adjustPageCurrent() {
		int n = mList == null ? 0 : mList.getIcAdvertsCount();
		// mIndicator.setCurGrid(arg0 % n, n);
		if (n > 0) {
			if (mCurrentItem == 0) {
				mCurrentItem = mAdp.CYCLE * n / 2;
			} else if (mCurrentItem == mAdp.getCount() - 1) {
				mCurrentItem = mAdp.getCount() - n - 1;
				mCurrentItem = mAdp.CYCLE * n / 2 - 1;
			}
			mViewPage.setCurrentItem(mCurrentItem, false);
		}
	}

	public int getCurrentItem(boolean real) {
		if (real) {
			int n = mList == null ? 0 : mList.getIcAdvertsCount();
			if (n > 0) {
				return mCurrentItem % n;
			}
			return -1;
		}
		return mCurrentItem;
	}

	public void setCurrentItem(int index, boolean real, boolean anim) {
		if (real) {
			int n = mList == null ? 0 : mList.getIcAdvertsCount();
			mCurrentItem = n + index;
		} else {
			mCurrentItem = index;
		}
		if (mAdp != null) {
			mViewPage.setCurrentItem(mCurrentItem, anim);
		}
	}

	protected void toggleTimer(boolean auto) {
		if (mDuration > 0) {
			hasStopAuto = !auto;
			if (auto) {
				toggleTimer(mDuration);
				setScrollerDuration(800);
			} else {
				GlobalApp.getApp().getGlobalServiceMger().removeTimerListener(1, this);
				setScrollerDuration(FixedSpeedScroller.defaultDuration);
			}
		}
	}

	public void toggleTimer(int duration) {
		GlobalServiceMger mger = GlobalApp.getApp().getGlobalServiceMger();
		mDuration = duration;
		if (duration > 0) {
			hasStopAuto = false;
			ScheduleTask task = new ScheduleTask(1, this);
			task.setExecuteArg(duration, 0, false);
			mger.addTimerListener(task);
		} else {
			hasStopAuto = true;
			mger.removeTimerListener(1, this);
		}
	}

	@Override
	public void onTimerRun(int which, int timerState, boolean hasTask, int timesOrSize) {
		int realN = mList == null ? 0 : mList.getIcAdvertsCount();
		if (timerState == ITimerListener.TIMER_SCHEDULE && realN > 0) {
			int cur = getCurrentItem(false);
			cur = cur + 1;
			setCurrentItem(cur, false, true);
		}
	}

	public void setScrollerDuration(int duration) {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			Scroller currentScroller = (Scroller) mField.get(mViewPage);
			if (oladScroller == null) {
				oladScroller = currentScroller;
			}
			if (duration == FixedSpeedScroller.defaultDuration) {
				mField.set(mViewPage, oladScroller);
			} else {
				if (currentScroller instanceof FixedSpeedScroller) {
					mScrollerFixed = (FixedSpeedScroller) currentScroller;
				} else {
					mScrollerFixed = new FixedSpeedScroller(getContext(),
							new AccelerateDecelerateInterpolator());
					mField.set(mViewPage, mScrollerFixed);
				}
				mScrollerFixed.setFixedDuration(duration);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
