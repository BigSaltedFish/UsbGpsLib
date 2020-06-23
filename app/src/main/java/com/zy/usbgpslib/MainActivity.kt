package com.zy.usbgpslib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.widget.Toast
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.CoordinateConverter
import com.zy.usbgpslib.entity.GpsInfo
import com.zy.usbgpslib.listener.UsbGpsListener
import io.ztc.appkit.appBase.activity.AppActivity
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppActivity(), EasyPermissions.PermissionCallbacks {

    private var isFrist = true;

    private val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )

    companion object{
        var mapView: BaiduMap? = null
    }

    override fun initContext(): Any {
        return this
    }

    override fun layout(): Int {
        return R.layout.activity_main
    }

    override fun defaultAction() {
        getPermission()
    }


    override fun initView() {
        initToolbar(R.id.toolbar).title("UbGpsLib")
        UsbGpsService.getInstance().start(this, object : UsbGpsListener {
            @SuppressLint("ShowToast")
            override fun onFailed(msg: String) {
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }

            override fun onRead(gpsInfo: GpsInfo) {
                initMap(gpsInfo.latitude,gpsInfo.longitude)
                val spn = SpannableStringBuilder()
                if (gpsInfo.latitudeDirection?.toString()!=null){
                    spn.append(gpsInfo.latitudeDirection?.toString()).append(":")
                    spn.append(gpsInfo.latitude?.toString()).append("\r\n")
                }
                if (gpsInfo.longitudeDirection?.toString()!=null){
                    spn.append(gpsInfo.longitudeDirection?.toString()).append(":")
                    spn.append(gpsInfo.longitude?.toString()).append("\r\n")
                }
                spn.append("定位状态:").append(gpsInfo.locationStatus?.toString()).append("\r\n")
                spn.append("可见卫星数:").append(gpsInfo.visibleSatelliteCount?.toString()).append("\r\n")
                receiveText.text = spn
            }

            override fun onUsbAttached() {
                val spn = SpannableStringBuilder()
                spn.append("usb设备已插入\r\n")
                receiveText.append(spn)
            }

            override fun onUsbDetached() {
                val spn = SpannableStringBuilder()
                isFrist = true
                spn.append("usb设备已拔出\r\n")
                receiveText.append(spn)
            }
        }
        )
    }

    var mCircleOptions:CircleOptions? = null
    var option:OverlayOptions? = null
    var mapOptin:MapStatus? = null
    var mapUpdate:MapStatusUpdate? = null

    fun initMap(lat:Double?,lng:Double?){
        if (lat!=null && lng!=null){
            mapView = ZzMap.map
            val converter = CoordinateConverter()
                    .from(CoordinateConverter.CoordType.GPS)
                    .coord(LatLng(lat, lng))
            //转换坐标
            val desLatLng: LatLng = converter.convert()
            mapOptin = MapStatus.Builder()
                    .target(desLatLng)
                    .zoom(16F)
                    .build()
            mapUpdate = MapStatusUpdateFactory.newMapStatus(mapOptin)
            //构造CircleOptions对象
            mCircleOptions = CircleOptions().center(desLatLng)
                    .radius(10)
                    .fillColor(-0x33333333) //填充颜色
                    .stroke(Stroke(5, -0x33333333)) //边框宽和边框颜色
            //在地图上显示圆

            //构建Marker图标
            val bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.dw)
            //构建MarkerOption，用于在地图上添加Marker
            option = MarkerOptions()
                    .position(desLatLng)
                    .icon(bitmap)
            //在地图上添加Marker，并显示
            //ZzMap.removeAllViews()
            mapView!!.clear()
            mapView!!.addOverlay(mCircleOptions)
            mapView!!.addOverlay(option)
            if (isFrist){
                mapView!!.setMapStatus(mapUpdate)
                isFrist = false
            }
        }
    }


    override fun initListener() {
        dw.setOnClickListener {
            mapView!!.setMapStatus(mapUpdate)
        }
    }

    //获取权限
    private fun getPermission() {
        if (EasyPermissions.hasPermissions(this, *permissions)) {
            //已经打开权限
            //Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show()
            val handler = Handler()
            if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT == 0){
                finish()
            }
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取相关设备权限 请在设置中打开", 1, *permissions)
        }
    }


    override fun onResume() {
        super.onResume()
        //在activity执行onResume时必须调用mMapView. onResume ()
        ZzMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时必须调用mMapView. onPause ()
        ZzMap.onPause()
    }

    override fun onDestroy() {
        UsbGpsService.getInstance().onDestroy()
        super.onDestroy()
        ZzMap.onDestroy()
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(p0: Int, p1: MutableList<String>) {
        Toast.makeText(this, "请同意相关权限，否则应用无法使用", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsGranted(p0: Int, p1: MutableList<String>) {
        Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show()
    }
}