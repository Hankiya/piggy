package com.howbuy.lib.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.Interpolator;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.Linear0to1;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.LogUtils;
/**
 * @author rexy 840094530@qq.com
 * @date 2014-1-10 下午5:15:57
 */
public abstract class AbsSfView extends SurfaceView implements
		SurfaceHolder.Callback, IAnimChaged {
	protected String TAG =null;
	public static final int FLAG_NONE = 0, FLAG_BASE = 1;
	protected boolean mAnimFillAfter = false;
	protected float mDensity, mAnimRate = -1;
	protected int mFlag;
	private int mFlagPre;
	private Linear0to1 mLinear = null;
	protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Interpolator mPolater = null;
	protected final Rect mRecBounds = new Rect();
	protected final Rect mRecFrame = new Rect();
	protected final RectF mRecTemp = new RectF();
	private boolean mSaved = false;
	private SurfaceHolder mSurfaceHoder = null;

	public AbsSfView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		TAG = getClass().getSimpleName();
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mPaint.setStrokeWidth(mDensity);
		mPaint.setTextAlign(Align.CENTER);
		mSurfaceHoder = getHolder();
		mSurfaceHoder.addCallback(this);
		setZOrderOnTop(true);
		mSurfaceHoder.setFormat(PixelFormat.TRANSLUCENT);
	}

	protected void computeCanvasRegion(int w, int h, boolean fromUser) {
		int pl = getPaddingLeft(), pr = getPaddingRight();
		int pt = getPaddingTop(), pb = getPaddingBottom();
		mRecFrame.set(pl, pt, w - pr, h - pb);
		onFrameSizeChanged(fromUser);
		invalidate();
	}

	private void drawSurface(Rect rect, boolean fromUser) {
		synchronized (mPaint) {
			Canvas can = null;
			try {
				can = mSurfaceHoder.lockCanvas(rect);
				if (can != null) {
					can.drawColor(0, Mode.CLEAR);
					onDrawSurface(can);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (can != null) {
					mSurfaceHoder.unlockCanvasAndPost(can);
				}
			}
		}

	}

	/**
	 * format rate to [-1,1];
	 */
	protected float formatAnimRate(float result, boolean noReversal) {
		result = noReversal ? result : 1 - result;
		if (result > 1) {
			result = 2 - result;
		} else if (result < -1) {
			result = -result - 2;
		}
		return result;
	}

	private Linear0to1 getLinear() {
		if (mLinear == null) {
			Handler h = getHandler();
			if (h == null) {
				h = GlobalApp.getApp().getHandler();
			}
			mLinear = new Linear0to1(h, mPolater, this);
		}
		return mLinear;
	}

	public float getInterpolator(float t) {
		if (mPolater != null) {
			if (t >= 0 && t <= 1) {
				return mPolater.getInterpolation(t);
			}
			return t;
		}
		return t > 0.5f ? 1 : 0;
	}

	public SurfaceHolder getSurfaceHolder() {
		return mSurfaceHoder;
	}

	@Override
	public void invalidate() {
		invalidate(null);
	}

	@Override
	public void invalidate(Rect dirty) {
		boolean visible = isCanvasVisible();
		if (visible && dirty != null && !dirty.isEmpty()) {
			super.invalidate(dirty);
		}
		if (visible) {
			drawSurface(dirty == null ? mRecBounds : dirty, true);
		}
	}

	public boolean isAnimFillAfter() {
		return mAnimFillAfter;
	}

	final public boolean isAnimRuning() {
		return mLinear == null ? false : getLinear().isRunning();
	}

	final public boolean isCanvasVisible() {
		return getVisibility() == VISIBLE && mRecFrame != null
				&& !mRecFrame.isEmpty();
	}

	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		return true;
	}

	protected abstract void onDrawSurface(Canvas can);

	protected void onFlagPreExchanged(boolean isSave) {
	}

	protected abstract void onFrameSizeChanged(boolean fromUser);

	protected boolean onViewFirstSteped(int w, int h) {
		return false;
	}

	@Override
	public void requestLayout() {
		super.requestLayout();
		if (isCanvasVisible()) {
			computeCanvasRegion(getWidth(), getHeight(), true);
			notifyDataChanged(isCanvasVisible(), true);
		}
	}

	final public boolean startAnim(int which, int duration, int period,
			boolean animFillAfter, boolean reversal) {
		boolean result = getLinear().start(which, duration, period, reversal);
		setAnimFillAfter(result ? animFillAfter : mAnimFillAfter);
		return result;
	}

	final public void stopAnim(boolean animFillAfter) {
		boolean result, before = mAnimFillAfter;
		mAnimFillAfter = animFillAfter;
		result = getLinear().stop();
		setAnimFillAfter(result ? mAnimFillAfter : before);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (w == 0 || h == 0) {
			mRecBounds.setEmpty();
			mRecFrame.setEmpty();
		} else {
			mRecBounds.set(0, 0, w, h);
			boolean firstInit = mRecFrame.isEmpty();
			computeCanvasRegion(w, h, false);
			notifyDataChanged(false, false);
			if (!firstInit || !onViewFirstSteped(w, h)) {
				invalidate();
			}

		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mLinear != null) {
			mLinear.destory();
			mLinear = null;
		}
	}

	public void setAnimFillAfter(boolean fillAfter) {
		mAnimFillAfter = fillAfter;
	}

	public boolean setInterpolator(Interpolator polator) {
		mPolater = polator;
		if (mLinear != null) {
			return mLinear.setInterpolator(polator);
		}
		return true;
	}

	final protected boolean saveFlag(int flag) {
		synchronized (mPaint) {
			if (mSaved == false) {
				onFlagPreExchanged(true);
				mFlagPre = mFlag;
				mFlag = flag;
				mSaved = true;
				return true;
			}
			return false;
		}
	}

	final protected boolean restoreFlag() {
		synchronized (mPaint) {
			if (mSaved) {
				onFlagPreExchanged(false);
				mFlag = mFlagPre;
				mSaved = false;
				return true;
			}
			return false;
		}
	}

	final public int getFlag() {
		return mFlag;
	}

	final protected Paint getPaint() {
		return mPaint;
	}

	final protected Rect getRectFrame() {
		return mRecFrame;
	}

	final protected void setFlag(int flag) {
		mFlag = flag;
	}

	final public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final protected void addFlag(int flag) {
		mFlag |= flag;
	}

	final protected void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}

	final protected void reverseFlag(int flag) {
		mFlag ^= flag;
	}

	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	final protected void dd(String msg, Object... args) {
		d(TAG, String.format(msg, args));
	}

	final protected void pop(String msg, boolean debug) {
		if (debug) {
			LogUtils.popDebug(msg);
		} else {
			LogUtils.pop(msg);
		}
	}
}