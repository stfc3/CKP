package com.dvd.ckp.utils;

import java.text.SimpleDateFormat;
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
    public static String convertDateToString(Date pstrDate, String pstrPattern){
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
<<<<<<< HEAD
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
=======
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

    public static void main(String[] arg) {
        try {
            System.out.println(convertDateToString(new Date(), "dd/MM/yyyy HH:MM:ss"));
            System.out.println(convertStringToTime("20170922110926", "HH:MM:ss"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
>>>>>>> 2a7e67d571f490733647ef86fb3ce791726b6a54
}
