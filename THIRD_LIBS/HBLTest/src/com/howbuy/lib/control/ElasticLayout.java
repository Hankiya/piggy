package com.howbuy.lib.control;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.howbuy.lib.compont.Linear0to1;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.ViewUtils;

public class ElasticLayout extends LinearLayout implements IAnimChaged {
	private static final int FLAG_BASE = 1;
	public static final int RECORD_TOP = FLAG_BASE,
			RECORD_BOT = FLAG_BASE << 1, RECORD_LEFT = FLAG_BASE << 2,
			RECORD_RIGHT = FLAG_BASE << 3;
	public static final int SCALE_TOP = FLAG_BASE << 8,
			SCALE_BOT = FLAG_BASE << 9, SCALE_LEFT = FLAG_BASE << 10,
			SCALE_RIGHT = FLAG_BASE << 11, SCALE_CENTER = FLAG_BASE << 12;
	public static final int SHADE_TOP = FLAG_BASE << 13,
			SHADE_BOT = FLAG_BASE << 14, SHADE_LEFT = FLAG_BASE << 15,
			SHADE_RIGHT = FLAG_BASE << 16, SHADE_CENTER = FLAG_BASE << 17,
			SHADE_VERTICAL_BAR = FLAG_BASE << 18;
	public static final int SCREEN_TOP = FLAG_BASE << 19,
			SCREEN_BOT = FLAG_BASE << 20, SCREEN_LEFT = FLAG_BASE << 21,
			SCREEN_RIGHT = FLAG_BASE << 22;
	public static final int SLID_FROM_HORIZONAL = FLAG_BASE << 23,
			SLID_FROM_VERTICAL = FLAG_BASE << 24;
	public static final int ANIM_TOP = FLAG_BASE, ANIM_BOT = FLAG_BASE << 1,
			ANIM_LEFT = FLAG_BASE << 2, ANIM_RIGHT = FLAG_BASE << 3,
			ANIM_CENTER = FLAG_BASE << 4, ANIM_CENTER_TOP = FLAG_BASE << 5,
			ANIM_CENTER_BOT = FLAG_BASE << 6, ANIM_SCALE = FLAG_BASE << 7;

	private float mSpeed = 0.8f, mThreshold = 2, mDensity = 0;
	private float mElasticRangeV = 0.65f, mElasticRangeH = 0.5f;
	private int mFlag = 0, mShadeWidth = 20;
	private int mMaxDistanceV, mMaxDistanceH, mTouchRange = 20;
	private int mVisibleHeight, mVisibleWidth, mStartX, mStartY;
	private int mCurOffsetX, mDesDistanceX, mCurOffsetY, mDesDistanceY;
	private ElasticState mState = ElasticState.NORMAL;
	private IElasticEvent mRefreshListen;
	private IScrollable mOverable;
	private ViewGroup.LayoutParams mLayContainerLp = null;
	private RelativeLayout mLayContainer;
	private GroupSide mSider = new GroupSide(this);
	private Linear0to1 mLinear;
	private Interpolator mPolater = new DecelerateInterpolator(2);

	private float mBaseLen, mLastScale;
	private boolean mIsViewGroup = true, mScaling = false, mSliding = false,
			mTouchEnable = true, mEnableElasticBoth = true,
			mNeedAdjust = false, mWrapContent = false, mAnimFillAfter;
	/** Top and bottom shadows colors */
	private static final int[] SHADOWS_COLORS = new int[] { 0x00000000,
			0x66000000, 0xff000000 };
	private int mVisibleSide = 0;
	private final Rect mRect = new Rect();
	private final RectF mRecRat = new RectF(0, 0, 0, 0);
	private GradientDrawable mDrawShadeLeft, mDrawShadeRight,
			mDrawShadeCenLeft, mDrawShadeCenRight;
	private ColorDrawable mDrawLeft, mDrawRight, mDrawTop, mDrawBot, mDrawCen;

	public ElasticLayout(Context context) {
		this(context, null);
	}

	public ElasticLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFlag(IScrollable.SCROLL_TOP | IScrollable.SCROLL_BOT
				| IScrollable.SCROLL_LEFT | IScrollable.SCROLL_RIGHT
				| SCALE_CENTER | SHADE_TOP | SHADE_BOT | SHADE_LEFT
				| SHADE_RIGHT | SHADE_CENTER | SHADE_VERTICAL_BAR | SCREEN_TOP
				| SCREEN_BOT | SCREEN_LEFT | SCREEN_RIGHT);
		setClickable(true);
		initInnerView(context);
		initResourcesIfNecessary();
		mDensity = context.getResources().getDisplayMetrics().density;
		mDensity = Math.max(1, mDensity);
		mTouchRange = (int) (mTouchRange * mDensity);
		mShadeWidth = (int) (mShadeWidth * mDensity);
	}

	private void initResourcesIfNecessary() {
		if (mDrawShadeLeft == null) {
			mDrawShadeLeft = new GradientDrawable(Orientation.LEFT_RIGHT,
					SHADOWS_COLORS);
			mDrawShadeRight = new GradientDrawable(Orientation.RIGHT_LEFT,
					SHADOWS_COLORS);

			mDrawShadeCenRight = new GradientDrawable(Orientation.LEFT_RIGHT,
					SHADOWS_COLORS);
			mDrawShadeCenLeft = new GradientDrawable(Orientation.RIGHT_LEFT,
					SHADOWS_COLORS);
			mDrawLeft = new ColorDrawable(0xff000000);
			mDrawRight = new ColorDrawable(0xff000000);
			mDrawTop = new ColorDrawable(0xff000000);
			mDrawBot = new ColorDrawable(0xff000000);
			mDrawCen = new ColorDrawable(0xff000000);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mSider.onAttchedChanged(true, this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mLinear != null) {
			mLinear.destory();
			mLinear = null;
		}
		mSider.onAttchedChanged(false, this);
	}

	protected void adjustLayerBounds(View v) {
		mRect.left = v.getLeft();
		mRect.top = v.getTop();
		mRect.right = v.getRight();
		mRect.bottom = v.getBottom();
	}

	protected void drawDrawable(Canvas can, Drawable drawer, int alpha) {
		drawer.setAlpha(alpha);
		drawer.setBounds(mRect);
		drawer.draw(can);
	}

	protected void drawLeftRightShade(Canvas can) {
		// int cenAlpha = Math .round(255 * ((float) Math.sqrt((1 -
		// mElasticRangeH) / 2 + mRecRat.left)));
		int cenAlpha = Math
				.round(255 * ((1 - mElasticRangeH) / 2 + mRecRat.left));
		if (hasFlag(mVisibleSide, RECORD_LEFT)) {
			adjustLayerBounds(mSider.getLeftContainer());
			mRect.left = mRect.right - mShadeWidth;
			drawDrawable(can, mDrawShadeLeft, getLayerAlpha(null, RECORD_LEFT));// left
			if (hasFlag(SHADE_CENTER)) {
				mRect.left = mRect.right;
				mRect.right += mShadeWidth;
				drawDrawable(can, mDrawShadeCenLeft, cenAlpha);// left
			}
		}
		if (hasFlag(mVisibleSide, RECORD_RIGHT)) {
			adjustLayerBounds(mSider.getRightContainer());
			mRect.right = mRect.left + mShadeWidth;
		
			drawDrawable(can, mDrawShadeRight,
					getLayerAlpha(null, RECORD_RIGHT));// right
			if (hasFlag(SHADE_CENTER)) {
				mRect.right = mRect.left;
				mRect.left -= mShadeWidth;
			
				drawDrawable(can, mDrawShadeCenRight, cenAlpha);// left
			}
		}
	}

	protected int getLayerAlpha(View v, int sideType) {
		if (v != null) {
			adjustLayerBounds(v);
		}
		int alpha = 0;
		switch (sideType) {
		case RECORD_TOP: {
			alpha = Math.round(255 * (mElasticRangeV - mRecRat.top));
			mRect.left = mSider.getLeftContainer().getRight();
			mRect.right = mSider.getRightContainer().getLeft();
			mRect.bottom -= mSider.getTopRefeshH(true);
			break;
		}
		case RECORD_BOT: {
			alpha = Math.round(255 * (mElasticRangeV - mRecRat.top));
			mRect.left = mSider.getLeftContainer().getRight();
			mRect.right = mSider.getRightContainer().getLeft();
			mRect.top += mSider.getBotRefeshH(true);
			break;
		}
		case RECORD_LEFT: {
			alpha = Math.round(255 * (mElasticRangeH - mRecRat.left));
			break;
		}
		case RECORD_RIGHT: {
			alpha = Math.round(255 * (mElasticRangeH - mRecRat.left));
			break;
		}
		default: {
			alpha = Math.round(255 * Math.max(mRecRat.top, mRecRat.left));
			mRect.left = mSider.getLeftContainer().getRight();
			mRect.right = mSider.getRightContainer().getLeft();
			break;
		}
		}
		return alpha;
	}

	protected void drawLayerShade(Canvas can) {
		if (hasFlag(mVisibleSide, RECORD_LEFT) && hasFlag(SHADE_LEFT)) {
			drawDrawable(can, mDrawLeft,
					getLayerAlpha(mSider.getLeftContainer(), RECORD_LEFT));// left
		}
		if (hasFlag(mVisibleSide, RECORD_RIGHT) && hasFlag(SHADE_RIGHT)) {
			drawDrawable(can, mDrawRight,
					getLayerAlpha(mSider.getRightContainer(), RECORD_RIGHT));// right
		}
		if (hasFlag(mVisibleSide, RECORD_TOP) && hasFlag(SHADE_TOP)) {
			drawDrawable(can, mDrawTop,
					getLayerAlpha(mSider.getTopContainer(), RECORD_TOP));// top
		}
		if (hasFlag(mVisibleSide, RECORD_BOT) && hasFlag(SHADE_BOT)) {
			drawDrawable(can, mDrawBot,
					getLayerAlpha(mSider.getBotContainer(), RECORD_BOT));// bot
		}
		if (hasFlag(SHADE_CENTER)) {
			drawDrawable(can, mDrawCen, getLayerAlpha(mLayContainer, 0));// cen
		}
	}

	@Override
	protected void dispatchDraw(Canvas can) {
		super.dispatchDraw(can);
		if (mLayContainer != null) {
			drawLayerShade(can);
			if (hasFlag(SHADE_VERTICAL_BAR)
					&& (hasFlag(RECORD_LEFT) || hasFlag(RECORD_RIGHT))) {
				drawLeftRightShade(can);// shade;
			}
		}
	}

	@Override
	public void removeAllViews() {
		mLayContainer.removeAllViews();
	}

	@Override
	public void addView(View v, int index, ViewGroup.LayoutParams params) {
		if (mDensity != 0) {
			if (v != null) {
				if (mLayContainer.getChildCount() >= 1) {
					android.util.Log.e("ElasticLayout",
							"ElasticLayout LAYOUT MUST HAVE ONEY ONE CHILD");
					throw new RuntimeException(
							"THIS LAYOUT MUST HAVE ONEY ONE CHILD");
				} else {
					if (v instanceof IScrollable) {
						mOverable = (IScrollable) v;
					} else if (!(v instanceof ViewGroup)) {
						mIsViewGroup = false;
					}
					mLayContainerLp = params;
					mLayContainer.addView(v, -1, -1);
					if (mLayContainerLp == null) {
						mLayContainerLp = new RelativeLayout.LayoutParams(-1,
								-1);
					} else if (mLayContainerLp.height == -1) {
						mLayContainerLp.height = -2;
					}
				}
			}
		} else {
			super.addView(v, index, params);
		}
	}

	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		int measureWmode = MeasureSpec.getMode(wSpec);
		int measureWSize = MeasureSpec.getSize(wSpec);
		int measureHmode = MeasureSpec.getMode(hSpec);
		int measureHSize = MeasureSpec.getSize(hSpec);
		if (measureHmode != MeasureSpec.EXACTLY
				|| measureWmode != MeasureSpec.EXACTLY) {
		}
		if (mWrapContent) {
			int size[] = new int[] { 0, 0 };
			View v = mLayContainer.getChildAt(0);
			if (v != null) {
				v.setLayoutParams(mLayContainerLp);
				ViewUtils.measureView(v,  measureWSize,
						measureHSize, size);
			}
			mVisibleWidth = size[0];
			mVisibleHeight = size[1];
			View parent = (View) getParent();
			parent.getLayoutParams().width = mVisibleWidth;
			parent.getLayoutParams().height = mVisibleHeight;

		} else {
			mVisibleWidth = measureWSize;
			mVisibleHeight = measureHSize;
		}
		mMaxDistanceH = (int) (measureWSize * mElasticRangeH);
		mMaxDistanceV = (int) (measureHSize * mElasticRangeV);
		/*
		 * d(String.format(
		 * "onMeasure([wMod=%1$d , wSize=%2$d],[hMod=%3$d , hSize=%4$d])",
		 * measureWmode, measureWSize, measureHmode, measureHSize) + " getW=" +
		 * getWidth() + " getH=" + getHeight() + " getMW=" + getMeasuredWidth()
		 * + " getMH=" + getMeasuredHeight());
		 */
		super.onMeasure(MeasureSpec.makeMeasureSpec(mVisibleWidth * 3,
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
				mVisibleHeight * 3, MeasureSpec.EXACTLY));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != 0 && h != 0) {
			d(String.format("onSizeChanged  scroll to (%1$d,%2$d)",
					mVisibleWidth, mVisibleHeight));
			mSider.getLeftContainer().getLayoutParams().height = mVisibleHeight;
			mSider.getRightContainer().getLayoutParams().height = mVisibleHeight;
			setElasticRange(mElasticRangeH, false);
			setElasticRange(mElasticRangeV, true);
			scrollTo(mVisibleWidth, mVisibleHeight);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////
	private int getDisableSlidEdge(float dx, float dy, int touchType) {
		int result = 0;
		if (!hasFlag(touchType)) {
			if (Math.abs(dx) > 0 && hasFlag(SLID_FROM_HORIZONAL)) {
				result |= SLID_FROM_HORIZONAL;
			}
			if (Math.abs(dy) > 0 && hasFlag(SLID_FROM_VERTICAL)) {
				result |= SLID_FROM_VERTICAL;
			}
		}
		return result;
	}

	private int getTouchEdgeType(float x, float y) {
		int touchType = 0;
		if (x < mTouchRange || x > mVisibleWidth - mTouchRange) {
			touchType |= SLID_FROM_HORIZONAL;
		}
		if (y < mTouchRange || y > mVisibleHeight - mTouchRange) {
			touchType |= SLID_FROM_VERTICAL;
		}
		return touchType;
	}

	private boolean isTouchOnEdge(int slidFlag, float x, float y) {
		mNeedAdjust = false;
		int touchType = getTouchEdgeType(x, y);
		if (hasFlag(slidFlag & touchType)) {
			return true;
		}
		touchType &= ~slidFlag;
		if (touchType == 0) {
			mNeedAdjust = true;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		int action = e.getAction() & MotionEvent.ACTION_MASK;
		int x = (int) e.getX(0), y = (int) e.getY(0);
		if (mTouchEnable) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				mStartX = x;
				mBaseLen = 0;
				mNeedAdjust = mScaling = false;

				int flag = getSlidFromEdgeFlag();
				mSliding = flag == 0 ? true : isTouchOnEdge(flag, x, y);
				if (mSliding) {
					startRecord(e, 0, 0);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (mScaling) {
					if (e.getPointerCount() == 2) {
						float value = (float) Math.hypot(x - e.getX(1),
								y - e.getY(1));
						float scale = value / mBaseLen;
						if (scale <= 1) {
							mLastScale = scale;
							scaleToVal(scale);
						} else {
							mBaseLen = value;
						}
					}
				} else {
					if (mSliding) {
						handleMove(e);
					}
				}
				break;

			case MotionEvent.ACTION_POINTER_1_DOWN: {
				if (hasFlag(SCALE_CENTER) && getOffsetRadius() < 2
						&& e.getEventTime() - e.getDownTime() < 200) {
					mBaseLen = (float) Math.hypot(x - e.getX(1), y - e.getY(1));
					subFlag(RECORD_TOP | RECORD_BOT | RECORD_LEFT
							| RECORD_RIGHT);
					mSliding = !(mScaling = true);
					scrollTo(0, 0, 0, false, false);
				}
			}
				break;
			case MotionEvent.ACTION_POINTER_1_UP: {
				if (e.getPointerCount() > 2)
					break;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (mSliding) {
					//
					handleUp(e);
					mSliding = false;
				} else {
					if (mScaling && hasFlag(SCALE_CENTER)) {
						smoothScaleToVal(mLastScale, 1);
						mScaling = false;
					}
				}
				break;
			}
		} else {
			mStartX = x;
			mStartY = y;
			mBaseLen = 0;
		}
		if (mState == ElasticState.NORMAL
				|| mState == ElasticState.HEAD_PULL_END
				|| mState == ElasticState.FOOT_PUSH_END) {
			mStartY = (int) e.getY();
			return super.dispatchTouchEvent(e);
		}
		return true;
	}

	private void checkMoveAble(int edgeType, int recordType) {
		if (mOverable != null) {
			if (hasFlag(edgeType) && mOverable.isScrollable(edgeType)
					&& mSider.isScrollable(recordType)) {
				addFlag(recordType);
			} else {
				subFlag(recordType);
			}
		} else if (!mIsViewGroup) {
			if (hasFlag(edgeType) && mSider.isScrollable(recordType)) {
				addFlag(recordType);
			} else {
				subFlag(recordType);
			}
		}
	}

	public void startRecord(MotionEvent e, float dx, float dy) {
		if (!(hasFlag(RECORD_TOP) || hasFlag(RECORD_BOT)
				|| hasFlag(RECORD_LEFT) || hasFlag(RECORD_RIGHT))) {
			mStartY = (int) e.getY();
			checkMoveAble(IScrollable.SCROLL_TOP, RECORD_TOP);
			checkMoveAble(IScrollable.SCROLL_BOT, RECORD_BOT);
			checkMoveAble(IScrollable.SCROLL_LEFT, RECORD_LEFT);
			checkMoveAble(IScrollable.SCROLL_RIGHT, RECORD_RIGHT);
			/*
			 * d("startRecord  top bot left right are false enable : " +
			 * String.format("top=%1$s,bot=%2$s,left=%3$s,right=%4$s",
			 * hasFlag(RECORD_TOP), hasFlag(RECORD_BOT), hasFlag(RECORD_LEFT),
			 * hasFlag(RECORD_RIGHT)));
			 */
		} else {
			if (hasFlag(RECORD_TOP) && hasFlag(RECORD_BOT)) {
				float containY = e.getY();
				if (containY > mVisibleHeight / 2) {
					subFlag(RECORD_TOP);
				} else {
					subFlag(RECORD_BOT);
				}
			} else {
				if (mState == ElasticState.NORMAL) {
					reverseFlag(RECORD_TOP);
					reverseFlag(RECORD_BOT);
					if (hasFlag(RECORD_TOP)) {
						checkMoveAble(IScrollable.SCROLL_TOP, RECORD_TOP);
					}
					if (hasFlag(RECORD_BOT)) {
						checkMoveAble(IScrollable.SCROLL_BOT, RECORD_BOT);
					}
				}
			}

			if (hasFlag(RECORD_LEFT) && hasFlag(RECORD_RIGHT)) {
				float containX = e.getX();
				if (containX > mVisibleWidth / 2) {
					subFlag(RECORD_LEFT);
				} else {
					subFlag(RECORD_RIGHT);
				}
			} else {
				if ((hasFlag(RECORD_LEFT) && dx > 0)
						|| (hasFlag(RECORD_RIGHT) && dx < 0)) {
					reverseFlag(RECORD_LEFT);
					reverseFlag(RECORD_RIGHT);
					if (hasFlag(RECORD_LEFT)) {
						checkMoveAble(IScrollable.SCROLL_LEFT, RECORD_LEFT);
					}
					if (hasFlag(RECORD_RIGHT)) {
						checkMoveAble(IScrollable.SCROLL_RIGHT, RECORD_RIGHT);
					}
				}
			}
		}
	}

	private int adjustMoveDistance(int dx, int max, boolean isX) {
		int adjust = 0;
		if (dx > max) {
			adjust = (dx - max);
			dx = max;
		} else if (dx < -max) {
			adjust = (dx + max);
			dx = -max;
		}
		if (isX) {
			mStartX -= adjust / mSpeed;
		} else {
			mStartY -= adjust / mSpeed;
		}
		return dx;
	}

	public void handleMove(MotionEvent e) {
		float x = e.getX(0), y = e.getY(0);
		int dx = (int) ((mStartX - x) * mSpeed), dy = (int) ((mStartY - y) * mSpeed);
		startRecord(e, dx, dy);
		if (mState != ElasticState.FOOT_PUSH_END && hasFlag(RECORD_BOT)) {
			adjustBotState(mSider.getBotRefeshH(true), dy);
		} else if (mState != ElasticState.HEAD_PULL_END && hasFlag(RECORD_TOP)) {
			adjustTopState(mSider.getTopRefeshH(true), dy);
		}
		if ((hasFlag(RECORD_LEFT) && dx < 0)
				|| (dx > 0 && hasFlag(RECORD_RIGHT))) {
			dx = adjustMoveDistance(dx, mMaxDistanceH, true);
		} else {
			dx = 0;
		}
		dy = adjustMoveDistance(dy, mMaxDistanceV, false);
		if (mNeedAdjust) {
			int disSide = getDisableSlidEdge(dx, dy, getTouchEdgeType(x, y));
			if (hasFlag(disSide, SLID_FROM_HORIZONAL)) {
				dx = 0;
				mStartX = (int) x;
			}
			if (hasFlag(disSide, SLID_FROM_VERTICAL)) {
				dy = 0;
				mStartY = (int) y;
			}
			if (dx == 0 && dy == 0)
				return;
		}
		if (mState == ElasticState.FOOT_PUSH_START
				|| mState == ElasticState.FOOT_PUSH_MID
				|| mState == ElasticState.HEAD_PULL_START
				|| mState == ElasticState.HEAD_PULL_MID) {
			if (mEnableElasticBoth) {
				scrollTo(0, dx, dy, false, false);
			} else {
				if (Math.abs(dx) > Math.abs(dy)) {
					mStartY = (int) y;
					scrollTo(0, dx, 0, false, false);
				} else {
					mStartX = (int) x;
					scrollTo(0, 0, dy, false, false);
				}
			}
		} else {
			scrollTo(0, dx, 0, false, false);
		}

	}

	public void adjustTopState(int topHeight, int dy) {
		ElasticState preState = null;
		switch (mState) {
		case HEAD_PULL_MID: {
			if (dy <= 0 && (-dy < topHeight * mThreshold)) {
				preState = mState;
				mState = ElasticState.HEAD_PULL_START;
			} else if (dy >= 0) {
				preState = mState;
				mState = ElasticState.NORMAL;
			}
		}
			break;
		case HEAD_PULL_START: {
			if (topHeight != 0 && -dy >= topHeight * mThreshold) {
				preState = mState;
				mState = ElasticState.HEAD_PULL_MID;
			} else if (dy >= 0) {
				preState = mState;
				mState = ElasticState.NORMAL;
				// onRefreshComplete(true);
			}
		}
			break;

		default: {// NORMAL
			if (dy <= 0) {
				preState = mState;
				mState = ElasticState.HEAD_PULL_START;
			}
		}
		}

		if (preState != null) {
			mState.setPreState(preState);
			mSider.onTopBotStateChanged(mState, true);
			if (mRefreshListen != null) {
				mRefreshListen.onElasticStateChanged(mState, true, mRecRat);
			}
		}

	}

	public void adjustBotState(int botHeight, int dy) {// dy=(mStartY -
		ElasticState preState = null;
		switch (mState) {
		case FOOT_PUSH_MID: {
			if (dy > 0 && (dy < botHeight * mThreshold)) {
				preState = mState;
				mState = ElasticState.FOOT_PUSH_START;
			} else if (dy <= 0) {
				preState = mState;
				mState = ElasticState.NORMAL;
			}
		}
			break;
		case FOOT_PUSH_START: {
			if (botHeight != 0 && dy >= botHeight * mThreshold) {
				preState = mState;
				mState = ElasticState.FOOT_PUSH_MID;
			} else if (dy <= 0) {
				preState = mState;
				mState = ElasticState.NORMAL;
				// onRefreshComplete(false);
			}
		}
			break;

		default: {// NORMAL
			if (dy >= 0) {
				preState = mState;
				mState = ElasticState.FOOT_PUSH_START;
			}
		}
		}
		if (preState != null) {
			mState.setPreState(preState);
			mSider.onTopBotStateChanged(mState, false);
			if (mRefreshListen != null) {
				mRefreshListen.onElasticStateChanged(mState, true, mRecRat);
			}
		}
	}

	public void handleUp(MotionEvent e) {
		if (mState == ElasticState.FOOT_PUSH_MID) {
			// d(String.format("handleUp(which=%1$d,valy=%2$d )",
			// ANIM_CENTER_BOT, -mSider.getBotRefeshH(true)));
			scrollTo(ANIM_CENTER_BOT, 0, mSider.getBotRefeshH(true), true,
					false);

		} else if (mState == ElasticState.HEAD_PULL_MID) {
			// d(String.format("handleUp(which=%1$d,valy=%2$d )",
			// ANIM_CENTER_TOP,-mSider.getTopRefeshH(true)));
			scrollTo(ANIM_CENTER_TOP, 0, -mSider.getTopRefeshH(true), true,
					false);

		} else {
			if (getOffsetRadius() > 0) {
				if (mLayContainer.getChildCount() > 0) {
					scrollTo(judgeToSide(e), true);
				} else {
					scrollTo(0, true);
				}

			}
		}
	}

	public void onRefreshComplete(boolean isHead) {
		scrollTo(0, true);
	}

	protected void smoothScaleToVal(float scaleFrom, float scaleTo) {
		int pl = mLayContainer.getPaddingLeft();
		int pt = mLayContainer.getPaddingTop();
		if (pl == 0 || pt == 0) {
			return;
		} else {
			scaleFrom = (float) (1 - Math.sqrt(scaleFrom));
			scaleTo = (float) (1 - Math.sqrt(scaleTo));
			scaleTo = Math.abs(scaleTo - scaleFrom) * 0.5f;
			int duration = (int) Math.max(150, 1400 * scaleTo);
			startAnim(ANIM_SCALE, duration, -1, false,false);
		}
	}

	protected void scaleToVal(float scale) {
		scale = (float) (1 - Math.sqrt(scale));
		scale *= 0.4f;
		int l = (int) (mVisibleWidth * scale);
		int t = (int) (mVisibleHeight * scale);
		mLayContainer.setPadding(l, t, l, t);
		if (mLayContainer.getBackground() == null) {
			mLayContainer.invalidate();
		}
	}

	private void resetToNormalState() {
		subFlag(RECORD_TOP | RECORD_BOT | RECORD_LEFT | RECORD_RIGHT);
		mVisibleSide = 0;
		mRecRat.set(0, 0, 0, 0);
		if (mState != ElasticState.NORMAL) {
			mState.setPreState(mState);
			mState = ElasticState.NORMAL;
			mSider.onTopBotStateChanged(
					mState,
					mState.equalPre(ElasticState.HEAD_PULL_START)
							|| mState.equalPre(ElasticState.HEAD_PULL_MID)
							|| mState.equalPre(ElasticState.HEAD_PULL_END));
			if (mRefreshListen != null) {
				mRefreshListen.onElasticStateChanged(mState, true, mRecRat);
			}
		} else {
		
		}

	}

	/**
	 * @param to
	 *            one of
	 *            IScrollable.SCROLL_LEFT|IScrollable.SCROLL_RIGHT|IScrollable
	 *            .SCROLL_BOT|IScrollable.SCROLL_TOP
	 * @param anim
	 * @return true for scroll to the destination ,false for scroll to center .
	 */
	public boolean scrollTo(int to, boolean anim) {
		// d(String.format("scrollTo(to=%1$d,anim=%2$s)", to, anim));
		int type = ANIM_CENTER;
		boolean reset = mState != ElasticState.NORMAL;
		int dx = 0, dy = 0;
		if (to != 0) {
			if (hasFlag(to, IScrollable.SCROLL_LEFT)
					&& mSider.getLeft() != null) {
				if (hasFlag(SCREEN_LEFT)) {
					dx = -mVisibleWidth;
					type = ANIM_LEFT;
				}
			} else if (hasFlag(to, IScrollable.SCROLL_RIGHT)
					&& mSider.getRight() != null) {
				if (hasFlag(SCREEN_RIGHT)) {
					dx = mVisibleWidth;
					type = ANIM_RIGHT;
				}
			} else if (hasFlag(to, IScrollable.SCROLL_TOP)
					&& mSider.getTop() != null) {
				if (hasFlag(SCREEN_TOP)) {
					dy = -mVisibleHeight;
					type = ANIM_TOP;
				}
			} else if (hasFlag(to, IScrollable.SCROLL_BOT)
					&& mSider.getBot() != null) {
				if (hasFlag(SCREEN_BOT)) {
					dy = mVisibleHeight;
					type = ANIM_BOT;
				}
			}
		}
		scrollTo(type, dx, dy, anim, reset);
		return !(dx == 0 && dy == 0 && to != 0);
	}

	protected void scrollTo(int which, int valx, int valy, boolean anim,
			boolean resetNormal) {
		if (anim) {
			smoothScrollToVal(which, valx, valy);
		} else {
			scrollToVal(valx, valy);
			if (resetNormal) {
				resetToNormalState();
			}
		}
	}

	private void smoothScrollToVal(int which, int valx, int valy) {
		mCurOffsetX = getScrollX() - mVisibleWidth;
		mCurOffsetY = getScrollY() - mVisibleHeight;
		mDesDistanceX = valx - mCurOffsetX;
		mDesDistanceY = valy - mCurOffsetY;
		if (mDesDistanceX != 0 || mDesDistanceY != 0) {
			float duration = Math.max((float) Math.abs(mDesDistanceX)
					/ mVisibleWidth, (float) Math.abs(mDesDistanceY)
					/ mVisibleHeight);
			duration = Math.max(350, duration * 1000);
			startAnim(which, (int) duration, 0, false,false);
		} else {
			resetToNormalState();
		}
	}

	private void scrollToVal(int valx, int valy) {
		float x = Math.abs(valx), y = Math.abs(valy);
		mVisibleSide = 0;
		if (x > 0) {
			mVisibleSide |= (valx > 0 ? RECORD_RIGHT : RECORD_LEFT);
		}
		if (y > 0) {
			mVisibleSide |= (valy > 0 ? RECORD_BOT : RECORD_TOP);
		}
		mRecRat.left = x / mVisibleWidth;
		mRecRat.top = y / mVisibleHeight;
		mRecRat.right = x / mMaxDistanceH;
		mRecRat.bottom = y / mMaxDistanceV;
		mSider.onScrollChanged(mState, mVisibleSide, (int) x, (int) y, mRecRat);
		scrollTo(mVisibleWidth + valx, mVisibleHeight + valy);
		invalidate();
		if (mRefreshListen != null) {
			mRefreshListen.onElasticStateChanged(mState, false, mRecRat);
		}
	}

	protected float getOffsetRadius() {
		float x = getScrollX() - mVisibleWidth;
		float y = getScrollY() - mVisibleHeight;
		return (float) Math.hypot(x, y);
	}

	private int judgeToSide(MotionEvent e) {
		if (mLayContainer.getChildCount() == 0) {
			return 0;
		}
		int side = 0;
		float x = getScrollX() - mVisibleWidth;
		float y = getScrollY() - mVisibleHeight;
		float ax = Math.abs(x) / mVisibleWidth;
		float ay = Math.abs(y) / mVisibleHeight;
		if (mElasticRangeH < 1) {
			x = ax = 0;
		}
		// 有下拉刷新时,是不可以上下循环的.
		if (mElasticRangeV < 1
				|| (mSider.getBotRefeshH(true) > 0 || mSider
						.getTopRefeshH(true) > 0)) {
			y = ay = 0;
		}
		if (ax != ay) {
			boolean switchAble = Math.max(ax, ay) > 0.5f;
			if (!switchAble) {
				long time = e.getEventTime() - e.getDownTime();
				float vx = Math.abs(x) * 1000 / time;
				float vy = Math.abs(y) * 1000 / time;
				if (Math.max(vx, vy) > 1000) {
					ax = vx;
					ay = vy;
					switchAble = true;
				}
			}
			if (switchAble) {
				if (ax > ay) {
					side = x > 0 ? IScrollable.SCROLL_RIGHT
							: IScrollable.SCROLL_LEFT;
				} else {
					side = y > 0 ? IScrollable.SCROLL_BOT
							: IScrollable.SCROLL_TOP;
				}
			}
		}
		return side;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void d(String msg) {
		if (hasFlag(RECORD_TOP)) {
			android.util.Log.d("bsh", mState.name() + " TopRecored> " + msg);
		} else if (hasFlag(RECORD_BOT)) {
			android.util.Log.d("bsh", mState.name() + " BotRecored> " + msg);
		} else {
			android.util.Log.d("bsh", mState.name() + " NoRecored> " + msg);
		}
	}

	// /////////////////////////////////////////////////
	public void setAdapter(BaseAdapter adp) {
		if (adp != null) {
			adp.registerDataSetObserver(mObserver);
		}
		if (mSider.getAdpter() != null) {
			mSider.getAdpter().unregisterDataSetObserver(mObserver);
		}
		mSider.setAdpter(adp);
	}

	public BaseAdapter getAdapter() {
		return mSider.getAdpter();
	}

	public int getCurIndex() {
		return mSider.getCurIndex();
	}

	// /////////////////////////////////////////////////

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
		mFlag &= ~flag;
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

	final public boolean startAnim(int which, int duration, int period,
			boolean animFillAfter,boolean reversal) {
		boolean result = getLinear().start(which, duration, period,reversal);
		setAnimFillAfter(result ? animFillAfter : mAnimFillAfter);
		return result;
	}

	final public void stopAnim(boolean animFillAfter) {
		boolean result, before = mAnimFillAfter;
		mAnimFillAfter = animFillAfter;
		result = getLinear().stop();
		setAnimFillAfter(result ? mAnimFillAfter : before);
	}

	public void setAnimFillAfter(boolean fillAfter) {
		mAnimFillAfter = fillAfter;
	}

	private Linear0to1 getLinear() {
		if (mLinear == null) {
			mLinear = new Linear0to1(getHandler(),mPolater, this);
		}
		return mLinear;
	}
	 
	public float getInterpolator(float t) {
		if (mPolater != null) {
			if (t >= 0 && t <= 1) {
				return mPolater.getInterpolation(t);
			}
			return t;
		}
		return t > 0.5f ? 1 : 0;
	}

	public boolean setInterpolator(Interpolator polator) {
		mPolater = polator;
		if (mLinear != null) {
			return mLinear.setInterpolator(polator);
		}
		return true;
	}

	/**
	 * @param @param scrollType
	 *        IScrollable.SCROLL_LEFT|IScrollable.SCROLL_RIGHT|
	 *        IScrollable.SCROLL_BOT|IScrollable.SCROLL_TOP
	 * @param @param enable
	 */
	public void setScrollEnable(int scrollType, boolean enable) {
		scrollType &= (IScrollable.SCROLL_TOP | IScrollable.SCROLL_BOT
				| IScrollable.SCROLL_LEFT | IScrollable.SCROLL_RIGHT);
		if (scrollType != 0) {
			if (enable) {
				addFlag(scrollType);
			} else {
				subFlag(scrollType);
			}
		}
	}

	/**
	 * @param scale
	 *            SCALE_TOP = 256, SCALE_BOT = 512, SCALE_LEFT = 1024,
	 *            SCALE_RIGHT = 2048,SCALE_CENTER
	 * @param @param enable
	 * @return void
	 * @throws
	 */
	public void setScaleEnable(int scale, boolean enable) {
		int scaleCen = scale & SCALE_CENTER;
		int scaleOth = scale
				& (SCALE_TOP | SCALE_BOT | SCALE_LEFT | SCALE_RIGHT);
		if (scaleCen != 0) {
			if (enable) {
				addFlag(scale);
			} else {
				subFlag(scale);
			}
		}
		if (scaleOth != 0) {
			if (enable) {
				mSider.addFlag(scale);
			} else {
				mSider.subFlag(scale);
			}
		}
	}

	/**
	 * @param shadeType
	 *            SHADE_TOP|SHADE_BOT|SHADE_LEFT|SHADE_RIGHT|SHADE_CENTER
	 * @param @param enable
	 * @return void
	 * @throws
	 */
	public void setShadeEnable(int shadeType, boolean enable) {
		shadeType &= (SHADE_TOP | SHADE_BOT | SHADE_LEFT | SHADE_RIGHT | SHADE_CENTER);
		if (shadeType != 0) {
			if (enable) {
				addFlag(shadeType);
			} else {
				subFlag(shadeType);
			}
		}
	}

	/**
	 * @param slidFromType
	 *            SLID_FROM_HORIZONAL|SLID_FROM_VERTICAL
	 * @param enable
	 * @return void
	 * @throws
	 */

	public void setSlidFromEdgeEnable(int slidFromType, boolean enable) {
		slidFromType &= (SLID_FROM_HORIZONAL | SLID_FROM_VERTICAL);
		if (slidFromType != 0) {
			if (enable) {
				addFlag(slidFromType);
			} else {
				subFlag(slidFromType);
			}
		}
	}

	/**
	 * @param sideType
	 *            ElasticLayout.RECORD_TOP|ElasticLayout.RECORD_BOT
	 * @param @param enable
	 * @return void
	 * @throws
	 */
	public void setRefeshEnable(int sideType, boolean enable) {
		mSider.setRefeshEnable(sideType, enable);
	}

	public void setShadeVerticalBarEnable(boolean enable) {
		if (enable) {
			addFlag(SHADE_VERTICAL_BAR);
		} else {
			subFlag(SHADE_VERTICAL_BAR);
		}
	}

	/**
	 * @param animType
	 *            ANIM_TOP|ANIM_BOT|ANIM_LEFT|ANIM_RIGHT
	 * @param @param enable
	 */
	public void setAnimToScreenEnable(int animType, boolean enable) {
		animType &= (SCREEN_TOP | SCREEN_BOT | SCREEN_LEFT | SCREEN_RIGHT);
		if (animType != 0) {
			if (enable) {
				addFlag(animType);
			} else {
				subFlag(animType);
			}
		}
	}

	public int getScrollFlag() {
		return mFlag
				& (IScrollable.SCROLL_TOP | IScrollable.SCROLL_BOT
						| IScrollable.SCROLL_LEFT | IScrollable.SCROLL_RIGHT);
	}

	/**
	 * @param scrollType
	 *            IScrollable.SCROLL_TOP | IScrollable.SCROLL_BOT |
	 *            IScrollable.SCROLL_LEFT | IScrollable.SCROLL_RIGHT
	 * @return boolean
	 * @throws
	 */
	public boolean isScrollEnable(int scrollType) {
		return hasFlag(getScrollFlag(), scrollType);
	}

	public int getScaleFlag() {
		return (mSider.getFlag() & (SCALE_TOP | SCALE_BOT | SCALE_LEFT | SCALE_RIGHT))
				| (mFlag & SCALE_CENTER);
	}

	/**
	 * @param @param scaleType
	 *        SCALE_TOP|SCALE_BOT|SCALE_LEFT|SCALE_RIGHT|SCALE_CENTER;
	 * @return boolean
	 * @throws
	 */
	public boolean isScaleEnable(int scaleType) {
		return hasFlag(getScaleFlag(), scaleType);
	}

	public int getShadeFlag() {
		return mFlag
				& (SHADE_TOP | SHADE_BOT | SHADE_LEFT | SHADE_RIGHT | SHADE_CENTER);
	}

	/**
	 * @param @param animType ANIM_TOP|ANIM_BOT|ANIM_LEFT|ANIM_RIGHT
	 * @param @return
	 */
	public boolean isAnimToScreenAble(int animType) {
		return hasFlag(getAnimToScreenFlag(), animType);
	}

	public int getAnimToScreenFlag() {
		return mFlag & (SCREEN_TOP | SCREEN_BOT | SCREEN_LEFT | SCREEN_RIGHT);
	}

	/**
	 * @param slidFromType
	 *            SLID_FROM_HORIZONAL|SLID_FROM_VERTICAL
	 * @param
	 */
	public boolean isSlidFromEdgeAble(int slidFromType) {
		return hasFlag(getSlidFromEdgeFlag(), slidFromType);
	}

	public int getSlidFromEdgeFlag() {
		return mFlag & (SLID_FROM_HORIZONAL | SLID_FROM_VERTICAL);
	}

	/**
	 * @param @param shadeType
	 *        SHADE_TOP|SHADE_BOT|SHADE_LEFT|SHADE_RIGHT|SHADE_CENTER;
	 * @return boolean
	 * @throws
	 */
	public boolean isShadeEnable(int shadeType) {
		return hasFlag(getShadeFlag(), shadeType);
	}

	public int getRefeshFlag() {
		return mSider.getFlag()
				& (ElasticLayout.RECORD_TOP | ElasticLayout.RECORD_BOT);
	}

	/**
	 * @param @param shadeTypeRECORD_TOP|RECORD_BOT;
	 * @return boolean
	 */
	public boolean isRefeshEnable(int refeshType) {
		return hasFlag(getRefeshFlag(), refeshType);
	}

	public void setElasticBoth(boolean enable) {
		this.mEnableElasticBoth = enable;
	}

	public boolean isElasticBoth() {
		return this.mEnableElasticBoth;
	}

	public void setWrapContent(boolean wrapContent) {
		this.mWrapContent = wrapContent;
	}

	public boolean isWrapContent() {
		return this.mWrapContent;
	}

	public void setHeadFootHandle(GroupSide headfoot) {
		if (headfoot != null) {
			headfoot.wrapOriginal(mSider);
			mSider = headfoot;
		}
	}

	public GroupSide getWrapOld() {
		return mSider;
	}

	public boolean isAnimRun() {
		return mLinear == null ? false : getLinear().isRunning();
	}

	public void setElasticEvent(IElasticEvent l) {
		mRefreshListen = l;
	}

	public void setView(int recordType, View v) {
		mSider.setView(recordType, v);
	}

	public void setHeadFootView(View vh, View vf) {
		mSider.setView(vh, vf, mSider.getLeft(), mSider.getRight());
	}

	public void setHeadFootView(int resh, int resf) {
		mSider.setView(getContext(), resh, resf, -1, -1);
	}

	public void setLeftRightView(View vl, View vr) {
		mSider.setView(mSider.getTop(), mSider.getBot(), vl, vr);
	}

	public void setLeftRightView(int resl, int resr) {
		mSider.setView(getContext(), -1, -1, resl, resr);
	}

	/***
	 * 设置拉动效果幅度建议值(0,1)
	 */
	public void setSpeed(float speed) {
		this.mSpeed = speed;
		mSpeed = Math.min(Math.max(0.05f, mSpeed), 1);
	}

	public void setShadeWidth(float wid) {
		mShadeWidth = (int) (wid * mDensity);
	}

	/***
	 * 设置最大拉动的位置，请在（0.0f-1.0f）之间取值。
	 */
	public void setElasticRange(float range, boolean isVertical) {
		range = Math.min(Math.max(0.01f, range), 1f);
		if (isVertical) {
			this.mElasticRangeV = range;
			int val = (int) ((1 - mElasticRangeV) * mVisibleHeight);
			mSider.setInitPadding(-1, val);
		} else {
			this.mElasticRangeH = range;
			int val = (int) ((1 - mElasticRangeH) * mVisibleWidth);
			mSider.setInitPadding(val, -1);
		}
	}

	/***
	 * 设置拉动和松开状态切换的临界值threshold 1.5f-5之间。
	 */
	public void setThreshold(int threshold) {
		this.mThreshold = threshold;
		mThreshold = Math.min(Math.max(1.5f, mThreshold), 5);
	}

	public float getThreshold() {
		return mThreshold;
	}

	public interface IElasticEvent extends IAnimChaged {
		public boolean onElasticRefresh(boolean isHead, float val);

		public void onElasticStateChanged(ElasticState state, boolean newState,
				RectF rate);

		public void onElasticViewChanged(int pos, View curV, int animFlag);
	}

	public interface IScrollable {
		public static final int SCROLL_TOP = FLAG_BASE << 4,
				SCROLL_BOT = FLAG_BASE << 5, SCROLL_LEFT = FLAG_BASE << 6,
				SCROLL_RIGHT = FLAG_BASE << 7;

		/**
		 * @param sideType
		 *            SCROLL_TOP = 16, SCROLL_BOT = 32, SCROLL_LEFT = 64,
		 *            SCROLL_RIGHT = 128;
		 * @return true for enable scroll for the edge.
		 * @throws
		 */
		public boolean isScrollable(int scrollType);

		public boolean hasScroll(int val, int Type);
	}

	public enum ElasticState {
		HEAD_PULL_MID, HEAD_PULL_START, HEAD_PULL_END, FOOT_PUSH_START, FOOT_PUSH_MID, FOOT_PUSH_END, NORMAL;
		private static ElasticState mPreState = ElasticState.NORMAL;

		public void setPreState(ElasticState state) {
			mPreState = state;
		}

		public ElasticState getPreState() {
			return mPreState;
		}

		public boolean equalPre(ElasticState state) {
			return mPreState == state;
		}
	}

	@Override
	public void onAnimChaged(View nullView,int type, int which, float val, float dval ) {
		if(IAnimChaged.TYPE_CHANGED==type){
			if (which == ANIM_SCALE) {
				scaleToVal(mLastScale + (1 - mLastScale) * val);
			} else {
				scrollTo(which, (int) (mCurOffsetX + mDesDistanceX * val),
						(int) (mCurOffsetY + mDesDistanceY * val), false, false);
			}
		}else if(IAnimChaged.TYPE_START==type){
			mTouchEnable = false;
			if (which != ANIM_SCALE) {
				mSider.onAnimChanged(mState, which, true);
			}
		}else {
			onTweenFinished(which, val);
			mTouchEnable = true;
		}
 
		if (mRefreshListen != null) {
			mRefreshListen.onAnimChaged(null,type, which, val,
					dval);
		}
	}

	public void onTweenFinished(final int which, final float endVal) {
		if (which != ANIM_SCALE) {
			mSider.onAnimChanged(mState, which, false);
		}
		switch (which) {
		case ANIM_SCALE: {
			mLastScale = 0;
			mScaling = false;
		}
			break;
		case ANIM_CENTER: {
			resetToNormalState();
			//
		}
			break;
		case ANIM_CENTER_TOP: {
			mState.setPreState(mState);
			mState = ElasticState.HEAD_PULL_END;
			mSider.onTopBotStateChanged(mState, true);
			if (mRefreshListen == null
					|| !mRefreshListen.onElasticRefresh(true,
							mSider.getTopRefeshH(true))) {
				onRefreshComplete(true);
			}
		}
			break;
		case ANIM_CENTER_BOT: {
			mState.setPreState(mState);
			mState = ElasticState.FOOT_PUSH_END;
			mSider.onTopBotStateChanged(mState, false);
			if (mRefreshListen == null
					|| !mRefreshListen.onElasticRefresh(false,
							mSider.getBotRefeshH(true))) {
				onRefreshComplete(false);
			}
		}
			break;
		default: {
			int anim = which & (ANIM_LEFT | ANIM_RIGHT | ANIM_TOP | ANIM_BOT);
			if (anim != 0) {
				 mSider.exchangeView(mLayContainer, anim);
				scrollTo(0, false);
				if (mRefreshListen != null) {
					mRefreshListen.onElasticViewChanged(mSider.getCurIndex(),
							mLayContainer.getChildAt(0), anim);
				}
			}
		}
		}
	}

	private View addView(LinearLayout p, View child, int w, int h, int gravity) {
		LinearLayout.LayoutParams lpl = new LinearLayout.LayoutParams(w, h, 1);
		lpl.gravity = gravity;
		p.addView(child, lpl);
		return child;
	}

	private void initInnerView(Context c) {
		setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout l, r, t, b;
		LinearLayout m;
		l = (RelativeLayout) addView(this, new RelativeLayout(c), 0, -1, 16);
		m = (LinearLayout) addView(this, new LinearLayout(c), 0, -1, 16);
		m.setOrientation(LinearLayout.VERTICAL);
		r = (RelativeLayout) addView(this, new RelativeLayout(c), 0, -1, 16);
		t = (RelativeLayout) addView(m, new RelativeLayout(c), -1, 0, -1);
		mLayContainer = (RelativeLayout) addView(m, new RelativeLayout(c), -1,
				0, -1);
		b = (RelativeLayout) addView(m, new RelativeLayout(c), -1, 0, -1);
		mSider.setContainer(t, b, l, r);
	}

	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
		}

		@Override
		public void onInvalidated() {
		}
	};
}

/**
 * //GridView judge scroll to bottom and top
 * 
 * @Override public boolean isScrollable(int scrollType) { boolean result=false;
 *           if(hasScroll(scrollType, IScrollable.SCROLL_TOP)){ result= 0 ==
 *           getFirstVisiblePosition(); }else if(hasScroll(scrollType,
 *           IScrollable.SCROLL_BOT)){ result= (getCount() - 1) ==
 *           getLastVisiblePosition(); if(result&&getCount()>0){ int
 *           n=getChildCount()==0?-1:getChildCount()-1; if(n>=0){ Rect r=new
 *           Rect(); getChildVisibleRect(getChildAt(n), r, null);
 *           result=r.bottom<=getHeight(); } } }else{ result= true; } return
 *           result; }
 */
