package com.howbuy.control;

import howbuy.android.palmfund.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class QuickReturnFooter implements AnimationListener {
	protected static final String TAG = "QuickReturnFooter";
	private View mFootView;
	private FrameLayout.LayoutParams mFootLp;
	private int mFootHeight;
	private int headerTop;
	private int mContentId;
	private int mFootId;
	private Context mCx;
	private LayoutInflater mLf;
	private View mContent;
	private ViewGroup mScrollView;
	private ViewGroup mRootFrame;
	private Animation mAnim;
	/**
	 * True if the last scroll movement was in the "up" direction.
	 */
	private boolean mScrollDown, mAnimRun;
	private int mAnimType = 0, mAnimLastType = 0;

	private float mLastScrollTop = -1;
	private float mScrollThreshold = 2;
	/**
	 * Maximum time it takes the show/hide animation to complete. Maximum
	 * because it will take much less time if the header is already partially
	 * hidden or shown.
	 * <p>
	 * In milliseconds.
	 */
	private static final long ANIMATION_DURATION = 350;

	public interface OnSnappedChangeListener {
		void onSnappedChange(boolean snapped);
	}

	public QuickReturnFooter(Context context, int contentResId, int headerResId) {
		this.mCx = context;
		this.mContentId = contentResId;
		this.mFootId = headerResId;
		mScrollThreshold *= mCx.getResources().getDisplayMetrics().density;
	}

	/*private void d(String title, String msg) {
		LogUtils.d("Quicker", title + ">>" + msg);
	}*/

	public View createView() {
		mLf = LayoutInflater.from(mCx);
		mContent = mLf.inflate(mContentId, null);
		mFootView = mLf.inflate(mFootId, null);
		mFootLp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mFootLp.gravity = Gravity.BOTTOM;
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT,
				MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT,
				MeasureSpec.EXACTLY);
		mFootView.measure(widthMeasureSpec, heightMeasureSpec);
		mFootHeight = mFootView.getMeasuredHeight();
		createScrollView();
		return mRootFrame;
	}

	private void createScrollView() {
		mRootFrame = (FrameLayout) mLf.inflate(R.layout.qrh__scrollview_container, null);
		VerticalScrollView scrollView = (VerticalScrollView) mRootFrame
				.findViewById(R.id.rqh__scroll_view);
		scrollView.setOnScrollChangedListener(mOnScrollChangedListener);
		mScrollView = scrollView;
		mRootFrame.addView(mFootView, mFootLp);
		mScrollView.addView(mContent, 0);
		mScrollView.requestFocus();
		mFootView.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						mFootView.getViewTreeObserver().removeOnPreDrawListener(this);
						mScrollView.scrollTo(0, 0);
						return true;
					}
				});

	}

	/*
	 * final protected void d(String title, String msg) { if (title == null) {
	 * LogUtils.d(TAG, msg); } else { LogUtils.d(TAG, title + " -->" + msg); } }
	 */

	private VerticalScrollView.OnScrollChangedListener mOnScrollChangedListener = new VerticalScrollView.OnScrollChangedListener() {
		@Override
		public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
			if (!mAnimRun && t >= 0) {
				if (mLastScrollTop == -1) {
					mLastScrollTop = t;
					mScrollDown = (t - oldt) > 0;
					return;
				} else {
					float gap = t - mLastScrollTop;
					if (Math.abs(gap) > mScrollThreshold) {
						mScrollDown = (t - mLastScrollTop) > 0;
						if (animShowFooter(!mScrollDown, false)) {
						}
						mLastScrollTop = t;
					}

				}
			}
		}

		@Override
		public void onScrollIdle() {
			/*
			 * if (animShowFooter(!mScrollDown, false)) { d("onScrollIdle",
			 * "show footer " + (!mScrollDown) + " ,from lastPost false"); }
			 */
		}
	};

	private void cancelAnimation() {
		if (mAnim != null) {
			mFootView.clearAnimation();
			if (mAnimType == 1) {
				headerTop = 0;
				mAnimLastType = mAnimType;
			} else if (mAnimType == -1) {
				headerTop = 0;
				mAnimLastType = mAnimType;
			}
			mAnimType = 0;
			mAnim = null;
		}
	}

	public boolean animShowFooter(boolean show, boolean fromLastPos) {
		boolean handled = false;
		if (show) {
			mAnimType = 1;
			if (mAnimType != mAnimLastType) {
				if (mContent.getHeight() - (mScrollView.getHeight() + mScrollView.getScrollY()) > mFootHeight) {
					handled = animateHeader(fromLastPos ? headerTop : -mFootHeight, 0);
				}
			}
		} else {
			mAnimType = -1;
			if (mAnimType != mAnimLastType) {
				handled = animateHeader(fromLastPos ? headerTop : 0, -mFootHeight);
			}
		}
		return handled;
	}

	/**
	 * Animates the marginTop property of the header between two specified
	 * values.
	 * 
	 * @param startTop
	 *            Initial value for the marginTop property.
	 * @param endTop
	 *            End value for the marginTop property.
	 */
	private boolean animateHeader(final float startTop, final float endTop) {
		if (startTop != endTop) {
			cancelAnimation();
			final float deltaTop = endTop - startTop;
			mAnim = new Animation() {
				@Override
				protected void applyTransformation(float interpolatedTime, Transformation t) {
					headerTop = (int) (startTop + deltaTop * interpolatedTime);
					mFootLp.bottomMargin = headerTop;
					mFootView.setLayoutParams(mFootLp);
				}
			};
			mAnim.setAnimationListener(this);
			long duration = (long) (deltaTop / (float) mFootHeight * ANIMATION_DURATION);
			mAnim.setDuration(Math.abs(duration));
			mFootView.startAnimation(mAnim);
			return true;
		}
		return false;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		mAnimRun = true;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mAnimRun = false;
		cancelAnimation();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}
}
