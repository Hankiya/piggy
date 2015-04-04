package com.howbuy.lib.interfaces;

import android.view.View;
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-2-28 下午1:37:39
 */
public interface IAnimChaged {
	public static final int TYPE_START = 0;
	public static final int TYPE_CHANGED = 1;
	public static final int TYPE_END = 2;
	public void onAnimChaged(View v,final int type,final int which, final float val,final float dval);
}
