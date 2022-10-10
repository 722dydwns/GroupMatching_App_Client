package com.example.appgrouppurchasemaching.service

import android.Manifest
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.*
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityServiceBinding
import com.example.appgrouppurchasemaching.matching.MyLikeListActivity
import com.example.appgrouppurchasemaching.message.ChatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ServiceActivity : AppCompatActivity() , OnMapReadyCallback { //서비스 제공 액티비티
    //binding 설정
    lateinit var binding: ActivityServiceBinding

    lateinit var manager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var googleMap: GoogleMap

    var map_info : MapInfoModel ?= null

    //API에서 받은 데이터들 담을 리스트 변수 선언
    var nearby_lat = ArrayList<Double>()
    var nearby_lng = ArrayList<Double>()
    var nearby_name = ArrayList<String>()
    var nearby_vicinity = ArrayList<String>()
    //마커 초기화를 위해 마커들도 리스트에 담을 예정
    var nearby_marker_list = ArrayList<Marker>()

    //서비스 intent 변수
    lateinit var serviceIntent: Intent

    // -> 다이얼로그 띄울 목록 arrayOf() 생성
    val dialogData = arrayOf(
        "accounting",
         "atm", "bakery", "apartment",
        "bank", "bar", "beauty_salon", "bicycle_store",
        "book_store", "bowling_alley", "bus_station",
        "cafe", "car_dealer", "car_rental",
        "car_repair", "car_wash", "casino", "cemetery",
        "church", "city_hall", "clothing_store", "convenience_store",
        "courthouse", "dentist", "department_store", "doctor",
        "drugstore", "electrician", "electronics_store", "embassy",
        "fire_station", "florist", "funeral_home", "furniture_store",
        "gas_station", "gym", "hair_care", "hardware_store", "hindu_temple",
        "home_goods_store", "hospital", "insurance_agency",
        "jewelry_store", "laundry", "lawyer", "library", "light_rail_station",
        "liquor_store", "local_government_office", "locksmith", "lodging",
        "meal_delivery", "meal_takeaway", "mosque", "movie_rental", "movie_theater",
        "moving_company", "museum", "night_club", "painter", "park", "parking",
        "pet_store", "pharmacy", "physiotherapist", "plumber", "police", "post_office",
        "primary_school", "real_estate_agency", "restaurant", "roofing_contractor",
        "rv_park", "school", "secondary_school", "shoe_store", "shopping_mall",
        "spa", "stadium", "storage", "store", "subway_station", "supermarket",
        "synagogue", "taxi_stand", "tourist_attraction", "train_station",
        "transit_station", "travel_agency", "university", "eterinary_care","zoo"
    )

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

        //채팅창 정보 값 얻기
        val intent = getIntent()
        val OtherUid = intent.getStringExtra("OtherUid")
        val OtherName = intent.getStringExtra("OtherName")

        //binding 처리
        binding = ActivityServiceBinding.inflate(layoutInflater)
        binding.mapToolbar.title = "약속장소 정하기"

        binding.mapToolbar.inflateMenu(R.menu.map_menu)
        binding.mapToolbar.setOnMenuItemClickListener{
            when(it.itemId) {
                R.id.main_menu_place ->{
                        val placeListBuilder = AlertDialog.Builder(this)
                        placeListBuilder.setTitle("장소 종류 선택")
                        placeListBuilder.setNegativeButton("취소", null)
                        placeListBuilder.setNeutralButton("초기화"){dialogInterface, i ->
                            //데이터 리스트들 초기화
                            nearby_lat.clear()
                            nearby_lng.clear()
                            nearby_name.clear()
                            nearby_vicinity.clear()

                            for(m in nearby_marker_list){ //마커도 for문 돌며 차례로 제거
                                m.remove()
                            }
                            nearby_marker_list.clear()

                        }
                        //다이얼로그 목록 세팅해주고
                        placeListBuilder.setItems(dialogData) {dialogInterface, i ->
                            //데이터 리스트들 초기화
                            nearby_lat.clear()
                            nearby_lng.clear()
                            nearby_name.clear()
                            nearby_vicinity.clear()

                            for(m in nearby_marker_list){ //마커도 for문 돌며 차례로 제거
                                 m.remove()
                            }
                            nearby_marker_list.clear()

                            getNearbyPlaceData(dialogData[i]) //i번째 데이터 넘겨줌
                        }
                        //띄우기
                        placeListBuilder.show()
                    true
                }

               //뒤로가기 연결

                else -> false
            }
        }

        //'장소선택' 버튼 클릭 시, 이벤트 처리 [마커로 찍거나/검색한 장소값을 보내주어야 함]
        binding.promiseBtn.setOnClickListener {

            //채팅창에서 지도 눌러서 넘어온 값인 경우
            if(OtherName != null && OtherUid != null) {
                val Intent = Intent(this, ChatActivity::class.java)
                Intent.putExtra("name", OtherName)
                Intent.putExtra("uid", OtherUid)
                //여기서 map_info 객체의 값을 전달해주거나

                if (map_info != null) {
                    Log.d("share_location", "map_info is not null")
                    Intent.putExtra("mapInfo", map_info)
                    Log.d("share_location", "Intent 에 map_info 넘겨줌. map_info.marker_title: ${map_info?.marker_title}")
                }
                startActivity(Intent)

            }else{ //메뉴컨트롤러 화면에서 넘어온 지도의 경우에는 채팅창 정보가 없다.
                //메시지 띄우고

                Toast.makeText(this@ServiceActivity, "채팅창 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                //매칭 리스트 화면으로 전환 시킬 것
                val intent = Intent(this, MyLikeListActivity::class.java)
                startActivity(intent)

                //여기서 map_info 객체의 값을 전달해주거나
                finish()
            }
        }

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

    /**
    fun setLocationLatLngIfThisClickedByMessage() {
        val intent = getIntent()

        if (intent.hasExtra("isClickedByMessage")) {
            // 메세지에서 공유된 위치를 클릭해서 들어온 경우
            Log.d("test", "intent.hashExtra(\"isClickedByMessage\"): ${intent.hasExtra("isClickedByMessage")}")

            // "장소선택" 버튼 숨김
            binding.promiseBtn.visibility = View.GONE

            Log.d("test", "this::googleMap.isInitialized: ${this::googleMap.isInitialized}")
            if (this::googleMap.isInitialized) {
                val latitude = intent.getDoubleExtra("latitude", 35.0)
                val longitude = intent.getDoubleExtra("longitude", 35.0)
                searchLocationByLatLng(latitude, longitude)
            }
        }
    }
    */

    // 지도가 준비가 완료되면 호출되는 메서드
    override fun onMapReady(p0: GoogleMap) {
        Log.d("test", "onMapReady called")
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


        /**
        val intent = getIntent()
        if (intent.hasExtra("isClickedByMessage")) {
            setLocationLatLngIfThisClickedByMessage()
        } else {
        */
            //서비스에서 현 위치값을 가져오는 쓰레드 가동시키기
            serviceRunning = true
            thread {
                while (serviceRunning) {
                    SystemClock.sleep(1000) //1초마다
                    myLocation = ipcService?.returnUserLocation()

                    runOnUiThread {
                        if (myLocation != null) {
                            setUserLocation(myLocation!!, true)
                            //현 위치 로그 찍기
                            Log.d("test", myLocation.toString())

                            serviceRunning = false
                        }
                    }
                }
            }
        /**
        }
        */
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

    // ->장소 선택 각 항목 터치 시, API에서 관련 데이터 가져오는 함수
    fun getNearbyPlaceData(type: String) { //매개변수로 받은(사용자 선택값)

        //네트워크와 데이터 처리할 땐 thread 필수
        thread {
            //요청할 API site 주소
            var site = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
            site += "?location=${myLocation?.latitude},${myLocation?.longitude}"
            site += "&radius=500000&type=${type}"
            site += "&key=AIzaSyB7jqeJDg21reY8e60ycR-qVGtMfuVn-ZU&language=ko"


            //API 주소에서 데이터 가져오기
            val url = URL(site)
            val conn = url.openConnection() as HttpURLConnection

            //데이터 읽어온다.
            val isr = InputStreamReader(conn.inputStream, "UTF-8")
            val br = BufferedReader(isr)

            var str:String? = null
            val buf = StringBuffer()

            //반복문 돌면서 데이터 읽어들이기
            do{
                str = br.readLine()
                if(str != null) {
                    buf.append(str)
                }
            }while (str != null)

            val data = buf.toString()
            //Log.d("test", data)

            //JSON 객체 생성
            val root = JSONObject(data)
            // status == OK 일 때만 가져옴
            if(root.getString("status") == "OK") {
                val results = root.getJSONArray("results")
                for(i in 0 until results.length()) {
                    val results_item = results.getJSONObject(i)
                    val geometry = results_item.getJSONObject("geometry")

                    val location = geometry.getJSONObject("location")
                    val lat = location.getDouble("lat")
                    val lng = location.getDouble("lng")
                    val name = results_item.getString("name")
                    val vicinity = results_item.getString("vicinity")

                    //로컬 변수 리스트에 받은 데이터들 다시 담기
                    nearby_lat.add(lat)
                    nearby_lng.add(lng)
                    nearby_name.add(name)
                    nearby_vicinity.add(vicinity)

                    //화면 처리
                    runOnUiThread{
                        for(i in 0 until nearby_lat.size) {
                            val loc = LatLng(nearby_lat[i], nearby_lng[i])

                            var placeMarkerOptions = MarkerOptions()
                            placeMarkerOptions.position(loc)
                            placeMarkerOptions.title(nearby_name[i])
                            placeMarkerOptions.snippet(nearby_vicinity[i])

                            //구글맵 마커 추가시킴
                            val placeMarker = googleMap.addMarker(placeMarkerOptions)
                            nearby_marker_list.add(placeMarker!!)

                            //마커 클릭 리스너
                            googleMap.setOnMarkerClickListener (object: GoogleMap.OnMarkerClickListener{
                                override fun onMarkerClick(p0: Marker): Boolean {
                                    //토스트 메시지 띄우기
                                    Toast.makeText(this@ServiceActivity, p0.title + p0.snippet , Toast.LENGTH_SHORT).show()


                                    map_info = MapInfoModel(p0.position, p0.title.orEmpty(), p0.snippet.orEmpty()) //지도 정보 객체에 담음
                                    Log.d("share_location", "map_info 객체 만들어짐! position: ${map_info?.marker_position.toString()}")

                                    return false
                                }
                            })

                            }
                        }
                    }

                }
            }

        }


    //지도 장소 검색 메소드 - view 의 '검색'버튼 클릭시 호출되도록 onCLick 연결
    fun searchLocation(view: View) {
        val locationSearch: EditText = binding.etSearch
        var location:String

        location = locationSearch.text.toString().trim()
        var addressList :List<Address>? = null

        if(location == null || location == "") {
            Toast.makeText(this, "provide location", Toast.LENGTH_SHORT).show()

        }else{
            val geoCoder = Geocoder(this)
            try{
                addressList = geoCoder.getFromLocationName(location, 1)
            }catch(e:IOException){
                e.printStackTrace()
            }

            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)

            //주변장소 마커 찍을 경우 검색한 장소를 기준으로 재측정 가능하도록 여기서 변수값 변경
            myLocation?.latitude = address.latitude
            myLocation?.longitude = address.longitude

            //지도에 연결
            googleMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            //여기서 키보드 내리기
            hideKeyboard()

            //마커 찍은 곳 장소 정보값 찍기
            //마커 클릭 리스너
            googleMap.setOnMarkerClickListener (object: GoogleMap.OnMarkerClickListener{

                override fun onMarkerClick(p0: Marker): Boolean {
                    //토스트 메시지 띄우기 -> snippet 은 null이고, Title, 좌표값은 가져올 수 있음
                    Toast.makeText(this@ServiceActivity, p0.title , Toast.LENGTH_SHORT).show()

                    //지도 데이터 객체에 담기

                    return false
                }
            })
        }
    }

    fun searchLocationByLatLng(latitude: Double, longitude: Double) {
        val MAX_RESULTS = 1
        val geoCoder = Geocoder(this)

        Log.d("test_doin", "searchLocationByLatLng latitude(${latitude}), longitude(${longitude}")

        try{
            var addressList : List<Address>? = null
            //addressList = geoCoder.getFromLocation(latitude, longitude, MAX_RESULTS)
            addressList = geoCoder.getFromLocationName("회기역", MAX_RESULTS)

            // MAX_RESULTS == 1 이니까 addressList 는 항상 길이가 1 이하
            val address = addressList[0]
            val latLng = LatLng(address.latitude, address.longitude)

            // 가져온 주소 기반으로 다시 myLocation 설정
            myLocation?.latitude = latLng.latitude
            myLocation?.longitude = latLng.longitude

            // 지도에 연결
            googleMap!!.addMarker(MarkerOptions().position(latLng).title("선택된 위치"))
            googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            hideKeyboard()
        }catch(e:IOException){
            e.printStackTrace()
        }
    }

    //키보드 내리기 메소드
    fun hideKeyboard() {
        // 키보드 내리기
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


}









