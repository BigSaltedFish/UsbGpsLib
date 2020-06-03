package com.zy.usbgpslib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TextView receiveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiveText = findViewById(R.id.receive_text);
        UsbGpsService.getInstance().start(this, new UsbGpsListener() {
                    @Override
                    public void onFailed(String msg) {
                        Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onRead(GpsInfo gpsInfo) {
                        SpannableStringBuilder spn = new SpannableStringBuilder();
                        spn.append(gpsInfo.getLatitudeDirection()+":");
                        spn.append(gpsInfo.getLatitude()+"\r\n");
                        receiveText.append(spn);
                    }

                    @Override
                    public void onUsbAttached() {
                        SpannableStringBuilder spn = new SpannableStringBuilder();
                        spn.append("usb设备已插入\r\n");
                        receiveText.append(spn);
                    }

                    @Override
                    public void onUsbDetached() {
                        SpannableStringBuilder spn = new SpannableStringBuilder();
                        spn.append("usb设备已拔出\r\n");
                        receiveText.append(spn);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        UsbGpsService.getInstance().onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
    }
}