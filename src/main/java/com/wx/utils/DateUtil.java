package com.wx.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * 
 * 时间工具类
 */
public class DateUtil {

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String SHORT_FORMAT = "yyyy-MM-dd HH:mm";

	public static final String HOUR_FORMAT = "yyyy-MM-dd HH";

	public static final String HOUR_FORMAT_mm = "yyyy-MM-dd HH:00";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String MONTH_FORMAT = "yyyy-MM";

	public static final String DATE_FORMAT_MMDDHHMM = "MM-dd HH:mm";

	public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

	public static final String DATE_FOMATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String REPORT_NAME_FORMAT = "yyyyMMddHH";

	public static final String START_FORMAT = "yyyy-MM-dd 00:00:00";

	public static final String END_FORMAT = "yyyy-MM-dd 23:59:59";

	public static final String MM_START_FORMAT = "yyyy-MM-dd 00:00";

	public static final String MM_END_FORMAT = "yyyy-MM-dd 23:59";
	
	public static final String MM_dd = "MM-dd";

	/**
	 * 将字串转成日期和时间
	 * 
	 * @param date
	 * @param format
	 * @return Date
	 * @throws ParseException
	 */
	public static Date convertStrToDate(String date, String format) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(date);
	}

	public static Date convertStrToDate(String date) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);
		return formatter.parse(date);
	}

	/**
	 * 获取当前时间，并返回指定格式的时间字符串。
	 * 
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String getCurrentDate(String format) {
		return new DateTime().toString(format);
	}

	/**
	 * 格式化时间
	 * 
	 * @param format
	 *            格式
	 * @return String
	 */
	public static String format(Timestamp date, String format) {
		return new DateTime(date).toString(format);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date, String format) {
		return new DateTime(date).toString(format);
	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date) {
		return new DateTime(date).toString(DEFAULT_FORMAT);
	}

	/**
	 * 计算几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		return new DateTime(d).minusDays(day).toDate();
	}

	public static List<Date> getDateListBefore(Date d, int day) {
		List<Date> dateList = new ArrayList<Date>();
		for (int i = 0; i < day; i++) {
			dateList.add(new DateTime(d).minusDays(i).toDate());
		}
		return dateList;
	}

	/**
	 * 计算几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @param format
	 * @return
	 */
	public static String getDateBefore(Date d, int day, String format) {
		return new DateTime(d).minusDays(day).toString(format);
	}

	/**
	 * 计算几小时前的时间
	 * 
	 * @param d
	 * @param hour
	 * @return
	 */
	public static Date getHourBefore(Date d, int hour) {
		return new DateTime(d).minusHours(hour).toDate();
	}

	/**
	 * 计算几个月的时间
	 * 
	 * @param d
	 * @param hour
	 * @return
	 */
	public static Date getMonthBefore(Date d, int month) {
		return new DateTime(d).minusMonths(month).toDate();
	}

	/**
	 * 计算时间间隔
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDayBetween(Date d1, Date d2) {
		DateTime dt1 = new DateTime(d1);
		DateTime dt2 = new DateTime(d2);
		Period p = new Period(dt1, dt2, PeriodType.days());
		return p.getDays();
	}
	/**
	 * 计算时间间隔 返回秒
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getSecondsBetween(Date d1, Date d2) {
		DateTime dt1 = new DateTime(d1);
		DateTime dt2 = new DateTime(d2);
		Period p = new Period(dt1, dt2, PeriodType.seconds());
		return p.getSeconds();
	}
	

	/**
	 * 日期转string
	 * 
	 * @param date
	 *            to str
	 * @return
	 */
	public static String dateToStrLong(Date dateDate, SimpleDateFormat formatter) {
		String dateString = formatter.format(dateDate);
		return dateString;
	}

	public static void main(String[] args) throws Exception {
		
		System.out.println(DateUtil.format(new Date(), DateUtil.SHORT_FORMAT));
	}
}
