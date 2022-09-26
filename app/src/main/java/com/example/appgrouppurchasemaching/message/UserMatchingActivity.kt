package com.example.appgrouppurchasemaching.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityUserMatchingBinding

class UserMatchingActivity : AppCompatActivity() { //사용자 약속잡기 매칭 관련 메인 액티비티

    //바인딩
    lateinit var binding : ActivityUserMatchingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMatchingBinding.inflate(layoutInflater)

        binding.promiseToolar.title = "약속잡기 매칭 화면"

        setContentView(binding.root)


    }
}