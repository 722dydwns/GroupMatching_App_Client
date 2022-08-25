package com.example.appgrouppurchasemaching

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.appgrouppurchasemaching.databinding.ActivityServiceBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class ServiceActivity : AppCompatActivity() , OnMapReadyCallback { //서비스 제공 액티비티
    //binding 설정
    lateinit var binding : ActivityServiceBinding

    // 허용받을 권한 목록
    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    lateinit var manager : LocationManager
    lateinit var locationListener : LocationListener
    lateinit var googleMap : GoogleMap
    var myMarker : Marker? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding 처리
        binding = ActivityServiceBinding.inflate(layoutInflater)
        binding.mapToolbar.title = "Google Map 현재위치"
        //툴바에 메뉴 추가
        binding.mapToolbar.inflateMenu(R.menu.map_menu)

        binding.mapToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.main_menu_location ->{
                    getMyLocation()
                    true
                }
                else -> false
            }
        }

        setContentView(binding.root)

        //권한 요청하기 사용자에게
        requestPermissions(permission_list, 0)

        // 맵의 상태가 변경되면 호출될 메서드가 구현되어 있는 곳을 등록한다.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    // 지도가 준비가 완료되면 호출되는 메서드
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        //구글 지도의 일부 옵션 설정을 위해 권환 확인 코드 추가한다.
        val a1 = Manifest.permission.ACCESS_FINE_LOCATION
        val a2 = Manifest.permission.ACCESS_COARSE_LOCATION

        if(ActivityCompat.checkSelfPermission(this, a1) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, a2) == PackageManager.PERMISSION_GRANTED){

            //확대 축소 버튼 옵션
            googleMap.uiSettings.isZoomControlsEnabled = true
            //구글 제공 - 현재 위치 표시
            googleMap.isMyLocationEnabled = true

            //현재 위치 표시 마커 중복 제거
            //googleMap.uiSettings.isMyLocationButtonEnabled = false

        }

        getMyLocation() //현재 위치 측정 메소드 호출
    }

    // '현재 위치 측정'하는 메서드
    fun getMyLocation(){
        // 위치 정보를 관리하는 매니저를 추출한다.
        manager = getSystemService(LOCATION_SERVICE) as LocationManager

        // 저장되어 있는 위치값이 있으면 가져온다.
        val a1 = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        val a2 = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

        if (a1 && a2) {
            return
        }
        //getLastKnownLocation()
        val location1 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val location2 = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        // 새로운 위치 측정을 요청
        locationListener = LocationListener {
            setUserLocation(it)
        }

        if(location1 != null){
            setUserLocation(location1)
        } else if(location2 != null){
            setUserLocation(location2)
        }

        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true){
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                0f, locationListener)
        } else if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true){
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0f, locationListener)
        }
    }

    // 위치 값을 받아 지도를 이동시킨다.
    fun setUserLocation(location:Location){
        // 위치 측정을 중단한다.
        manager.removeUpdates(locationListener)

        // 위도와 경도값을 관리하는 객체
        val loc1 = LatLng(location.latitude, location.longitude)
        // 지도를 이동시키기 위한 객체를 생성한다.
        val loc2 = CameraUpdateFactory.newLatLngZoom(loc1, 15f)
        // 이동한다.
        googleMap.animateCamera(loc2)

    }

}







