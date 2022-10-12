package com.example.appgrouppurchasemaching.service

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng

/**
 * 약속 장소로 마커 찍는 애 담는 객체
 *
 * Intent 에 담아서 전달하기 위해 직렬화 인터페이스인 Parcelable 을 implement 했다.
 *
 * 구현이 더 간단한 Serializable 을 선택하지 않은 이유는,
 * member 로 갖고있는 LatLng 객체가 Parcelable 를 implement 했기 때문이다.
 *
 * @see [https://velog.io/@jaeyunn_15/Android-Parcelable-vs-Serializable]
 *
 */
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