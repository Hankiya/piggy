package com.howbuy.lib.control.curveview;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.howbuy.lib.compont.BezierParam;
import com.howbuy.lib.control.curveview.CurveControl.CtrlOption;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;

/**
 * this class for draw curve ,and can be extends for custom draw
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */

public class BezierLine extends Curve{
	protected  BezierParam mBezier;
	protected boolean mShowControl=false;
	
	public BezierLine(CtrlOption ctrlOpt, RectF rect,
			ArrayList<? extends ICharData> data, ICharType type) {
		super(ctrlOpt, rect, data, type);
	    mBezier=new BezierParam(mPath,mCtrlOpt.getBezierSmoonth());
	}
	
	protected void drawCurve(Canvas g, Paint paint, float dx, float padR) {
	
		int[] se = adjustStartAdEnd();
		float x = mRect.right - 1 - padR + (mCtrlOpt.mIOffset - se[0]) * dx, right = x;
		float  val0 = getYAt(se[0]);
		if (mLen > 1) {
			se[0]+=1;  
			mBezier.setSmoonth(mCtrlOpt.getBezierSmoonth());
			mBezier.setFirstPoint(x, val0, x-dx, se[0]==mLen?val0:getYAt(se[0]));
			x =x-dx-dx;
			for (int i = se[0]+1; i < se[1]; i++, x -= dx) {
				mBezier.getNextPath(x, getYAt(i),false);
				if(mShowControl){
					RectF rec=mBezier.getCtrlPoint();
					g.drawCircle(rec.right, rec.bottom, 1.5f, paint);
					g.drawCircle(rec.left, rec.top, 1.5f, paint);
				} 
			}
			mBezier.getLastPath(false);
			if(mShowControl){
				RectF rec=mBezier.getCtrlPoint();
				g.drawCircle(rec.right, rec.bottom, 1.5f, paint);
				g.drawCircle(rec.left, rec.top, 1.5f, paint); 	
			}   
			g.drawPath(mPath, paint); 
			mPath.lineTo(mRect.left, mRect.bottom);
			mPath.lineTo(mRect.right, mRect.bottom);
			drawPathShade(g, paint, x + dx, right);
		} else {
			g.drawCircle(x, val0, 2, paint);
		}
	}
 
 
}
