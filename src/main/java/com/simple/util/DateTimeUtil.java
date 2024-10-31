package com.simple.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    /**
     * 오늘 날짜 가져오기
     *  - 00시 00분 00초 00ms 처리.
     * @return
     */
    public static Date getToday() {
        Calendar cal = Calendar.getInstance();

        return getDay(cal.getTime());
    }

    int a = Calendar.DAY_OF_WEEK;

    /**
     * 날짜 데이타에서 시간을 초기화한다.
     *  - 00시 00분 00초 00ms 처리.
     * @param d
     * @return
     */
    public static Date getDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static Date add(Date date, int field, int amount) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, amount);

        return cal.getTime();
    }

    public static int getYear(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.YEAR);
    }

    /**
     * 날짜를 문자열로 변환 ("yyyy-MM-dd HH:mm:ss")
     * @param date
     * @return
     */
    public static String convertString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String convertStringDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public static Date convertDate(String s, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(s);
    }

    /**
     * 시스템 현재시간 호출
     *
     * @param format : ex) yyyyMMdd
     * @return
     */
    public static String getSystemDate(String format) {
        return getFormatDate(null, format);
    }

    /**
     * 날짜 Format 변환
     * @param date
     * @param format
     * @return
     */
    public static String getFormatDate(Date date, String format) {
        Calendar cal = Calendar.getInstance();

        if (date != null)
            cal.setTime(date);

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(cal.getTime());
    }


    /**
     * 오늘 날짜의 주차를 가져온다
     * @return
     */
    public static int getWeekNo(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getToday());
        return cal.get(Calendar.WEEK_OF_MONTH);
    }

}
