package com.zy.usbgpslib.entity;

import androidx.annotation.NonNull;

import com.zy.usbgpslib.enums.GPGLLStatusEnum;
import com.zy.usbgpslib.util.GpsTools;
import com.zy.usbgpslib.util.StringUtils;


public class GPGLL extends GspBase{



    @NonNull
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        //lat
        if(!StringUtils.checkEmpty(getArr2())){
            String latitudeDirection = GpsTools.getDirection(getArr2());
            sb.append(latitudeDirection+":");
            sb.append(GpsTools.latitudeOrLongitudeConvert(Double.parseDouble(getArr1())));
            sb.append(",");
        }
        //lng
        if(!StringUtils.checkEmpty(getArr4())){
            String longitudeDirection = GpsTools.getDirection(getArr4());
            sb.append(longitudeDirection+":");
            sb.append(GpsTools.latitudeOrLongitudeConvert(Double.parseDouble(getArr3())));
            sb.append(",");
        }
        //时间
        if(!StringUtils.checkEmpty(getArr5())){
            String utcTime = GpsTools.utc2Local(getArr5(),GpsTools.UTC_TIME_PATTERN,GpsTools.LOCAL_TIME_PATTERN);
            sb.append("utcTime:"+utcTime);
            sb.append(",");
        }
        //状态
        if(!StringUtils.checkEmpty(getArr6())){
            String status = GPGLLStatusEnum.get(getArr6()).getValue();
            sb.append("状态:"+status);
        }

        return sb.toString();
    }
}
