package howbuy.android.piggy.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class IcsLinearLayout extends LinearLayout {
	/**
	 * Don't show any dividers.
	 */
	public static final int SHOW_DIVIDER_NONE = 0;
	/**
	 * Show a divider at the beginning of the group.
	 */
	public static final int SHOW_DIVIDER_BEGINNING = 1;
	/**
	 * Show dividers between each item in the group.
	 */
	public static final int SHOW_DIVIDER_MIDDLE = 2;
	/**
	 * Show a divider at the end of the group.
	 */
	public static final int SHOW_DIVIDER_END = 4;
	private Drawable mDivider;
	private int mDividerWidth;
	private int mDividerHeight;
	private int mShowDividers;
	private int mDividerPadding;

	public IcsLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setDividerDrawable(Drawable divider) {
		if (divider == mDivider) {
			return;
		}
		mDivider = divider;
		if (divider != null) {
			mDividerWidth = divider.getIntrinsicWidth();
			mDividerHeight = divider.getIntrinsicHeight();
		} else {
			mDividerWidth = 0;
			mDividerHeight = 0;
		}
		setWillNotDraw(divider == null);
		requestLayout();
	}

	public void setShowDividers(int divideShow) {
		mShowDividers = divideShow;
	}

	public void setDividerPadding(int padding) {
		mDividerPadding = padding;
	}

	@Override
	protected void measureChildWithMargins(View child,
			int parentWidthMeasureSpec, int widthUsed,
			int parentHeightMeasureSpec, int heightUsed) {
		final int index = indexOfChild(child);
		final int orientation = getOrientation();
		final LayoutParams params = (LayoutParams) child.getLayoutParams();
		if (hasDividerBeforeChildAt(index)) {
			if (orientation == VERTICAL) {
				// Account for the divider by pushing everything up
				params.topMargin = mDividerHeight;
			} else {
				// Account for the divider by pushing everything left
				params.leftMargin = mDividerWidth;
			}
		}

		final int count = getChildCount();
		if (index == count - 1) {
			if (hasDividerBeforeChildAt(count)) {
				if (orientation == VERTICAL) {
					params.bottomMargin = mDividerHeight;
				} else {
					params.rightMargin = mDividerWidth;
				}
			}
		}
		super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed,
				parentHeightMeasureSpec, heightUsed);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mDivider != null) {
			if (getOrientation() == VERTICAL) {
				drawDividersVertical(canvas);
			} else {
				drawDividersHorizontal(canvas);
			}
		}
		super.onDraw(canvas);
	}

	private void drawDividersVertical(Canvas canvas) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE) {
				if (hasDividerBeforeChildAt(i)) {
					final android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) child
							.getLayoutParams();
					final int top = child.getTop() - lp.topMargin/*
																 * -
																 * mDividerHeight
																 */;
					drawHorizontalDivider(canvas, top);
				}
			}
		}

		if (hasDividerBeforeChildAt(count)) {
			final View child = getChildAt(count - 1);
			int bottom = 0;
			if (child == null) {
				bottom = getHeight() - getPaddingBottom() - mDividerHeight;
			} else {
				// final LayoutParams lp = (LayoutParams)
				// child.getLayoutParams();
				bottom = child.getBottom()/* + lp.bottomMargin */;
			}
			drawHorizontalDivider(canvas, bottom);
		}
	}

	private void drawDividersHorizontal(Canvas canvas) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			if (child != null && child.getVisibility() != GONE) {
				if (hasDividerBeforeChildAt(i)) {
					final android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) child
							.getLayoutParams();
					final int left = child.getLeft() - lp.leftMargin/*
																	 * -
																	 * mDividerWidth
																	 */;
					drawVerticalDivider(canvas, left);
				}
			}
		}

		if (hasDividerBeforeChildAt(count)) {
			final View child = getChildAt(count - 1);
			int right = 0;
			if (child == null) {
				right = getWidth() - getPaddingRight() - mDividerWidth;
			} else {
				// final LayoutParams lp = (LayoutParams)
				// child.getLayoutParams();
				right = child.getRight()/* + lp.rightMargin */;
			}
			drawVerticalDivider(canvas, right);
		}
	}

	private void drawHorizontalDivider(Canvas canvas, int top) {
		mDivider.setBounds(getPaddingLeft() + mDividerPadding, top, getWidth()
				- getPaddingRight() - mDividerPadding, top + mDividerHeight);
		mDivider.draw(canvas);
	}

	private void drawVerticalDivider(Canvas canvas, int left) {
		mDivider.setBounds(left, getPaddingTop() + mDividerPadding, left
				+ mDividerWidth, getHeight() - getPaddingBottom()
				- mDividerPadding);
		mDivider.draw(canvas);
	}

	private boolean hasDividerBeforeChildAt(int childIndex) {
		if (childIndex == 0 || childIndex == getChildCount()) {
			return false;
		}
		if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
			boolean hasVisibleViewBefore = false;
			for (int i = childIndex - 1; i >= 0; i--) {
				if (getChildAt(i).getVisibility() != GONE) {
					hasVisibleViewBefore = true;
					break;
				}
			}
			return hasVisibleViewBefore;
		}
		return false;
	}
}
