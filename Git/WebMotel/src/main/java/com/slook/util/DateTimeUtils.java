/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.slook.util;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sondm2@viettel.com.vn
 * @version 1.0
 * @since Apr, 12, 2010
 */
public class DateTimeUtils {

    private static final Logger logger = Logger.getLogger(DateTimeUtils.class);

    /**
     * private constructor
     */
    private DateTimeUtils() {
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.format(date);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return "";
    }

    public static String formatDateCommon(Date date) {
        return format(date, Constant.PATTERN.DATE_COMMON);
    }

    public static String formatDateTimeCommon(Date date) {
        return format(date, Constant.PATTERN.DATETIME_COMMON);
    }

    public static String formatDateTimeChart(Date date) {
        return format(date, Constant.PATTERN.DATETIME_CHART);
    }
    public static String formatDateChart(Date date) {
        return format(date, Constant.PATTERN.DATE_CHART);
    }

    public static Date parse(String _date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(_date);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static Date trunc(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date add(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    public static Date add(Date date, int field, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);
        return cal.getTime();
    }


    public static int getCurrentHour() {
        int currentHour = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentHour = now.get(Calendar.HOUR_OF_DAY);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return currentHour;
    }

    public static int getCurrentMinute() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.MINUTE);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return currentMinute;
    }

    public static int getCurrentDayOfMonth() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.DAY_OF_MONTH);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return currentMinute;
    }

    public static int getCurrentMonth() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.MONTH);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return currentMinute;
    }

    public static int getCurrentYear() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.YEAR);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return currentMinute;
    }

    //dungvv8_15082016_old

    public static String getSysDateTime(String pattern) throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.format(calendar.getTime());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw ex;
        }
    }

}
