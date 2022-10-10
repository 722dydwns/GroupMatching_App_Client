package com.example.appgrouppurchasemaching.service

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

class MapInfoModel( // 약속장소로 마커 찍은 애 담는 객체

    //지도값
    val marker_position: LatLng?, //위,경도 객체
    val marker_title: String?,
    val marker_snippet: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(marker_position, flags)
        parcel.writeString(marker_title)
        parcel.writeString(marker_snippet)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MapInfoModel> {
        override fun createFromParcel(parcel: Parcel): MapInfoModel {
            return MapInfoModel(parcel)
        }

        override fun newArray(size: Int): Array<MapInfoModel?> {
            return arrayOfNulls(size)
        }
    }
}