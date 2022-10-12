package com.example.appgrouppurchasemaching.matching

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
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
import com.google.firebase.database.ktx.getValue

class OtherLikeListActivity : AppCompatActivity() { //'나를' 원하는 매칭 대상들을 뽑아오기

    //바인딩
    lateinit var binding : ActivityOtherLikeListBinding

    //내 uid
    private val uid = FirebaseAuthUtils.getUid()

    //나를 좋아요한 대상들 정보 데이터 모델
    private val OtherLikeMeList = mutableListOf<UserDataModel>()
//    private val OtherLikeList = mutableListOf<String>()
    private val OtherLikeMatchingMap = mutableMapOf<String, FirebaseRef.Matching>()

    lateinit var listviewAdapter : ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtherLikeListBinding.inflate(layoutInflater)

        binding.myLikeToolbar.title = " 나를 원하는 대상 리스트 "

        setContentView(binding.root)

        getOtherUserLikeList()

        //리스트뷰 Adapter연결
        val otherLikeListView = binding.userListView   //findViewById<ListView>(R.id.userListView)
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

//                    if(OtherLikeList.contains(user?.uid) && user?.uid != uid ) {
//                        OtherLikeMeList.add(user!!)
//                    }
                    if (OtherLikeMatchingMap.containsKey(user?.uid) && user?.uid != uid) {
                        user!!.isMatch = OtherLikeMatchingMap[user?.uid]!!.isMatch
                        OtherLikeMeList.add(user!!)
                    }
//                    OtherLikeMeList.reverse()
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
                    /**
                     * 현재 dataModel의 depth를 잘 알고 있어야 한다. realtime database에서
                     * snapshot depth를 헷갈려서 여태 삽질하고있었다.
                     * 현재 함수 아래에서 addValueEventListener 를 통해 지금 이 리스너(postListener)를 등록하는 코드를 보면,
                     * userWantMatchingRef 를 root로 모든 데이터를 가져오고 있다.
                     * userWantMatchingRef 의 트리구조는
                     * root(userWantMatching) - {매칭 원하는 사람 UID} - {글 올린 사람 UID} - {데이터 구조체(FirebaseRef.Matching)} 이다.
                     *
                     * 현재 콜백(onDataChange) 에서 parameter로 넘어오는 DataSnapshot 은
                     * 위에 적어둔 트리구조를 통째로 가지고 있다.
                     * 즉, dataSnapshot.key 를 로그찍어보면 아마 "userWantMaching" 이 찍힐것이다. (이걸 몰랐다)
                     *
                     * 따라서 dataSnapshot.children 객체들 (dataModel) 은
                     * {매칭 원하는 사람 UID} - {글 올린 사람 UID} - {데이터 구조체(FirebaseRef.Matching)}
                     * 형태를 갖고 있고, 여기서 key 는 {매칭 원하는 사람 UID} 가 된다.
                     *
                     * 기존에 내가 실수하던 부분은 dataModel.child(key) 에서 key 값으로 {매칭 원하는 사람 UID}를 쓰고 있던 점이다.
                     * 이미 dataModel 의 value 부분은 {글 올린 사람 UID} - {데이터 구조체(FirebaseRef.Matching)} 형태이기 때문에,
                     * 엉뚱한 UID로 child 검색 후 getValue() 하려고 하니 자꾸 null 이 들어왔던 것.
                     *
                     * dataModel.child(key) 에서 key 값으로 {글 올린 사람 UID}, 즉, 내 UID 를 사용하여
                     * 나와 매칭하고 싶은 이력이 있는지 확인하는 식으로 변경하니 문제없이 동작한다.
                     *
                     * 변경된 코드는 아래와 같다.
                     *
                     * 참고로, 여기서 사용중인 변수 uid 는 "내 UID" 이다.
                     */
                    val otherUid = dataModel.key.toString() // 상대방 uid
                    if (dataModel.hasChild(uid)) { // 현재 스냅샷에 내 UID 를 들고있는 value가 있는지 확인
                        val matchingData = dataModel.child(uid).getValue(FirebaseRef.Matching::class.java)
                        OtherLikeMatchingMap.put(otherUid, matchingData!!)
                    }
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