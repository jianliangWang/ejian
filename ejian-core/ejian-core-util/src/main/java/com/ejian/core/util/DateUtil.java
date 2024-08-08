package com.ejian.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String DATE_TIME_FORMAT_NUMBER =  "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_NUMBER =  "yyyyMMdd";
    public static final String DATE_TIME_FORMAT_ZN =  "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_BEGIN =  "yyyy-MM-dd 00:00:00";
    public static final String DATE_TIME_FORMAT_END =  "yyyy-MM-dd 23:59:59";

    public static final String TIME_FORMAT_MIN = "00:00:00";
    public static final String TIME_FORMAT_MAX = "23:59:59";
    /**
     * 获取当前时间，时间格式：YYYYMMDDhhmmss
     * @return 字符串
     */
    public static String getDate_Y_M_D_h_m_s(){
        return getCurrentDate(DATE_TIME_FORMAT_NUMBER);
    }

    public static String getDateYYYYMMDD(){
        return getCurrentDate(DATE_FORMAT_NUMBER);
    }

    /**
     * 获取当前时间，时间格式：YYYY-MM-dd HH:mm:ss
     * @return 字符串
     */
    public static String getCurrentNow(){
        return getCurrentDate(DATE_TIME_FORMAT_ZN);
    }

    /**
     * 获取当前日期0点 例如：2023-01-23 00:00:00
     * @return 返回字符串
     */
    public static String getCurrentBeginTime(){
        return getCurrentDate(DATE_TIME_FORMAT_BEGIN);
    }

    /**
     * 获取当前日期的最后时刻 例如：2023-01-23 23:59:59
     * @return 返回字符串
     */
    public static String getCurrentEndTime(){
        return getCurrentDate(DATE_TIME_FORMAT_END);
    }

    /**
     * 获取当前时间指定格式
     * @param format 时间格式
     * @return 指定格式的时间字符串
     */
    public static String getCurrentDate(String format){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    public static String getLocalDateFormat(LocalDateTime localDateTime, String format){
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime getCurrentDate(){
        return LocalDateTime.now();
    }

    public static String getDayMinTime(String day){
        return getDayTime(day, TIME_FORMAT_MIN);
    }

    public static String getDayMaxTime(String day){
        return getDayTime(day, TIME_FORMAT_MAX);
    }

    public static String getDayTime(String day, String time){
        return day + time;
    }

    public static void main(String[] args) {
        System.out.println(getCurrentDate());
    }
}
