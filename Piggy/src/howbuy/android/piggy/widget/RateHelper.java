package howbuy.android.piggy.widget;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;


public class RateHelper {
	private Interpolator mPolater = null;
	private ArrayList<IAnimChaged> mCallist = null;
	private Handler mHandSchedule = null;
	private Timer mTime = null;
	private float mValue, mPreValue;
	private boolean mReversal = false;
	private int mWhich = 0;
	private int mScheduleCounter = 0;
	private int mScheduleDest = 0;

	private static Interpolator DEF_INTERPOLATOR = /*
													 * new Interpolator() {
													 * 
													 * @Override public float
													 * getInterpolation(float t)
													 * { return (float) (0.5f +
													 * Math.cos(Math.PI * (1 +
													 * t)) / 2); } };
													 */new AccelerateDecelerateInterpolator();

	public RateHelper(Handler hadler, Interpolator polater, IAnimChaged callback) {
		mCallist = new ArrayList<IAnimChaged>();
		setInterpolation(polater);
		setHandler(hadler);
		addCallback(callback);
	}

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

	public boolean setInterpolation(Interpolator l) {
		if (mTime == null || mPolater == null) {
			mPolater = l;
			return true;
		}
		return false;

	}

	private void toggleTimer(boolean launchTimer, int delay, int period) {
		if (launchTimer) {
			if (mTime == null) {
				mScheduleCounter = -1;
				mTime = new Timer();
				mTime.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						if (++mScheduleCounter == 0) {
							mPreValue = mValue = getInterpolationValue(mReversal ? 1 : 0);
							mHandSchedule
									.post(new NotifyRunable(IAnimChaged.TYPE_START, mValue, 0));
						} else {
							float t = mScheduleCounter / (float) mScheduleDest, dVal;
							if (t <= 1) {
								t = getInterpolationValue(t);
								mValue = mReversal ? 1 - t : t;
								dVal = mValue - mPreValue;
								mPreValue = mValue;
								mHandSchedule.post(new NotifyRunable(IAnimChaged.TYPE_CHANGED,
										mValue, dVal));
							} else {
								t = getInterpolationValue(Math.min(1, t));
								mValue = mReversal ? 1 - t : t;
								toggleTimer(false, 0, 0);
							}
						}
					}
				}, delay, period);
			}
		} else {
			if (mTime != null) {
				mTime.cancel();
				mTime.purge();
				mTime = null;
				if (mScheduleCounter > 0) {
					mHandSchedule.post(new NotifyRunable(IAnimChaged.TYPE_END, mValue, mValue
							- mPreValue));
				}
				mScheduleDest = 0;
			}
		}
	}

	public void addCallback(IAnimChaged l) {
		if (l != null && !mCallist.contains(l)) {
			mCallist.add(l);
		}
	}

	public boolean removeCallback(IAnimChaged l) {
		return mCallist.remove(l);
	}

	public ArrayList<IAnimChaged> getCallback() {
		return mCallist;
	}

	public boolean setHandler(Handler handler) {
		if (mTime == null || mHandSchedule == null) {
			mHandSchedule = handler;
			return true;
		}
		return false;

	}

	public Handler getHandler() {
		return mHandSchedule;
	}

	public boolean start(int which, int delay, int duration, int period, boolean reversal) {
		if (mTime == null) {
			mWhich = which;
			delay = Math.max(1, delay);
			if (duration <= 0) {
				duration = 300;
			}
			if (period <= 0) {
				period = Math.min(40, Math.max((int) Math.sqrt(duration * 1.2f), 8));
			}
			mScheduleDest = (int) Math.ceil(duration / (double) period);
			toggleTimer(true, delay, period);
		}
		return false;
	}

	public void stop() {
		toggleTimer(false, 0, 0);
	}

	private void notifyStarted(float val) {
		for (int i = 0; i < mCallist.size(); i++) {
			mCallist.get(i).onAnimChaged(null, IAnimChaged.TYPE_START, mWhich, val, 0);
		}
	}

	private void notifyValueChanged(float val, float dval) {
		for (int i = 0; i < mCallist.size(); i++) {
			mCallist.get(i).onAnimChaged(null, IAnimChaged.TYPE_CHANGED, mWhich, val, dval);
		}
	}

	private void notifyFinished(float val, float dval) {
		for (int i = 0; i < mCallist.size(); i++) {
			mCallist.get(i).onAnimChaged(null, IAnimChaged.TYPE_END, mWhich, val, dval);
		}
	}

	class NotifyRunable implements Runnable {
		int mAnimType = 0;
		float mVal, mDval;

		public NotifyRunable(int type, float val, float dval) {
			mAnimType = type;
			mVal = val;
			mDval = dval;
		}

		@Override
		public void run() {
			switch (mAnimType) {
			case IAnimChaged.TYPE_CHANGED:
				notifyValueChanged(mVal, mDval);
				break;
			case IAnimChaged.TYPE_START:
				notifyStarted(mVal);
				break;
			case IAnimChaged.TYPE_END:
				notifyFinished(mVal, mDval);
				break;
			}
		}
	}
}
