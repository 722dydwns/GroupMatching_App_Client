package com.example.appgrouppurchasemaching.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class MyLocationService : Service() { // 위치 서비스

    lateinit var manager : LocationManager
    lateinit var locationListener: LocationListener

    var binder = MyLocationServiceBinder()
    var myLocation : Location? = null //사용자 현재 위치 변수

    override fun onBind(intent: Intent): IBinder {
       return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("myLocationService", "myLocationService",
                                                NotificationManager.IMPORTANCE_HIGH)
            //알림 띄우기
            manager.createNotificationChannel(channel)
            val builder = NotificationCompat.Builder(this, "myLocationService")
            builder.setSmallIcon(android.R.drawable.ic_menu_mylocation)
            builder.setContentTitle("현재 위치 측정 중")
            val notifiaction = builder.build()
            startForeground(10, notifiaction)
        }

        //위치 측정
        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        val a1 = Manifest.permission.ACCESS_FINE_LOCATION
        val a2 = Manifest.permission.ACCESS_COARSE_LOCATION

        if(ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_DENIED
            || ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_DENIED) {

            return super.onStartCommand(intent, flags, startId)
        }

        val location1 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val location2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        locationListener = LocationListener {
            getUserLocation(it) // 사용자 위치 정보 가져옴
        }

        if(location1 != null) {
            Log.d("test_location", "current is location1(GPS): ${location1.latitude}, ${location1.longitude}")
            getUserLocation(location1)
        }else if(location2 != null) {
            Log.d("test_location", "current is location2(NETWORK): ${location2.latitude}, ${location2.longitude}")
            getUserLocation(location2)
        }

        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        } else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    //현재 사용자 위치 '측정' 메소드
    fun getUserLocation(location : Location){
        //Log.d("test", "${location.longitude}, ${location.latitude}")

        myLocation = location //현재 사용자 위치를 변수에 담아두고
    }

    //현재 사용자 위치 '반환' 메소드
    fun returnUserLocation() : Location? {
        return myLocation //현재 위치 반환
    }

    //이 서비스 중단될 경우 자동 호출
    override fun onDestroy() {
        super.onDestroy()
        manager.removeUpdates(locationListener)
    }

    //접속하는 Activity에서 서비스 추출을 위해 사용하는 클래스
    inner class MyLocationServiceBinder : Binder() {
        fun getService() : MyLocationService{
            return this@MyLocationService
        }
    }

}