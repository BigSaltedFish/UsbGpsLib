package com.zy.usbgpslib.enums;

public enum LocationStatusEnum {
    VALID('A',"定位"),
    INVALID('V',"未定位");
    char key;

    String value;

    LocationStatusEnum(char key,String value){
        this.key = key;
        this.value = value;
    }
}
