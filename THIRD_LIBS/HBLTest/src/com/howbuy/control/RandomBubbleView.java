package com.howbuy.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.howbuy.lib.adp.AbsSimpleAdp;
import com.howbuy.lib.compont.PathCurve;
import com.howbuy.lib.compont.PathHelper;
import com.howbuy.lib.compont.PathPoint;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.ViewUtils;

public class RandomBubbleView extends ViewGroup implements View.OnClickListener {
	protected String TAG = "RandomBubbleView";
	public static final int SORT_ASC = 1, SORT_DESC = 2, SORT_RANDOM = 4;
	protected int mSortType = SORT_ASC;
	protected float mDensity;
	protected final Rect mRecFrame = new Rect();
	protected final Rect mRecTemp = new Rect();
	private boolean mFirstVisible = true;
	private Random mRandom = new Random(System.currentTimeMillis());
	private PathHelper mPathHelpr = new PathHelper(0.9f);
	protected int mMaxChildWidth = 180, mMaxChildHeight = 100,
			mMinChildCount = 0, mChildCount = 40;
	private float mChildCountFactor = 0.9f, mMaxWdivH = mMaxChildWidth
			/ (float) mMaxChildHeight;// 与上同步.
	private final LinkedList<ViewData> mReuseViews = new LinkedList<ViewData>();
	private final ArrayList<Integer> mRemovedIndex = new ArrayList<Integer>();
	private final LinkedHashMap<Integer, ViewData> mVisibleView = new LinkedHashMap<Integer, ViewData>();
	private static final int[] POSITION = new int[] { 0, 1, 2, 4, 8, 5, 6, 10,
			9 };

	public void setSortType(int sortType) {
		sortType &= (SORT_ASC | SORT_DESC | SORT_RANDOM);
	}

	public int getSortType() {
		return mSortType;
	}

	protected int getNextIndex() {
		int n = mRemovedIndex.size();
		if (n == 0) {
			return -1;
		} else {
			if (ViewUtils.hasFlag(mSortType, SORT_ASC)) {
				return mRemovedIndex.remove(0);
			} else if (ViewUtils.hasFlag(mSortType, SORT_DESC)) {
				return mRemovedIndex.remove(n - 1);
			} else {
				return mRemovedIndex.remove(mRandom.nextInt(n));
			}
		}
	}

	protected void resetAllToEmpty() {
		mReuseViews.clear();
		mRemovedIndex.clear();
		mVisibleView.clear();
		removeAllViews();
	}

	protected void updateRemovedIndex() {
		int n = mAdpter == null ? 0 : mAdpter.getCount();
		mRemovedIndex.clear();
		for (int i = 0; i < n; i++) {
			if (!mVisibleView.containsKey(i)) {
				mRemovedIndex.add(i);
			}
		}
	}

	protected int removeInvisibleView(int index, ViewData vd) {
		mReuseViews.add(vd);
		mRemovedIndex.add(index);
		vd.reset(true);
		removeViewInLayout(vd.view);
		mAdpter.recycleView(index, vd.view);
		dd("---------removeInvisibleView(index:%1$d (vd.left=%2$d,vd.top=%3$d))",
				index, vd.left, vd.top);
		return index;
	}

	private void addNewViewToLayout(int w, int h, long time) {
		int index = getNextIndex();
		if (index == -1)
			return;
		ViewData vd = mReuseViews.poll();
		if (vd == null) {
			vd = new ViewData(this);
		}
		vd.setView(mAdpter.getView(index, vd.view, this), index, time);
		ViewGroup.LayoutParams lp = vd.view.getLayoutParams();
		if (lp == null) {
			lp = new ViewGroup.LayoutParams(-2, -2);
			vd.view.setLayoutParams(lp);
		} else {
			lp.width = lp.height = -2;
		}
		addViewInLayout(vd.view, -1, vd.view.getLayoutParams(), true);
		mVisibleView.put(index, vd);
		measureChild(vd.view, w, h);
		adjustChildSize(vd);
		initChildPath(vd, 2);
		dd("+++++++++addNewViewToLayout(index:%1$d (vd.left=%2$d,vd.top=%3$d))",
				index, vd.left, vd.top);
	}

	private AbsSimpleAdp mAdpter = null;
	private TimerTask mTask = new TimerTask() {
		@Override
		public void run() {
			dd("TimerTask =================> call: removeOrAddToLayout>requestLayout");
			post(new Runnable() {
				@Override
				public void run() {
					removeOrAddToLayout(mRecFrame.width(), mRecFrame.height());
					requestLayout();
				}
			});
		}
	};

	private Timer mTimer = null;

	private void stopSchedule() {
		if (mTimer != null) {
			mTask.cancel();
			mTimer.purge();
		}
	}

	private void startSchedual(int delay, int period) {
		stopSchedule();
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (delay > 0) {
			mTimer.schedule(mTask, delay, period);
		} else {
			mTimer.schedule(mTask, 100, period);
		}
	}

	public AbsSimpleAdp getAdapter() {
		return mAdpter;
	}

	public void setAdapter(AbsSimpleAdp adp) {
		if (mAdpter != null) {
			mAdpter.onAttachChanged(this, false);
			mAdpter.unregisterDataSetObserver(mObserver);
		}
		mAdpter = adp;
		if (mAdpter != null) {
			mAdpter.registerDataSetObserver(mObserver);
			mAdpter.onAttachChanged(this, true);
		}
		if(mObserver!=null){
			mObserver.onInvalidated();
		}
	}

	public RandomBubbleView(Context context) {
		this(context, null);
	}

	public RandomBubbleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TAG = this.getClass().getSimpleName();
		mDensity = getContext().getResources().getDisplayMetrics().density;
		mMaxChildWidth *= mDensity;
		mMaxChildHeight *= mDensity;
	}

	protected void computeCanvasRegion(int w, int h, boolean fromUser) {
		int pl = getPaddingLeft(), pr = getPaddingRight();
		int pt = getPaddingTop(), pb = getPaddingBottom();
		mRecFrame.set(pl, pt, w - pr, h - pb);
		mMinChildCount = (int) (mRecFrame.width() * mRecFrame.height()
				* mChildCountFactor / ((mMaxChildWidth) * (mMaxChildHeight)));
	}

	private void removeInvisibleView() {
		long time = System.currentTimeMillis();
		Iterator<Entry<Integer, ViewData>> iterator = mVisibleView.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, ViewData> entry = iterator.next();
			ViewData vd = entry.getValue();
			vd.updatePosition(time);
			mRecTemp.set(vd.left, vd.top, vd.left + vd.view.getMeasuredWidth(),
					vd.top + vd.view.getMeasuredHeight());
			if (!mRecTemp.intersect(mRecFrame)) {
				if (-1 != removeInvisibleView(entry.getKey(), vd)) {
					iterator.remove();
				}

			}
		}
	}

	private void addNewViewToLayout(int w, int h) {
		int addCount = Math.max(mChildCount, mMinChildCount)
				- mVisibleView.size();
		long time = System.currentTimeMillis();
		mRandom.setSeed(time);
		if (mAdpter != null) {
			for (int i = 0; i < addCount; i++) {
				addNewViewToLayout(w, h, time);
			}
		}
	}

	private void removeOrAddToLayout(int w, int h) {
		removeInvisibleView();
		addNewViewToLayout(w, h);
	}

	private void adjustChildSize(ViewData vd) {
		int oldw = vd.view.getMeasuredWidth();
		int oldh = vd.view.getMeasuredHeight();
		boolean isBig = oldw > mMaxChildWidth || oldh > mMaxChildHeight;
		if (isBig) {
			float wDivH = oldw / oldh, scale = 1;
			if (isBig) {
				scale = wDivH < mMaxWdivH ? mMaxChildHeight / (float) oldh
						: mMaxChildWidth / (float) oldw;
				oldw = (int) (oldw * scale);
				oldh = (int) (oldh * scale);
			}
			vd.view.measure(
					MeasureSpec.makeMeasureSpec(oldw, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(oldh, MeasureSpec.EXACTLY));
		}
	}

	final Rect getRectFrame() {
		return mRecFrame;
	}

	final public boolean isCanvasVisible() {
		return getVisibility() == VISIBLE && mRecFrame != null
				&& !mRecFrame.isEmpty();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopSchedule();
	}

	protected void onFlagPreExchanged(boolean isSave) {

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int right = 0, bottom = 0;
		dd("onLayout(visibleN=%1$d,childN=%2$d)", mVisibleView.size(),
				getChildCount());
		Iterator<Entry<Integer, ViewData>> iterator = mVisibleView.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Integer, ViewData> entry = iterator.next();
			ViewData vd = entry.getValue();
			if (vd.view.getVisibility() != View.GONE) {
				right = vd.left + vd.view.getMeasuredWidth();
				bottom = vd.top + vd.view.getMeasuredHeight();
				vd.view.layout(vd.left, vd.top, right, bottom);
			}
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) {
			mFirstVisible = true;
		} else {
			computeCanvasRegion(w, h, false);
			if (mFirstVisible && !onViewFirstSteped(w, h)) {
				//requestLayout();
			}
			if(mObserver!=null){
				mObserver.onInvalidated();
			}
		}
	}

	public boolean onViewFirstSteped(int w, int h) {
		mFirstVisible =false;
		startSchedual(10, 150);
		return false;
	}

 

	private DataSetObserver mObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			resetAllToEmpty();
			updateRemovedIndex();
			if (isCanvasVisible()) {
				removeOrAddToLayout(mRecFrame.width(), mRecFrame.height());
				requestLayout();
			}
			d(null,"DataSetObserver onChanged");
		}

		@Override
		public void onInvalidated() {
			resetAllToEmpty();
			updateRemovedIndex();
			if (isCanvasVisible()) {
				removeOrAddToLayout(mRecFrame.width(), mRecFrame.height());
				requestLayout();
			}
			d(null,"DataSetObserver onInvalidated");
		}

	};

	// flag 0原始中间, 1 左 ,2右,4上,8下.把屏分成 九宫格.随机坐标根据flag分布到其中一个格子里面.
	private void getNextPosition(Rect rect, int[] pos, int r, int w, int h,
			int flag) {
		float newR = r * mRandom.nextFloat();
		double pi = mRandom.nextFloat() * Math.PI * 2;
		pos[0] = (int) (newR * Math.cos(pi)) + rect.centerX();
		pos[1] = (int) (newR * Math.sin(pi)) + rect.centerY();
		if (!rect.contains(pos[0], pos[1])) {
			getNextPosition(rect, pos, r, w, h, flag);
			return;
		}
		if (ViewUtils.hasFlag(flag, 1)) {
			pos[0] -= w;
		}
		if (ViewUtils.hasFlag(flag, 2)) {
			pos[0] += w;
		}
		if (ViewUtils.hasFlag(flag, 4)) {
			pos[1] -= h;
		}
		if (ViewUtils.hasFlag(flag, 8)) {
			pos[1] += h;
		}
	}

	// avoidVal=0时忽略过滤.//在九宫格内均匀分布.九种关系.
	private int getNextFlag(int[] avoid) {
		int result = POSITION[mRandom.nextInt(9)];
		if (result == avoid[0] || result == avoid[1]) {
			return getNextFlag(avoid);
		}
		return result;
	}

	// n大于等于2. n为轨迹段数.
	private void initChildPath(ViewData vd, int n) {
		mRecTemp.set(mRecFrame);
		ViewUtils.scaleRect(mRecTemp, 0.33333f);
		int w = mRecTemp.width(), h = mRecTemp.height(), r = (int) Math.hypot(
				w, h) / 2;
		int pos[] = new int[2], poss[] = new int[] { -1, -1 };
		int i, flag1 = getNextFlag(poss), flag2 = getNextFlag(poss);
		getNextPosition(mRecTemp, pos, r, w, h, flag1);
		getNextPosition(mRecTemp, poss, r, w, h, flag2);
		mPathHelpr.setFirstPoint(vd.path, poss[0], poss[1], pos[0], pos[1]);
		poss[0] = flag1;
		poss[1] = flag2;
		n = n < 2 ? 0 : n - 2;
		for (i = 0; i < n; i++) {
			flag1 = getNextFlag(poss);
			getNextPosition(mRecTemp, pos, r, w, h, flag1);
			poss[0] = poss[1];
			poss[1] = flag1;
			mPathHelpr.getNextPath(pos[0], pos[1]);
		}
		PathPoint p = vd.path.getPoint(true);
		if (p.mX + mRecFrame.left > mRecFrame.centerX()) {
			pos[0] = mRecFrame.right + mRandom.nextInt(w) + 15
					+ vd.view.getMeasuredWidth();
		} else {
			pos[0] = mRecFrame.left - mRandom.nextInt(w) - 15
					- vd.view.getMeasuredWidth();
		}
		if (p.mY + mRecFrame.top > mRecFrame.centerY()) {
			pos[1] = mRecFrame.bottom + mRandom.nextInt(h) + 15
					+ vd.view.getMeasuredHeight();
		} else {
			pos[1] = mRecFrame.top - mRandom.nextInt(h) - 15
					- vd.view.getMeasuredHeight();
		}
		mPathHelpr.getNextPath(pos[0], pos[1]);
		mPathHelpr.getLastPath();
		vd.left = mRecTemp.left;
		vd.top = mRecTemp.top;
		vd.time = System.currentTimeMillis();
		vd.path.init();
	}

	static class ViewData {
		PathCurve path = new PathCurve(12000);
		OnClickListener clickListener;
		View view;
		int left;
		int top;
		long time = 0;
		int index = -1;

		ViewData(OnClickListener l) {
			this.clickListener = l;
		}

		void setView(View v, int index, long curTime) {
			this.view = v;
			this.index = index;
			this.view.setOnClickListener(clickListener);
		}

		void reset(boolean clean) {
			path.reset();
			if (clean) {
				this.view.setOnClickListener(null);
			}
		}

		void updatePosition(long cur) {
			PathPoint p = path.getValueAt((int) (cur - time));
			if (p != null) {
				left = (int) p.mX;
				top = (int) p.mY;
			} else {
				p = path.getPoint(true);
				if (p != null) {
					left = (int) p.mX;
					top = (int) p.mY;
				} else {
					left = -100000;
					top = -100000;
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (mBubbleListener != null) {
			Iterator<Entry<Integer, ViewData>> iterator = mVisibleView
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, ViewData> entry = iterator.next();
				ViewData vd = entry.getValue();
				if (vd.view.equals(v)) {
					dd("click view index=%1$d key=%2$d", vd.index,
							entry.getKey());
					vd.view.bringToFront();
					mBubbleListener.onBubbleClickListener(this, vd.view,
							vd.index);
					break;
				}
			}
		}
	}

	private BubbleClickListener mBubbleListener = null;

	public void setBubbleClickListener(BubbleClickListener l) {
		mBubbleListener = l;
	}

	public interface BubbleClickListener {
		void onBubbleClickListener(RandomBubbleView p, View v, int index);
	}
	final protected void d(String title, String msg) {
		if (title == null) {
			LogUtils.d(TAG, msg);
		} else {
			LogUtils.d(TAG, title + " -->" + msg);
		}
	}

	final protected void dd(String msg, Object... args) {
		d(TAG, String.format(msg, args));
	}

	final protected void pop(String msg, boolean debug) {
		if (debug) {
			LogUtils.popDebug(msg);
		} else {
			LogUtils.pop(msg);
		}
	}
}
