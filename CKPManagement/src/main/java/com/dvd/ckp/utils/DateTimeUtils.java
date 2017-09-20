package com.dvd.ckp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		System.out.println(convertStringToTime("06051992", "ddMMyyyy"));
	}
}
