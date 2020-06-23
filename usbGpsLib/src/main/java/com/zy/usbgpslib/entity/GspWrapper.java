package com.zy.usbgpslib.entity;


import com.zy.usbgpslib.enums.GPGLLStatusEnum;
import com.zy.usbgpslib.util.GpsTools;
import com.zy.usbgpslib.util.StringUtils;

import java.util.Date;

public class GspWrapper {

    private GspBase gpgga;

    private GspBase gpgll;

    private GspBase gpgsa;

    private GspBase gpgsv;

    private GspBase gprmc;

    private GspBase gpvtg;

    public GpsInfo buildGpsInfo(){
        GpsInfo gpsInfo = new GpsInfo();
        String llArr1 = gpgll.getArr1();
        String llArr2 = gpgll.getArr2();
        String llArr3 = gpgll.getArr3();
        String llArr4 = gpgll.getArr4();
        String llArr5 = gpgll.getArr5();
        String llArr6 = gpgll.getArr6();


        if(!StringUtils.checkEmpty(llArr1)){
            gpsInfo.setLatitude(GpsTools.latitudeOrLongitudeConvert(Double.parseDouble(llArr1)));
        }
        if(!StringUtils.checkEmpty(llArr3)){
            gpsInfo.setLongitude(GpsTools.latitudeOrLongitudeConvert(Double.parseDouble(llArr3)));
        }
        if(!StringUtils.checkEmpty(llArr2)){
            gpsInfo.setLatitudeDirection(GpsTools.getDirection(llArr2));
        }
        if(!StringUtils.checkEmpty(llArr4)){
            gpsInfo.setLongitudeDirection(GpsTools.getDirection(llArr4));
        }

        if(!StringUtils.checkEmpty(llArr5)){
            String utcTime = GpsTools.utc2Local(llArr5,GpsTools.UTC_TIME_PATTERN,GpsTools.LOCAL_TIME_PATTERN);
        }
        if(!StringUtils.checkEmpty(llArr6)){
            String status = GPGLLStatusEnum.get(llArr6).getValue();
            gpsInfo.setLocationStatus(status);
        }
        gpsInfo.setDate(new Date());
        return gpsInfo;

    }

    public void setValue(GspBase gspBase){
        if(gspBase.getClass().equals(GPGGA.class)){
            setGpgga(gspBase);
        }else if(gspBase.getClass().equals(GPGLL.class)){
            setGpgll(gspBase);
        }else if(gspBase.getClass().equals(GPGSA.class)){
            setGpgsa(gspBase);
        }else if(gspBase.getClass().equals(GPGSV.class)){
            setGpgsv(gspBase);
        }else if(gspBase.getClass().equals(GPRMC.class)){
            setGprmc(gspBase);
        }else if(gspBase.getClass().equals(GPVTG.class)){
            setGpvtg(gspBase);
        }
    }

    public boolean complete(){
        if(null != gpgga
                && null != gpgll
                && null != gpgsa
                && null != gpgsv
                && null != gprmc
                && null != gpvtg){
            return true;
        }
        return false;
    }
    public void reset(){
        gpgga = null;
        gpgll = null;
        gpgsa = null;
        gpgsv = null;
        gprmc = null;
        gpvtg = null;

    }

    public GspBase getGpgga() {
        return gpgga;
    }

    public void setGpgga(GspBase gpgga) {
        this.gpgga = gpgga;
    }

    public GspBase getGpgll() {
        return gpgll;
    }

    public void setGpgll(GspBase gpgll) {
        this.gpgll = gpgll;
    }

    public GspBase getGpgsa() {
        return gpgsa;
    }

    public void setGpgsa(GspBase gpgsa) {
        this.gpgsa = gpgsa;
    }

    public GspBase getGpgsv() {
        return gpgsv;
    }

    public void setGpgsv(GspBase gpgsv) {
        this.gpgsv = gpgsv;
    }

    public GspBase getGprmc() {
        return gprmc;
    }

    public void setGprmc(GspBase gprmc) {
        this.gprmc = gprmc;
    }

    public GspBase getGpvtg() {
        return gpvtg;
    }

    public void setGpvtg(GspBase gpvtg) {
        this.gpvtg = gpvtg;
    }


}
