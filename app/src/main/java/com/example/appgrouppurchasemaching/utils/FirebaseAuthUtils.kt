package com.example.appgrouppurchasemaching.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class FirebaseAuthUtils { //회원 Uid get

    companion object {

        private lateinit var auth : FirebaseAuth

        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
    }

}