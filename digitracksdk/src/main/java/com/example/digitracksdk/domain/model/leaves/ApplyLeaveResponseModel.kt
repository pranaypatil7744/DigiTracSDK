package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class ApplyLeaveResponseModel(
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class ApplyLeaveRequestModel(
    var Empcode:String? = "",
    var RequestTypeId:Int? = 0,
    var RegularizationDate:String? = "",
    var InTime:String? = "",
    var OutTime:String? = "",
    var Location:String? = "",
    var Remarks:String? = "",
    var ToDate:String? = "",
    var LeaveAppliedFor:String? = ""
)
