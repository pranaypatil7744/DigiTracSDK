package com.example.digitracksdk.presentation.my_letters.candidate_loi.model

import androidx.annotation.Keep

@Keep
data class CandidateLoiModel(
    var name:String? = "",
    var companyName:String? = "",
    var joiningDate:String? = "",
    var fileUrl:String? = "",
    var candidateLoiStatus: CandidateLoiStatus,
    var no:String? = null,
    val loiTrackingId:Int? =0
)

enum class CandidateLoiStatus(var value:Int){
    ACCEPTED(1),
    AWAITING(2),
    REJECTED(3),
}