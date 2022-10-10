package com.example.appgrouppurchasemaching.message

class Message {
    enum class Type {
        STRING,
        LOCATION
    }
    //메시지 내용, senderUid
    var type : Message.Type? = null
    var message : String? = null
    var senderId : String? = null

    var latitude : Double? = null
    var longitude : Double? = null

    constructor(){}

    constructor(message: String?, senderId: String?, type: Message.Type = Type.STRING) {
        this.type = type
        this.message = message
        this.senderId = senderId
    }

    fun setShareLocation(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }

}