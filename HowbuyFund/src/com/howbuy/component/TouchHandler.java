package com.howbuy.component;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.howbuy.lib.compont.GlobalApp;

public class TouchHandler {
	private int mMaxTouch = 2;
	private float mTouchSlopArea = 10f;
	private int mTouchSlopTime = 150;
	private int mTouchDownTime = 150;
	private boolean mWaitForUser = true;
	private boolean mUserFocused = false;
	private boolean mUserMoved = false;
	private boolean mAboutToOneTouch = false;
	private boolean mEnableTouch = true;
	private long mAboutToOneTouchTime = 0;

	private RectF mCurArea = new RectF(0, 0, 0, 0);// 事件过程中不自己清除，由外面来清除。
	private ArrayList<PointF> mDownEvent = new ArrayList<PointF>(mMaxTouch);
	private ITouchEvent mTouchListener = null;

	/*
	 * private void d(String title, String msg) { LogUtils.d("FragCharLand",
	 * title + " -->" + msg); }
	 */

	public TouchHandler(Context context, ITouchEvent l) {
		int touchSlop;
		if (context == null) {
			touchSlop = ViewConfiguration.getTouchSlop();
		} else {
			final ViewConfiguration configuration = ViewConfiguration.get(context);
			touchSlop = configuration.getScaledTouchSlop();
		}
		mTouchListener = l;
		mTouchSlopTime = 100;
		mTouchSlopArea = touchSlop * touchSlop / 16;
	}

	public boolean hasUserFocused() {
		return mUserFocused;
	}

	public void resetTouch(boolean enable) {
		mEnableTouch = enable;
		if (!mEnableTouch) {
			mWaitForUser = true;
			mUserFocused = false;
			mUserMoved = false;
			mAboutToOneTouch = false;
			mAboutToOneTouchTime = 0;
		}
		// d("resetTouch", "mEnableTouch=" + mEnableTouch);
	}

	public RectF getCurArea() {
		return mCurArea;
	}

	public int getTouchPointCount() {
		return mDownEvent.size();
	}

	public boolean onTouchEvent(MotionEvent e) {
		boolean handled = false;
		int origionalAction = e.getAction();
		int pointIndex = (origionalAction & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT; // 等效于
		int action = e.getAction() & MotionEvent.ACTION_MASK;
		if (pointIndex >= mMaxTouch || !mEnableTouch) {
			return false;
		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mUserMoved = false;
			mDownEvent.clear();
			mDownEvent.add(new PointF(e.getX(pointIndex), e.getY(pointIndex)));
			handled = handDown(pointIndex, e);
			break;
		case MotionEvent.ACTION_UP:
			if (pointIndex < mDownEvent.size()) {
				mDownEvent.remove(pointIndex);
				handled = handUp(pointIndex, e);
			}
			mUserMoved = false;
			break;
		case MotionEvent.ACTION_MOVE:
			handled = handMove(pointIndex, e);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mDownEvent.add(pointIndex, new PointF(e.getX(pointIndex), e.getY(pointIndex)));
			handled = handDown(pointIndex, e);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mDownEvent.remove(pointIndex);
			handled = handUp(pointIndex, e);
			break;
		case MotionEvent.ACTION_CANCEL:
			handled = handCancle(pointIndex, e);
			break;
		}
		return handled;
	}

	private boolean handDown(int pointIndex, MotionEvent e) {
		// d("handDown", "pointIndex=" + pointIndex + " e.preasure=" +
		// e.getPressure(pointIndex));
		if (pointIndex == 0) {
			mWaitForUser = true; // 是否在等用户交互。
			mUserFocused = false;// 是否用户已经交互上了。
			GlobalApp.getApp().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					/*
					 * d("handDown", "post down size=" + mDownEvent.size() +
					 * " ,mUserMoved=" + mUserMoved + ",mEnableTouch=" +
					 * mEnableTouch);
					 */
					if (mDownEvent.size() > 0 && mUserMoved == false && mEnableTouch) {
						mUserFocused = true;
						PointF p = mDownEvent.get(0);
						if (mDownEvent.size() == 1) {
							mCurArea.left = mCurArea.right = p.x;
							mCurArea.top = mCurArea.bottom = p.y;
						} else {
							mCurArea.left = p.x;
							mCurArea.top = p.y;
							p = mDownEvent.get(1);
							mCurArea.right = p.x;
							mCurArea.bottom = p.y;
						}
						if (mTouchListener != null) {
							mTouchListener.onTouchTimeOut(mDownEvent.size());
						}
						mUserMoved = true;
					}
				}
			}, mTouchDownTime);
		} else {
			if (mUserFocused) {// one touch to two touch.
				mCurArea.right = e.getX(pointIndex);
				mCurArea.bottom = e.getY(pointIndex);
				if (mTouchListener != null) {
					mTouchListener.onLineChanged(1, 2);
				}
			}
		}
		return true;
	}

	private boolean handMove(int pointIndex, MotionEvent e) {
		// d("handMove", "mWaitForUser=" + mWaitForUser + " , mUserFocused=" +
		// mUserFocused);
		if (!mWaitForUser && !mUserFocused) {
			return false;
		}
		if (!mUserFocused) {
			mWaitForUser = mTouchSlopTime > e.getEventTime() - e.getDownTime();
			PointF p = mDownEvent.get(0);
			mUserMoved = true;
			if (!mWaitForUser) {
				if (mDownEvent.size() == 2) { // 在特定时间后两个手指同时按下了。
					mCurArea.left = p.x;
					mCurArea.top = p.y;
					p = mDownEvent.get(1);
					mCurArea.right = p.x;
					mCurArea.bottom = p.y;
					mUserFocused = true;
				} else {
					float dx = e.getX(pointIndex) - p.x;
					float dy = e.getY(pointIndex) - p.y;
					if (dx * dx + dy * dy < mTouchSlopArea) {
						mCurArea.set(p.x, p.y, p.x, p.y);
						mUserFocused = true;
					}
				}
				if (mTouchListener != null) {
					mTouchListener.onTouchTimeOut(mUserFocused ? mDownEvent.size() : 0);
				}
			} else {
				if (mDownEvent.size() == 2) {
					mUserMoved = false;
					// d("handMove", "move in two point mUserMoved=false");
				} else {
					float dx = e.getX(pointIndex) - p.x;
					float dy = e.getY(pointIndex) - p.y;
					if (dx * dx + dy * dy < mTouchSlopArea) {
						mUserMoved = false;
						// d("handMove",
						// "move in mTouchSlopArea mUserMoved=false");
					}
				}
			}
		} else {
			float x = e.getX(0);
			float y = e.getY(0);
			if (mDownEvent.size() == 1) {// move one touch.
				mCurArea.set(x, y, x, y);
				if (mAboutToOneTouch) {
					mAboutToOneTouch = mTouchSlopTime > e.getEventTime() - mAboutToOneTouchTime
							- 50;
					if (mAboutToOneTouch) {
						float dx = x - mCurArea.left;
						float dy = y - mCurArea.top;
						if (dx * dx + dy * dy > mTouchSlopArea * 6f) {
							mCurArea.right = 0;
							mCurArea.bottom = 0;
							mAboutToOneTouch = false;
							if (mTouchListener != null) {
								mTouchListener.onLineChanged(2, 1);
							}
						}
					} else {
						mCurArea.right = 0;
						mCurArea.bottom = 0;
						mAboutToOneTouch = false;
						if (mTouchListener != null) {
							mTouchListener.onLineChanged(2, 1);
						}
					}

				}
			} else {// move two touch.
				if (e.getPointerCount() == 2) {
					mCurArea.left = x;
					mCurArea.top = y;
					x = e.getX(1);
					y = e.getY(1);
					mCurArea.right = x;
					mCurArea.bottom = y;
				}
			}
			if (mTouchListener != null) {
				mTouchListener.onLineMove(mDownEvent.size());
			}
		}
		return true;
	}

	private boolean handUp(int pointIndex, MotionEvent e) {
		if (mUserFocused) {
			// d("handUp", "pointIndex=" + pointIndex + " downCount=" +
			// mDownEvent.size());
			if (mDownEvent.size() == 1) {// two touch to one touch
				mAboutToOneTouch = true;
				mAboutToOneTouchTime = e.getEventTime();
				if (pointIndex == 0) {
					float x = mCurArea.left;
					float y = mCurArea.top;
					mCurArea.left = mCurArea.right;
					mCurArea.top = mCurArea.bottom;
					mCurArea.right = x;
					mCurArea.bottom = y;
				}
			} else {
				if (mTouchListener != null) {
					mTouchListener.onLineChanged(mAboutToOneTouch ? 2 : 1, 0);
				}

			}
		}
		if (mTouchListener != null) {
			if (e.getEventTime() - e.getDownTime() < mTouchDownTime) {
				mTouchListener.onTabSingle(e);
			}
		}
		return (!mWaitForUser && !mUserFocused);
	}

	private boolean handCancle(int pointIndex, MotionEvent e) {
		// d("handCancle", "pointIndex=" + pointIndex + ",e=" + e);
		return (!mWaitForUser && !mUserFocused);
	}

	public interface ITouchEvent {
		public void onTouchTimeOut(int lineN);

		public void onLineChanged(int preLineN, int curLineN);

		public void onLineMove(int lineN);

		public void onTabSingle(MotionEvent e);
	}
}