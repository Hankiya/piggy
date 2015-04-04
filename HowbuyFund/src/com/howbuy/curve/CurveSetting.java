package com.howbuy.curve;

import android.content.Context;

public class CurveSetting {
	protected float MIN_SPACE = 0.01f;
	protected float MAX_SPACE = 4000;
	protected String mMeasureTxt = "99.9929";
	protected float mDensity = 1;
	/* line size and txt size */
	protected float mGridSize = 0.75f;
	protected float mCoordSize = 1.25f;
	protected float mCrossSize = 1.5f;
	protected float mCurveSize = 1;
	protected float mCurveTxtSize = 11;
	protected float mClickRaidus = 10;
	protected float mCoordTxtXSize = 13;
	protected float mCoordTxtYSize = 19;
	protected float mCoordTxtWidth = 1f;
	protected float mBubbleTxtSize = 14;
	protected float mTextYOffset = 5;
	private float mHDeep = 10, mVDeep = 15;
	/* line color and txt color */
	protected int mCoordColor = 0xff000000;
	protected int mGridColor = 0xff222222;
	protected int mCoordTxtYColor = 0xee000000;
	protected int mCoordTxtXColor = 0xffff0011;
	protected int mCrossColor = 0xff0aa0ff;
	protected int mBubbleYColor = 0xaa1100f0;
	protected int mBubbleXColor = 0xaaaa0000;
	protected int mBubbleTxtYColor = 0xff01f01f;
	protected int mBubbleTxtXColor = 0xff770077;
	protected int mBackYColor = 0xffff0000;
	protected int mBackXColor = 0xff00ff00;
	protected int mRow = 5, mCol = 4;
	protected float mBezierSmooth = 0.5f;
	protected float mCoordUpWeight = 0.10f, mCoordBotWeight = 0.05f;
	protected float mInitOffset = 0;
	protected boolean mEnableCoordX = true;
	protected boolean mEnableCoordY = true;
	protected boolean mEnableCoordTxtY = true;
	protected boolean mEnableCoordTxtX = false;
	protected boolean mEnableGrid = true;
	protected boolean mEnableBackgroud = true;
	protected boolean mEnable3D = false;
	protected boolean mEnableBubble = true;
	protected boolean mEnableGridDash = true;
	protected boolean mEnableTxtYInSide = false;
	protected boolean mEnableCrossHorizonal = true;
	protected boolean mEnableAutoCalcuMinMax = false;
	protected boolean mEnableInitCent = true;
	protected boolean mEnableInitShowAll = false;
	protected boolean mEnableInitOffset = true;
	protected boolean mEnableSelectArea = false;

	public int getBackXColor() {
		return mBackXColor;
	}

	public int getBackYColor() {
		return mBackYColor;
	}

	public void setBackColor(int colorX, int colorY) {
		mBackXColor = colorX;
		mBackYColor = colorY;

	}

	public CurveSetting(Context cx) {
		this(cx.getResources().getDisplayMetrics().density);
	}

	public CurveSetting(float density) {
		this.mDensity = density;
		this.mCoordSize *= mDensity;
		this.mGridSize *= mDensity;
		this.mCoordTxtYSize *= mDensity;
		this.mCoordTxtXSize *= mDensity;
		this.mCrossSize *= mDensity;
		this.mBubbleTxtSize *= mDensity;
		this.mCurveSize *= mDensity;
		this.mCurveTxtSize *= mDensity;
		this.mClickRaidus *= mDensity;
		this.mTextYOffset *= mDensity;
		this.mCoordTxtWidth *= mDensity;
		this.mHDeep *= mDensity;
		this.mVDeep *= mDensity;
		MIN_SPACE *= mDensity;
		MAX_SPACE *= mDensity;
	}

	public float getHdeep() {
		return mHDeep;
	}

	public float getVdeep() {
		return mVDeep;
	}

	public void setDeep(float hdep, float vdep) {
		mHDeep = hdep * mDensity;
		mVDeep = vdep * mDensity;
	}

	public float getDensity() {
		return this.mDensity;
	}

	public float getBezierSmoonth() {
		return mBezierSmooth;
	}

	public void setBezierSmoonth(float smoth) {
		this.mBezierSmooth = smoth;
	}

	public float getGridSize() {
		return mGridSize;
	}

	public void setGridSize(float dpVal) {
		this.mGridSize = mDensity * dpVal;
	}

	public float getCoordSize() {
		return mCoordSize;
	}

	public void setCoordSize(float dpVal) {
		this.mCoordSize = mDensity * dpVal;
	}

	public float getCrossSize() {
		return mCrossSize;
	}

	public void setCrossSize(float dpVal) {
		this.mCrossSize = mDensity * dpVal;
	}

	public float getCurveSize() {
		return mCurveSize;
	}

	public void setCurveSize(float dpVal) {
		this.mCurveSize = mDensity * dpVal;
	}

	public float getCurveTxtSize() {
		return mCurveTxtSize;
	}

	public void setCurveTxtSize(float dpVal) {
		this.mCurveTxtSize = mDensity * dpVal;
	}

	public float getCoordTxtXSize() {
		return mCoordTxtXSize;
	}

	public void setCoordTxtXSize(float dpVal) {
		this.mCoordTxtXSize = mDensity * dpVal;
	}

	public float getClickRaidus() {
		return mClickRaidus;
	}

	public void setClickRaidus(float dpVal) {
		this.mClickRaidus = mDensity * dpVal;
	}

	public float getCoordTxtWidth() {
		return mCoordTxtWidth;
	}

	public void setCoordTxtWidth(float width) {
		mCoordTxtWidth = width;
	}

	public float getCoordTxtYSize() {
		return mCoordTxtYSize;
	}

	public void setCoordTxtYSize(float dpVal) {
		this.mCoordTxtYSize = mDensity * dpVal;
	}

	public float getBubbleTxtSize() {
		return mBubbleTxtSize;
	}

	public void setBubbleTxtSize(float dpVal) {
		this.mBubbleTxtSize = mDensity * dpVal;
	}

	public void setMinMaxSpace(float dpMin, float dpMax) {
		this.MAX_SPACE = mDensity * dpMax;
		this.MIN_SPACE = mDensity * dpMin;
	}

	public void setCoordUpBotWeight(float up, float bot) {
		if (up < 0) {
			up = mCoordUpWeight;
		}
		if (bot < 0) {
			bot = mCoordBotWeight;
		}
		if (mCoordUpWeight != up || mCoordBotWeight != bot) {
			mCoordUpWeight = up;
			mCoordBotWeight = bot;
			if (mCoordUpWeight + mCoordBotWeight > 0.5f) {
				float scale = 0.5f / (mCoordUpWeight + mCoordBotWeight);
				mCoordUpWeight *= scale;
				mCoordBotWeight *= scale;
			}
		}
	}

	public int getCoordColor() {
		return mCoordColor;
	}

	public void setCoordColor(int mCoordColor) {
		this.mCoordColor = mCoordColor;
	}

	public int getGridColor() {
		return mGridColor;
	}

	public void setGridColor(int mGridColor) {
		this.mGridColor = mGridColor;
	}

	public int getCoordTxtYColor() {
		return mCoordTxtYColor;
	}

	public void setCoordTxtYColor(int mCoordTxtYColor) {
		this.mCoordTxtYColor = mCoordTxtYColor;
	}

	public int getCoordTxtXColor() {
		return mCoordTxtXColor;
	}

	public void setCoordTxtXColor(int mCoordTxtXColor) {
		this.mCoordTxtXColor = mCoordTxtXColor;
	}

	public int getCrossColor() {
		return mCrossColor;
	}

	public void setCrossColor(int mCrossColor) {
		this.mCrossColor = mCrossColor;
	}

	public int getBubbleYColor() {
		return mBubbleYColor;
	}

	public void setBubbleYColor(int mBubbleYColor) {
		this.mBubbleYColor = mBubbleYColor;
	}

	public int getBubbleXColor() {
		return mBubbleXColor;
	}

	public void setBubbleXColor(int mBubbleXColor) {
		this.mBubbleXColor = mBubbleXColor;
	}

	public int getBubbleTxtYColor() {
		return mBubbleTxtYColor;
	}

	public void setBubbleTxtYColor(int mBubbleTxtYColor) {
		this.mBubbleTxtYColor = mBubbleTxtYColor;
	}

	public int getBubbleTxtXColor() {
		return mBubbleTxtXColor;
	}

	public void setBubbleTxtXColor(int mBubbleTxtXColor) {
		this.mBubbleTxtXColor = mBubbleTxtXColor;
	}

	public int getRow() {
		return mRow;
	}

	public void setRow(int mRow) {
		this.mRow = mRow;
	}

	public int getCol() {
		return mCol;
	}

	public void setCol(int mCol) {
		this.mCol = mCol;
	}

	public float getCoordUpWeight() {
		return mCoordUpWeight;
	}

	public void setCoordUpWeight(float mCoordUpWeight) {
		this.mCoordUpWeight = mCoordUpWeight;
	}

	public float getCoordBotWeight() {
		return mCoordBotWeight;
	}

	public void setCoordBotWeight(float mCoordBotWeight) {
		this.mCoordBotWeight = mCoordBotWeight;
	}

	public float getInitOffset() {
		return mInitOffset;
	}

	public void setInitOffset(float mInitOffset) {
		this.mInitOffset = mInitOffset;
	}

	public boolean isEnableCoordX() {
		return mEnableCoordX;
	}

	public void setEnableCoordX(boolean mEnableCoordX) {
		this.mEnableCoordX = mEnableCoordX;
	}

	public boolean isEnableCoordY() {
		return mEnableCoordY;
	}

	public void setEnableCoordY(boolean mEnableCoordY) {
		this.mEnableCoordY = mEnableCoordY;
	}

	public boolean isEnableBubble() {
		return mEnableBubble;
	}

	public void setEnableBackgroud(boolean enable) {
		this.mEnableBackgroud = enable;
	}

	public boolean isEnableBackgroud() {
		return mEnableBackgroud;
	}

	public void setEnable3D(boolean enable) {
		this.mEnable3D = enable;
	}

	public boolean isEnable3D() {
		return mEnable3D;
	}

	public void setEnableBubble(boolean mEnableBubble) {
		this.mEnableBubble = mEnableBubble;
	}

	public boolean isEnableGrid() {
		return mEnableGrid;
	}

	public void setEnableGrid(boolean mEnableGrid) {
		this.mEnableGrid = mEnableGrid;
	}

	public boolean isEnableGridDash() {
		return mEnableGridDash;
	}

	public void setEnableGridDash(boolean mEnableGridDash) {
		this.mEnableGridDash = mEnableGridDash;
	}

	public boolean isEnableCoordTxtY() {
		return mEnableCoordTxtY;
	}

	public void setEnableCoordTxtY(boolean mEnableCoordTxtY) {
		this.mEnableCoordTxtY = mEnableCoordTxtY;
	}

	public boolean isEnableCoordTxtX() {
		return mEnableCoordTxtX;
	}

	public void setEnableCoordTxtX(boolean mEnableCoordTxtX) {
		this.mEnableCoordTxtX = mEnableCoordTxtX;
	}

	public boolean isEnableCrossHorizonal() {
		return mEnableCrossHorizonal;
	}

	public void setEnableCrossHorizonal(boolean mEnableCrossHorizonal) {
		this.mEnableCrossHorizonal = mEnableCrossHorizonal;
	}

	public boolean isEnableAutoCalcuMinMax() {
		return mEnableAutoCalcuMinMax;
	}

	public void setEnableAutoCalcuMinMax(boolean mEnableAutoCalcuMinMax) {
		this.mEnableAutoCalcuMinMax = mEnableAutoCalcuMinMax;
	}

	public boolean isEnableInitCent() {
		return mEnableInitCent;
	}

	public void setEnableInitCent(boolean mEnableInitCent) {
		this.mEnableInitCent = mEnableInitCent;
	}

	public boolean isEnableInitShowAll() {
		return mEnableInitShowAll;
	}

	public void setEnableInitShowAll(boolean mEnableInitShowAll) {
		this.mEnableInitShowAll = mEnableInitShowAll;
	}

	public boolean isEnableInitOffset() {
		return mEnableInitOffset;
	}

	public void setEnableInitOffset(boolean mEnableInitOffset) {
		this.mEnableInitOffset = mEnableInitOffset;
	}

	public boolean isEnableSelectArea() {
		return mEnableInitOffset;
	}

	public void setEnableSelectArea(boolean mEnableSelectArea) {
		this.mEnableSelectArea = mEnableSelectArea;
	}

	public boolean isEnableTxtYInSide() {
		return mEnableTxtYInSide;
	}

	public void setEnableTxtYInSide(boolean enableTxtYInSide) {
		this.mEnableTxtYInSide = enableTxtYInSide;
	}

	public String getMeasureTxt() {
		return mMeasureTxt;
	}

	public void setMeasureTxt(String mMeasureTxt) {
		this.mMeasureTxt = mMeasureTxt;
	}
}
