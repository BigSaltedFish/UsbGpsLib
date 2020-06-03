package com.zy.usbgpslib.util;


import com.zy.usbgpslib.enums.GpsDirectionEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GpsTools {
    public static String UTC_TIME_PATTERN = "hhmmss.sss";
    public static String LOCAL_TIME_PATTERN = "hh:mm:ss";
    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));//时区定义并进行时间获取
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return utcTime;
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }

    public static double latitudeOrLongitudeConvert(double dm){
        int d=(int) (dm/100);
        double mm=dm/100-d;
        mm=mm*100/60;
        double dd=d+mm;
        return dd;
    }
    public static int trimNum(String numStr){
        int lastIndex = numStr.lastIndexOf("0");
        String realNumStr = numStr.substring(lastIndex);
        return Integer.parseInt(realNumStr);
    }
    public static String getDirection(String direct){
        GpsDirectionEnum gpsDirectionEnum = GpsDirectionEnum.get(direct);
        return gpsDirectionEnum.getValue();
    }

}
