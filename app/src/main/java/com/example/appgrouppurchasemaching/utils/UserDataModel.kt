package com.example.appgrouppurchasemaching.utils

class UserDataModel (

    val uid : String? = null,
    val nickname : String? = null,
    val userId : String? = null,
    val userPw : String? = null,

    // Flag for matching status.
    // Used in MyLikeListActivity, OtherLikeListActivity
    var isMatch : Boolean? = null
)