package com.howbuy.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DashedLine extends View {
	private Paint paint = null;
	private Path path = null;
	private PathEffect pe = null;
	private float mDensity = 1;

	public DashedLine(Context paramContext) {
		this(paramContext, null);
	}

	public DashedLine(Context paramContext, AttributeSet atr) {
		super(paramContext, atr);
		mDensity = getResources().getDisplayMetrics().density;
		this.paint = new Paint();
		this.path = new Path();
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setColor(0xffdddddd);
		this.paint.setAntiAlias(true);
		this.paint.setStrokeWidth(mDensity * 2f);
		float[] arrayOfFloat = new float[] { mDensity * 2f, mDensity * 2f, mDensity * 2f,
				mDensity * 2f };
		this.pe = new DashPathEffect(arrayOfFloat, mDensity * 1f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.path.moveTo(0.0F, 0.0F);
		this.path.lineTo(getMeasuredWidth(), 0.0F);
		this.paint.setPathEffect(this.pe);
		canvas.drawPath(this.path, this.paint);
	}

}