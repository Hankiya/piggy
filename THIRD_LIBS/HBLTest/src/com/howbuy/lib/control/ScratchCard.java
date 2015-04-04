package com.howbuy.lib.control;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.howbuy.lib.compont.Linear0to1;
import com.howbuy.lib.interfaces.IAnimChaged;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-18 下午4:40:58
 */
public class ScratchCard extends FrameLayout implements IAnimChaged {
	protected static Xfermode XFMODER = new PorterDuffXfermode(
			PorterDuff.Mode.CLEAR);
	protected String TAG = "ScratchCard";
	protected final Rect mRecFrame = new Rect();
	private boolean mFirstVisible = true;
	private float mTouchTolerate = 1f;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaintCard, mPaint;
	private final Path mPath = new Path();
	private float mX, mY;

	protected float mDensity;
	private Drawable mDrawableCard = null;

	private int mColorScratchDefault = 0x99ff0000;
	private float mTouchWidth = 35, mAnimRate = -1;

	private Linear0to1 mLinear = null;
	private Interpolator mPolater = null;
	protected boolean mAnimFillAfter = false;
	protected boolean mCardRemoved = false;

	private Linear0to1 getLinear() {
		if (mLinear == null) {
			mLinear = new Linear0to1(getHandler(), mPolater, this);
		}
		return mLinear;
	}

	final public boolean isAnimRuning() {
		return mLinear == null ? false : getLinear().isRunning();
	}

	final public boolean isCanvasVisible() {
		return getVisibility() == VISIBLE && mRecFrame != null
				&& !mRecFrame.isEmpty();
	}

	final private boolean startAnim(int which, int duration, int period,
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

	public ScratchCard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScratchCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mTouchWidth *= (mDensity);
		mTouchTolerate *= mDensity;
		mPaintCard = new Paint();
		mPaintCard.setXfermode(XFMODER);
		// mPaint.setDither(true);
		mPaintCard.setStyle(Paint.Style.STROKE);
		mPaintCard.setStrokeJoin(Paint.Join.ROUND);
		mPaintCard.setStrokeCap(Paint.Cap.ROUND);
		mPaintCard.setStrokeWidth(mTouchWidth);
		mPaint = new Paint();
		setInterpolator(new AccelerateInterpolator());
	}

	public void setCardDefColor(int color) {
		mColorScratchDefault = color;
	}

	public void setTouchWidthRaw(float width) {
		mTouchWidth = width;
		mPaintCard.setStrokeWidth(mTouchWidth);
		if (!mRecFrame.isEmpty()) {
			invalidate();
		}
	}

	public void setTouchWidth(float width) {
		width = Math.max(Math.min(width, 100), 10);
		setTouchWidthRaw(width * mDensity);
	}

	public void removeCardInAnim(int duration) {
		startAnim(1, duration, -1, true, true);
	}

	public void resetCard(boolean resetTouch, boolean resetCard) {
		if (!mRecFrame.isEmpty()) {
			if (resetTouch) {
				resetPoint();
				mPath.reset();
				resetCard = true;
				mCardRemoved = false;
				if (isEnabled() == false) {
					setEnabled(true);
				}
			}
			if (resetCard) {
				setupBitmap(true);
			}
			invalidate();
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (!mRecFrame.isEmpty()) {
			super.dispatchDraw(canvas);
			if (!mCardRemoved) {
				setupBitmap(false);
				mCanvas.drawPath(mPath, mPaintCard);
				if (isAnimRuning()) {
					mPaint.setAlpha((int) (255 * mAnimRate));
				} else {
					mPaint.setAlpha(255);
				}
				canvas.drawBitmap(mBitmap, 0, 0, mPaint);
			}
		}
	}

	private void setupBitmap(boolean forceDraw) {
		if (mBitmap == null) {
			mBitmap = Bitmap.createBitmap(mRecFrame.width(),
					mRecFrame.height(), Config.ARGB_4444);
			mCanvas = new Canvas(mBitmap);
			forceDraw = true;
		}
		if (forceDraw || (mDrawableCard == null && getBackground() != null)) {
			mDrawableCard = getBackground();
			if (mDrawableCard == null) {
				mCanvas.drawColor(mColorScratchDefault, Mode.SRC);
			} else {
				mCanvas.drawColor(0, Mode.SRC);
				if (mDrawableCard.getBounds().isEmpty()) {
					mDrawableCard.setBounds(mRecFrame);
				}
				mDrawableCard.draw(mCanvas);
			}
		}
	}

	private void destoryBitmap() {
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mBitmap == null || mCardRemoved || isAnimRuning() || !isEnabled()) {
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touchDown(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			if (mPath.isEmpty()) {
				touchDown(event.getX(), event.getY());
			} else {
				touchMove(event.getX(), event.getY());
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			touchUp(event.getX(), event.getY());
			break;
		default:
			break;
		}
		return true;
	}

	private void resetPoint() {
		mPoints.clear();
		mPointRects.clear();
		mPointRect = null;
		if (mListener != null) {
			mListener.onScratchChanged(this, 0);
		}
	}

	public void setScratchListener(IScratchListener l) {
		mListener = l;
	}

	private IScratchListener mListener;
	private final HashMap<Integer, Point> mPoints = new HashMap<Integer, Point>();
	private final ArrayList<Rect> mPointRects = new ArrayList<Rect>();
	private Rect mPointRect = null;
	private int mCol = 0, mRow = 0, mCels = 0;

	public ArrayList<Rect> getRecordRect() {
		return mPointRects;
	}

	public ArrayList<Point> getRecordPoint() {
		ArrayList<Point> list = new ArrayList<Point>();
		list.addAll(mPoints.values());
		return list;
	}

	private void recordPoint(int x, int y, boolean continuous) {
		if (mRecFrame.contains(x, y)) {
			float cell = mTouchWidth * 0.98f;
			int c = Math.round((x - mRecFrame.left) / cell);
			int r = Math.round((y - mRecFrame.top) / cell);
			int key = c * 10000 + r;
			if (!mPoints.containsKey(key)) {
				mPoints.put(key, new Point(x, y));
				if (!continuous) {
					mCol = (int) Math.round(mRecFrame.width() / cell);
					mRow = (int) Math.round(mRecFrame.height() / cell);
					mCels = mCol * mRow;
					mPointRect = new Rect(x, y, x, y);
					mPointRects.add(mPointRect);
				} else {
					if (mPointRect != null) {
						mPointRect.union(x, y);
						checkCardState();
					}
				}
			}
			invalidate(mRecFrame);
		}
	}

	private void checkCardState() {
		int n = mPointRects.size();
		for (int i = 0; i < n; i++) {
			Rect rect = mPointRects.get(i);
			if (!rect.equals(mPointRect)) {
				if (rect.intersect(mPointRect)) {
					rect.union(mPointRect);
					mPointRects.remove(mPointRect);
					mPointRect = rect;
					break;
				}
			}
		}
		float rate = adjustRate(mPoints.size() / (float) mCels);
		if (mListener != null) {
			mListener.onScratchChanged(this, rate);
		}
		/**
		 * StringBuffer sb = new StringBuffer(); for (int i = 0; i <
		 * mPointRects.size(); i++) { mPointRect = mPointRects.get(i);
		 * sb.append("rect_" + i).append(mPointRect.toShortString())
		 * .append("  "); } dd(
		 * "checkCardState>point size is %1$d ,total cels is %2$d ,point area is %3$f"
		 * , mPoints.size(), mCels, rate); dd("checkCardState>rects:%1$s",
		 * sb.toString());
		 */
	}

	private float adjustRate(float rate) {
		if (rate < 0.5f) {
			if (rate < 0.25f) {
				if (rate < 0.15f) {
					rate *= (1.1 + rate);
				} else {
					rate *= 1.25f;
				}
			} else {
				if (rate < 0.35f) {
					rate *= 1.2f;
				} else {
					rate *= 1.15f;
				}
			}
		} else {
			if (rate < 0.65f) {
				rate *= 1.13f;
			} else {
				if (rate < 0.85f) {
					rate *= 1.1f;
				} else {
					rate *= 1.05f;
				}
			}
		}
		return Math.min(rate, 1);
	}

	private void touchDown(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		recordPoint((int) x, (int) y, false);
	}

	private void touchMove(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= mTouchTolerate || dy >= mTouchTolerate) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			recordPoint((int) x, (int) y, true);
		}
	}

	private void touchUp(float x, float y) {
		mPath.lineTo(x, y);
		recordPoint((int) x, (int) y, true);
		if (mPointRect == null || mPointRect.isEmpty()) {// also draw touch
															// point .
			mPath.lineTo(x, y + mTouchTolerate);
			invalidate();
		}
	}

	protected void computeCanvasRegion(int w, int h, boolean fromUser) {
		int pl = getPaddingLeft(), pr = getPaddingRight();
		int pt = getPaddingTop(), pb = getPaddingBottom();
		mRecFrame.set(pl, pt, w - pr, h - pb);
		destoryBitmap();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) {
			mFirstVisible = true;
		} else {
			computeCanvasRegion(w, h, false);
			if (mFirstVisible && !onViewFirstSteped(w, h)) {
				requestLayout();
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		destoryBitmap();
	}

	public boolean onViewFirstSteped(int w, int h) {
		mFirstVisible = false;
		resetCard(true, false);
		return false;
	}
 
	public interface IScratchListener {
		public void onScratchChanged(ScratchCard card, float rate);

		public void onScratchRemoved();
	}

	@Override
	public void onAnimChaged(View v, int type, int which, float val, float dval) {
		mAnimRate = formatAnimRate(val, true);
		if (type == IAnimChaged.TYPE_CHANGED) {
			invalidate();
		} else if (type == IAnimChaged.TYPE_END) {
			mCardRemoved = true;
			if (mListener != null) {
				mListener.onScratchRemoved();
			}
		}
	}
}
