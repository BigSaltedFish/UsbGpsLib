package com.zy.usbgpslib

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import io.ztc.appkit.view.Toolbar.ToolbarConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SDKInitializer.initialize(applicationContext)
        SDKInitializer.setCoordType(CoordType.BD09LL)
        ToolbarConfig.init()
    }

}