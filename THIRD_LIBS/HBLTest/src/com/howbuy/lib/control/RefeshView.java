package com.howbuy.lib.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.howbuy.lib.compont.BezierParam;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.lib.interfaces.IAnimChaged;

//http://huaban.com/pins/127879311/
public class RefeshView extends AbsView {
	public static final int ANIM_TO_PRELOADING = FLAG_BASE,
			ANIM_TO_LOADING = FLAG_BASE << 1, ANIM_TO_NORMAL = FLAG_BASE << 2;
	private int mBaseHeight = 45, mVisibleHeight = -1,
			mGravity = Gravity.BOTTOM;
	private float mThreshold = 0, mTxtCenterVerticalOffset = 0, mTextAlpha = 1,
			mTextOffsetY = 0;
	protected BezierParam mBezier;
	private final Rect mRect = new Rect();
	private final RectF mRectBase = new RectF();
	private final Path mPath = new Path();
	private Ball mBall = null;
	private String mText = "松开刷新";
	// ////////////////////////////////////////////////////////
	private int mColorUInner = 0xff394552;
	private int mColorUOuter = 0xaaeeeeee;
	private int mColorText = 0xffffffff;
	private float mSizeTxt = 20f;

	// ////////////////////////////////////////////////////////
	public RefeshView(Context context) {
		this(context, null);
	}

	public RefeshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFlag(0);
		mBaseHeight *= mDensity;
		mSizeTxt *= mDensity;
		mTxtCenterVerticalOffset = ViewUtils.getTxtCenterVerticalOffset(mSizeTxt);
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setTextSize(mSizeTxt);
		mBall = new Ball(mDensity);
		mBezier = new BezierParam(mPath, 0.9f);
	}

	protected void drawObject(Canvas can) {
		mPaint.setColor(mColorUInner);
		can.drawRect(mRectBase, mPaint);
		can.save();
		can.clipRect(mRectBase, Op.XOR);
		if (!mPath.isEmpty()) {
			can.drawPath(mPath, mPaint);
			can.clipPath(mPath, Op.XOR);
		}
		can.drawColor(mColorUOuter);
		can.restore();
		if (mText != null) {
			if (!mPath.isEmpty()) {
				if (mVisibleHeight == mBaseHeight) {
					mPaint.setColor(mColorText);
					if (!(hasFlag(ANIM_TO_PRELOADING) || (mBall.isVisible() && getAnimFlag() == 0))) {
						can.drawText(mText, mRectBase.centerX() + mTextOffsetY,
								mRectBase.centerY() -mTxtCenterVerticalOffset, mPaint);
					}

				} else {
					mPaint.setColor(ViewUtils.color(mColorText, mTextAlpha,
							0 ));
					can.drawText(mText, mRectBase.centerX(), mRectBase.bottom
							+ mTextOffsetY, mPaint);
				}
			} else {
				if (hasFlag(ANIM_TO_NORMAL)) {
					mPaint.setColor(ViewUtils.color(mColorText, mTextAlpha,
							0));
					can.drawText(mText, mRectBase.centerX(),
							mRectBase.centerY() +mTxtCenterVerticalOffset, mPaint);
				}
			}

		}
		mBall.draw(can);
	}

	@Override
	protected void onDraw(Canvas can) {
		super.onDraw(can);
		if (!mRecFrame.isEmpty()) {
			can.save();
			can.clipRect(mRect);
			drawObject(can);
			can.restore();
		}
	}

	private void calBallRoate(boolean fromUser) {
		float cen = mThreshold - mBaseHeight;
		float top = mThreshold + cen + mBaseHeight / 2;
		if (mVisibleHeight < mThreshold) {
			if (fromUser) {
				mTextAlpha = (mVisibleHeight - mBaseHeight) / cen / 4;
				mBall.setRoate(360 * mTextAlpha, false);
			}
		} else {
			if (mVisibleHeight <= top) {
				if (fromUser) {
					top = Math.min( 1, Math.max(0, (mThreshold + cen - mVisibleHeight) / cen));
					mTextAlpha = 1f - top * 0.75f;
					mBall.setRoate(180 - 90 * top, false);
				}
			}
		}
	}

	@Override
	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		if (mVisibleHeight >= mBaseHeight) {
			float cen = mRect.centerX();
			float top = mRect.top + mBaseHeight;
			mBezier.setFirstPoint(mRect.left, top, cen, mRect.bottom);
			mBezier.getNextPath(mRect.right, top, false);
			mBezier.getLastPath(false);
			mPath.close();
			calBallRoate(fromUser);
			if (needInvalidate) {
				invalidate();
			}
		} else {
			mPath.reset();
			mTextAlpha = 1;
		}
		return true;
	}

	@Override
	protected void onFrameSizeChanged(boolean fromUser) {
		if (fromUser == false) {
			mVisibleHeight = -1;
		}
		float d = mBaseHeight * 0.75f;
		float cen = mRecFrame.centerX();
		float r = d * 0.5f;
		mRectBase.set(cen - r, mRecFrame.bottom - d, cen + r, mRecFrame.bottom);
		mBall.setBounds(mRectBase);
		if (mVisibleHeight == -1) {
			mVisibleHeight = mRecFrame.height();
			mRect.set(mRecFrame);
		} else {
			setVisibleHeight(mVisibleHeight, false);
		}
	}

	/**
	 * @param gravity
	 *            Gravity.TOP|Gravity.CENTER|Gravity.CENTER_VERTICAL|Gravity.
	 *            BOTTOM
	 * @return void
	 * @throws
	 */
	public void setGravity(int gravity) {
		gravity = gravity <= 0 ? mGravity : gravity;
		int pre = mGravity;
		mGravity = gravity;
		if (pre != mGravity) {
			requestLayout();
		}
	}

	public void setText(String text) {
		mText = text;
	}

	/**
	 * 
	 * @param height
	 * @return void
	 * @throws
	 */
	public void setVisibleHeight(int height, boolean fromUser) {
		mVisibleHeight = height;
		int pH = getHeight();
		if (pH > 0) {
			mVisibleHeight = Math.min(height, pH);
			if (mVisibleHeight != pH) {
				mRect.set(mRecFrame);
				int pt = getPaddingTop(), pb = getPaddingBottom();
				int canvasH = pH - pt - pb, adjustVal = 0;
				if (Gravity.TOP == mGravity) {
					adjustVal = (int) (mVisibleHeight - canvasH - pt);
					mRect.bottom += adjustVal;

				} else if (Gravity.CENTER == mGravity
						|| Gravity.CENTER_VERTICAL == mGravity) {
					adjustVal = (int) (mVisibleHeight - canvasH - (pt >> 1) - (pb >> 1)) >> 1;
					mRect.top -= adjustVal;
					mRect.bottom += adjustVal;
				} else {
					adjustVal = (int) (mVisibleHeight - canvasH - pb);
					mRect.top -= adjustVal;
				}
				if (!mRect.isEmpty()) {
					mRectBase.set(mRect);
					if (mRect.height() > mBaseHeight) {
						mRectBase.bottom = mRect.top + mBaseHeight;
					}
					notifyDataChanged(true, true);
				}
			}
		}
	}

	public void setRefeshThreshold(float height) {
		mBall.setRoate(0, false);
		mThreshold = Math.max(Math.min(height, mBaseHeight * 5),
				mBaseHeight * 1.5f);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
		requestLayout();
	}

	public void onParentAnimChanged(boolean isStart, boolean readyLoading) {
		if (readyLoading) {
			if (isStart) {
				startAnim(RefeshView.ANIM_TO_PRELOADING, false);
			} else {
				startAnim(RefeshView.ANIM_TO_LOADING, false);
			}
		} else {
			if (isStart) {
				startAnim(RefeshView.ANIM_TO_NORMAL, false);
			} else {
				float d = mBaseHeight * 0.75f;
				float cen = mRecFrame.centerX();
				float r = d * 0.5f;
				mBall.setBounds(cen - r, mRecFrame.bottom - d, cen + r, mRecFrame.bottom);
				mBall.setVisible(true);
			}
		}
	}

	public void startAnim(int animType, boolean fromUser) {
		int duration = 250;
		if (animType == ANIM_TO_PRELOADING) {
			duration = (int) ((mVisibleHeight * duration / (float) mBaseHeight));
			duration = Math.min(duration, 1000);
		} else if (animType == ANIM_TO_NORMAL) {
			duration = 100;
		}
		if (animType == ANIM_TO_NORMAL || animType == ANIM_TO_LOADING) {
			stopAnim(true);
		}
		startAnim(animType, duration, -1, false,false);
	}

	@Override
	public void onAnimChaged(View nullView,final int type, final int which, final float val,
			final float dval) {
		if(IAnimChaged.TYPE_CHANGED==type){
			if (hasFlag(ANIM_TO_PRELOADING)) {
				mTextOffsetY = -val * mBall.getBounds().top * 0.45f;
				mBall.setOffset(false, 0, -dval * mThreshold);
			} else if (hasFlag(ANIM_TO_NORMAL)) {
				mTextAlpha = 1 - val;
			} else if (hasFlag(ANIM_TO_LOADING)) {
				mTextOffsetY = (val - 1) * getWidth();
				postInvalidate();
			}
		}else{
			if (which != 0) {
				if(IAnimChaged.TYPE_START==type){
					addFlag(which);
					mTextOffsetY = 0;
					if (hasFlag(ANIM_TO_LOADING)) {
						mTextOffsetY = -getWidth();
					}
				}else{
					subFlag(which);
					if (hasFlag(which, ANIM_TO_PRELOADING)) {
						mBall.setVisible(false);
					} 
				}
			}
		}
	}
 

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public int getAnimFlag() {
		return mFlag & (ANIM_TO_NORMAL | ANIM_TO_LOADING | ANIM_TO_NORMAL);
	}

	public int getBaseHeight() {
		return mBaseHeight;
	}

	public void setBaseHeight(int baseH) {
		mBaseHeight = baseH;
	}
}
