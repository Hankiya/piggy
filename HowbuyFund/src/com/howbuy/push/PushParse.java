package com.howbuy.push;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 获取操作
 * 
 * @author yescpu
 * 
 */
final class PushParse {
	public enum JpushType {
		News, Opinion, Interview, Trust, Trade, Fund, CommonWap, Update, Other
	}

	public static final JpushType getPushType(String actionMsg) {
		if (TextUtils.isEmpty(actionMsg)) {
			return null;
		}
		if (actionMsg.equals("N")) {
			return JpushType.News;
		} else if (actionMsg.equals("O")) {
			return JpushType.Opinion;
		} else if (actionMsg.equals("I")) {
			return JpushType.Interview;
		} else if (actionMsg.equals("FI")) {
			return JpushType.Trust;
		} else if (actionMsg.equals("T")) {
			return JpushType.Trade;
		} else if (actionMsg.equals("F")) {
			return JpushType.Fund;
		} else if (actionMsg.equals("C")) {
			return JpushType.CommonWap;
		} else if (actionMsg.equals("U")) {
			return JpushType.Update;
		} else {
			return JpushType.Other;
		}

	}

	/**
	 * 获取code
	 * 
	 * @param value
	 * @return
	 */
	public static final String getPushCode(String actionMsg) {
		String pattern = "T=\\((.+?)\\)";
		String res = doRegst(actionMsg, pattern);
		return res;
	}

	/**
	 * 获取值
	 * 
	 * @param value
	 * @return
	 */
	public static final String getPushValue(String actionMsg) {
		String pattern = "V=\\((.+?)\\)";
		String res = doRegst(actionMsg, pattern);
		return res;
	}

	/**
	 * 扩展值
	 * 
	 * @param extra
	 * @return
	 */
	public static final String getPushExtra(String actionMsg) {
		String pattern = "E=\\((.+?)\\)";
		String res = doRegst(actionMsg, pattern);
		return res;
	}

	/**
	 * 正则表达式匹配
	 * 
	 * @param acionMsg
	 * @param pattern
	 * @return
	 */
	public static final String doRegst(String acionMsg, String pattern) {
		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(acionMsg);
		String result = "";
		while (matcher.find()) {
			System.out.println(matcher.groupCount());
			if (matcher.groupCount() == 1) {
				result = matcher.group(1);// 只取第一组
				return result;
			}
		}
		return null;
	}

}
