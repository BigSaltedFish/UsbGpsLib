package com.zy.usbgpslib.util;


import com.zy.usbgpslib.entity.GPGGA;
import com.zy.usbgpslib.entity.GPGLL;
import com.zy.usbgpslib.entity.GPGSA;
import com.zy.usbgpslib.entity.GPGSV;
import com.zy.usbgpslib.entity.GPRMC;
import com.zy.usbgpslib.entity.GPVTG;
import com.zy.usbgpslib.entity.GspBase;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GpsParser {
    private static final String GPGGA_REGEX = "\\$G[PN]GGA\\S+\\r\\n";
    private static final String GPGLL_REGEX = "\\$G[PN]GLL\\S+\\r\\n";
    private static final String GPGSA_REGEX = "\\$G[PN]GSA\\S+\\r\\n";
    private static final String GPGSV_REGEX = "\\$G[PN]GSV\\S+\\r\\n";
    private static final String GPRMC_REGEX = "\\$G[PN]RMC\\S+\\r\\n";
    private static final String GPVTG_REGEX = "\\$G[PN]VTG\\S+\\r\\n";

    private static Pattern GPGGA_PATTERN = Pattern.compile(GPGGA_REGEX);
    private static Pattern GPGLL_PATTERN = Pattern.compile(GPGLL_REGEX);
    private static Pattern GPGSA_PATTERN = Pattern.compile(GPGSA_REGEX);
    private static Pattern GPGSV_PATTERN = Pattern.compile(GPGSV_REGEX);
    private static Pattern GPRMC_PATTERN = Pattern.compile(GPRMC_REGEX);
    private static Pattern GPVTG_PATTERN = Pattern.compile(GPVTG_REGEX);




    public static GspBase parse(String msg){

        String rlt = match(GPGGA_PATTERN,msg);
        if(!"".equals(rlt)){
            return parse(GPGGA.class,rlt);
        }
        rlt = match(GPGLL_PATTERN,msg);
        if(!"".equals(rlt)){
            return parse(GPGLL.class,rlt);
        }
        rlt = match(GPGSA_PATTERN,msg);
        if(!"".equals(rlt)){
            return parse(GPGSA.class,rlt);
        }
        rlt = match(GPGSV_PATTERN,msg);
        if(!"".equals(rlt)){
            return parse(GPGSV.class,rlt);
        }
        rlt = match(GPRMC_PATTERN,msg);
        if(!"".equals(rlt)){
            return parse(GPRMC.class,rlt);
        }
        rlt = match(GPVTG_PATTERN,msg);
        if(!"".equals(rlt)){
            return parse(GPVTG.class,rlt);
        }
        return null;
    }
    private static String match(Pattern pattern,String msg){
        String rlt = "";
        Matcher matcher = pattern.matcher(msg);
        if(matcher.find()){
            rlt = matcher.group();
            return rlt;
        }
        return rlt;
    }

    public static <T extends GspBase> T parse(Class<T> clazz, String filterStr) {
        try{
            Map<String,String> map = buildMap(filterStr);
            T t = clazz.newInstance();
            for(String key:map.keySet()){
                String methodSuffix = StringUtils.toUpperCaseFirstOne(key);
                Method m = clazz.getSuperclass().getDeclaredMethod("set"+ methodSuffix,String.class);
                m.setAccessible(true);
                m.invoke(t,map.get(key));
            }
            return t;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public static Map<String,String> buildMap(String filterStr){
        String array[] = filterStr.split(",");
        Map<String,String> map = new HashMap<>();
        for(int i=0;i<array.length;i++){
            if(i == 0){
                continue;
            }
            String arr = array[i];
            if(!StringUtils.checkEmpty(arr)){
                map.put("arr"+(i),arr);
            }
        }
        return map;
    }
}
