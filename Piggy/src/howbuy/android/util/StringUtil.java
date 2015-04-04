package howbuy.android.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;

import com.google.myjson.Gson;

public class StringUtil {
	public static final String floatFormatFureStr = "0.0000";
	public static final String floatFormatTwoStr = "0.00";
	public static final String floatFormatOneStr = "0.0";
	public static final String floatFormatTwoOneStr = "00.00";
	public static final String floatFormatBaiFen = "###,##0.00";
	public static final String floatFormatSaveOrOut = "###,###.##";

	/**
	 * 把一个String 类型的数据格式成浮点数数据格式化浮点型数据
	 * 
	 * @param a
	 * @param formatStr
	 * @return
	 */
	public static String getFloat(float a, String formatStr) {
		DecimalFormat df = new DecimalFormat(formatStr);
		return df.format(a);
	}

	/**
	 * 传入一个输入流返回一个此流的字符串形式
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream in) throws IOException {
		// 容易出现乱码
		// StringBuffer out = new StringBuffer();
		// byte[] b = new byte[2048];
		// for (int n; (n = in.read(b)) != -1;) {
		// out.append(new String(b, 0, n));
		// }
		// return out.toString();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = in.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	/**
	 * 把一个字符串转换成Base64位
	 * 
	 * @param s
	 * @return
	 */
	public static String getBASE64(String s) {
		byte[] b;
		if (s == null) {
			return null;
		} else {
			b = Base64.encode(s.getBytes(), Base64.DEFAULT);
			return new String(b);
		}
	}

	/**
	 * 
	 * 格式化净值
	 * 
	 * @param s
	 * @return
	 */
	public static String getNetWorth(String s, Boolean isSimu, Boolean isYiBai) {
		String networthValue = s;
		if (networthValue != null && networthValue.equals("") == false) {
			float netWorthFloat = Float.parseFloat(networthValue);
			if (isSimu) {
				if (isYiBai != null && isYiBai) {
					netWorthFloat = netWorthFloat / 100;
				}
			}
			networthValue = getFloat(netWorthFloat, StringUtil.floatFormatFureStr);
			if (isYiBai == true) {
				networthValue = networthValue + "*";
			}
		} else {
			networthValue = "--";
		}
		return networthValue;
	}

	/**
	 * 格式化利率
	 * 
	 * @param s
	 * @return
	 */
	public static String getBaiFenBi(String s, TextView textView, Boolean isSimu) {
		String hbdrValue = s;
		if (hbdrValue != null && hbdrValue.equals("") == false) {
			if ("-9999".contains(hbdrValue)) {
				hbdrValue = "--";
				textView.setTextColor(Color.GRAY);
			} else {
				float hbdrFloat = Float.parseFloat(hbdrValue);
				if (isSimu) {
					hbdrFloat *= 100f;
				}
				hbdrValue = StringUtil.getFloat(hbdrFloat, StringUtil.floatFormatTwoStr) + "%";
				if (hbdrValue.contains("-")) {
					textView.setTextColor(Color.rgb(55, 149, 54));
				} else if (hbdrValue.equals("0.00%")) {
					textView.setTextColor(Color.BLACK);
				} else {
					textView.setTextColor(Color.rgb(207, 2, 17));
				}
			}
		} else {
			hbdrValue = "--";
			textView.setTextColor(Color.GRAY);
		}
		textView.setText(hbdrValue);
		return hbdrValue;
	}

	/**
	 * 格式化利率
	 * 
	 * @param s
	 * @return
	 */
	public static String getBaiFenBi1(String s, TextView textView, Boolean isSimu) {
		String hbdrValue = s;
		if (hbdrValue != null && hbdrValue.equals("") == false) {
			if ("-9999".contains(hbdrValue)) {
				hbdrValue = "--";
				textView.setTextColor(Color.GRAY);
			} else {
				float hbdrFloat = Float.parseFloat(hbdrValue);
				if (isSimu) {
					hbdrFloat = hbdrFloat / 100f;
				}
				hbdrValue = StringUtil.getFloat(hbdrFloat, StringUtil.floatFormatTwoStr) + "%";
				if (hbdrValue.contains("-")) {
					textView.setTextColor(Color.rgb(55, 149, 54));
				} else if (hbdrValue.equals("0.00%")) {
					textView.setTextColor(Color.GRAY);
				} else {
					textView.setTextColor(Color.rgb(207, 2, 17));
				}
			}
		} else {
			hbdrValue = "--";
			textView.setTextColor(Color.GRAY);
		}
		textView.setText(hbdrValue);
		return hbdrValue;
	}

	/**
	 * 判断一个字符是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();

	}

	/**
	 * 判断是否为汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBb2312(String str) {
		String march = "^[\u4e00-\u9fa5]+$";
		return str.matches(march);
		// char[] chars = str.toCharArray();
		// boolean isGB2312 = false;
		// for (int i = 0; i < chars.length; i++) {
		// byte[] bytes = ("" + chars[i]).getBytes();
		// if (bytes.length == 2) {
		// int[] ints = new int[2];
		// ints[0] = bytes[0] & 0xff;
		// ints[1] = bytes[1] & 0xff;
		// if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1]
		// <= 0xFE) {
		// isGB2312 = true;
		// break;
		// }
		// }
		// }
		// return isGB2312;
	}

	/**
	 * 去除字符串的前后空格
	 * 
	 * @param vlaue
	 * @return
	 */
	public static String noStartEndSpace(String vlaue) {
		String a = vlaue.trim();
		while (a.startsWith(" ")) {
			a = a.substring(1, a.length()).trim();
		}
		while (a.endsWith(" ")) {
			a = a.substring(0, a.length() - 1).trim();
		}
		return a;
	}

	public static String toGson(Object object) {
		Gson sGson = new Gson();
		return sGson.toJson(object);
	}

	/**
	 * 格式化float
	 * 
	 * @param value
	 * @param pattern
	 * @return
	 */
	public static String formatFloat(float value, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		String sdf = df.format(value, new StringBuffer(), new FieldPosition(0)).toString();
		return sdf;
	}

	/**
	 * 格式化货币
	 * 
	 * @param _currency
	 * @return
	 */
	public static String formatCurrency(String _currency) {
		if (TextUtils.isEmpty(_currency)) {
			return null;
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(_currency);
		StringBuffer buff = new StringBuffer();
		buff.append("###,##0.00");
		formater = new DecimalFormat(buff.toString());
		return formater.format(num);
	}
	
	public static String formatNum(String _currency,String format) {
		if (TextUtils.isEmpty(_currency)) {
			return null;
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(_currency);
		formater = new DecimalFormat(format);
		return formater.format(num);
	}

	/**fromatAll
	 * 
	 * @param number
	 * @param format
	 * @return
	 */
	public static String formatAll(String number, String format) {
		String res = "";
		try {
			double num = Double.parseDouble(number);
			NumberFormat formater = new DecimalFormat(format);
			res = formater.format(num);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 收益百分比
	 * 
	 * @param _xiaoShu
	 * @return
	 */
	public static String formatBaiFen(String _xiaoShu) {
		if (TextUtils.isEmpty(_xiaoShu)) {
			return "";
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(_xiaoShu);
		StringBuffer buff = new StringBuffer();
		buff.append("###,##0.00");
		formater = new DecimalFormat(buff.toString());
		return formater.format(num);
	}

	/**
	 * 格式化银行卡 （显示）
	 * 
	 * @param card
	 * @return
	 */
	public static String formatViewBankCard(String card) {

		if (TextUtils.isEmpty(card) || card.equals("--")) {
			return "--";
		}

		int length = card.length();
		card = card.substring(length - 4, length);
		card = "尾号" + card;

		return card;
	}

	/**
	 * 格式化银行卡 （输入）
	 * 
	 * @param card
	 * @return
	 */
	public static String formatInputBankCard(String card) {
		int spilitMax = 4;
		if (TextUtils.isEmpty(card)) {
			return "";
		}
		card = card.replaceAll(" ", "");
		int length = card.length();
		if (length <= spilitMax) {
			return card;
		}
		int fourL = length / spilitMax;

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fourL; i++) {
			String t = card.substring(i * spilitMax, (i + 1) * spilitMax);
			sb.append(t).append(" ");
		}

		int end = fourL * spilitMax;
		if (end < length) {
			String endV = card.substring(end);
			sb.append(endV);
		} else {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * 格式化默认输入的身份证号
	 * 
	 * @param card
	 * @return
	 */
	public static String formatExitIdCard(String card) {
		int spilitMax = 4;
		if (TextUtils.isEmpty(card)) {
			return "";
		}
		card = card.replaceAll(" ", "");
		int length = card.length();
		int middle;
		if (length <= spilitMax * 2) {
			return card;
		}

		middle = length - spilitMax * 2 + 1;
		String start = card.substring(0, spilitMax - 1);
		String end = card.substring(length - 4);
		StringBuilder sb = new StringBuilder(start);
		for (int i = 0; i < middle; i++) {
			sb.append("*");
		}
		sb.append(end);
		return sb.toString();
	}

}
