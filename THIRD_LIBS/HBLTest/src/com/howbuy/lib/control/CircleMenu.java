package com.howbuy.lib.control;

/**
 * to draw a pie also can use google webview such as:
 WebView webView = new WebView(this);
 String url = "http://chart.apis.google.com/chart?cht=p3&chs=300x150&chd=t:30,60,10";
 webView.loadUrl(url);
 setContentView(webView);
 * onAttachToWindow>onMeasure>onSizeChaged>onSurfaceCreated>onSufaceChaged>onSufaceDestoryed>onDetachedFromWindow.
 * everytime when onSizeChanged will call onSurfaceChanged.
 */
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.howbuy.lib.adp.AbsDataAdp;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.utils.ViewUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-21 上午9:12:37
 */

// 所有方向改变都是顺时为正.
// 画饼形是从3点钟方向在顺时开始扫画的.startAngle,sweepAngle.其中sweepAngle为正的时候是顺时扫过的角度,负时为逆时扫过的正角度.
public class CircleMenu extends AbsSfView implements OnLoadCompleteListener {
	protected static final Xfermode XFMODER = new PorterDuffXfermode(
			PorterDuff.Mode.CLEAR);
	protected static MaskFilter MASKFILTER = null;
	protected static final int POS_INNER = 1, POS_OUTER = 2, POS_FRAME = 4;
	private static final int ANIM_ROATE = FLAG_BASE;
	private static final int ANIM_TOGGLE = FLAG_BASE << 1;
	private static final int ANIM_ROATE_SECTOR = FLAG_BASE << 2;
	public static final int FLAG_ENABLE_SELECT = FLAG_BASE << 3;
	public static final int FLAG_ENABLE_SOUND = FLAG_BASE << 4;
	public static final int FLAG_ROATE_BACKGROUD = FLAG_BASE << 5;
	public static final int FLAG_CLICK_SECTOR = FLAG_BASE << 6;
	public static final int FLAG_CLICK_CENTER = FLAG_BASE << 7;
	public static final int FLAG_ROATE_SECTOR = FLAG_BASE << 8;
	public static final int FLAG_ENABLE_ROATE = FLAG_BASE << 9;

	protected static int INNER_SIZE_DEF = 45;
	private final RectF mRecVisible = new RectF();
	private final RectF mRecOuter = new RectF();
	private final RectF mRecInner = new RectF();
	private final RectF mRecFixedInner = new RectF();
	private final PointF mPCent = new PointF();

	private float mAnimValue = 0, mAnimDval = 0;
	private boolean mAnimStop = true, isMoveAble = false;

	private AbsDataAdp<Sector> mAdpter = null;

	private int mFlagPos = FLAG_NONE;
	private float mPrePrePointAngle = -1, mPrePointAngle = -1,
			mCurPointAngle = -1;
	private float mOffsetAngle = 0;
	private int mIndPreIndex = -1, mIndCurIndex = -1;
	private int mPreSelect = -1, mSelect = -1, mFixedSelect = -1,
			mDownSelect = -1;
	private VelocityTracker mVeTraker = null;
	private float mScaleMinRate = -1;// -2 close, -1 open.
	private float mScaleRate = -1;
	private Drawable mBackgroud = null;
	private SoundPool mSoundPool = null;
	private int mSoundId = 0;

	private int mSoundResDef = -1;
	private int mColorBackgroudDef = 0xffaa0000;
	private int mColorInnerDef = 0xffff1111;
	private int mColorDivider = 0xff0000ff;
	private float mDividerSize = 2;
	private float mOutDivInnerRate = 3f;
	private float mAnimRoateSpeed = 0.5f;
	private float mTolerateTouchAngle = 0.4f;
	private float mAdjustInnearAngle = 45, mAdjustOuterAngle = 45,
			mIndicatorAngle = 90;
	private Sector mCenterSector = null, mSelectSector = null;

	public void setIndicatorAngle(float angle) {
		angle = (angle % 360 + 360) % 360;
		this.mIndicatorAngle = angle;
	}

	public void setAdjustOuterAngel(float angle) {
		this.mAdjustOuterAngle = angle;
	}

	public void setAdjustInnerAngle(float angle) {
		this.mAdjustInnearAngle = angle;
	}

	/**
	 * suggest to set between 0.3f and 1f;
	 * 
	 * @param min
	 *            angle that can touch to move the circle .
	 * @return void
	 * @throws
	 */
	public void setTolerateTouchAngle(float angle) {
		if (angle > 0 && angle < 5) {
			this.mTolerateTouchAngle = angle;
		}
	}

	/**
	 * 
	 * @param speed
	 *            value between 0 and 1, the bigger and the faster animate
	 *            roate.
	 * @return void
	 * @throws
	 */
	public void setAnimSpeed(float speed) {
		if (speed >= 0 && speed <= 1) {
			this.mAnimRoateSpeed = speed;
		}
	}

	/**
	 * @param rate
	 *            must bigger than 1,suggest to set 3 nearby.
	 * @return void
	 * @throws
	 */
	public void setRateOutDivInner(float rate) {
		if (rate > 1 && rate < 5) {
			this.mOutDivInnerRate = rate;
		}
	}

	/**
	 * set divider width measuerd in angle. suggest to between [0,6]
	 * 
	 * @param angle
	 *            between 0 and 360, the method no adjust .
	 * @throws
	 */
	public void setDividerSize(int angle) {
		this.mDividerSize = angle;
	}

	public void setColorDivider(int color) {
		this.mColorDivider = color;
	}

	public void setColorInnerDef(int color) {
		this.mColorInnerDef = color;
	}

	public void setColorBackgroudDef(int color) {
		this.mColorBackgroudDef = color;
	}

	public void setSelect(int index) {
		if (mFixedSelect != -1) {
			mPreSelect = mFixedSelect;
		}
		mFixedSelect = index;
		if (hasFlag(FLAG_ENABLE_SELECT)) {
			mSelect = mFixedSelect;
		}
		if (isCanvasVisible()) {
			invalidate();
		}
	}

	public void setCenterSector(Sector sector) {
		mCenterSector = sector;
		if (mCenterSector != null && isCanvasVisible()) {
			mCenterSector.createBmp();
			invalidate();
		}
	}

	public void setSelectSector(Sector sector) {
		this.mSelectSector = sector;
		if (mSelectSector != null && isCanvasVisible()
				&& hasFlag(FLAG_ROATE_SECTOR)) {
			invalidate();
		}
	}

	public void setSoundRes(int resId) {
		mSoundResDef = resId;
		if (mSoundPool != null && mSoundResDef != -1) {
			mSoundId = mSoundPool.load(getContext(), resId, 0);
		}
	}

	/**
	 * flag is some of FLAG_KEEP_SECTOR|FLAG_ROATE_BACKGROUD|FLAG_SELECT_CENTER|
	 * FLAG_SELECT_SECTOR|FLAG_SOUND.
	 * 
	 * @return void
	 * @throws
	 */
	public void setMenuAction(int flag) {
		subFlag(getUserFlag());
		addFlag(flag);
		if (hasFlag(FLAG_ENABLE_SELECT)) {
			mSelect = mFixedSelect;
		} else {
			mSelect = -1;
		}
		if (isCanvasVisible()) {
			invalidate();
		}
	}

	public void setTextSize(float size) {
		mPaint.setTextSize(size);
	}

	public void setTextStroke(float stroke) {
		mPaint.setStrokeWidth(stroke);
	}

	public void setCircleMenuEvent(ICircleMenuEvent l) {
		mListener = l;
	}

	private int getUserFlag() {
		return (FLAG_ENABLE_SELECT | FLAG_CLICK_CENTER | FLAG_CLICK_SECTOR
				| FLAG_ENABLE_SOUND | FLAG_ENABLE_ROATE | FLAG_ROATE_SECTOR | FLAG_ROATE_BACKGROUD)
				& mFlag;
	}

	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			if (measureData()) {
				invalidate();
			}
		}

		@Override
		public void onInvalidated() {
			if (measureData()) {
				invalidate();
			}
		}
	};

	public AbsDataAdp<Sector> getAdapter() {
		return mAdpter;
	}

	public int getCount() {
		return mAdpter == null ? 0 : mAdpter.getCount();
	}

	public boolean isEmpty() {
		return 0 == getCount();
	}

	public void setAdapter(AbsDataAdp<Sector> adp) {
		if (mAdpter != null) {
			mAdpter.onAttachChanged(this, false);
			mAdpter.unregisterDataSetObserver(mObserver);
		}
		mAdpter = adp;
		if (mAdpter != null) {
			mAdpter.registerDataSetObserver(mObserver);
			mAdpter.onAttachChanged(this, true);
		}
		if (isCanvasVisible()) {
			mObserver.onInvalidated();
		}
	}

	public CircleMenu(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		INNER_SIZE_DEF = (int) (INNER_SIZE_DEF * mDensity);
		mPaint.setTextSize(18 * mDensity);
		if (MASKFILTER == null) {
			new BlurMaskFilter(mDensity * 2f, BlurMaskFilter.Blur.SOLID);
		}
		mFlag |= FLAG_ROATE_BACKGROUD | FLAG_ROATE_SECTOR | FLAG_ENABLE_SOUND
				| FLAG_CLICK_SECTOR | FLAG_CLICK_CENTER | FLAG_ENABLE_SELECT
				| FLAG_ENABLE_ROATE;
		Sector.setBounds(mRecInner, mRecOuter, MASKFILTER);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	public boolean toggleMenu(boolean open, boolean anim) {
		if (!(mScaleMinRate < 0 || mAnimStop == false)) {
			if (anim) {
				if (open) {
					if (getMenuOpenState() == -1) {
						return startAnim(ANIM_TOGGLE, 400, 15, false, false);
					}
				} else {
					if (getMenuOpenState() == 1) {
						return startAnim(ANIM_TOGGLE, 400, 15, false, true);
					}
				}
			} else {
				mScaleRate = open ? 1 : mScaleMinRate;
				computeCanvasRegion(getWidth(), getHeight(), false);
				invalidate(null);
				return true;
			}
		}
		return false;
	}

	private float indexToAngleRaw(int index) {
		int n = getCount();
		if (index >= 0 && index < n) {
			for (int i = 0; i < n; i++) {
				if (i == index) {
					return mAdpter.getItem(i).getCenter();
				}
			}
		}
		return -1;
	}

	private int angleToIndexRaw(float angle) {
		angle = (angle % 360 + 360) % 360;
		int n = getCount(), result = n - 1;
		if (n > 0) {
			for (int i = 0; i < n; i++) {
				if (mAdpter.getItem(i).getStart() > angle) {
					result = i - 1;
					break;
				}
			}
			return result;
		}
		return -1;
	}

	private float indexToAngle(int index) {
		float result = indexToAngleRaw(index);
		if (result != -1) {
			result = (result + mOffsetAngle + mAdjustOuterAngle) % 360;
		}
		return result;
	}

	private int angleToIndex(float angle) {
		return angleToIndexRaw(angle - (mOffsetAngle + mAdjustOuterAngle));
	}

	public boolean setToIndex(int index, boolean anim) {
		return setToIndex(0, anim);
	}

	private void roateCenter(Canvas can, float roate, boolean pre) {
		if (roate != 0) {
			if (pre) {
				can.save();
				can.rotate(roate, mPCent.x, mPCent.y);
			} else {
				can.restore();
			}
		}
	}

	private void drawSelectSelector(Canvas can, float roateOuter) {
		if (mSelectSector != null && hasFlag(FLAG_ROATE_SECTOR)) {
			mFixedSelect = mFixedSelect == -1 ? mPreSelect : mFixedSelect;
			if (mFixedSelect == -1) {
				mFixedSelect = mPreSelect = 0;
				if (hasFlag(FLAG_ENABLE_SELECT)) {
					mSelect = 0;
				}
			}
			if (mSelectSector.getSweep() == 0) {
				Sector curSelect = mAdpter.getItem(mFixedSelect);
				mSelectSector.mDAngle = curSelect.mDAngle;
				mSelectSector.mSAngle = curSelect.mSAngle;
			}
			roateOuter += mSelectSector.mTemp * mAnimRate;
			roateCenter(can, roateOuter, true);
			mSelectSector.draw(can, 0, 0, mAnimStop);
			roateCenter(can, roateOuter, false);
		} else {
			if (mSelectSector != null) {
				if (mSelectSector.mDAngle != 0 && hasFlag(FLAG_ROATE_SECTOR)) {
					mSelectSector.draw(can, 0, 0, true);
				}
				mSelectSector.mDAngle = 0;
				mSelectSector.mSAngle = 0;
				mSelectSector.mTemp = 0;
			}
		}
	}

	private void drawBackgroud(Canvas can, float roateOuter) {
		mPaint.setColor(0x77ffffff);
		can.drawRect(mRecBounds, mPaint);
		if (getBackground() == null) {
			if (mBackgroud == null) {
				mPaint.setColor(mColorBackgroudDef);
				can.drawArc(mRecVisible, 0, 360, true, mPaint);
			} else {
				mBackgroud.setBounds((int) mRecVisible.left,
						(int) mRecVisible.top, (int) mRecVisible.right,
						(int) mRecVisible.bottom);
				roateOuter = hasFlag(FLAG_ROATE_BACKGROUD) ? roateOuter : 0;
				roateCenter(can, roateOuter, true);
				mBackgroud.draw(can);
				roateCenter(can, roateOuter, false);
			}
		} else {
			if ((mBackgroud = getBackground()) != null) {
				setBackgroundDrawable(null);
				mBackgroud.setBounds((int) mRecVisible.left,
						(int) mRecVisible.top, (int) mRecVisible.right,
						(int) mRecVisible.bottom);
				mBackgroud.draw(can);
			}
		}
	}

	private void drawOuter(Canvas can, int n, float rate) {
		float txtsize = mPaint.getTextSize(), stroke = mPaint.getStrokeWidth();
		if (getMenuOpenState() == 0) {
			txtsize = Math.max(3, txtsize * mAnimRate);
			stroke = Math.max(0.2f, stroke * mAnimRate);
		}
		boolean pressAble = hasFlag(FLAG_CLICK_SECTOR);
		if (n == 1) {
			mAdpter.getItem(0).draw(can, txtsize, stroke,
					(mSelect == 0) && pressAble);
		} else {
			mPaint.setColor(mColorDivider);
			mPaint.setMaskFilter(MASKFILTER);
			for (int i = 0; i < n; i++) {
				float start = mAdpter.getItem(i).draw(can, txtsize, stroke,
						mSelect == i && pressAble);
				if (mDividerSize > 0 && mColorDivider != 0) {
					can.drawArc(mRecOuter, start, mDividerSize, true, mPaint);
				}
			}
			mPaint.setMaskFilter(null);
		}
	}

	private void drawInner(Canvas can, float rate) {
		if (!mRecFixedInner.isEmpty()) {
			mPaint.setColor(getBackground() == null ? mColorBackgroudDef : 0);
			mPaint.setXfermode(XFMODER);
			can.drawArc(mRecFixedInner, 0, 360, true, mPaint);
			mPaint.setXfermode(null);
			if (mCenterSector == null) {
				mPaint.setColor(mColorInnerDef);
				can.drawArc(mRecFixedInner, 0, 360, true, mPaint);
			} else {
				mCenterSector.drawCenter(can, mRecFixedInner,
						mPaint.getTextSize(), mPaint.getStrokeWidth(),
						mDownSelect == -2 && hasFlag(FLAG_CLICK_CENTER));
			}
		}
	}

	@Override
	protected void onDrawSurface(Canvas can) {
		float rate = (mAnimStop || getMenuOpenState() == 0) ? 1 : mAnimRate;
		float roateInner = mAdjustInnearAngle;
		float roateOuter = mAdjustOuterAngle + mOffsetAngle
				+ (mAnimStop ? 0 : (mAnimValue * mAnimRate));
		drawBackgroud(can, roateOuter - mAdjustOuterAngle);
		if (getCount() > 0) {
			if (getMenuOpenState() != -1) {
				roateCenter(can, roateOuter, true);
				drawOuter(can, getCount(), rate);
				roateCenter(can, roateOuter, false);
			}
			drawSelectSelector(can, roateOuter);
			roateCenter(can, roateInner, true);
			drawInner(can, rate);
			roateCenter(can, roateInner, false);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAdpter != null) {
			mAdpter.onAttachChanged(this, false);
		}
		if (mCenterSector != null) {
			mCenterSector.destoryBmp();
		}
		if (mSoundPool != null) {
			mSoundPool.release();
			mSoundPool = null;
		}
	}

	// 0 for changing,-1 for close,1 for open.
	public int getMenuOpenState() {
		if (mScaleRate == -1) {
			return mScaleMinRate == -1 ? 1 : -1;
		}
		if (mScaleRate <= mScaleMinRate) {
			return -1;
		}
		if (mScaleRate >= 1) {
			return 1;
		}
		return 0;
	}

	// recOuter and recInner and recVisible to change by scale rate.
	// recFixedInner and recFrame is fixed rect .
	@Override
	protected void onFrameSizeChanged(boolean fromUser) {
		if (!fromUser) {
			mScaleMinRate = getMenuOpenState() == 1 ? -1 : -2;
		}
		mRecTemp.set(mRecFrame);
		ViewUtils.scaleRect(mRecFixedInner, mRecTemp, 1f / mOutDivInnerRate);
		if (mScaleMinRate < 0) {
			float wIner = Math
					.max(10,
							mRecFixedInner.width()
									- ViewUtils.getTxtHeight(mPaint
											.getTextSize() * 1.6f));
			if (mScaleMinRate == -1) {
				mScaleMinRate = wIner / getWidth();
				mScaleRate = 1;
			} else {// close
				mScaleMinRate = wIner / getWidth();
				mScaleRate = mScaleMinRate;
			}
		}
		mRecVisible.set(0, 0, getWidth(), getHeight());
		ViewUtils.scaleRect(mRecVisible, mScaleRate);
		ViewUtils.scaleRect(mRecOuter, mRecFixedInner, mOutDivInnerRate
				* mScaleRate);
		ViewUtils.scaleRect(mRecInner, mRecFixedInner, mScaleRate);
		mPCent.set(mRecFixedInner.centerX(), mRecFixedInner.centerY());
	}

	@Override
	protected void onMeasure(int wSpec, int hSpec) {
		// int measureWmode = MeasureSpec.getMode(wSpec);
		int measureWSize = MeasureSpec.getSize(wSpec);
		// int measureHmode = MeasureSpec.getMode(hSpec);
		int measureHSize = MeasureSpec.getSize(hSpec);
		if (measureWSize != 0 && measureHSize != 0) {
			int size = Math.min(measureWSize, measureHSize);
			getSurfaceHolder().setFixedSize(size, size);
			setMeasuredDimension(size, size);
		} else {
			getSurfaceHolder().setFixedSize(INNER_SIZE_DEF * 2,
					INNER_SIZE_DEF * 2);
			setMeasuredDimension(INNER_SIZE_DEF * 2, INNER_SIZE_DEF * 2);
		}
	}

	private void playSound() {
		if (mSoundResDef != -1) {
			try {
				if (mSoundPool == null) {
					mSoundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
					mSoundPool.setOnLoadCompleteListener(this);
					mSoundId = mSoundPool.load(getContext(), mSoundResDef, 0);
				} else {
					mSoundPool.play(mSoundId, 1, 1, 0, 0, 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void touchDown(float x, float y) {
		if (mFlagPos == 7 || (getMenuOpenState() == -1 && mFlagPos != 0)) {
			mDownSelect = -2;
		} else {
			mDownSelect = ((mFlagPos == 6) ? angleToIndex(mCurPointAngle) : -1);
			if (mDownSelect != -1) {
				mPreSelect = mFixedSelect == -1 ? mPreSelect : mFixedSelect;
				mSelect = mDownSelect;
			}
		}
		invalidate();
	}

	private boolean touchMove(float x, float y) {
		if (mDownSelect != -2 && mPrePointAngle != -1) {
			float space = mCurPointAngle - mPrePointAngle;
			if (Math.abs(space) > mTolerateTouchAngle) {
				if (!isMoveAble) {
					if (hasFlag(FLAG_ENABLE_ROATE)) {
						isMoveAble = getMenuOpenState() == 1;
						mDownSelect = -1;
						mSelect = mPreSelect;
					} else {
						int curSel = ((mFlagPos == 6) ? angleToIndex(mCurPointAngle)
								: -1);
						if (curSel != mSelect) {
							mSelect = curSel;
							invalidate();
						}
					}
				} else {
					mOffsetAngle += space;
					mOffsetAngle = (mOffsetAngle + 360) % 360;
					int curIndicator = angleToIndex(mIndicatorAngle);
					if (mIndCurIndex != curIndicator) {
						mIndPreIndex = mIndCurIndex;
						mIndCurIndex = curIndicator;
						dispatchMenuEvent(mIndPreIndex, mIndCurIndex, false);
					}
					invalidate();
				}
			} else {
				mPrePointAngle = mPrePrePointAngle;
			}
		}
		return isMoveAble;
	}

	private void touchUp(float x, float y) {
		boolean hasKeepSelect = hasFlag(FLAG_ENABLE_SELECT);
		int result = -1;
		if (mDownSelect == -2) {
			result = mDownSelect;
		} else {
			if (mDownSelect == mSelect) {
				mFixedSelect = result = mSelect;
			} else {
				mSelect = mPreSelect;
			}
		}
		isMoveAble = false;
		mDownSelect = -1;
		if (!hasKeepSelect) {
			mSelect = -1;
		}
		float vx = 0, vy = 0;
		if (mVeTraker != null) {
			mVeTraker.computeCurrentVelocity(100, 11000);
			vx = mVeTraker.getXVelocity();
			vy = mVeTraker.getYVelocity();
			mVeTraker.recycle();
			mVeTraker = null;
		}
		if (result > -1) {
			boolean selectChanged = mPreSelect != mFixedSelect;
			if (selectChanged) {
				if (mSelectSector != null) {
					Sector curSelect = mAdpter.getItem(mFixedSelect);
					mSelectSector.mDAngle = curSelect.mDAngle;
					mSelectSector.mSAngle = curSelect.mSAngle;
					if (mPreSelect != -1 && hasFlag(FLAG_ROATE_SECTOR)) {
						mSelectSector.mTemp = mAdpter.getItem(mPreSelect)
								.getCenter() - curSelect.getCenter();
						if (mSelectSector.mTemp > 180) {
							mSelectSector.mTemp -= 360;
						} else if (mSelectSector.mTemp < -180) {
							mSelectSector.mTemp += 360;
						}
						int duration = Math.max(200,
								(int) (Math.abs(mSelectSector.mTemp) * 2.5f));
						startAnim(ANIM_ROATE_SECTOR, duration, -1, false, true);
					} else {
						mSelectSector.mTemp = 0;
					}
				} else {

				}
				dispatchMenuEvent(mFixedSelect, mFixedSelect, true);

			} else if (!hasKeepSelect
					&& (mSelectSector == null || !hasFlag(FLAG_ENABLE_ROATE))) {
				dispatchMenuEvent(mFixedSelect, mFixedSelect, true);
			}
			invalidate();
		} else {
			if (result == -2) {
				dispatchMenuEvent(result, result, true);
			} else {
				if (hasFlag(FLAG_ENABLE_ROATE)) {
					mAnimValue = mCurPointAngle - mPrePrePointAngle;
					if (mAnimValue > 150) {
						mAnimValue -= 360;
					} else if (mAnimValue < -150) {
						mAnimValue += 360;
					}
					animRoate(vx, vy, mCurPointAngle, mAnimValue > 0);
				} else {
					invalidate();
				}
			}
		}
	}

	private void animRoate(float vx, float vy, float curAngle, boolean positive) {
		vx = (float) (Math.sin(curAngle * Math.PI / 180) * Math.abs(vx));
		vy = (float) (Math.sin((curAngle + 90) * Math.PI / 180) * Math.abs(vy));
		mAnimValue = (float) (Math.abs(vx + vy) * ((positive) ? 0.45f : -0.45f));
		int index = angleToIndex(mIndicatorAngle - mAnimValue);
		Sector s = index == -1 ? null : mAdpter.getItem(index);
		mAnimValue = mListener == null ? mAnimValue : mListener.onPrepareRoate(
				mAnimValue, s, index);
		float abs = Math.abs(mAnimValue);
		if (abs > 0f) {
			int duration = Math.min(1500, (int) (450 + abs * 4f
					* (1 - mAnimRoateSpeed))), frame = Math.max(10,
					(int) (15 - 12 * abs / duration));
			if (abs < 40) {
				duration -= (200 - abs * (abs / 2) * .25f);
			}
			// d("animUpRoate==>   absRoate="+abs+" duration="+duration+" frame="+frame+" sf="+(abs/duration));
			startAnim(ANIM_ROATE, duration, frame, false, false);
		}
	}

	// if in touch area return true,else return false;
	private boolean parseEvent(float x, float y, int action) {
		mPrePrePointAngle = mPrePointAngle;
		mPrePointAngle = mCurPointAngle;
		mCurPointAngle = ViewUtils.pointToAngle(x, y, mPCent.x, mPCent.y);
		mFlagPos = 0;
		if (ViewUtils.pointInCircle(x, y, mPCent.x, mPCent.y,
				mRecInner.width() / 2)) {
			mFlagPos |= POS_INNER; // 1
		}
		if (ViewUtils.pointInCircle(x, y, mPCent.x, mPCent.y,
				mRecOuter.width() / 2)) {
			mFlagPos |= POS_OUTER; // 2
		}
		if (ViewUtils.pointInCircle(x, y, mPCent.x, mPCent.y,
				mRecVisible.width() / 2)) {
			mFlagPos |= POS_FRAME; // 4
		}
		boolean handed = !(mFlagPos == 0 && action == MotionEvent.ACTION_DOWN);
		return handed;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (!mAnimStop || !isEnabled()) {
			return false;
		}
		int action = MotionEvent.ACTION_MASK & e.getAction();
		float x = e.getX(), y = e.getY();
		boolean handled = parseEvent(x, y, action);
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			touchDown(x, y);
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			if (touchMove(x, y)) {
				if (mVeTraker == null) {
					mVeTraker = VelocityTracker.obtain();
				}
				mVeTraker.addMovement(e);
			}
		}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			touchUp(x, y);
		}
			break;
		}
		return handled;
	}

	@Override
	protected boolean onViewFirstSteped(int w, int h) {
		if (mCenterSector != null) {
			mCenterSector.createBmp();
		}
		if (getCount() > 0) {
			mObserver.onInvalidated();
		}
		return true;
	}

	public boolean measureData() {
		int n = getCount();
		boolean result = n > 0;
		if (n > 0) {
			if (!(result == mAdpter.onMeasureData(0, mDividerSize, null))) {
				result = measureData(n, mDividerSize);
			}
		}
		return result;
	}

	public boolean measureData(int n, float argf) {
		if (n > 0) {
			argf = (n == 1 ? 0 : argf);
			float wSum = 0, avarageAngles = 0, angles = 360 - argf * n;
			int wCount = 0;
			if (!mAdpter.isAllSame(0)) {
				for (int i = 0; i < n; i++) {
					Sector it = mAdpter.getItem(i);
					if (it.getWeight() > 0) {
						wSum += it.getWeight();
						wCount++;
					} else {
						avarageAngles += it.getMinSweepAngle();
					}
				}
			}
			avarageAngles = (wSum == 0 ? angles : avarageAngles);
			float averageAngle = (avarageAngles == 0 ? 0 : avarageAngles / n
					- wCount);
			measureData(angles - avarageAngles, averageAngle, argf, wSum,
					wCount);
		}
		return n > 0;
	}

	private void measureData(float weightAngles, float averageAngle,
			float divider, float wSum, int wN) {
		boolean adjust = (wSum == 0);
		int n = getCount();
		float start = 0, dAngle;
		for (int i = 0; i < n; i++) {
			Sector it = mAdpter.getItem(i);
			it.setStartAngle(start);
			if (it.getWeight() > 0) {
				dAngle = it.setSweepAngle(weightAngles * it.getWeight() / wSum,
						adjust);
			} else {
				dAngle = it.setSweepAngle(averageAngle, adjust);
			}
			start += (dAngle + divider);
		}
	}

	private void dispatchMenuEvent(int a, int b, boolean isClick) {
		if (mListener != null) {
			if (isClick) {
				boolean enable = (a == -2) ? hasFlag(FLAG_CLICK_CENTER)
						: hasFlag(FLAG_CLICK_SECTOR);
				if (enable) {
					Sector s = a == -2 ? mCenterSector : mAdpter.getItem(a);
					mListener.onMenuClick(this, s, a, getMenuOpenState() == 1);
				}
			} else {
				if (hasFlag(FLAG_ENABLE_SOUND)) {
					playSound();
				}
				mListener.onMenuPassIndicator(this, a, b);
			}
		}
	}

	private ICircleMenuEvent mListener = null;

	public interface ICircleMenuEvent {
		public void onMenuClick(CircleMenu v, Sector s, int i, boolean isOpend);

		public void onMenuPassIndicator(CircleMenu v, int pre, int cur);

		public float onPrepareRoate(float roat, Sector indicatorSector,
				int index);
	}

	private void handRoateChange(float dval) {
		mAnimDval += dval * mAnimValue;
		int curIndicator = angleToIndex(mIndicatorAngle
				- (mAnimRate * mAnimValue));
		if (mIndCurIndex != curIndicator) {
			mIndPreIndex = mIndCurIndex;
			mIndCurIndex = curIndicator;
			dispatchMenuEvent(mIndPreIndex, mIndCurIndex, false);
			invalidate();
		} else {
			if (Math.abs(mAnimDval) > 0.1f) {
				// d("onAnimChaged " + " val=" + val + " dval=" + dval +
				// " percent=" + percent +" dAng="+mAnimDval);
				invalidate();
				mAnimDval = 0;
			}
		}
	}

	@Override
	public void onAnimChaged(View nullView, final int type, final int which,
			final float val, final float dval) {
		mAnimRate = formatAnimRate(val, true);
		if (IAnimChaged.TYPE_CHANGED == type) {
			if (hasFlag(which, ANIM_ROATE)) {
				handRoateChange(dval);
			}
			if (hasFlag(which, ANIM_TOGGLE)) {
				mScaleRate = mScaleMinRate + (1 - mScaleMinRate) * mAnimRate;
				onFrameSizeChanged(true);
				invalidate();
			}
			if (hasFlag(which, ANIM_ROATE_SECTOR)) {
				invalidate();
			}

		} else if (IAnimChaged.TYPE_START == type) {
			addFlag(which);
			mAnimStop = false;
		} else {// finished
			mAnimStop = true;
			if (hasFlag(which, ANIM_ROATE)) {
				mOffsetAngle = (mOffsetAngle + mAnimValue + 360) % 360;
				invalidate();
			}
			subFlag(which);
			mAnimValue = 0;
			if (mSelectSector != null) {
				mSelectSector.mTemp = 0;
			}
		}
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		soundPool.play(mSoundId, 1, 1, 0, 0, 1);
	}
}