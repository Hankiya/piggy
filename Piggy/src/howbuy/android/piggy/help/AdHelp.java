package howbuy.android.piggy.help;

import howbuy.android.piggy.R;
import howbuy.android.piggy.ui.fragment.AdPageFragment;
import howbuy.android.piggy.widget.FixedSpeedScroller;
import howbuy.android.piggy.widget.PageControlView;
import howbuy.android.util.Cons;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.AdvertList;
import com.howbuy.wireless.entity.protobuf.AdvertListProtos.ICAdvert;

public class AdHelp {
	public static final long delayAdAnimTime = 10 * 1000;
	public static final String NAME = "AdHelp";
	private RelativeLayout mAdLay;
	private ViewPager mAdPage;
	private ProgressBar mAdLodingPb;
	private PageControlView mIndicator;
	private Activity mContext;
	private FixedSpeedScroller mCurrScroller;
	private Scroller mDefaultScroller;
	private Timer mTimer;
	protected int mPageCurrItem = 100;
	private MyPageChangeListener mPageChangeListener;

	private Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			Log.d(NAME, "handleMessage=" + Thread.currentThread().getId());
			int pistion = (Integer) msg.obj;
			Log.d(NAME, "handleMessagemdata=" + pistion);
			if (pistion != mAdPage.getCurrentItem()) {
				mAdPage.setCurrentItem(pistion);
			}
		};
	};

	public AdHelp(Activity mContext, ViewGroup v) {
		super();
		this.mContext = mContext;
		findAllView(v);
	}

	public void findAllView(ViewGroup v) {
		mAdLay = (RelativeLayout) v.findViewById(R.id.lay_ad);
		mAdPage = (ViewPager) v.findViewById(R.id.ad_lay_pager);
		mAdLodingPb = (ProgressBar) v.findViewById(R.id.ad_lay_press);
		mIndicator = (PageControlView) v.findViewById(R.id.ad_lay_indeic);
	}

	public void setAdView(AdvertList advertList) {
		Log.d(NAME, "setAdView");
		mAdLay.setVisibility(View.VISIBLE);
		mAdLodingPb.setVisibility(View.GONE);
		mAdPage.removeAllViews();
		mPageChangeListener = new MyPageChangeListener(mIndicator, advertList.getIcAdvertsCount());
		mIndicator.setCount(advertList.getIcAdvertsCount());
		MyPageAdapter myPageAdapter = new MyPageAdapter(((SherlockFragmentActivity) mContext).getSupportFragmentManager(), advertList);
		mAdPage.setAdapter(myPageAdapter);
		//
		mAdPage.setPageMargin(2);
		mAdPage.setOnPageChangeListener(mPageChangeListener);
		mAdPage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// 如果用户收到滑动 则取消定时器
				if (mTimer != null && event.getAction() == MotionEvent.ACTION_MOVE) {
					setViewPageAnimiation(delayAdAnimTime, FixedSpeedScroller.defaultDuration);
					mTimer.cancel();
					mTimer = null;
				}
				return false;
			}
		});
		mTimer = new Timer();
		setViewPageAnimiation(delayAdAnimTime, FixedSpeedScroller.customDuration);
		mAdPage.setCurrentItem(100, false);
	}

	/**
	 * 设置动画切换时间
	 * 
	 * @param delay
	 *            多久之后自动切换图片
	 * @param amDuration
	 *            图片切换时间
	 */
	void setViewPageAnimiation(long delay, int amDuration) {
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mPageCurrItem++;
				if (mPageCurrItem == 199) {
					mPageCurrItem = 100;
				}
				mHandler.obtainMessage(Cons.SHOW_ReFresh, mPageCurrItem).sendToTarget();
			}
		}, delay, 1000 * 4);
		setViewPageScrollerType(amDuration);
	}

	/**
	 * 设置动画的Type
	 * 
	 * @see
	 * @param amDuration
	 */
	void setViewPageScrollerType(int amDuration) {
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			if (mDefaultScroller == null) {
				mDefaultScroller = (Scroller) mField.get(mAdPage);
			}
			if (amDuration == FixedSpeedScroller.defaultDuration) {
				mField.set(mAdPage, mDefaultScroller);
				Log.d(NAME, "mDefaultScroller.getDuration()=" + mDefaultScroller.getDuration());
			} else {
				mCurrScroller = new FixedSpeedScroller(mContext, new AccelerateInterpolator());
				mCurrScroller.setmDuration(amDuration);
				mField.set(mAdPage, mCurrScroller);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MyPageAdapter extends FragmentStatePagerAdapter {
		AdvertList mAdvertList;
		FragmentManager fm;

		public MyPageAdapter(FragmentManager fm, AdvertList mAdvertList) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.mAdvertList = mAdvertList;
			this.fm = fm;
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-gene00rated method stub
			int truePostion = getTruePostion(position);
			Fragment fragment = null;
			ICAdvert icAdvert = mAdvertList.getIcAdvertsList().get(truePostion);
			Bundle bundle = new Bundle();
			bundle.putString("advID", icAdvert.getAdvID());
			bundle.putString("advTitle", icAdvert.getAdvTitle());
			bundle.putString("advImageUrl", icAdvert.getAdvImageUrl());
			bundle.putString("advEventCode", icAdvert.getAdvEventCode());
			fragment = Fragment.instantiate(mContext, AdPageFragment.class.getName(), bundle);
			return fragment;
		}

		protected int getTruePostion(int position) {
			int truePostion = position % mAdvertList.getIcAdvertsCount();
			return truePostion;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 200;
		}
	}

	public interface ScrollToCallback {
		public void callback(int currentIndex);
	}

	public class MyPageChangeListener implements OnPageChangeListener {
		private ScrollToCallback mScrollToCallback;
		private int mPostion = 0;
		private int truePageSum;

		public MyPageChangeListener(ScrollToCallback MScrollToCallback, int truePageSum) {
			this.mScrollToCallback = MScrollToCallback;
			this.truePageSum = truePageSum;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if (mPostion != position) {
				mScrollToCallback.callback(position % truePageSum);
				mPostion = position;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}

	}

	public void noneAd() {
		// TODO Auto-generated method stub
		mAdLay.setVisibility(View.GONE);
		Log.d(NAME, "noneAd");
	}

	public void distroyAd() {
		Log.d(NAME, "distroyAd");
		if (mTimer != null) {
			mTimer.cancel();
		}
	}

	public void startAd() {
		Log.d(NAME, "startAd");
		setViewPageAnimiation(delayAdAnimTime, FixedSpeedScroller.customDuration);
	}

	public boolean hasAd() {
		mAdLay.setVisibility(View.GONE);
		return mAdLay.getVisibility() == View.GONE ? false : true;
	}
}
