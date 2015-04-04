package howbuy.android.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;

/**
 * 时间处理类
 * 
 * @author lixing.tang
 * 
 */
public class DateDuffUtils extends DateUtils {
	private static DateDuffUtils instance;
	private static String mTimestampLabelHoursAgo;
	private static String mTimestampLabelJustNow;
	private static String mTimestampLabelMinuteAgo;
	private static String mTimestampLabelToday;
	private static String mTimestampLabelYesterday;
	public static final long millisInADay = 86400000L;
	public static String[] weekdays = new DateFormatSymbols().getWeekdays();
	public static Calendar calendar = Calendar.getInstance();

	public static DateDuffUtils getInstance(Context paramContext) {
		if (instance == null) {
			instance = new DateDuffUtils();
			mTimestampLabelYesterday = "昨天";// 昨天
			mTimestampLabelToday = "今天";//
			mTimestampLabelJustNow = "刚刚";// 刚刚
			mTimestampLabelMinuteAgo = "分钟前";// 几分钟
			mTimestampLabelHoursAgo = "小时前";// 几小时
		}
		return instance;
	}

	public static boolean isYesterday(long paramLong) {
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String olad = format.format(today);
		String oladEd = format.format(new Date(paramLong * 1000));
		return olad.equals(oladEd);
	}

	public static int dayForWeek(Long paramLong) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(paramLong);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static boolean isLastWake(Long paramLong) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.WEDNESDAY, calendar.get(Calendar.WEDNESDAY) - 1);
		if (isSameWeekDates(calendar.getTimeInMillis(), paramLong)) {// 同一周
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSameYear(Long paramLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String olad = format.format(new Date(paramLong));
		String curr = format.format(new Date(System.currentTimeMillis()));
		return olad.equals(curr);
	}

	public static boolean isSameWeekDates(Long date1, Long date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(new Date(date1));
		cal2.setTime(new Date(date2));
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	public String getTimeDiffString(long paramLong) {
		Calendar localCalendar1 = Calendar.getInstance();
		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.setTimeInMillis(paramLong);
		long l1 = localCalendar1.getTimeInMillis() - localCalendar2.getTimeInMillis();
		// (l1 / 1000L);
		boolean bool1 = isToday(paramLong);
		boolean bool2 = isYesterday(paramLong);

		if (bool1) {// 今天
			if (l1 < 60 * 1000) {// 小于一分钟 刚刚
				return mTimestampLabelJustNow;
			} else if (l1 < 60 * 1000 * 60) {// 小于60分钟 几分钟前
				return (l1 / (60 * 1000)) + mTimestampLabelMinuteAgo;
			} else if (l1 > 60 * 1000 * 60) {// 几个小时以前
				return l1 / (60 * 1000 * 60) + mTimestampLabelHoursAgo;
			} else {
				return mTimestampLabelToday;// 今天
			}
		}
		// else if (bool2) {// 昨天
		// return "Created on" + mTimestampLabelYesterday;
		// } else if (isSameWeekDates(System.currentTimeMillis(), paramLong)) {
		// return "Created on" + weekdays[dayForWeek(paramLong)];
		// } else if (isLastWake(paramLong)) {// 上周
		// return "Created on " + "last " + weekdays[dayForWeek(paramLong)];
		// }
		else {
			SimpleDateFormat format = new SimpleDateFormat("MM-dd");
			return format.format(new Date(paramLong));
		}
	}
}