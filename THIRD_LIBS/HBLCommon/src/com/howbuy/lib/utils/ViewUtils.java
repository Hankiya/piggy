package com.howbuy.lib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ViewUtils {
	private static final Paint mPaint = new Paint();

	/*
	 * public static void stopListViewScrollingAndScrollToTop(ListView listView)
	 * { if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
	 * { listView.setSelection(0);
	 * listView.dispatchTouchEvent(MotionEvent.obtain
	 * (SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
	 * MotionEvent.ACTION_CANCEL, 0, 0, 0)); } else {
	 * listView.dispatchTouchEvent
	 * (MotionEvent.obtain(SystemClock.uptimeMillis(),
	 * SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
	 * listView.dispatchTouchEvent
	 * (MotionEvent.obtain(SystemClock.uptimeMillis(),
	 * SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
	 * listView.setSelection(0); } }
	 */

	/** Runs a piece of code after the next layout run */
	@SuppressLint("NewApi")
	public static void doAfterLayout(final View view, final Runnable runnable) {
		final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// Layout pass done, unregister for further events
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
					view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				} else {
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
				runnable.run();
			}
		};
		view.getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}

	public static void showKeybord(View v, boolean show) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		if (show) {
			imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
		} else {
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		}
	}

	/**
	 * judge whether the keyboard is active.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isKeybordShow(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		return imm.isActive();
	}

	/**
	 * @param @param v
	 * @param @param type 1 start, -1 stop and other value for change it state
	 *        auto.
	 * @param @param isCancleCleanAnim
	 * @return boolean return false if the view have no anim
	 * @throws
	 */
	public static boolean invokeAnim(View v, int type, boolean isCancleCleanAnim) {
		Animation anim = v == null ? null : v.getAnimation();
		if (anim != null) {
			if (anim.hasStarted() && !anim.hasEnded()) {
				if (type != 1) {
					anim.cancel();
					if (isCancleCleanAnim) {
						v.clearAnimation();
					}
				}
			} else {
				if (type != -1) {
					anim.startNow();
				}
			}
			v.invalidate();
		}
		return anim != null;
	}

	/**
	 * @param child
	 * @param maxWidth
	 *            unknown for 0.
	 * @param maxHeight
	 *            unknown for 0.
	 * @param result
	 *            accept the width and height ,clound not be null.
	 * @return int[] allow to be null.
	 * @throws
	 */
	public static void measureView(View child, int maxWidth, int maxHeight, int result[]) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (null == p) {
			p = new ViewGroup.LayoutParams(-2, -2);
		}
		int heightSpec;// = ViewGroup.getChildMeasureSpec(0, 0, p.height);
		int widthSpec;
		if (p.width > 0) {// exactly size
			widthSpec = MeasureSpec.makeMeasureSpec(p.width, MeasureSpec.EXACTLY);
		} else if (p.width == -2 || maxWidth <= 0) {// wrapcontent
			widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		} else if (p.width == -1) {
			widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
		} else {// fillparent
			widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
		}
		if (p.height > 0) {
			heightSpec = MeasureSpec.makeMeasureSpec(p.height, MeasureSpec.EXACTLY);
		} else if (p.height == -2 || maxHeight <= 0) {
			heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		} else if (p.height == -1) {
			heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
		} else {
			heightSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
		}
		child.measure(widthSpec, heightSpec);
		result[0] = child.getMeasuredWidth();
		result[1] = child.getMeasuredHeight();
	}

	public static boolean pointInCircle(float x, float y, float cenX, float cenY, float r) {
		float dx = x - cenX;
		float dy = y - cenY;
		return dx * dx / (r * r) + dy * dy / (r * r) < 1;
	}

	public static boolean pointInOval(float x, float y, RectF rect) {
		if (rect.contains(x, y)) {
			float dx = x - rect.centerX();
			float dy = y - rect.centerY();
			float a = rect.width() / 2;
			float b = rect.height() / 2;
			return dx * dx / (a * a) + dy * dy / (b * b) < 1;
		}
		return false;
	}

	/**
	 * 返回顺时针方向,由3点钟方向开始的度数[0,360].
	 */
	public static float pointToAngle(float x, float y, float cenx, float ceny) {
		float dx = x - cenx;
		float dy = y - ceny;
		if (dx == 0) {
			return (dy == 0 ? -1 : (dy > 0 ? 90 : 270));
		} else {
			if (dx > 0) {
				return (dy < 0 ? 360 : 0) + (float) (180 * Math.atan(dy / dx) / Math.PI);
			} else {
				return 180 + (float) (180 * Math.atan(dy / dx) / Math.PI);
			}
		}
	}

	@SuppressLint("NewApi")
	public static void setBackground(View v, Drawable d) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			v.setBackgroundDrawable(d);
		} else {
			v.setBackground(d);
		}
	}

	/**
	 * set view visible state,this will not set a same state twice or more.
	 * 
	 * @param visible
	 *            View.GONE View.VISIBLE View.INVISIBLE
	 */
	public static void setVisibility(View view, int visible) {
		if (view != null && view.getVisibility() != visible) {
			view.setVisibility(visible);
		}
	}

	public static int setListViewHeightBasedOnChildren(ListView lv) {
		ListAdapter listAdapter = lv.getAdapter();
		int n = listAdapter == null ? 0 : listAdapter.getCount();
		if (n == 0) {
			return -1;
		}
		int totalHeight = 0, result[] = new int[2];
		LinearLayout ll = new LinearLayout(lv.getContext());
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View v = listAdapter.getView(i, null, lv);
			if (v instanceof RelativeLayout) {
				ll.removeAllViews();
				ll.addView(v);
				measureView(ll, 0, 0, result);
			} else {
				measureView(v, 0, 0, result);
			}
			totalHeight += result[1];
		}
		ViewGroup.LayoutParams params = lv.getLayoutParams();
		params.height = totalHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
		lv.setLayoutParams(params);
		return params.height;
	}

	public static PointF dividePoint(float x1, float y1, float x2, float y2, float a, PointF result) {
		if (result == null) {
			result = new PointF();
		}
		float b = a + 1;
		if (b == 0) {
			result.x = 2 * x1 - x2;
			result.y = 2 * y1 - y2;
		} else {
			result.x = (x1 + a * x2) / b;
			result.y = (y1 + a * y2) / b;
		}
		return result;
	}

	public static PointF dividePoint(float x1, float y1, float x2, float y2, float a) {
		PointF p = new PointF();
		return dividePoint(x1, y1, x2, y2, a, p);
	}

	public static PointF dividePoint(PointF p, float x2, float y2, float a) {
		return dividePoint(p.x, p.y, x2, y2, a);
	}

	public static PointF dividePoint(PointF p, float x2, float y2, float a, PointF result) {
		return dividePoint(p.x, p.y, x2, y2, a, result);
	}

	public static PointF dividePoint(float x1, float y1, PointF p, float a) {
		return dividePoint(x1, y1, p.x, p.y, a);
	}

	public static PointF dividePoint(float x1, float y1, PointF p, float a, PointF result) {
		return dividePoint(x1, y1, p.x, p.y, a, result);
	}

	public static PointF dividePoint(PointF p1, PointF p2, float a) {
		return dividePoint(p1.x, p1.y, p2.x, p2.y, a);
	}

	public static PointF dividePoint(PointF p1, PointF p2, float a, PointF result) {
		return dividePoint(p1.x, p1.y, p2.x, p2.y, a, result);
	}

	public static RectF centRect(RectF recDes, float cenX, float cenY) {
		float dx = cenX - recDes.centerX();
		float dy = cenY - recDes.centerY();
		recDes.offset(dx, dy);
		return recDes;
	}

	public static Rect centRect(Rect recDes, int cenX, int cenY) {
		int dx = cenX - recDes.centerX();
		int dy = cenY - recDes.centerY();
		recDes.offset(dx, dy);
		return recDes;
	}

	public static Rect centRect(Rect recDes, float cenX, float cenY) {
		return centRect(recDes, Math.round(cenX), Math.round(cenY));
	}

	public static RectF scaleRect(RectF recDes, RectF recIn, float scale) {
		recDes.set(0, 0, recIn.width() * scale, recIn.height() * scale);
		return centRect(recDes, recIn.centerX(), recIn.centerY());
	}

	public static RectF scaleRect(RectF recDes, float scale) {
		float cenX = recDes.centerX();
		float cenY = recDes.centerY();
		float newWidth = recDes.width() * scale;
		float newHeight = recDes.height() * scale;
		recDes.set(0, 0, newWidth, newHeight);
		return centRect(recDes, cenX, cenY);
	}

	public static Rect scaleRect(Rect recDes, float scale) {
		float cenX = recDes.centerX();
		float cenY = recDes.centerY();
		int newWidth = Math.round(recDes.width() * scale);
		int newHeight = Math.round(recDes.height() * scale);
		recDes.set(0, 0, newWidth, newHeight);
		return centRect(recDes, Math.round(cenX), Math.round(cenY));
	}

	public static int[] color(int color) {
		int[] r = new int[4];
		r[0] = color >>> 24;
		r[1] = (color & 0x00FF0000) >>> 16;
		r[2] = (color & 0x0000FF00) >>> 8;
		r[3] = color & 0x000000FF;
		return r;
	}

	/**
	 * keep the RGB value and return a new color with a new alpha.
	 * 
	 * @param @param color original color with alpha value.
	 * @param @param alphaScale between 0 and 1;
	 */
	public static int color(int color, int alpha) {
		if (alpha >= 0 && alpha <= 255) {
			return (color & 0x00FFFFFF) | (alpha << 24);
		}
		return color;
	}

	/**
	 * @param @param color original color with alpha value.
	 * @param @param scale between 0 and 1;
	 * @param scaleType
	 *            0 for only alpha ,1 for only rgb,other for all.
	 */
	public static int color(int color, float scale, int scaleType) {
		if (scale >= 0 && scale <= 1) {
			if (scaleType == 0) {
				int alpha = Math.round(((color >>> 24) * scale));
				return (color & 0x00FFFFFF) | (alpha << 24);
			} else {
				int argb[] = color(color);
				int na = scaleType == 1 ? argb[0] : Math.min(Math.round(argb[0] * scale), 255);
				int nr = Math.min(Math.round(argb[1] * scale), 255);
				int ng = Math.min(Math.round(argb[2] * scale), 255);
				int nb = Math.min(Math.round(argb[3] * scale), 255);
				int ncolor = (na << 24) | (nr << 16) | (ng << 8) | nb;
				return ncolor;
			}
		}
		return color;
	}

	public static float getTxtWidth(String txt, float txtSize) {
		mPaint.setTextSize(txtSize);
		return mPaint.measureText(txt);
	}

	public static float getTxtHeight(float txtSize) {
		mPaint.setTextSize(txtSize);
		return mPaint.descent() - mPaint.ascent();
	}

	public static float getTxtCenterVerticalOffset(float txtSize) {
		mPaint.setTextSize(txtSize);
		return mPaint.descent() - (mPaint.descent() - mPaint.ascent()) / 2;
	}

	public static Rect getTxtBounds(String txt, int len, float txtSize, Rect recDes) {
		if (recDes == null) {
			recDes = new Rect();
		}
		mPaint.setTextSize(txtSize);
		mPaint.getTextBounds(txt, 0, len, recDes);
		return recDes;
	}

	public static float getTxtSize(String txt, float minSize, Rect recMax) {
		int len = txt == null ? 0 : txt.length();
		if (len == 0 || recMax == null || recMax.isEmpty()) {
			return minSize;
		}
		Rect recTest = new Rect(0, 0, 0, 0);
		float rate = 0.95f, w = recMax.width(), h = recMax.height();
		float result = Math.max(Math.min(w, h), minSize);
		do {
			getTxtBounds(txt, len, result, recTest);
			if (result < minSize) {
				result = minSize;
				break;
			}
			result *= rate;
		} while (recTest.width() > w || recTest.height() > h);
		return result;
	}

	public static Bitmap getBitmap(View v, boolean includeBar) {
		Rect r = new Rect(0, 0, 0, 0);
		if (!includeBar) {
			v.getWindowVisibleDisplayFrame(r);
		}
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight() - r.top, Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		canvas.translate(0, -r.top);
		v.draw(canvas);
		return bitmap;
	}

	public static boolean hasFlag(int value, int flag) {
		return flag == 0 ? false : flag == (value & flag);
	}

	public static int addFlag(int value, int flag) {
		return value |= flag;
	}

	public static int subFlag(int value, int flag) {
		return flag == 0 ? value : value & ~flag;
	}

	public static int reverseFlag(int value, int flag) {
		return value ^= flag;
	}

}
