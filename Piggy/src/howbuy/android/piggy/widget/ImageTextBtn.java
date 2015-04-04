package howbuy.android.piggy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class ImageTextBtn extends Button {

	public ImageTextBtn(Context context) {
		super(context);
	}

	public ImageTextBtn(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
//		setTextColor(Color.WHITE);
	}

	public ImageTextBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		/*setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.imgBtn_padding));
		String text=" "+getText();
		setText(text);*/
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		/*Drawable[] drawables = getCompoundDrawables();
		if (drawables != null) {
			Drawable drawableLeft = drawables[0];
			if (drawableLeft != null) {
				float textWidth = getPaint().measureText(getText().toString())+new Screen(getContext()).dip2px(20);
				int drawablePadding = getCompoundDrawablePadding();
				int drawableWidth =  drawableLeft.getIntrinsicWidth();
				float bodyWidth = textWidth + drawableWidth + drawablePadding;
				canvas.translate((getWidth() - bodyWidth) / 2, 0);
			}
		}*/
		super.onDraw(canvas);
	}

}
