package com.howbuy.lib.control;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.PointF;
import android.graphics.RectF;

import com.howbuy.lib.utils.ViewUtils;

public class Ball { 
	private final Paint mPaint = new Paint();
	private final RectF mRect = new RectF();
	private float mRH = 0, mRV = 0, mDensity = 1,mRoateDegree;
	private PointF mCenP = new PointF();
	private boolean mVisible=true,mRoateBall;    
	private int mColorBall = 0xffe57c35;
	private int mColorArrow = 0xffeeeeee;
    private float mSizeArrow=3; 
	public Ball(float density) {
		this.mDensity = density;
		mSizeArrow*=mDensity;
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
	}

	public void draw(Canvas can) {
		if(mVisible){
			can.save();
			drawBall(can);
			drawArrow(can);	
			can.restore();
		}
	}
	private void drawBall(Canvas can){
	   	if(mRoateBall){
	      can.rotate(mRoateDegree,mRect.centerX(),mRect.centerY());
	    }
		mPaint.setColor(mColorBall);
		can.drawOval(mRect, mPaint);
	}
    private void drawArrow(Canvas can){
    	float cx=mRect.centerX();
    	PointF pCTop=ViewUtils.dividePoint(cx, mRect.top, cx, mRect.bottom, 0.3f);
    	PointF pCBot=ViewUtils.dividePoint(cx, mRect.bottom,cx, mRect.top, 0.3f);
    	PointF pCCen=ViewUtils.dividePoint(pCTop,pCBot,2f);
    	PointF pLeft=ViewUtils.dividePoint(pCCen, mRect.left,pCCen.y, 0.4f);
    	PointF pRight=ViewUtils.dividePoint(pCCen, mRect.right,pCCen.y, 0.4f);
    	mPaint.setColor(mColorArrow);
    	mPaint.setStrokeWidth(mSizeArrow);
    	mPaint.setStrokeCap(Cap.ROUND);
    	if(!mRoateBall){
    	can.rotate(mRoateDegree,mRect.centerX(),mRect.centerY());
    	}
    	can.drawLine(pCBot.x, pCBot.y, pCTop.x, pCTop.y, mPaint);
    	can.drawLine(pLeft.x, pLeft.y, pCBot.x, pCBot.y, mPaint);
    	can.drawLine(pRight.x, pRight.y, pCBot.x, pCBot.y, mPaint);
    }
	/**
	 * @return 0 for equeal ; 1 for RH>RV ;-1 for RH<RV;
	 * @throws
	 */
	public int compareR() {
		if (mRH == mRV) {
			return 0;
		} else {
			if (mRH > mRV) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public float getRVertical() {
		return mRV;
	}

	public float getRHorizonal() {
		return mRH;
	}

	public final RectF getBounds() {
		return mRect;
	}

	public void setVisible(boolean visible){
		mVisible=visible;
	}
	public boolean isVisible(){
		return mVisible;
	}
	public boolean setBounds(RectF rect) {
		if (rect != null && !rect.isEmpty()) {
			mRect.set(rect);
			mRH = mRect.width() / 2;
			mRV = mRect.height() / 2;
			mCenP.set(mRect.centerX(), mRect.centerY());
			return true;
		}
		return false;
	}

	public boolean setBounds(float l, float t, float r, float b) {
		if (!(l >= r || t>= b)) {
			mRect.set(l, t, r, b);
			mRH = mRect.width() / 2;
			mRV = mRect.height() / 2;
			mCenP.set(mRect.centerX(), mRect.centerY());
			return true;
		}
		return false;
	}
	
	public void setOffset(boolean toOffset,float dx,float dy){
		if(toOffset){
			mRect.offsetTo(dx, dy);
		}else{
			mRect.offset(dx, dy);	
		}
		setBounds(mRect);
	}
	
	public void setCenter(float x,float y){
		x-=mRect.centerX();
		y-=mRect.centerY();
		setOffset(false,x,y);
	}
	
	public void setRoate(float roate,boolean roateBall){
		mRoateBall=roateBall;
		roate=(360+roate%360)%360;
		if(roate!=mRoateDegree){
			mRoateDegree=roate;	
		}
	}
    public float getArrowSize(){
    	return mSizeArrow;
    }
    public void setArrowSize(float size){
    	mSizeArrow=size*mDensity;
    }
    public int getArrowColor(){
    	return mColorArrow;
    }
    public void setArrowColor(int color){
    	mColorArrow=color;
    }
    public int getBallColor(){
    	return mColorBall;
    }
    public void setBallColor(int color){
    	mColorBall=color;
    }
}
