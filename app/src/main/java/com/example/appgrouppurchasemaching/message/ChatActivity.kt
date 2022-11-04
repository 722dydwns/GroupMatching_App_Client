package com.example.appgrouppurchasemaching.message

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityChatBinding
import com.example.appgrouppurchasemaching.matching.MyLikeListActivity
import com.example.appgrouppurchasemaching.service.MapInfoModel
import com.example.appgrouppurchasemaching.service.ServiceActivity
import com.example.appgrouppurchasemaching.utils.FirebaseRef
import com.example.appgrouppurchasemaching.utils.FirebaseRef.Companion.CHAT_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import okhttp3.internal.notify

class ChatActivity : AppCompatActivity() { //'채팅' 액티비티 화면

    lateinit var binding : ActivityChatBinding

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendButton : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>

    var receiverRoom : String? = null
    var senderRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("test_doin", "ChatActivity.onCreate called")
        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val intent = getIntent()
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid") //채팅 상대방 정보
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid //현재 로그인 사용자

        //지도 아이콘 클릭 시 -> 여기서 지도 누를 때, 채팅방 정보 함께 넘겨줄 것
        binding.mapOption.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)

            intent.putExtra("OtherUid", receiverUid.toString())
            intent.putExtra("OtherName", name)

            startActivity(intent)
        }

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        binding.ChatToolbar.title = " " + name

        binding.ChatToolbar.inflateMenu(R.menu.back_back_menu)
        binding.ChatToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.back_btn ->{
                    //finish()
                    val intent = Intent(this, MyLikeListActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }
            true
        }

        chatRecyclerView = binding.chatRecyclerView
        messageBox = binding.messageBox
        sendButton = binding.sendButton
        messageList = ArrayList()
        messageAdapter = MessageAdapter(applicationContext, messageList)
        messageAdapter.setMessagebtnClickListner( //제안 장소 버튼 클릭 시
            View.OnClickListener {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("약속 장소 최종 선택")
                dialogBuilder.setMessage("이 주소로 약속 장소를 최종 선택하시겠습니까 ?")
                dialogBuilder.setPositiveButton("최종 매칭"){ dialogInterface: DialogInterface, i: Int ->
                    //이벤트 처리
                    /**
                     * DB 상에 상대 UID, 내 UID, 매칭 장소 주소값(아직 적용 x) 등의 데이터 올리는 코드.
                     *
                     * 아래 코드의 parameter를 잘 보면
                     * waterUid(매칭 원하는 사람) 에 receiverUid 를,
                     * wantedUid(글 올린 사람) 에 senderUid 를 대입했다.
                     *
                     * 그 이유는 최종 매칭 버튼은 "상대가 제안한 장소를 수락" 하는 시나리오만 있다고 가정하고 구현했기 때문이다.
                     * 내가 제안한 장소를 내가 수락하는 것은 좀 이상하다.
                     *
                     * ChatActivity namespace에서는 메세지나 송수신자를 구분할 수 없으므로,
                     * listener 는 단순하게 아래 기능만 구현하고
                     * MessageAdapter 에서 이 listener를 receiveBtn 에만 적용하는 방식으로 구현한다.
                     */
                    FirebaseRef.setUserMatchingSuccess(receiverUid!!, senderUid!!)

                    // TODO: 채팅에서 최종 매칭 누르고 나면, 게시글에서도 매칭이 됐는지 안됐는지 Flag가 떠야될거같음
                }
                dialogBuilder.setNegativeButton("취소"){ dialogInterface: DialogInterface, i: Int ->
                    //이벤트 처리 -> 창 닫기
                }
                dialogBuilder.setNeutralButton("잠깐만요"){ dialogInterface: DialogInterface, i: Int ->
                    //이벤트 처리
                  Toast.makeText(this, "지도에서 장소를 다시 선택해주세요 ", Toast.LENGTH_SHORT).show()
                }
                dialogBuilder.show()

            }

        )

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //리사이클러뷰에 세팅
        FirebaseRef.chatsRef.child(senderRoom!!).child(CHAT_MESSAGE)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear() //데이터 중복 방지
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged() //재세팅
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        // messageList에 지도 정보 넘어온거 있으면 메시지 하나 추가함
        // ChatActivity 에서 onCreate에서 한번 호출됨

        if (intent.hasExtra("mapInfo")) {

            val mapInfo = intent.getParcelableExtra("mapInfo") as MapInfoModel?

            if (mapInfo == null) {

            } else {
                val messageObject = Message(" ${mapInfo.marker_title} \n${mapInfo.marker_snippet}", senderUid, Message.Type.LOCATION)
                messageObject.setShareLocation(mapInfo.marker_position!!.latitude, mapInfo.marker_position!!.longitude)

                FirebaseRef.pushMessage(senderRoom!!, receiverRoom!!, messageObject)
            }
        }

        //보내기 버튼 클릭 시 이벤트 처리 = 메시지를 DB 에 담는 처리
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            if (message.isEmpty()) {
                return@setOnClickListener
            }

            val messageObject = Message(message, senderUid)
            FirebaseRef.pushMessage(senderRoom!!, receiverRoom!!, messageObject)
            messageBox.setText("")
        }
    }


}