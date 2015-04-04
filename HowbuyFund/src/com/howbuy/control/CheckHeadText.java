package com.howbuy.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-7-30 下午5:01:15
 */
public class CheckHeadText extends TextView implements Checkable {
	protected boolean mChecked = false;
	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	public static int HEAD_LEFT = 1, HEAD_RIGHT = 2, HEAD_TOP = 4, HEAD_BOTTOM = 8,
			CHECK_ABLE = 16, CLICK_CHECK = 32;
	private int mPaddingH = 0;
	private int mPaddingV = 0;
	private float mHeadHeight = 1, mDensity = 1f;
	private int mFlag = HEAD_BOTTOM | CHECK_ABLE;
	private final Rect mRect = new Rect();
	private final RectF mTempRec = new RectF();
	private final Paint mHeadPaint = new Paint();

	public CheckHeadText(Context context) {
		this(context, null, 0);
	}

	public CheckHeadText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CheckHeadText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mHeadPaint.setStyle(Style.FILL);
		mHeadPaint.setAntiAlias(true);
		mDensity = getResources().getDisplayMetrics().density;
		mHeadHeight *= mDensity;
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean checked) {
		if (hasFlag(CHECK_ABLE)) {
			if (mChecked != checked) {
				mChecked = checked;
				refreshDrawableState();
			}
		}
	}

	@Override
	public boolean performClick() {
		/*
		 * XXX: These are tiny, need some surrounding 'expanded touch area',
		 * which will need to be implemented in Button if we only override
		 * performClick()
		 */
		/* When clicked, toggle the state */
		if (hasFlag(CLICK_CHECK)) {
			toggle();
		}
		return super.performClick();
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!mRect.isEmpty() && mFlag != 0) {
			mTempRec.setEmpty();
			if (hasFlag(HEAD_LEFT)) {
				mTempRec.set(mRect.left, mRect.top + mPaddingV, mRect.left + mHeadHeight,
						mRect.bottom - mPaddingV);
				canvas.drawRect(mTempRec, mHeadPaint);
			}
			if (hasFlag(HEAD_RIGHT)) {
				mTempRec.set(mRect.right - mHeadHeight, mRect.top + mPaddingV, mRect.right,
						mRect.bottom - mPaddingV);
				canvas.drawRect(mTempRec, mHeadPaint);
			}
			if (hasFlag(HEAD_TOP)) {
				mTempRec.set(mRect.left + mPaddingH, mRect.top, mRect.right - mPaddingH, mRect.top
						+ mHeadHeight);
				canvas.drawRect(mTempRec, mHeadPaint);
			}
			if (hasFlag(HEAD_BOTTOM)) {
				mTempRec.set(mRect.left + mPaddingH, mRect.bottom - mHeadHeight, mRect.right
						- mPaddingH, mRect.bottom);
				canvas.drawRect(mTempRec, mHeadPaint);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0 && h != 0) {
			mRect.set(0, 0, w, h);
		} else {
			mRect.setEmpty();
		}
	}

	public void setHeadColor(int color) {
		mHeadPaint.setColor(color);
	}

	public void setHeadHeight(float height) {
		mHeadHeight = height * mDensity;
	}

	public void setHeadHeightRaw(float height) {
		mHeadHeight = height;
	}

	public void setPaddingHorizonal(int padding) {
		mPaddingH = (int) (padding * mDensity);
	}

	public void setPaddingHorizonalRaw(int padding) {
		mPaddingH = padding;
	}

	public void setPaddingVerticalRaw(int padding) {
		mPaddingV = padding;
	}

	public void setPaddingVertical(int padding) {
		mPaddingV = (int) (padding * mDensity);
	}

	final public int getFlag() {
		return mFlag;
	}

	final public void setFlag(int flag) {
		mFlag = flag;
	}

	final public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	final public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final public void addFlag(int flag) {
		mFlag |= flag;
	}

	final public void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}

	final protected void reverseFlag(int flag) {
		mFlag ^= flag;
	}

	/*
	 * @Override public Parcelable onSaveInstanceState() { // Force our ancestor
	 * class to save its state setFreezesText(true); Parcelable superState =
	 * super.onSaveInstanceState();
	 * 
	 * SavedState ss = new SavedState(superState);
	 * 
	 * ss.checked = isChecked(); return ss; }
	 * 
	 * @Override public void onRestoreInstanceState(Parcelable state) {
	 * SavedState ss = (SavedState) state;
	 * 
	 * super.onRestoreInstanceState(ss.getSuperState()); setChecked(ss.checked);
	 * requestLayout(); }
	 */

	/*
	 * static class SavedState extends BaseSavedState { boolean checked;
	 * 
	 * SavedState(Parcelable superState) { super(superState); }
	 * 
	 * private SavedState(Parcel in) { super(in); checked = (Boolean)
	 * in.readValue(null); }
	 * 
	 * @Override public void writeToParcel(Parcel out, int flags) {
	 * super.writeToParcel(out, flags); out.writeValue(checked); }
	 * 
	 * @Override public String toString() { return "CheckText.SavedState{" +
	 * Integer.toHexString(System.identityHashCode(this)) + " checked=" +
	 * checked + "}"; }
	 * 
	 * public static final Parcelable.Creator<SavedState> CREATOR = new
	 * Parcelable.Creator<SavedState>() { public SavedState
	 * createFromParcel(Parcel in) { return new SavedState(in); }
	 * 
	 * public SavedState[] newArray(int size) { return new SavedState[size]; }
	 * }; }
	 */

}
