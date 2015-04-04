package com.howbuy.control;

import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VerticalScrollView extends ScrollView {

	private PointF mPDis = new PointF();
	private PointF mPLast = new PointF();
	private boolean mOnlyVerticalScroll = true;

	// Edge-effects don't mix well with the translucent action bar in Android
	// 2.X
	private boolean mDisableEdgeEffects = true;

	private OnScrollChangedListener mOnScrollChangedListener;
	private boolean mScrollChangedSinceLastIdle;

	public VerticalScrollView(Context context) {
		this(context, null);
	}

	public VerticalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

	@Override
	protected float getTopFadingEdgeStrength() {
		// http://stackoverflow.com/a/6894270/244576
		if (mDisableEdgeEffects && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getTopFadingEdgeStrength();
	}

	@Override
	protected float getBottomFadingEdgeStrength() {
		// http://stackoverflow.com/a/6894270/244576
		if (mDisableEdgeEffects && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			return 0.0f;
		}
		return super.getBottomFadingEdgeStrength();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mOnlyVerticalScroll) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPDis.x = mPDis.y = 0f;
				mPLast.x = ev.getX();
				mPLast.y = ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float curX = ev.getX();
				final float curY = ev.getY();
				mPDis.x += Math.abs(curX - mPLast.x);
				mPDis.y += Math.abs(curY - mPLast.y);
				mPLast.x = curX;
				mPLast.y = curY;
				if (mPDis.x > mPDis.y) {
					return false;
				}
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean b = super.onTouchEvent(ev);
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if (mOnScrollChangedListener != null && mScrollChangedSinceLastIdle) {
				mScrollChangedSinceLastIdle = false;
				mOnScrollChangedListener.onScrollIdle();
			}
		}
		return b;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		mScrollChangedSinceLastIdle = true;
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);

		void onScrollIdle();
	}
}
