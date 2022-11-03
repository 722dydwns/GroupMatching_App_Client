package com.example.appgrouppurchasemaching.utils

import com.example.appgrouppurchasemaching.matching.Matching
import com.example.appgrouppurchasemaching.message.Message
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef { //RealDB 저장 규격 짜기
    companion object {
        val database = Firebase.database

        val userInfoRef = database.getReference("userInfo")//회원 정보는 이 경로로

        /**
         * Firebase Realtime database - "chats" reference
         */
        val chatsRef = database.getReference("chats")

        /**
         * Firebase Realtime database 에 message depth 를 하나 만들어주기 위한 pathString
         */
        val CHAT_MESSAGE = "messages"

        /**
         * db 에 message 저장하는 메소드.
         *
         * companion object 로 선언되어 있다.
         *
         * parameter 중 senderRoom 과 receiverRoom 은 서로 반대로 대입해도 무관하다.
         *
         * @param[senderRoom] 보내는 방 ID
         * @param[receiverRoom] 받는 방 ID
         * @param[msg] Message Object
         */
        fun pushMessage(senderRoom : String, receiverRoom : String, msg : Message) {
            chatsRef.child(senderRoom).child(CHAT_MESSAGE).push().setValue(msg)
                .addOnSuccessListener {
                    chatsRef.child(receiverRoom).child(CHAT_MESSAGE).push().setValue(msg)
                        .addOnSuccessListener {
                            // 필요시 성공 로그 추가
                        }.addOnFailureListener(OnFailureListener { /* 필요시 실패 로그 추가 */ })
                }.addOnFailureListener(OnFailureListener { /* 필요시 실패 로그 추가 */ })
        }


        /**
         * Firebase Realtime database - "userWantMatching" reference
         */
        val userWantMatchingRef = database.getReference("userWantMatching") //회원이 매칭 원하는 다른 User 정보는 이 경로로

        /**
         * 매칭할 두 유저 정보를 userWantMatching 에 넣는 메소드.
         *
         * companion object 로 선언되어 있다.
         *
         * @param[wanterUid] 매칭을 원하는 사람 UID
         * @param[wantedUid] 글 올린 사람 UID
         */
        fun addUserMatching(wanterUid : String, wantedUid : String) {
            val matchingData = Matching(wanterUid, wantedUid)
            userWantMatchingRef.child(wanterUid).child(wantedUid).setValue(matchingData)
        }

        /**
         * 매칭이 성공했다는 정보를 userWantMatching 에 넣어주는 메소드.
         * Matching 의 멤버 isMatching을 true 로 변경하여 set 해준다.
         *
         * companion object 로 선언되어 있다.
         *
         * @param[wanterUid] 매칭을 원하는 사람 UID
         * @param[wantedUid] 글 올린 사람 UID
         */
        fun setUserMatchingSuccess(wanterUid: String, wantedUid: String) {
            val matchingData = Matching(wanterUid, wantedUid)
            matchingData.setMatchSuccess()
            userWantMatchingRef.child(wanterUid).child(wantedUid).setValue(matchingData)
        }
    }
}