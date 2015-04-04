package howbuy.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 * 
 * @author yescpu
 * 
 */
public class TimestampUtil {

	public static final String FORMAT = "yyyyMMdd HH:mm:ss";
	public static final String FORMATHeng = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMATNoS = "yyyy-MM-dd";
	public static final String FORMATNoChinase = "yyyy年MM月dd日";
	public static final String FORMATUPLOAD = "yyyyMMdd";
	public static final String FORMATTwoDay = "MM-dd";
	public static final String FORMATUPLOADHeng = "yyyy-MM-dd";

	/**
	 * 输入一种日期格式，返回一种日期格式
	 * 
	 * @param dateStr
	 * @param formatIn
	 * @param formatOut
	 * @return
	 * @throws Exception
	 */
	public static String getChangeHeng(String dateStr, String formatIn, String formatOut) throws Exception {
		try {
			SimpleDateFormat dfIn = new SimpleDateFormat(formatIn);
			SimpleDateFormat dfOut = new SimpleDateFormat(formatOut);
			return dfOut.format(dfIn.parse(dateStr));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * // 将DATE转为时间戳
	 * 
	 * @param user_time
	 * @return
	 */

	public static String getTimeStamp(String user_time, String formatStr) {
		String re_time = null;
		SimpleDateFormat sdf;
		sdf = new SimpleDateFormat(formatStr);
		Date d;
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, str.length());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * // 将时间戳转为DATE类型
	 * 
	 * @param cc_time
	 * @return
	 */
	public static String getTimeYyMmDd(String cc_time, String formatStr) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);// 时mm分ss秒
		if (cc_time != null && cc_time.equals("") == false) {
			long lcc_time = Long.valueOf(cc_time);
			re_StrTime = sdf.format(new Date(lcc_time));
			return re_StrTime;
		} else {
			return "--";
		}
	}

	/**
	 * 获取半年前日期
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getBeforHalfYear(String formatStr) {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.MONTH, -6); // 年份减1
		Date lastMonth = ca.getTime(); // 结果
		return getTimeYyMmDd(String.valueOf(lastMonth.getTime()), formatStr);
	}

	/**
	 * 获取一年前的今天的时间
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getBeforYear(String formatStr) {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.YEAR, -1); // 年份减1
		Date lastMonth = ca.getTime(); // 结果
		return getTimeYyMmDd(String.valueOf(lastMonth.getTime()), formatStr);
	}

	/**
	 * 获取一年前的今天的时间
	 * 
	 * @param formatStr
	 * @return
	 */
	public static String getBeforThreeYear(String formatStr) {
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(new Date()); // 设置时间为当前时间
		ca.add(Calendar.YEAR, -3); // 年份减1
		Date lastMonth = ca.getTime(); // 结果
		return getTimeYyMmDd(String.valueOf(lastMonth.getTime()), formatStr);
	}

	/**
	 * F10根据日期获取2011第几季度
	 * 
	 * @param date
	 * @return 季度
	 */
	public static String getJiDu(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATUPLOAD);
		Date dateTrue = null;
		String year = null;
		String jiDu = null;
		try {
			dateTrue = sdf.parse(date);
			year = String.valueOf(dateTrue.getYear() + 1900);
			int a = dateTrue.getMonth();
			if (a > 0 && a < 4) {
				jiDu = "一";
			} else if (a > 3 && a < 7) {
				jiDu = "二";
			} else if (a > 6 && a < 10) {
				jiDu = "三";
			} else {
				jiDu = "四";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dateTrue != null && year != null && jiDu != null) {
			return year + "年第" + jiDu + "季度";
		} else {
			return null;
		}
	}

}