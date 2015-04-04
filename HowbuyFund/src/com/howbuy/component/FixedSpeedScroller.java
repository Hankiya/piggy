package com.howbuy.component;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
	public static final int defaultDuration = 250;
	public static final int customDuration = 500;
	private int mDuration = 1000;

	public FixedSpeedScroller(Context context) {
		super(context);
	}

	public FixedSpeedScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	public void setFixedDuration(int duration) {
		mDuration = duration;
	}

	public int getFixedDuration() {
		return mDuration;
	}

}