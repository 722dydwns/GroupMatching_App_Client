package com.example.appgrouppurchasemaching.message

import android.app.Service
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.databinding.ActivityChatBinding
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
                    finish()
                }
            }
            true
        }

        chatRecyclerView = binding.chatRecyclerView
        messageBox = binding.messageBox
        sendButton = binding.sendButton
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

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