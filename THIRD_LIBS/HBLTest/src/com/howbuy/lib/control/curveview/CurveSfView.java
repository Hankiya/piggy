package com.howbuy.lib.control.curveview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.howbuy.lib.control.AbsSfView;
import com.howbuy.lib.entity.CharDataType;
import com.howbuy.lib.entity.ClickType;
import com.howbuy.lib.entity.CrossType;
import com.howbuy.lib.interfaces.IAnimChaged;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;
import com.howbuy.lib.interfaces.ICurveEvent;
import com.howbuy.lib.utils.ViewUtils;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-1-17 上午9:25:29
 */
public class CurveSfView extends AbsSfView {
	protected static final int FLAG_INVALIDATE_CACHE = FLAG_BASE;
	public static final int FLAG_VISIBLE_ANIM = FLAG_BASE << 1;
	protected int mBackGroudColor = 0x66aaaaaa;
	protected boolean isIncrease = true;
	protected boolean mAnimStop = true;
	protected ArrayList<CharDataType> mData = new ArrayList<CharDataType>();
	protected IAnimChaged mListen = null;
	protected boolean mCrossVisible = false, mCrossMoveAble = false, mSelectAreaVisible = false,
			mScaling = false;
	protected GestureDetector mGesture;
	protected ICurveEvent mCurveListener = null;
	protected final int[] mSelect = new int[] { 0, -1 };
	protected int mIndex = 0, mTempSelect = -1;
	protected float mDownX = 0, mPreX, mMoveAreaLen = 0, mBaseLen = -1;
	protected Scroller mScroller = null;
	protected final RectF mRecCurve = new RectF();
	protected final RectF mRecSelect = new RectF();
	protected CurveSetting mSetting = new CurveSetting(this.getContext());
	protected CurveControl mCurveCtrl = new CurveControl(mRecFrame, mRecCurve, mSetting,
			"CURVE_CTRL");

	protected void setCurveControl(CurveControl ctrl) {
		if (ctrl != null) {
			mCurveCtrl = ctrl;
		}
	}

	public CurveSfView(Context cx, AttributeSet attr) {
		super(cx, attr);
		setLongClickable(true);// add.
		mGesture = new GestureDetector(new CurveGesture());// add.
		mScroller = new Scroller(cx, new LinearInterpolator());
	}

	protected boolean addCurve(ICharType charType, ArrayList<? extends ICharData> datas,
			boolean measure) {
		boolean result = false;
		if (mCurveCtrl.addCurve(charType, datas, measure)) {
			if (measure) {
				invalidate(measure, true);
			}
			result = true;
		}

		return result;
	}

	public boolean addCurve(ICharType charType, ArrayList<? extends ICharData> datas) {
		return addCurve(charType, datas, true);
	}

	public boolean addCurveData(ICharType charType, ArrayList<? extends ICharData> datas,
			boolean end, boolean ignoreIfNotExist) {
		return addCurveData(charType, datas, end, ignoreIfNotExist, true);
	}

	protected boolean addCurveData(ICharType charType, ArrayList<? extends ICharData> datas,
			boolean end, boolean ignoreIfNotExist, boolean measure) {
		boolean result = false;
		Curve curve = mCurveCtrl.findCurve(charType, true);
		if (curve != null) {
			result = mCurveCtrl.addCurveData(curve, datas, end);
			if (result && curve.isVisible()) {
				if (measure) {
					invalidate(measure, true);
				}
			}
			return result;
		} else if (!ignoreIfNotExist) {
			addCurve(charType, datas, measure);
		}
		return result;
	}

	public boolean addCurveData(HashMap<ICharType, ArrayList<? extends ICharData>> datas,
			boolean end, boolean ignoreIfNotExist) {
		boolean result = false;
		if (datas != null) {
			Iterator<Entry<ICharType, ArrayList<? extends ICharData>>> list = datas.entrySet()
					.iterator();
			while (list.hasNext()) {
				Entry<ICharType, ArrayList<? extends ICharData>> entry = list.next();
				result |= addCurveData(entry.getKey(), entry.getValue(), end, ignoreIfNotExist,
						false);
			}
		}
		if (result) {
			invalidate(result, true);
		}
		return result;
	}

	public void changeCrossShow(boolean visible, MotionEvent e) {
		float x = e.getX(), y = e.getY();
		if (!visible) {
			mCrossMoveAble = mCrossVisible = false;
			mSelectAreaVisible = false;
			invalidate(false, false);
			if (mCurveListener != null) {
				mCurveListener.onCrossEvent(CrossType.CROSS_HIDE,
						mCurveCtrl.getValuesAt(mSelect[mIndex]), x, y, mSelect[mIndex]);
			}
		} else {
			mSelect[mIndex] = pointToIndex(x, y, false);

			if (mSelect[mIndex] != -1 && mRecFrame.contains((int) x, (int) y)) {
				mCrossMoveAble = mCrossVisible = true;
				invalidate(false, false);
				if (mCurveListener != null) {
					mCurveListener.onCrossEvent(CrossType.CROSS_SHOW,
							mCurveCtrl.getValuesAt(mSelect[mIndex]), x, y, mSelect[mIndex]);
				}
			}
		}
	}

	public void cleanCurve(boolean cleanData, boolean cleanCurve) {

		mCurveCtrl.destory(cleanData, cleanCurve);
		mSelect[0] = 0;
		mSelect[1] = 0;
		mCrossVisible = mCrossMoveAble = false;
		mSelectAreaVisible = false;
		mMoveAreaLen = 0;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			float dx = (mScroller.getCurrX() - mBaseLen) * 0.5f;
			int moveType = mCurveCtrl.moveCurves(dx);
			if (moveType != 0) {
				if (mSetting.mEnableAutoCalcuMinMax) {
					invalidate(moveType == 1, true);
				} else {
					invalidate(false, true);
				}
			}
			mBaseLen = mScroller.getCurrX();
		} else {
			mScroller.abortAnimation();
			mBaseLen = -1;
		}
	}

	public boolean containt(ICharType charType, boolean all) {
		return mCurveCtrl.findCurve(charType, all) != null;
	}

	public void destory(boolean cleanData) {

		cleanCurve(cleanData, true);
	}

	protected void drawSelectArea(Canvas canvas, Paint p, int select1, int select2) {
		float[] a = indexToPoint(select1);
		float[] b = indexToPoint(select2);
		if (a[0] > b[0]) {
			mRecSelect.left = Math.min(Math.max(b[0], mRecCurve.left), mRecCurve.right);
			mRecSelect.right = Math.max(Math.min(a[0], mRecCurve.right), mRecCurve.left);
		} else {
			mRecSelect.right = Math.min(Math.max(b[0], mRecCurve.left), mRecCurve.right);
			mRecSelect.left = Math.max(Math.min(a[0], mRecCurve.right), mRecCurve.left);
		}
		mRecSelect.top = mRecCurve.top;
		mRecSelect.bottom = mRecCurve.bottom;
		if (!mRecSelect.isEmpty()) {
			p.setColor(0x55000000);
			canvas.drawRect(mRecSelect, p);
		}
	}

	protected void drawCrossLine(Canvas canvas, Paint p, int selected) {
		final boolean drawCrossH = mSetting.mEnableCrossHorizonal && !mSelectAreaVisible
				&& mCurveCtrl.size(false) < 2;
		if (!drawCrossH) {
			p.setStrokeWidth(mSetting.mCrossSize * 1.5f);
		}
		float txtHigh = ViewUtils.getTxtHeight(mSetting.mBubbleTxtSize);
		float r = Math.max(drawCrossH ? txtHigh / 6 : txtHigh / 3, 5);
		float startY = mRecCurve.top + mRecFrame.top;
		StringBuffer sb = new StringBuffer();
		ArrayList<CharDataType> list = mCurveCtrl.getValuesAt(selected);
		for (int i = 0; i < list.size(); i++) {
			CharDataType data = list.get(i);
			sb.append(mCurveCtrl.getValue(data)).append("    ");
		}
		CharDataType.sort(list);
		float[] cenXY = indexToPoint(selected);
		if (cenXY == null || cenXY[0] < mRecCurve.left || cenXY[0] > mRecCurve.right) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			CharDataType data = list.get(i);
			cenXY = indexToPoint(data.mType, selected);
			p.setColor(mSetting.mCrossColor);
			canvas.drawCircle(cenXY[0], cenXY[1], r * 1.2f, p);
			canvas.drawLine(cenXY[0], startY, cenXY[0], cenXY[1] - r, p);
			p.setColor(ViewUtils.color(data.mType.getColor(0), 255));
			canvas.drawCircle(cenXY[0], cenXY[1], r, p);
			startY = cenXY[1] + r;
		}
		p.setColor(mSetting.mCrossColor);
		canvas.drawLine(cenXY[0], startY, cenXY[0], mRecCurve.bottom + mRecFrame.top, p);

		if (!mSelectAreaVisible) {
			drawHorizonalAdBubbleTxt(canvas, p, sb, drawCrossH);
		}
	}

	protected void drawHorizonalAdBubbleTxt(Canvas canvas, Paint p, StringBuffer sb,
			boolean drawCrossH) {
		float[] cenXY = indexToPoint(mSelect[mIndex]);
		float txtWidth = 0;
		float txtHigh = ViewUtils.getTxtHeight(mSetting.mBubbleTxtSize);
		float r = Math.max(drawCrossH ? txtHigh / 6 : txtHigh / 3, 5);
		float radius = txtHigh / 8;
		p.setTextSize(mSetting.mBubbleTxtSize);
		if (drawCrossH) {
			if (mSetting.mEnableBubble) {
				txtWidth = Math.max(p.measureText(sb.toString()), txtHigh * 2f);
			}
			p.setColor(mSetting.mCrossColor);
			float left = mSetting.mEnableTxtYInSide ? mRecCurve.left + mRecFrame.left : 0;
			canvas.drawLine(left + (txtWidth == 0 ? 0 : txtWidth + radius * 4), cenXY[1], cenXY[0]
					- r, cenXY[1], p);
			canvas.drawLine(cenXY[0] + r, cenXY[1], mRecCurve.right + mRecFrame.left, cenXY[1], p);
		}

		if (mSetting.mEnableBubble) {
			p.setTextAlign(Align.CENTER);
			String xStr = mCurveCtrl.getValueAt(mSelect[mIndex]).mData.getTime("yyyy-MM-dd");
			if (txtWidth == 0)
				txtWidth = Math.max(p.measureText(sb.toString()), txtHigh * 2f);
			if (drawCrossH) {
				mRecSelect.top = Math.min(Math.max(cenXY[1] - radius * 0.5f - txtHigh * 0.5f, 0),
						getHeight() - (txtHigh + radius * 2));
				mRecSelect.bottom = mRecSelect.top + txtHigh + radius;
				if (mSetting.mEnableTxtYInSide) {
					mRecSelect.left = mRecCurve.left + mRecFrame.left + 1;
				} else {
					mRecSelect.left = 1;
				}

				mRecSelect.right = mRecSelect.left + txtWidth + radius * 4;
			} else {

				mRecSelect.top = Math.max(mRecCurve.top + mRecFrame.top - txtHigh - radius - 1, 0);
				mRecSelect.bottom = mRecSelect.top + txtHigh + radius;
				mRecSelect.left = Math.min(Math.max(cenXY[0] - txtWidth / 2 - radius * 2f, 0),
						getWidth() - (txtWidth + radius * 4));
				mRecSelect.right = mRecSelect.left + txtWidth + radius * 4;
			}
			float txtCenterVerticalOffset = ViewUtils
					.getTxtCenterVerticalOffset(mSetting.mBubbleTxtSize);
			drawBubbleText(canvas, sb.toString().trim(), mRecSelect, radius,
					txtCenterVerticalOffset, p, false);

			txtWidth = p.measureText(xStr);
			mRecSelect.top = mRecCurve.bottom + mRecFrame.top + 1;
			mRecSelect.bottom = mRecSelect.top + txtHigh + radius;
			mRecSelect.left = Math.min(Math.max(cenXY[0] - txtWidth / 2 - radius * 2f, 0),
					getWidth() - (txtWidth + radius * 4));
			mRecSelect.right = mRecSelect.left + txtWidth + radius * 4;
			drawBubbleText(canvas, xStr, mRecSelect, radius, txtCenterVerticalOffset, p, true);
		}
	}

	protected void drawBubbleText(Canvas canvas, String str, RectF f, float radius,
			float txtCenterVerticalOffset, Paint p, boolean isCoordX) {
		if (isCoordX) {
			p.setColor(mSetting.mBubbleXColor);
		} else {
			p.setColor(mSetting.mBubbleYColor);
		}
		canvas.drawRoundRect(f, radius, radius, p);
		if (isCoordX) {
			p.setColor(mSetting.mBubbleTxtXColor);
		} else {
			p.setColor(mSetting.mBubbleTxtYColor);
		}
		canvas.drawText(str, f.centerX(), f.centerY() + txtCenterVerticalOffset, p);
	}

	public int getBackGroudColor() {
		return mBackGroudColor;
	}

	public ArrayList<ICharData> getCurveData(ICharType charType) {
		return mCurveCtrl.getCurveData(charType);

	}

	protected RectF getRectCurve() {
		return mRecCurve;
	}

	public CurveSetting getSetting() {
		return mSetting;
	}

	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {

		mCurveCtrl.whenDataChanged(true, mSetting.mEnableAutoCalcuMinMax);
		addFlag(FLAG_INVALIDATE_CACHE);
		if (needInvalidate) {
			invalidate();
		}
		return true;
	}

	@Override
	protected void onAttachedToWindow() {
		if (mCurveListener != null) {
			mCurveListener.onAttachChanged(true);
		}
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mCurveListener != null) {
			mCurveListener.onAttachChanged(false);
		}
		destory(false);
		mCurveCtrl.destoryCanvas();
	}

	@Override
	protected void onDrawSurface(Canvas can) {
		if (hasFlag(FLAG_INVALIDATE_CACHE)) {
			mCurveCtrl.draw(mAnimStop, mAnimRate);
			subFlag(FLAG_INVALIDATE_CACHE);
		}
		can.drawColor(mAnimStop ? mBackGroudColor : ViewUtils.color(mBackGroudColor, mAnimRate, 0),
				Mode.SRC);
		mCurveCtrl.drawCurve(can, mRecFrame);
		if (mCrossVisible) {
			mPaint.reset();
			mPaint.setStrokeWidth(mSetting.mCrossSize);
			if (mSelectAreaVisible) {
				drawSelectArea(can, mPaint, mSelect[0], mSelect[1]);
				drawCrossLine(can, mPaint, mSelect[0]);
				drawCrossLine(can, mPaint, mSelect[1]);
			} else {
				drawCrossLine(can, mPaint, mSelect[mIndex]);
			}
		}
	}

	public boolean onViewFirstSteped(int w, int h) {
		if (hasFlag(FLAG_VISIBLE_ANIM)) {
			return startAnimation(1200, 15, true);
		}
		return false;
	}

	@Override
	protected void onFrameSizeChanged(boolean fromUser) {

		addFlag(FLAG_INVALIDATE_CACHE);
		mRecCurve.set(mRecFrame);
		mRecCurve.offsetTo(0, 0);
		mRecCurve.top += Math.max((mSetting.mArrowSize),
				ViewUtils.getTxtHeight(mSetting.mCoordTxtYSize))
				+ mSetting.mDensity;
		mRecCurve.left += mSetting.mEnableTxtYInSide ? 4 : (ViewUtils.getTxtWidth(
				mSetting.mMeasureTxt, mSetting.mCoordTxtYSize));
		mRecCurve.right -= (mSetting.mArrowSize + +mSetting.mDensity);
		mRecCurve.bottom -= (1.2f * Math.max(ViewUtils.getTxtHeight(mSetting.mCoordTxtXSize),
				ViewUtils.getTxtHeight(mSetting.mBubbleTxtSize)));
		mCurveCtrl.whenFramChanged();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return super.onTouchEvent(event);
		} else if (!mScroller.isFinished()) {
			mScroller.forceFinished(true);
		}
		mGesture.onTouchEvent(event);
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mPreX = mDownX = event.getX();
			if (mCrossVisible) {
				mBaseLen = 0;
				mCrossMoveAble = false;
				int downSel = pointToIndex(mDownX, event.getY(), mCurveCtrl.size(false) < 1);
				if (Math.abs(downSel - mSelect[mIndex]) < 3
						&& Math.abs(mCurveCtrl.indexToPoint(downSel)[0] - mDownX) < mSetting.mClickRaidus
						&& Math.abs(downSel - mSelect[mIndex]) * mCurveCtrl.getSpace() < mSetting.mClickRaidus) {
					mCrossMoveAble = true;
				} else {
					if (mSelectAreaVisible && !mCrossMoveAble) {
						mMoveAreaLen = 0;
						mIndex = 1 - mIndex;
						if (Math.abs(downSel - mSelect[mIndex]) < 3
								&& Math.abs(mCurveCtrl.indexToPoint(downSel)[0] - mDownX) < mSetting.mClickRaidus
								&& Math.abs(downSel - mSelect[mIndex]) * mCurveCtrl.getSpace() < mSetting.mClickRaidus) {
							mCrossMoveAble = true;
						} else {
							mIndex = 1 - mIndex;
						}
					}
				}
			}
		}
			break;

		case MotionEvent.ACTION_MOVE: {
			// TODO calculate scale value and call data changed methond .
			if (!mScaling && event.getPointerCount() == 1) {
				float x = event.getX();
				if (mCrossVisible && mCrossMoveAble) {
					if (mPreX != x) {
						float y = event.getY();
						int select = pointToIndex(x, true);
						if (select > -1 && select != mSelect[mIndex]) {
							mSelect[mIndex] = select;
							invalidate(false, false);
							if (mCurveListener != null) {
								mCurveListener.onCrossEvent(CrossType.CROSS_MOVE,
										mCurveCtrl.getValuesAt(mSelect[mIndex]), x, y,
										mSelect[mIndex]);
							}
						}
					}
				} else {
					if (!mCrossVisible || mSelectAreaVisible) {
						float dx = (x - mPreX);
						if (!mCrossVisible || !pointInSelectArea(x)) {// 如果移手指在选择区内
							int moveType = mCurveCtrl.moveCurves(dx);
							if (moveType != 0) {
								if (mSetting.mEnableAutoCalcuMinMax) {
									invalidate(moveType == 1, true);
								} else {
									invalidate(false, true);
								}
							}
						} else {
							if ((mMoveAreaLen > 0 && dx > 0) || (mMoveAreaLen < 0 && dx < 0)) {
								mMoveAreaLen += dx;
							} else {
								mMoveAreaLen = dx;
							}
							int dIndex = mMoveAreaLen > 0 ? -1 : 1;
							boolean extendLeft = (dx < 0 && -1 == indexInScreen(dIndex
									+ Math.max(mSelect[0], mSelect[1])));
							boolean extendRight = (dx > 0 && 1 == indexInScreen(dIndex
									+ Math.min(mSelect[0], mSelect[1])));
							if ((extendLeft || extendRight)) {
								int moveType = mCurveCtrl.moveCurves(-dx);
								if (moveType != 0) {
									if (mSetting.mEnableAutoCalcuMinMax) {
										invalidate(moveType == 1, true);
									} else {
										invalidate(false, true);
									}
								}
							} else {
								if (Math.abs(mMoveAreaLen) > mCurveCtrl.getSpace()) {
									dIndex = (int) (mMoveAreaLen / mCurveCtrl.getSpace());
									mMoveAreaLen -= (dIndex * mCurveCtrl.getSpace());
									mSelect[0] -= dIndex;
									mSelect[1] -= dIndex;
									mSelect[0] = Math.min(Math.max(0, mSelect[0]),
											mCurveCtrl.getCurveSize(null));
									mSelect[1] = Math.min(Math.max(0, mSelect[1]),
											mCurveCtrl.getCurveSize(null));
									invalidate(false, false);
								}
							}

						}
					}
				}
				mPreX = x;
			} else if (event.getPointerCount() == 2) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				float value = (float) Math.sqrt(x * x + y * y);
				if (mBaseLen <= 0) {
					mBaseLen = value;
					mPreX = (event.getX(0) + event.getX(1)) / 2;
					mTempSelect = pointToIndex(mPreX, y, false);
					if (mTempSelect == -1) {
						mScaling = false;
					} else {
						mScaling = true;
						mPreX = mCurveCtrl.indexToPoint(mTempSelect)[0];
					}

				} else {
					if (mCrossVisible && !mSelectAreaVisible) {
						changeCrossShow(false, event);
					}
					float scale = (value / mBaseLen);
					if (mScaling && scale != 1) {
						if (mCurveListener != null) {
							if (!mCurveListener.onScaleChange(scale)) {
								int moveType = mCurveCtrl.scaleCurves(scale, mPreX, mTempSelect);
								if (moveType != 0) {
									if (mSetting.mEnableAutoCalcuMinMax) {
										invalidate(moveType == 1, true);
									} else {
										invalidate(false, true);
									}
								}
							}
						} else {
							int moveType = mCurveCtrl.scaleCurves(scale, mPreX, mTempSelect);
							if (moveType != 0) {
								if (mSetting.mEnableAutoCalcuMinMax) {
									invalidate(moveType == 1, true);
								} else {
									invalidate(false, false);
								}
							}
						}
					}
					mBaseLen = value;
				}
			}
		}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if (mScaling || mCrossMoveAble) {
				mBaseLen = 0;
			} else {
				mBaseLen = -1;
			}
			mScaling = false;
			mCrossMoveAble = false;

		}
			break;
		}
		return true;
	}

	// value belong (-2,2);

	@Override
	public void onAnimChaged(View nullView, final int type, final int which, final float val,
			final float dval) {
		mAnimRate = formatAnimRate(val, isIncrease);
		if (IAnimChaged.TYPE_CHANGED == type) {
			invalidate(false, true);
		} else if (IAnimChaged.TYPE_START == type) {
			mAnimStop = false;
			invalidate(false, true);
		} else {
			mAnimStop = true;
			if (which != FLAG_NONE) {
				restoreFlag();
			}
			if (!mAnimFillAfter) {
				invalidate(false, true);
			}
		}
		if (mListen != null && isCanvasVisible()) {
			mListen.onAnimChaged(this, type, which, val, dval);
		}
	}

	protected int indexInScreen(int index) {
		return mCurveCtrl.indexInScreen(index);
	}

	protected float[] indexToPoint(int index) {
		float[] result = null;
		result = mCurveCtrl.indexToPoint(index);
		if (result != null) {
			result[0] += mRecFrame.left;
			result[1] += mRecFrame.top;
		}
		return result;
	}

	protected float[] indexToPoint(ICharType iCharType, int index) {
		float[] result = null;
		result = mCurveCtrl.indexToPoint(iCharType, index);
		if (result != null) {
			result[0] += mRecFrame.left;
			result[1] += mRecFrame.top;
		}
		return result;
	}

	protected void invalidate(boolean dataChanged, boolean drawCache) {
		if (drawCache) {
			addFlag(FLAG_INVALIDATE_CACHE);
		} else {
			subFlag(FLAG_INVALIDATE_CACHE);
		}

		if (dataChanged) {
			notifyDataChanged(dataChanged, true);
		} else {
			invalidate();
		}
	}

	public boolean isRateIncrease() {
		return isIncrease;
	}

	protected boolean pointInSelectArea(float x) {
		return mCurveCtrl.pointInArea(x, mSelect[0], mSelect[1]);
	}

	protected int pointToIndex(float x, boolean visible) {
		int index = -1;
		index = mCurveCtrl.pointToIndex(x - mRecFrame.left, visible);
		return index;
	}

	protected int pointToIndex(float x, float y, boolean pointClick) {
		int index = -1;
		index = mCurveCtrl.pointToIndex(x - mRecFrame.left, y - mRecFrame.top, pointClick);
		return index;
	}

	public boolean removeCurve(ICharType charType, boolean cleanData) {
		boolean result = mCurveCtrl.removeCurve(charType, cleanData);
		if (result) {
			invalidate(result, true);
		}
		return result;
	}

	public boolean removeCurveData(ICharType charType, int index) {
		boolean result = false;
		Curve curve = mCurveCtrl.findCurve(charType, true);
		if (curve != null) {
			result = mCurveCtrl.removeCurveData(curve, index);
			if (result && curve.isVisible()) {
				invalidate(true, true);
			}
			return result;
		}
		return result;
	}

	public boolean removeCurveData(ICharType charType, int from, int to) {
		boolean result = false;

		Curve curve = mCurveCtrl.findCurve(charType, true);
		if (curve != null) {
			result = mCurveCtrl.removeCurveData(curve, from, to);
			if (result && curve.isVisible()) {
				invalidate(true, true);
			}
			return result;
		}
		return result;
	}

	public void setAnimLinstener(IAnimChaged l) {
		this.mListen = l;
	}

	public void setBackGroudColor(int mBackGroudColor) {
		this.mBackGroudColor = mBackGroudColor;
	}

	public void setCurveData(HashMap<ICharType, ArrayList<? extends ICharData>> datas) {
		cleanCurve(true, true);
		if (datas != null) {
			mCurveCtrl.setCurveList(datas);
		}
	}

	public void setCurveEvent(ICurveEvent l) {
		this.mCurveListener = l;
	}

	public void setCurveFactory(CurveFactory factory) {
		mCurveCtrl.setCurveFactory(factory);
	}

	public boolean setCurveVisible(ICharType charType, boolean visible) {
		boolean result = mCurveCtrl.setCurveVisible(charType, visible);
		if (result) {
			invalidate(false, true);
		}
		return result;
	}

	public void setRateIncrease(boolean increase) {
		this.isIncrease = increase;
	}

	public int size(boolean all) {
		return mCurveCtrl.size(all);
	}

	public boolean startAnimation(int duration, int period, boolean animFillAfter) {
		return startAnim(FLAG_NONE, duration, period, animFillAfter, false);
	}

	protected class CurveGesture extends SimpleOnGestureListener {
		public boolean onDoubleTap(MotionEvent e) {
			if (mCurveListener != null) {
				float x = e.getX(), y = e.getY();
				mCurveListener
						.onClickEvent(ClickType.CLICK_DOUBLE, x, y, pointToIndex(x, y, false));
			}
			changeCrossShow(!mCrossVisible, e);
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (mCurveListener != null) {
				float x = e.getX(), y = e.getY();
				mCurveListener.onClickEvent(ClickType.CLICK_LONE, x, y, pointToIndex(x, y, false));
			}
			changeCrossShow(true, e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if (mCurveListener != null) {
				float x = e.getX(), y = e.getY();
				mCurveListener.onClickEvent(ClickType.CLICK_ONCE, x, y, pointToIndex(x, y, false));
			}
			if (mCrossVisible) {
				if (mSetting.mEnableSelectArea) {
					if (!mSelectAreaVisible) {
						int index = 1 - mIndex;
						mSelect[index] = pointToIndex(e.getX(), e.getY(), false);
						if (mSelect[index] != -1 && mSelect[index] != mSelect[mIndex]) {
							mSelectAreaVisible = true;
							invalidate(false, false);
						}
					} else {
						if (mSelectAreaVisible = true) {
							mSelectAreaVisible = false;
							invalidate(false, false);
						}
					}
				} else {
					changeCrossShow(true, e);
				}
			}
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			int distance = (int) (velocityX / 1);
			if (mBaseLen == -1 && Math.abs(distance) > 100) {
				if (distance > 1000) {
					distance = 1000;
				} else if (distance < -1000) {
					distance = -1000;
				}
				if (distance != 0) {
					mScroller.startScroll(0, 0, (int) (velocityX / 10), 0);
				}
			}
			return true;
		}

	}

}
