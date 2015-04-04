package com.howbuy.lib.control;

import java.util.Arrays;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.howbuy.lib.compont.GlobalApp;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.StrUtils;
import com.howbuy.lib.utils.ViewUtils;

public class Sector {
	public static final int FLAG_ENABLE = 1;
	public static final int FLAG_ENABLE_TEXT = 2;
	public static final int FLAG_FIXED_BMPSIZE = 4;
	public static final int FLAG_FORCE_REPLACE_IFBMPNULL = 8;
	public static final int FLAG_TEXT_POINT_OUTER = 16;
	protected static final Path PATH = new Path();
	protected static final Paint PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
	protected static final Rect RECTBMP = new Rect();
	protected static RectF RECT_INNER, RECT_OUTER, RECT = new RectF();
	protected static int SUGGEST_BMP_SIZE = 0;
	protected static MaskFilter MASKFILTER = null;

	protected static int DEF_COLOR_NORMAL = 0XAA00FF00;
	protected static int DEF_COLOR_PRESSED = 0XAA00AA00;
	protected static int DEF_COLOR_DISABLE = 0XAA007700;

	public static void setDefColor(int normal, int pressed, int disable) {
		DEF_COLOR_NORMAL = normal;
		DEF_COLOR_PRESSED = pressed;
		DEF_COLOR_DISABLE = disable;
	}

	/**
	 * @param flag
	 * @param changeType
	 *            -1 for sub,1 for add,0 for equal.
	 * @return void
	 * @throws
	 */
	public void addFlags(int flag, int addType) {
		flag &= ((1 << 6) - 1);
		if (addType == 0) {
			mFlag = flag;
		} else if (addType == -1) {
			mFlag &= ~flag;
		} else if (addType == 1) {
			mFlag |= flag;
		}
	}

	public static void setSuggestBmpSizeRaw(int size) {
		SUGGEST_BMP_SIZE = size;
	}

	public static void setBounds(RectF inner, RectF outer, MaskFilter mask) {
		RECT_INNER = inner;
		RECT_OUTER = outer;
		PAINT.setTextAlign(Align.CENTER);
		if (mask != null) {
			MASKFILTER = mask;
		}
	}

	protected int mKey, mFlag = FLAG_ENABLE | FLAG_ENABLE_TEXT
			| FLAG_FIXED_BMPSIZE;
	protected float mTemp, mWeight;
	protected float mSAngle, mDAngle; // 开始角度,扫过的角度.
	protected float mMaxDAngle = 120, mMinDAngle = 40, mCenAngle;
	protected int mColorNormal = 0x7700ff00, mColorPressed = 0x7700aa00,
			mColorDisable = 0x77007700;
	protected int mColorTxtNormal = 0xff000000, mColorTxtPressed = 0xff333333,
			mColorTxtDisable = 0xff777777;
	protected Bitmap mBmpNormal, mBmpPressed, mBmpDisable;
	protected String mTxtNormal, mTxtPressed, mTxtDisable;
	protected int[] mBmpRes = new int[] { 0, 0, 0 };
	protected int mPaddBmp = 5;
 

	public Sector(int key, float weight, int paddingBmp) {
		this.mKey = key;
		this.mWeight = weight;
		if (paddingBmp >= 0) {
			mPaddBmp = paddingBmp;
		}
		PAINT.setTextAlign(Align.CENTER);
		setColor(DEF_COLOR_NORMAL, DEF_COLOR_PRESSED, DEF_COLOR_DISABLE);
	}

	public int getPadding() {
		return mPaddBmp;
	}

	public float getCenter() {
		return mCenAngle;
	}

	public void setPadding(int bmpPadding) {
		mPaddBmp = bmpPadding;
	}

	/**
	 * set it before show view.
	 */
	public void setBmpRes(int resNormal, int resPressed, int resDisable) {
		mBmpRes[0] = resNormal;
		mBmpRes[1] = resPressed;
		mBmpRes[2] = resDisable;
	}

	public void onAttachChanged(boolean isAttach) {
		if (isAttach) {
			createBmp();
		} else {
			destoryBmp();
		}

	}

	protected Bitmap createBmp(int res) {
		if (res > 0) {
			return BitmapFactory.decodeResource(GlobalApp.getApp()
					.getResources(), res);
		}
		return null;

	}

	protected void createBmp() {
		destoryBmp();
		if (mBmpNormal == null) {
			mBmpNormal = createBmp(mBmpRes[0]);
		}
		if (mBmpPressed == null) {
			mBmpPressed = createBmp(mBmpRes[1]);
		}
		if (mBmpDisable == null) {
			mBmpDisable = createBmp(mBmpRes[2]);
		}
	}

	protected void destoryBmp() {
		if (mBmpNormal != null) {
			mBmpNormal.recycle();
			mBmpNormal = null;
		}
		if (mBmpPressed != null) {
			mBmpPressed.recycle();
			mBmpPressed = null;
		}
		if (mBmpDisable != null) {
			mBmpDisable.recycle();
			mBmpDisable = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		destoryBmp();
	}

	public int getKey() {
		return mKey;
	}

	public void setKey(int key) {
		mKey = key;
	}

	public void setColor(int normal, int pressed, int disable) {
		mColorNormal = normal;
		mColorPressed = pressed;
		mColorDisable = disable;
	}

	public void setTextColor(int normal, int pressed, int disable) {
		mColorTxtNormal = normal;
		mColorTxtPressed = pressed;
		mColorTxtDisable = disable;
	}

	public void setText(String normal, String pressed, String disable) {
		mTxtNormal = normal;
		mTxtPressed = pressed;
		mTxtDisable = disable;
	}

	protected void setBitmap(Bitmap normal, Bitmap pressed, Bitmap disable) {
		mBmpNormal = normal;
		mBmpPressed = pressed;
		mBmpDisable = disable;
	}

	public boolean isEmptyAllBitmap() {
		return !(mBmpNormal != null || mBmpPressed != null || mBmpDisable != null);
	}

	public boolean isEmptyAllTxt() {
		return !(mTxtNormal != null || mTxtPressed != null || mTxtDisable != null);
	}

	public int getColor(boolean isPressed) {
		if (ViewUtils.hasFlag(mFlag, FLAG_ENABLE)) {
			return isPressed ? mColorPressed : mColorNormal;
		}
		return mColorDisable;
	}

	public int getTxtColor(boolean isPressed) {
		if (ViewUtils.hasFlag(mFlag, FLAG_ENABLE)) {
			return isPressed ? mColorTxtPressed : mColorTxtNormal;
		}
		return mColorTxtDisable;
	}

	public String getText(boolean isPressed, boolean forceUnempty) {
		String txt = mTxtDisable;
		if (ViewUtils.hasFlag(mFlag, FLAG_ENABLE)) {
			txt = isPressed ? mTxtPressed : mTxtNormal;
		}
		if (txt == null && forceUnempty) {
			if (mTxtNormal != null) {
				txt = mTxtNormal;
			} else if (mTxtPressed != null) {
				txt = mTxtPressed;
			} else {
				txt = mTxtDisable;
			}
		}
		return txt;
	}

	public Bitmap getBitmap(boolean isPressed, boolean forceUnempty) {
		Bitmap bmp = mBmpDisable;
		if (ViewUtils.hasFlag(mFlag, FLAG_ENABLE)) {
			bmp = isPressed ? mBmpPressed : mBmpNormal;
		}
		if (bmp == null && forceUnempty) {
			if (mBmpNormal != null) {
				bmp = mBmpNormal;
			} else if (mBmpPressed != null) {
				bmp = mBmpPressed;
			} else {
				bmp = mBmpDisable;
			}
		}
		return bmp;
	}

	public float getFloat() {
		return mTemp;
	}

	public void setFloat(float val) {
		mTemp = val;
	}

	public void setEnable(boolean enable) {
		if (enable) {
			mFlag |= FLAG_ENABLE;
		} else {
			mFlag &= ~FLAG_ENABLE;
		}
	}

	public boolean isEnable() {
		return ViewUtils.hasFlag(mFlag, FLAG_ENABLE);
	}

	public float getMaxSweepAngle() {
		return mMaxDAngle;
	}

	public float getMinSweepAngle() {
		return mMinDAngle;
	}

	public void setMaxSweepAngle(float angle) {
		mMaxDAngle = Math.min(360, Math.max(mMinDAngle, angle));
	}

	public void setMinSweepAngle(float angle) {
		mMinDAngle = Math.min(mMaxDAngle, Math.max(0, angle));
	}

	public void setWeight(float weight) {
		mWeight = weight;
	}

	public float getWeight() {
		return mWeight;
	}

	public void setStartAngle(float angle) {
		mSAngle = angle;
	}

	public float setSweepAngle(float angle, boolean adjust) {
		if (adjust) {
			mDAngle = Math.max(mMinDAngle, Math.min(angle, mMaxDAngle));
		} else {
			mDAngle = angle;
		}
		mCenAngle = mSAngle + mDAngle / 2;
		return mDAngle;
	}

	public float getStart() {
		return mSAngle;
	}

	public float getSweep() {
		return mDAngle;
	}

	public void drawCenter(Canvas c, RectF rect, float txtSize,
			float strokeWidth, boolean isPressed) {
		int color = getColor(isPressed);
		Bitmap bmp = getBitmap(isPressed, true);
		PAINT.setColor(color);
		c.drawArc(rect, 0, 360, true, PAINT);
		if (bmp != null) {
			RECT.set(rect);
			RECT.offsetTo(0, 0);
			adjustBmpSize(bmp, rect.centerX(), rect.centerY());
			if (!RECT.isEmpty()) {
				c.drawBitmap(bmp, RECTBMP, RECT, null);
			}
		} else {
			if (ViewUtils.hasFlag(mFlag, FLAG_FORCE_REPLACE_IFBMPNULL)) {
				String txt = getText(isPressed, true);
				if (!StrUtils.isEmpty(txt)) {
					PAINT.setTextSize(txtSize * 1.4f);
					PAINT.setStrokeWidth(strokeWidth * 1.4f);
					float textHeihgt = -PAINT.ascent() / 2;
					c.drawText(txt, rect.centerX(),
							rect.centerY() + textHeihgt, PAINT);
				}
			}
		}
	}

	/**
	 * @param @param can
	 * @param type
	 *            -1 disable,0 normal,1 pressed.
	 * @return void
	 * @throws
	 */
	public float draw(Canvas c, float textSize, float strokeWidth,
			boolean isPressed) {
		PAINT.setTextSize(textSize);
		PAINT.setStrokeWidth(strokeWidth);
		drawSector(c, isPressed);
		if (ViewUtils.hasFlag(mFlag, FLAG_ENABLE_TEXT)) {
			RECT.set(RECT_OUTER);
			drawPathTxt(c, RECT, isPressed);
		}
		return getStart() + getSweep();
	}

	protected void drawSector(Canvas can, boolean isPressed) {
		PAINT.setColor(getColor(isPressed));
		PAINT.setMaskFilter(MASKFILTER);
		can.drawArc(RECT_OUTER, getStart(), getSweep(), true, PAINT);
		PAINT.setMaskFilter(null);
		Bitmap bmp = getBitmap(isPressed, true);
		if (bmp == null
				&& ViewUtils.hasFlag(mFlag, FLAG_FORCE_REPLACE_IFBMPNULL)) {
			float oldSize = PAINT.getTextSize();
			float oldWidth = PAINT.getStrokeWidth();
			PAINT.setTextSize(oldSize * 1.3f);
			PAINT.setStrokeWidth(oldWidth * 1.3f);
			RECT.set(RECT_OUTER);
			float textHeihgt = ViewUtils.getTxtHeight(oldSize * 1.3f) / 2;
			float dx = (RECT_OUTER.right - RECT_INNER.right) / 2 + textHeihgt;
			float dy = (RECT_OUTER.bottom - RECT_INNER.bottom) / 2 + textHeihgt;
			RECT.inset(dx, dy);
			drawPathTxt(can, RECT, isPressed);
			PAINT.setTextSize(oldSize);
			PAINT.setStrokeWidth(oldWidth);
		} else {
			if (bmp != null) {
				drawPathBmp(can, bmp);
			}
		}
	}

	protected RectF adjustBmpSize(Bitmap bmp, float cenX, float cenY) {
		RECTBMP.set(0, 0, bmp.getWidth(), bmp.getHeight());
		if (ViewUtils.hasFlag(mFlag, FLAG_FIXED_BMPSIZE)) {
			if (SUGGEST_BMP_SIZE > 0) {
				RECT.right = RECT.bottom = SUGGEST_BMP_SIZE;
			} else {
				float mWdivH = RECTBMP.width() / RECTBMP.height();
				float wh = RECT.width() / RECT.height();
				if (wh > mWdivH) {
					RECT.right = RECT.bottom * mWdivH;
				} else if (wh < mWdivH) {
					RECT.bottom = RECT.right / mWdivH;
				}
				if (RECT.right > RECTBMP.right) {
					RECT.right = RECTBMP.right;
					RECT.bottom = RECTBMP.bottom;
				}
			}
		}
		RECT.right -= mPaddBmp;
		RECT.bottom -= mPaddBmp;
		return ViewUtils.centRect(RECT, cenX, cenY);
	}

	protected void drawPathBmp(Canvas can, Bitmap bmp) {
		float cenX = RECT_OUTER.centerX(), cenY = RECT_OUTER.centerY();
		can.save();
		can.rotate(-90 + getStart() + getSweep() / 2, cenX, cenY);
		float avrgD = (RECT_OUTER.width() + RECT_INNER.width()) / 2;
		avrgD *= (Math.sin(Math.PI * getSweep() / 360));
		RECT.set(0, 0, avrgD, RECT_OUTER.bottom - RECT_INNER.bottom);
		adjustBmpSize(bmp, cenX, (RECT_OUTER.bottom + RECT_INNER.bottom) / 2);
		PAINT.setColor(0x44f0ffff);
		can.drawRect(RECT, PAINT);
		if (!RECT.isEmpty()) {
			can.drawBitmap(bmp, RECTBMP, RECT, null);
		}
		can.restore();
	}

	protected void drawPathTxt(Canvas can, RectF rect, boolean isPressed) {
		String text = getText(isPressed, true);
		if (!StrUtils.isEmpty(text)) {
			float mTxtCenterVerticalOffset = ViewUtils.getTxtCenterVerticalOffset(PAINT.getTextSize());			PATH.reset();
			if (ViewUtils.hasFlag(mFlag, FLAG_TEXT_POINT_OUTER)) {
				PATH.addArc(rect, getStart(), getSweep());
			} else {
				mTxtCenterVerticalOffset += ViewUtils.getTxtHeight(PAINT.getTextSize());
				PATH.addArc(rect, getStart() + getSweep(), -getSweep());
			}
			PAINT.setColor(getTxtColor(isPressed));
			can.drawTextOnPath(text, PATH, 0, mTxtCenterVerticalOffset, PAINT);
		}
	}

	@Override
	public String toString() {
		return "Sector [mKey=" + mKey + ", mTemp=" + mTemp + ", mWeight="
				+ mWeight + ", mSAngle=" + mSAngle + ", mDAngle=" + mDAngle
				+ ", mMaxDAngle=" + mMaxDAngle + ", mMinDAngle=" + mMinDAngle
				+ ", mColorNormal=" + mColorNormal + ", mColorPressed="
				+ mColorPressed + ", mColorDisable=" + mColorDisable
				+ ", mBmpNormal=" + mBmpNormal + ", mBmpPressed=" + mBmpPressed
				+ ", mBmpDisable=" + mBmpDisable + ", mTxtNormal=" + mTxtNormal
				+ ", mTxtPressed=" + mTxtPressed + ", mTxtDisable="
				+ mTxtDisable + ", mFlag=" + mFlag + ", mBmpRes="
				+ Arrays.toString(mBmpRes) + ", mPaddBmp=" + mPaddBmp + "]";
	}

	public String toShortString() {
		return "Sector [mKey=" + mKey + ", mSAngle=" + mSAngle + ", mDAngle="
				+ mDAngle + ", mBmpNormal=" + mBmpNormal + ", mBmpPressed="
				+ mBmpPressed + ", mBmpDisable=" + mBmpDisable
				+ ", mTxtNormal=" + mTxtNormal + ", mTxtPressed=" + mTxtPressed
				+ ", mTxtDisable=" + mTxtDisable + ", mFlag=" + mFlag + "]";
	}

}
