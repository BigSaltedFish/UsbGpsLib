package com.zy.usbgpslib.entity;

import java.util.Date;

public class GpsInfo {
    //纬度
    private double latitude;
    //经度
    private double longitude;
    //纬度方向
    private String latitudeDirection;
    //经度方向
    private String longitudeDirection;
    //定位状态
    private String locationStatus;

    private String utcTime;

    private String utcDate;
    //可见卫星总数
    private int visibleSatelliteCount;

    private Date date;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLatitudeDirection() {
        return latitudeDirection;
    }

    public void setLatitudeDirection(String latitudeDirection) {
        this.latitudeDirection = latitudeDirection;
    }

    public String getLongitudeDirection() {
        return longitudeDirection;
    }

    public void setLongitudeDirection(String longitudeDirection) {
        this.longitudeDirection = longitudeDirection;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public String getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(String utcTime) {
        this.utcTime = utcTime;
    }

    public String getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(String utcDate) {
        this.utcDate = utcDate;
    }

    public int getVisibleSatelliteCount() {
        return visibleSatelliteCount;
    }

    public void setVisibleSatelliteCount(int visibleSatelliteCount) {
        this.visibleSatelliteCount = visibleSatelliteCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
