package com.howbuy.control;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.howbuy.lib.adp.AbsAdp;
import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.compont.Linear0to1;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.ViewUtils;

public class ExpandableLinearLayout extends com.actionbarsherlock.internal.widget.IcsLinearLayout
		implements IAnimChaged {
	private LayoutParams mChildLp = new LayoutParams(-1, -2);
	public final static int EXPAND_STATE_UNKNOW = -1;// first init state.
	public final static int EXPAND_STATE_UNEXPAND = 0;// have un expand.
	public final static int EXPAND_STATE_EXPAND = 1;// have expand.
	public final static int EXPAND_STATE_UNEXPANDING = 2;// unexpanding.
	public final static int EXPAND_STATE_EXPANDING = 3;// expanding
	private final static int ANIM_EXPAND = 1;// anim to expand.
	private final static int ANIM_UNEXPNAD = 2;// anim to unexpand.
	private int mExpandState = EXPAND_STATE_UNKNOW;// expand state first set to
	private boolean mBlockChildTouch = false;// weather to disable child touch.
	private AbsAdp mAdapter = null;
	private ArrayList<ViewInf> mViewInfs = new ArrayList<ViewInf>();

	private void d(String title, String msg) {
		LogUtils.d("layout", title + ">>" + msg);
	}

	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			ExpandableLinearLayout.this.removeAllViews();
			int n = mAdapter == null ? 0 : mAdapter.getCount();
			mViewInfs.clear();
			int[] r = new int[] { -1, -1 };
			for (int i = 0; i < n; i++) {
				View v = mAdapter.getView(i, null, ExpandableLinearLayout.this);
				if (mAdapter.hasFlag(i, 0)) {
					ViewInf inf = ViewInf.measure(v, mChildLp, r);
					mViewInfs.add(inf.init(i, mExpandState == EXPAND_STATE_EXPAND));
					d("onChange", inf.toString());
				}
				addView(v);
			}
			d("onChange", "reset all view ,expandable num=" + mViewInfs.size());
		}

		@Override
		public void onInvalidated() {
			d("onInvalidated", "do nothing right now");
		}
	};

	public void setAdapter(AbsAdp adapter, boolean expand) {
		if (expand) {
			mExpandState = EXPAND_STATE_EXPAND;
		} else {
			mExpandState = EXPAND_STATE_UNEXPAND;
		}
		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mObserver);
		}
		mAdapter = adapter;
		if (mAdapter != null) {
			mAdapter.registerDataSetObserver(mObserver);
		}
		mObserver.onChanged();
	}

	public ExpandableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mBlockChildTouch) {
			return true;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	public void setBlockChildTouch(boolean block) {
		mBlockChildTouch = block;
	}

	public boolean toggleExpand(int duration, int period) {
		if (!mViewInfs.isEmpty()) {
			boolean expand = mExpandState == EXPAND_STATE_EXPAND;
			d("toggleExpand", "expand=" + expand + ",unexpand="
					+ (mExpandState == EXPAND_STATE_UNEXPAND));
			if (expand || mExpandState == EXPAND_STATE_UNEXPAND) {// if no anim
				return startAnim(expand ? ANIM_UNEXPNAD : ANIM_EXPAND, duration, period, expand);
			}
		}
		return false;
	}

	@Override
	public void onAnimChaged(View v, int type, int which, float val, float dval) {
		if (type != IAnimChaged.TYPE_CHANGED) {
			if (type == IAnimChaged.TYPE_END) {
				notifyExpandChanged(which, 0);
				d("onAnimChaged", "end type=" + type + ",val=" + val + ",mExpandState="
						+ mExpandState);
			} else {
				notifyExpandChanged(which, getLinear().getDuration());
				d("onAnimChaged", "start type=" + type + ",val=" + val + ",mExpandState="
						+ mExpandState);
			}
		}
		int n = mViewInfs.size();
		for (int i = 0; i < n; i++) {
			mViewInfs.get(i).update(val);
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
		if (mExpandListen != null) {
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

	private static class ViewInf {
		int pos = -1;
		View view = null;
		int height = -1;
		LayoutParams lpCur = new LayoutParams(-1, 0);

		public ViewInf(View v, int height) {
			this.view = v;
			this.height = height;
		}

		public ViewInf init(int pos, boolean expand) {
			this.pos = pos;
			update(expand ? 1 : 0);
			view.setLayoutParams(lpCur);
			return this;
		}

		public void update(float rate) {
			lpCur.height = (int) (height * rate);
			if (lpCur.height == 0) {
				view.setVisibility(View.GONE);
			} else {
				if (view.getVisibility() != View.VISIBLE) {
					view.setVisibility(View.VISIBLE);
				}
			}
			view.requestLayout();
		}

		public static ViewInf measure(View v, LayoutParams l, int[] r) {
			v.setLayoutParams(l);
			ViewUtils.measureView(v, 0, 0, r);
			return new ViewInf(v, r[1]);
		}

		@Override
		public String toString() {
			return "ViewInf[pos=" + pos + ", height=" + height + ", lpCur.h=" + lpCur.height + "]";
		}

	}

}
