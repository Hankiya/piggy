package howbuy.android.piggy.widget;

import howbuy.android.piggy.help.AdHelp.ScrollToCallback;
import howbuy.android.util.AndroidUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageControlView extends LinearLayout implements ScrollToCallback {
	private int count;
	private Context context;
	private Bitmap select,unSelect;
	
	
	private void initSelectBitmap(){
		int width=AndroidUtil.dip2px(7);
		select=Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
		unSelect=Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
		Canvas canvas1=new Canvas(select);
		Canvas canvas2=new Canvas(unSelect);
		Paint p1=new Paint();
		p1.setAntiAlias(true);
		p1.setColor(0xffffffff);
		Paint p2=new Paint(p1);
		p2.setAlpha(122);
		canvas1.drawCircle(width/2f, width/2f, width/2f, p1);
		canvas2.drawCircle(width/2f, width/2f, width/2f, p2);
	}

	public void setCount(int count) {
		removeAllViews();
		this.count = count;
		initSelectBitmap();
		LinearLayout.LayoutParams lParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lParams.setMargins(AndroidUtil.dip2px(15), 0, 0, 0);
		for (int i = 0; i < this.count; i++) {
			ImageView imageView = new ImageView(context);
			if (i == 0) {
				imageView.setImageBitmap(select);
			} else {
				imageView.setImageBitmap(unSelect);
			}
			this.addView(imageView,lParams);
		}
	}

	public PageControlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	public PageControlView(Context context) {
		super(context);
		this.init(context);
	}

	private void init(Context context) {
		this.context = context;
		setGravity(Gravity.CENTER_HORIZONTAL);
		setPadding(0, 10, 0, 10);
	}

	@Override
	public void callback(int currentIndex) {
		doPageControl(currentIndex);
	}

	public void doPageControl(int currentIndex) {
		for (int i = 0; i < this.count; i++) {
			ImageView mImageView = (ImageView) this.getChildAt(i);
			if (currentIndex == i) {
				mImageView.setImageBitmap(select);
			} else {
				mImageView.setImageBitmap(unSelect);
			}
		}
	}
}
