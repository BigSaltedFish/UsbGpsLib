package com.zy.usbgpslib.enums;

public enum GPGLLStatusEnum {
    DINGWEI("A","定位"),
    WEIDINGWEI("V","未定位");

    String key;
    String value;
    GPGLLStatusEnum(String key, String value){
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

    public static GPGLLStatusEnum get(String key){
        for(GPGLLStatusEnum gpgllStatusEnum: GPGLLStatusEnum.values()){
            if(gpgllStatusEnum.getKey().equals(key)){
                return gpgllStatusEnum;
            }
        }
        return null;
    }
}
