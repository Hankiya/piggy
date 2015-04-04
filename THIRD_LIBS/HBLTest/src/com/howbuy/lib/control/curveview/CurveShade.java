package com.howbuy.lib.control.curveview;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

import com.howbuy.lib.control.curveview.CurveControl.CtrlOption;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.lib.interfaces.ICharType;

/**
 * this class for draw curve ,and can be extends for custom draw
 * @author rexy 840094530@qq.com
 * @date 2013-11-25 下午4:24:41
 */
public class CurveShade extends BezierShadeLine {
	private int mColorShapeStart = 0xaaff0000;
	private int mColorShapeEnd = 0xaaffffff;

	public CurveShade(CtrlOption ctrlOption, RectF rect,
			ArrayList<? extends ICharData> datas, ICharType charType) {
		super(ctrlOption, rect, datas, charType);
	}

	protected void drawPathShade(Canvas g, Paint paint, float lx,
			float rx) { 
		//mPath.lineTo(lx, mRect.bottom - 1);
		//mPath.lineTo(rx, mRect.bottom - 1);
		//mPath.close();
		ShapeDrawable shaper = new ShapeDrawable(new PathShape(mPath,
				mRect.width(), mRect.height()));
		LinearGradient linear = new LinearGradient(0, mRect.top, 0,
				mRect.bottom, mColorShapeStart, mColorShapeEnd, TileMode.CLAMP);
		((ShapeDrawable) shaper).getPaint().setShader(linear);
		((ShapeDrawable) shaper).getPaint().setDither(true);
		shaper.setBounds(0, 0, (int) mRect.width(), (int) mRect.height());
		shaper.draw(g);
	}
	public void setShapeColor(int colorStart,int colorEnd){
		mColorShapeStart=colorStart;
		mColorShapeEnd=colorEnd;
	}

}
