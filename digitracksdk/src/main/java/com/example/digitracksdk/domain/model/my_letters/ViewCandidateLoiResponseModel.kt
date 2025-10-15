package com.example.digitracksdk.domain.model.my_letters

import androidx.annotation.Keep

@Keep
data class ViewCandidateLoiResponseModel(
    var status:String? = "",
    var Message:String? = "",
    var LOIImage:String? = "",
)
@Keep
data class ViewCandidateLoiRequestModel(
    var LOITrackingId:Int = 0,
    var InnovId:String = "",
)
