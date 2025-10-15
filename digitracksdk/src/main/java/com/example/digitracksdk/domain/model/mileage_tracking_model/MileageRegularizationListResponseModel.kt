package com.example.digitracksdk.domain.model.mileage_tracking_model

import androidx.annotation.Keep

@Keep
data class MileageRegularizationListResponseModel(
    var MileageRegularizationlst:ArrayList<MileageRegularizationListModel>? = ArrayList(),
    var Status:String? = "",
    var Message:String? = ""
)

@Keep
data class MileageRegularizationListModel(
    var SrNo:String? = "",
    var MileageRegularizationId:String? = "",
    var TravelDate:String? = "",
    var StartReading:String? = "",
    var EndReading:String? = "",
    var Remark:String? = "",
    var ApprovalStatus:String? = "",
    var ApproveBy:String? = "",
    var ApprovedDate:String? = "",
    var ApprovalRemark:String? = ""
)


@Keep
data class MileageRegularizationListRequestModel(
    var EmpCode:String? = ""
)