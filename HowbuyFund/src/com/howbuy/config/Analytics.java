package com.howbuy.config;

import java.util.HashMap;

import android.content.Context;

import com.howbuy.lib.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

public class Analytics {
	private static String TAG = "Analytics";
	public static final String REXIAO_CLICK = "REXIAO_CLICK";
	public static final String TUIJIAN_CLICK = "TUIJIAN_CLICK";
	public static final String CXG_CLICK = "CXG_CLICK";
	public static final String SMJX_CLICK = "SMJX_CLICK";
	public static final String ADD_CUSTOM_FUNDS = "ADD_CUSTOM_FUNDS";
	public static final String DELETE_CUSTOM_FUNDS = "DELETE_CUSTOM_FUNDS";
	public static final String EDIT_CUSTOM_FUNDS = "EDIT_CUSTOM_FUNDS";
	public static final String CUSTOM_COUNT = "CUSTOM_COUNT";
	public static final String VIEW_ORDER_PART = "VIEW_ORDER_PART";
	public static final String SWITCH_CLASSIFY_OPTION = "SWITCH_CLASSIFY_OPTION";
	public static final String VIEW_FUND_DETAIL = "VIEW_FUND_DETAIL";
	public static final String SWITCH_FONT_SIZE = "SWITCH_FONT_SIZE";
	public static final String ADD_FAVORITE_NEWS = "ADD_FAVORITE_NEWS";
	public static final String DELETE_FAVORITE_NEWS = "DELETE_FAVORITE_NEWS";
	public static final String VIEW_NEWS_INFO = "VIEW_NEWS_INFO";
	public static final String TRADE_LOGIN_SUCCEED = "TRADE_LOGIN_SUCCEED";
	public static final String LOAD_NEW_TRADE_PAGE = "LOAD_NEW_TRADE_PAGE";
	public static final String SHARE_INFO = "SHARE_INFO";

	public static final String VIEW_HORIZONTAL_CHART_VIEW = "VIEW_HORIZONTAL_CHART_VIEW";
	public static final String VIEW_VERTICAL_CHART_VIEW = "VIEW_VERTICAL_CHART_VIEW";
	public static final String ONE_FINGER_TOUCH_EVENT = "ONE_FINGER_TOUCH_EVENT";
	public static final String TWO_FINGER_TOUCH_EVENT = "TWO_FINGER_TOUCH_EVENT";
	public static final String FUND_PERFORMANCE_EXPANDED = "FUND_PERFORMANCE_EXPANDED";
	public static final String CLICK_BUY_BUTTON = "CLICK_BUY_BUTTON";
	public static final String MAKE_APPOINTMENT = "MAKE_APPOINTMENT";
	public static final String ACTIVE_APP_BY_LOGINED_USER = "ACTIVE_APP_BY_LOGINED_USER";
	public static final String SMS_SUBSCRIPTION = "SMS_SUBSCRIPTION";
	public static final String CALL_400 = "CALL_400";
	public static final String MORE_APP = "MORE_APP";
	// ///////////////////////////////////////////////////////////////////////////////
	public static final String KEY_ORDER = "order";
	public static final String KEY_FROM = "from";
	public static final String KEY_ACTION = "action";
	public static final String KEY_TYPE = "type";
	public static final String KEY_COUNT = "count";
	public static final String KEY_PART = "part";
	public static final String KEY_SWITCH_NAME = "switch_name";
	public static final String KEY_CHANNEL = "channel";
	public static final String KEY_FOR = "for";

	public static final void onEvent(Context cx, String eventId, HashMap<String, String> map) {
		MobclickAgent.onEvent(cx, eventId, map);
		LogUtils.d(TAG, "onEvent-->" + eventId + ": " + map);
	}

	/*
	 * map_key_value 一定是2的倍数，且满足kay,value的迭代。
	 */
	public static final void onEvent(Context cx, String eventId, String... map_key_value) {
		int n = map_key_value == null ? 0 : map_key_value.length;
		if (n == 0) {
			MobclickAgent.onEvent(cx, eventId);
			LogUtils.d(TAG, "onEvent-->" + eventId);
		} else {
			HashMap<String, String> map = new HashMap<String, String>(n / 2 + 1);
			for (int i = 0; i < n; i += 2) {
				map.put(map_key_value[i], map_key_value[i + 1]);
			}
			onEvent(cx, eventId, map);
		}
	}

	public static final void onPageStart(String title) {
		MobclickAgent.onPageStart(title);
		LogUtils.d(TAG, "onPageStart-->title=" + title);
	}

	public static final void onPageEnd(String title) {
		MobclickAgent.onPageEnd(title);
		LogUtils.d(TAG, "onPageEnd-->title=" + title);
	}

	public static final void onResume(Context cx) {
		MobclickAgent.onResume(cx);
		LogUtils.d(TAG, "onResume-->aty=" + cx.getClass().getSimpleName());
	}

	public static final void onPause(Context cx) {
		MobclickAgent.onPause(cx);
		LogUtils.d(TAG, "onPause-->aty=" + cx.getClass().getSimpleName());
	}

}
