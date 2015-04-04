package com.howbuy.component;

import java.util.Arrays;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

import com.howbuy.lib.utils.LogUtils;

public class CardDrawable extends Drawable {
	private static final int SHADE_L = 1;
	private static final int SHADE_T = 2;
	private static final int SHADE_R = 4;
	private static final int SHADE_B = 8;
	private int mFlag = 0;
	Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔。
	RectF mShapeSize = new RectF(0, 0, 0, 5);// 四边shade的宽度。
	Rect mColorStarts = new Rect(0x22222222, 0x22222222, 0x22222222, 0x33333333);// 四边的开始颜色。
	Rect mColorEnds = new Rect(0x00000000, 0x00000000, 0x00000000, 0x00000000);// 四边的结束颜色。
	int mColor = 0xffffffff;
	Path mPath = new Path();
	LinearGradient mShadeL, mShadeT, mShadeR, mShadeB;// 四边的shape对象。

	public CardDrawable setShadeWidth(float left, float top, float right, float bottom) {
		if (left != mShapeSize.left || right != mShapeSize.right) {
			mShadeT = mShadeB = null;
		}
		if (left != mShapeSize.top || right != mShapeSize.bottom) {
			mShadeL = mShadeR = null;
		}
		mShapeSize.set(left, top, right, bottom);
		return this;
	}

	public CardDrawable setShadeColorL(int start, int end) {
		mColorStarts.left = start;
		mColorEnds.left = end;
		mShadeL = null;
		return this;
	}

	public CardDrawable setShadeColorT(int start, int end) {
		mColorStarts.top = start;
		mColorEnds.top = end;
		mShadeT = null;
		return this;
	}

	public CardDrawable setShadeColorR(int start, int end) {
		mColorStarts.right = start;
		mColorEnds.right = end;
		mShadeR = null;
		return this;
	}

	public CardDrawable setShadeColorB(int start, int end) {
		mColorStarts.bottom = start;
		mColorEnds.bottom = end;
		mShadeB = null;
		return this;
	}

	public void setColor(int color) {
		mColor = color;
	}

	public CardDrawable(int color) {
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(mColor = color);
	}

	@Override
	public void draw(Canvas can) {
		final Rect r = getBounds();
		if (!r.isEmpty()) {
			checkShadeFlag();
			for (int i = 0; i < 4; i++) {
				mPath.reset();
				Shader s = getShade(i, r);
				if (s != null) {
					mPaint.setShader(s);
					can.drawPath(mPath, mPaint);
				}
			}
			int left = r.left, top = r.top, right = r.right, bottom = r.bottom;
			if (hasFlag(SHADE_L)) {
				left += mShapeSize.left;
			}
			if (hasFlag(SHADE_T)) {
				top += mShapeSize.top;
			}
			if (hasFlag(SHADE_R)) {
				right -= mShapeSize.right;
			}
			if (hasFlag(SHADE_B)) {
				bottom -= mShapeSize.bottom;
			}
			mPaint.setShader(null);
			can.drawRect(left, top, right, bottom, mPaint);
		}
	}

	Shader getShade(int i, Rect r) {
		Shader s = null;
		switch (i) {
		case 0:
			s = getShadeL(r);
			break;
		case 1:
			s = getShadeT(r);

			break;
		case 2:
			s = getShadeR(r);
			break;
		case 3:
			s = getShadeB(r);
			break;
		}

		return s;
	}

	Shader getShadeL(Rect r) {
		if (hasFlag(SHADE_L)) {
			float left = r.left, right = r.left + mShapeSize.left;
			float t = r.top, b = r.bottom;
			if (hasFlag(SHADE_T)) {
				t += mShapeSize.top;
			}
			if (hasFlag(SHADE_B)) {
				b -= mShapeSize.bottom;
			}
			mPath.moveTo(left, r.top);
			mPath.lineTo(left, r.bottom);
			mPath.lineTo(right, b);
			mPath.lineTo(right, t);
			mPath.close();
			if (mShadeL == null) {
				mShadeL = new LinearGradient(right, 0, left, 0, mColorStarts.left, mColorEnds.left,
						TileMode.CLAMP);
			}
		}
		return mShadeL;
	}

	Shader getShadeR(Rect r) {
		if (hasFlag(SHADE_R)) {
			float left = r.right - mShapeSize.right, right = r.right;
			float t = r.top, b = r.bottom;
			if (hasFlag(SHADE_T)) {
				t += mShapeSize.top;
			}
			if (hasFlag(SHADE_B)) {
				b -= mShapeSize.bottom;
			}
			mPath.moveTo(right, r.top);
			mPath.lineTo(right, r.bottom);
			mPath.lineTo(left, b);
			mPath.lineTo(left, t);
			mPath.close();
			if (mShadeR == null) {
				mShadeR = new LinearGradient(left, 0, right, 0, mColorStarts.right,
						mColorEnds.right, TileMode.CLAMP);
			}
		} else {
			mShadeR = null;
		}
		return mShadeR;
	}

	Shader getShadeT(Rect r) {
		if (hasFlag(SHADE_T)) {
			float top = r.top, bottom = r.top + mShapeSize.top;
			float lt = r.left, rt = r.right;
			if (hasFlag(SHADE_L)) {
				lt += mShapeSize.left;
			}
			if (hasFlag(SHADE_R)) {
				rt -= mShapeSize.right;
			}
			mPath.moveTo(r.left, top);
			mPath.lineTo(r.right, top);
			mPath.lineTo(rt, bottom);
			mPath.lineTo(lt, bottom);
			mPath.close();
			if (mShadeT == null) {
				mShadeT = new LinearGradient(0, bottom, 0, top, mColorStarts.top, mColorEnds.top,
						TileMode.CLAMP);
			}
		} else {
			mShadeT = null;
		}
		return mShadeT;
	}

	Shader getShadeB(Rect r) {
		if (hasFlag(SHADE_B)) {
			float top = r.bottom - mShapeSize.bottom, bottom = r.bottom;
			float lt = r.left, rt = r.right;
			if (hasFlag(SHADE_L)) {
				lt += mShapeSize.left;
			}
			if (hasFlag(SHADE_R)) {
				rt -= mShapeSize.right;
			}
			mPath.moveTo(r.left, bottom);
			mPath.lineTo(r.right, bottom);
			mPath.lineTo(rt, top);
			mPath.lineTo(lt, top);
			mPath.close();
			if (mShadeB == null) {
				mShadeB = new LinearGradient(0, top, 0, bottom, mColorStarts.bottom,
						mColorEnds.bottom, TileMode.CLAMP);
			}
		} else {
			mShadeB = null;
		}
		return mShadeB;
	}

	private void checkShadeFlag() {
		if (mShapeSize.left > 0 && (mColorStarts.left | mColorEnds.left) != 0) {
			addFlag(SHADE_L);
		} else {
			subFlag(SHADE_L);
			mShadeL = null;
		}
		if (mShapeSize.right > 0 && (mColorStarts.right | mColorEnds.right) != 0) {
			addFlag(SHADE_R);
		} else {
			subFlag(SHADE_R);
			mShadeR = null;
		}
		if (mShapeSize.top > 0 && (mColorStarts.top | mColorEnds.top) != 0) {
			addFlag(SHADE_T);
		} else {
			subFlag(SHADE_T);
			mShadeT = null;
		}
		if (mShapeSize.bottom > 0 && (mColorStarts.bottom | mColorEnds.bottom) != 0) {
			addFlag(SHADE_B);
		} else {
			subFlag(SHADE_B);
			mShadeB = null;
		}
	}

	@Override
	public boolean getPadding(Rect padding) {
		checkShadeFlag();
		boolean hasShade = ((SHADE_L | SHADE_T | SHADE_R | SHADE_B) & mFlag) != 0;
		if (hasShade) {
			if (hasFlag(SHADE_L)) {
				padding.left = (int) mShapeSize.left;
			}
			if (hasFlag(SHADE_T)) {
				padding.top = (int) mShapeSize.top;
			}
			if (hasFlag(SHADE_R)) {
				padding.right = (int) mShapeSize.right;
			}
			if (hasFlag(SHADE_B)) {
				padding.bottom = (int) mShapeSize.bottom;
			}
		}
		return false;
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mShadeL = mShadeR = mShadeT = mShadeB = null;
	}

	 
	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}

	final protected boolean hasFlag(int flag) {
		return flag == 0 ? false : flag == (mFlag & flag);
	}

	final protected void addFlag(int flag) {
		mFlag |= flag;
	}

	final protected void subFlag(int flag) {
		if (flag != 0) {
			mFlag &= ~flag;
		}
	}
}
