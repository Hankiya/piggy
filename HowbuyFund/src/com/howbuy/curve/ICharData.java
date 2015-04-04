package com.howbuy.curve;

/**
 * @author rexy 840094530@qq.com
 * @date 2013-12-15 下午3:01:42
 */
public interface ICharData {
	public Object getExtras(int type);

	public int getColor(boolean selected);

	public float getValue(int type);

	public long getTime();

	public String getValueStr(int type);

	public String getTime(String format);
}