package com.howbuy.curve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.howbuy.lib.utils.ViewUtils;

public class CurveView extends View {
	protected final Rect mRecFrame = new Rect();
	protected final RectF mRecCurve = new RectF();
	protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected CurveSetting mSetting = new CurveSetting(this.getContext());
	protected final CurveControl mCurveCtrl = new CurveControl(mRecFrame, mRecCurve, mSetting,
			"CURVE_CTRL");

	public CurveView(Context context, AttributeSet attr) {
		super(context, attr);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setAntiAlias(true);
	}

	public CurveSetting getSetting() {
		return mSetting;
	}

	public void setCurveFactory(CurveFactory factory) {
		mCurveCtrl.setCurveFactory(factory);
	}

	protected boolean addCurve(ICharType charType, ArrayList<? extends ICharData> datas,
			boolean measure) {
		boolean result = false;
		if (mCurveCtrl.addCurve(charType, datas, measure)) {
			if (measure) {
				whenDataChanged(measure, true);
			}
			result = true;
		}
		return result;
	}

	public boolean setCurve(ICharType charType, ArrayList<? extends ICharData> datas) {
		boolean removed = mCurveCtrl.removeCurve(charType, true);
		boolean added = addCurve(charType, datas, true);
		return removed || added;
	}

	public boolean addCurve(ICharType charType, ArrayList<? extends ICharData> datas) {
		return addCurve(charType, datas, true);
	}

	public boolean removeCurve(ICharType charType, boolean cleanData) {
		boolean result = mCurveCtrl.removeCurve(charType, cleanData);
		if (result) {
			whenDataChanged(false, true);
		}
		return result;
	}

	public void cleanCurve(boolean cleanData, boolean cleanCurve) {
		mCurveCtrl.destory(cleanData, cleanCurve);
	}

	public void setCurveData(HashMap<ICharType, ArrayList<? extends ICharData>> datas) {
		cleanCurve(true, true);
		if (datas != null) {
			mCurveCtrl.setCurveList(datas);
		}
	}

	public boolean removeCurveData(ICharType charType, int index) {
		boolean result = false;
		Curve curve = mCurveCtrl.findCurve(charType, true);
		if (curve != null) {
			result = mCurveCtrl.removeCurveData(curve, index);
			if (result && curve.isVisible()) {
				whenDataChanged(false, true);
			}
			return result;
		}
		return result;
	}

	public boolean removeCurveData(ICharType charType, int from, int to) {
		boolean result = false;

		Curve curve = mCurveCtrl.findCurve(charType, true);
		if (curve != null) {
			result = mCurveCtrl.removeCurveData(curve, from, to);
			if (result && curve.isVisible()) {
				whenDataChanged(false, true);
			}
			return result;
		}
		return result;
	}

	public boolean addCurveData(ICharType charType, ArrayList<? extends ICharData> datas,
			boolean end, boolean ignoreIfNotExist) {
		return addCurveData(charType, datas, end, ignoreIfNotExist, true);
	}

	protected boolean addCurveData(ICharType charType, ArrayList<? extends ICharData> datas,
			boolean end, boolean ignoreIfNotExist, boolean measure) {
		boolean result = false;
		Curve curve = mCurveCtrl.findCurve(charType, true);
		if (curve != null) {
			result = mCurveCtrl.addCurveData(curve, datas, end);
			if (result && curve.isVisible()) {
				if (measure) {
					whenDataChanged(false, true);
				}
			}
			return result;
		} else if (!ignoreIfNotExist) {
			addCurve(charType, datas, measure);
		}
		return result;
	}

	public boolean addCurveData(HashMap<ICharType, ArrayList<? extends ICharData>> datas,
			boolean end, boolean ignoreIfNotExist) {
		boolean result = false;
		if (datas != null) {
			Iterator<Entry<ICharType, ArrayList<? extends ICharData>>> list = datas.entrySet()
					.iterator();
			while (list.hasNext()) {
				Entry<ICharType, ArrayList<? extends ICharData>> entry = list.next();
				result |= addCurveData(entry.getKey(), entry.getValue(), end, ignoreIfNotExist,
						false);
			}
		}
		if (result) {
			whenDataChanged(false, true);
		}
		return result;
	}

	public boolean setCurveVisible(ICharType charType, boolean visible) {
		boolean result = mCurveCtrl.setCurveVisible(charType, visible);
		if (result) {
			whenDataChanged(false, true);
		}
		return result;
	}

	public ArrayList<ICharData> getCurveData(ICharType charType) {
		return mCurveCtrl.getCurveData(charType);

	}

	protected Paint getPaint() {
		return mPaint;
	}

	protected Rect getRectFrame() {
		return mRecFrame;
	}

	protected RectF getRectCurve() {
		return mRecCurve;
	}

	public int size(boolean all) {
		return mCurveCtrl.size(all);
	}

	public boolean containt(ICharType charType, boolean all) {
		return mCurveCtrl.findCurve(charType, all) != null;
	}

	public void rePaintOrLayout(boolean rePaint) {
		if (rePaint) {
			whenDataChanged(true, true);
		} else {
			int w = getWidth(), h = getHeight();
			if (w != 0 && h != 0) {
				computeCanvasRegion(w, h, false);
				whenDataChanged(true, true);
			}

		}
	}

	/**
	 * when data has changed recalculate the max and min and scale and repaint.
	 * 
	 * @param remeasure
	 *            to re calculate the max and min of each curve.
	 * @param remeasure
	 *            true to calculate each curve of it's max and min.
	 * @param @param needInvalidate true to redraw all the curve.
	 * @throws
	 */
	public void whenDataChanged(boolean remeasure, boolean needInvalidate) {
		mCurveCtrl.whenDataChanged(remeasure, mSetting.mEnableAutoCalcuMinMax);
		if (needInvalidate) {
			if( mCurveCtrl.draw(true, -1)){
				postInvalidate();	
			}
		} 
	}
   
	/**
	 * when the paint region is changed ,recalculate the scale and repaint.
	 * 
	 * @param @param rect new region
	 * @throws
	 */
	protected void whenFrameChanged(boolean reCachePaint) {
		mCurveCtrl.whenFramChanged();
		if (reCachePaint) {
			if (mCurveCtrl.draw(true, -1)) {
				postInvalidate();
			}
		}
	}

	protected void computeCanvasRegion(int w, int h, boolean reCachePaint) {
		int pl = getPaddingLeft();
		int pr = getPaddingRight();
		int pt = getPaddingTop();
		int pb = getPaddingBottom();
		mRecFrame.set(pl, pt, w - pr, h - pb);
		adjustCurveRect();
		whenFrameChanged(reCachePaint);
	}

	// update adjustCurveRect
	protected void adjustCurveRect() {
		mRecCurve.set(mRecFrame);
		mRecCurve.offsetTo(0, 0);
		mRecCurve.top += mSetting.mDensity;
		mRecCurve.bottom -= mSetting.mDensity;
		mRecCurve.left += mSetting.mDensity * 4;
		mRecCurve.right -= mSetting.mEnableTxtYInSide ? 0 : (ViewUtils.getTxtWidth(
				mSetting.mMeasureTxt+"-", mSetting.mCoordTxtYSize) + mSetting.mTextYOffset);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) {
			mRecFrame.setEmpty();
		} else {
			computeCanvasRegion(w, h, false);
			mCurveCtrl.refushCapacity();
			whenDataChanged(true, true);
		}
	}

	protected void onDraw(Canvas can) {
		super.onDraw(can);
		mCurveCtrl.drawCurve(can, mRecFrame);
		mPaint.reset();
	}

	protected int pointToIndex(float x, boolean visible) {
		int index = -1;
		index = mCurveCtrl.pointToIndex(x - mRecFrame.left, visible);
		return index;
	}

	protected int pointToIndex(float x, float y, boolean pointClick) {
		int index = -1;
		index = mCurveCtrl.pointToIndex(x - mRecFrame.left, y - mRecFrame.top, pointClick);
		return index;
	}

	protected float[] indexToPoint(int index) {
		float[] result = null;
		result = mCurveCtrl.indexToPoint(index);
		if (result != null) {
			result[0] += mRecFrame.left;
			result[1] += mRecFrame.top;
		}
		return result;
	}

	protected float[] indexToPoint(ICharType iCharType, int index) {
		float[] result = null;
		result = mCurveCtrl.indexToPoint(iCharType, index);
		if (result != null) {
			result[0] += mRecFrame.left;
			result[1] += mRecFrame.top;
		}
		return result;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		destory(false);
		mCurveCtrl.destoryCanvas();
	}

	public void destory(boolean cleanData) {
		cleanCurve(cleanData, true);
	}
}
