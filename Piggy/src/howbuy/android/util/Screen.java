package howbuy.android.util;

import android.content.Context;

public class Screen {
	private Context context;

	public Screen(Context context) {
		this.context = context;
	}

	public int getWidth() {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public int getHeight() {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
