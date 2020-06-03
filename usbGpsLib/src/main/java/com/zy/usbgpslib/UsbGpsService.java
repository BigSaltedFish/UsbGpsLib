package com.zy.usbgpslib;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.zy.usbgpslib.driver.UsbSerialDriver;
import com.zy.usbgpslib.driver.UsbSerialPort;
import com.zy.usbgpslib.driver.UsbSerialProber;
import com.zy.usbgpslib.entity.GpsInfo;
import com.zy.usbgpslib.entity.GspBase;
import com.zy.usbgpslib.entity.GspWrapper;
import com.zy.usbgpslib.listener.UsbGpsListener;
import com.zy.usbgpslib.util.GpsParser;
import com.zy.usbgpslib.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;

public class UsbGpsService  implements SerialInputOutputManager.Listener{
    private static final String TAG = "UsbGpsService";
    private UsbManager usbManager;
    private UsbSerialPort usbSerialPort;
    private static UsbGpsService instance;

    public static UsbGpsService getInstance(){
        if(null == instance){
            synchronized (UsbGpsService.class){
                if (null == instance)
                    instance = new UsbGpsService();
            }
        }
        return instance;
    }
    @Override
    public void onNewData(byte[] data) {
        mainLooper.post(() -> {
            receive(data);
        });
    }
    GspWrapper gspWrapper = new GspWrapper();
    private void receive(byte[] data) {
        if(data.length > 0){
            String msg = new String(data);
            Log.i(TAG, "receive: "+msg);
            GspBase gspBase = GpsParser.parse(msg);
            if(null != gspBase){
                gspWrapper.setValue(gspBase);
            }
            if(gspWrapper.complete()){
                GpsInfo gpsInfo = gspWrapper.buildGpsInfo();
                if(null != listener){
                    listener.onRead(gpsInfo);
                }
                gspWrapper.reset();
            }
        }
    }
    @Override
    public void onRunError(Exception e) {
        mainLooper.post(() -> {
            if(null != listener){
                listener.onFailed(e.getMessage());
            }
        });
    }

    private enum UsbPermission { Unknown, Requested, Granted, Denied };
    private UsbPermission usbPermission = UsbPermission.Unknown;
    private SerialInputOutputManager usbIoManager;
    private static final String INTENT_ACTION_GRANT_USB = "com.zy.usbgps.GRANT_USB";
    private boolean connected = false;
    private Handler mainLooper   = new Handler(Looper.getMainLooper());
    private Context context;
    private UsbGpsService() {
    }
    public void start(Context context, UsbGpsListener listener){
        this.context = context;
        this.listener = listener;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(INTENT_ACTION_GRANT_USB);
        context.registerReceiver(broadcastReceiver, intentFilter);
        if(usbPermission == UsbPermission.Unknown || usbPermission == UsbPermission.Granted)
            connect();
    }
    public void onDestroy(){
        if(connected){
            disconnect();
        }
        context.unregisterReceiver(broadcastReceiver);
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(INTENT_ACTION_GRANT_USB)) {
                usbPermission = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                        ? UsbPermission.Granted : UsbPermission.Denied;
                connect();
            }else if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
                connect();
                if(null != listener){
                    listener.onUsbAttached();
                }
            }else if(intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)){
                if(connected){
                    disconnect();
                    if(null != listener){
                        listener.onUsbDetached();
                    }
                }
            }
        }
    };
    private void connect() {
        UsbDevice device = null;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        Map<String,UsbDevice> map  = usbManager.getDeviceList();
        for(String key:map.keySet()){
            device = map.get(key);
            break;
        }
        if(device == null) {
            if(null != listener){
                listener.onFailed("为获取到外接设备");
            }

            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            if(null != listener){
                listener.onFailed("驱动为空");
            }
            return;
        }
        if(driver.getPorts().size() < 0) {
            if(null != listener){
                listener.onFailed("端口为空");
            }
            return;
        }
        usbSerialPort = driver.getPorts().get(0);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && usbPermission == UsbPermission.Unknown && !usbManager.hasPermission(driver.getDevice())) {
            usbPermission = UsbPermission.Requested;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(INTENT_ACTION_GRANT_USB), 0);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if(null != listener){
                listener.onFailed("usb连接为空");
            }
            return;
        }

        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            usbIoManager = new SerialInputOutputManager(usbSerialPort, this);
            Executors.newSingleThreadExecutor().submit(usbIoManager);
            connected = true;
        } catch (Exception e) {
            disconnect();
        }
    }

    private void disconnect() {
        connected = false;
        if(usbIoManager != null)
            usbIoManager.stop();
        usbIoManager = null;
        try {
            usbSerialPort.close();
        } catch (IOException ignored) {}
        usbSerialPort = null;
        usbPermission = UsbPermission.Unknown;
    }

    private UsbGpsListener listener;

}
