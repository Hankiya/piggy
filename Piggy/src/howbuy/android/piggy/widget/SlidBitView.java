package howbuy.android.piggy.widget;

import howbuy.android.util.StringUtil;

import java.util.Arrays;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * @author rexy 840094530@qq.com
 * @date 2014-7-11 下午4:27:22
 */
public class SlidBitView extends View implements IAnimChaged {
	protected String TAG = null;
	private static final int ANIM_SLID = 1 << 1;
	public static final int ANIM_MASK_IN = 1 << 2;
	public static final int ANIM_MASK_OUT = 1 << 3;
	public static final int ANIM_FIRST_VISIBLE = 1 << 4;
	protected int mFlag;
	protected float mDensity, mAnimRate = -1;
	private float mPreValue = -1, mCurValue = 0;
	private String mFormatStyle = "00000.00";
	private final Rect mTxtMaxBounds = new Rect();
	private char[] mPreChars, mCurChars, mDesChars;
	private int mCharLen;
	private float mDrawPos[];
	private int mPreAnimTime = 0, mCurAnimTimes = 0;
	int mBitDuration = 200, mBitFrame = 0, mMaxBit = 0;

	private float mBitPadH = 10, mBitPadV = 5, mRadius = 5, mBitMargin = 3, mPointPad = 2;
	private Paint mPainBk = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected final Rect mRecFrame = new Rect();
	protected final RectF mRecTemp = new RectF();
	RateHelper mRateHelper = null;

	public SlidBitView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TAG = getClass().getSimpleName();
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mPaint.setStrokeWidth(mDensity);
		mPaint.setTextAlign(Align.CENTER);
		mAnimRate = -10000;
		mPaint.setColor(0xff000000);
		mPaint.setTextAlign(Align.LEFT);
		mBitPadH *= mDensity;
		mBitPadV *= mDensity;
		mRadius *= mDensity;
		mBitMargin *= mDensity;
		mPointPad *= mDensity;
		addFlag(ANIM_FIRST_VISIBLE);
		mPainBk.setColor(0xaaff0000);
		mRateHelper = new RateHelper(new Handler(), new LinearInterpolator(), this);
	}

	public void setBitArg(float bitPadH, float bitPadV, float pointPad, float bitMarg) {
		mBitPadH = bitPadH;
		mBitPadV = bitPadV;
		mPointPad = pointPad;
		mBitMargin = bitMarg;
		requestLayout();
	}

	public void setBitAnimDuration(int durantion) {
		mBitDuration = durantion;
	}

	public void setBitBackRoundRadius(float r) {
		mRadius = Math.max(Math.min(r * mDensity, Math.min(mBitPadH, mBitPadV)), 1);
		invalidate();
	}

	public void setTxtSizeRaw(float size) {
		mPaint.setTextSize(size);
		mPainBk.setTextSize(size);
		mPaint.getTextBounds(mFormatStyle, 0, mFormatStyle.length(), mTxtMaxBounds);
		float[] ws = new float[mFormatStyle.length()];
		mPaint.getTextWidths(mFormatStyle, 0, mFormatStyle.length(), ws);
		float sum = 0;
		for (int i = 0; i < mFormatStyle.length(); i++) {
			sum += ws[i];
		}
		if (sum > mTxtMaxBounds.width()) {
			mTxtMaxBounds.right += (sum - mTxtMaxBounds.width());
		}
		mTxtMaxBounds.offsetTo(0, 0);
		requestLayout();
	}

	public void setTxtStrokeRaw(float stroke) {
		mPaint.setStrokeWidth(stroke);
		mPainBk.setStrokeWidth(stroke);
	}

	public void setTxtStroke(float stroke) {
		setTxtStrokeRaw(stroke * mDensity);
	}

	public void setTxtSize(float size) {
		setTxtSizeRaw(size*(getContext().getResources().getDisplayMetrics().scaledDensity));
	}

	public void setColor(int textColor, int bitBackcolor) {
		mPaint.setColor(textColor);
		mPainBk.setColor(bitBackcolor);
		invalidate();
	}

	public boolean setCurrentNum(float num, boolean ignoreSame) {
		if (mCurValue != num || ignoreSame) {
			mPreValue = mCurValue;
			mCurValue = num;
			if (mCurValue != mPreValue || mMaxBit == 0) {
				notifyDataChanged(isCanvasVisible(), true);
			} else if (ignoreSame) {
				mPreAnimTime = 0;
				mCurAnimTimes = 0;
				for (int i = 0; i < mCharLen; i++) {
					char c = mDesChars[i];
					if (c != '.') {
						mPreChars[i] = '0';
					} else {
						mPreChars[i] = '.';
					}
				}
				prepareCurChar(1);
			}

			if (mBitDuration > 0 && isCanvasVisible()) {
				d("setCurrentNum", "duration=" + (mBitDuration * mMaxBit) + " ,maxBit=" + mMaxBit
						+ ",num=" + num);
				int period = Math.min(40, Math.max((int) Math.sqrt(mBitDuration * 1.5f), 15));
				if (!mRateHelper.start(ANIM_SLID, 1, mBitDuration * mMaxBit, period, false)) {
					mRateHelper.stop();
					mRateHelper.start(ANIM_SLID, 1, mBitDuration * mMaxBit, period, false);
				}
			} else {
				invalidate();
			}
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
			setTxtSizeRaw(mPaint.getTextSize());
		}
		mFormatStyle = sb.toString();
		notifyDataChanged(isCanvasVisible(), true);
	}

	private void setMaskFilter() {
		mPaint.setMaskFilter(null);
		if (hasFlag(ANIM_MASK_IN)) {
			float mask = (float) (mPaint.getStrokeWidth() * 3 * Math.max(0.001,
					formatAnimRate(mAnimRate, true)));
			mPaint.setMaskFilter(new BlurMaskFilter(mask, BlurMaskFilter.Blur.SOLID));
		} else if (hasFlag(ANIM_MASK_OUT)) {
			float mask = (float) (mPaint.getStrokeWidth() * 3 * Math.max(0.001,
					formatAnimRate(mAnimRate, false)));
			mPaint.setMaskFilter(new BlurMaskFilter(mask, BlurMaskFilter.Blur.SOLID));
		}
	}

	private void drawUnSlidChar(Canvas can, float x, float y) {
		int n = mDrawPos.length;
		mRecTemp.top = mRecFrame.top;
		mRecTemp.bottom = mRecFrame.bottom;
		for (int i = 0; i < n; i++) {
			boolean isPoint = mDesChars[i] == '.';
			mRecTemp.left = x;
			mRecTemp.right = mRecTemp.left + mDrawPos[i];
			if (isPoint) {
				mRecTemp.right += mPointPad + mPointPad;
				can.drawText(mDesChars, i, 1, mRecTemp.left + mPointPad - mDrawPos[i] / 5, y,
						mPainBk);
			} else {
				mRecTemp.right += mBitPadH + mBitPadH;
				x += mBitMargin;
				can.drawRoundRect(mRecTemp, mRadius, mRadius, mPainBk);
				can.drawText(mDesChars, i, 1, mRecTemp.left + mBitPadH, y, mPaint);
			}
			x += mRecTemp.width();
		}
	}

	private void drawSlidChar(Canvas can, float x, float y) {
		float texH = mTxtMaxBounds.height() + mPaint.descent() / 2;
		float up = texH * mAnimRate, down = texH - up;
		int color = mPaint.getColor();
		int n = mDrawPos.length;
		mRecTemp.top = mRecFrame.top;
		mRecTemp.bottom = mRecFrame.bottom;
		for (int i = 0; i < n; i++) {
			boolean isPoint = mDesChars[i] == '.';
			mRecTemp.left = x;
			mRecTemp.right = mRecTemp.left + mDrawPos[i];
			if (isPoint) {
				mRecTemp.right += mPointPad + mPointPad;
				can.drawText(mDesChars, i, 1, mRecTemp.left + mPointPad - mDrawPos[i] / 5, y,
						mPainBk);
			} else {
				mRecTemp.right += mBitPadH + mBitPadH;
				x += mBitMargin;
				can.drawRoundRect(mRecTemp, mRadius, mRadius, mPainBk);
				if (mCurChars[i] == mPreChars[i]) {
					// mPaint.setColor(color);
					can.drawText(mCurChars, i, 1, mRecTemp.left + mBitPadH, y, mPaint);
				} else {
					// mPaint.setColor(ViewUtils.color(color, mAnimRate, 0));
					can.drawText(mCurChars, i, 1, mRecTemp.left + mBitPadH, y + down, mPaint);
					// mPaint.setColor(ViewUtils.color(color, 1 - mAnimRate,
					// 0));
					can.drawText(mPreChars, i, 1, mRecTemp.left + mBitPadH, y - up, mPaint);
				}
			}
			x += mRecTemp.width();
		}
		mPaint.setColor(color);
	}

	public void onDraw(Canvas can) {
		super.onDraw(can);
		if (!mRecFrame.isEmpty()) {
			can.clipRect(mRecFrame);
			float x = mRecFrame.left, y = mRecFrame.centerY()
					- (mPaint.descent() - (mPaint.descent() - mPaint.ascent()) / 2);
			if (mDrawPos != null) {
				setMaskFilter();
				if (hasFlag(ANIM_SLID)) {
					drawSlidChar(can, x, y);
				} else {
					drawUnSlidChar(can, x, y);
				}
			}
		}
	}

	protected boolean onViewFirstSteped(int w, int h) {
		if (w > 0 && h > 0) {
			if (hasFlag(ANIM_FIRST_VISIBLE)) {
				postDelayed(new Runnable() {
					@Override
					public void run() {
						setCurrentNum(mCurValue, true);
					}
				}, 50);
				return true;
			}
		}
		return false;
	}

	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		boolean measued = fromUser;
		if (measued) {
//			String cur = StrUtils.formatF(mCurValue, mFormatStyle);
			String cur = StringUtil.formatFloat(mCurValue, mFormatStyle);
			int difCur = cur.length() - mFormatStyle.length();
			if (difCur > 0) {
				cur = cur.substring(difCur);
			}
			calCharAndWidth(cur, mFormatStyle.length());
		}
		if (needInvalidate) {
			invalidate();
		}
		return measued;
	}

	protected void computeCanvasRegion(int w, int h, boolean fromUser) {
		int pl = getPaddingLeft(), pr = getPaddingRight();
		int pt = getPaddingTop(), pb = getPaddingBottom();
		mRecFrame.set(pl, pt, w - pr, h - pb);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) {
			mRecFrame.setEmpty();
		} else {
			boolean firstInit = mRecFrame.isEmpty();
			computeCanvasRegion(w, h, false);
			notifyDataChanged(false, false);
			if (!firstInit || !onViewFirstSteped(w, h)) {
				invalidate();
			}
		}
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

	private void calCharAndWidth(String cur, int n) {
		int noZeroCur = Math.min(findFirstUnZero(cur, 0, n, false), n - 1);
		mMaxBit = mPreAnimTime = mCurAnimTimes = 0;
		mCharLen = n - noZeroCur;
		mDesChars = new char[mCharLen];
		mCurChars = new char[mCharLen];
		mPreChars = new char[mCharLen];
		mDrawPos = new float[mCharLen];
		cur.getChars(noZeroCur, n, mDesChars, 0);
		mPaint.getTextWidths(mDesChars, 0, mCharLen, mDrawPos);
		for (int i = 0; i < mCharLen; i++) {
			char c = mDesChars[i];
			if (c != '.') {
				mMaxBit = Math.max(mMaxBit, c - 48);
				mPreChars[i] = '0';
			} else {
				mPreChars[i] = '.';
			}
		}
		// d("calCharAndWidth", "mCharLen=" + mCharLen + ",mMaxBit=" + mMaxBit);
		// d("calCharAndWidth", "mDesChars=" + Arrays.toString(mDesChars));
		// d("calCharAndWidth", "mPreChars=" + Arrays.toString(mPreChars));
		prepareCurChar(1);
	}

	private void prepareCurChar(int times) {
		char cur = (char) (times + 48);
		char des = 0;
		for (int i = 0; i < mCharLen; i++) {
			des = mDesChars[i];
			if (des == '.') {
				mCurChars[i] = '.';
			} else {
				if (cur <= des) {
					mCurChars[i] = cur;
				}
			}
		}
	}

	private boolean prepareNextChar(int time) {
		if (mMaxBit > time) {
			System.arraycopy(mCurChars, 0, mPreChars, 0, mCharLen);
			prepareCurChar(++time);
			d("calCurChar", "mCurChars=" + Arrays.toString(mCurChars) + ",  change times=" + time
					+ " in " + mMaxBit);
			return true;
		}
		return false;
	}

	@Override
	public void onAnimChaged(View nullView, int type, int which, float val, float dval) {
		// d("onAnimChaged", "type=" + type + ",val=" + val + ",dval=" + dval);
		if (type == IAnimChaged.TYPE_CHANGED) {
			mAnimRate = val * mMaxBit;
			mCurAnimTimes = (int) Math.floor(mAnimRate);
			mAnimRate = mAnimRate - mCurAnimTimes;
			if (mCurAnimTimes != mPreAnimTime) {
				mPreAnimTime = mCurAnimTimes;
				if (prepareNextChar(mCurAnimTimes)) {
				}
				d("onAnimChaged", "mPreBitChangeTimes=" + mCurAnimTimes + ",mBitChangeTimes"
						+ mPreAnimTime);
			} else {
				d("onAnimChaged", "val=" + val + ",mCurAnimTimes" + mCurAnimTimes);
			}
			if (isCanvasVisible()) {
				invalidate();
			}
		} else if (type == IAnimChaged.TYPE_END) {
			mAnimRate = -10000;
			subFlag(which);
			if (isCanvasVisible()) {
				invalidate();
			}
		} else {
			addFlag(which);
		}
	}

	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		if (!mTxtMaxBounds.isEmpty()) {
			int h = (int) (mTxtMaxBounds.height() + mBitPadV * 2);
			int w = mTxtMaxBounds.width();
			int len = mFormatStyle.length();
			if (mFormatStyle.contains(".")) {
				w = (int) (w + mBitPadH * (len - 1) * 2 + mPointPad * 2 + (Math.max(len - 2, 0) * mBitMargin));
			} else {
				w = (int) (w + mBitPadH * len * 2 + (len - 1) * mBitMargin);
			}
			setMeasuredDimension(
					resolveSize((int) (w + getPaddingLeft() + getPaddingRight()), wSpec),
					resolveSize((int) (h + getPaddingTop() + getPaddingBottom()), hSpec));
		} else {
			super.onMeasure(wSpec, hSpec);
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

	final public boolean isCanvasVisible() {
		return getVisibility() == VISIBLE && mRecFrame != null && !mRecFrame.isEmpty();
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
		if (title == null) {
			Log.d(TAG, msg);
		} else {
			Log.d(TAG, title + " -->" + msg);
		}
	}

	final protected void dd(String msg, Object... args) {
		d(TAG, String.format(msg, args));
	}
}