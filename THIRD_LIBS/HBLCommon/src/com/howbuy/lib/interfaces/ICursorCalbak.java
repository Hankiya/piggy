package com.howbuy.lib.interfaces;

import com.howbuy.lib.error.WrapException;

import android.database.Cursor;
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-2-28 下午1:37:45
 */
public interface ICursorCalbak {
	void getCursor(String key, Cursor cursor, WrapException e);
}