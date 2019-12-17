package com.ruimeng.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * @Title: stringToDate
     * @Description: 字符串转date
     * @param time 时间字符串
     * @throws ParseException 简单描述
     * @return Date 时间
     */
    public static Date stringToDate(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(time);
    }

    /**
     * @Title: dateToString
     * @Description: date转字符串
     * @param date 日期
     * @return String 返回类型
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
