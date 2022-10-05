package com.example.appgrouppurchasemaching.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.board.BoardMainActivity
import com.example.appgrouppurchasemaching.databinding.ActivityMyLikeListBinding
import com.example.appgrouppurchasemaching.utils.FirebaseAuthUtils
import com.example.appgrouppurchasemaching.utils.FirebaseRef
import com.example.appgrouppurchasemaching.utils.MyInfo
import com.example.appgrouppurchasemaching.utils.UserDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyLikeListActivity : AppCompatActivity() { //'내가' 원하는 매칭 대상 리스트 액티비티 화면

    //바인딩
    lateinit var binding : ActivityMyLikeListBinding
    //내 uid
    private val uid = FirebaseAuthUtils.getUid()

    //내가 좋아요한 대상의 정보 데이터 모델
    private val likeUserList = mutableListOf<UserDataModel>()
    private val likeUserListUid = mutableListOf<String>() //Uid

    lateinit var listviewAdapter : ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLikeListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        getMyUserData() // 현재 로그인한 사용자 정보 닉네임 얻기

        binding.myLikeToolbar.title = " 내가 원하는 매칭 리스트 "

        getMyLikeList() //내가 좋아요한 애들 목록 얻기

        //리스트뷰 Adapter연결
        val userListView = findViewById<ListView>(R.id.userListView)
        listviewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listviewAdapter

        //내가 좋아요한 유저 롱클릭 시, 메시지 보내기 창 떠서 메시지 보낼 수 있게 하고
        //상대방에게 알림 띄워주고
        userListView.setOnItemClickListener { parent, view, position, id ->
            //화면 전환 처리 -> 바로 채팅 액티비티로 이동
            val intent = Intent(this, ChatActivity::class.java)

            //대상 이름, Uid 필요
            var OtherUserName = likeUserList[position].nickname
            var OtherUserUid = likeUserList[position].uid

            intent.putExtra("name", OtherUserName)
            intent.putExtra("uid",OtherUserUid)

            startActivity(intent)

        }

        //'내가 좋아요하는 리스트' 버튼
        binding.MyLikeBtn.setOnClickListener {

        }

        //'나를 좋아하고 있는 리스트'버튼
        binding.OtherLikeMeBtn.setOnClickListener {
            val intent = Intent(this, OtherLikeListActivity::class.java)
            startActivity(intent)
        }

    }
    //내 정보 받아오기
    private fun getMyUserData(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val data = dataSnapshot.getValue(UserDataModel::class.java)

                MyInfo.myNickname = data?.nickname.toString() // 내 로그인 Info 정보는 여기에서 저장
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    //내가 좋아요한 애들 리스트 얻기
    private fun getMyLikeList(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    // 내가 좋아요 한 사람들의 uid가 likeUserList에 들어있음
                    likeUserListUid.add(dataModel.key.toString())
                }
                getUserDataList() //사용자 데이터를 UserDataModel 타입으로 얻기
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userWantMatchingRef.child(uid).addValueEventListener(postListener)

    }

    //사용자 '정보 데이터' 리스트
    private fun getUserDataList(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                likeUserList.clear() // 한번 정리해주고

                for (dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 전체 유저중에 내가 좋아요한 사람들 '정보' 이면서 나 자신은 아닌 대상만 add함
                    if(likeUserListUid.contains(user?.uid) && user?.uid != uid) {
                        likeUserList.add(user!!)
                    }
                    likeUserList.reverse() //역순
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


}