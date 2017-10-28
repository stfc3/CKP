package com.dvd.ckp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import bsh.ParseException;

public class DateTimeUtils {
	private static final Logger logger = Logger.getLogger(DateTimeUtils.class);

	/*
	 * @todo: Action chuyen tu dang date sang string
	 * 
	 * @param: pstrDate
	 * 
	 * @param: pstrPattern
	 */
	public static String convertDateToString(Date pstrDate, String pstrPattern) throws Exception {
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
	
	public static Long getDifferenceDay(Date startDate, Date endDate){
		Long diff = endDate.getTime() - startDate.getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static int getLastDayOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	public static void main(String[] arg) {
		try {
//			System.out.println(convertDateToString(new Date(), "dd/MM/yyyy HH:MM:ss"));
			System.out.println(getDifferenceDay(convertStringToTime("20171001", "yyyyMMdd"),new Date()));
			System.out.println("Last day of month: " + getLastDayOfMonth(convertStringToTime("20170901", "yyyyMMdd")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
