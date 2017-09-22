package com.dvd.ckp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

	public static void main(String[] arg) {
		try {
			System.out.println(convertDateToString(new Date(), "dd/MM/yyyy HH:MM:ss"));
			System.out.println(convertStringToTime("20170922110926", "HH:MM:ss"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
