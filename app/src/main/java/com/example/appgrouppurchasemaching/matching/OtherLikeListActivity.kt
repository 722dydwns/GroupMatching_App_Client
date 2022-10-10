package com.example.appgrouppurchasemaching.matching

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.board.BoardMainActivity
import com.example.appgrouppurchasemaching.databinding.ActivityOtherLikeListBinding
import com.example.appgrouppurchasemaching.intro.MainActivity
import com.example.appgrouppurchasemaching.message.ChatActivity
import com.example.appgrouppurchasemaching.utils.FirebaseAuthUtils
import com.example.appgrouppurchasemaching.utils.FirebaseRef
import com.example.appgrouppurchasemaching.utils.UserDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class OtherLikeListActivity : AppCompatActivity() { //'나를' 원하는 매칭 대상들을 뽑아오기

    //바인딩
    lateinit var binding : ActivityOtherLikeListBinding

    //내 uid
    private val uid = FirebaseAuthUtils.getUid()

    //나를 좋아요한 대상들 정보 데이터 모델
    private val OtherLikeMeList = mutableListOf<UserDataModel>()

    private val OtherLikeList = mutableListOf<String>()

    lateinit var listviewAdapter : ListViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherLikeListBinding.inflate(layoutInflater)

        binding.myLikeToolbar.title = " 나를 원하는 대상 리스트 "

        setContentView(binding.root)

        getOtherUserLikeList()

        //리스트뷰 Adapter연결
        val otherLikeListView = findViewById<ListView>(R.id.userListView)
        listviewAdapter = ListViewAdapter(this, OtherLikeMeList)
        otherLikeListView.adapter = listviewAdapter

        //나와 매칭을 원하는 대상의 유저 단순 클릭 시 -> 채팅 가능
        otherLikeListView.setOnItemClickListener { parent, view, position, id ->
            //화면 전환 처리 -> 바로 채팅 액티비티로 이동
            val intent = Intent(this, ChatActivity::class.java)

            //대상 이름, Uid 필요
            var OtherUserName = OtherLikeMeList[position].nickname
            var OtherUserUid = OtherLikeMeList[position].uid

            intent.putExtra("name", OtherUserName)
            intent.putExtra("uid",OtherUserUid)

            startActivity(intent)
        }

        //뒤로가기 처리 = BackBtn
        binding.myLikeToolbar.inflateMenu(R.menu.back_back_menu)
        binding.myLikeToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.back_btn -> {
                    val intent = Intent(this, BoardMainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            true
        }

        //'내가 좋아요하는 리스트' 버튼
        binding.MyLikeBtn.setOnClickListener {
            val intent = Intent(this, MyLikeListActivity::class.java)
            startActivity(intent)
        }

        //'나를 좋아하고 있는 리스트'버튼
        binding.OtherLikeMeBtn.setOnClickListener {
            val intent = Intent(this, OtherLikeListActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    //사용자 '정보 데이터' 리스트
    private fun getUserDataList(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                OtherLikeMeList.clear()

                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)

                    if(OtherLikeList.contains(user?.uid) && user?.uid != uid ) {
                        OtherLikeMeList.add(user!!)
                    }
                    OtherLikeMeList.reverse()
                }
                listviewAdapter.notifyDataSetChanged() //다시 데이터 그려주기 리스트뷰에

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }


    // 대상의 '좋아요 리스트'를 다시 가져오기
    private fun getOtherUserLikeList() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //대상의 좋아요 리스트 속에 내 Uid 가 존재하는 지 확인 -> 있다면 매칭
                for (dataModel in dataSnapshot.children) {
                   OtherLikeList.add(dataModel.key.toString())
                }
                getUserDataList() //호출
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }

        //대상의 uid가 '나를' 좋아하고 있을때의 대상 목록을 가져올 거임.child
        FirebaseRef.userWantMatchingRef.addValueEventListener(postListener)
    }

}