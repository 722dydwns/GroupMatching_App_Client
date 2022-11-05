package com.example.appgrouppurchasemaching.matching

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
    //멤버
    var user1Uid: String = ""
    var user2Uid: String = ""
    var isMatch: Boolean = false
    var boardIdx: Int? = null

    fun setMatchSuccess() {
        isMatch = true
    }

    @Override
    override fun toString() : String {
        return "{\"user1Uid\": ${user1Uid}, \"user2Uid\": ${user2Uid}, \"isMatch\": ${isMatch}, \"boardIdx\": ${boardIdx?.toString()}}"
    }
}