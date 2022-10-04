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

class MyLikeListActivity : AppCompatActivity() { //나의 좋아요 리스트 액티비티 화면

    //바인딩
    lateinit var binding : ActivityMyLikeListBinding
    //내 uid
    private val uid = FirebaseAuthUtils.getUid()
    //대상 uid
    lateinit var getterUid : String

    private val likeUserList = mutableListOf<UserDataModel>()
    private val likeUserListUid = mutableListOf<String>()

    lateinit var listviewAdapter : ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLikeListBinding.inflate(layoutInflater)

        setContentView(binding.root)
        getMyUserData() // 현재 로그인한 사용자 정보 닉네임 얻기

        binding.myLikeToolbar.title = "원하는 매칭 리스트"

        getMyLikeList() //내가 좋아요한 애들 목록 얻기

        //리스트뷰 Adapter연결
        val userListView = findViewById<ListView>(R.id.userListView)
        listviewAdapter = ListViewAdapter(this, likeUserList)
        userListView.adapter = listviewAdapter

        //내가 좋아요한 유저 롱클릭 시, 메시지 보내기 창 떠서 메시지 보낼 수 있게 하고
        //상대방에게 알림 띄워주고
        userListView.setOnItemLongClickListener { parent, view, position, id ->
            getterUid = likeUserList[position].uid.toString()
            showDialog() //메시지 보내기 Dialog 띄우기
            return@setOnItemLongClickListener(true)
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

    private fun getMyLikeList(){ //내가 좋아요한 애들 리스트 만 얻기
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    // 내가 좋아요 한 사람들의 uid가 likeUserList에 들어있음
                    likeUserListUid.add(dataModel.key.toString())
                }
                getUserDataList()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userWantMatchingRef.child(uid).addValueEventListener(postListener)

    }

    //사용자 정보 데이터 리스트
    private fun getUserDataList(){

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (dataModel in dataSnapshot.children) {

                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 전체 유저중에 내가 좋아요한 사람들의 정보만 add함
                    if(likeUserListUid.contains(user?.uid)) {

                        likeUserList.add(user!!)
                    }

                }
                listviewAdapter.notifyDataSetChanged() //다시 데이터 그려주기 리스트뷰에
                Log.d("test", likeUserList.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("test", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }


    //다이얼로그
    private fun showDialog() {
        //다이얼로그 창 레이아웃 뷰 가져오고
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("매칭 시도하기")

        val mAlertDialog = mBuilder.show()

        //보내기 버튼 클릭 이벤트 처리
        val btn = mAlertDialog.findViewById<Button>(R.id.sendBtnArea)

        //사용자 입력한 메시지 텍스트 받아오기
        val textArea = mAlertDialog.findViewById<EditText>(R.id.sendTextArea)

        btn?.setOnClickListener {

            //메시지 데이터 모델 정의
            val msgModel = MsgModel(MyInfo.myNickname, textArea!!.text.toString())

            FirebaseRef.userMsgRef.child(getterUid).push().setValue(msgModel)

            mAlertDialog.dismiss() //다이얼로그 종료
        }

        //FB RealTime DB에 message 경로
        // message 데이터
            // 받는 사람 uid
            // 메시지 내용
            // 누가 보냈는지

    }

}