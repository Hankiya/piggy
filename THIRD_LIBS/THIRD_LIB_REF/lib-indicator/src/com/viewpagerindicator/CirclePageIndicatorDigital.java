/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewpagerindicator;

import static android.widget.LinearLayout.HORIZONTAL;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Draws circles with numbers (one for each view). The current view position is filled and
 * others are only stroked.
 */
public class CirclePageIndicatorDigital extends CirclePageIndicator {
	private float mSelectedRadius;
	private float mCurLong, mMovLong;
	private final RectF mRect = new RectF();
	private final RectF mRectTxt = new RectF();
	private final Paint mPaintTxt = new Paint(Paint.ANTI_ALIAS_FLAG);
	FontMetrics mFontMetric = null;
	private boolean mShowPageNum = true;
	private boolean mShowMoveNum=true;
	private int mColorTxtNormal = 0xff000000, mColorTxtSelected = 0xffff0000, mColorTxtMove = 0xff0000ff;
	private int mColorFillNormal = 0xff0000aa, mColorFillSelected = 0xff000000,mColorFillMove = 0xff00aa00;

	public void setShowPageNum(boolean shownum){
		this.mShowPageNum=shownum;
		invalidate();
	}
	public boolean isShowNum(){
		return  mShowPageNum;
	}
 
	/**
	 * @param color
	 * @param type
	 *            0:normal, 1:selected,2:move
	 * @throws
	 */
	public void setColorTxt(int color,int type){
		if(type==0){
			mColorTxtNormal=color;
		}else if(type==1){
			mColorTxtSelected=color;
		}else if(type==2){
			mColorTxtMove=color;
		}
	}
	/**
	 * @param color
	 * @param type
	 *            0:normal, 1:selected,2:move
	 * @throws
	 */
	public void setColorFill(int color ,int type){
		if(type==0){
			mColorFillNormal=color;
		}else if(type==1){
			mColorFillSelected=color;
		}else if(type==2){
			mColorFillMove=color;
		}
	}
	
	protected void print(String msg) {
		Log.d("indicator", msg);
	}

	private void drawCircle(Canvas can, float cx, float cy, boolean normal, int i) {
		mPaintPageFill.setColor(normal ? mColorFillNormal : mColorFillSelected);
		float radius = mRadius;
		if(!normal){
			if(mSnap){
				radius=mSelectedRadius;
			}else{
				radius=mRadius+(mSelectedRadius-mRadius)*(1-mPageOffset);
				if(i==0){
					cx-=(mSelectedRadius-mRadius)*0.5f*mPageOffset;	
				}
			}
		}else{
			if(i==mCurrentPage+1&&!mSnap&&mPageOffset!=0){
				radius=mRadius+(mSelectedRadius-mRadius)*(mPageOffset);
				mPaintPageFill.setColor(mColorFillSelected);
				normal=false;
				if(mOrientation==HORIZONTAL){
				    cx-=(mSelectedRadius-mRadius)*1.5f*mPageOffset;	
				}else{
				    cy-=(mSelectedRadius-mRadius)*1.5f*mPageOffset;
				}
			}
		}
		float fillRadius = radius - mPaintStroke.getStrokeWidth() / 2.0f;
		can.drawCircle(cx, cy, fillRadius, mPaintPageFill);
		if (fillRadius != radius) {
			can.drawCircle(cx, cy, radius, mPaintStroke);
		}
		if (mShowPageNum) {
			mRectTxt.set(cx - radius, cy - radius, cx + radius, cy + radius);
			drawCircleTxt(can, "" + (i + 1), normal ? 0 : 1, 0);
		}
	}

	private float formatOffset() {
		float result = mPageOffset;
		if (mPageOffset < 0.5f) {
			result = 1 - mPageOffset;
		}
		print("formatOffset orig offset=" + mPageOffset + " format offset="
				+ result);
		return  result;
	}

	private void drawMoveCircle(Canvas can, float cx, float cy, boolean fixed,
			int i) {
		float radius = (fixed) ? mSelectedRadius
				: (mSelectedRadius * formatOffset( ));
		mPaintFill.setColor(mColorFillMove);
		can.drawCircle(cx, cy, radius, mPaintFill);
		if (mShowMoveNum) {
			mRectTxt.set(cx - radius, cy - radius, cx + radius, cy + radius);
			drawCircleTxt(can, "" + (i + 1), 2, mPageOffset);
		}
	}

	/**
	 * @param type
	 *            0:normal, 1:selected,2:move
	 * @param rate
	 * @throws
	 */
	private void drawCircleTxt(Canvas can, String txt, int type, float rate) {
		int color = mColorTxtMove;
		if (type == 0) {
			color = mColorTxtNormal;
		}else if(type==1){
			color = mColorTxtSelected;
		}
		float txtSize = getTxtSize(txt, 9, mRectTxt);
		mPaintTxt.setTextSize(txtSize);
		mPaintTxt.setColor(color);
		can.drawText(txt, mRectTxt.centerX(), mRectTxt.centerY()
				- getTxtOffset(txtSize), mPaintTxt);
	}

	private float calculateSelectedPosition(float longPos,float dlong) {
		float result=0;
		mCurLong = longPos;
		if (!mSnap) {
			if(mCurrentPage==0){
				dlong=dlong/3f;
				result=+dlong*(mPageOffset);
			}
			mCurLong = longPos-dlong*(mPageOffset);
		} 
		return result;
	}

	private void calculateMovePosition(int n, float longOffset, float dlong) {
		float cx = (mSnap ? mSnapPage : mCurrentPage) * dlong;
		if (!mSnap) {
			cx += mPageOffset * dlong;
		}
		if (n > 1 && mCurrentPage != 0) {
			cx += (mSelectedRadius - mRadius) * 1.5f;
		}else{
			cx+=(mSelectedRadius-mRadius)*0.5f*mPageOffset;
		}
		if (mOrientation == HORIZONTAL) {
			mMovLong = longOffset + cx;
		} else {
			mMovLong = longOffset + cx;
		}
	}

	@Override
	protected void onDraw(Canvas can) {
		final int n = mViewPager == null ? 0 : mViewPager.getAdapter()
				.getCount();
		if (n == 0 || mRect.isEmpty()) {
			return;
		}
		if (mCurrentPage >= n) {
			setCurrentItem(n - 1);
			return;
		}
		final float shortOffsetCenter = mOrientation == HORIZONTAL ? mRect
				.centerY() : mRect.centerX();
		float cenX = 0, cenY = 0, longOffset = 0, longOffsetCenter = 0, dlong = mRadius * 3, dLong = (mRadius + mSelectedRadius) * 1.5f;
		if (mCentered) {
			if (mOrientation == HORIZONTAL) {
				longOffsetCenter = (getWidth() - getWrapLength()) / 2;
				cenY = shortOffsetCenter;
			} else {
				longOffsetCenter = (getHeight() - getWrapLength()) / 2;
				cenX = shortOffsetCenter;
			}
			longOffsetCenter = Math.max(longOffsetCenter, 0);
		}
		longOffsetCenter += (0 == mCurrentPage ? mSelectedRadius : mRadius);
		longOffset = longOffsetCenter;
		calculateMovePosition(n, longOffset, dlong);
		// Draw stroked circles
		for (int i = 0; i < n; i++) {
			if (i == mCurrentPage) {
				longOffsetCenter+=calculateSelectedPosition(longOffsetCenter,mPageOffset==0?0:dLong-dlong);
				if (mOrientation == HORIZONTAL) {
					cenX = mCurLong;
				} else {
					cenY = mCurLong;
				}
				drawCircle(can, cenX, cenY, false, i);
				longOffsetCenter += dLong;
				if (mSnap) {
					drawMoveCircle(can, cenX, cenY, mSnap, mCurrentPage);
				} 
			} else {
				if (mOrientation == HORIZONTAL) {
					cenX = longOffsetCenter;
				} else {
					cenY = longOffsetCenter;
				}
				drawCircle(can, cenX, cenY, true, i);
				if (i + 1 == mCurrentPage) {
					longOffsetCenter += dLong;
				} else {
					longOffsetCenter += dlong;
				}
			}
		}
		if (!mSnap) {
			if (mOrientation == HORIZONTAL) {
				cenX = mMovLong;
			} else {
				cenY = mMovLong;
			}
			drawMoveCircle(can, cenX, cenY, mSnap, mPageOffset > 0.5f ? mCurrentPage + 1 : mCurrentPage);
			
			if (mPageOffset == 0) {
				drawMoveCircle(can, cenX, cenY, true, mCurrentPage);
			}
		}
	}

	protected int getWrapLength() {
		int result = 0;
		final int n = mViewPager.getAdapter().getCount();
		if (n > 0) {
			if (n == 1) {
				result += 2*Math.max(mSelectedRadius, mRadius) + 1;
			} else {
				if (n > 1) {
					result += ((n - 1) << 1) * mRadius + mSelectedRadius * 2;
					result += (n - 2) * mRadius + mSelectedRadius;
				}
			}
		}
		return result;
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	protected int measureLong(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
			// We were told how big to be
			result = specSize;
		} else {
			// Calculate the width according the views count
			result = getPaddingLeft() + getPaddingRight();
			result += getWrapLength();
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	protected int measureShort(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the height
			final int n = mViewPager == null ? 0 : mViewPager.getAdapter()
					.getCount();
			result = getPaddingTop() + getPaddingBottom();
			if (n > 0) {
				result += (int) (2 * Math.max(mRadius, mSelectedRadius) + 1);
			}
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	public CirclePageIndicatorDigital(Context context) {
		this(context, null);
	}

	public CirclePageIndicatorDigital(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
	}

	public CirclePageIndicatorDigital(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		mPaintTxt.setTextAlign(Align.CENTER);
		mRadius*=1.5f;
		mSelectedRadius = mRadius * 1.5f;
	}

	public boolean onViewFirstSteped(int w, int h) {
		return false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w == 0 || h == 0) {
			mRect.setEmpty();
		} else {
			boolean firstInit = mRect.isEmpty();
			int pl = getPaddingLeft(), pr = getPaddingRight();
			int pt = getPaddingTop(), pb = getPaddingBottom();
			mRect.set(pl, pt, w - pr, h - pb);
			if (!firstInit || !onViewFirstSteped(w, h)) {
				invalidate();
			}
		}
	}

	public Rect getTxtBounds(String txt, int len, float txtSize, Rect recDes) {
		if (recDes == null) {
			recDes = new Rect();
		}
		mPaintTxt.setTextSize(txtSize);
		mPaintTxt.getTextBounds(txt, 0, len, recDes);
		return recDes;
	}

	public float getTxtSize(String txt, float minSize, RectF recMax) {
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

	public float getTxtOffset(float txtSize) {
		mPaintTxt.setTextSize(txtSize);
		if (mFontMetric == null) {
			mFontMetric = new FontMetrics();
		}
		mPaintTxt.getFontMetrics(mFontMetric);
		return (mFontMetric.ascent + mFontMetric.descent) / 2;
	}
}
