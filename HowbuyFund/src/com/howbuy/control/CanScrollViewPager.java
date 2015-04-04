package com.howbuy.control;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CanScrollViewPager extends ViewPager {
	private boolean canHScroll = true;

	public boolean isCanHScroll() {
		return canHScroll;
	}

	public void setCanHScroll(boolean canHScroll) {
		this.canHScroll = canHScroll;
	}

	public CanScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (isCanHScroll()) {
			try {
				return super.onInterceptTouchEvent(arg0);
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}
}
