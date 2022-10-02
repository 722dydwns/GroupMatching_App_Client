package com.example.appgrouppurchasemaching.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityUserMatchingBinding
import com.example.appgrouppurchasemaching.utils.FirebaseRef

class UserMatchingActivity : AppCompatActivity() { //사용자 약속잡기 매칭 관련 메인 액티비티

    //바인딩
    lateinit var binding : ActivityUserMatchingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMatchingBinding.inflate(layoutInflater)

        binding.promiseToolar.title = "약속잡기 매칭 화면"

        setContentView(binding.root)

        //매칭 리스트 목록 클릭 이벤트 처리
        binding.matchingList.setOnClickListener {
            val intent = Intent(this, MyLikeListActivity::class.java)
            startActivity(intent)
        }


    }

}