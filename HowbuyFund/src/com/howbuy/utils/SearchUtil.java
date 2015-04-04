package com.howbuy.utils;

import android.text.TextUtils;

public class SearchUtil {
	public static final String JustNumber = "^\\d+$";// 纯数字
	public static final String JustChar = "^[A-Za-z]+$";// 纯字母
	public static final String JustNumberAndChar = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+$";// 字母+数字(不能是字母，不能为数字，并且...)
	public static final String JustStrartChar = "^[A-Za-z]{1}\\d+$";// 字母开头+数字
	public static final String JustChinease = "^.*[\u4e00-\u9fa5]+.*$";// 中间包含汉字就可以

	public static String isNull(String queryString) {
		if (TextUtils.isEmpty(queryString)) {
			return null;
		}
		return queryString.toLowerCase();
	}

	public static final boolean isJustNumber(String queryString) {
		String qs = queryString;
		if (qs.matches(JustNumber)) {
			return true;
		}
		return false;
	};

	public static final boolean isJustChar(String queryString) {
		String qs = queryString;
		if (qs.matches(JustChar)) {
			return true;
		}
		return false;
	};

	public static final boolean isJustStartChar(String queryString) {
		String qs = queryString;
		if (qs.matches(JustStrartChar)) {
			return true;
		}
		return false;
	}

	public static final boolean isJustStartCharReverse(String queryString) {
		String qs = queryString;
		if (qs.matches(JustNumberAndChar)) {
			if (isJustStartChar(queryString) == false) {
				return true;
			}
		}
		return false;
	};

	public static final boolean isJustChinese(String queryString) {
		String qs = queryString;
		if (qs.matches(JustChinease)) {
			return true;
		}
		return false;
	};

	public static final StringBuilder transfomKeyWords(String queryString) {
		StringBuilder sb = new StringBuilder();
		return transfomKeyWords(queryString, sb);
	}

	private static final StringBuilder transfomKeyWords(String queryString, StringBuilder sb) {
		sb.append("'%");
		for (int i = 0; i < queryString.length(); i++) {
			sb.append(queryString.charAt(i)).append("%");
		}
		sb.append("'");
		return sb;
	}

	@Deprecated
	public static final String transformFuzzy(String queryString, String... colomn) {
		String tempStr;
		StringBuilder temp = new StringBuilder();
		temp = transfomKeyWords(queryString, temp);
		tempStr = temp.toString();

		// 重置
		temp = new StringBuilder();
		for (int i = 0; i < colomn.length; i++) {
			// if (i == 0) {
			// temp.append(" and");
			// } else {
			temp.append(" or");
			// }
			temp.append(" " + colomn[i]);
			temp.append(" like ");
			temp.append(tempStr);
		}

		return temp.toString();
	}

}
