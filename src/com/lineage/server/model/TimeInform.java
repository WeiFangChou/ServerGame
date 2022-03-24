package com.lineage.server.model;

import com.lineage.config.Config;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeInform {
    private static TimeInform _instance;

    static TimeZone timezone = TimeZone.getTimeZone(Config.TIME_ZONE);
    static Calendar rightNow = Calendar.getInstance(timezone);

    public static TimeInform time() {
        if (_instance == null) {
            _instance = new TimeInform();
        }
        return _instance;
    }

    public static String getYear(int type, int i) {
        if (type == 0) {
            return String.valueOf(rightNow.get(1) + i);
        }
        if (type == 1) {
            return "西元 " + String.valueOf(rightNow.get(1));
        }
        if (type == 2) {
            return "民國 " + String.valueOf(rightNow.get(1) - 1911);
        }
        return null;
    }

    public static String getMonth() {
        return String.valueOf(rightNow.get(2) + 1);
    }

    public static String getDay() {
        return String.valueOf(rightNow.get(5));
    }

    public static int GetNowHour() {
        return Calendar.getInstance().get(11);
    }

    public static int GetNowMinute() {
        return Calendar.getInstance().get(12);
    }

    public static int GetNowSecond() {
        return Calendar.getInstance().get(13);
    }

    public static String getNowTime(int type, int type_year) {
        switch (type) {
            case 1:
                return String.valueOf(getYear(type_year, 0)) + "年 " + getMonth() + "月" + getDay() + "日 " + getDayOfWeek();
            case 2:
                return String.valueOf(GetNowHour()) + "時" + GetNowMinute() + "分" + GetNowSecond() + "秒";
            case 3:
                return String.valueOf(getYear(type_year, 0)) + "年" + getMonth() + "月" + getDay() + "日" + getDayOfWeek() + GetNowHour() + "時" + GetNowMinute() + "分" + GetNowSecond() + "秒";
            default:
                return null;
        }
    }

    public static String getDayOfWeek() {
        switch (rightNow.get(7)) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return null;
        }
    }

    public String getNowTime_Standard() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
