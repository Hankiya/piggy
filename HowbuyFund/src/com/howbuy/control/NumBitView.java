package com.howbuy.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.howbuy.lib.control.AbsView;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.lib.utils.ViewUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-4-04 下午4:27:22
 */
public class NumBitView extends AbsView {
	public static final int SLID_DIF_BIT_ONLY = FLAG_BASE;
	public static final int SLID_DIF_FIRST_ALL = FLAG_BASE << 1;
	public static final int SLID_ALL = FLAG_BASE << 2;
	public static final int SLID_ALL_IF_BITNUM_DIF = FLAG_BASE << 3;
	private static final int ANIM_SLID = FLAG_BASE << 4;
	private static final int ANIM_MASK_IN = FLAG_BASE << 5;
	private static final int ANIM_MASK_OUT = FLAG_BASE << 6;
	private float mPreValue, mCurValue;
	private String mFormatStyle = "00000.00";
	private Bitmap mBitmap = null;
	private Canvas mCanvas = null;
	private boolean mAnimMask = true, mInitValue = false;
	private float mTxtStroke;
	private final Rect mTxtMaxBounds = new Rect();
	private int mGravity = Gravity.CENTER;
	private char[] mPreChars, mCurChars;
	private int mPreCharLen, mCurCharLen;
	private float mDrawPos[], mDrawWidth;
	private String mDefText = "----.--";

	public void setDefText(String text) {
		mDefText = text;
	}

	public int getSlidMode() {
		return mFlag & (SLID_DIF_BIT_ONLY | SLID_DIF_FIRST_ALL | SLID_ALL);
	}

	public void setSlidMode(int slidmode) {
		int slid = slidmode & (SLID_DIF_BIT_ONLY | SLID_DIF_FIRST_ALL | SLID_ALL);
		if (slid != 0) {
			subFlag(getSlidMode());
			addFlag(slidmode);
		}
	}

	public NumBitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mAnimRate = -10000;
		mPaint.setColor(0xff000000);
		mPaint.setTextAlign(Align.LEFT);
		setTextSize(20);
		setInterpolator(new AccelerateDecelerateInterpolator());
	}

	public void setTextSizeRaw(float size) {
		mPaint.setTextSize(size);
		mTxtStroke = mPaint.getStrokeWidth();
		mPaint.getTextBounds(mFormatStyle, 0, mFormatStyle.length(), mTxtMaxBounds);
		if (!mRecFrame.isEmpty()) {
			destoryCanvas();
			setupCanvas();
		}
	}

	public void setTxtStrokeRaw(float stroke) {
		mPaint.setStrokeWidth(stroke);
		mTxtStroke = stroke;
	}

	public void setTxtStroke(float stroke) {
		setTxtStrokeRaw(stroke * mDensity);
	}

	public void setTextSize(float size) {
		setTextSizeRaw(SysUtils.getDimension(getContext(), TypedValue.COMPLEX_UNIT_SP, size));
	}

	public void setTextColor(int color) {
		mPaint.setColor(color);
	}

	public void setTextGravity(int gravity) {
		mGravity = gravity;
	}

	public void setAnimMask(boolean animMask) {
		mAnimMask = animMask;
	}

	public boolean setCurrentNum(float num, int duration) {
		if (mCurValue != num) {
			mCurValue = num;
			if (!mInitValue) {
				mPreValue = mCurValue;
				mInitValue = true;
			}
			if (duration <= 0) {
				if (mCurValue != mPreValue) {
					mPreValue = mCurValue;
					notifyDataChanged(isCanvasVisible(), true);
				}
			} else {
				notifyDataChanged(false, true);
				if (!startAnim(ANIM_SLID, duration, 0, false, false)) {
					stopAnim(false);
					startAnim(ANIM_SLID, duration, 0, false, false);
				}
			}
			return true;
		}
		return false;
	}

	public void setMaxBitAndDecimal(int maxBit, int decimal) {
		maxBit = Math.min(Math.max(maxBit, 1), 20);
		decimal = Math.min(Math.max(decimal, 0), maxBit - 1);
		StringBuffer sb = new StringBuffer(maxBit + 1);
		int it = maxBit - decimal;
		for (int i = 0; i < it; i++) {
			sb.append(0);
		}
		if (decimal > 0) {
			sb.append('.');
			for (int i = 0; i < decimal; i++) {
				sb.append(0);
			}
		}
		String newFormat = sb.toString();
		if (!newFormat.equals(mFormatStyle)) {
			mFormatStyle = newFormat;
			setTextSizeRaw(mPaint.getTextSize());
		}
		mFormatStyle = sb.toString();
		notifyDataChanged(isCanvasVisible(), true);
	}

	private void setupCanvas() {
		if (mBitmap == null && !mTxtMaxBounds.isEmpty()) {
			mBitmap = Bitmap.createBitmap((int) mTxtMaxBounds.width(),
					(int) mTxtMaxBounds.height(), Config.ARGB_4444);
			mTxtMaxBounds.offsetTo(0, 0);
			mCanvas = new Canvas(mBitmap);
		}
	}

	private void destoryCanvas() {
		if (mBitmap != null) {
			if (!mBitmap.isRecycled()) {
				mBitmap.recycle();
			}
			mBitmap = null;
		}
		mCanvas = null;
	}

	private void setMaskFilter() {
		mPaint.setMaskFilter(null);
		if (hasFlag(ANIM_MASK_IN)) {
			float mask = (float) (mTxtStroke * 3 * Math.max(0.001, formatAnimRate(mAnimRate, true)));
			mPaint.setMaskFilter(new BlurMaskFilter(mask, BlurMaskFilter.Blur.SOLID));
		} else if (hasFlag(ANIM_MASK_OUT)) {
			float mask = (float) (mTxtStroke * 3 * Math
					.max(0.001, formatAnimRate(mAnimRate, false)));
			mPaint.setMaskFilter(new BlurMaskFilter(mask, BlurMaskFilter.Blur.SOLID));
		}
	}

	private void adjustCanvas(boolean start) {
		if (start) {
			float transX = 0, transY = 0;
			if (!mInitValue) {
				if (mDefText != null) {
					mDrawWidth = mPaint.measureText(mDefText);
				} else {
					mDrawWidth = 0;
				}
			}
			mCanvas.save();
			if (mGravity == Gravity.RIGHT) {
				transX += mTxtMaxBounds.width() - mDrawWidth;
			} else if (mGravity == Gravity.CENTER) {
				transX += (mTxtMaxBounds.width() - mDrawWidth) / 2;
			}
			if (transX != 0 || transY != 0) {
				mCanvas.translate(transX, transY);
			}
		} else {
			mCanvas.restore();
		}
	}

	private void drawUnSlidChar(float x, float y) {
		int startCur = mDrawPos.length - mCurCharLen;
		int n = mDrawPos.length;
		for (int i = 0; i < n; i++) {
			if (i >= startCur) {
				mCanvas.drawText(mCurChars, i, 1, x, y, mPaint);
			}
			x += mDrawPos[i];
		}
	}

	private void drawSlidAllChar(float x, float y, int startCur, int startPre) {
		float texH = mTxtMaxBounds.height() + mPaint.descent() / 2;
		float up = texH * mAnimRate, down = texH - up;
		int color = mPaint.getColor();
		int n = mDrawPos.length;
		for (int i = 0; i < n; i++) {
			if (i >= startCur) {
				mPaint.setColor(ViewUtils.color(color, mAnimRate, 0));
				mCanvas.drawText(mCurChars, i, 1, x, y + down, mPaint);
			}
			if (i >= startPre) {
				mPaint.setColor(ViewUtils.color(color, 1 - mAnimRate, 0));
				mCanvas.drawText(mPreChars, i, 1, x, y - up, mPaint);
			}
			x += mDrawPos[i];
		}
		mPaint.setColor(color);
	}

	private void drawSlidDifOnely(float x, float y, int startCur, int startPre) {
		float texH = mTxtMaxBounds.height() + mPaint.descent() / 2;
		float up = texH * mAnimRate, down = texH - up;
		int color = mPaint.getColor();
		int n = mDrawPos.length;
		for (int i = 0; i < n; i++) {
			if (mCurChars[i] == mPreChars[i]) {
				if (i >= startCur) {
					mPaint.setColor(color);
					mCanvas.drawText(mCurChars, i, 1, x, y, mPaint);
				}
			} else {
				if (i >= startCur) {
					mPaint.setColor(ViewUtils.color(color, mAnimRate, 0));
					mCanvas.drawText(mCurChars, i, 1, x, y + down, mPaint);
				}
				if (i >= startPre) {
					mPaint.setColor(ViewUtils.color(color, 1 - mAnimRate, 0));
					mCanvas.drawText(mPreChars, i, 1, x, y - up, mPaint);
				}
			}
			x += mDrawPos[i];
		}
		mPaint.setColor(color);
	}

	private void drawSlidDifAfter(float x, float y, int startCur, int startPre) {
		float texH = mTxtMaxBounds.height() + mPaint.descent() / 2;
		float up = texH * mAnimRate, down = texH - up;
		int color = mPaint.getColor();
		int n = mDrawPos.length;
		int firstDif = findFirstDif(mCurChars, mPreChars, n);
		for (int i = 0; i < n; i++) {
			if (i < firstDif) {// all bit before firstDif is the same.
				if (i >= startCur) {
					mPaint.setColor(color);
					mCanvas.drawText(mCurChars, i, 1, x, y, mPaint);
				}
			} else {
				if (i >= startCur) {
					mPaint.setColor(ViewUtils.color(color, mAnimRate, 0));
					mCanvas.drawText(mCurChars, i, 1, x, y + down, mPaint);
				}
				if (i >= startPre) {
					mPaint.setColor(ViewUtils.color(color, 1 - mAnimRate, 0));
					mCanvas.drawText(mPreChars, i, 1, x, y - up, mPaint);
				}
			}
			x += mDrawPos[i];
		}
		mPaint.setColor(color);
	}

	private void drawSlidChar(float x, float y) {
		int startCur = mDrawPos.length - mCurCharLen;
		int startPre = mDrawPos.length - mPreCharLen;
		boolean hasBitNumDif = hasFlag(SLID_ALL_IF_BITNUM_DIF);
		if (hasBitNumDif || hasFlag(SLID_ALL)) {
			if (startCur != startPre || !hasBitNumDif) {
				drawSlidAllChar(x, y, startCur, startPre);
				return;
			}
		}
		if (hasFlag(SLID_DIF_BIT_ONLY)) {
			drawSlidDifOnely(x, y, startCur, startPre);
		} else {
			drawSlidDifAfter(x, y, startCur, startPre);
		}
	}

	private void drawCache() {
		if (!mTxtMaxBounds.isEmpty() && mCanvas != null) {
			adjustCanvas(true);
			float x = 0, y = mTxtMaxBounds.bottom;
			if (mDrawPos != null) {
				mBitmap.eraseColor(0);
				setMaskFilter();
				if (hasFlag(ANIM_SLID)) {
					drawSlidChar(x, y);
				} else {
					drawUnSlidChar(x, y);
				}
			} else {
				if (mDefText != null) {
					mCanvas.drawText(mDefText, x, y, mPaint);
				}
			}
			adjustCanvas(false);
		}
	}

	@Override
	protected void onFrameSizeChanged(boolean fromUser) {
		// destoryCanvas();
		// setupCanvas();
	}

	@Override
	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		boolean measued = fromUser && mInitValue;
		if (measued) {
			String pre = StrUtils.formatF(mPreValue, mFormatStyle);
			String cur = StrUtils.formatF(mCurValue, mFormatStyle);
			int difPre = pre.length() - mFormatStyle.length();
			int difCur = cur.length() - mFormatStyle.length();
			if (difCur > 0) {
				cur = cur.substring(difCur);
			}
			if (difPre > 0) {
				pre = pre.substring(difPre);
			}
			calCharAndWidth(cur, pre, mFormatStyle.length());
		}
		if (!mRecFrame.isEmpty()) {
			drawCache();
		}
		if (needInvalidate) {
			invalidate();
		}
		return measued;
	}

	private final int findFirstDif(char[] cur, char[] pre, int n) {
		int offset = 0;
		while (offset < n && cur[offset] == pre[offset])
			++offset;
		return offset;
	}

	/*
	 * return len for all is zero.then return between start and len and include
	 * start.
	 */
	private final int findFirstUnZero(String str, int start, int len, boolean ignorePoint) {
		while (start < len && str.charAt(start++) == '0')
			;
		if (str.charAt(--start) == '.') {
			if (ignorePoint) {
				start++;
				while (start < len && str.charAt(start++) == '0')
					;
			}
		}
		return start;
	}

	/*
	 * return len for all is zero.then return between start and len and include
	 * start.
	 */
/*	private final int findFirstUnZero(char[] str, int start, int len, boolean ignorePoint) {
		while (start < len && str[start++] == '0')
			;
		if (str[--start] == '.') {
			if (ignorePoint) {
				start++;
				while (start < len && str[start++] == '0')
					;
			}
		}
		return start;
	}*/

	private void calCharAndWidth(String cur, String pre, int n) {
		int noZeroCur = findFirstUnZero(cur, 0, n, true);
		int noZeroPre = findFirstUnZero(pre, 0, n, true);
		mPreCharLen = n - noZeroPre;
		mCurCharLen = n - noZeroCur;
		int maxCharLen = Math.max(mPreCharLen, mCurCharLen);
		mCurChars = new char[maxCharLen];
		mPreChars = new char[maxCharLen];
		mDrawPos = new float[maxCharLen];
		int startCur = maxCharLen - mCurCharLen;
		int startPre = maxCharLen - mPreCharLen;
		cur.getChars(noZeroCur, n, mCurChars, startCur);
		pre.getChars(noZeroCur, n, mPreChars, startPre);
		while (--startCur >= 0) {
			mCurChars[startCur] = '0';
		}
		while (--startPre >= 0) {
			mPreChars[startPre] = '0';
		}
		mPaint.getTextWidths(mCurChars, 0, maxCharLen, mDrawPos);
		mDrawWidth = 0;
		// d("calCharAndWidth", "cur=" + cur + ",pre=" + pre + ", n=" + n);
		for (int i = 0; i < maxCharLen; i++) {
			mDrawWidth += mDrawPos[i];
			// d("calCharAndWidth",
			// String.format("w[%1$d]=%2$.2f,cur=%3$s,pre=%3$s", i, mDrawPos[i],
			// mCurChars[i], mPreChars[i]));
		}
		// d("calCharAndWidth", "curLen=" + mCurCharLen + ",preLen=" +
		// mPreCharLen + ",mDrawWidth=" + mDrawWidth);
	}

	public void onDraw(Canvas can) {
		if (mBitmap != null) {
			can.drawBitmap(mBitmap, mRecFrame.left, mRecFrame.top, null);
		}
	}

	/*
	 * public static int resolveSize(int size, int measureSpec) { int result =
	 * size; int specMode = MeasureSpec.getMode(measureSpec); int specSize =
	 * MeasureSpec.getSize(measureSpec); switch (specMode) { case
	 * MeasureSpec.UNSPECIFIED: result = size; break; case MeasureSpec.AT_MOST:
	 * result = Math.min(size, specSize); break; case MeasureSpec.EXACTLY:
	 * result = specSize; break; } return result; }
	 */

	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		// int measureWmode = MeasureSpec.getMode(wSpec);
		// int measureWSize = MeasureSpec.getSize(wSpec);
		// int measureHmode = MeasureSpec.getMode(hSpec);
		// int measureHSize = MeasureSpec.getSize(hSpec);
		if (!mTxtMaxBounds.isEmpty()) {
			setMeasuredDimension(
					resolveSize(
							(int) (mTxtMaxBounds.width() + getPaddingLeft() + getPaddingRight()),
							wSpec),
					resolveSize(
							(int) (mTxtMaxBounds.height() + getPaddingTop() + getPaddingBottom()),
							hSpec));
		} else {
			super.onMeasure(wSpec, hSpec);
		}
	}

	@Override
	public void onAnimChaged(View nullView, int type, int which, float val, float dval) {
		if (type == IAnimChaged.TYPE_CHANGED) {
			mAnimRate = val;
			drawCache();
			if (isCanvasVisible()) {
				invalidate();
			}
		} else if (type == IAnimChaged.TYPE_END) {
			mPreCharLen = mCurCharLen;
			mPreValue = mCurValue;
			mPreChars = mCurChars;
			mAnimRate = -10000;
			if (mAnimMask && hasFlag(ANIM_SLID)) {
				startAnim(ANIM_MASK_IN, 500, 0, false, false);
			}
			if (hasFlag(ANIM_MASK_IN)) {
				startAnim(ANIM_MASK_OUT, 500, 0, false, false);
			}
			subFlag(which);
			drawCache();
			if (isCanvasVisible()) {
				invalidate();
			}
		} else {
			addFlag(which);
		}
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		destoryCanvas();
		destroyDrawingCache();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		setupCanvas();
	}

}