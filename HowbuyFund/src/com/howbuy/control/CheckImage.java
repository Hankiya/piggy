package com.howbuy.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-8-1 下午3:01:15
 */
@SuppressLint("NewApi")
public class CheckImage extends ImageView implements Checkable {
	protected boolean mChecked = false;
	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	public static int CHECK_ABLE = 1, CLICK_CHECK = 2;
	private int mFlag = CHECK_ABLE | CLICK_CHECK;

	public CheckImage(Context context) {
		this(context, null, 0);
	}

	public CheckImage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CheckImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
			if (mCheckListener != null) {
				mCheckListener.onCheckImageChanged(this, mChecked);
			}
		}
	}

	@Override
	public boolean performClick() {
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
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		return drawableState;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		/*
		 * if (w != 0 && h != 0) { mRect.set(0, 0, w, h); } else {
		 * mRect.setEmpty(); }
		 */
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

	OnCheckImageChangeListener mCheckListener = null;

	public void setOnCheckImageChangeListener(OnCheckImageChangeListener l) {
		mCheckListener = l;
	}

	public static interface OnCheckImageChangeListener {
		void onCheckImageChanged(View buttonView, boolean isChecked);
	}

	static class SavedState extends BaseSavedState {
		boolean checked;
		int flag;

		/**
		 * Constructor called from {@link CompoundButton#onSaveInstanceState()}
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			checked = (Boolean) in.readValue(null);
			flag = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeValue(checked);
			out.writeInt(flag);
		}

		@Override
		public String toString() {
			return "CompoundButton.SavedState{"
					+ Integer.toHexString(System.identityHashCode(this)) + " checked=" + checked
					+ "}";
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.checked = isChecked();
		ss.flag = mFlag;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		mFlag = ss.flag;
		setChecked(ss.checked);
		requestLayout();
	}
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(CheckImage.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CheckImage.class.getName());
    }
}
