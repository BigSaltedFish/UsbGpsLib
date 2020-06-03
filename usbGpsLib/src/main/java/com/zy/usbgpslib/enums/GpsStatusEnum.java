package com.zy.usbgpslib.enums;

public enum GpsStatusEnum {
    WEIDINGWEI(0,"未定位"),
    FEICHAFENDINGEI(1,"非差分定位"),
    CHAFENDINGWEI(2,"差分定位"),
    WUXIAOPPS(3,"无效PPS"),
    ZHENGZAIGUSUAN(6,"正在估算");
    int key;
    String value;

    GpsStatusEnum(int key, String value){
        this.key = key;
        this.value  = value;
    }
}
