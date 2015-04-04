package com.howbuy.lib.control.curveview;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;

import com.howbuy.lib.control.curveview.CurveControl.CtrlOption;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;
import com.howbuy.lib.utils.StrUtils;

/**
 * this class for draw curve ,and can be extends for custom draw
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */
public class CurveRectTxt extends Curve {
	private int mCurveTxtColor=0xff1a005a;
	public CurveRectTxt(CtrlOption ctrlOption, RectF rect,
			ArrayList<? extends ICharData> datas, ICharType charType) {
		super(ctrlOption, rect, datas, charType);
	}
	public void draw(Canvas g, Paint paint) {
	
		paint.setColor(mType.getColor(0));
		paint.setStyle(Paint.Style.STROKE);
		mPath.reset();
		drawCurve(g, paint, mCtrlOpt.mSpace, mCtrlOpt.mOffsetRx);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
	}
	protected void drawCurve(Canvas g, Paint paint, float dx, float padR) {
		Path pa=new Path();
		pa.addRect(mRect, Direction.CW);
		final Rect rectf=mCtrlOpt.getRectFrame();
		//mRect.left-mCtrlOpt.getSetting().getmArrowSize() change to rectf.left
		pa.addRect(new RectF(mRect.left-mCtrlOpt.getSetting().getArrowSize(),mRect.bottom,rectf.right,rectf.bottom), Direction.CW);
	    g.restore();
		g.save();
		g.clipPath(pa);
		PathEffect pathEffect = paint.getPathEffect();
		paint.setPathEffect(null);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextAlign(Align.CENTER);
		float width=paint.getStrokeWidth();
		paint.setStrokeWidth(1.5f);
	
		int[] se = adjustStartAdEnd();
		float x = mRect.right - 1 - padR + (mCtrlOpt.getIndexOffset() - se[0]) * dx;
		padR = (dx / 4)*1.1f;
		RectF rec = new RectF();
		float r=padR/7;
		for (int i = se[0]; i < se[1]; i++, x -= dx) {
			ICharData data = mData.get(i);
			rec.top = getYAt(data);
			rec.bottom = mRect.bottom+r;
			rec.left = x - padR;
			rec.right = x + padR;
			mRecTemp.set(mRect);
			mRecTemp.bottom-=mCtrlOpt.getSetting().getCoordSize()/2;
			g.save();
			g.clipRect(mRecTemp);
			paint.setColor( data.getColor(false));
			mRecTemp.set(rec);
			if(!mCtrlOpt.mAnimStop){
			 mRecTemp.top=(rec.bottom-rec.height()*mCtrlOpt.mAnimRate);
			}
			g.drawRoundRect(mRecTemp, r, r, paint);
			g.restore();
			drawRectText(g, paint, mRecTemp, mType,data);
			
		}
		paint.setPathEffect(pathEffect);
		paint.setStrokeWidth(width);
		g.restore();
		g.save();
		g.clipRect(mRect);
	}

	private void drawRectText(Canvas g, Paint paint, RectF rect,ICharType type, ICharData data) {
		paint.setColor(mCurveTxtColor);
		String strVal=mCtrlOpt.mAnimStop?data.getValueStr(0):"Y"+StrUtils.formatF(data.getValue(0)*mCtrlOpt.mAnimRate, 2)+"W";
		g.drawText(strVal, rect.centerX(), rect.top
				- paint.descent()-3, paint); 
	 	if(mCtrlOpt.drawCoordTxt( g,type,data, rect, true)){
	 	}
	}

}
