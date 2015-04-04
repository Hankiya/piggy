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
import com.howbuy.lib.utils.ViewUtils;

/**
 * this class for draw curve ,and can be extends for custom draw
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */
public class Bar3D extends Curve {
	private final Rect mRecBounds=new Rect();
	private int mCurveTxtColor=0xff1a0f50;
	private CurveSetting mSetting=null;
	
	public Bar3D(CtrlOption ctrlOption, RectF rect,
			ArrayList<? extends ICharData> datas, ICharType charType) {
		super(ctrlOption, rect, datas, charType);
		mSetting=ctrlOption.getSetting();
	}
	public void draw(Canvas g, Paint paint) {
	
		paint.setColor(mType.getColor(0));
		paint.setStyle(Paint.Style.STROKE);
		mPath.reset();
		drawCurve(g, paint, mCtrlOpt.mSpace, mCtrlOpt.mOffsetRx);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
	}
	
	protected void drawRect(Canvas can, Paint paint,int type,int color,float dval){
		mPath.reset();
		 mRecBounds.set((int)mRecTemp.left, (int)mRecTemp.top,(int)mRecTemp.right,(int)mRecTemp. bottom);
	    if(type==0){
	     mPath.addRect(mRecTemp, Direction.CW);  
	    }else{
		    	mPath.moveTo(mRecBounds.left, mRecBounds.bottom);
		    	if(type==1||type==3){
		    	mPath.lineTo(mRecBounds.left, mRecBounds.top);
		    	mPath.lineTo(mRecBounds.right, mRecBounds.top-dval);
		    	mPath.lineTo(mRecBounds.right, mRecBounds.bottom-dval);
		    	}else if(type==2){
		    		mPath.lineTo(mRecBounds.left+dval, mRecBounds.top);
			    	mPath.lineTo(mRecBounds.right+dval, mRecBounds.top);
			    	mPath.lineTo(mRecBounds.right, mRecBounds.bottom);
		    	} 
	    }
	   mRecBounds.offsetTo(0, 0);
	   mCtrlOpt.drawPathShade(can,mPath, mRecTemp,mRecBounds, color, ViewUtils.color(color, 0.5f, 1));
	}
	
	protected void drawBar(Canvas g, Paint paint,RectF rec,int color){
		mRecTemp.set(rec);
		mRecTemp.bottom=mRecTemp.top;
		mRecTemp.top=mRecTemp.bottom- mSetting.getVdeep();
		drawRect(g, paint, 2, color, mSetting.getHdeep());
		
		mRecTemp.set(rec);
		mRecTemp.left=mRecTemp.right;
		mRecTemp.right=mRecTemp.left+mSetting.getHdeep();
		drawRect(g, paint, 3, color, mSetting.getVdeep());
		paint.setColor(color);
	    g.drawRect(rec, paint);
	}
	
	protected void drawCurve(Canvas g, Paint paint, float dx, float padR) {
		Path pa=new Path();
		mRecTemp.set(mRect);
		mRecTemp.top-=mSetting.getVdeep();
		mRecTemp.right+=mSetting.getHdeep();
		pa.addRect(mRecTemp, Direction.CW);
		final Rect rectf=mCtrlOpt.getRectFrame();
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
		for (int i = se[0]; i < se[1]; i++, x -= dx) {
			ICharData data = mData.get(i);
			rec.top = getYAt(data);
			rec.bottom = mRect.bottom;
			rec.left = x - padR;
			rec.right = x + padR;
			if(!mCtrlOpt.mAnimStop){
				rec.top=(rec.bottom-rec.height()*mCtrlOpt.mAnimRate);
			}
			drawBar(g,paint,rec,data.getColor(false) );
			mRecTemp.set(rec);
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
	    float y=rect.top - paint.descent()-mSetting.getVdeep()*0.2f;
		g.drawText(strVal, rect.centerX()+mSetting.getHdeep()/2,y , paint); 
	 	if(mCtrlOpt.drawCoordTxt( g,type,data, rect, true)){
	 	}
	}

}
