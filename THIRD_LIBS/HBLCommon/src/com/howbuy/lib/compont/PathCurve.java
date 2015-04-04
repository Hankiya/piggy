package com.howbuy.lib.compont;

import java.util.ArrayList;
import java.util.Collection;
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-1-24 上午10:34:56
 */
public class PathCurve {

	// The points in the path
	ArrayList<PathPoint> mPoints = new ArrayList<PathPoint>();
	private long mTimeSpace = 0;
	protected int mDuration = 3000;
	protected boolean mHasInited = false;

	public PathCurve(int druation) {
		this.mDuration = druation;
	}
	public void moveTo(float x, float y) {
		if (!mHasInited) {
			mPoints.add(PathPoint.moveTo(x, y));
		}
	}
	public void lineTo(float x, float y) {
		if (!mHasInited) {
			mPoints.add(PathPoint.lineTo(x, y));
		}
	}
	public void cubicTo(float c0X, float c0Y, float c1X, float c1Y, float x,
			float y) {
		if (!mHasInited) {
			mPoints.add(PathPoint.cubicTo(c0X, c0Y, c1X, c1Y, x, y));
		}
	}
	
	public void quadTo(float cX, float cY,float x,
			float y) {
		if (!mHasInited) {
			mPoints.add(PathPoint.quadTo(cX, cY, x, y));
		}
	}
	
	public Collection<PathPoint> getPoints() {
		return mPoints;
	}
	public PathPoint getPoint(int index){
		if(index>=0&&index<mPoints.size()){
			return mPoints.get(index);
		}
		return null;
	}
	public PathPoint getPoint(boolean lastOflastOrFirst){
		if(lastOflastOrFirst){
			return getPoint(mPoints.size()-1);
		}
		return getPoint(0);
	}
	public void setDuration(int duration) {
		if (duration > 0) {
			mDuration = duration;
		}
	}

	public void reset() {
		mPoints.clear();
		mTimeSpace = 0;
		mHasInited = false;
	}

	public void init() {
		if (!mHasInited) {
			int segmentCount = 0;
			for (int i = 0; i < mPoints.size(); ++i) {
				if (mPoints.get(i).mOperation != PathPoint.MOVE) {
					mPoints.get(i).mTimeFrame = ++segmentCount;
				} else {
					mPoints.get(i).mTimeFrame = segmentCount;
				}
			}
			if (segmentCount > 0) {
				mTimeSpace = mDuration / segmentCount;
				for (int i = 0; i < mPoints.size(); ++i) {
					mPoints.get(i).mTimeFrame *= mTimeSpace;
				}
				mHasInited = true;
			}
		}
	}

	public PathPoint getValueAt(int time) {
		PathPoint result=null;
		if (!mHasInited || time > mDuration) {
			reset();
		} else {
			int s = -1,e = -1;float t=0;
			for (int i = 0; i < mPoints.size(); i++) {
				if (mPoints.get(i).mTimeFrame > time) {
					s = i-1;
					break;
				}
			}
			if (s != -1) {
				for (int i = s + 1; i < mPoints.size(); i++) {
					if (mPoints.get(i).mOperation != PathPoint.MOVE) {
						e = i;
						break;
					}
				}
				if (e != -1) {
					PathPoint ps = mPoints.get(e - 1), pe = mPoints.get(e);
					t = (time - ps.mTimeFrame) /(float) (pe.mTimeFrame - ps.mTimeFrame);
					result= evaluate(t, ps, pe);
				}
			}
		}
		return result;
	}

	private PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
		float x, y;
		if (endValue.mOperation == PathPoint.CURVE3||endValue.mOperation == PathPoint.CURVE2) {
			float oneMinusT = 1 - t;
			x = oneMinusT * oneMinusT * oneMinusT * startValue.mX + 3
					* oneMinusT * oneMinusT * t * endValue.mControl0X + 3
					* oneMinusT * t * t * endValue.mControl1X + t * t * t
					* endValue.mX;
			y = oneMinusT * oneMinusT * oneMinusT * startValue.mY + 3
					* oneMinusT * oneMinusT * t * endValue.mControl0Y + 3
					* oneMinusT * t * t * endValue.mControl1Y + t * t * t
					* endValue.mY;
		} else if (endValue.mOperation == PathPoint.LINE) {
			x = startValue.mX + t * (endValue.mX - startValue.mX);
			y = startValue.mY + t * (endValue.mY - startValue.mY);
		} else {
			x = endValue.mX;
			y = endValue.mY;
		}
		return PathPoint.moveTo(x, y);
	}
	
	/*
        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLoc", 
                new PathEvaluator (){}, path.getPoints().toArray());
        
        public class PathEvaluator implements TypeEvaluator<PathPoint> {
            @Override
            public PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
                float x, y;
                if (endValue.mOperation == PathPoint.CURVE) {
                    float oneMinusT = 1 - t;
                    x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                            3 * oneMinusT * oneMinusT * t * endValue.mControl0X +
                            3 * oneMinusT * t * t * endValue.mControl1X +
                            t * t * t * endValue.mX;
                    y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                            3 * oneMinusT * oneMinusT * t * endValue.mControl0Y +
                            3 * oneMinusT * t * t * endValue.mControl1Y +
                            t * t * t * endValue.mY;
                } else if (endValue.mOperation == PathPoint.LINE) {
                    x = startValue.mX + t * (endValue.mX - startValue.mX);
                    y = startValue.mY + t * (endValue.mY - startValue.mY);
                } else {
                    x = endValue.mX;
                    y = endValue.mY;
                }
                Log.d("pathanim", "startValue="+startValue+" endValue="+endValue+"    x="+x+" y="+y+" t="+t);
                return PathPoint.moveTo(x, y);
            }
	 */
}
