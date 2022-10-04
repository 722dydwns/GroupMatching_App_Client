package com.example.appgrouppurchasemaching.message

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityMyMsgBinding
import com.example.appgrouppurchasemaching.utils.FirebaseAuthUtils
import com.example.appgrouppurchasemaching.utils.FirebaseRef
import com.example.appgrouppurchasemaching.utils.MyInfo
import com.example.appgrouppurchasemaching.utils.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MyMsgActivity : AppCompatActivity() { //내 메시지 목록 액티비티

    lateinit var binding : ActivityMyMsgBinding

    lateinit var listViewAdapter: MsgAdapter
    val msgList= mutableListOf<MsgModel>()
    val senderNameList = mutableListOf<String>()
    val senderUidList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyMsgBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.title = "받은 매칭 목록 리스트"

        getMyMsg() //내 메시지 목록 불러오기
        getUserDataList() //호출

        //어댑터 연결하기
        val listview = findViewById<ListView>(R.id.msgListView)
        listViewAdapter = MsgAdapter(this,msgList )
        listview.adapter = listViewAdapter

        //받은 메시지 목록 클릭 시, -> 해당 User와의 메시지 채팅 창 이동
        listview.setOnItemClickListener { parent, view, position, id ->

            //화면 전환 처리리
           val intent = Intent(this, ChatActivity::class.java)

            //보낸 이의 이름
            var senderUserName = senderNameList[position]
            var senderUserUid = senderUidList[position] // 여기서 계속 에러가 남 왜지 ?

            intent.putExtra("name", senderUserName)
            intent.putExtra("uid", senderUserUid)

            startActivity(intent)
        }

    }

    //받은 메시지 데이터 불러오기
    private fun getMyMsg() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //중복 데이터 쌓임 방지용
                msgList.clear()
                senderNameList.clear()
                senderUidList.clear()

                for (dataModel in dataSnapshot.children) {
                    val msg = dataModel.getValue(MsgModel::class.java)
                    senderNameList.add(msg?.senderInfo.toString()) //여기서 대상 회원의 닉네임 받아주고  // Error 남 String 형 변환 안됨?
                    msgList.add(msg!!) //리스트에 데이터모델 담기

                }
                //최신순으로 데이터 보이게 리스트 '역순 정렬'
                msgList.reverse()
                senderNameList.reverse()
                senderUidList.reverse()

                listViewAdapter.notifyDataSetChanged() //받은 메시지 데이터 목록 데이터를 다시 리스트뷰에 그려주기

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)

    }

    //대상 사용자 정보 데이터 리스트
    private fun getUserDataList(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataModel in dataSnapshot.children) {

                    val user = dataModel.getValue(UserDataModel::class.java)
                    if(senderNameList.contains(user?.nickname)){
                        senderUidList.add(user?.uid.toString()) //보낸 사람 uid 값 담고
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

}