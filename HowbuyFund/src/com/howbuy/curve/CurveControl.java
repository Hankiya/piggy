package com.howbuy.curve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;

/**
 * this class for control any T extends Curve and it can extends for custom
 * control.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:25:55
 */
public class CurveControl {
	public static String VALUE_FORMAT = "0.0000";
	protected String TAG = "CurveControl";
	protected Curve mCurve;
	protected Canvas mCanvas;
	protected Bitmap mCanvasBmp;
	protected int mSize = 0, mVisibleSize = 0;
	protected float mMin1 = 0, mMin = 0;
	protected float mMax1 = 1, mMax = 1;
	protected boolean mEnablePadRight = true;
	protected CurveSetting mSetting = null;
	protected CurveFactory mFactory = null;
	protected final CtrlOption mCtrlOpt;
	protected final HashMap<ICharType, Curve> mCurveList = new HashMap<ICharType, Curve>();
	protected final ArrayList<CharDataType> mSelectResult = new ArrayList<CharDataType>();
	protected final RectF mRecCurve;
	protected final Rect mRecFrame;
	protected final Paint mPaint = new Paint();
	protected Path mPath = new Path();
	protected final RectF mRect = new RectF();

	public CurveControl(Rect rectFrame, RectF rectCurve, CurveSetting setting, String tag) {
		this(rectFrame, rectCurve, setting, null, tag);
	}

	public CurveControl(Rect rectFrame, RectF rectCurve, CurveSetting setting,
			HashMap<ICharType, ArrayList<? extends ICharData>> datas, String tag) {
		this.mRecCurve = rectCurve;
		this.mRecFrame = rectFrame;
		this.TAG = tag;
		this.mSetting = setting;
		this.mCtrlOpt = new CtrlOption(mRecCurve);
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		if (datas != null) {
			setCurveList(datas);
		}
	}

	public void setCurveSetting(CurveSetting set) {
		this.mSetting = set;
	}

	public void setCurveFactory(CurveFactory factory) {
		mFactory = factory;
	}

	/**
	 * set curve data and init curves.
	 * 
	 * @param @param datas
	 * @throws
	 */
	protected void setCurveList(HashMap<ICharType, ArrayList<? extends ICharData>> datas) {

		Iterator<Entry<ICharType, ArrayList<? extends ICharData>>> list = datas.entrySet()
				.iterator();
		while (list.hasNext()) {
			Entry<ICharType, ArrayList<? extends ICharData>> data = list.next();
			mCurveList.put(data.getKey(), createCurve(data.getValue(), data.getKey()));
		}
	}

	protected Curve createCurve(ArrayList<? extends ICharData> datas, ICharType charType) {
		Curve curve = null;
		if (mFactory != null) {
			curve = mFactory.createCurve(mCtrlOpt, new RectF(mRecCurve), datas, charType);
		} else {
			curve = CurveFactory.getDefCurve(mCtrlOpt, new RectF(mRecCurve), datas, charType);
		}
		int size = curve.size();
		mSize += 1;
		mVisibleSize += 1;
		mCtrlOpt.initSpace(mEnablePadRight, size, mSetting.mEnableInitShowAll);
		mCurve = curve;

		return curve;
	}

	/**
	 * add curve data and init a new curve.
	 * 
	 * @param measure
	 *            true to calculate the max and min of the curve data,otherwise
	 *            ignore it.
	 */
	public boolean addCurve(ICharType charType, ArrayList<? extends ICharData> data, boolean measure) {
		if (!mCurveList.containsKey(charType)) {
			Curve curve = createCurve(data, charType);
			if (measure) {
				curve.whenDataChanged(measure, mSetting.mEnableAutoCalcuMinMax);
			}
			mCurveList.put(charType, curve);
			return true;
		}
		return false;

	}

	/**
	 * remove a curve.
	 */
	public boolean removeCurve(ICharType charType, boolean cleanData) {
		Curve curve = mCurveList.remove(charType);
		if (curve != null) {
			mSize -= 1;
			mVisibleSize -= 1;
			curve.destory(cleanData);
			return true;
		}
		return false;
	}

	public boolean setCurveVisible(ICharType iCharType, boolean visible) {
		Curve curve = findCurve(iCharType, true);
		if (curve != null && curve.isVisible() != visible) {
			curve.setVisible(visible);
			mVisibleSize += (visible ? 1 : -1);
			return true;
		}
		return false;
	}

	public boolean addCurveData(Curve curve, ArrayList<? extends ICharData> datas, boolean end) {
		float last = curve.getYAt(curve.size() - 1);
		boolean result = curve.addData(datas, end);
		if (result) {
			curve.whenDataChanged(false, mSetting.mEnableAutoCalcuMinMax);
			if (mCtrlOpt.mOffsetX < 0) {
				last = (mRecCurve.right - last) / 2;
				last = Math.min(last, -mCtrlOpt.mOffsetX - mCtrlOpt.mSpace / 2);
				if (curve.size() > mCtrlOpt.mMaxVisibleLen) {
					mCtrlOpt.mMaxVisibleLen = curve.size();
				}
				if (moveCurves(last) == 1) {
					whenDataChanged(true, mSetting.mEnableAutoCalcuMinMax);
				}
			}
		}
		return result;
	}

	public boolean removeCurveData(Curve curve, int from, int to) {
		boolean result = curve.removeData(from, to);
		if (result) {
			curve.whenDataChanged(false, mSetting.mEnableAutoCalcuMinMax);
			whenDataChanged(false, mSetting.mEnableAutoCalcuMinMax);
		}
		return result;
	}

	public boolean removeCurveData(Curve curve, int index) {
		boolean result = curve.removeData(index);
		if (result) {
			curve.whenDataChanged(false, mSetting.mEnableAutoCalcuMinMax);
		}
		return result;
	}

	public Curve findCurve(ICharType iCharType, boolean all) {

		if (mCurveList.containsKey(iCharType)) {
			Curve curve = mCurveList.get(iCharType);
			if (all) {
				return curve;
			} else if (curve.isVisible()) {
				return curve;
			}
		}
		return null;
	}

	/**
	 * get the curve size in this container.
	 * 
	 * @param @param all set true to get all size otherwise get the visible
	 *        curves count.
	 */
	public int size(boolean all) {

		return all ? mSize : mVisibleSize;
	}

	/**
	 * get the the size of appoint curve with type of iCharType.
	 * 
	 * @param @param iCharType allow to be none when need the max size of the
	 *        curves.
	 * @return return -1 if the curve is not exist;
	 * @throws
	 */
	public int getCurveSize(ICharType iCharType) {

		if (iCharType != null) {
			Curve curve = findCurve(iCharType, true);
			if (curve != null) {
				return curve.size();
			}
		} else {
			return mCtrlOpt.mMaxVisibleLen;
		}
		return -1;
	}

	public ArrayList<ICharData> getCurveData(ICharType iCharType) {

		if (iCharType != null) {
			Curve curve = findCurve(iCharType, true);
			if (curve != null) {
				return curve.getData();
			}
		} else {
			if (mCurve != null) {
				return mCurve.getData();
			}
		}
		return null;
	}

	protected String getValue(CharDataType data) {
		return data.toString(mCtrlOpt.mDataType);
	}

	protected CharDataType getValueAt(int index) {
		return mCurve.getDataAt(index);
	}

	protected ArrayList<CharDataType> getValuesAt(int index) {
		mSelectResult.clear();
		Iterator<Entry<ICharType, Curve>> list = mCurveList.entrySet().iterator();
		while (list.hasNext()) {
			Curve curve = list.next().getValue();
			if (curve.isVisible()) {
				CharDataType dataType = curve.getDataAt(index);
				if (dataType != null) {
					mSelectResult.add(dataType);
				}
			}
		}
		return mSelectResult;
	}

	protected float getSpace() {
		return mCtrlOpt.mSpace;
	}

	protected int getDataType() {
		return mCtrlOpt.mDataType;
	}

	protected boolean setDataType(int dataType) {
		mCtrlOpt.mDataType = dataType;
		return draw(true, -1);
	}

	protected void resetMinMax() {
		mMin1 = mMin = Integer.MAX_VALUE;
		mMax1 = mMax = Integer.MIN_VALUE;
	}

	/**
	 * when data has changed notify each sub curve to calculate the max and min
	 * value,and adjust them.
	 * 
	 * @param adjustChildMaxAndMin
	 *            true to adjust all sub curve min and max.
	 * @return void
	 * @throws
	 */
	protected void whenDataChanged(boolean remeasure, boolean calcuVisible) {
		Iterator<Entry<ICharType, Curve>> list = mCurveList.entrySet().iterator();
		resetMinMax();
		mCtrlOpt.mMaxVisibleLen = 0;
		mCurve = null;
		while (list.hasNext()) {
			Curve curve = list.next().getValue();
			if (curve.isVisible()) {
				if (mCtrlOpt.mMaxVisibleLen < curve.size()) {
					mCtrlOpt.mMaxVisibleLen = curve.size();
					mCurve = curve;
				}
				if (remeasure) {
					curve.whenDataChanged(false, calcuVisible);
				}
				minMax(curve.getMin(), curve.getMax());
			} else {
			}
		}
		adjstChildMinMax();
	}

	protected void whenFramChanged() {

		destoryCanvas();
		setupCanvas();
		Iterator<Entry<ICharType, Curve>> list = mCurveList.entrySet().iterator();
		while (list.hasNext()) {
			list.next().getValue().setBounds(mRecCurve);
		}
		if (!mCtrlOpt.mHasInitWithData) {
			mCtrlOpt.initSpace(mEnablePadRight, mCurve == null ? 0 : mCurve.size(),
					mSetting.mEnableInitShowAll);
		}

	}

	public void refushCapacity() {
		mCtrlOpt.mCapacity = (int) (mCtrlOpt.mRect.width() / mCtrlOpt.mSpace);
		if (!mEnablePadRight) {
			mCtrlOpt.mCapacity++;
		}
	}

	/**
	 * adjust all child max and min
	 * 
	 * @param
	 * @return void
	 * @throws
	 */
	protected void adjstChildMinMax() {
		adjustMinMax(false);
		Iterator<Entry<ICharType, Curve>> list = mCurveList.entrySet().iterator();
		while (list.hasNext()) {
			Curve curve = list.next().getValue();
			if (curve.isVisible()) {
				curve.setMinMax(mMin, mMax);
			}
		}
		mSetting.mMeasureTxt = StrUtils.formatF(mMax, VALUE_FORMAT);
	}

	protected void adjustMinMax(boolean checkYmin) {
		if (mSetting.mCoordUpWeight != 0 || mSetting.mCoordBotWeight != 0) {
			float lenY = (mMax1 - mMin1) / (1 - mSetting.mCoordBotWeight - mSetting.mCoordUpWeight);
			mMin = mMin1 - lenY * mSetting.mCoordBotWeight;
			mMax = mMax1 + lenY * mSetting.mCoordUpWeight;
			if (checkYmin && mMin < 0) {
				mMax = mMax1 + mSetting.mCoordUpWeight * (mMin1) / mSetting.mCoordBotWeight;
				mMin = Math.min(0, mMin1);
				lenY = mMax - mMin;
				mSetting.mCoordUpWeight = (mMax - mMax1) / lenY;
				mSetting.mCoordBotWeight = (mMin1 - mMin) / lenY;
			}
		}
		if (mMax == mMin) {
			if (mMax == 0 && size(true) > 0) {
				mMax = 1.5f;
				mMin = -1f;
			} else {
				mMax *= 1.2f;
				mMin *= 0.8f;
			}
		}

	}

	protected void minMax(float min, float max) {
		mMin = mMin1 = Math.min(mMin1, min);
		mMax = mMax1 = Math.max(mMax1, max);

	}

	protected void drawCurve(Canvas canvas, Rect rec) {
		if (mCanvasBmp != null && !mCanvasBmp.isRecycled()) {
			canvas.drawBitmap(mCanvasBmp, rec.left, rec.top, mPaint);
		} else {
		}
	}

	protected void drawRect(Canvas can, Paint paint, int type, int startColor, int endColor,
			float dval) {
		mPath.reset();
		if (type == 0) {
			mPath.addRect(mRect, Direction.CW);
		} else {
			mPath.moveTo(mRect.left, mRect.bottom);
			if (type == 1) {
				mPath.lineTo(mRect.left, mRect.top);
				mPath.lineTo(mRect.right, mRect.top - dval);
				mPath.lineTo(mRect.right, mRect.bottom - dval);
			} else {
				mPath.lineTo(mRect.left + dval, mRect.top);
				mPath.lineTo(mRect.right + dval, mRect.top);
				mPath.lineTo(mRect.right, mRect.bottom);
			}
		}
		mPath.close();
		mCtrlOpt.drawPathShade(can, mPath, mRect,
				new Rect(0, 0, (int) mRect.width(), (int) mRect.height()), startColor, endColor);
	}

	protected void drawBackgroud(Canvas can, Paint paint) {
		if (mSetting.mEnable3D) {
			int startColor = mSetting.mBackYColor, endColor = ViewUtils.color(startColor, 0.5f, 1);
			mRect.set(mRecCurve);
			mRect.right = mRect.left + mSetting.getHdeep();
			drawRect(can, paint, 1, startColor, endColor, mSetting.getVdeep());

			startColor = mSetting.mBackXColor;
			endColor = ViewUtils.color(startColor, 0.5f, 1);
			mRect.set(mRecCurve);
			mRect.top = mRect.bottom - mSetting.getVdeep();
			drawRect(can, paint, 2, startColor, endColor, mSetting.getHdeep());

			startColor = ViewUtils.color(mSetting.mBackYColor, 0.5f, 1);
			mRect.set(mRecCurve);
			mRect.offset(mSetting.getHdeep(), -mSetting.getVdeep());
			drawRect(can, paint, 0, startColor, endColor, 0);
		} else {
			/*
			 * can.drawLine(mRecCurve.left, mRecCurve.bottom, mRecCurve.left,
			 * mRecCurve.top, paint); can.drawLine(mRecCurve.left,
			 * mRecCurve.bottom, mRecCurve.right, mRecCurve.bottom, paint);
			 * can.drawLine(mRecCurve.right, mRecCurve.top, mRecCurve.right,
			 * mRecCurve.bottom, paint); can.drawLine(mRecCurve.right,
			 * mRecCurve.top, mRecCurve.left, mRecCurve.top, paint);
			 */
		}

	}

	protected boolean draw(boolean animStop, float rate) {
		mCtrlOpt.mAnimStop = animStop;
		mCtrlOpt.mAnimRate = rate;
		if (mCanvas != null) {
			mCanvasBmp.eraseColor(0);
			if (mSetting.mEnableBackgroud) {
				drawBackgroud(mCanvas, mPaint);
			}
			if (mSetting.mEnableGrid && !mSetting.mEnable3D) {
				drawGrid(mCanvas, mPaint);
			} else {
				if (mSetting.mEnableCoordTxtX) {
					drawGridColAdXText(mCanvas, mPaint, null, false);
				}
			}

			if (mSetting.mEnableCoordX || mSetting.mEnableCoordY) {
				drawCoord(mCanvas, mPaint);
			} else {
				mCanvas.drawLine(mRecFrame.left, mRecCurve.top, mRecFrame.right, mRecCurve.top,
						mPaint);
				mCanvas.drawLine(mRecFrame.left, mRecCurve.bottom, mRecFrame.right,
						mRecCurve.bottom, mPaint);
			}

			if (size(false) > 0) {
				drawCurve(mCanvas, mPaint);
			}
			if (mSetting.mEnableCoordTxtY) {
				drawYText(mCanvas, mPaint);
			}
			return true;
		}
		return false;
	}

	// before draw curve we'd better to sort the curve drawer order by it's
	// getType.
	protected void drawCurve(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(mRecCurve);
		Iterator<Entry<ICharType, Curve>> list = mCurveList.entrySet().iterator();
		paint.setTextSize(mSetting.mCurveTxtSize);
		paint.setStrokeWidth(mSetting.mCurveSize);
		while (list.hasNext()) {
			Curve curve = list.next().getValue();
			if (curve.isVisible() && curve.size() > 0) {
				curve.draw(mCanvas, paint);
			}
		}
		canvas.restore();
	}

	protected void drawGrid(Canvas canvas, Paint paint) {
		DashPathEffect pathEffect = null;
		if (mSetting.mEnableGridDash) {
			pathEffect = new DashPathEffect(new float[] { 8, 5 }, 0.5f);
			paint.setPathEffect(pathEffect);
		}
		paint.setStrokeWidth(mSetting.mGridSize);
		paint.setColor(mSetting.mGridColor);
		drawGridRow(canvas, paint);
		drawGridColAdXText(canvas, paint, pathEffect, true);
		if (mSetting.mEnableGridDash) {
			paint.setPathEffect(null);
		}
	}

	protected void drawGridRow(Canvas canvas, Paint paint) {

		RectF recCurve = mRecCurve;
		float y = recCurve.top;
		float mDRow = mRecCurve.height() / mSetting.mRow;
		while (y <= recCurve.bottom) {
			canvas.drawLine(recCurve.left, y, recCurve.right, y, paint);
			y += mDRow;
		}
	}

	protected void drawGridColAdXText(Canvas canvas, Paint paint, DashPathEffect pathEffect,
			boolean enableGrid) {

		paint.setStrokeWidth(mSetting.mGridSize);
		paint.setTextSize(mSetting.mCoordTxtXSize);
		paint.setTextAlign(Align.CENTER);
		RectF recCurve = mRecCurve;
		float txtHight = ViewUtils.getTxtHeight(mSetting.mCoordTxtXSize);
		float txtCenterVerticalOffset = ViewUtils
				.getTxtCenterVerticalOffset(mSetting.mCoordTxtXSize);
		float xOffset = 0;
		float dc = (recCurve.width() - 2) / mSetting.mRow;
		float x = recCurve.left + 1;
		int i = 0;
		while (x <= recCurve.right) {
			if (enableGrid) {
				paint.setColor(mSetting.mGridColor);
				canvas.drawLine(x, recCurve.top, x, recCurve.bottom, paint);
			}
			if (mSetting.mEnableCoordTxtX) {
				paint.setColor(mSetting.mCoordTxtXColor);
				int id = pointToIndex(x, 0, false);
				ICharData data = mCtrlOpt.mMaxVisibleLen != 0 ? mCurve.getDataAt(id).mData : null;
				String xStr = data != null ? data.getTime("MM/dd") : "00:" + id;
				xOffset = 0;
				if (i == 0) {
					xOffset = Math.max(0, paint.measureText(xStr) / 2 - x);
				}
				if (i++ == mSetting.mCol) {
					xOffset = Math.min(0,
							(mRecFrame.width() - recCurve.right) - paint.measureText(xStr) / 2);
				}
				paint.setPathEffect(null);
				canvas.drawText(xStr, x + xOffset, recCurve.bottom + txtHight
						+ txtCenterVerticalOffset + 5, paint);
				paint.setPathEffect(pathEffect);
			}
			x += dc;
		}
	}

	protected void drawCoord(Canvas canvas, Paint paint) {

		paint.setColor(mSetting.mCoordColor);
		paint.setStrokeWidth(mSetting.mCoordSize);
		RectF recCurve = mRecCurve;
		float top = recCurve.top;
		float right = recCurve.right;
		float arowLen = 6;
		float arowspace = arowLen / 2;
		if (mSetting.mEnableCoordY) {
			canvas.drawLine(recCurve.left, recCurve.bottom, recCurve.left, top, paint);
			canvas.drawLine(recCurve.left, top, recCurve.left - arowspace, top + arowLen, paint);
			canvas.drawLine(recCurve.left, top, recCurve.left + arowspace, top + arowLen, paint);
		}
		if (mSetting.mEnableCoordX) {
			canvas.drawLine(recCurve.left, recCurve.bottom, right, recCurve.bottom, paint);
			canvas.drawLine(right, recCurve.bottom, right - arowLen, recCurve.bottom - arowspace,
					paint);
			canvas.drawLine(right, recCurve.bottom, right - arowLen, recCurve.bottom + arowspace,
					paint);
		}
	}

	protected void drawYText(Canvas canvas, Paint paint) {

		RectF recCurve = mRecCurve;
		float txtLeft = recCurve.right;
		if (mSetting.mEnableTxtYInSide) {
			paint.setTextAlign(Align.RIGHT);
			txtLeft -= mSetting.mTextYOffset;
		} else {
			paint.setTextAlign(Align.LEFT);
			txtLeft += mSetting.mTextYOffset;
		}

		paint.setColor(mSetting.mCoordTxtYColor);
		paint.setTextSize(mSetting.mCoordTxtYSize);
		paint.setStrokeWidth(mSetting.mCoordTxtWidth);
		float mDRow = mRecCurve.height() / mSetting.mRow;
		float y = recCurve.top - mSetting.mTextYOffset;
		float dval = (mMax - mMin) / (mSetting.mRow), val = mMax;
		while (y <= recCurve.bottom) {
			canvas.drawText(/* valueFormat(val, 2) */StrUtils.formatF(val, VALUE_FORMAT), txtLeft,
					y, paint);
			y += mDRow;
			val -= dval;
		}
	}

	public int scaleCurves(float scale, float cenX, int oldCenIndex) {
		int result = 0;
		if (mCtrlOpt.adjustSpace(mEnablePadRight, mCtrlOpt.mSpace * scale)) {
			float x = mCtrlOpt.getXAt(oldCenIndex);

			result = mCtrlOpt.moveCurve(cenX - x);
			if (result == 0) {
				return -1;
			}
		}
		return result;
	}

	protected int moveCurves(float x) {
		return mCtrlOpt.moveCurve(x);
	}

	public int pointToIndex(float x, boolean visible) {
		int index = pointToIndex(x, 0, false);
		if (visible && index != -1) {
			if (index < mCtrlOpt.mIOffset) {
				index = -2;
			} else if (mCurve.getYAt(index) < 0) {
				index = -3;
			}
		}
		return index;
	}

	public int pointToIndex(float x, float y, boolean pointClick) {
		int index = -1;
		if (mCtrlOpt.mMaxVisibleLen > 0) {
			index = Math.min(
					Math.round((mRecCurve.right - mCtrlOpt.mOffsetRx - x) / (mCtrlOpt.mSpace)),
					mCtrlOpt.mCapacity);
			index = Math.max(Math.min(mCtrlOpt.mMaxVisibleLen - 1, index + mCtrlOpt.mIOffset), 0);
			if (pointClick) {
				if (Math.abs(mCurve.getYAt(index) - y) < mSetting.mClickRaidus) {
				} else {
					index = -1;
				}
			}
		}
		return index;
	}

	protected float[] indexToPoint(int index) {
		return indexToPoint(mCurve, index);
	}

	protected float[] indexToPoint(Curve curve, int index) {
		if (index >= 0 && index < mCtrlOpt.mMaxVisibleLen && curve != null) {
			float x = mCtrlOpt.getXAt(index);
			float y = curve.getYAt(index);
			return new float[] { x, y };
		}
		return null;
	}

	protected boolean pointInArea(float x, int selecet1, int select2) {
		float x1 = mCtrlOpt.getXAt(selecet1);
		float x2 = mCtrlOpt.getXAt(select2);
		return !((x > x1 && x > x2) || (x < x1 && x < x2));
	}

	protected int indexInScreen(int index) {
		float x = mCtrlOpt.getXAt(index);
		if (x > mRecCurve.right) {
			return 1;
		}
		if (x < mRecCurve.left) {
			return -1;
		}
		return 0;
	}

	protected float[] indexToPoint(ICharType iCharType, int index) {
		return indexToPoint(findCurve(iCharType, true), index);
	}

	protected void setupCanvas() {
		if (mCanvas == null)
			mCanvas = new Canvas();
		if (mCanvasBmp != null)
			mCanvasBmp.recycle();
		try {
			mCanvasBmp = Bitmap.createBitmap((int) mRecFrame.width(), (int) mRecFrame.height(),
					Config.ARGB_4444);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			mCanvasBmp = null;
			mCanvas = null;
			System.gc();
			return;
		}
		mCanvas.setBitmap(mCanvasBmp);
		mCanvas.setDensity(mCanvasBmp.getDensity());
	}

	protected void destoryCanvas() {
		if (mCanvasBmp != null) {
			mCanvasBmp.recycle();
			mCanvasBmp = null;
		}
		mCanvas = null;
	}

	protected boolean destory(boolean cleanData, boolean cleanCurve) {
		boolean result = cleanData;
		if (cleanData) {
			Iterator<Entry<ICharType, Curve>> list = mCurveList.entrySet().iterator();
			while (list.hasNext()) {
				result |= list.next().getValue().destory(cleanData);
			}
			mCtrlOpt.mMaxVisibleLen = 0;
			mCtrlOpt.mHasInitWithData = false;
		}
		if (cleanCurve) {
			mCurveList.clear();
			mSize = 0;
			mVisibleSize = 0;
			mCurve = null;
			mCtrlOpt.reset();
		}
		return result;
	}

	/*
	 * public static String valueFormat(float val, int decimal) { try { if (val
	 * == 0) { return " 0 "; } if (decimal > 0) { String format =
	 * "%1$.#f".replace("#", decimal + ""); String r = String.format(format,
	 * val); int len = r.length(); while (r.charAt(--len) == '0') ; if
	 * (r.charAt(len) == '.') { return r.substring(0, len); } else { return
	 * r.substring(0, ++len); } } } catch (Exception e) { } return val + ""; }
	 */

	public final class CtrlOption {
		protected float mOffsetX;
		final RectF mRect;
		public float mSpace;
		public float mOffsetRx;
		int mDataType = 0;
		public int mIOffset = 0;
		int mCapacity = -1;
		int mMaxVisibleLen = 0;
		boolean mHasInitWithData = false;
		public boolean mAnimStop = true;
		public float mAnimRate = -1;

		public CtrlOption(RectF rect) {
			mRect = rect;
		}

		void reset() {
			mOffsetX = 0;
			mOffsetRx = 0;
			mIOffset = 0;
			mCapacity = -1;
			mMaxVisibleLen = 0;
			mHasInitWithData = false;
		}

		public Rect getRectFrame() {
			return mRecFrame;
		}

		public float getXAt(int index) {
			return mRect.right - mOffsetRx - (index - mIOffset) * mSpace;
		}

		public CurveSetting getSetting() {
			return mSetting;
		}

		public float getBezierSmoonth() {
			return mSetting.mBezierSmooth;
		}

		public float getIndexOffset() {
			return mIOffset;
		}

		// -1,0,1
		int checkMoveAble(boolean right, boolean adjust) {
			int result = 0;
			if (right) {
				float maxOffset = (mMaxVisibleLen - 1) * mSpace - mRect.width() + mSpace / 2;
				if (mOffsetX < maxOffset) {
					result = 1;
				} else if (adjust) {
					mOffsetX = maxOffset;
					mIOffset = (int) (mOffsetX / mSpace);
					maxOffset = mOffsetX - mSpace * mIOffset;
					if (maxOffset > 0) {
						mIOffset++;
						mOffsetRx = mSpace - maxOffset;
					}

				}
			} else {
				if (mIOffset > 0 || (mIOffset == 0 && mOffsetRx < mSpace / 2)) {
					result = -1;
				} else if (adjust) {
					mIOffset = 0;
					mOffsetRx = mSpace / 2;
					mOffsetX = -mOffsetRx;
				}

			}
			return result;
		}

		// -1,0,1;
		int moveCurve(float dx) {
			int result = 0;
			if (dx != 0) {
				result = checkMoveAble(dx > 0, false);
				if (result != 0) {
					mOffsetX += dx;
					int preIndex = mIOffset;
					mIOffset = (int) (mOffsetX / mSpace);
					dx = mOffsetX - mSpace * mIOffset;
					if (dx > 0) {
						mIOffset++;
						mOffsetRx = mSpace - dx;

					} else {
						mIOffset = 0;
						mOffsetRx = -mOffsetX;
					}
					checkMoveAble(result > 0, true);
					result = (mIOffset != preIndex) ? 1 : -1;
				}
			}
			return result;
		}

		protected void initSpace(boolean mEnablePadRight, boolean showAllData) {
			float initSpace = 0;
			if (mEnablePadRight) {
				initSpace = (mSetting.MAX_SPACE + mSetting.MIN_SPACE) / 2;
				if (!mHasInitWithData) {
					if (showAllData) {
						if (mMaxVisibleLen > 0) {
							mSpace = mRect.width() / (Math.max(mMaxVisibleLen - 1, 1));
							mSpace = Math.max(Math.min(mSetting.MAX_SPACE, mSpace),
									mSetting.MIN_SPACE);
							mHasInitWithData = true;
						} else {
							mHasInitWithData = false;
							mSpace = initSpace;
						}
					} else {
						mSpace = initSpace;
					}
					initSpace = (mMaxVisibleLen - 1) * mSpace;
					if (mSetting.mEnableInitCent && initSpace < mRect.width()) {
						mOffsetX = ((mMaxVisibleLen - 1) * mSpace - mRect.width()) / 2;
						mIOffset = (int) (mOffsetX / mSpace);
						mOffsetRx = -mOffsetX % mSpace;
					} else {
						if (mSetting.mEnableInitOffset) {
							mOffsetRx = mSpace / 2;
							mOffsetX = -mOffsetRx;
						} else {
							mOffsetRx = mOffsetX = 0;
						}
					}
					mCapacity = (int) (mRect.width() / mSpace);
				}
			} else {
				if (!mHasInitWithData) {
					initSpace = (mSetting.MAX_SPACE + mSetting.MIN_SPACE) / 3;
					if (showAllData) {
						if (mMaxVisibleLen > 1) {
							mSpace = mRect.width() / (mMaxVisibleLen - 1);
							mSpace = Math.max(Math.min(mSetting.MAX_SPACE, mSpace),
									mSetting.MIN_SPACE);
							mHasInitWithData = true;
						} else {
							mSpace = initSpace;
							mHasInitWithData = false;
						}
					} else {
						mSpace = initSpace;
					}
					initSpace = (mMaxVisibleLen - 1) * mSpace;
					if (mSetting.mEnableInitCent && initSpace < mRect.width()) {
						mOffsetX = ((mMaxVisibleLen - 1) * mSpace - mRect.width()) / 2;
						mIOffset = (int) (mOffsetX / mSpace);
						mOffsetRx = -mOffsetX % mSpace;
					} else {
						if (mSetting.mEnableInitOffset) {
							mOffsetRx = mSpace / 2;
							mOffsetX = -mOffsetRx;
						} else {
							mOffsetRx = mOffsetX = 0;
						}
					}
					mCapacity = (int) (mRect.width() / mSpace) + 1;
				}
			}
		}

		void initSpace(boolean mEnablePadRight, int size, boolean showAllData) {
			if (mMaxVisibleLen < size) {
				mMaxVisibleLen = size;
				if (!mRect.isEmpty()) {
					initSpace(mEnablePadRight, showAllData);
				} else {
				}
			} else if (mCapacity == -1 && !mRect.isEmpty()) {
				initSpace(mEnablePadRight, showAllData);
			}
		}

		boolean adjustSpace(boolean mEnablePadRight, float newSpace) {
			boolean modified = false;
			newSpace = Math.max(mSetting.MIN_SPACE, Math.min(newSpace, mSetting.MAX_SPACE));

			if (mSpace != newSpace) {
				mSpace = newSpace;
				modified = true;
			}
			if (mEnablePadRight) {
				if (modified == false) {
				} else {
					mCapacity = (int) (mRect.width() / mSpace);
				}
			} else {
				if (modified == false) {
				} else {
					mCapacity = (int) (mRect.width() / mSpace) + 1;
				}
			}
			if (modified) {
				mIOffset = (int) (mOffsetX / mSpace);
				float dx = mOffsetX - mSpace * mIOffset;
				if (dx > 0) {
					mIOffset++;
					mOffsetRx = mSpace - dx;

				} else {
					mIOffset = 0;
					mOffsetRx = -mOffsetX;
				}
			}
			return modified;
		}

		public boolean drawCoordTxt(Canvas g, ICharType type, ICharData data, RectF rect,
				boolean isX) {
			if (!mSetting.mEnableCoordTxtX) {
				Paint paint = mPaint;
				paint.setTextSize(mSetting.mCoordTxtXSize);
				paint.setColor(mSetting.mCoordTxtXColor);
				float offset = ViewUtils.getTxtHeight(mSetting.mCoordTxtXSize) / 2
						- ViewUtils.getTxtCenterVerticalOffset(mSetting.mCoordTxtXSize) + 3;
				g.drawText(data.getTime("MM/dd"), rect.centerX(), mRect.bottom + offset
						+ mSetting.mCoordSize, paint);
			}
			return !mSetting.mEnableCoordTxtX;
		}

		public void drawPathShade(Canvas g, Path path, RectF recLinear, Rect rectBounds,
				int startColor, int endColor) {
			ShapeDrawable shaper = new ShapeDrawable(new PathShape(path, recLinear.width(),
					recLinear.height()));
			LinearGradient linear = new LinearGradient(recLinear.left, recLinear.top,
					recLinear.right, recLinear.bottom, startColor, endColor, TileMode.CLAMP);
			((ShapeDrawable) shaper).getPaint().setShader(linear);
			((ShapeDrawable) shaper).getPaint().setDither(true);
			shaper.setBounds(rectBounds);
			shaper.draw(g);
		}
	}

}
