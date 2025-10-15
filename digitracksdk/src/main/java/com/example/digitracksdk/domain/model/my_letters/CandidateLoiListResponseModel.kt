package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class CandidateLoiListResponseModel(
    val InnovID:String? = "",
    val LOITrackingID:String? = "",
    val CandidateName:String? = "",
    val Message:String? = "",
    val status:String? = "",
    var LstLoi:ArrayList<ListLoiModel>? = ArrayList()
)
@Keep
data class ListLoiModel(
    var LOITrackingID:Int? = 0,
    var ClientName:String? = "",
    var DateofJoining:String? = "",
    var LOIStatus:String? = "",
)

