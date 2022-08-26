package com.example.appgrouppurchasemaching.service

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.ActivityCompat
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityServiceBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlin.concurrent.thread

class ServiceActivity : AppCompatActivity() , OnMapReadyCallback { //서비스 제공 액티비티
    //binding 설정
    lateinit var binding: ActivityServiceBinding

    lateinit var manager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var googleMap: GoogleMap
    var myMarker: Marker? = null

    //서비스 intent 변수
    lateinit var serviceIntent: Intent

    //IPC 사용
    var ipcService: MyLocationService? = null
    var serviceRunning = false //현재 서비스 실행 중 여부 변수
    var myLocation : Location? = null

    //서비스 접속 관리 객체
    val connection = object : ServiceConnection {
        //서비스 접속 시
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //사용할 서비스 추출한다.
            val binder = service as MyLocationService.MyLocationServiceBinder
            ipcService = binder.getService() //변수에 담아주고
        }

        //서비스 접속 해제 시
        override fun onServiceDisconnected(name: ComponentName?) {
            ipcService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 처리
        binding = ActivityServiceBinding.inflate(layoutInflater)
        binding.mapToolbar.title = "Google Map 현재 위치 확인"

        setContentView(binding.root)

        // 맵의 상태가 변경되면 호출될 메서드가 구현되어 있는 곳을 등록한다.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //서비스 가동 중 아닌 경우를 확인한 뒤 ->  서비스 가동시킨다.
        val chk = isServiceRunning("com.example.appgrouppurchasemaching.MyLocationService")
        serviceIntent = Intent(this, MyLocationService::class.java)

        if (chk == false) { //접속 X
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent) //백그라운드에서 실행
            } else { //접속 O
                startService(serviceIntent) //서비스가 실행
            }
        }

        //서비스에 접속한다.
        bindService(serviceIntent, connection, BIND_AUTO_CREATE)

    }

    // 지도가 준비가 완료되면 호출되는 메서드
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        // 구글 지도의 옵션 설정을 위해 권한을 확인한다.
        val a1 = Manifest.permission.ACCESS_FINE_LOCATION
        val a2 = Manifest.permission.ACCESS_COARSE_LOCATION

        if (ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_GRANTED
        ) {

            // 확대 축소 버튼
            googleMap.uiSettings.isZoomControlsEnabled = true

            // 현재 위치를 표시한다.
            googleMap.isMyLocationEnabled = true

        }

        //서비스에서 현 위치값을 가져오는 쓰레드 가동시키기
        serviceRunning = true

        thread {
            while (serviceRunning) {
                SystemClock.sleep(1000) //1초마다
                myLocation = ipcService?.returnUserLocation()

                runOnUiThread {
                    if (myLocation != null) {
                        setUserLocation(myLocation!!, true)
                        serviceRunning = false
                    }
                }
            }
        }
    }

    fun setUserLocation(location:Location, zoom : Boolean){

        // 위도와 경도값을 관리하는 객체
        val loc1 = LatLng(location.latitude, location.longitude)
        if(zoom == true) {
            // 지도를 이동시키기 위한 객체를 생성한다.
            val loc2 = CameraUpdateFactory.newLatLngZoom(loc1, 15f)
            // 이동한다.
            googleMap.animateCamera(loc2)
        } else {
            val loc2 = CameraUpdateFactory.newLatLng(loc1)
            googleMap.animateCamera(loc2)
        }

    }

    //서비스 가동 여부 확인 메소드
    fun isServiceRunning(name : String) : Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        //현재 실행 중인 서비스를 가져온다.
        val serviceList = manager.getRunningServices(Int.MAX_VALUE)

        for(serviceInfo in serviceList) {
            //서비스의 이름이 원하는 이름인지 확인
            if(serviceInfo.service.className == name) {
                return true
            }
       }
        return false
    }

    //액티비티 종료 시 서비스 종료시킴
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection) //서비스 접속 해제시킴
        stopService(serviceIntent)
    }

}







