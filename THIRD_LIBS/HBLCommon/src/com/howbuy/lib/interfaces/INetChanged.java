package com.howbuy.lib.interfaces;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-2-28 下午1:37:50
 */
public interface INetChanged {
	public static final int NET_TYPE_NONE = 0;
	public static final int NET_TYPE_UNKNOW = 1;
	public static final int NET_TYPE_2G = 2;
	public static final int NET_TYPE_3G = 3;
	public static final int NET_TYPE_WIFI = 4;
	public boolean onNetChanged(int netType, int preType);
}
