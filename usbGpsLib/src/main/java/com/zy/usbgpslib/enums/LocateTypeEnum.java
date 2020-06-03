package com.zy.usbgpslib.enums;

public enum LocateTypeEnum {
    WEIDINGWEI(1,"未定位"),
    DINGWEI2D(2,"2D定位"),
    DINGWEI3D(3,"3D定位");
    int key;
    String desc;

    LocateTypeEnum(int key,String desc){
        this.key = key;
        this.desc = desc;
    }
}
