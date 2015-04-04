package com.howbuy.lib.control.curveview;

/**
 * to draw a pie also can use google webview such as:
 WebView webView = new WebView(this);
 String url = "http://chart.apis.google.com/chart?cht=p3&chs=300x150&chd=t:30,60,10";
 webView.loadUrl(url);
 setContentView(webView);
 * onAttachToWindow>onMeasure>onSizeChaged>onSurfaceCreated>onSufaceChaged>onSufaceDestoryed>onDetachedFromWindow.
 * everytime when onSizeChanged will call onSurfaceChanged.
 */
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.howbuy.lib.control.AbsView;
import com.howbuy.lib.entity.CharDataType;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-10 下午2:33:28
 */
public class CircleView extends AbsView {
	private float mRoateRate = 0.5f;
	private float mCenTxtSize = 18f;
	private float mIndicator = 90;
	private int mTxtColor = 0xffff00ff;
	public static int FLAG_ANIM_ALPHA = 1;
	public static int FLAG_ANIM_SCALE = 2;
	public static int FLAG_ANIM_SWEEP = 4;
	public static int FLAG_ANIM_ROATE = 8;
	public static int FLAG_ANIM_EXTEND_SELECTED = 16;
	public static int FLAG_ENABLE_INDICATOR = 32;
	public static int FLAG_ENABLE_SELECT = 64;
	public static int FLAG_ENABLE_KEEPSELECTED = 128;
	public static int FLAG_ENABLE_CENTXT = 256;
	private float mSum = 0;
	private float mDivider = 2;
	private int mSelected = -1;
	private float mOffset = 0, mBaseOffset = 0;
	private float mWdivH = 4 / 3f;
	private float mRoateCycle = 360;
	private float mInnerDivOut = 1 / 3f;
	private int mDividerColor = 0xaa777777;
	private int mBackGroudColor = /* 0x11ff0000 */0xffaa0000;
	private boolean mAdjustTangent = true;
	private boolean isIncrease = true;
	private int mLen = 0;
	private int mDataType = 0;
	private int mPreSelected = -1;
	private int mDownSelected = -1;
	private boolean mAnimStop = true;
	private final RectF mRecOutPie = new RectF();
	private final RectF mRecInnerPie = new RectF();
	private String mCenStr = null, mCenStrExtra = null;
	private ArrayList<CharDataType> mData = new ArrayList<CharDataType>();

	public void setIndicator(float indicator) {
		mIndicator = indicator % 360;
	}

	public float getIndicator() {
		return mIndicator;
	}

	public void setRoateRate(float rate) {
		mRoateRate = Math.max(Math.min(rate, 1), 0);
	}

	public float getRoateRate() {
		return mRoateRate;
	}

	public void setCenterText(String text, String extras) {
		mCenStr = text;
		mCenStrExtra = extras;
	}

	public void setTxtColor(int color) {
		mTxtColor = color;
	}

	public int getTxtColor() {
		return mTxtColor;
	}

	public void setTexSize(float size) {
		mCenTxtSize = size;
	}
    public float getTextSize(){
    	return mCenTxtSize;
    }

	public float getmDivider() {
		return mDivider;
	}

	public void setDivider(float mDivider) {
		this.mDivider = mDivider;
	}

	public float getmOffset() {
		return mOffset;
	}

	public void setOffset(float mOffset) {
		mBaseOffset = (360 + mOffset % 360) % 360;
	}

	public float getmWdivH() {
		return mWdivH;
	}

	public void setWdivH(float mWdivH) {
		this.mWdivH = mWdivH;
	}

	public float getmInnerDivOut() {
		return mInnerDivOut;
	}

	public void setInnerDivOut(float mInnerDivOut) {
		this.mInnerDivOut = mInnerDivOut;
	}

	public int getDividerColor() {
		return mDividerColor;
	}

	public void setDividerColor(int mDividerColor) {
		this.mDividerColor = mDividerColor;
	}

	public int getBackGroudColor() {
		return mBackGroudColor;
	}

	public void setBackGroudColor(int mBackGroudColor) {
		this.mBackGroudColor = mBackGroudColor;
	}

	public boolean isAdjustTangent() {
		return mAdjustTangent;
	}

	public void setAdjustTangent(boolean mAdjustTangent) {
		this.mAdjustTangent = mAdjustTangent;
	}

	public boolean isEnableExtendSelected() {
		return hasFlag(FLAG_ANIM_EXTEND_SELECTED);
	}

	public void setEnableExtendSelected(boolean mEnableExtend) {
		if (mEnableExtend) {
			addFlag(FLAG_ANIM_EXTEND_SELECTED);
		} else {
			subFlag(FLAG_ANIM_EXTEND_SELECTED);
		}
		requestLayout();
	}

	public boolean isEnableAlpah() {
		return hasFlag(FLAG_ANIM_ALPHA);
	}

	public void setEnableAlpah(boolean mEnableAlpah) {
		if (mEnableAlpah) {
			addFlag(FLAG_ANIM_ALPHA);
		} else {
			subFlag(FLAG_ANIM_ALPHA);
		}
	}

	public boolean isEnableRoate() {
		return hasFlag(FLAG_ANIM_ROATE);
	}

	public void setEnableRoate(boolean mEnableRoate) {
		if (mEnableRoate) {
			addFlag(FLAG_ANIM_ROATE);
		} else {
			subFlag(FLAG_ANIM_ROATE);
		}
	}

	public boolean isEnableScale() {
		return hasFlag(FLAG_ANIM_SCALE);
	}

	public void setEnableScale(boolean mEnableScale) {
		if (mEnableScale) {
			addFlag(FLAG_ANIM_SCALE);
		} else {
			subFlag(FLAG_ANIM_SCALE);
		}
	}

	public boolean isEnableSweep() {
		return hasFlag(FLAG_ANIM_SWEEP);
	}

	public void setEnableSweep(boolean mEnableSweep) {
		if (mEnableSweep) {
			addFlag(FLAG_ANIM_SWEEP);
		} else {
			subFlag(FLAG_ANIM_SWEEP);
		}
	}

	public boolean isEnableSelected() {
		return hasFlag(FLAG_ENABLE_SELECT);
	}

	public void setEnableSelected(boolean enable) {
		if (enable) {
			addFlag(FLAG_ENABLE_SELECT);
		} else {
			subFlag(FLAG_ENABLE_SELECT);
		}
	}

	public boolean isEnableIndicator() {
		return hasFlag(FLAG_ENABLE_INDICATOR);
	}

	public void setEnableIndicator(boolean enable) {
		boolean hasIndicator = hasFlag(FLAG_ENABLE_INDICATOR);
		if (enable) {
			addFlag(FLAG_ENABLE_INDICATOR);
		} else {
			subFlag(FLAG_ENABLE_INDICATOR);
		}
		if (!(hasIndicator && enable)) {
			requestLayout();
		}
	}

	public boolean isEnableKeepSelected() {
		return hasFlag(FLAG_ENABLE_KEEPSELECTED);
	}

	public void setEnableKeepSelected(boolean enable) {
		if (enable) {
			addFlag(FLAG_ENABLE_KEEPSELECTED);
		} else {
			subFlag(FLAG_ENABLE_KEEPSELECTED);
		}
	}

	public boolean isEnableCentTxt() {
		return hasFlag(FLAG_ENABLE_CENTXT);
	}

	public void setEnableCentTxt(boolean enable) {
		if (enable) {
			addFlag(FLAG_ENABLE_CENTXT);
		} else {
			subFlag(FLAG_ENABLE_CENTXT);
		}
	}

	public boolean isRateIncrease() {
		return isIncrease;
	}

	public void setRateIncrease(boolean increase) {
		this.isIncrease = increase;
	}

	public CircleView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		setFlag(FLAG_ANIM_ALPHA | FLAG_ANIM_SCALE | FLAG_ANIM_SWEEP | FLAG_ANIM_ROATE | FLAG_ANIM_EXTEND_SELECTED | FLAG_ENABLE_INDICATOR | FLAG_ENABLE_SELECT
				| FLAG_ENABLE_KEEPSELECTED | FLAG_ENABLE_CENTXT);
		mCenTxtSize *= mDensity;
		mPaint.setTextSize(mCenTxtSize);
	}

	public int color(int color) {
		return ViewUtils.color(color, Math.abs(mAnimRate), 2);
	}

	public CharDataType getData(int index) {
		if (index < 0 || index >= mLen) {
			return null;
		}
		return mData.get(index);
	}

	public ArrayList<CharDataType> getData() {
		return mData;
	}

	public CharDataType getSelectedData(boolean cur) {
		if (cur) {
			return getData(mSelected);
		} else {
			return getData(mPreSelected);
		}
	}

	public int size() {
		return mLen;
	}

	public int getSelectedIndex(boolean cur) {
		if (cur) {
			return mSelected;
		} else {
			return mPreSelected;
		}
	}

	public void clean(boolean needInvalidate) {
		synchronized (mData) {
			mData.clear();
			mLen = 0;
			mOffset = 0;
			mPreSelected = mSelected = -1;
		}
		if (needInvalidate) {
			notifyDataChanged(true, true);
		}
	}

	public void setData(ArrayList<CharDataType> data) {
		clean(data == null);
		if (data != null) {
			synchronized (mData) {
				mData.addAll(data);
			}
			notifyDataChanged(true, true);
		}
	}

	public void addData(CharDataType data, boolean isEnd) {
		if (data != null) {
			synchronized (mData) {
				if (isEnd) {
					mData.add(data);
				} else {
					mData.add(0, data);
				}
			}
			notifyDataChanged(true, true);
		}
	}

	public boolean remove(CharDataType data) {
		boolean result = false;
		synchronized (mData) {
			result = mData.remove(data);
		}
		if (result) {
			notifyDataChanged(true, true);
		}
		return result;
	}

	public CharDataType remove(int index) {
		CharDataType data = null;
		if (index >= 0 && index < mLen) {
			synchronized (mData) {
				data = mData.remove(index);
			}
		}
		if (data != null) {
			notifyDataChanged(true, true);
		}
		return data;
	}

	@Override
	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		mLen = mData.size();
		mSum = 0;
		float avaiableDegree = 360 - (mLen > 1 ? mDivider * mLen : 0);
		for (int i = 0; i < mLen; i++) {
			CharDataType data = mData.get(i);
			data.mWhat = data.mData.getValue(mDataType);
			mSum += data.mWhat;
		}
		if (mSum > 0) {
			for (int i = 0; i < mLen; i++) {
				CharDataType data = mData.get(i);
				data.mWhat = (data.mWhat / mSum) * avaiableDegree;
			}
			if (needInvalidate) {
				invalidate();
			}
			return true;
		} else {
			return false;
		}

	}

	@Override
	protected void onDraw(Canvas can) {
		super.onDraw(can);
		RectF recOut = mRecOutPie;
		RectF recIn = mRecInnerPie;
		float offset = mBaseOffset + mOffset;
		if (!mAnimStop) {
			if (hasFlag(FLAG_ANIM_ROATE)) {
				offset += mRoateCycle * mAnimRate;
			}
			if (hasFlag(FLAG_ANIM_SCALE)) {
				recOut = scaleRect(mRecOutPie);
				if (!mRecInnerPie.isEmpty()) {
					recIn = scaleRect(mRecInnerPie);
				}
			}
		}
		drawPies(can, recOut, recIn, offset);
	}

	@Override
	protected void onFrameSizeChanged(boolean fromUser) {
		if (mAdjustTangent) {
			mRecOutPie.set(mRecFrame);
		} else {
			float w = mRecFrame.width();
			float h = mRecFrame.height();
			float wh = w / h;
			if (wh > mWdivH) {
				w = (int) (h * mWdivH);
			} else if (wh < mWdivH) {
				h = (int) (w / mWdivH);
			}
			mRecOutPie.set(0, 0, w, h);
			ViewUtils.centRect(mRecOutPie, mRecFrame.centerX(), mRecFrame.centerY());
		}
		if (hasFlag(FLAG_ANIM_EXTEND_SELECTED)) {
			float a = mRecOutPie.width() / 25;
			float b = mRecOutPie.height() / 25;
			mRecOutPie.top += b;
			mRecOutPie.bottom -= b;
			mRecOutPie.left += a;
			mRecOutPie.right -= a;
		}
		if (mInnerDivOut > 0 && mInnerDivOut < 1) {
			ViewUtils.scaleRect(mRecInnerPie, mRecOutPie, mInnerDivOut);
		} else {
			mRecInnerPie.setEmpty();
		}
	}

	/**
	 * 顺时针90度为准线开始画.
	 * 
	 * @param @param can
	 * @param @param fromUser
	 * @return void
	 * @throws
	 */
	private void drawPies(Canvas can, RectF recOut, RectF rcIn, float offset) {
		mPaint.setAntiAlias(true);
		float sDegree = offset;
		for (int i = 0; i < mLen; i++) {
			CharDataType data = mData.get(i);
			int color = data.mData.getColor(i == mSelected);
			mPaint.setColor(!mAnimStop && hasFlag(FLAG_ANIM_ALPHA) ? color(color) : color);
			if (i == mSelected) {
				if (hasFlag(FLAG_ANIM_EXTEND_SELECTED)) {
					float a = recOut.width() / 50;
					float b = recOut.height() / 50;
					if (!mAnimStop) {
						float s = Math.abs(mAnimRate);
						a *= s;
						b *= s;
					}
					mRecTemp.set(recOut);
					mRecTemp.top -= b;
					mRecTemp.bottom += b;
					mRecTemp.left -= a;
					mRecTemp.right += a;
					drawArc(can, mRecTemp, sDegree, data.mWhat, offset, mPaint);
					sDegree += data.mWhat;
					if (mDivider > 0 && mLen > 1) {
						sDegree += mDivider;
					}
					continue;
				}
			}
			drawArc(can, recOut, sDegree, data.mWhat, offset, mPaint);
			sDegree += data.mWhat;
			if (mDivider > 0 && mLen > 1) {
				mPaint.setColor(!mAnimStop && hasFlag(FLAG_ANIM_ALPHA) ? color(mDividerColor) : mDividerColor);
				if (!(hasFlag(FLAG_ANIM_EXTEND_SELECTED) && ((i == mSelected - 1) || (mSelected == 0 && i == mLen - 1)))) {
					drawArc(can, recOut, sDegree, mDivider, offset, mPaint);
				}
				sDegree += mDivider;
			}
		}
		if (!rcIn.isEmpty()) {
			mPaint.setColor(mBackGroudColor);
			can.drawArc(rcIn, 0, 360, true, mPaint);
			if (hasFlag(FLAG_ENABLE_CENTXT)) {
				drawArcCenTxt(can, rcIn);
			}

		}
	}

	private void drawArcCenTxt(Canvas can, RectF rcIn) {
		int color = (!mAnimStop && hasFlag(FLAG_ANIM_ALPHA)) ? color(mTxtColor) : mTxtColor;
		mPaint.setColor(color);
		String str = null;
		boolean emp1 = StrUtils.isEmpty(mCenStr);
		boolean emp2 = StrUtils.isEmpty(mCenStrExtra);
		if (emp1 && emp2) {
			String sum = valueFormat(mSum, 2);
			if (mSelected == -1) {
				str = sum;
			} else {
				String sub = valueFormat(getData(mSelected).mData.getValue(mDataType), 2);
				str = sub + "/" + sum;
			}
		} else {
			if (emp1 || emp2) {
				str = emp2 ? mCenStr : mCenStrExtra;
			}
		}
		if (str == null) {
			float h = (ViewUtils.getTxtHeight(mCenTxtSize) * 2.4f);
			mRecTemp.set(rcIn);
			mRecTemp.inset(0, (rcIn.height() - h) / 2);
			drawArcCenTex(can, mRecTemp, mCenStr, mCenStrExtra);
		} else {
			drawArcCenTex(can, rcIn, str);
		}

	}

	private void drawArcCenTex(Canvas can, RectF rcIn, String str1, String str2) {
		float half = rcIn.height() / 2;
		rcIn.bottom -= half;
		drawArcCenTex(can, mRecTemp, str1);
		mRecTemp.top += half;
		mRecTemp.bottom += half;
		drawArcCenTex(can, mRecTemp, str2);
	}

	private void drawArcCenTex(Canvas can, RectF rcIn, String txt) {
		boolean measure = (!mAnimStop && hasFlag(FLAG_ANIM_SCALE));
		float txtSize = measure ? mCenTxtSize * mAnimRate : mCenTxtSize;
		mPaint.setTextSize(txtSize);
		can.drawText(txt, rcIn.centerX(), rcIn.centerY() - ViewUtils.getTxtCenterVerticalOffset(txtSize), mPaint);
	}

	private void drawArc(Canvas can, RectF rect, float start, float dDegree, float offset, Paint p) {
		if (mAnimStop || !hasFlag(FLAG_ANIM_SWEEP)) {
			can.drawArc(rect, start, dDegree, true, p);
		} else {
			float end = start + dDegree;
			float idearEnd = 360 * Math.abs(mAnimRate) + offset;
			end = Math.min(end, idearEnd);
			end = Math.max(start, end);
			if (start != end) {
				can.drawArc(rect, start, end - start, true, p);
			}
		}
	}

	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		int measureWmode = MeasureSpec.getMode(wSpec);
		int measureWSize = MeasureSpec.getSize(wSpec);
		int measureHmode = MeasureSpec.getMode(hSpec);
		int measureHSize = MeasureSpec.getSize(hSpec);
		if (measureWSize != 0 && measureHSize != 0) {
			if (mAdjustTangent) {
				int size = Math.min(measureWSize, measureHSize);
				float wh = measureWSize / measureHSize;
				if (wh > mWdivH) {
					measureWSize = (int) (measureHSize * mWdivH);
				} else if (wh < mWdivH) {
					measureHSize = (int) (measureWSize / mWdivH);
				}
				setMeasuredDimension(measureWSize, measureHSize);
			} else {
				// after call this ,the onSizeChanged will called. the
				setMeasuredDimension(measureWSize, measureHSize);
			}

		} else {
			super.onMeasure(wSpec, hSpec);
		}

	}

	private RectF scaleRect(RectF recIn, float scale) {
		return ViewUtils.scaleRect(new RectF(), recIn, scale);
	}

	private RectF scaleRect(RectF recIn) {
		return scaleRect(recIn, Math.abs(mAnimRate));
	}

	@Override
	public boolean onViewFirstSteped(int w, int h) {
		return startAnimation(800, 50, true);
	}

	public boolean startAnimation(int duration, int period, boolean animFillAfter) {
		return startAnim(FLAG_NONE, duration, period, animFillAfter, false);
	}

	@Override
	public void onAnimChaged(View nullView, final int type, final int which, final float val, final float dval) {
		mAnimRate = formatAnimRate(val, isIncrease);
		if (IAnimChaged.TYPE_CHANGED == type) {
			invalidate();
		} else if (IAnimChaged.TYPE_START == type) {
			mAnimStop = false;
		} else {
			mAnimStop = true;
			if (which != FLAG_NONE) {
				mOffset += mRoateCycle;
				mOffset %= 360;
				restoreFlag();
			}

			if (!mAnimFillAfter) {
				invalidate();
			}
		}

		if (mListen != null) {
			mListen.onAnimChaged(this, type, which, val, dval);
		}

	}

	private float indexToRaidus(int index) {
		float result = mOffset;
		if (index >= 0 && index < mLen) {
			int n = index;
			float end = 0;
			for (int i = 0; i < n; i++) {
				end += mData.get(i).mWhat;
				if (mDivider > 0 && mLen > 1) {
					end += mDivider;
				}
			}
			result = end + mData.get(index).mWhat / 2 + mOffset + mBaseOffset;
		}
		return (360 + result % 360) % 360;
	}

	/**
	 * 返回对应角度处的数据索引.
	 * 
	 * @param @param raidus 顺时3点方向开始的角度
	 */
	private int raidusToIndex(float raidus) {
		raidus = (raidus - mOffset - mBaseOffset) % 360;
		if (raidus < 0) {
			raidus += 360;
		}
		float end = 0;
		for (int i = 0; i < mLen; i++) {
			end += mData.get(i).mWhat;
			if (end > raidus) {
				return i;
			}
			if (mDivider > 0 && mLen > 1) {
				end += mDivider;
			}
		}
		return -1;
	}

	private int pointToIndex(float x, float y) {
		return raidusToIndex(pointToRadius(x, y));
	}

	/**
	 * 返回顺时针方向,由3点钟方向开始的度数[0,360].
	 * 
	 * @param @param x
	 * @param @param y
	 * @param @return
	 * @return float
	 * @throws
	 */
	private float pointToRadius(float x, float y) {
		float dx = x - mRecOutPie.centerX();
		float dy = y - mRecOutPie.centerY();
		float raius = 0;
		if (dx == 0) {
			if (dy == 0)
				return -1;
			if (dy > 0) {
				raius = 90;
			} else {
				raius = 270;
			}
		} else {
			if (dx > 0) {
				raius = (float) (180 * Math.atan(dy / dx) / Math.PI);
				if (raius < 0) {
					raius += 360;
				}
			} else {
				raius = (float) (180 * Math.atan(dy / dx) / Math.PI);
				raius += 180;
			}
		}
		return raius;
	}

	private boolean pointInPie(float x, float y, RectF rect) {
		if (rect.contains(x, y)) {
			float dx = x - rect.centerX();
			float dy = y - rect.centerY();
			float a = rect.width() / 2;
			float b = rect.height() / 2;
			return dx * dx / (a * a) + dy * dy / (b * b) < 1;
		}
		return false;
	}

	private boolean pointInPie(float x, float y) {
		if (mRecInnerPie.isEmpty()) {
			return pointInPie(x, y, mRecOutPie);
		} else {
			return pointInPie(x, y, mRecOutPie) && !pointInPie(x, y, mRecInnerPie);
		}
	}

	public boolean animToIndex(int index, boolean setSelected) {

		if (index < 0 || index >= mLen || (hasFlag(FLAG_ENABLE_KEEPSELECTED) && mPreSelected == index) || (!hasFlag(FLAG_ENABLE_KEEPSELECTED) && mPreSelected == index)) {

			return false;
		} else {
			if (setSelected) {
				mSelected = index;
			}
			float radius = indexToRaidus(index);
			float gap = Math.abs(radius - mIndicator);
			if (hasFlag(FLAG_ANIM_EXTEND_SELECTED)) {
				saveFlag(FLAG_ANIM_ROATE | FLAG_ANIM_EXTEND_SELECTED);
				addFlag(FLAG_ENABLE_CENTXT | FLAG_ENABLE_INDICATOR | FLAG_ENABLE_SELECT | FLAG_ENABLE_KEEPSELECTED);
			} else {
				saveFlag(FLAG_ANIM_ROATE);
				addFlag(FLAG_ENABLE_CENTXT | FLAG_ENABLE_INDICATOR | FLAG_ENABLE_SELECT | FLAG_ENABLE_KEEPSELECTED);
			}
			mRoateCycle = gap % 360;
			if (mRoateCycle > 180) {
				mRoateCycle = 360 - mRoateCycle;
			}
			isIncrease = true;
			if (radius > mIndicator) {
				if (gap < 180) {
					mRoateCycle = -mRoateCycle;
				}
			} else {
				if (gap > 180) {
					mRoateCycle = -mRoateCycle;
				}
			}
			boolean result = false;
			int duration = (int) Math.min((1 - mRoateRate) * Math.max(Math.abs(mRoateCycle) * 12, 200), 1800);
			if (duration == 0) {
				mOffset += mRoateCycle;
				mOffset %= 360;
				invalidate();
			} else {
				result = startAnim(FLAG_ANIM_ROATE, duration, 50, false, false);
			}
			if (!result) {
				restoreFlag();
			}
			return result;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (!isEnabled())
			return super.onTouchEvent(e);
		int action = MotionEvent.ACTION_MASK & e.getAction();
		float x = e.getX();
		float y = e.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			if (hasFlag(FLAG_ENABLE_SELECT) && mAnimStop && pointInPie(x, y)) {
				mPreSelected = mSelected == -1 ? mPreSelected : mSelected;
				mDownSelected = pointToIndex(x, y);
				if (mDownSelected != -1 && mSelected != mDownSelected) {
					mSelected = mDownSelected;
					invalidate();
				}
			}
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			if (mDownSelected != -1 && pointInPie(x, y)) {
				int select = pointToIndex(x, y);
				if (select != -1 && mSelected != select) {
					mSelected = select;
					invalidate();
				}
			}
		}
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if (mDownSelected != -1) {
				int select = -1;
				if (pointInPie(x, y)) {
					select = pointToIndex(x, y);
					if (select != -1) {
						mSelected = select;
						select = -1;
					}
				}
				if (mSelected != mPreSelected) {
					select = mSelected;
				}
				if (!hasFlag(FLAG_ENABLE_KEEPSELECTED)) {
					mSelected = -1;
					invalidate();
				}
				if (select != -1) {
					if (mListen != null) {
						mListen.onSelectedChaged(select, mPreSelected, hasFlag(FLAG_ENABLE_INDICATOR));
					}
					if (hasFlag(FLAG_ENABLE_INDICATOR)) {
						animToIndex(select, false);
					}
				}
				mDownSelected = -1;
			}
		}
			break;
		}

		return true;
	}

	public static String valueFormat(float val, int decimal) {
		try {
			if (val == 0) {
				return " 0 ";
			}
			if (decimal > 0) {
				String format = "%1$.#f".replace("#", decimal + "");
				String r = String.format(format, val);
				int len = r.length();
				while (r.charAt(--len) == '0')
					;
				if (r.charAt(len) == '.') {
					return r.substring(0, len);
				} else {
					return r.substring(0, ++len);
				}
			}
		} catch (Exception e) {
		}
		return val + "";
	}

	private ICircleEvent mListen = null;

	public void setCircleEvent(ICircleEvent l) {
		this.mListen = l;
	}

	public interface ICircleEvent extends IAnimChaged {
		public void onSelectedChaged(int cur, int pre, boolean hasAnim);
	}
}