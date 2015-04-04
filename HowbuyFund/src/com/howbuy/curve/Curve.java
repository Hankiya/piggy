package com.howbuy.curve;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.howbuy.curve.CurveControl.CtrlOption;

/**
 * this class for draw curve ,and can be extends for custom draw
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */

public class Curve {
	protected int mLen = 0;
	protected float mScale = 1;
	protected float mMinCoord = 0, mMinData = 0;
	protected float mMaxCoord = 1, mMaxData = 1;
	protected boolean mVisible = true;
	protected ICharType mType = null;
	protected Path mPath = new Path();
	protected final RectF mRect = new RectF();
	protected final RectF mRecTemp = new RectF();
	protected final CtrlOption mCtrlOpt;
	protected final ArrayList<ICharData> mData = new ArrayList<ICharData>();
	protected final CharDataType mTempData = new CharDataType(null, null);

	public Curve(CtrlOption ctrlOpt, RectF rect, ArrayList<? extends ICharData> data, ICharType type) {
		this.mCtrlOpt = ctrlOpt;
		this.mRect.set(rect);
		this.mType = type;
		if (data != null) {
			mData.addAll(data);
		}
		this.mLen = mData.size();

	}

	public ICharType getType() {
		return mType;
	}

	public ArrayList<ICharData> getData() {
		return mData;
	}

	public int size() {
		return mLen;
	}

	public boolean isVisible() {
		return mVisible;
	}

	public void setVisible(boolean visible) {
		this.mVisible = visible;
	}

	public float getYAt(int index) {
		float result = -1;
		CharDataType data = getDataAt(index);
		if (data != null) {
			result = getYAt(data.mData);
		}

		return result;
	}

	public float getYAt(ICharData data) {
		float result = -1;
		if (data != null) {
			result = mRect.top - mScale * (data.getValue(mCtrlOpt.mDataType) - mMaxCoord);
		}

		return result;
	}

	/**
	 * @return null if the index of value is not found.
	 */
	public CharDataType getDataAt(int index) {
		CharDataType dataType = null;
		mTempData.clear();
		if (index >= 0 && index < mLen) {
			mTempData.set(mData.get(index), mType);
			dataType = mTempData;
		}

		return dataType;
	}

	protected boolean addData(ArrayList<? extends ICharData> datas, boolean end) {
		boolean result = false;
		int n = datas == null ? 0 : datas.size();
		if (n > 0) {
			if (end) {
				result = mData.addAll(datas);
			} else {
				result = mData.addAll(0, datas);
			}
			mLen = mData.size();
		}

		return result;
	}

	protected boolean removeData(int index) {
		boolean result = false;
		if (index >= 0 && index < mLen) {
			if (mData.remove(index) != null) {
				mLen--;
				result = true;
			}
		}
		return result;
	}

	protected boolean removeData(int from, int to) {
		boolean result = false;
		from = Math.max(0, from);
		to = Math.min(to, mLen - 1);
		if (from <= to) {
			int preLen = mLen;
			for (int i = to; i >= from; i--) {
				if (mData.remove(i) != null) {
					mLen--;
				}
			}
			result = preLen != mLen;
		}
		return result;
	}

	protected int[] adjustStartAdEnd() {
		int[] se = new int[2];
		se[0] = Math.max(mCtrlOpt.mIOffset - 2, 0);
		se[1] = Math.min(mCtrlOpt.mCapacity + mCtrlOpt.mIOffset + 2, mLen);
		return se;
	}

	public float getMin() {
		return mMinData;
	}

	public float getMax() {
		return mMaxData;
	}

	/**
	 * calculate the max and min value of the curve datas
	 * 
	 * @param @param val
	 * @param @param visible true to take into account.
	 * @return float
	 * @throws
	 */
	protected float minMax(float val, boolean visible) {

		if (visible) {
			return minMax(val);
		}
		return val;
	}

	/**
	 * calculate the max and min value of the curve datas
	 */
	protected float minMax(float val) {
		mMinCoord = mMinData = Math.min(mMinData, val);
		mMaxCoord = mMaxData = Math.max(mMaxData, val);

		return val;
	}

	/**
	 * calculate the max and min value of the curve datas
	 */
	protected void minMax(float min, float max) {
		mMinCoord = mMinData = Math.min(mMinData, min);
		mMaxCoord = mMaxData = Math.max(mMaxData, max);

	}

	/**
	 * set the min and max to a appointed value .
	 */
	protected void setMinMax(float min, float max) {
		mMinCoord = min;
		mMaxCoord = max;
		calcuScale(true);

	}

	/**
	 * check min and max value.
	 * 
	 * @return correct to return 0; if min equal max return 1 ,then if both are
	 *         init value return 2;
	 */
	protected int checkMinMax() {
		int code = 0;
		if (mMinData == mMaxData) {
			code |= 1;
		}
		if (mMinData == Integer.MAX_VALUE || mMaxData == Integer.MIN_VALUE) {
			code |= 2;
		}

		return code;
	}

	/**
	 * compute the scale on the screen .call this only when max and min value
	 * have been calculated and adjusted.
	 * 
	 * @param @param scale 0 to recompute ,otherwise to assignment the scale a
	 *        new value.
	 */
	protected float calcuScale(boolean apply) {
		float result = (float) mRect.height() / (mMaxCoord - mMinCoord);
		if (apply) {
			mScale = result;
		}

		return result;
	}

	/**
	 * reset the min and max value to init state.
	 */
	protected void resetMinMax(int code) {

		if (code != 0) {
			if (code < 0) {
				mMinCoord = mMinData = Integer.MAX_VALUE;
				mMaxCoord = mMaxData = Integer.MIN_VALUE;
			} else if (code == 1) {
				mMinCoord *= 0.95f;
				mMaxCoord *= 1.05f;
			} else if (code == 2) {
				mMinCoord = 0;
				mMaxCoord = 1;
			}
		}
	}

	/**
	 * when data set changed
	 * 
	 * @param @param calScale true to calculate new scale value for translate y
	 *        to screen.
	 */
	public void whenDataChanged(boolean calScale, boolean calcuVisible) {
		if (mLen > 0) {
			resetMinMax(-1);
			int start = 0, end = mLen;
			if (calcuVisible) {
				start = Math.max(mCtrlOpt.mIOffset - 1, 0);
				end = Math.min(mCtrlOpt.mIOffset + mCtrlOpt.mCapacity + 1, mLen);
			}

			for (int i = start; i < end; i++) {
				minMax(mData.get(i).getValue(mCtrlOpt.mDataType));
			}
			resetMinMax(checkMinMax());
			if (calScale) {
				calcuScale(true);
			}
		}
	}

	/**
	 * set the draw bounds and recompute the scale factor.
	 * 
	 * @param @param rect new bounds.
	 */
	public void setBounds(RectF rect) {

		this.mRect.set(rect);
		calcuScale(true);
	}

	/**
	 * draw curve on the buffered canvas which relate to a bitmap.
	 */
	public void draw(Canvas g, Paint paint) {
		paint.setColor(mType.getColor(0));
		paint.setStyle(Paint.Style.STROKE);
		mPath.reset();
		g.save();
		mRecTemp.set(mRect);
		if (!mCtrlOpt.mAnimStop) {
			mRecTemp.right *= mCtrlOpt.mAnimRate;
		}
		g.clipRect(mRecTemp);
		drawCurve(g, paint, mCtrlOpt.mSpace, mCtrlOpt.mOffsetRx);
		g.restore();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	protected void drawCurve(Canvas g, Paint paint, float dx, float padR) {
		int[] se = adjustStartAdEnd();
		float x = mRect.right - 1 - padR + (mCtrlOpt.mIOffset - se[0]) * dx, right = x;
		float val0 = getYAt(se[0]);
		if (mLen > 1) {
			paint.setPathEffect(new CornerPathEffect(10f));
			mPath.moveTo(x, val0);
			x -= dx;
			for (int i = se[0] + 1; i < se[1]; i++, x -= dx) {
				mPath.lineTo(x, getYAt(i));
			}
			g.drawPath(mPath, paint);
			mPath.lineTo(x + dx, mRect.bottom);
			mPath.lineTo(right, mRect.bottom);
			drawPathShade(g, paint, x + dx, right);
			paint.setPathEffect(null);
		} else {
			g.drawCircle(x, val0, 2, paint);
		}
	}

	protected void drawPathShade(Canvas g, Paint paint, float lx, float rx) {
	}

	public boolean destory(boolean cleanData) {

		if (cleanData) {
			mData.clear();
			mLen = 0;
			return cleanData;
		}
		return false;
	}

}
