package com.example.appgrouppurchasemaching.message

import android.app.Service
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.appgrouppurchasemaching.R
import com.example.appgrouppurchasemaching.board.BoardMainActivity
import com.example.appgrouppurchasemaching.service.ServiceActivity
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context : Context, val messageList : ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE = 1;
    val ITEM_SENT = 2;
    val ITEM_RECEIVE_LOCATION = 3;
    val ITEM_SENT_LOCATION = 4;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {
            ITEM_SENT -> { // 보내는 메시지 뷰 타입
                val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
                return SentViewHolder(view)
            }
            ITEM_SENT_LOCATION -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.send_location, parent, false)
                return SentLocationViewHolder(view)
            }
            ITEM_RECEIVE -> { // 받는 메시지 뷰 타입
                val view: View = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
                return ReceiveViewHolder(view)
            }
            ITEM_RECEIVE_LOCATION -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.recieve_location, parent, false)
                return ReceiveLocationViewHolder(view)
            }
            else -> {
                // test code.
                val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
                return SentViewHolder(view)
            }
        }
    }


    private var messageBtnClickListener : View.OnClickListener? = null

    fun setMessagebtnClickListner(listener: View.OnClickListener) {
        messageBtnClickListener = listener
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        when (holder.javaClass) {
            SentViewHolder::class.java -> {
                val viewHolder = holder as SentViewHolder
                viewHolder.sentMessage.text = currentMessage.message
            }
            SentLocationViewHolder::class.java -> {
                val viewHolder = holder as SentLocationViewHolder
                viewHolder.sentBtn.text = currentMessage.message

                viewHolder.sentBtn.setOnClickListener {
                    /**
                     * 내가 보낸 장소 제안 버튼을 눌렀을 때의 동작
                     *
                     * 단순히 로그만 찍는다.
                     *
                     * @see ChatActivity.kt messageAdapter.setMessagebtnClickListner 리스너 등록하는 부분 설명
                     */
                    Log.d("test", "sentBtn Clicked")
                }

            }
            ReceiveViewHolder::class.java -> {
                val viewHolder = holder as ReceiveViewHolder
                viewHolder.recieveMessage.text = currentMessage.message
            }
            ReceiveLocationViewHolder::class.java -> {
                val viewHolder = holder as ReceiveLocationViewHolder
                viewHolder.receiveBtn.text = currentMessage.message

                viewHolder.receiveBtn.setOnClickListener {
                    Log.d("test", "receiveBtn Clicked")
                    messageBtnClickListener?.onClick(viewHolder.itemView)
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            if(currentMessage.type == Message.Type.STRING) {
                return ITEM_SENT
            } else {
                return ITEM_SENT_LOCATION
            }
        }else{
            if(currentMessage.type == Message.Type.STRING) {
                return ITEM_RECEIVE
            } else {
                return ITEM_RECEIVE_LOCATION
            }
        }
    }


    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val sentMessage = itemView.findViewById<TextView>(R.id.txt_send_message)
    }

    class SentLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentBtn = itemView.findViewById<Button>(R.id.btn_send_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.txt_recieve_message)
    }

    class ReceiveLocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveBtn = itemView.findViewById<Button>(R.id.btn_recieve_message)
    }

}