package com.howbuy.lib.control;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.Linear0to1;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.LogUtils;

public abstract class AbsView extends View implements IAnimChaged {
	protected String TAG=null;
	public static final int FLAG_NONE = 0, FLAG_BASE = 1;
	protected boolean mAnimFillAfter = false;
	protected float mDensity,mAnimRate=-1;
	protected int mFlag;
	private int mFlagPre;
	private Linear0to1 mLinear = null;
	protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Interpolator mPolater = null;
	protected final Rect mRecFrame = new Rect();
	protected final RectF mRecTemp = new RectF();
	private boolean mSaved = false;

	public AbsView(Context context) {
		this(context, null);
	}

	public AbsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TAG=getClass().getSimpleName();
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mPaint.setStrokeWidth(mDensity);
		mPaint.setTextAlign(Align.CENTER);
	}

	protected void computeCanvasRegion(int w, int h, boolean fromUser) {
		int pl = getPaddingLeft(), pr = getPaddingRight();
		int pt = getPaddingTop(), pb = getPaddingBottom();
		mRecFrame.set(pl, pt, w - pr, h - pb);
        onFrameSizeChanged(fromUser);
	}

	/**
	 * format rate to [-1,1];
	 */
	protected float formatAnimRate(float result,boolean noReversal) {
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
			Handler h=getHandler();
			if(h==null){
				h=GlobalApp.getApp().getHandler();
			}
			mLinear = new Linear0to1(h,mPolater, this);
		} 
		return mLinear;
	}
    final protected Paint getPaint(){
    	return mPaint;
    }
    final protected Rect getRectFrame(){
    	return mRecFrame;
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

	public boolean isAnimFillAfter() {
		return mAnimFillAfter;
	}

	final public boolean isAnimRuning() {
		return mLinear == null ? false : getLinear().isRunning();
	}
	final public boolean isCanvasVisible(){
		return getVisibility() == VISIBLE &&mRecFrame!=null&& !mRecFrame.isEmpty();
	}
    
	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		return true;
	}
	
	@Override
	public void onAnimChaged(View nullView,int type, int which, float val, float dval) {
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mLinear != null) {
			mLinear.destory();
			mLinear = null;
		}
	}

	protected void onFlagPreExchanged(boolean isSave) {
	}
    protected abstract void onFrameSizeChanged(boolean fromUser);
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) {
			mRecFrame.setEmpty();
		} else {
			boolean firstInit = mRecFrame.isEmpty();
			computeCanvasRegion(w, h, false);
			notifyDataChanged(false, false);
		    if(!firstInit||!onViewFirstSteped(w, h)){
			  invalidate();	
			}
		}
	}

	protected boolean onViewFirstSteped(int w, int h) {
		return false;
	}

	@Override
	public void requestLayout() {
		super.requestLayout();
		if(isCanvasVisible()){
			computeCanvasRegion(getWidth(), getHeight(), true);
			notifyDataChanged(true, true);
		}
	}

	final public boolean startAnim(int which, int duration, int period,
			boolean animFillAfter,boolean reversal) {
		boolean result = getLinear().start(which, duration, period,reversal);
		setAnimFillAfter(result ? animFillAfter : mAnimFillAfter);
		return result;
	}

	final public void stopAnim(boolean animFillAfter) {
		boolean result, before = mAnimFillAfter;
		mAnimFillAfter = animFillAfter;
		result = getLinear().stop();
		setAnimFillAfter(result ? mAnimFillAfter : before);
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
		if(title==null){
			LogUtils.d(TAG, msg);
		}else{
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
