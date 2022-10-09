package com.example.appgrouppurchasemaching.service

import com.google.android.gms.maps.model.LatLng

class MapInfoModel ( // 약속장소로 마커 찍은 애 담는 객체

    //지도값
    var marker_position : LatLng? = null, //위,경도 객체
    var marker_title : String? = null,
    var marker_snippet : String? = null
        )