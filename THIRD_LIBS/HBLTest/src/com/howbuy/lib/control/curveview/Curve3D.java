package com.howbuy.lib.control.curveview;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import com.howbuy.lib.compont.BezierParam;
import com.howbuy.lib.control.curveview.CurveControl.CtrlOption;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;

/**
 * this class for draw curve ,and can be extends for custom draw
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */
public class Curve3D extends Curve {
	protected BezierParam mBezier;
	protected boolean mShowControlPoint = false;
	protected CurveSetting mSetting = null;

	public Curve3D(CtrlOption ctrlOpt, RectF rect,
			ArrayList<? extends ICharData> data, ICharType type) {
		super(ctrlOpt, rect, data, type);
		mBezier = new BezierParam(new Path(), mCtrlOpt.getBezierSmoonth());
		mSetting = ctrlOpt.getSetting();
	}

	public void draw(Canvas g, Paint paint) {
	
		paint.setColor(mType.getColor(0));
		paint.setStyle(Paint.Style.STROKE);
		mPath.reset();
		drawCurve(g, paint, mCtrlOpt.mSpace, mCtrlOpt.mOffsetRx);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	protected void drawCurve(Canvas g, Paint paint, float dx, float padR) {
		int[] se = adjustStartAdEnd();
		float x = mRect.right - 1 - padR + (mCtrlOpt.mIOffset - se[0]) * dx;
		float val0 = getYAt(se[0]);
		if (mLen > 1) {
			mRecTemp.set(mRect);
			mRecTemp.top -= mSetting.getVdeep();
			mRecTemp.right += mSetting.getHdeep();
			g.restore();
			g.save();
			g.clipRect(mRecTemp);
			float strokWid = paint.getStrokeWidth();
			paint.setStrokeWidth(2 * strokWid);
			float dvx = strokWid;
			int time = (int) (mSetting.getVdeep() / dvx);
			float dhx = mSetting.getHdeep() / time;
			se[0] += 1;
			mBezier.setSmoonth(mCtrlOpt.getBezierSmoonth());
			mBezier.setFirstPoint(x, val0, x - dx, se[0] == mLen ? val0
					: getYAt(se[0]));
			paint.setColor(mData.get(se[0] - 1).getColor(false));
			g.drawLine(mBezier.getCurPoint().x - dhx / 2,
					mBezier.getCurPoint().y + dvx / 2, mBezier.getCurPoint().x
							+ mSetting.getHdeep() + dhx / 2,
					mBezier.getCurPoint().y - mSetting.getVdeep(), paint);
			x = x - dx - dx;
			for (int i = se[0] + 1; i < se[1]; i++, x -= dx) {
				Path path = mBezier.getNextPath(x, getYAt(i), true);
				paint.setShader(new LinearGradient(mBezier.getPrePoint().x,
						mBezier.getPrePoint().y, mBezier.getCurPoint().x,
						mBezier.getCurPoint().y, mData.get(i - 2).getColor(
								false), mData.get(i - 1).getColor(false),
						TileMode.CLAMP));
				g.drawPath(path, paint);
				for (int j = 1; j <= time; j++) {
					path.offset(dhx, -dvx);
					g.drawPath(path, paint);
				}

				if (mShowControlPoint) {
					RectF rec = mBezier.getCtrlPoint();
					g.drawCircle(rec.right, rec.bottom, 1.5f, paint);
					g.drawCircle(rec.left, rec.top, 1.5f, paint);
				}
			}
			Path path = mBezier.getLastPath(true);
			paint.setShader(new LinearGradient(mBezier.getPrePoint().x, mBezier
					.getPrePoint().y, mBezier.getCurPoint().x, mBezier
					.getCurPoint().y, mData.get(se[1] - 2).getColor(false),
					mData.get(se[1] - 1).getColor(false), TileMode.CLAMP));
			g.drawPath(path, paint);
			for (int j = 1; j <= time; j++) {
				path.offset(dhx, -dvx);
				g.drawPath(path, paint);
			}
			g.drawLine(mBezier.getCurPoint().x - dhx / 2,
					mBezier.getCurPoint().y + dvx / 2, mBezier.getCurPoint().x
							+ mSetting.getHdeep() + dhx / 2,
					mBezier.getCurPoint().y - mSetting.getVdeep(), paint);
			paint.setShader(null);
			paint.setStrokeWidth(strokWid);
			// paint.setStrokeCap(strokCap);
			if (mShowControlPoint) {
				RectF rec = mBezier.getCtrlPoint();
				g.drawCircle(rec.right, rec.bottom, 1.5f, paint);
				g.drawCircle(rec.left, rec.top, 1.5f, paint);
			}
			g.restore();
			g.save();
			g.clipRect(mRect);
		} else {
			g.drawCircle(x, val0, 2, paint);
		}
	}
}
