package com.zy.usbgpslib.listener;


import com.zy.usbgpslib.entity.GpsInfo;

public interface UsbGpsListener {

    void onFailed(String msg);

    void onRead(GpsInfo gpsInfo);

    void onUsbAttached();

    void onUsbDetached();
}
