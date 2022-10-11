package com.example.appgrouppurchasemaching.message

import android.app.AlertDialog
import android.app.Service
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityChatBinding
import com.example.appgrouppurchasemaching.matching.MyLikeListActivity
import com.example.appgrouppurchasemaching.service.MapInfoModel
import com.example.appgrouppurchasemaching.service.ServiceActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() { //'채팅' 액티비티 화면

    lateinit var binding : ActivityChatBinding

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendButton : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList : ArrayList<Message>
    private lateinit var mDbRef : DatabaseReference

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

        mDbRef = FirebaseDatabase.getInstance().getReference()

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
                dialogBuilder.setMessage("이 주소로 약속 정소를 최종 선택하시겠습니까 ?")
                dialogBuilder.setPositiveButton("최종 매칭"){ dialogInterface: DialogInterface, i: Int ->
                    //이벤트 처리
                }
                dialogBuilder.setNegativeButton("취소"){ dialogInterface: DialogInterface, i: Int ->
                    //이벤트 처리
                }
                dialogBuilder.setNeutralButton("잠깐만요"){ dialogInterface: DialogInterface, i: Int ->
                    //이벤트 처리
                }
                dialogBuilder.show()

            }

        )

        /**
        messageAdapter.setMessagebtnClickListner(
            View.OnClickListener {
                Log.d("share_location", "message btn click listener")

                val currentMessage = messageList[0]

                Log.d("share_location", "currentMessage: ${currentMessage}")

                val intent = Intent(this, ServiceActivity::class.java)
                intent.putExtra("latitude", currentMessage.latitude)
                intent.putExtra("longitude", currentMessage.longitude)
                intent.putExtra("isClickedByMessage", true)
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        )
        */
        //messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //리사이클러뷰에 세팅
        mDbRef.child("chats").child(senderRoom!!).child("messages")
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
        Log.d("share_location", "intent.hasExtra(\"mapInfo\"): ${intent.hasExtra("mapInfo")}")
        if (intent.hasExtra("mapInfo")) {
            Log.d("share_location", "intent 에 mapInfo 가 있음")

            val mapInfo = intent.getParcelableExtra("mapInfo") as MapInfoModel?
            Log.d("share_location", "mapInfo.marker_snippet: ${mapInfo?.marker_snippet}")
            Log.d("share_location", "intent 에서 mapInfo 받음")

            if (mapInfo == null) {
                Log.d("share_location", "mapInfo is null")
            } else {
                val messageObject = Message(" ${mapInfo.marker_title} \n${mapInfo.marker_snippet}", senderUid, Message.Type.LOCATION)
                messageObject.setShareLocation(mapInfo.marker_position!!.latitude, mapInfo.marker_position!!.longitude)
                Log.d("share_location", "messageObject.message: ${messageObject.message}, messageObject.type: ${messageObject.type}")

                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        Log.d("share_location", "senderRoom 에는 보내짐")
                        mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener {
                                Log.d("share_location", "receiverRoom 에도 보내짐")
                            }
                    }
            }
        }

        //보내기 버튼 클릭 시 이벤트 처리 = 메시지를 DB 에 담는 처리
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            if (message.isEmpty()) {
                return@setOnClickListener
            }

            val messageObject = Message(message, senderUid)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")

        }


    }


}