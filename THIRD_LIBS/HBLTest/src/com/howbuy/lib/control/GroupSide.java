package com.howbuy.lib.control;

import android.content.Context;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.howbuy.lib.control.ElasticLayout.ElasticState;
import com.howbuy.lib.utils.ViewUtils;

public class GroupSide {
	private float scaleRange = 0.2f;
	private static final int FLAG_BASE = 16;
	protected static int INIT_TOP = FLAG_BASE, INIT_BOT = FLAG_BASE << 1,
			INIT_LEFT = FLAG_BASE << 2, INIT_RIGHT = FLAG_BASE << 3;
	protected static final int REFESH_TOP = FLAG_BASE << 8, REFESH_BOT = FLAG_BASE << 9;
	protected int mFlag = 0, mVTopRefeshH = -1, mVBotRefeshH = -1;
	protected int mInitPaddingH = 0, mInitPaddingV = 0;
	protected View mVTop, mVBot, mVLeft, mVRight;
	protected RelativeLayout mTopContainer, mBotContainer, mLeftContainer, mRightContainer;
	protected ElasticState mCurState = ElasticState.NORMAL;
	protected GroupSide mWrapOld = null;
	protected ElasticLayout mElasticView = null;

	protected GroupSide(ElasticLayout elasticView) {
		this.mElasticView = elasticView;
		if (mWrapOld == null) {
			mFlag = (ElasticLayout.SCALE_TOP | ElasticLayout.SCALE_BOT | ElasticLayout.SCALE_LEFT
					| ElasticLayout.SCALE_RIGHT | REFESH_TOP | REFESH_BOT);
		}
	}

	public GroupSide(GroupSide wrap) {
		if (mWrapOld == null && wrap == null) {
			mFlag = (ElasticLayout.SCALE_TOP | ElasticLayout.SCALE_BOT | ElasticLayout.SCALE_LEFT
					| ElasticLayout.SCALE_RIGHT | REFESH_TOP | REFESH_BOT);
		}
		wrapOriginal(wrap);
	}

	protected void onAttchedChanged(boolean isAttach, ElasticLayout elasticView) {
		if (isAttach) {
			mElasticView = elasticView;
		} else {
			mElasticView = null;
		}
	}

	// /////有待扩展.////////////////////////////////////////////////////////
	protected BaseAdapter mAdpter = null;
	protected int mCurIndex = -1;

	protected BaseAdapter setAdpter(BaseAdapter adp) {
		this.mAdpter = adp;
		if (adp != null) {
			resetInit(true);
			mCurIndex = 0;
			mAdpter.notifyDataSetInvalidated();
		} else {
			mCurIndex = -1;
		}
		return mAdpter;
	}

	protected BaseAdapter getAdpter() {
		return mAdpter;
	}

	protected int getCurIndex() {
		return mCurIndex;
	}

	protected boolean setCurIndex(int index) {
		boolean result = false;
		if (mAdpter != null) {
			result = !(index < 0 || index >= mAdpter.getCount());
		} else {
			mCurIndex = -1;
		}
		if (result) {
			mAdpter.notifyDataSetInvalidated();
		}
		return result;
	}

	private int addInnerView(int add) {
		int flag = 0;
		if (hasFlag(add, ElasticLayout.RECORD_TOP)) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -2);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mTopContainer.addView(mVTop, lp);
			flag |= INIT_TOP;
		}
		if (hasFlag(add, ElasticLayout.RECORD_BOT)) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -2);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			mBotContainer.addView(mVBot, lp);
			flag |= INIT_BOT;
		}

		if (hasFlag(add, ElasticLayout.RECORD_LEFT)) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -2);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			mLeftContainer.addView(mVLeft, lp);
			flag |= INIT_LEFT;
		}
		if (hasFlag(add, ElasticLayout.RECORD_RIGHT)) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -2);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			mRightContainer.addView(mVRight, lp);
			flag |= INIT_RIGHT;
		}
		addFlag(onViewAdded(flag));
		return flag >> 4;
	}

	private int removeInnerView(int remove) {// add need'nt to judge the view
		int flag = 0;
		if (hasFlag(remove, ElasticLayout.RECORD_TOP)) {
			mTopContainer.removeView(mVTop);
			flag |= INIT_TOP;
			mVTop = null;
		}
		if (hasFlag(remove, ElasticLayout.RECORD_BOT)) {
			mBotContainer.removeView(mVBot);
			flag |= INIT_BOT;
			mVBot = null;
		}

		if (hasFlag(remove, ElasticLayout.RECORD_LEFT)) {
			mLeftContainer.removeView(mVLeft);
			flag |= INIT_LEFT;
			mVLeft = null;
		}
		if (hasFlag(remove, ElasticLayout.RECORD_RIGHT)) {
			mRightContainer.removeView(mVRight);
			flag |= INIT_RIGHT;
			mVRight = null;
		}
		subFlag(onViewRemoved(flag));
		return flag >> 4;
	}

	private void resetInit(boolean resetAll) {
		int remove = 0, add = 0;
		if (resetAll) {
			if (mVTop != null & mTopContainer != null) {
				remove |= ElasticLayout.RECORD_TOP;
			}
			if (mVBot != null & mBotContainer != null) {
				remove |= ElasticLayout.RECORD_BOT;
			}
			if (mVLeft != null & mLeftContainer != null) {
				remove |= ElasticLayout.RECORD_LEFT;
			}
			if (mVRight != null & mRightContainer != null) {
				remove |= ElasticLayout.RECORD_RIGHT;
			}
			removeView(remove);
		} else {
			remove = mFlag & (INIT_TOP | INIT_BOT | INIT_LEFT | INIT_RIGHT);
		}
		add |= mVTop == null ? 0 : INIT_TOP;
		add |= mVBot == null ? 0 : INIT_BOT;
		add |= mVLeft == null ? 0 : INIT_LEFT;
		add |= mVRight == null ? 0 : INIT_RIGHT;
		if (remove != 0 && !resetAll) {
			subFlag(onViewRemoved(remove));
		}
		if (add != 0) {
			addFlag(onViewAdded(add));
		}
	}

	protected boolean wrapOriginal(GroupSide wrap) {
		if (wrap != null && wrap != mWrapOld) {
			this.mFlag |= wrap.getFlag();
			this.mElasticView = mElasticView == null ? wrap.mElasticView : mElasticView;
			this.mAdpter = mAdpter == null ? wrap.mAdpter : mAdpter;
			setView(wrap.mVTop, wrap.mVBot, wrap.mVLeft, wrap.mVRight);
			setContainer(wrap.mTopContainer, wrap.mBotContainer, wrap.mLeftContainer,
					wrap.mRightContainer);
			resetInit(mAdpter != null);
			mWrapOld = wrap;
			mVTopRefeshH = -1;
			mVBotRefeshH = -1;
			return true;
		}
		return false;
	}

	public void setContainer(RelativeLayout top, RelativeLayout bot, RelativeLayout left,
			RelativeLayout right) {
		this.mTopContainer = top;
		this.mBotContainer = bot;
		this.mLeftContainer = left;
		this.mRightContainer = right;
		int add = 0;
		if (!hasFlag(ElasticLayout.RECORD_TOP) && mVTop != null && mTopContainer != null) {
			add |= ElasticLayout.RECORD_TOP;
		}
		if (!hasFlag(ElasticLayout.RECORD_BOT) && mVBot != null && mBotContainer != null) {
			add |= ElasticLayout.RECORD_BOT;
		}
		if (!hasFlag(ElasticLayout.RECORD_LEFT) && mVLeft != null && mLeftContainer != null) {
			add |= ElasticLayout.RECORD_LEFT;
		}
		if (!hasFlag(ElasticLayout.RECORD_RIGHT) && mVRight != null && mRightContainer != null) {
			add |= ElasticLayout.RECORD_RIGHT;
		}
		addView(add);
	}

	public int setView(Context cx, int resTop, int resBot, int resLeft, int resRight) {
		if (mAdpter != null)
			return 0;
		LayoutInflater lf = LayoutInflater.from(cx);
		View vTop = mVTop, vBot = mVBot, vLeft = mVLeft, vRight = mVRight;
		vTop = resTop > 0 ? lf.inflate(resTop, null) : mVTop;
		vBot = resBot > 0 ? lf.inflate(resBot, null) : mVBot;
		vLeft = resLeft > 0 ? lf.inflate(resLeft, null) : mVLeft;
		vRight = resRight > 0 ? lf.inflate(resRight, null) : mVRight;
		return setView(vTop, vBot, vLeft, vRight);
	}

	public int setView(int which, View v) {
		View top = mVTop, bot = mVBot, left = mVLeft, right = mVRight;
		which &= (ElasticLayout.RECORD_TOP | ElasticLayout.RECORD_BOT | ElasticLayout.RECORD_LEFT | ElasticLayout.RECORD_RIGHT);
		if (which != 0) {
			if (hasFlag(which, ElasticLayout.RECORD_TOP)) {
				top = v;
			} else if (hasFlag(which, ElasticLayout.RECORD_BOT)) {
				bot = v;
			} else if (hasFlag(which, ElasticLayout.RECORD_LEFT)) {
				left = v;
			} else {
				right = v;
			}
			return setView(top, bot, left, right);
		}
		return which;
	}

	// 只要发生改变返回大于0。
	public int setView(View top, View bot, View left, View right) {
		if (mAdpter != null)
			return 0;
		int add = 0, remove = 0;
		if (mVTop != top) {
			if (mVTop != null & mTopContainer != null) {
				remove |= ElasticLayout.RECORD_TOP;
			}
			if ((mVTop = top) != null) {
				setRefeshEnable(ElasticLayout.RECORD_TOP, hasFlag(REFESH_TOP));
				add |= mTopContainer == null ? 0 : ElasticLayout.RECORD_TOP;
			}
		}
		if (mVBot != bot) {
			if (mVBot != null & mBotContainer != null) {
				remove |= ElasticLayout.RECORD_BOT;
			}

			if ((mVBot = bot) != null) {
				setRefeshEnable(ElasticLayout.RECORD_BOT, hasFlag(REFESH_BOT));
				add |= mBotContainer == null ? 0 : ElasticLayout.RECORD_BOT;
			}
		}

		if (mVLeft != left) {
			if (mVLeft != null & mLeftContainer != null) {
				remove |= ElasticLayout.RECORD_LEFT;
			}
			if ((mVLeft = left) != null) {
				add |= mLeftContainer == null ? 0 : ElasticLayout.RECORD_LEFT;
			}
		}
		if (mVRight != right) {
			if (mVRight != null & mRightContainer != null) {
				remove |= ElasticLayout.RECORD_RIGHT;
			}
			if ((mVRight = right) != null) {
				add |= mRightContainer == null ? 0 : ElasticLayout.RECORD_RIGHT;
			}
		}
		return removeView(remove) & addView(add);
	}

	private int addView(int add) {
		add &= (ElasticLayout.RECORD_TOP | ElasticLayout.RECORD_BOT | ElasticLayout.RECORD_LEFT | ElasticLayout.RECORD_RIGHT);
		if (add != 0) {
			addFlag(addInnerView(add));
		}
		return add;
	}

	/**
	 * @param remove
	 * @return remove
	 */
	public int removeView(int remove) {
		remove &= mFlag;
		if (remove != 0) {
			subFlag(removeInnerView(remove));
		}
		return remove;
	}

	private void setRefeshEnable(View v, int flag, boolean enable) {
		if (enable) {
			addFlag(flag);
			if (v != null) {
				v.setVisibility(View.VISIBLE);
			}
		} else {
			subFlag(flag);
			if (v != null) {
				v.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * @param sideType
	 *            ElasticLayout.RECORD_TOP|ElasticLayout.RECORD_BOT
	 */
	public void setRefeshEnable(int sideType, boolean enable) {
		sideType &= (ElasticLayout.RECORD_TOP | ElasticLayout.RECORD_BOT);
		if (sideType != 0) {
			if (hasFlag(sideType, ElasticLayout.RECORD_TOP)) {
				View v = (mVTop == null ? null : mVTop.findViewWithTag("head"));
				setRefeshEnable(v, REFESH_TOP, enable);
			}
			if (hasFlag(sideType, ElasticLayout.RECORD_BOT)) {
				View v = (mVBot == null ? null : mVBot.findViewWithTag("foot"));
				setRefeshEnable(v, REFESH_BOT, enable);
			}
		}
	}

	/**
	 * you can decide whether the view can be slid scroll when it has no pre or
	 * next view to be displayed.
	 * 
	 * @param scrollType
	 *            RECORD_TOP = 1, RECORD_BOT = 2, RECORD_LEFT = 4, RECORD_RIGHT
	 *            = 8
	 * @return boolean
	 */
	protected boolean isScrollable(int scrollType) {
		return true;
	}

	public int getTopRefeshH(boolean considerVisible) {
		if (mVTop == null || (!hasFlag(REFESH_TOP) && considerVisible)) {
			return 0;
		} else {
			if (mVTopRefeshH == -1) {
				View v = mVTop.findViewWithTag("head");
				if (v == null) {
					mVTopRefeshH = 0;
				} else {
					int[] r = new int[] { 0, 0 };
					ViewUtils.measureView(v, 0, 0, r);
					mVTopRefeshH = r[1];
				}
			}
			return mVTopRefeshH;
		}
	}

	public int getBotRefeshH(boolean considerVisible) {
		if (mVBot == null || (!hasFlag(REFESH_BOT) && considerVisible)) {
			return 0;
		} else {
			if (mVBotRefeshH == -1) {
				View v = mVBot.findViewWithTag("foot");
				if (v == null) {
					mVBotRefeshH = 0;
				} else {
					int[] r = new int[] { 0, 0 };
					ViewUtils.measureView(v, 0, 0, r);
					mVBotRefeshH = r[1];
				}
			}
			return mVBotRefeshH;
		}
	}

	private void setPading(View v, int l, int t, int r, int b) {
		if (v != null) {
			v.setPadding(l, t, r, b);
		}
	}

	public void setInitPadding(int valH, int valV) {
		if (valH > 0) {
			mInitPaddingH = valH;
			setPading(mLeftContainer, valH, 0, 0, 0);
			setPading(mRightContainer, 0, 0, valH, 0);
		}
		if (valV > 0) {
			mInitPaddingV = valV;
			setPading(mTopContainer, 0, valV, 0, 0);
			setPading(mBotContainer, 0, 0, 0, valV);
		}
	}

	/**
	 * @param add
	 *            INIT_TOP = 16, INIT_BOT = 32, INIT_LEFT = 64, INIT_RIGHT =
	 *            128;可组合
	 * @return add
	 */
	protected int onViewAdded(int add) {
		return 0;
	}

	/**
	 * @param remove
	 *            INIT_TOP = 16, INIT_BOT = 32, INIT_LEFT = 64, INIT_RIGHT =
	 *            128;可组合
	 * @return remove
	 */
	protected int onViewRemoved(int remove) {
		return 0;
	}

	protected View exchangeView(ViewGroup pp, ViewGroup pc, ViewGroup pn, View p, View c, View n) {
		d(ElasticState.NORMAL,
				"exchangeView p=" + p.getTag() + " c=" + c.getTag() + " n=" + n.getTag());
		ViewGroup.LayoutParams lpp = p.getLayoutParams(), lpc = c.getLayoutParams(), lpn = n
				.getLayoutParams();
		pp.removeView(p);
		pn.removeView(n);
		pp.addView(c, lpc);
		pc.addView(n, lpn);
		pn.addView(p, lpp);
		return p;
	}

	protected View exchangeView(ViewGroup px, ViewGroup pc, View x, View c) {
		ViewGroup.LayoutParams lpx = x.getLayoutParams(), lpc = c.getLayoutParams();
		px.removeView(x);
		px.addView(c, lpc);
		pc.addView(x, lpx);
		return c;
	}

	protected View exchangeView(ViewGroup p, int animType) {
		View r = null, c = p.getChildAt(0);
		p.removeView(c);
		if (0 != (animType & (ElasticLayout.ANIM_LEFT | ElasticLayout.ANIM_RIGHT))) {
			if (ElasticLayout.ANIM_LEFT == animType) {
				if (mVRight == null) {
					r = exchangeView(mLeftContainer, p, mVLeft, c);
					mVLeft = c;
				} else {
					r = exchangeView(mRightContainer, p, mLeftContainer, mVRight, c, mVLeft);
					mVLeft = r;
					mVRight = c;
				}
			} else {
				if (mVLeft == null) {
					r = exchangeView(mRightContainer, p, mVRight, c);
					mVRight = c;
				} else {
					r = exchangeView(mLeftContainer, p, mRightContainer, mVLeft, c, mVRight);
					mVRight = r;
					mVLeft = c;
				}
			}
		} else {
			if (ElasticLayout.ANIM_TOP == animType) {
				if (mVBot == null) {
					r = exchangeView(mTopContainer, p, mVTop, c);
					mVTop = c;
				} else {
					r = exchangeView(mBotContainer, p, mTopContainer, mVBot, c, mVTop);
					mVTop = r;
					mVBot = c;
				}
			} else {
				if (mVTop == null) {
					r = exchangeView(mBotContainer, p, mVBot, c);
					mVBot = c;
				} else {
					r = exchangeView(mTopContainer, p, mBotContainer, mVTop, c, mVBot);
					mVBot = r;
					mVTop = c;
				}
			}
		}
		d(ElasticState.NORMAL, " anime type=" + animType);
		return r;
	}

	/**
	 * @param sideType
	 *            int RECORD_TOP = 1, RECORD_BOT = 2, RECORD_LEFT = 4,
	 *            RECORD_RIGHT = 8
	 * @param disX
	 *            ,disY absolute distance x and y from the normal position.
	 * @param rate
	 *            mRecRat.left=x/mVisibleWidth; mRecRat.top=y/mVisibleHeight;
	 *            mRecRat.right=x/mMaxDistanceH; mRecRat.bottom=y/mMaxDistanceV;
	 * @return void
	 * @throws
	 */
	protected void onScrollChanged(ElasticState state, int sideType, int disX, int disY, RectF rate) {
		float rateH = 1 - rate.right, rateV = 1 - rate.bottom;
		int padH, padV;
		if (hasFlag(ElasticLayout.SCALE_LEFT) && hasFlag(sideType, ElasticLayout.RECORD_LEFT)) {
			padH = (int) (rateH * (mLeftContainer.getWidth() - mInitPaddingH) * scaleRange);
			padV = (int) (rateH * mLeftContainer.getHeight() * scaleRange);
			setPading(mLeftContainer, mInitPaddingH + padH, padV, padH, padV);
		}
		if (hasFlag(ElasticLayout.SCALE_RIGHT) && hasFlag(sideType, ElasticLayout.RECORD_RIGHT)) {
			padH = (int) (rateH * (mRightContainer.getWidth() - mInitPaddingH) * scaleRange);
			padV = (int) (rateH * mRightContainer.getHeight() * scaleRange);
			setPading(mRightContainer, padH, padV, mInitPaddingH + padH, padV);
		}
		if (hasFlag(ElasticLayout.SCALE_TOP) && hasFlag(sideType, ElasticLayout.RECORD_TOP)) {
			if (getTopRefeshH(true) <= 0) {
				padH = (int) (rateV * (mTopContainer.getWidth()) * scaleRange);
				padV = (int) (rateV * (mTopContainer.getHeight() - mInitPaddingV) * scaleRange);
				setPading(mTopContainer, padH, mInitPaddingV + padV, padH, padV);
			}
		}
		if (hasFlag(ElasticLayout.SCALE_BOT) && hasFlag(sideType, ElasticLayout.RECORD_BOT)) {
			if (getBotRefeshH(true) <= 0) {
				padH = (int) (rateV * (mBotContainer.getWidth()) * scaleRange);
				padV = (int) (rateV * (mBotContainer.getHeight() - mInitPaddingV) * scaleRange);
				setPading(mBotContainer, padH, padV, padH, mInitPaddingV + padV);
			}
		}
	}

	public void onTopBotStateChanged(ElasticState state, boolean isHead) {
		mCurState = state;
	}

	/**
	 * @param state
	 * @param animType
	 *            ANIM_TOP ANIM_BOT ANIM_LEFT ANIM_RIGHT ANIM_CENTER
	 *            ANIM_CENTER_TOP ANIM_CENTER_BOT
	 * @param start
	 * @return void
	 * @throws
	 */
	public void onAnimChanged(ElasticState state, int animType, boolean start) {

	}

	public boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	public boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	protected void addFlag(int flag) {
		mFlag |= flag;
	}

	protected void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}

	protected void reverseFlag(int flag) {
		mFlag ^= flag;
	}

	protected void setFlag(int flag) {
		mFlag = flag;
	}

	public int getFlag() {
		return mFlag;
	}

	public View getLeft() {
		return mVLeft;
	}

	public View getTop() {
		return mVTop;
	}

	public View getRight() {
		return mVRight;
	}

	public View getBot() {
		return mVBot;
	}

	public RelativeLayout getTopContainer() {
		return mTopContainer;
	}

	public RelativeLayout getBotContainer() {
		return mBotContainer;
	}

	public RelativeLayout getLeftContainer() {
		return mLeftContainer;
	}

	public RelativeLayout getRightContainer() {
		return mRightContainer;
	}

	protected void d(ElasticState mState, String msg) {
		android.util.Log.d("bsh", mState.name() + " NoRecored> " + msg);
	}
}
