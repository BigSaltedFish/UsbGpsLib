package com.zy.usbgpslib.enums;

public enum GpsDirectionEnum {
    NORTH("N","北纬"),
    WEST("W","西经"),
    EAST("E","东经"),
    SOUTH("S","南纬");

     String key;
     String value;
    GpsDirectionEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static GpsDirectionEnum get(String key){
        for(GpsDirectionEnum gpsDirectionEnum: GpsDirectionEnum.values()){
            if(gpsDirectionEnum.getKey().equals(key)){
                return gpsDirectionEnum;
            }
        }
        return null;
    }
}
