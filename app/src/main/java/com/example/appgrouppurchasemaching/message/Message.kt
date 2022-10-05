package com.example.appgrouppurchasemaching.message

class Message {
    //메시지 내용, senderUid
    var message : String? = null
    var senderId : String? = null

    constructor(){}

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }
}