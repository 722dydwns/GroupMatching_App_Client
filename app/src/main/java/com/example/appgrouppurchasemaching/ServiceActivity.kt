package com.example.appgrouppurchasemaching

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.appgrouppurchasemaching.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() { //서비스 제공 액티비티
    //binding 설정
    lateinit var binding : ActivityServiceBinding

    //허용받을 권한 목록
    val permission_list = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 설정
        binding = ActivityServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //권한 요청하기 사용자에게
        requestPermissions(permission_list, 0)

    }
}