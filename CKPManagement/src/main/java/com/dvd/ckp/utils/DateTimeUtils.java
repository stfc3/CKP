package com.dvd.ckp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class DateTimeUtils {

	private static final Logger logger = Logger.getLogger(DateTimeUtils.class);

	/*
	 * @todo: Action chuyen tu dang date sang string
	 * 
	 * @param: pstrDate
	 * 
	 * @param: pstrPattern
	 */
	public static String convertDateToString(Date pstrDate, String pstrPattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pstrPattern);
		try {
			if (pstrDate == null) {
				return "";
			}
			return dateFormat.format(pstrDate);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/*
	 * @todo: Action chuyen tu dang string sang date
	 * 
	 * @param: pstrDate
	 * 
	 * @param: pstrPattern
	 */
	public static Date convertStringToTime(String pstrDate, String pstrPattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pstrPattern);
		try {
			return dateFormat.parse(pstrDate);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static Long getDifferenceDay(Date startDate, Date endDate) {
		Long diff = endDate.getTime() - startDate.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;
	}

	public static int getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static java.sql.Date convertStringToDateSql(String pstrDate, String pstrPattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pstrPattern);
		try {
			Date date = dateFormat.parse(pstrDate);
			java.sql.Date dateSql = new java.sql.Date(date.getTime());
			return dateSql;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	public static boolean compareDate(Date startDate, Date endDate){
		return startDate.after(endDate);
	}

	public static boolean compareMonth(Date startDate, Date endDate){
		Calendar cal = Calendar.getInstance();		
		cal.setTime(startDate);
		
		int monthStartDate = cal.get(Calendar.MONTH);
		
		cal.setTime(endDate);
		
		int monthEndDate = cal.get(Calendar.MONTH);
		
		return (monthStartDate == monthEndDate);
	}
	public static void main(String[] arg) {
		try {
			Date fromDate = convertStringToTime("20170101", "yyyyMMdd");
			Date toDate = convertStringToTime("20170131", "yyyyMMdd");
			System.out.println(getDifferenceDay(fromDate, toDate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
