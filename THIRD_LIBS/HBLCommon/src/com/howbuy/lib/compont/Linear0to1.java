package com.howbuy.lib.compont;

import java.util.ArrayList;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.howbuy.lib.interfaces.IAnimChaged;

/**
 * 提供动画。从0到1的变化，并把变化过程传到调用它接口。
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-12-11 上午9:00:59
 */
public class Linear0to1 {
	private static final int FRAME_TIME_DEF = 45;
	private int mFramePeriod = FRAME_TIME_DEF;
	private ArrayList<IAnimChaged> mCallist = null;
	private Handler mHandSchedule = null;
	private int mDuration = 1500, mWhich = 0;
	private boolean mRunning, mRunState = true, mScheduleHandlerOuter = false, mReversal = false;
	private long mBase;
	private float mValue;
	private Interpolator mPolater = null;
	private static Interpolator DEF_INTERPOLATOR = /*
													 * new Interpolator() {
													 * 
													 * @Override public float
													 * getInterpolation(float t)
													 * { return (float) (0.5f +
													 * Math.cos(Math.PI * (1 +
													 * t)) / 2); } };
													 */new AccelerateDecelerateInterpolator();

	final public float getInterpolationValue(float t) {
		if (mPolater == null) {
			return DEF_INTERPOLATOR.getInterpolation(t);
		} else {
			return mPolater.getInterpolation(t);
		}
	}

	final public Interpolator getInterpolation() {
		if (mPolater == null) {
			return DEF_INTERPOLATOR;
		} else {
			return mPolater;
		}
	}

	final public Handler getHandler() {
		if (mHandSchedule == null) {
			HandlerThread trd = new HandlerThread("Trd_Linear");
			trd.start();
			mHandSchedule = new Handler(trd.getLooper());
			mScheduleHandlerOuter = false;
		}
		return mHandSchedule;
	}

	public int getDuration() {
		return mDuration;
	}

	public int getPeriod() {
		return mFramePeriod;
	}

	public Linear0to1(Handler hadler, IAnimChaged callback) {
		this(hadler, null, callback);
	}

	public Linear0to1(Handler hadler, Interpolator polater, IAnimChaged callback) {
		mCallist = new ArrayList<IAnimChaged>();
		mValue = getInterpolationValue(mReversal ? 1f : 0f);
		mHandSchedule = hadler;
		mPolater = polater;
		if (mHandSchedule != null) {
			mScheduleHandlerOuter = true;
		}
		if (null != callback) {
			mCallist.add(callback);
		}
	}

	public boolean isRunning() {
		return mRunning;
	}

	public boolean setInterpolator(Interpolator polator) {
		if (mRunning == false) {
			mPolater = polator;
		}
		return !mRunning;
	}

	public boolean setPeriod(int period) {
		if (!mRunning) {
			if (period > 0) {
				mFramePeriod = period;
				return true;
			}
		}
		return false;
	}

	public void addCallBack(IAnimChaged calBack) {
		mCallist.add(calBack);
	}

	public void removeCallBack(IAnimChaged calBack) {
		mCallist.remove(calBack);
	}

	public boolean start(int which, int duration, boolean reversal) {
		if (mRunning) {
			return false;
		} else {
			mReversal = reversal;
			mDuration = Math.max(duration, mFramePeriod);
			mWhich = which;
			notifyStarted(mWhich);
			getHandler().postAtTime(mTick, mBase = SystemClock.uptimeMillis());
			return true;
		}
	}

	public boolean start(int which, int duration, int period, boolean reversal) {
		if (!mRunning) {
			if (period > 0 && period < duration) {
				mFramePeriod = period;
			} else {
				mFramePeriod = Math.min(40, Math.max((int) Math.sqrt(duration * 1.2f), 8));
			}
		}
		return start(which, duration, reversal);
	}

	public boolean stop() {
		if (mRunning) {
			mRunState = false;
			getHandler().removeCallbacks(mTick);
			notifyFinished(mWhich, mValue);
			return !mRunning;
		}
		return true;
	}

	public void destory() {
		stop();
		mPolater = null;
		if (mHandSchedule != null) {
			mHandSchedule.removeCallbacks(mTick);
			if (!mScheduleHandlerOuter) {
				mHandSchedule.getLooper().quit();
			}
			mHandSchedule = null;
		}

	}

	private Runnable mTick = new Runnable() {
		public void run() {
			getHandler().removeCallbacks(this);
			long now = SystemClock.uptimeMillis();
			long diff = now - mBase;
			float t = Math.max(Math.min(1, diff / (float) mDuration), 0);
			float old = mValue;
			mValue = getInterpolationValue(mReversal ? 1 - t : t);
			long next = now + mFramePeriod;
			notifyValueChanged(mWhich, mValue, mValue - old);
			if (diff < mDuration) {
				if (mRunState) {
					getHandler().postAtTime(this, next);
				}
			} else {
				notifyFinished(mWhich, mValue);
			}
		}
	};

	private void notifyStarted(int which) {
		mRunState = true;
		mRunning = true;
		mValue = getInterpolationValue(mReversal ? 1 : 0);
		for (int i = 0; i < mCallist.size(); i++) {
			mCallist.get(i).onAnimChaged(null, IAnimChaged.TYPE_START, which, mValue, 0);
		}
	}

	private void notifyValueChanged(int which, float val, float dVal) {
		for (int i = 0; i < mCallist.size(); i++) {
			mCallist.get(i).onAnimChaged(null, IAnimChaged.TYPE_CHANGED, which, val, dVal);
		}
	}

	private void notifyFinished(int which, float val) {
		mRunning = false;
		for (int i = 0; i < mCallist.size(); i++) {
			mCallist.get(i).onAnimChaged(null, IAnimChaged.TYPE_END, which, val, 0);
		}
	}
}
