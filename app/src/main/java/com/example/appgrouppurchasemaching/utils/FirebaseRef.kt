package com.example.appgrouppurchasemaching.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef { //RealDB 저장 규격 짜기

    companion object {

        val database = Firebase.database

        val userInfoRef = database.getReference("userInfo")//회원 정보는 이 경로로

        val userWantMatchingRef = database.getReference("userWantMatching") //회원이 매칭 원하는 다른 User 정보는 이 경로로

    }
}