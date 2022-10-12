package com.example.appgrouppurchasemaching.utils

import com.example.appgrouppurchasemaching.message.Message
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef { //RealDB 저장 규격 짜기
    /**
     * 매칭 정보를 갖고있는 class.
     * data class 용도로 사용하지만,
     * firebase에 저장하기 위해 parameter 로 전달할 때 애로사항이 있어서 일반 class로 선언했다.
     *
     * 매칭 목록에 노출해야 할 정보가 더 필요하다면 이 class의 member 로 선언하여 사용하면 된다. (ex. address)
     *
     * @property[user1Uid] 매칭할/된 사람 1
     * @property[user2Uid] 매칭할/된 사람 2
     * @property[isMatch] 매칭 성공 여부. 기본값 false
     */
    class Matching() {
        constructor(user1Uid: String, user2Uid: String) : this() {
            this.user1Uid = user1Uid
            this.user2Uid = user2Uid
        }
        var user1Uid: String = ""
        var user2Uid: String = ""
        var isMatch: Boolean = false

        fun setMatchSuccess() {
            isMatch = true
        }

        @Override
        override fun toString() : String {
            return "{\"user1Uid\": ${user1Uid}, \"user2Uid\": ${user2Uid}, \"isMatch\": ${isMatch}}"
        }
    }

    companion object {
        val database = Firebase.database

        val userInfoRef = database.getReference("userInfo")//회원 정보는 이 경로로

//        val userMsgRef = database.getReference("userMsg") // 메시지 내용 데이터는 이 경로로

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