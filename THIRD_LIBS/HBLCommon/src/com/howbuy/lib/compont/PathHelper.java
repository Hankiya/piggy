package com.howbuy.lib.compont;

import android.graphics.PointF;
import android.graphics.RectF;

public class PathHelper {
	private float mSmooth = 0f, mInitAdjust = 20;
	// bezier path.
	private PathCurve mBPath = null;
	protected final RectF mCtrlRect = new RectF(0, 0, 0, 0);
	// 原始线段左中右三个点外部传入.
	private final PointF mPre = new PointF(0, 0);
	private final PointF mCur = new PointF(0, 0);
	private final PointF mNxt = new PointF(0, 0);
	// 以下是计算过程中的点.
	private final PointF mMidPr = new PointF(0, 0);// 左线段中点.
	private final PointF mMidNx = new PointF(0, 0);// 右线段中点.
	private final PointF mPfCtrl = new PointF(0, 0);
	private float mLenPr = 0, mLenNx = 0;// 左右线段的长度.

	// smoonth控制圆滑因素,越大越平滑,越小越尖锐.
	public PathHelper(float smoonth) {
		setSmoonth(smoonth);
	}

	public void setSmoonth(float smoonth) {
		smoonth = Math.max(Math.min(1f, smoonth), 0.000001f);
		mSmooth = (1 - smoonth) / smoonth;
	}

	/**
	 * 当下一个点不存在时调用它.
	 * 
	 * @return Path
	 * @throws
	 */
	public PathCurve getLastPath() {
		float x = mCur.x + (mCur.x - mPre.x) / mInitAdjust;
		float y = mCur.y + mCur.y - mPre.y;
		return getNextPath(x, y);
	}

	/**
	 * 当下一个点存在,且当前点已经设置过时调用它.
	 * 
	 * @param @param x
	 * @param @param y
	 * @param @return
	 * @return Path
	 * @throws
	 */
	public PathCurve getNextPath(float nextX, float nextY) {
		if (nextX != mCur.x && nextY != mCur.y) {
			mNxt.set(nextX, nextY);
			mMidNx.set(mNxt.x - mCur.x, mNxt.y - mCur.y);
			mLenNx = (float) Math.sqrt(mMidNx.x * mMidNx.x + mMidNx.y
					* mMidNx.y);
			mMidNx.set((mCur.x + mNxt.x) / 2, (mCur.y + mNxt.y) / 2);
		}
		return calCtrlAdPath();
	}

	/**
	 * 当前一个点不存在时,调用,nxtX,nxtY要如实传入.
	 * 
	 * @param @param curX
	 * @param @param curY
	 * @return void
	 * @throws
	 */
	public void setFirstPoint(PathCurve path, float curX, float curY,
			float nxtX, float nxtY) {
		mBPath = path;
		mCur.set(curX, curY);
		mPre.set(curX + (curX - nxtX) / mInitAdjust, curY + curY - nxtY);
		mMidPr.set(mPre.x - mCur.x, mPre.y - mCur.y);
		mLenPr = (float) Math.sqrt(mMidPr.x * mMidPr.x + mMidPr.y
				* mMidPr.y);
		mMidPr.set((mCur.x + mPre.x) / 2, (mCur.y + mPre.y) / 2);
		mNxt.set(nxtX, nxtY);
		mMidNx.set(mNxt.x - mCur.x, mNxt.y - mCur.y);
		mLenNx = (float) Math.sqrt(mMidNx.x * mMidNx.x + mMidNx.y
				* mMidNx.y);
		mMidNx.set((mCur.x + mNxt.x) / 2, (mCur.y + mNxt.y) / 2);
		calCtrlPoint();
		reset();
		mBPath.reset();
		mBPath.moveTo(mPre.x, mPre.y);
		mLenNx = 0;
	}

	public RectF getCtrlPoint() {
		return mCtrlRect;
	}

	public PointF getCurPoint() {
		return mPre;
	}

	public PointF getPrePoint() {
		return mNxt;
	}

	private PathCurve calCtrlAdPath() {
		if (mLenNx != 0) {
			calCtrlPoint();
			return getBezier3Path();
		}
		return mBPath;
	}

	private void calCtrlPoint() {// 计算控制点.
		float a = mLenPr / mLenNx, b = a + 1;
		float mx = (mMidPr.x + a * mMidNx.x) / b;
		float my = (mMidPr.y + a * mMidNx.y) / b;
		b = mSmooth + 1;
		mCtrlRect.right = (mMidPr.x + mSmooth * mx) / b;
		mCtrlRect.bottom = (mMidPr.y + mSmooth * my) / b;
		mCtrlRect.left = mPfCtrl.x;
		mCtrlRect.top = mPfCtrl.y;
		mPfCtrl.x = (mMidNx.x + mSmooth * mx) / b;
		mPfCtrl.y = (mMidNx.y + mSmooth * my) / b;
		a = mCur.x - mx;
		b = mCur.y - my;
		mPfCtrl.x += a;
		mPfCtrl.y += b;
		mCtrlRect.right += a;
		mCtrlRect.bottom += b;
	}

	private PathCurve getBezier3Path() {// 更新曲线路径.
		mBPath.cubicTo(mCtrlRect.left, mCtrlRect.top, mCtrlRect.right,
				mCtrlRect.bottom, mCur.x, mCur.y);
		reset();
		return mBPath;
	}

	@SuppressWarnings("unused")
	private PathCurve getBezier2Path(boolean interupt) {// 更新曲线路径.
		if (interupt) {
			mBPath.reset();
			mBPath.moveTo(mPre.x, mPre.y);
		}
		mBPath.quadTo((mCtrlRect.left + mCtrlRect.right) / 2,
				(mCtrlRect.top + mCtrlRect.bottom) / 2, mCur.x, mCur.y);
		reset();
		return mBPath;
	}

	private void reset() {// 数据缓存.
		float x = mPre.x;
		float y = mPre.y;
		mPre.set(mCur);
		mCur.set(mNxt);
		mNxt.set(x, y);
		mMidPr.set(mMidNx);
		mLenPr = mLenNx;
		mLenNx = 0;
	}
}
