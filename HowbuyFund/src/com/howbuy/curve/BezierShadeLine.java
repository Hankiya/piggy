package com.howbuy.curve;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;

import com.howbuy.curve.CurveControl.CtrlOption;
import com.howbuy.lib.compont.BezierParam;

/**
 * this class for draw curve ,and can be extends for custom draw
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */

public class BezierShadeLine extends Curve {
	protected BezierParam mBezier;
	protected boolean mShowControl = true;

	public BezierShadeLine(CtrlOption ctrlOpt, RectF rect, ArrayList<? extends ICharData> data,
			ICharType type) {
		super(ctrlOpt, rect, data, type);
		mBezier = new BezierParam(new Path(), mCtrlOpt.getBezierSmoonth());
	}

	protected void drawCurve(Canvas g, Paint paint, float dx, float padR) {
		int[] se = adjustStartAdEnd();
		float x = mRect.right - 1 - padR + (mCtrlOpt.mIOffset - se[0]) * dx, right = x;
		float val0 = getYAt(se[0]);
		if (mLen > 1) {
			float strokWid = paint.getStrokeWidth();
			paint.setStrokeWidth(2 * strokWid);
			// Cap strokCap=paint.getStrokeCap();
			// paint.setStrokeCap(Cap.ROUND);
			se[0] += 1;
			mBezier.setSmoonth(mCtrlOpt.getBezierSmoonth());
			mBezier.setFirstPoint(x, val0, x - dx, se[0] == mLen ? val0 : getYAt(se[0]));
			x = x - dx - dx;
			for (int i = se[0] + 1; i < se[1]; i++, x -= dx) {
				Path path = mBezier.getNextPath(x, getYAt(i), true);
				paint.setShader(new LinearGradient(mBezier.getPrePoint().x,
						mBezier.getPrePoint().y, mBezier.getCurPoint().x, mBezier.getCurPoint().y,
						mData.get(i - 2).getColor(false), mData.get(i - 1).getColor(false),
						TileMode.CLAMP));
				g.drawPath(path, paint);
				path.lineTo(mBezier.getCurPoint().x, mRect.bottom);
				path.lineTo(mBezier.getPrePoint().x, mRect.bottom);
				path.close();
				mPath.addPath(path);
				if (mShowControl) {
					RectF rec = mBezier.getCtrlPoint();
					g.drawCircle(rec.right, rec.bottom, 1.5f, paint);
					g.drawCircle(rec.left, rec.top, 1.5f, paint);
				}
			}
			Path path = mBezier.getLastPath(true);
			paint.setShader(new LinearGradient(mBezier.getPrePoint().x, mBezier.getPrePoint().y,
					mBezier.getCurPoint().x, mBezier.getCurPoint().y, mData.get(se[1] - 2)
							.getColor(false), mData.get(se[1] - 1).getColor(false), TileMode.CLAMP));
			g.drawPath(path, paint);
			path.lineTo(mBezier.getCurPoint().x, mRect.bottom);
			path.lineTo(mBezier.getPrePoint().x, mRect.bottom);
			path.close();
			mPath.addPath(path);
			paint.setShader(null);
			paint.setStrokeWidth(strokWid);
			// paint.setStrokeCap(strokCap);
			if (mShowControl) {
				RectF rec = mBezier.getCtrlPoint();
				g.drawCircle(rec.right, rec.bottom, 1.5f, paint);
				g.drawCircle(rec.left, rec.top, 1.5f, paint);
			}
			drawPathShade(g, paint, x + dx, right);
		} else {
			g.drawCircle(x, val0, 2, paint);
		}
	}

}
