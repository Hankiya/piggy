package com.howbuy.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.howbuy.component.TouchHandler;
import com.howbuy.component.TouchHandler.ITouchEvent;

public class TouchCurveView extends CurveView implements ITouchEvent {
	private int mSelect1 = -1, mSelect2 = -1;
	private RectF mRecTemp = new RectF();
	private PaintFlagsDrawFilter mPaintFlag = null;
	private TouchHandler mTouchHandler = null;

	public TouchCurveView(Context context, AttributeSet attr) {
		super(context, attr);
		mTouchHandler = new TouchHandler(context, this);
	}

	protected void onDraw(Canvas can) {
		super.onDraw(can);
		mPaint.setColor(mSetting.getCrossColor());
		if (mSelect1 != -1 && mSelect2 != -1) {
			if (mSelect1 == mSelect2) {
				drawSingleLine(can, mPaint);
			} else {
				drawDoubleLine(can, mPaint);
			}
		}
	}

	private void drawSingleLine(Canvas canvas, Paint p) {
		if (mPaintFlag == null) {
			mPaintFlag = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
					| Paint.FILTER_BITMAP_FLAG);
		}
		canvas.setDrawFilter(mPaintFlag);
		float r = mSetting.getCrossSize() * 1.6f, R = r * 1.5f;
		float top = mRecCurve.top + mRecFrame.top;
		float[] cenXY = indexToPoint(mSelect1);
		mPaint.setStrokeWidth(mSetting.mDensity);
		canvas.drawCircle(cenXY[0], cenXY[1], r, p);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(cenXY[0], cenXY[1], R, p);
		mPaint.setStrokeWidth(mSetting.getCrossSize());
		canvas.drawLine(cenXY[0], cenXY[1] - R, cenXY[0], top, p);
		canvas.drawLine(cenXY[0], cenXY[1] + R, cenXY[0], mRecCurve.bottom + mRecFrame.top, p);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	private void drawDoubleLine(Canvas canvas, Paint p) {
		float top = mRecCurve.top + mRecFrame.top;
		float bot = mRecCurve.bottom + mRecFrame.top;
		float[] cen1 = indexToPoint(mSelect1);
		float[] cen2 = indexToPoint(mSelect2);
		mRecTemp.set(cen1[0], top, cen2[0], bot);
		if (mRecTemp.isEmpty()) {
			mRecTemp.sort();
		}
		mPaint.setColor(0x33666666);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawRect(mRecTemp, mPaint);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(mSetting.getCrossColor());
		mPaint.setStrokeWidth(mSetting.getCrossSize());
		canvas.drawLine(cen1[0], top, cen1[0], bot, p);
		canvas.drawLine(cen2[0], top, cen2[0], bot, p);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			mTouchHandler.resetTouch(false);
			return false;
		} else {
			mTouchHandler.resetTouch(true);
		}
		try {
			mTouchHandler.onTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onTouchTimeOut(int lineN) {
		if (lineN > 0) {
			adjustTouchIndex(lineN, "onTouchTimeOut");
		}
	}

	@Override
	public void onLineChanged(int preLineN, int curLineN) {
		if (mTouchHandler.hasUserFocused()) {
			if (curLineN > 0) {
				adjustTouchIndex(curLineN, "onLineChanged");
			} else {
				if (preLineN > 0) {
					adjustTouchIndex(preLineN, "onLineChanged empty");
				}
				if (mTouchObserver != null) {
					mTouchObserver.onTouchObserver(false, 0);
				}
			}
		}
	}

	@Override
	public void onLineMove(int lineN) {
		if (mTouchHandler.hasUserFocused()) {
			adjustTouchIndex(lineN, "onLineMove");
		}
	}

	private void adjustTouchIndex(int touchCount, String arg) {
		RectF r = mTouchHandler.getCurArea();
		if (touchCount == 1) {
			mSelect1 = mSelect2 = pointToIndex(r.left, true);
		} else {
			mSelect1 = pointToIndex(r.left, true);
			mSelect2 = pointToIndex(r.right, true);
		}
		invalidate();
		int n = mTouchHandler.getTouchPointCount();
		if (mTouchObserver != null && n > 0) {
			mTouchObserver.onTouchObserver(true, n);
		}
	}

	public int getSelect1() {
		return mSelect1;
	}

	public int getSelect2() {
		return mSelect2;
	}

	public ITouchObserver mTouchObserver = null;

	public void setTouchObverser(ITouchObserver l) {
		mTouchObserver = l;
	}

	public interface ITouchObserver {
		public void onTouchObserver(boolean focused, int touchCount);
	}

	@Override
	public void onTabSingle(MotionEvent e) {
		if (mTouchObserver != null) {
			mTouchObserver.onTouchObserver(false, 1);
		}
	}

	public void cleanTouchSign() {
		mSelect1 = mSelect2 = -1;
		invalidate();
	}
}
