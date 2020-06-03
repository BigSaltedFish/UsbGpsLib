package com.zy.usbgpslib.util;

public class StringUtils {

    public static boolean checkEmpty(String s){
        if(null != s && !"".equals(s)){
            return false;
        }
        return true;
    }
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
