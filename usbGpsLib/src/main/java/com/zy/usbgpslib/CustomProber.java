package com.zy.usbgpslib;


import com.zy.usbgpslib.driver.CdcAcmSerialDriver;
import com.zy.usbgpslib.driver.ProbeTable;
import com.zy.usbgpslib.driver.UsbSerialProber;

/**
 * add devices here, that are not known to DefaultProber
 *
 * if the App should auto start for these devices, also
 * add IDs to app/src/main/res/xml/device_filter.xml
 */
public class CustomProber {

    public static UsbSerialProber getCustomProber() {
        ProbeTable customTable = new ProbeTable();
        customTable.addProduct(0x1546, 0x01A8, CdcAcmSerialDriver.class); // e.g. Digispark CDC
        return new UsbSerialProber(customTable);
    }

}
