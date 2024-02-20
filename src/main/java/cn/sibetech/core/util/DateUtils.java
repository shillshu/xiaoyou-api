package cn.sibetech.core.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 
 * 日期,时间工具类
 */
public class DateUtils {
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	public final static String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Get the previous time, from how many days to now.
	 * 
	 * @param days
	 *            How many days.
	 * @return The new previous time.
	 */
	public static Date previous(int days) {
		return new Date(System.currentTimeMillis() - days * 3600000L * 24L);
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(Date d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(long d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDate(String d) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(d);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm".
	 */
	public static Date parseDateTime(String dt) {
		try {
			return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}
	
	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm:ss".
	 */
	public static Date parseDateTimePatten(String dt) {
		if(StringUtils.isEmpty(dt)){
			return null;
		}
		try {
			return new SimpleDateFormat(DEFAILT_DATE_TIME_PATTERN).parse(dt);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/** 日期 */
	public final static String DEFAILT_DATE_PATTERN = "yyyy-MM-dd";
	/** 日期时间 */
	public final static String DEFAILT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public final static String DEFAILT_DATE_TIME2_PATTERN = "yyyy-MM-dd HH:mm";
	/** 时间 */
	public final static String DEFAULT_TIME_PATTERN = "HH:mm";
	/**
	 * 每天的毫秒数
	 */
	public final static long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

	/**
	 * 转换日期字符串得到指定格式的日期类型
	 * 
	 * @param formatString
	 *            需要转换的格式字符串
	 * @param targetDate
	 *            需要转换的时间
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertString2Date(String formatString,
												String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate))
			return null;
		SimpleDateFormat format = null;
		Date result = null;
		format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	public static final Date convertString2Date(String[] formatString,
												String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate)) {
			return null;
		}
		SimpleDateFormat format = null;
		Date result = null;
		String errorMessage = null;
		Integer errorOffset = null;
		for (String dateFormat : formatString) {
			try {
				format = new SimpleDateFormat(dateFormat);
				result = format.parse(targetDate);
			} catch (ParseException pe) {
				result = null;
				errorMessage = pe.getMessage();
				errorOffset = pe.getErrorOffset();
			} finally {
				if (result != null && result.getTime() > 1) {
					break;
				}
			}
		}
		if (result == null) {
			throw new ParseException(errorMessage, errorOffset);
		}
		return result;
	}

	/**
	 * 转换字符串得到默认格式的日期类型
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertString2Date(String strDate) throws ParseException {
		Date result = null;
		try {
			result = convertString2Date(DEFAILT_DATE_PATTERN, strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}

	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2String(Date targetDate) {
		return convertDate2String(DEFAILT_DATE_PATTERN, targetDate);
	}
	
	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDateTime2String(Date targetDate) {
		return convertDate2String(DEFAULT_TIME_PATTERN, targetDate);
	}

	/**
	 * 比较日期大小
	 * 
	 * @param src
	 * @param src
	 * @return int; 1:DATE1>DATE2;
	 */
	public static int compare_date(Date src, Date src1) {

		String date1 = convertDate2String(DEFAILT_DATE_TIME_PATTERN, src);
		String date2 = convertDate2String(DEFAILT_DATE_TIME_PATTERN, src1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return 0;
	}

	/**
	 * 日期比较
	 * 
	 * 判断时间date1是否在时间date2之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 日期比较
	 * 
	 * 判断当前时间是否在时间date2之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @param date2
	 * @return
	 */
	public static boolean isDateBefore(String date2) {
		if (date2 == null) {
			return false;
		}
		try {
			Date date1 = new Date();
			DateFormat df = getDateFormat(date2);
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			return false;
		}
	}
	
	
	/**
	 * 日期比较
	 * 
	 * 判断当前时间是否在时间date1 date2之间 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @return
	 */
	public static String isInDate(Date date, String strDateBegin,  
	        String strDateEnd) {  
		
		String flag = "";
		try {
			Date begin = convertString2Date(DEFAILT_DATE_TIME_PATTERN,strDateBegin);
			Date end = convertString2Date(DEFAILT_DATE_TIME_PATTERN,strDateEnd);
			if(date.before(begin)){
				flag = "0";
			}
			if(date.after(end)){
				flag = "2";
			}
			if(date.after(begin) && date.before(end)){
				flag = "1";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}  

	/**
	 * 比较当前时间与时间date2的天相等 时间格式 2008-11-25 16:30:10 如:当前时间是2008-11-25
	 * 16:30:10与传入时间2008-11-25 15:31:20 相比较,返回true即相等
	 * 
	 * @param date2
	 * @return boolean; true:相等
	 * @author zhangjl
	 */
	public static boolean equalDate(String date2) {
		try {
			String date1 = convertDate2String(DEFAILT_DATE_TIME_PATTERN,
					new Date());
			date1.equals(date2);
			Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
			Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);
			return d1.equals(d2);
		} catch (ParseException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * 比较时间date1与时间date2的天相等 时间格式 2008-11-25 16:30:10
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:相等
	 * @author zhangjl
	 */
	public static boolean equalDate(String date1, String date2) {
		try {

			Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
			Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);

			return d1.equals(d2);
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 比较时间date1是否在时间date2之前 时间格式 2008-11-25 16:30:10
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:在date2之前
	 * @author 胡建国
	 */
	public static boolean beforeDate(String date1, String date2) {
		try {
			Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
			Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);
			return d1.before(d2);
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 获取上个月开始时间
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return 上个月的第一天
	 */
	public static Date getBoferBeginDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), (currentDate
				.get(Calendar.MONTH)) - 1, result
				.getActualMinimum(Calendar.DATE), 0, 0, 0);
		return result.getTime();
	}

	/**
	 * 获取指定月份的第一天
	 * 
	 * @param currentDate
	 * @return
	 */
	public static Date getBeginDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), (currentDate
				.get(Calendar.MONTH)), result.getActualMinimum(Calendar.DATE));
		return result.getTime();
	}


	/**
	 * 获取上个月结束时间
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return 上个月最后一天
	 */
	public static Date getBoferEndDate(Calendar currentDate) {
		Calendar result = currentDate;
		result.set(Calendar.DATE, 1);
		result.add(Calendar.DATE, -1);
		return result.getTime();
	}

	/**
	 * 获取两个时间的时间间隔
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getDaysBetween(Calendar beginDate, Calendar endDate) {
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int days = endDate.get(Calendar.DAY_OF_YEAR)
				- beginDate.get(Calendar.DAY_OF_YEAR) + 1;
		int year = endDate.get(Calendar.YEAR);
		if (beginDate.get(Calendar.YEAR) != year) {
			beginDate = (Calendar) beginDate.clone();
			do {
				days += beginDate.getActualMaximum(Calendar.DAY_OF_YEAR);
				beginDate.add(Calendar.YEAR, 1);
			} while (beginDate.get(Calendar.YEAR) != year);
		}
		return days;
	}

	/**
	 * 获取两个时间的时间间隔(月份)
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getMonthsBetween(Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			Date swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int months = endDate.getMonth() - beginDate.getMonth();
		int years = endDate.getYear() - beginDate.getYear();

		months += years * 12;

		return months;
	}

	/**
	 * 获取两个时间内的工作日
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getWorkingDay(Calendar beginDate, Calendar endDate) {
		int result = -1;
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int charge_start_date = 0;
		int charge_end_date = 0;
		int stmp;
		int etmp;
		stmp = 7 - beginDate.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - endDate.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {
			charge_end_date = etmp - 1;
		}
		result = (getDaysBetween(getNextMonday(beginDate),
				getNextMonday(endDate)) / 7)
				* 5 + charge_start_date - charge_end_date;
		return result;
	}

	/**
	 * 根据当前给定的日期获取当前天是星期几(中国版的)
	 * 
	 * @param date
	 *            任意时间
	 * @return
	 */
	public static String getChineseWeek(Calendar date) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];

	}

	public static String dateToWeek(String datetime) {

		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		Date date;
		try {
			date = f.parse(datetime);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//一周的第几天
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 *            任意时间
	 * @return
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * 获取两个日期之间的休息时间
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getHolidays(Calendar beginDate, Calendar endDate) {
		return getDaysBetween(beginDate, endDate)
				- getWorkingDay(beginDate, endDate);

	}

	public static boolean isDateEnable(Date beginDate, Date endDate,
									   Date currentDate) {
		// 开始日期
		long beginDateLong = beginDate.getTime();
		// 结束日期
		long endDateLong = endDate.getTime();
		// 当前日期
		long currentDateLong = currentDate.getTime();
		if (currentDateLong >= beginDateLong && currentDateLong <= endDateLong) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 获取当前月份的第一天
	 * 
	 *            当前时间
	 * @return
	 */
	public static Date getMinDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), currentDate
				.get(Calendar.MONTH), currentDate
				.getActualMinimum(Calendar.DATE));
		return result.getTime();
	}

	public static Calendar getDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date);
		return calendar;
	}

	public static Calendar getDate(int year, int month) {
		return getDate(year, month, 0);
	}

	public static Date getCountMinDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, calendar.getActualMinimum(Calendar.DATE));
		return calendar.getTime();
	}

	public static Date getCountMaxDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 0);
		return calendar2.getTime();
	}

	/**
	 * 获取当前月份的第一天
	 * 
	 *            当前时间
	 * @return
	 */
	public static Date getMinDate() {
		Calendar currentDate = Calendar.getInstance();
		return getMinDate(currentDate);
	}

	/**
	 * 获取当前月分的最大天数
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return
	 */
	public static Date getMaxDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), currentDate
				.get(Calendar.MONTH), currentDate
				.getActualMaximum(Calendar.DATE));
		return result.getTime();
	}

	/**
	 * 获取当前月分的最大天数
	 * 
	 *            当前时间
	 * @return
	 */
	public static Date getMaxDate() {
		Calendar currentDate = Calendar.getInstance();
		return getMaxDate(currentDate);
	}

	/**
	 * 获取今天最大的时间
	 * 
	 * @return
	 */
	public static String getMaxDateTimeForToDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		return convertDate2String(DEFAILT_DATE_TIME_PATTERN, calendar.getTime());
	}

	/**
	 * 获取日期最大的时间
	 * 
	 * @return
	 */
	public static Date getMaxDateTimeForToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * 获取今天最小时间
	 * 
	 * @return
	 */
	public static String getMinDateTimeForToDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		return convertDate2String(DEFAILT_DATE_TIME_PATTERN, calendar.getTime());
	}

	/**
	 * 获取 date 最小时间
	 * 
	 * @return
	 */
	public static Date getMinDateTimeForToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * 获取发生日期的结束时间 根据用户设置的日期天数来判定这这个日期是什么(例如 (getHappenMinDate = 2008-10-1) 的话
	 * 那么 (getHappenMaxDate = 2008-11-1) 号)
	 * 
	 * @return
	 */
	public static Date getHappenMaxDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 加减天数
	 * 
	 * @param num
	 * @param Date
	 * @return
	 */
	public static Date addDay(int num, Date Date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date);
		calendar.add(Calendar.DATE, num);// 把日期往后增加 num 天.整数往后推,负数往前移动
		return calendar.getTime(); // 这个时间就是日期往后推一天的结果
	}



	/**
	 * 计算两端时间的小时差
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getHour(Date begin, Date end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(begin);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		Long millisecond = c2.getTimeInMillis() - c1.getTimeInMillis();
		Long hour = millisecond / 1000 / 60 / 60;
		Long minute = (millisecond / 1000 / 60) % 60;
		if (minute >= 30) {
			hour++;
		}

		return hour.intValue();
	}

	/**
	 * 格式化日期
	 */
	public static String dateFormat(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormat.getDateInstance().format(date);
	}

	/**
	 * @return String
	 * @throws ParseException
	 */
	public static String setDateFormat(Date myDate, String strFormat)
			throws ParseException {
		if (myDate == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);
		return sDate;
	}

	public static String setDateFormat(String myDate, String strFormat)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);

		return sDate;
	}

	/*****************************************
	 * @功能 计算某年某月的结束日期
	 * @return interger
	 * @throws ParseException
	 ****************************************/
	public static String getYearMonthEndDay(int yearNum, int monthNum)
			throws ParseException {

		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "31";
		if (tempMonth.equals("1") || tempMonth.equals("3")
				|| tempMonth.equals("5") || tempMonth.equals("7")
				|| tempMonth.equals("8") || tempMonth.equals("10")
				|| tempMonth.equals("12")) {
			tempDay = "31";
		}
		if (tempMonth.equals("4") || tempMonth.equals("6")
				|| tempMonth.equals("9") || tempMonth.equals("11")) {
			tempDay = "30";
		}
		if (tempMonth.equals("2")) {
			if (isLeapYear(yearNum)) {
				tempDay = "29";
			} else {
				tempDay = "28";
			}
		}
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return tempDate;// setDateFormat(tempDate,"yyyy-MM-dd");
	}

	/*****************************************
	 * @功能 判断某年是否为闰年
	 * @return boolean
	 * @throws ParseException
	 ****************************************/
	public static boolean isLeapYear(int yearNum) {
		boolean isLeep = false;
		/** 判断是否为闰年，赋值给一标识符flag */
		if ((yearNum % 4 == 0) && (yearNum % 100 != 0)) {
			isLeep = true;
		} else if (yearNum % 400 == 0) {
			isLeep = true;
		} else {
			isLeep = false;
		}
		return isLeep;
	}

	/**
	 * 格式化日期
	 * 
	 * @throws ParseException
	 * 
	 *             例: DateUtils.formatDate("yyyy-MM-dd HH",new Date())
	 *             "yyyy-MM-dd HH:00:00"
	 */
	public static Date formatDate(String formatString, Date date)
			throws ParseException {
		if (date == null) {
			date = new Date();
		}
		if (StringUtils.isBlank(formatString))
			formatString = DateUtils.DEFAILT_DATE_PATTERN;

		date = DateUtils.convertString2Date(formatString, DateUtils
				.convertDate2String(formatString, date));

		return date;
	}

	/**
	 * 格式化日期 yyyy-MM-dd
	 * 
	 * @throws ParseException
	 *             例： DateUtils.formatDate(new Date()) "yyyy-MM-dd 00:00:00"
	 */
	public static Date formatDate(Date date) throws ParseException {
		date = formatDate(DateUtils.DEFAILT_DATE_PATTERN, date);
		return date;
	}

	/**
	 * @throws ParseException
	 *             根据日期获得 星期一的日期
	 * 
	 */
	public static Date getMonDay(Date date) throws ParseException {

		Calendar cal = Calendar.getInstance();
		if (date == null)
			date = new Date();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(Calendar.WEEK_OF_YEAR, -1);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		date = formatDate(cal.getTime());

		return date;
	}

	/**
	 * @throws ParseException
	 *             根据日期获得 星期日 的日期
	 * 
	 */
	public static Date getSunDay(Date date) throws ParseException {

		Calendar cal = Calendar.getInstance();
		if (date == null)
			date = new Date();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
			cal.add(Calendar.WEEK_OF_YEAR, 1);

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		date = formatDate(cal.getTime());
		return date;
	}

	/**
	 * 根据传入时间的格式返回不同的SimpleDateFormat(yyyyMMddHHmm/yyyy-MM-dd/yyyy-MM-dd HH:mm)
	 *
	 * @param time
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(String time) {
		SimpleDateFormat sf = null;
		if (time.length() == 6) {
			sf = new SimpleDateFormat("yyyyMM");
		} else if (time.length() == 7) {
			sf = new SimpleDateFormat("yyyy-MM");
		} else if (time.length() == 8) {
			sf = new SimpleDateFormat("yyyyMMdd");
		} else if (time.length() == 12) {
			sf = new SimpleDateFormat("yyyyMMddHHmm");
		} else if (time.length() == 10) {
			sf = new SimpleDateFormat("yyyy-MM-dd");
		} else if (time.length() == 16) {
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (time.length() == 19) {
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		if (sf == null) {
		}
		return sf;
	}

	public static boolean compareDate(String kssj, String jssj) throws ParseException {
		SimpleDateFormat sf;
		long cur_second;
		long ks_second;
		long js_second;
		//开始日期和结束日期都不为空
		if (!StringUtils.isBlank(kssj) && !StringUtils.isBlank(jssj)) {
			sf = getDateFormat(kssj);
			cur_second = sf.parse(sf.format(new Date())).getTime();
			ks_second = sf.parse(kssj).getTime();
			js_second = sf.parse(jssj).getTime();
			if ((cur_second < ks_second) || (cur_second > js_second)) {
				return false;
			} else {
				return true;
			}
		}
		//开始时间不为空，结束日期为空
		if (!StringUtils.isBlank(kssj)) {
			sf = getDateFormat(kssj);
			cur_second = sf.parse(sf.format(new Date())).getTime();
			ks_second = sf.parse(kssj).getTime();
			//当前日期在开始日期之前
			if (ks_second > cur_second) {
				return false;
			} else {
				return true;
			}

		}
		//开始时间为空，结束时间不为空
		if (!StringUtils.isBlank(jssj)) {
			sf = getDateFormat(jssj);
			cur_second = sf.parse(sf.format(new Date())).getTime();
			js_second = sf.parse(jssj).getTime();
			//当前日期在结束日期之后
			if (js_second < cur_second) {
				return false;
			} else {
				return true;
			}
		}
		// 两者都为空
		return true;
	}

	public static boolean compareDateBetween(String kssj, String jssj, String sj) throws ParseException {
		SimpleDateFormat sf;
		long cur_second;
		long ks_second;
		long js_second;
		//开始日期和结束日期都不为空
		if (!StringUtils.isBlank(kssj) && !StringUtils.isBlank(jssj)) {
			sf = getDateFormat(kssj);
			cur_second = sf.parse(sj).getTime();
			ks_second = sf.parse(kssj).getTime();
			js_second = sf.parse(jssj).getTime();
			if ((cur_second < ks_second) || (cur_second > js_second)) {
				return false;
			} else {
				return true;
			}
		}
		//开始时间不为空，结束日期为空
		if (!StringUtils.isBlank(kssj)) {
			sf = getDateFormat(kssj);
			cur_second = sf.parse(sf.format(new Date())).getTime();
			ks_second = sf.parse(kssj).getTime();
			//当前日期在开始日期之前
			if (ks_second > cur_second) {
				return false;
			} else {
				return true;
			}

		}
		//开始时间为空，结束时间不为空
		if (!StringUtils.isBlank(jssj)) {
			sf = getDateFormat(jssj);
			cur_second = sf.parse(sj).getTime();
			js_second = sf.parse(jssj).getTime();
			//当前日期在结束日期之后
			if (js_second < cur_second) {
				return false;
			} else {
				return true;
			}
		}
		// 两者都为空
		return true;
	}

	/**
	 * 获得 下个月的第一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getNextDay(Date date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		return formatDate(cal.getTime());
	}

	/**
	 * 根据日期获取坐在周
	 */
	public static String[] getWeekDays(Date date,boolean weekendAble) throws ParseException {
		int daySize = 7;
		if(!weekendAble) daySize = 5;
		String[] days = new String[daySize];
		days[0] = convertDate2String(date);
		for(int i=1;i<daySize;i++){
			days[i] = convertDate2String(addDay(i,parseDate(days[0])));
		}
		return  days;
	}

	public static String getCurrentDateTime(){
		SimpleDateFormat format = new SimpleDateFormat(DEFAILT_DATE_TIME_PATTERN);;
		return  format.format(new Date());
	}

	public static String getCurrentDateTimeShort(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");;
		return  format.format(new Date());
	}

	public static String getCurrentDate(){
		SimpleDateFormat format = new SimpleDateFormat(DEFAILT_DATE_PATTERN);;
		return  format.format(new Date());
	}

	public static String getNextNDayString(String date, int n){
		Date d = parseDate(date);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, n);
		return  convertDate2String(calendar.getTime());
	}

	/*public static void main(String[] args){
		String date = DateUtils.getNextNDayString("2021-09-09", -1);
		System.out.println(date);
	}*/

	//根据当前日期内一周的日期
	public static List<String> dateToWeek(Date mdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> list = new ArrayList<String>();
		String week = getWeekOfDate(mdate);
		for(int i=Integer.parseInt(week)-1;i>=0;i--){
			Calendar c = Calendar.getInstance();
			c.setTime(mdate);
			if(!getWeekOfDate(mdate).equals("1")){
				c.add(Calendar.DAY_OF_MONTH,-i);
				String rq = sdf.format(c.getTime());
				list.add(DateUtils.dateToWeek(rq) + " " + rq);
			}else{
				c.add(Calendar.DAY_OF_MONTH,0);
				String rq = sdf.format(c.getTime());
				list.add(DateUtils.dateToWeek(rq) + " " + rq);            }
		}
		for(int j=1;j<=7-Integer.parseInt(week);j++){
			Calendar c = Calendar.getInstance();
			c.setTime(mdate);
			c.add(Calendar.DAY_OF_MONTH, j);
			String rq = sdf.format(c.getTime());
			list.add(DateUtils.dateToWeek(rq) + " " + rq);        }
		return list;
	}


	//获取当前是星期几
	public  static String getWeekOfDate(Date data) {
		String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekDay < 0)
			weekDay = 0;
		return weekDays[weekDay];
	}

	//计算当前周，下一周，上一周的日期
	public static List<String> weekend(String weekend) {
		List<String> days;
		if(weekend.equals("0")){
			days = dateToWeek(new Date());
		}else{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(weekend));
			days=dateToWeek(c.getTime());
		}
		return days;
	}

	//根据开始时间和结束时间的区间的星期几,取出对应的日期
	public static List<String> queryDate(String kssj, String jssj, String[] week) {
		List<String> date=new ArrayList<String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			//日期转换
			Date start= sdf.parse(kssj);
			Date rare=sdf.parse(jssj);
			int days= getDistanceDay(start,rare);//获取相隔的天数
			for(int i=0;i<week.length;i++){
				for(int j=0;j<=days;j=j+1){
					Date findDate = addDate(start,j);//每次加一天获取新的日期
					int weekOfDay=Integer.valueOf(getWeekOfDate(findDate));//获取每次加一天获取新的日期是星期几
					if(weekOfDay==Integer.valueOf(week[i])){
						date.add(sdf.format(findDate));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/*当前日期后一周的日期 不包含当前日期*/
	public static List<String> getNextDayWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<String> list = new ArrayList<String>();
		for (int j = 1; j <= 7 ; j++) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, j);
			String rq = sdf.format(c.getTime());
			list.add(DateUtils.dateToWeek(rq) + " " + rq);
		}
		return list;
	}

	/*当前日期后一周的日期 包含当前日期*/
	public static List<String> getDayWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<String> list = new ArrayList<String>();
		for (int j = 0; j < 7 ; j++) {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, j);
			String rq = sdf.format(c.getTime());
			list.add(DateUtils.dateToWeek(rq) + " " + rq);
		}
		return list;
	}

	//获取日期相隔几天
	public  static int getDistanceDay(Date beagin,Date end) {
		Calendar calender = Calendar.getInstance();
		calender.setTime(beagin);
		long milliseconds1 = calender.getTimeInMillis();
		calender.setTime(end);
		long milliseconds2 = calender.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		int diffDays = (int)(diff / (24 * 60 * 60 * 1000));
		return diffDays;
	}

	//日期加上天数得到新的日期
	public  static Date addDate(Date date,long day) {
		long time = date.getTime(); // 得到指定日期的毫秒数
		day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
		time+=day; // 相加得到新的毫秒数
		return new Date(time); // 将毫秒数转换成日期
	}

	//日期加上天数得到新的日期
	public  static Date addDateMin(Date date,int day) {
		long time = date.getTime(); // 得到指定日期的毫秒数
		day = day * 60 * 1000; // 要加上的天数转换成毫秒数
		time+=day; // 相加得到新的毫秒数
		return new Date(time); // 将毫秒数转换成日期
	}


	public static String getCurrentYear(){
		Calendar date = Calendar.getInstance();
		String year = String.valueOf(date.get(Calendar.YEAR));
		return year;
	}

	public static int getCurrentMonth(){
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.MONTH);
	}

	/*判断据当前时间是否已不足24 小时 true 不足     false  足*/
	public static boolean timeCompare(String time){
		Date date = DateUtils.parseDateTime(time);
		Date nextDay = DateUtils.addDay(-1, date);
		int flag = DateUtils.compare_date(nextDay, Calendar.getInstance().getTime());
		return flag < 1;
	}


	public static String convert2chinese(String time)throws ParseException{
		Date date  = DateUtils.convertString2Date(time);
		return new SimpleDateFormat("yyyy年MM月dd日").format(date);
	}

	public static String convert2chineseHour(String time)throws ParseException{
		Date date  = DateUtils.convertString2Date(DEFAULT_TIME_PATTERN, time);
		return new SimpleDateFormat("HH时mm分").format(date);
	}

	public static Date convert(String targetDate){
		if (StringUtils.isBlank(targetDate)) {
			return null;
		}
		SimpleDateFormat format = getDateFormat(targetDate);
		Date result = null;
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return result;
	}


	public static void main(String[] args) throws ParseException{
	    System.out.println(isDateBefore("2022-10-08"));
	}


}
