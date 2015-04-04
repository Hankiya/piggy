package com.howbuy.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.Linear0to1;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.ViewUtils;

public class ExpandGroup extends LinearLayout implements IAnimChaged {
	public final static int EXPAND_STATE_UNKNOW = -1;
	public final static int EXPAND_STATE_UNEXPAND = 0;
	public final static int EXPAND_STATE_EXPAND = 1;
	public final static int EXPAND_STATE_UNEXPANDING = 2;
	public final static int EXPAND_STATE_EXPANDING = 3;

	private final static int ANIM_EXPAND = 1;
	private final static int ANIM_UNEXPNAD = 2;
	private View mLayExpand;
	private boolean mExpand = true;
	private int mExpandRealHeight = -1, mExpandState = EXPAND_STATE_UNKNOW;
	private ViewGroup.LayoutParams mExpandLp = null;
	private boolean mBlockChildTouch = false, mAdjustIfListView = true;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mBlockChildTouch) {
			return true;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	public ExpandGroup(Context context) {
		super(context);
	}

	public ExpandGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
	}

	public void updateExpand(int expandHeigh) {
		if (expandHeigh > 0) {
			mExpandRealHeight = expandHeigh;
		} else {
			setExpnadView(mLayExpand, mExpandRealHeight);
			mLayExpand.invalidate();
		}
	}

	public void setBlockChildTouch(boolean block) {
		mBlockChildTouch = block;
	}

	public void setAdjustIfListView(boolean adjust) {
		mAdjustIfListView = adjust;
	}

	public void setExpnadView(View v, int defExpndHeight) {
		mExpandRealHeight = defExpndHeight;
		mLayExpand = v;
		if (mAdjustIfListView && mLayExpand instanceof ListView) {
			ListView lv = (ListView) mLayExpand;
			if (lv.getAdapter() != null && lv.getCount() > 0) {
				mExpandRealHeight = ViewUtils.setListViewHeightBasedOnChildren(lv);
			}
		}
		mLayExpand.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						mLayExpand.getViewTreeObserver().removeOnPreDrawListener(this);
						mExpandLp = mLayExpand.getLayoutParams();
						if (!(mLayExpand instanceof ListView)) {
							View p = (View) mLayExpand.getParent();
							final int wSpc = View.MeasureSpec.makeMeasureSpec(p.getMeasuredWidth()
									- p.getPaddingLeft() - p.getPaddingRight(),
									View.MeasureSpec.AT_MOST);
							final int hSpc = View.MeasureSpec.makeMeasureSpec(0,
									View.MeasureSpec.UNSPECIFIED);
							mLayExpand.measure(wSpc, hSpc);
							mExpandRealHeight = mLayExpand.getMeasuredHeight();
						}
						if (mLayExpand.getVisibility() != View.VISIBLE || mExpandLp.height == 0) {
							mExpand = false;
							mExpandState = EXPAND_STATE_UNEXPAND;
							mExpandLp.height = 0;
							mLayExpand.setVisibility(View.VISIBLE);
						} else {
							mExpand = true;
							mExpandState = EXPAND_STATE_EXPAND;
						}
						return true;
					}
				});

	}

	public boolean toggleExpand(int duration, int period, boolean expand) {
		if (expand != mExpand) {
			return toggleExpand(duration, period);
		}
		return false;
	}

	public boolean toggleExpand(int duration, int period) {
		if (mLayExpand != null && mExpandLp != null) {
			if (duration > 0) {
				return startAnim(mExpand ? ANIM_UNEXPNAD : ANIM_EXPAND, duration, period, mExpand);
			} else {
				mExpandLp.height = mExpand ? mExpandRealHeight : 0;
				mLayExpand.setLayoutParams(mExpandLp);
				if (mExpandListen != null) {
					mExpandListen.onExpnadChanged(mExpand ? IExpandChanged.CHANGE_END_EXPAND : IExpandChanged.CHANGE_END_UNEXPAND, 0);
				}
			}
		}
		return false;
	}

	@Override
	public void onAnimChaged(View v, int type, int which, float val, float dval) {
		mExpandLp.height = (int) (val * mExpandRealHeight);
		mLayExpand.setLayoutParams(mExpandLp);
		if (type != IAnimChaged.TYPE_CHANGED) {
			if (type == IAnimChaged.TYPE_END) {
				mExpand = !mExpand;
				notifyExpandChanged(which, 0);
			} else {
				notifyExpandChanged(which, getLinear().getDuration());
			}
		}
	}

	public int getExpandState() {
		return mExpandState;
	}

	// /////////////////////////////////////////////////////////////////////
	private Linear0to1 mLinear = null;

	final private Linear0to1 getLinear() {
		if (mLinear == null) {
			mLinear = new Linear0to1(GlobalApp.getApp().getHandler(), null, this);
		}
		return mLinear;
	}

	final public boolean startAnim(int which, int duration, int period, boolean reversal) {
		boolean result = getLinear().start(which, duration, period, reversal);
		return result;
	}

	final public void stopAnim() {
		if (mLinear != null) {
			mLinear.stop();
		}
	}

	// /////////////////////////////////////////////////////////////////////
	protected void notifyExpandChanged(int animWhich, int duration) {
		if (mExpandListen != null) {
			int changeType = 0;
			if (duration > 0) {
				if (animWhich == ANIM_EXPAND) {
					changeType = IExpandChanged.CHANGE_START_EXPAND;
					mExpandState = EXPAND_STATE_EXPANDING;
				} else {
					changeType = IExpandChanged.CHANGE_START_UNEXPAND;
					mExpandState = EXPAND_STATE_UNEXPANDING;
				}
			} else {
				if (animWhich == ANIM_EXPAND) {
					changeType = IExpandChanged.CHANGE_END_EXPAND;
					mExpandState = EXPAND_STATE_EXPAND;
				} else {
					changeType = IExpandChanged.CHANGE_END_UNEXPAND;
					mExpandState = EXPAND_STATE_UNEXPAND;
				}
			}
			mExpandListen.onExpnadChanged(changeType, duration);
		}
	}

	private IExpandChanged mExpandListen = null;

	public void setOnExpnadChangedListener(IExpandChanged l) {
		mExpandListen = l;
	}

	public interface IExpandChanged {
		public final static int CHANGE_START_EXPAND = 1;
		public final static int CHANGE_START_UNEXPAND = 2;
		public final static int CHANGE_END_EXPAND = 4;
		public final static int CHANGE_END_UNEXPAND = 8;

		void onExpnadChanged(int changeType, int duration);
	}
}
