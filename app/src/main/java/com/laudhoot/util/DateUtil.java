package com.laudhoot.util;

import com.laudhoot.di.module.SystemServicesModule;

import java.util.Date;

/**
 * Created by root on 3/11/15.
 */
public class DateUtil {

    public static long FOR_MILLIS_TO_SECONDS = 1000l;
    public static long FOR_MILLIS_TO_MINUTES = 60l * FOR_MILLIS_TO_SECONDS;
    public static long FOR_MILLIS_TO_HOURS = 60l * FOR_MILLIS_TO_MINUTES;
    public static long FOR_MILLIS_TO_DAYS = 24l * FOR_MILLIS_TO_HOURS;
    public static long FOR_MILLIS_TO_WEEKS = 7l * FOR_MILLIS_TO_DAYS;
    public static long FOR_MILLIS_TO_MONTHS = 4l * FOR_MILLIS_TO_WEEKS;
    public static long FOR_MILLIS_TO_YEARS = 4l * FOR_MILLIS_TO_MONTHS;

    public static String getElapsedDuration(Date date) throws IllegalArgumentException {
        if(date == null) {
            throw new IllegalArgumentException("date cannot be null");
        }
        long milliseconds = date.getTime();
        milliseconds = new Date().getTime() - milliseconds;
        if (milliseconds / FOR_MILLIS_TO_MINUTES == 0l) {
            return "Just now";
        } else if (milliseconds / FOR_MILLIS_TO_HOURS == 0l) {
            return (milliseconds / FOR_MILLIS_TO_MINUTES) + "M";
        } else if (milliseconds / FOR_MILLIS_TO_DAYS == 0l) {
            return (milliseconds / FOR_MILLIS_TO_HOURS) + "H";
        } else if (milliseconds / FOR_MILLIS_TO_WEEKS == 0l) {
            return (milliseconds / FOR_MILLIS_TO_DAYS) + "D";
        } else if (milliseconds / FOR_MILLIS_TO_MONTHS == 0l) {
            return (milliseconds / FOR_MILLIS_TO_WEEKS) + "W";
        } else if (milliseconds / FOR_MILLIS_TO_YEARS == 0l) {
            return (milliseconds / FOR_MILLIS_TO_MONTHS) + "M";
        } else {
            return (milliseconds / FOR_MILLIS_TO_YEARS) + "Y";
        }
    }

}
