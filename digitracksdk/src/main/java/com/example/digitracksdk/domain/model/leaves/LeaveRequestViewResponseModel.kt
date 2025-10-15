package com.example.digitracksdk.domain.model.leaves

import androidx.annotation.Keep

@Keep
data class LeaveRequestViewResponseModel(
    var lstAppliedLeave:ArrayList<ListAppliedLeaveModel>?= ArrayList(),
    var status:String? = "",
    var Message:String? = ""
)

@Keep
data class ListAppliedLeaveModel (
    var AssociateName:String? = "",
    var GNETAssociateID:String? = "",
    var AssociateID:String? = "",
    var RegularizationDate:String? = "",
    var RegularizationType:String? = "",
    var Status:String? = "",
    var Remarks:String? = "",
    var ApprovedDate:String? = "",
    var ApprovedRemark:String? = ""
)

@Keep
data class LeaveRequestViewRequestModel(
    var InnovID:String? = "",
    var GNETAssociateID:String? = "",
    var FromDate:String? = "",
    var ToDate:String? = "",
)