package com.howbuy.control;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class TouchImage extends ImageView {
	static final int NONE = 0;
	static final int TRANSLATE = 1;
	static final int ZOOM = 2;
	static final int BIGGER = 3;
	static final int SMALLER = 4;
	static final int CAN_TRANSLATE_LEFT = 1;
	static final int CAN_TRANSLATE_TOP = 2;
	static final int CAN_TRANSLATE_REGHT = 4;
	static final int CAN_TRANSLATE_BOTTOM = 8;
	static final int CAN_TRANSLATE_NONE = 0;
	static final int CAN_ZOOM_SMAL = 1;
	static final int CAN_ZOOM_BIG = 2;
	static final int CAN_ZOOM_NONE = 0;
	static final int TRANSLATE_PADDING = 5;
	private int mode = NONE;
	private float beforeLenght;
	private float afterLenght;
	private RectF mRectApply = new RectF();
	private Rect mRect = null;
	private Rect mViewRect = null;
	private int mPreX = -1, mPreY = -1;
	float rawX = 0, rawY = 0;
	private boolean mCanZoomSmaller = false;
	private Matrix mMatrix = null;

	private GestureDetector mGester = null;

	public void setGesture(GestureDetector gesture) {
		mGester = gesture;
	}

	public TouchImage(Context context) {
		super(context);
	}

	public TouchImage(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isEnabled()) {
			return super.onTouchEvent(event);
		}
		if (mGester != null) {
			mGester.onTouchEvent(event);
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (mRect == null) {
				setScaleType(ScaleType.MATRIX);
				mRect = getDrawable().getBounds();
				mMatrix = getImageMatrix();
			}
			if (event.getPointerCount() == 2) {
				beforeLenght = spacing(event);
			} else {
				mode = TRANSLATE;
				mPreX = (int) event.getX();
				mPreY = (int) event.getY();
				rawX = event.getRawX();
				rawY = event.getRawY();
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:// muti point down.
			if (event.getPointerCount() == 2 && spacing(event) > 10f) {
				mode = ZOOM;
				beforeLenght = spacing(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mode == NONE) {
				getCurrentScale(false);
			}
			mode = NONE;
			// center(true, true);
			rawX = event.getRawX() - rawX;
			rawY = event.getRawY() - rawY;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == ZOOM) {
				if (spacing(event) > 10f) {
					afterLenght = spacing(event);
					float scale = (float) afterLenght / beforeLenght;
					if (scale == 1) {
						break;
					} else {
						int type = getZoomType();
						if (scale < 1) {
							if (canZoomBig(type)) {
								if (!mCanZoomSmaller) {
									float preScale = getCurrentScale(false);
									if (preScale * scale < 1) {
										scale = 1 / preScale;
									}
								}
								zoomBy(scale);
							}
						} else {
							if (canZoomSmal(type)) {
								zoomBy(scale);
							}
						}
						beforeLenght = afterLenght;
					}
				}
			} else {
				if (event.getPointerCount() == 1) {
					float dx = event.getX() - mPreX;
					float dy = event.getY() - mPreY;
					if (Math.sqrt(dx * dx + dy * dy) > 15f) {
						if (mode == TRANSLATE) {
							mPreX = (int) event.getX();
							mPreY = (int) event.getY();
							int type = getTranslateType();
							float ddx = 0f, ddy = 0f;
							if (dx > 0) {
								if (canTranslateRight(type)) {
									ddx = dx;
								}
							} else {
								if (canTranslateLeft(type)) {
									ddx = dx;
								}
							}
							if (dy > 0) {
								if (canTranslateBottom(type)) {
									ddy = dy;
								}
							} else {
								if (canTranslateTop(type)) {
									ddy = dy;
								}
							}
							if (ddx != 0 || ddy != 0) {
								translateBy(ddx, ddy);
							}
						}
					}
				}
			}
			break;
		}
		return true;
	}

	public float getCurrentScale(boolean releativeToBmp) {
		if (mRect == null) {
			if (getDrawable() != null) {
				mRect = getDrawable().getBounds();
			}
		}
		if (mMatrix == null) {
			mMatrix = getImageMatrix();
		}
		if (mMatrix == null || mRect == null || mRect.isEmpty() || mViewRect == null
				|| mViewRect.isEmpty()) {
			return -1;
		}
		Rect r = releativeToBmp ? mRect : mViewRect;
		mRectApply.set(mRect);
		mMatrix.mapRect(mRectApply);
		float res = Math.max(mRectApply.width() / r.width(), mRectApply.height() / r.height());
		return res;
	}

	public final Rect getViewBounds() {
		getCurrentScale(false);
		return mViewRect;
	}

	public final RectF getImageBounds() {
		getCurrentScale(false);
		return mRectApply;
	}

	public void translateBy(float dx, float dy) {
		mMatrix.postTranslate(dx, dy);
		setImageMatrix(mMatrix);
		invalidate();
	}

	public void zoomBy(float scale) {
		mMatrix.postScale(scale, scale);
		center(true, true);
		invalidate();
	}

	public void center(boolean horizontal, boolean vertical) {
		RectF rect = new RectF(mRect);
		mMatrix.mapRect(rect);
		float deltaX = 0, deltaY = 0;
		int w = getWidth(), h = getHeight();
		if (vertical) {
			deltaY = (h >> 1) - rect.centerY();
		}
		if (horizontal) {
			deltaX = (w >> 1) - rect.centerX();

		}
		mMatrix.postTranslate(deltaX, deltaY);
		setImageMatrix(mMatrix);
		invalidate();
	}

	public void reset() {
		RectF rect = new RectF(mRect);
		mMatrix.mapRect(rect);
		float scale = mViewRect.width() / rect.width();
		zoomBy(scale);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mViewRect == null) {
			mViewRect = new Rect();
		}
		getDrawingRect(mViewRect);
	}

	private int getTranslateType() {
		mRectApply.set(mRect);
		mMatrix.mapRect(mRectApply);
		int dleft = mViewRect.left - (int) mRectApply.left;
		int dtop = mViewRect.top - (int) mRectApply.top;
		int dright = mViewRect.right - (int) mRectApply.right;
		int dbottom = mViewRect.bottom - (int) mRectApply.bottom;
		int left = CAN_TRANSLATE_NONE, top = CAN_TRANSLATE_NONE, right = CAN_TRANSLATE_NONE, bottom = CAN_TRANSLATE_NONE;
		boolean horizontal = false, vertical = false;
		if (mViewRect.width() < mRectApply.width() + TRANSLATE_PADDING) {
			horizontal = true;
		}
		if (mViewRect.height() < mRectApply.height() + TRANSLATE_PADDING) {
			vertical = true;
		}

		if (horizontal) {
			if (dleft > -TRANSLATE_PADDING) {
				right = CAN_TRANSLATE_REGHT;
			}
			if (dright < TRANSLATE_PADDING) {
				left = CAN_TRANSLATE_LEFT;

			}

		}
		if (vertical) {
			if (dtop > -TRANSLATE_PADDING) {
				bottom = CAN_TRANSLATE_BOTTOM;
			}
			if (dbottom < TRANSLATE_PADDING) {
				top = CAN_TRANSLATE_TOP;

			}

		}
		return left | top | bottom | right;
	}

	private int getZoomType() {
		mRectApply.set(mRect);
		mMatrix.mapRect(mRectApply);
		int rectW = (int) mRectApply.width();
		int viewW = mViewRect.width();
		int smal = CAN_ZOOM_NONE;
		int big = CAN_ZOOM_NONE;
		if (mCanZoomSmaller) {
			if (rectW > viewW >> 1) {
				big = CAN_ZOOM_BIG;
			}
		} else {
			if (rectW > viewW) {
				big = CAN_ZOOM_BIG;
			}
		}
		if (rectW < viewW << 1) {
			smal = CAN_ZOOM_SMAL;
		}
		return smal | big;
	}

	private boolean canTranslateLeft(int translateType) {
		return CAN_TRANSLATE_LEFT == (CAN_TRANSLATE_LEFT & translateType);
	}

	private boolean canTranslateTop(int translateType) {
		return CAN_TRANSLATE_TOP == (CAN_TRANSLATE_TOP & translateType);
	}

	private boolean canTranslateRight(int translateType) {
		return CAN_TRANSLATE_REGHT == (CAN_TRANSLATE_REGHT & translateType);
	}

	private boolean canTranslateBottom(int translateType) {
		return CAN_TRANSLATE_BOTTOM == (CAN_TRANSLATE_BOTTOM & translateType);
	}

	private boolean canZoomSmal(int zoomType) {
		return CAN_ZOOM_SMAL == (CAN_ZOOM_SMAL & zoomType);
	}

	private boolean canZoomBig(int zoomType) {
		return CAN_ZOOM_BIG == (CAN_ZOOM_BIG & zoomType);
	}
}