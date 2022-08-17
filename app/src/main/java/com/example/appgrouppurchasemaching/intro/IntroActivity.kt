package com.example.appgrouppurchasemaching.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgrouppurchasemaching.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() { //intro 액티비티 화면
    //바인딩 설정
    lateinit var binding : ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //바인딩 설정
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //시작하기 버튼 클릭 시 메인 액티비티로 이동
        binding.startbtn.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}