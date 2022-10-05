package com.example.appgrouppurchasemaching.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.board.BoardMainActivity
import com.example.appgrouppurchasemaching.databinding.ActivityMyPageBinding
import com.example.appgrouppurchasemaching.utils.FirebaseAuthUtils
import com.example.appgrouppurchasemaching.utils.FirebaseRef
import com.example.appgrouppurchasemaching.utils.UserDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyPageActivity : AppCompatActivity() { //마이페이지 = 회원 개인 정보 페이지

    //바인딩
    lateinit var binding : ActivityMyPageBinding

    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMyData() //호출

        binding.toolbar.title =  " 마이페이지[회원정보]"

    }


    //DB에서 내 정보 가져와서 뷰에 바인딩처리
    private fun getMyData() {

        val myNickname = binding.userNickname
        val myId=binding.userId
        val myUid = binding.userUid
        val myPassword = binding.userPw

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                //화면 상에 세팅

                myNickname.text = "  " + data!!.nickname
                myId.text = "  " + data!!.userId
                myUid.text ="  " +  data!!.uid
                myPassword.text = "  " + data!!.userPw
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }
}