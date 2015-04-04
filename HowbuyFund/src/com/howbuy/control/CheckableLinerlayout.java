package com.howbuy.control;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Lightweight ViewGroup that wraps list items obtained from user's ListAdapter.
 * ItemView expects a single child that has a definite height (i.e. the child's
 * layout height is not MATCH_PARENT). The width of ItemView will always match
 * the width of its child (that is, the width MeasureSpec given to ItemView is
 * passed directly to the child, and the ItemView measured width is set to the
 * child's measured width). The height of ItemView can be anything; the
 * 
 * 
 * The purpose of this class is to optimize slide shuffle animations.
 */
public class CheckableLinerlayout extends LinearLayout implements Checkable {
	private boolean mChecked;
	private static final String TAG = CheckableLinerlayout.class.getCanonicalName();
	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

	public CheckableLinerlayout(final Context context) {
		super(context);
		// setClickable(true);
		// setLongClickable(true);
	}

	public CheckableLinerlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setChecked(final boolean checked) {
		mChecked = checked;
		refreshDrawableState();
	}

	@Override
	protected int[] onCreateDrawableState(final int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked())
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		return drawableState;
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		final Drawable drawable = getBackground();
		if (drawable != null) {
			final int[] myDrawableState = getDrawableState();
			drawable.setState(myDrawableState);
			invalidate();
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean performLongClick() {
		return super.performLongClick();
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// Force our ancestor class to save its state
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState savedState = new SavedState(superState);
		savedState.checked = isChecked();
		return super.onSaveInstanceState();
	}

	@Override
	public void onRestoreInstanceState(final Parcelable state) {
		final SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		setChecked(savedState.checked);
		requestLayout();
	}

	// /////////////
	// SavedState //
	// /////////////
	private static class SavedState extends BaseSavedState {
		boolean checked;
		@SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR;
		static {
			CREATOR = new Parcelable.Creator<SavedState>() {
				@Override
				public SavedState createFromParcel(final Parcel in) {
					return new SavedState(in);
				}

				@Override
				public SavedState[] newArray(final int size) {
					return new SavedState[size];
				}
			};
		}

		SavedState(final Parcelable superState) {
			super(superState);
		}

		private SavedState(final Parcel in) {
			super(in);
			checked = (Boolean) in.readValue(null);
		}

		@Override
		public void writeToParcel(final Parcel out, final int flags) {
			super.writeToParcel(out, flags);
			out.writeValue(checked);
		}

		@Override
		public String toString() {
			return TAG + ".SavedState{" + Integer.toHexString(System.identityHashCode(this))
					+ " checked=" + checked + "}";
		}
	}
}
